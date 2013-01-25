package uk.bl.wap.crowdsourcing.controller;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

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
import uk.bl.wap.crowdsourcing.agent.TweetAnalyserService;
import uk.bl.wap.crowdsourcing.dao.AppConfigDao;
import uk.bl.wap.crowdsourcing.dao.SearchTermDao;
import uk.bl.wap.crowdsourcing.dao.TweetDao;
import uk.bl.wap.crowdsourcing.dao.WebCollectionDao;
import uk.bl.wap.crowdsourcing.logger.JsonLogger;

@Controller
public class TwitterStreamDaemonController {

	private Integer tweetCounter = 0;
	private Integer analysisTriggerValue = 10;
	
	int processCounter = 0;
	ModelAndView mv = new ModelAndView();
	
	private TweetAnalyserService tweetAnalyserService;
	
	@Autowired
	private SearchTermDao searchTermDao; 
	
	@Autowired
	private WebCollectionDao webCollectionDao;
	
	@Autowired
	private AppConfigDao appConfigDao;
	
	@Autowired
	private TweetDao tweetDao;  
	
	private AppConfig appConfig = null;
	private boolean termsFound = false;
	
	/** the application logger **/
	private final Log log;
	
	/** the json logger **/
	private JsonLogger jsonLogger;
	
	public TwitterStreamDaemonController() {
		log = LogFactory.getLog(getClass());
	}
	
	@RequestMapping(value="/twitterstream")
    public ModelAndView crowdsourcing(HttpServletRequest request) {

		Map<String, String> message = new HashMap<String, String>();
		String action = null;
		
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
		
		if (request.getParameter("action") != null) {
        	action = request.getParameter("action");
        }
		
		if (action != null && !action.isEmpty()) {
			if (action.contentEquals("start")) {
				if (Util.twitterStream == null) {
					this.run();
				}
			} else if (action.contentEquals("stop")) {
				if (Util.twitterStream != null) {
					this.stop();
				}
			}
		}
	
		if (Util.twitterStream != null) {
			message.put("message", "<span style=\"color:green;\">On</span>");
		} else {
			message.put("message", "<span style=\"color:red;\">Off</span>");
		}
		
	
        mv.addObject("message",message);
		mv.addObject("appConfig",appConfigDao.getAppConfig());
		mv.addObject("searchTermDao",searchTermDao);
		mv.addObject("analysisTriggerValue", analysisTriggerValue);
        mv.setViewName("twitterstream.jsp");
        return mv;

    }
	
	public final void run() {
		try {
			
			try {
				
				String[] trackArray = {getSearchTerms()};
				appConfig = appConfigDao.getAppConfig();
			       
			    if (checkAuthReady() && termsFound ) {
			    	 initStream(trackArray);
			    	 log.info("Twitter Stream started");
			     } else if (!termsFound) {
			    	 log.warn("Twitter Stream start aborted (no search terms defined)");
			     }
				
			} catch (Exception e) {
				log.error("Error initializing twitter stream: " + e.getMessage());
			}


		} catch (Exception e) {
			log.error("Error initializing twitter stream: " + e.getMessage());
		}
	}
	
	public void stop() {
		try {
			if (Util.twitterStream != null) {
				Util.twitterStream.shutdown();
				//Util.twitterStream.cleanUp();
				Util.twitterStream = null;
			}
		} catch (Exception e) {
			log.error("Error closing twitter stream: " + e.getMessage());
		}
	}
	
	public void initStream(String[] trackArray) {

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
        	
        	
            public void onStatus(Status status) {

            	URLEntity[] links = status.getURLEntities();
            	
            	if (links != null && links.length > 0) {
            		try {
            			Tweet tweet = new Tweet(status.getUser().getName(),status.getText(),status.getRetweetCount(),status.getId());
            			List<UrlEntity> urlEntities = new ArrayList<UrlEntity>();
            			tweet.setUrlEntities(urlEntities);
            			for( URLEntity link : links ) {
   							UrlEntity url = new UrlEntity();
   						    if(link.getExpandedURL() != null) {
    							url.setUrlOriginal(link.getExpandedURL().toString());
            				} else if (link.getURL() != null) {
            					url.setUrlOriginal(link.getURL().toString());
            				}
   						    url.setTweet(tweet);
							tweet.getUrlEntities().add(url);
            			}
            			log.debug("Persisting tweetId: " + tweet.getTweetiD());
            			String rawJson = DataObjectFactory.getRawJSON(status);
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
            				tweetAnalyserService.run();
            			}
            			
            			
            		} catch (Exception e) {
            			log.error("Transaction error on tweet" + e.getMessage() + ", cause: " + e.getCause());
            			log.debug(e);
            		} finally {

            		}
            	}
            	
            }
            public void onDeletionNotice(StatusDeletionNotice statusDeletionNotice) {}
            public void onTrackLimitationNotice(int numberOfLimitedStatuses) {}
            public void onScrubGeo(long userId, long upToStatusId) {}
            public void onException(Exception ex) {
            	log.error("Error retrieving tweet: " + ex.getMessage());
               }
        };
        
        Util.twitterStream = new TwitterStreamFactory(builder.build()).getInstance();
        Util.twitterStream.addListener(listener);

        if (trackArray != null) {
        	Util.twitterStream.filter(new FilterQuery(0, followArray, trackArray));
        }
	}
	
	
	
	public final boolean testScheduler() {

		int counter = 0;
		
		for (int x=0; x < 3;x++) {

			try {
				tweetDao.persist(new Tweet("test tweet: " + counter ));
			} catch (Exception e) {
    			log.error("Transaction error on tweet" + e.getMessage());
    		}
			counter ++;
			
        }

		return false;
	}

	public final String getSearchTerms() {
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
	
	public boolean checkAuthReady() {
		boolean result = false;
		if (appConfig.getConsumerKey() != null && !appConfig.getConsumerKey().isEmpty() && appConfig.getConsumerSecret() != null && !appConfig.getConsumerSecret().isEmpty()
				&& appConfig.getAccessToken() != null && !appConfig.getAccessToken().isEmpty()
				&& appConfig.getAccessTokenSecret() != null && !appConfig.getAccessTokenSecret().isEmpty()) {
			result = true;
		}
		return result;
	}

	/**
	 * @param jsonLogger the jsonLogger to set
	 */
	public void setJsonLogger(JsonLogger jsonLogger) {
		this.jsonLogger = jsonLogger;
	}

	/**
	 * @return the analysisTriggerValue
	 */
	public Integer getAnalysisTriggerValue() {
		return analysisTriggerValue;
	}

	/**
	 * @param analysisTriggerValue the analysisTriggerValue to set
	 */
	public void setAnalysisTriggerValue(Integer analysisTriggerValue) {
		log.info("TwitterStreamDaemonController setting analysisTriggerValue: " + analysisTriggerValue);
		this.analysisTriggerValue = analysisTriggerValue;
	}

	/**
	 * @return the tweetAnalyserService
	 */
	public TweetAnalyserService getTweetAnalyserService() {
		return tweetAnalyserService;
	}

	/**
	 * @param tweetAnalyserService the tweetAnalyserService to set
	 */
	public void setTweetAnalyserService(
			TweetAnalyserService tweetAnalyserService) {
		this.tweetAnalyserService = tweetAnalyserService;
	}
	
}
