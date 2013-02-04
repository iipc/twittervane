package uk.bl.wap.crowdsourcing.agent;

import static com.rosaloves.bitlyj.Bitly.as;
import static com.rosaloves.bitlyj.Bitly.expand;

import java.io.IOException;
import java.net.Authenticator;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.PasswordAuthentication;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;

import uk.bl.wap.crowdsourcing.AppConfig;
import uk.bl.wap.crowdsourcing.SearchTerm;
import uk.bl.wap.crowdsourcing.Tweet;
import uk.bl.wap.crowdsourcing.UrlEntity;
import uk.bl.wap.crowdsourcing.Util;
import uk.bl.wap.crowdsourcing.WebCollection;
import uk.bl.wap.crowdsourcing.dao.AppConfigDao;
import uk.bl.wap.crowdsourcing.dao.TweetDao;
import uk.bl.wap.crowdsourcing.dao.UrlEntityDao;
import uk.bl.wap.crowdsourcing.dao.WebCollectionDao;

import com.rosaloves.bitlyj.Url;

/**
 * @author twoods
 *
 */
/**
 * @author twoods
 *
 */
/**
 * @author twoods
 *
 */
/**
 * @author twoods
 *
 */
public class TweetAnalyserServiceImpl implements TweetAnalyserService {

	private TweetAnalyserStatusEnum status = TweetAnalyserStatusEnum.STOPPED;

	@Autowired
	private UrlEntityDao urlEntityDao;

	@Autowired
	private WebCollectionDao webCollectionDao;

	@Autowired
	private TweetDao tweetDao;

	@Autowired
	private AppConfigDao appConfigDao;

	private Integer jobNumber = null;
	private Integer processCounter = 0;
	private boolean purge = false;
	private boolean purgeAll = false;
	private boolean purgeProcessed = false;
	private boolean purgeFailed = false;
	private String urlInProgress = null;
	private AppConfig appConfig = null;

	/**
	 * Number of most popular (eg: top 10) tweets for url expansion
	 */
	private Integer topUrls = 10;

	/**
	 * The logger
	 */
	private final Log log = LogFactory.getLog(getClass());

	@Override
	public void run() {
		log.debug("TweetAnalyserServiceImpl.run");
		this.runJob(jobNumber);
	}

	@Override
	public void abort() {
		log.debug("TweetAnalyserServiceImpl.abort");
	}

	@Override
	public void pause() {
		log.debug("TweetAnalyserServiceImpl.pause");
	}

	@Override
	public void resume() {
		log.debug("TweetAnalyserServiceImpl.resume");
	}

	@Override
	public TweetAnalyserStatusEnum getStatus() {
		log.debug("TweetAnalyserServiceImpl.getStatus");
		return status;
	}

	private void runJob(Integer jobNumber) {

		status = TweetAnalyserStatusEnum.RUNNING;

		List<WebCollection> webCollections = webCollectionDao
				.getAllCollections();
		appConfig = appConfigDao.getAppConfig();

		this.processCounter = 0;
		boolean expandStatus = false;

		List<Tweet> tweets = tweetDao.getUnprocessedTweets(jobNumber);
		Set<String> popularUrls = urlEntityDao.getTopUrls(topUrls);

		List<WebCollection> tweetWebCollection = new ArrayList<WebCollection>();

		URL u = null;
		String expandedUrl = null;

		for (Tweet tweet : tweets) {
			log.debug("Analysing tweet id: " + tweet.getId());
			tweetWebCollection = matchCollections(webCollections, tweet.getText());

			List<UrlEntity> urlEntities = tweet.getUrlEntities();

			for (UrlEntity ue : urlEntities) {

				try {

					this.urlInProgress = ue.getUrlOriginal();

					// expand the url if the original url is popular
					if (popularUrls.contains(ue.getUrlOriginal())) {
						expandStatus = getExpandedUrl();
					}

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

					for (WebCollection wc : tweetWebCollection) {
						ue.setExpanded(expandStatus);
						if (expandStatus) {
							ue.setUrlFull(expandedUrl);
						}
						ue.setUrlDomain(u.getHost());
						ue.setTotalRetweets(tweet.getRetweetCount());
						ue.setWebCollection(wc);
					}
					if (tweetWebCollection.isEmpty()) {
						ue.setExpanded(expandStatus);
						ue.setUrlDomain(u.getHost());
						ue.setTotalRetweets(tweet.getRetweetCount());
						if (expandStatus) {
							ue.setUrlFull(expandedUrl);
							// attempt to resolve for the collection by scanning the expanded url for the search terms 
							tweetWebCollection = matchCollections(webCollections, ue.getUrlFull());
							for (WebCollection wc : tweetWebCollection) {
								ue.setWebCollection(wc);
							}
							if (tweetWebCollection.isEmpty()) {
								// attempt to resolve for the collection by scanning the original url for the search terms 
								tweetWebCollection = matchCollections(webCollections, ue.getUrlOriginal());
								for (WebCollection wc : tweetWebCollection) {
									ue.setWebCollection(wc);
								}
							}
						} 
					}
					if (tweetWebCollection.isEmpty()) {
						ue.setErrors("Failed to identify web collection: no search term found in tweet text");
					}

				} catch (Exception e) {
					log.error("Error adding urlEntity : " + e.getMessage());
					log.error(e);
				}

			}

			processCounter++;
			tweet.setProcessed(true);
			tweetDao.persist(tweet);
		}
		status = TweetAnalyserStatusEnum.STOPPED;
	}

