package uk.bl.wap.crowdsourcing.dao;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.Transactional;

import uk.bl.wap.crowdsourcing.ReportUrlEntity;
import uk.bl.wap.crowdsourcing.Tweet;
import uk.bl.wap.crowdsourcing.UrlEntity;
import uk.bl.wap.crowdsourcing.WebCollection;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"/testApplicationContext.xml"})
@TransactionConfiguration(defaultRollback=false)
public class UrlEntityTest {

	@Autowired
	private UrlEntityDao urlEntityDao;
	@Autowired
	private TweetDao tweetDao;
	@Autowired
	private WebCollectionDao webCollectionDao;
	
	private Logger logger = Logger.getLogger(UrlEntityTest.class);
	
	@Test
	@Transactional
	@Rollback(false)
	public void _first(){
		Long tweetId = 9887654321L;
		Long retweetCount = 1234L;
		
		// create a tweet to run the persistence tests
		Tweet tweet = new Tweet("testUserName", "testText", retweetCount, tweetId);
		tweetDao.persist(tweet);
		
		// create a WebCollection
		WebCollection webCollection = new WebCollection("JUnit Test Web Collection");
		webCollectionDao.persist(webCollection);
	}
	
	@Transactional
	@Rollback(false)	
	@Test
	public void persistTest() {
		
		String urlOriginal = "http://t";
		String urlFull = "http://test";
		String urlDomain = "testDomain";
		
		Tweet tweet = tweetDao.getAllTweets().get(0);
		WebCollection webCollection = webCollectionDao.getAllCollections().get(0);
		
		UrlEntity urlEntity = new UrlEntity();
		urlEntity.setUrlOriginal(urlOriginal);
		urlEntity.setUrlFull(urlFull);
		urlEntity.setUrlDomain(urlDomain);
		urlEntity.setWebCollection(webCollection);
		urlEntity.setTotalRetweets(3L);
		urlEntity.setTweet(tweet);

		int oldEntityCount = urlEntityDao.getTotalEntities().intValue();
		
		List<UrlEntity> urlEntities = new ArrayList<UrlEntity>();
		
		tweet.setUrlEntities(urlEntities);
		tweet.getUrlEntities().add(urlEntity);
		tweetDao.persist(tweet);
		
		urlEntity.setWebCollection(webCollection);
		webCollectionDao.persist(webCollection);

		int newEntityCount = urlEntityDao.getTotalEntities().intValue();
		
		Assert.assertEquals(1, newEntityCount - oldEntityCount);
		
	}
	
	@Transactional
	@Rollback(false)	
	@Test
	public void persist2Test() {
		
		String urlOriginal = "http://t2";
		String urlFull = "http://test2";
		String urlDomain = "testDomain2";
		
		Tweet tweet = tweetDao.getAllTweets().get(0);
		WebCollection webCollection = webCollectionDao.getAllCollections().get(0);
		
		UrlEntity urlEntity = new UrlEntity();
		urlEntity.setUrlOriginal(urlOriginal);
		urlEntity.setUrlFull(urlFull);
		urlEntity.setUrlDomain(urlDomain);
		urlEntity.setWebCollection(webCollection);
		urlEntity.setTotalRetweets(4L);
		urlEntity.setTweet(tweet);

		int oldEntityCount = urlEntityDao.getTotalEntities().intValue();
		
		List<UrlEntity> urlEntities = new ArrayList<UrlEntity>();
		
		tweet.setUrlEntities(urlEntities);
		tweet.getUrlEntities().add(urlEntity);
		tweetDao.persist(tweet);
		
		urlEntity.setWebCollection(webCollection);
		webCollectionDao.persist(webCollection);

		int newEntityCount = urlEntityDao.getTotalEntities().intValue();
		
		Assert.assertEquals(1, newEntityCount - oldEntityCount);
		
	}
	
	@Transactional
	@Rollback(false)	
	@Test
	public void updateTest() {
		
		Tweet tweet = tweetDao.getAllTweets().get(0);
		
		UrlEntity urlEntity = tweet.getUrlEntities().get(0);

		// check it contains a stored attribute value
		Assert.assertEquals("testDomain2", urlEntity.getUrlDomain());
		
		// modify the value
		urlEntity.setUrlDomain("anotherTestDomain");
		
		// persist the value
		tweetDao.persist(tweet);
		
		// retrieve and re-check
		tweet = tweetDao.getAllTweets().get(0);
		
		urlEntity = tweet.getUrlEntities().get(0);

		// check it contains a stored attribute value
		Assert.assertEquals("anotherTestDomain", urlEntity.getUrlDomain());
	}

	
	@Test
	public void getTopUrlAsObjectArrayTest() {
		
		WebCollection webCollection = webCollectionDao.getAllCollections().get(0);
		List<Object[]> topUrls = urlEntityDao.getTopUrlAsObjectArray(webCollection.getId());
		logger.info("topUrls size: " + topUrls.size());
		Assert.assertEquals(1, topUrls.size());
		// access the array
		String url = (String)topUrls.get(0)[0];
		Long count = ((BigInteger)topUrls.get(0)[1]).longValue();
		
	}
	
