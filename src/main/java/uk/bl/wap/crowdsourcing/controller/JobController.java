package uk.bl.wap.crowdsourcing.controller;

import static com.rosaloves.bitlyj.Bitly.as;
import static com.rosaloves.bitlyj.Bitly.expand;

import java.io.IOException;
import java.net.Authenticator;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.PasswordAuthentication;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.rosaloves.bitlyj.Url;

import uk.bl.wap.crowdsourcing.AppConfig;
import uk.bl.wap.crowdsourcing.SearchTerm;
import uk.bl.wap.crowdsourcing.Tweet;
import uk.bl.wap.crowdsourcing.UrlEntity;
import uk.bl.wap.crowdsourcing.UrlEntityFailed;
import uk.bl.wap.crowdsourcing.Util;
import uk.bl.wap.crowdsourcing.WebCollection;
import uk.bl.wap.crowdsourcing.dao.AppConfigDao;
import uk.bl.wap.crowdsourcing.dao.TweetDao;
import uk.bl.wap.crowdsourcing.dao.UrlEntityDao;
import uk.bl.wap.crowdsourcing.dao.UrlEntityFailedDao;
import uk.bl.wap.crowdsourcing.dao.WebCollectionDao;

@Controller
public class JobController {

	int processCounter = 0;
	ModelAndView mv = new ModelAndView();
	
	@Autowired
	private UrlEntityDao urlEntityDao;
	
	@Autowired
	private UrlEntityFailedDao urlEntityFailedDao;
	
	@Autowired
	private WebCollectionDao webCollectionDao;

	@Autowired
	private TweetDao tweetDao;
	
	@Autowired
	private AppConfigDao appConfigDao;
	
	private int jobNumber = 0;
	private boolean purge = false;
	private boolean purgeAll = false;
	private boolean purgeProcessed = false;
	private boolean purgeFailed = false;
	private String urlInProgress = null;
	private String bitlyLogin = null;
	private String bitlyApiKey = null;
	private AppConfig appConfig = null;
	
	@RequestMapping(value="/runjob")
    public ModelAndView crowdsourcing(HttpServletRequest request) {

		Map<String, String> message = new HashMap<String, String>();
		
		if (request.getParameter("jobNumber") != null) {
			jobNumber = Integer.parseInt(request.getParameter("jobNumber"));
		} else {
			jobNumber = 0;
		}
		
		if (request.getParameter("purge") != null) {
			purge = Boolean.parseBoolean(request.getParameter("purge"));
		} else {
			purge = false;
		}
		
		if (request.getParameter("purgeAll") != null) {
			purgeAll = Boolean.parseBoolean(request.getParameter("purgeAll"));
		} else {
			purgeAll= false;
		}
		
		if (request.getParameter("purgeProcessed") != null) {
			purgeProcessed = Boolean.parseBoolean(request.getParameter("purgeProcessed"));
		} else {
			purgeProcessed = false;
		}
		
		if (request.getParameter("purgeFailed") != null) {
			purgeFailed = Boolean.parseBoolean(request.getParameter("purgeFailed"));
		} else {
			purgeFailed = false;
		}
		
		if (jobNumber > 0) {
			runJob(jobNumber);
			message.put("message", ""+ processCounter);	 
		} else if (purge) {
			while (urlEntityDao.getTotalEntities().longValue() > 0) {
				urlEntityDao.deleteEntries();
			}
		} else if (purgeAll) {
			while (tweetDao.getTotalTweets().longValue() > 0) {
				tweetDao.deleteAllTweets();
			}
		} else if (purgeProcessed) {
			long lastTweet;
			try {
					lastTweet = (Long) urlEntityDao.getLastTweet();
			} 	catch (Exception e) {
					lastTweet = 0;
			}
			while (tweetDao.getTotalProcessedTweets(lastTweet).longValue() > 0) {
				tweetDao.deleteProcessedTweets(lastTweet);
			}
		} else if (purgeFailed) {
			while (urlEntityFailedDao.getTotalFailedEntities().longValue() > 0) {
				urlEntityFailedDao.deleteFailedEntries();
			}
		}
	
        mv.addObject("message",message);
		mv.addObject("urlEntityDao",urlEntityDao);
		mv.addObject("tweetDao",tweetDao);
        mv.setViewName("jobs.jsp");
        return mv;

    }
	
	private void runJob(int jobNumber) {
		
		List<WebCollection> webCollections = webCollectionDao.getAllCollections();
		appConfig = appConfigDao.getAppConfig();
		
		this.processCounter = 0;
		boolean expandStatus = false;
		
		long lastTweet;
		
		try {
				lastTweet = (Long) urlEntityDao.getLastTweet();
		} 	catch (Exception e) {
				lastTweet = 0;
		}
		List<Tweet> tweets = tweetDao.getUnprocessedTweets(jobNumber,lastTweet);
		
		List<WebCollection> tweetWebCollection = new ArrayList<WebCollection>();
		
		URL u = null;
		String expandedUrl = null;

		for (Tweet tweet : tweets) {
			
			tweetWebCollection = matchCollections(webCollections,tweet.getText());
			
			List<String> urlEntities = tweet.getUrlEntities();
			
			for (String ue: urlEntities) {
				
				try {
					
					this.urlInProgress = ue;
					
					expandStatus = getExpandedUrl();
					
					u = new URL(this.urlInProgress);
					
					if (u.getProtocol() != null) {
						expandedUrl = u.getProtocol() + "://";
					}
					expandedUrl += u.getHost();
					if (u.getPath() != null) {
						expandedUrl += u.getPath();
					}
					if (u.getQuery() != null) {
						expandedUrl += u.getQuery();
					}
					
					
					if (!tweetWebCollection.isEmpty()) {
						for (WebCollection wc: tweetWebCollection) {
							if (expandStatus) {
								urlEntityDao.persist(new UrlEntity(ue,expandedUrl,u.getHost(),tweet.getId(),wc.getId(),wc.getName(),(int) tweet.getRetweetCount()));
							} else {
								urlEntityFailedDao.persist(new UrlEntityFailed(ue,tweet.getId(),"Expand Failed",wc.getId(),wc.getName()));
							}
							
						//	processCounter ++;
						}
					} else {
						if (expandStatus) {
							urlEntityDao.persist(new UrlEntity(ue,expandedUrl,u.getHost(),tweet.getId(),(long) 0,"",(int) tweet.getRetweetCount()));
						} else {
							urlEntityFailedDao.persist(new UrlEntityFailed(ue,tweet.getId(),"Expand Failed",(long) 0,""));
						}
					}
					
				} catch (Exception e) {
					Util.log.severe("JobController.crowdsourcing : Error adding urlEntity : " + e.getMessage());
					try {
						urlEntityFailedDao.persist(new UrlEntityFailed(ue,tweet.getId(),"Expand Failed",0,""));
					} catch (Exception ex) {
						Util.log.severe("JobController.crowdsourcing : Error adding urlEntityFailed : " + e.getMessage());
					}
				}
				
			}
			processCounter ++;
			//tweetDao.deleteTweet(tweet.getId());
		}
	}
	
