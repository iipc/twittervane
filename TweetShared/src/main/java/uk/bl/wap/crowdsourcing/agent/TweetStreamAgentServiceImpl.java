package uk.bl.wap.crowdsourcing.agent;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;

import twitter4j.FilterQuery;
import twitter4j.Status;
import twitter4j.StatusDeletionNotice;
import twitter4j.StatusListener;
import twitter4j.TwitterStreamFactory;
import twitter4j.URLEntity;
import twitter4j.conf.ConfigurationBuilder;
import twitter4j.json.DataObjectFactory;
import uk.bl.wap.crowdsourcing.AppConfig;
import uk.bl.wap.crowdsourcing.SearchTerm;
import uk.bl.wap.crowdsourcing.Tweet;
import uk.bl.wap.crowdsourcing.UrlEntity;
import uk.bl.wap.crowdsourcing.Util;
import uk.bl.wap.crowdsourcing.WebCollection;
import uk.bl.wap.crowdsourcing.dao.AppConfigDao;
import uk.bl.wap.crowdsourcing.dao.SearchTermDao;
import uk.bl.wap.crowdsourcing.dao.TweetDao;
import uk.bl.wap.crowdsourcing.dao.WebCollectionDao;
import uk.bl.wap.crowdsourcing.logger.JsonLogger;

public class TweetStreamAgentServiceImpl implements TweetStreamAgentService {
 
	@Autowired
	private TweetAnalyserService tweetAnalyserService;
	 
	/** the application logger **/
	private Log log;
	private JsonLogger jsonLogger = null;
	
	private Integer tweetCounter = 0;
	private Integer analysisTriggerValue = 10;
	
	int processCounter = 0;

	@Autowired
	private SearchTermDao searchTermDao; 
	
	private WebCollectionDao webCollectionDao;
	
	@Autowired
	private AppConfigDao appConfigDao;
	
	@Autowired
	private TweetDao tweetDao;  
	
	private AppConfig appConfig = null;
	private boolean termsFound = false;
	private Integer displayLastStreamErrors = 3;
	private List<String> lastStreamErrors = new ArrayList<String>(); 
	
	private TweetStreamAgentStatusEnum status = TweetStreamAgentStatusEnum.SHUTDOWN;
	
	public TweetStreamAgentServiceImpl() {
		log = LogFactory.getLog(getClass());
		status = TweetStreamAgentStatusEnum.SHUTDOWN;
	}
	
	@Override
	public final void start() {
		log.debug("TweetStreamAgentServiceImpl.start");
		if (!appConfigDao.configExists()) {
			AppConfig ac = new AppConfig();
			ac.setAccessToken("");
			ac.setAccessTokenSecret("");
			ac.setBitlyApiKey("");
			ac.setBitlyLogin("");
			ac.setConsumerKey("");
			ac.setConsumerSecret("");
			ac.setHttpProxyHost("");
			ac.setHttpProxyPort("");
			ac.setHttpProxyUser("");
			ac.setHttpProxyPass("");
			appConfigDao.persist(ac);
		}
		try {
			
			try {
				
				String[] trackArray = {getSearchTerms()};
				appConfig = appConfigDao.getAppConfig();
			       
			    if (checkAuthReady() && termsFound ) {
			    	 initStream(trackArray);
			    	 status = TweetStreamAgentStatusEnum.RUNNING;
			    	 log.info("Twitter Stream started");
			     } else if (!termsFound) {
			    	 log.warn("Twitter Stream start aborted (no search terms defined or all search terms have expired)");
			    	 setLastStreamError("Twitter Stream start aborted (no search terms defined)");
			     }
				
			} catch (Exception e) {
				log.error("Error initializing twitter stream: " + e.getMessage());
				setLastStreamError(e.getMessage());
			}


		} catch (Exception e) {
			log.error("Error initializing twitter stream: " + e.getMessage());
			setLastStreamError(e.getMessage());
		}
	}

	@Override
	public final void stop() {
		log.debug("TweetStreamAgentServiceImpl.stop");
		try {
			if (Util.twitterStream != null) {
				Util.twitterStream.shutdown();
				Util.twitterStream.cleanUp();
				Util.twitterStream = null;
				status = TweetStreamAgentStatusEnum.SHUTDOWN;
				log.info("Twitter Stream stopped");
			}
		} catch (Exception e) {
			// log the exception if its not the inflater closed error (this is thrown when the stream is stopped)
			if (e.getMessage() != null && !e.getMessage().equals("Inflater has been closed")) {
				log.error("Error closing twitter stream: " + e.getMessage());
				setLastStreamError(e.getMessage());
			} else if (e.getMessage().equals("Inflater has been closed")) {
				log.info("Twitter Stream stopped");
			}
		}
	}
	