	@Test
	public void getTopUrlTest() {
		WebCollection webCollection = webCollectionDao.getAllCollections().get(0);
		List<Object[]> urlEntities = urlEntityDao.getTopUrl(webCollection.getId(), "", "", 0, 1);
		Assert.assertEquals(1, urlEntities.size());
		// access the array
		String url = (String)urlEntities.get(0)[0];
		Long count = ((BigInteger)urlEntities.get(0)[1]).longValue();
	}
	
	@Test
	public void getTotalEntitiesTest() {
		Number number = urlEntityDao.getTotalEntities();
		logger.info("Total url entities: " + number);
		Assert.assertNotSame(0, number);
	}
	
	@Test
	public void getTopDomainTest() {
		WebCollection webCollection = webCollectionDao.getAllCollections().get(0);
		List<Object[]> urlDomains = urlEntityDao.getTopDomain(webCollection.getId(), "", "", 0, 1);
		Assert.assertEquals(1, urlDomains.size());
		// access the array
		String url = (String)urlDomains.get(0)[0];
		Long count = ((BigInteger)urlDomains.get(0)[1]).longValue();
	}
	
	@Test
	public void getTopPopularityTest() {
		WebCollection webCollection = webCollectionDao.getAllCollections().get(0);
		List<Object[]> popularity = urlEntityDao.getTopPopularity(webCollection.getId(), "", "", 0, 1);
		Assert.assertNotSame(-1, popularity.size());
	}
	
	@Test
	public void getAllUrlEntitiesByCollectionTest() {
		WebCollection webCollection = webCollectionDao.getAllCollections().get(0);
		List<ReportUrlEntity> urlEntities = urlEntityDao.getAllUrlEntitiesByCollection(webCollection.getId(), null, null);
		Assert.assertNotSame(-1, urlEntities.size());
		// access the array
		String url = urlEntities.get(0).getUrlFull();
	}
	

	@Transactional()
	@Rollback(false)
	@Test
	public void deleteEntryTest() {
		int oldEntityCount = urlEntityDao.getTotalEntities().intValue();
		
		Tweet tweet = tweetDao.getAllTweets().get(0);
		UrlEntity urlEntity = tweet.getUrlEntities().get(0);
		logger.info("Removing urlEntity id: " + urlEntity.getId());
		tweet.getUrlEntities().remove(urlEntity);
		tweetDao.persist(tweet);
		urlEntityDao.deleteEntry(urlEntity.getId());
		
		int newEntityCount = urlEntityDao.getTotalEntities().intValue();
		
		Assert.assertEquals(-1, newEntityCount - oldEntityCount);
		
	}
	
	@Transactional()
	@Rollback(false)
	@Test
	public void deleteEntry2Test() {
		int oldEntityCount = urlEntityDao.getTotalEntities().intValue();
		
		Tweet tweet = tweetDao.getAllTweets().get(0);
		UrlEntity urlEntity = tweet.getUrlEntities().get(0);
		logger.info("Removing urlEntity id: " + urlEntity.getId());
		tweet.getUrlEntities().remove(urlEntity);
		tweetDao.persist(tweet);
		urlEntityDao.deleteEntry(urlEntity.getId());
		
		int newEntityCount = urlEntityDao.getTotalEntities().intValue();
		
		Assert.assertEquals(-1, newEntityCount - oldEntityCount);
		
	}
	
	@Test
	@Transactional
	@Rollback(false)
	public void tearDown() {
		List<Tweet> tweets = tweetDao.getAllTweets();
		for (Tweet tweet : tweets) {
			if (tweet.getText().equals("testText")) {
				tweetDao.deleteTweet(tweet.getId());
			}
		}
		
		List<WebCollection> webCollections = webCollectionDao.getAllCollections();
		for (WebCollection webCollection : webCollections) {
			if (webCollection.getName() != null && webCollection.getName().equals("JUnit Test Web Collection")) {
				webCollectionDao.deleteWebCollection(webCollection.getId());
			}
		}

	}

}