	private boolean getExpandedUrl() {
		boolean result = true;

		if (this.urlInProgress.indexOf("http://bit.ly") == 0 && appConfig.getBitlyLogin() != null && !appConfig.getBitlyLogin().isEmpty()
				&& appConfig.getBitlyApiKey() != null && !appConfig.getBitlyApiKey().isEmpty()) {
			result = this.getBitly();	
		} else if (Util.isShortenedUrl(this.urlInProgress)) {
			result = this.expandUrl();
			
			if (this.urlInProgress.indexOf("http://bit.ly") == 0 && appConfig.getBitlyLogin() != null && !appConfig.getBitlyLogin().isEmpty()
					&& appConfig.getBitlyApiKey() != null && !appConfig.getBitlyApiKey().isEmpty()) {
				result = this.getBitly();
			}
			if ( result && Util.isShortenedUrl(this.urlInProgress)) {
				result = this.expandUrl();
			}
		}
		return result;

	}
	
	private boolean expandUrl() {
		
		boolean result = true;
		HttpURLConnection con = null;
		
		try {
			
			URL urlOriginal = new URL(this.urlInProgress);

			try {
				if (appConfig.getHttpProxyHost() != null && !appConfig.getHttpProxyHost().isEmpty()) {
					System.setProperty("http.proxyHost",appConfig.getHttpProxyHost()) ;
				}
				if (appConfig.getHttpProxyPort() != null && !appConfig.getHttpProxyPort().isEmpty()) {
					System.setProperty("http.proxyPort",appConfig.getHttpProxyPort()) ;
				}
				if (appConfig.getHttpProxyUser()!= null && !appConfig.getHttpProxyUser().isEmpty()) {
					Authenticator.setDefault(new Authenticator() {
					      protected PasswordAuthentication getPasswordAuthentication() {
					        return new
					           PasswordAuthentication(appConfig.getHttpProxyUser(),appConfig.getHttpProxyUser().toCharArray());
					    }});
				}

				con = (HttpURLConnection)(urlOriginal.openConnection());
				con.setConnectTimeout(3000);
				con.setInstanceFollowRedirects( false );
				con.connect();
				String location = con.getHeaderField( "Location" );
				if (location != null && !location.isEmpty()) {
					this.urlInProgress = location;
				} else {
					result = false;
				}
			} catch (IOException e) {
				Util.log.severe("JobController.getExpandedUrl : Error connecting to url : " + e.getMessage());
				result = false;
			} finally {
				con.disconnect();
				con = null;
			}
		} catch (MalformedURLException e) {
			Util.log.severe("JobController.getExpandedUrl : Error creating url from string : " + e.getMessage());
			result = false;
		}
		
		return result;
	}

	private boolean getBitly() {
		boolean result = true;
		if (appConfig.getHttpProxyHost() != null && !appConfig.getHttpProxyHost().isEmpty()) {
			System.setProperty("http.proxyHost",appConfig.getHttpProxyHost()) ;
		}
		if (appConfig.getHttpProxyPort() != null && !appConfig.getHttpProxyPort().isEmpty()) {
			System.setProperty("http.proxyPort",appConfig.getHttpProxyPort()) ;
		}
		if (appConfig.getHttpProxyUser()!= null && !appConfig.getHttpProxyUser().isEmpty()) {
			Authenticator.setDefault(new Authenticator() {
			      protected PasswordAuthentication getPasswordAuthentication() {
			        return new
			           PasswordAuthentication(appConfig.getHttpProxyUser(),appConfig.getHttpProxyUser().toCharArray());
			    }});
		}
		try {
			Url url = as(appConfig.getBitlyLogin(), appConfig.getBitlyApiKey()).call(expand(this.urlInProgress));
			this.urlInProgress =  url.getLongUrl();
		} catch (Exception e) {
			result = false;
		}
		return result;
	}
	
	private List<WebCollection> matchCollections(List<WebCollection> webCollections,String tweetText) {
		List<WebCollection> lwc = new ArrayList<WebCollection>();
		for (WebCollection wc: webCollections) {
			for (SearchTerm searchTerms: wc.getSearchterms()) {
				if(tweetText.toLowerCase().contains(searchTerms.getTerm())) {
					if (!lwc.contains(wc)) {
						lwc.add(wc);
					}
				}
			}
		}
		return lwc;
	}
	
}