	private void initStream(String[] trackArray) {
		
		log.debug("TweetStreamAgentServiceImpl.initStream");
		
		long[] followArray = null;
		ConfigurationBuilder builder = new ConfigurationBuilder();
    	
		builder.setJSONStoreEnabled(true);
		
		builder.setDebugEnabled(true)
  	  	.setOAuthConsumerKey(appConfig.getConsumerKey())
  	  	.setOAuthConsumerSecret(appConfig.getConsumerSecret())
  	  	.setOAuthAccessToken(appConfig.getAccessToken())
  	  	.setOAuthAccessTokenSecret(appConfig.getAccessTokenSecret())
  	  	;
  	  	
		if (appConfig.getHttpProxyHost() != null && !appConfig.getHttpProxyHost().isEmpty()) {
				builder.setHttpProxyHost(appConfig.getHttpProxyHost());
				if (appConfig.getHttpProxyPort()!= null && !appConfig.getHttpProxyPort().isEmpty()) {
					builder.setHttpProxyPort((int) Integer.parseInt(appConfig.getHttpProxyPort()));
				}
				if (appConfig.getHttpProxyUser() != null && !appConfig.getHttpProxyUser().isEmpty()) {
					builder.setHttpProxyUser(appConfig.getHttpProxyUser());
				}
				if (appConfig.getHttpProxyPass() != null && !appConfig.getHttpProxyPass().isEmpty()) {
					builder.setHttpProxyPassword(appConfig.getHttpProxyPass());
				}
		}
		
        StatusListener listener = new StatusListener() {
        	
            public void onStatus(Status streamStatus) {
            	
            	if (Util.twitterStream != null) {
            	
	            	URLEntity[] links = streamStatus.getURLEntities();
	            	
	            	if (links != null && links.length > 0) {
	            		List<UrlEntity> urlEntities = new ArrayList<UrlEntity>();
	            		try {
	            			Tweet tweet = new Tweet(streamStatus.getUser().getName(),
	            					streamStatus.getText(),
	            					streamStatus.getRetweetCount(),
	            					streamStatus.getId());
	            			
	            			tweet.setUrlEntities(urlEntities);
	            			for( URLEntity link : links ) {
	   							UrlEntity url = new UrlEntity();
	   						    if(link.getExpandedURL() != null) {
	    							url.setUrlOriginal(link.getExpandedURL().toString());
	            				} else if (link.getURL() != null) {
	            					url.setUrlOriginal(link.getURL().toString());
	            				}
	   						    url.setTweet(tweet);
	   						    url.setPopularity(tweet.getRetweetCount());
								tweet.getUrlEntities().add(url);
	            			}
	            			log.debug("Persisting tweetId: " + tweet.getTweetiD());
	            			String rawJson = DataObjectFactory.getRawJSON(streamStatus);
	            			if (rawJson != null) {
	            				jsonLogger.log(rawJson);
	            			} else {
	            				log.warn("Json logging is disabled");
	            			}
	            			tweetDao.persist(tweet);
	            			
	            			tweetCounter++;
	            			
	            			if (tweetAnalyserService != null && tweetCounter >= analysisTriggerValue) {
	            				log.debug("Triggering analysis after receiving " + tweetCounter + " tweets");
	            				tweetCounter = 0;
	            				tweetAnalyserService.setJobNumber(tweetDao.getTotalUnprocessed().intValue());
	            				status = TweetStreamAgentStatusEnum.WAITING;
	            				tweetAnalyserService.run();
	            				// stop and start the tweet stream so the search terms are reloaded
	            				stop();
	            				start();
	             			}
	            			
	            		} catch (Exception e) {
	            			log.error("Transaction error on tweet" + e.getMessage() + ", cause: " + e.getCause());
	            			log.debug(e);
	            			setLastStreamError(e.getMessage());
	            		} finally {
	
	            		}
	            	}
            	}
            }
            public void onDeletionNotice(StatusDeletionNotice statusDeletionNotice) {}
            public void onTrackLimitationNotice(int numberOfLimitedStatuses) {}
            public void onScrubGeo(long userId, long upToStatusId) {}
            public void onException(Exception ex) {
    			// log the exception if its not the inflater closed error (this is thrown when the stream is stopped)
    			if (ex.getMessage() != null && !ex.getMessage().equals("Inflater has been closed")) {
	            	log.error("Error retrieving tweet: " + ex.getMessage());
	            	if (ex.getCause() != null)
	            		log.error("cause: " + ex.getCause());
	            	setLastStreamError(ex.getMessage());
    			}
               }
        };
        
        Util.twitterStream = new TwitterStreamFactory(builder.build()).getInstance();
        Util.twitterStream.addListener(listener);

        if (trackArray != null) {
        	Util.twitterStream.filter(new FilterQuery(0, followArray, trackArray));
        }
	}
	