	private boolean getExpandedUrl() {
		boolean result = true;

		if (this.urlInProgress.indexOf("http://bit.ly") == 0
				&& appConfig.getBitlyLogin() != null
				&& !appConfig.getBitlyLogin().isEmpty()
				&& appConfig.getBitlyApiKey() != null
				&& !appConfig.getBitlyApiKey().isEmpty()) {
			result = this.getBitly();
		} else if (Util.isShortenedUrl(this.urlInProgress)) {
			result = this.expandUrl();

			if (this.urlInProgress.indexOf("http://bit.ly") == 0
					&& appConfig.getBitlyLogin() != null
					&& !appConfig.getBitlyLogin().isEmpty()
					&& appConfig.getBitlyApiKey() != null
					&& !appConfig.getBitlyApiKey().isEmpty()) {
				result = this.getBitly();
			}
			if (result && Util.isShortenedUrl(this.urlInProgress)) {
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
				if (appConfig.getHttpProxyHost() != null
						&& !appConfig.getHttpProxyHost().isEmpty()) {
					System.setProperty("http.proxyHost",
							appConfig.getHttpProxyHost());
				}
				if (appConfig.getHttpProxyPort() != null
						&& !appConfig.getHttpProxyPort().isEmpty()) {
					System.setProperty("http.proxyPort",
							appConfig.getHttpProxyPort());
				}
				if (appConfig.getHttpProxyUser() != null
						&& !appConfig.getHttpProxyUser().isEmpty()) {
					Authenticator.setDefault(new Authenticator() {
						protected PasswordAuthentication getPasswordAuthentication() {
							return new PasswordAuthentication(appConfig
									.getHttpProxyUser(), appConfig
									.getHttpProxyUser().toCharArray());
						}
					});
				}

				con = (HttpURLConnection) (urlOriginal.openConnection());
				con.setConnectTimeout(3000);
				con.setInstanceFollowRedirects(false);
				con.connect();
				String location = con.getHeaderField("Location");
				if (location != null && !location.isEmpty()) {
					this.urlInProgress = location;
				} else {
					result = false;
				}
			} catch (IOException e) {
				log.error("JobController.getExpandedUrl : Error connecting to url : "
						+ e.getMessage());
				result = false;
			} finally {
				con.disconnect();
				con = null;
			}
		} catch (MalformedURLException e) {
			log.error("JobController.getExpandedUrl : Error creating url from string : "
					+ e.getMessage());
			result = false;
		}

		return result;
	}

	private boolean getBitly() {
		boolean result = true;
		if (appConfig.getHttpProxyHost() != null
				&& !appConfig.getHttpProxyHost().isEmpty()) {
			System.setProperty("http.proxyHost", appConfig.getHttpProxyHost());
		}
		if (appConfig.getHttpProxyPort() != null
				&& !appConfig.getHttpProxyPort().isEmpty()) {
			System.setProperty("http.proxyPort", appConfig.getHttpProxyPort());
		}
		if (appConfig.getHttpProxyUser() != null
				&& !appConfig.getHttpProxyUser().isEmpty()) {
			Authenticator.setDefault(new Authenticator() {
				protected PasswordAuthentication getPasswordAuthentication() {
					return new PasswordAuthentication(appConfig
							.getHttpProxyUser(), appConfig.getHttpProxyUser()
							.toCharArray());
				}
			});
		}
		try {
			Url url = as(appConfig.getBitlyLogin(), appConfig.getBitlyApiKey())
					.call(expand(this.urlInProgress));
			this.urlInProgress = url.getLongUrl();
		} catch (Exception e) {
			result = false;
		}
		return result;
	}

	private List<WebCollection> matchCollections(
			List<WebCollection> webCollections, String tweetText) {
		List<WebCollection> lwc = new ArrayList<WebCollection>();
		for (WebCollection wc : webCollections) {
			if (wc.getSearchTerms() != null) {
				for (SearchTerm searchTerms : wc.getSearchTerms()) {
					if (tweetText.toLowerCase().contains(
							searchTerms.getTerm().toLowerCase())) {
						if (!lwc.contains(wc)) {
							lwc.add(wc);
						}
					} else {
						// remove any '#' characters from the search term
						String searchTerm = searchTerms.getTerm().toLowerCase()
								.replaceAll("#", "");
						if (tweetText.toLowerCase().contains(searchTerm)) {
							if (!lwc.contains(wc)) {
								lwc.add(wc);
							}
						}
					}
				}
			}
		}
		return lwc;
	}

	/**
	 * @return the processCounter
	 */
	public Integer getProcessCounter() {
		return processCounter;
	}

	@Override
	public Number getTotalTweets() {
		return tweetDao.getTotalTweets();
	}

	@Override
	public Number getTotalUnprocessed() {
		return urlEntityDao.getTotalUnprocessed();
	}

	@Override
	public Number getTotalProcessedEntities() {
		return urlEntityDao.getTotalProcessedEntities();
	}

	/**
	 * @param jobNumber
	 *            the jobNumber to set
	 */
	public void setJobNumber(Integer jobNumber) {
		this.jobNumber = jobNumber;
	}

	/**
	 * Setter for topUrls
	 * @param topUrls
	 */
	public void setTopUrls(Integer topUrls) {
		this.topUrls = topUrls;
		log.debug("topUrls changed to: " + topUrls);
	}

	/**
	 * @return the topUrls
	 */
	public Integer getTopUrls() {
		return topUrls;
	}

	@Override
	public void purgeUrlEntities() {
		urlEntityDao.deleteEntries();	
	}

	@Override
	public void purgeTweets() {
		tweetDao.deleteAllTweets();
	}

	@Override
	public void purgeProcessedTweets() {
		tweetDao.deleteProcessedTweets();
	}

	@Override
	public void purgeFailedUrls() {
		urlEntityDao.deleteFailedEntries();
	}

}
