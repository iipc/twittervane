package uk.bl.wap.crowdsourcing.controller;


import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

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
import uk.bl.wap.crowdsourcing.AppConfig;
import uk.bl.wap.crowdsourcing.SearchTerm;
import uk.bl.wap.crowdsourcing.Tweet;
import uk.bl.wap.crowdsourcing.Util;
import uk.bl.wap.crowdsourcing.WebCollection;
import uk.bl.wap.crowdsourcing.dao.AppConfigDao;
import uk.bl.wap.crowdsourcing.dao.SearchTermDao;
import uk.bl.wap.crowdsourcing.dao.TweetDao;
import uk.bl.wap.crowdsourcing.dao.WebCollectionDao;

@Controller
public class TwitterStreamDaemonController {

	int processCounter = 0;
	ModelAndView mv = new ModelAndView();
	
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
			    	 //while (testScheduler()) {}
			     }
				
			} catch (Exception e) {
				Util.log.severe("Error initializing twitter stream: " + e.getMessage());
			}


		} catch (Exception e) {
			Util.log.severe("Error initializing twitter stream: " + e.getMessage());
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
			Util.log.severe("Error closing twitter stream: " + e.getMessage());
		}
	}
	
	public void initStream(String[] trackArray) {
		
		long[] followArray = null;
		ConfigurationBuilder builder = new ConfigurationBuilder();
    	
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
            			for( URLEntity link : links ) {
            				if(Util.getText(link.getExpandedURL()) != null) {
            					tweet.getUrlEntities().add(link.getExpandedURL().toString());
            				} else if (Util.getText(link.getURL()) != null) {
            					tweet.getUrlEntities().add(link.getURL().toString());
            				}
            			}
            			tweetDao.persist(tweet);
            		} catch (Exception e) {
            			Util.log.severe("Transaction error on tweet" + e.getMessage());
            		} finally {

            		}
            	}
            	
            }
            public void onDeletionNotice(StatusDeletionNotice statusDeletionNotice) {}
            public void onTrackLimitationNotice(int numberOfLimitedStatuses) {}
            public void onScrubGeo(long userId, long upToStatusId) {}
            public void onException(Exception ex) {
            	Util.log.severe("Error retrieving tweet: " + ex.getMessage());
               }
			//public void onStallWarning(StallWarning warning) {
				// TODO Auto-generated method stub
				
			//}
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
    			Util.log.severe("Transaction error on tweet" + e.getMessage());
    		}
			counter ++;
			
        }

		return false;
	}

	public final String getSearchTerms() {
		String trackList = "";
		
		for (WebCollection webCollection: webCollectionDao.getAllCollectionsForStream()) {
			for (SearchTerm searchTerm : webCollection.getSearchterms()) {
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
	
}