	/**
	 * @param lastStreamError the lastStreamError to set
	 */
	private void setLastStreamError(String lastStreamError) {
		while (lastStreamErrors.size() >= displayLastStreamErrors) {
			lastStreamErrors.remove(lastStreamErrors.size() - 1);
		}
		lastStreamErrors.add(0, getFormattedDate() + " - " + lastStreamError);
	}
	
	/**
	 * Return the current date and time formatted for display
	 * @return current date in yyyy-MM-dd HH:mm:ss format
	 */
	private String getFormattedDate() {
		Calendar calendar = new GregorianCalendar();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String formattedDate = sdf.format(calendar.getTime());
		return formattedDate;
	}

	@Override
	public AppConfig getAppConfig() {
		return appConfigDao.getAppConfig();
	}

	/**
	 * @return the analysisTriggerValue
	 */
	@Override
	public Integer getAnalysisTriggerValue() {
		return analysisTriggerValue;
	}

	/**
	 * @param analysisTriggerValue the analysisTriggerValue to set
	 */
	@Override
	public void setAnalysisTriggerValue(Integer analysisTriggerValue) {
		this.analysisTriggerValue = analysisTriggerValue;
	}

	/**
	 * @return the lastStreamError
	 */
	@Override
	public List<String> getLastStreamErrors() {
		return lastStreamErrors;
	}
	
	/**
	 * @param lastStreamErrors the lastStreamErrors to set
	 */
	@Override
	public void setLastStreamErrors(List<String> lastStreamErrors) {
		this.lastStreamErrors = lastStreamErrors;
	}
	
	/**
	 * @return the displayLastStreamErrors
	 */
	@Override
	public Integer getDisplayLastStreamErrors() {
		return displayLastStreamErrors;
	}

	/**
	 * @param displayLastStreamErrors the displayLastStreamErrors to set
	 */
	@Override
	public void setDisplayLastStreamErrors(Integer displayLastStreamErrors) {
		this.displayLastStreamErrors = displayLastStreamErrors;
	}

	@Override
	public List<SearchTerm> getAllSearchTerms() {
		return searchTermDao.getAllSearchTerms();
	}
	
	/**
	 * @param jsonLogger the jsonLogger to set
	 */
	public void setJsonLogger(JsonLogger jsonLogger) {
		this.jsonLogger = jsonLogger;
	}

	/**
	 * @param tweetAnalyserService the tweetAnalyserService to set
	 */
	public void setTweetAnalyserService(
			TweetAnalyserService tweetAnalyserService) {
		this.tweetAnalyserService = tweetAnalyserService;
	}

	private boolean checkAuthReady() {
		boolean result = false;
		if (appConfig.getConsumerKey() != null && !appConfig.getConsumerKey().isEmpty() && appConfig.getConsumerSecret() != null && !appConfig.getConsumerSecret().isEmpty()
				&& appConfig.getAccessToken() != null && !appConfig.getAccessToken().isEmpty()
				&& appConfig.getAccessTokenSecret() != null && !appConfig.getAccessTokenSecret().isEmpty()) {
			result = true;
		}
		return result;
	}
	
	
	private final String getSearchTerms() {
		String trackList = "";
		
		for (WebCollection webCollection: webCollectionDao.getAllCollectionsForStream()) {
			for (SearchTerm searchTerm : webCollection.getSearchTerms()) {
				trackList += searchTerm.getTerm() + ",";
				termsFound = true;
			}
		}
		
		if (trackList.length() > 0) {
			trackList = trackList.substring(0, trackList.lastIndexOf(","));
		}
		
		return trackList;
	}

	/**
	 * @return the webCollectionDao
	 */
	public WebCollectionDao getWebCollectionDao() {
		return webCollectionDao;
	}

	/**
	 * @param webCollectionDao the webCollectionDao to set
	 */
	public void setWebCollectionDao(WebCollectionDao webCollectionDao) {
		this.webCollectionDao = webCollectionDao;
	}

	/**
	 * @return the status
	 */
	public TweetStreamAgentStatusEnum getStatus() {
		return status;
	}

}
