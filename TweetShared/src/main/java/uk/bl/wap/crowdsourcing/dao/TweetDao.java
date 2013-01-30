package uk.bl.wap.crowdsourcing.dao;

import java.math.BigInteger;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Logger;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import uk.bl.wap.crowdsourcing.Tweet;

@Component
public class TweetDao {
	
	@SuppressWarnings("unused")
	private Logger log = Logger.getLogger("logtest");
	
    @PersistenceContext private EntityManager em;
    
    @Transactional
    public void persist(Tweet tweet) {
        em.merge(tweet);
    }
    
    public Number getTotalTweets() {
    	Number countResult;
    	try {
    		Query query = em.createQuery("SELECT count(t.id) FROM Tweet t");
    		countResult=(Number) query.getSingleResult();
    	} catch (Exception e) {
    		countResult = 0;
    	}
       	return countResult;
    }
    
    public Long getTotalTweetsByCollection(Long collectionId) {
    	// get the url count (processed + unprocessed)
    	Number countUrl;
    	String sql = null;
    	if (collectionId != null) {
    		sql = "SELECT count(distinct u.tweet_id) FROM url_entity u where u.web_collection_id = :collectionId ";
    	} else {
    		sql = "SELECT count(distinct u.tweet_id) FROM url_entity u where u.web_collection_id is null ";
    	}
		Query query = em.createNativeQuery(sql);
		if (collectionId != null) {
			query.setParameter("collectionId", collectionId);
		}
		countUrl =(Number) query.getSingleResult();

       	return countUrl.longValue();
    }
    
    public Long getTotalTweetsByPeriod(Long collectionId, Calendar start, Calendar end) {
    	// get the url count (processed + unprocessed)
    	Number countUrl;
    	String sql = null;
    	if (collectionId != null) {
    		sql = "SELECT count(t.id) FROM tweet t, url_entity u where t.id = u.tweet_id and u.web_collection_id = :collectionId and t.creation_date between :start and :end ";
    	} else {
    		sql = "SELECT count(t.id) FROM tweet t, url_entity u where t.id = u.tweet_id and u.web_collection_id is null and t.creation_date between :start and :end ";
    	}
		Query query = em.createNativeQuery(sql);
		if (collectionId != null) {
			query.setParameter("collectionId", collectionId);
		}
		query.setParameter("start", start);
		query.setParameter("end", end);
		countUrl =(Number) query.getSingleResult();

       	return countUrl.longValue();
    }
    
    public Number getTotalProcessedTweets(long lastTweet) {
    	Number countResult;
    	try {
    		Query query = em.createNativeQuery(
    				"SELECT count(t.id) FROM Tweet t WHERE t.processed = true", Tweet.class);
	   		query.setParameter("lastTweet", lastTweet);
    		countResult=(Number) query.getSingleResult();
    	} catch (Exception e) {
    		countResult = 0;
    	}
       	return countResult;
    }
    
    public Tweet getTweet(long id) {
    	  Tweet tweet = em.find(Tweet.class,id);
    	  return tweet; 
    	}
    
    public List<Tweet> getAllTweets() {
    	TypedQuery<Tweet> query = em.createQuery(
            "SELECT t FROM Tweet t ORDER BY t.id desc", Tweet.class);
    	query.setFirstResult(0);
    	query.setMaxResults(10);
    	return query.getResultList();
    }
    
    public List<Tweet> getTweetsByCollection(Long collectionId) {
    	TypedQuery<Tweet> query = em.createQuery(
            "SELECT t FROM Tweet t, UrlEntity u WHERE u.webCollection.id = :collectionId ORDER BY t.id desc", Tweet.class);
    	query.setParameter("collectionId", collectionId);
    	return query.getResultList();
    }
    
    public List<Tweet[]> getAllTweetsExpanded() {
    	TypedQuery<Tweet[]> query = em.createQuery(
            "SELECT t.name,t.text FROM Tweet t join t.urlEntities te ORDER BY t.id", Tweet[].class);
    	return query.getResultList();
    }
    
    public List<Tweet> getUnprocessedTweets(Integer jobNumber) {
    	TypedQuery<Tweet> query = em.createQuery(
                "SELECT t FROM Tweet t WHERE t.processed = false ORDER BY t.id asc", Tweet.class);
    	query.setFirstResult(0);
    	if (jobNumber != null) {
    		query.setMaxResults(jobNumber);
    	}
        return query.getResultList();
    }
    
   public HashMap<Long, Long> getTopTweets(Integer topTweets) {
    	
    	String sql = "SELECT t.id, COUNT(t.id) FROM tweet t ";
     	sql += " GROUP BY t.id ORDER BY COUNT(t.id) desc";
    	
     	Query query = em.createNativeQuery(sql);
    	query.setFirstResult(0);
    	query.setMaxResults(topTweets);
     	List<Object[]> tuples = query.getResultList();
     	HashMap<Long, Long> popularTweets = new HashMap<Long, Long>();
     	for (int i = 0; i < tuples.size() ; i++) {
     		popularTweets.put(((BigInteger)tuples.get(i)[0]).longValue(), ((BigInteger)tuples.get(i)[1]).longValue());
     	}
     	
     	return popularTweets;
    }
    
    @Transactional
    public boolean deleteTweet(long id) {
    	  try{
    	    Tweet tweet = em.find(Tweet.class,id);
    	    em.remove(tweet);
    	  } catch (Exception e) {
    	    return false;
    	  }
    	  return true;
    	}
    
    @Transactional
    public void deleteProcessedTweets() {
	   	Query query = em.createQuery(
	   			"SELECT t FROM Tweet t WHERE t.processed = true", Tweet.class);
	    	query.setFirstResult(0);
	    	query.setMaxResults(50000);
		    List<Tweet> results = query.getResultList();
		    for (Object entity : results) {
		        em.remove(entity);
		    }
    	}
    
    @Transactional
    public void deleteAllTweets() {
	   	Query query = em.createQuery(
	               "SELECT t FROM Tweet t", Tweet.class);
	    	query.setFirstResult(0);
	    	query.setMaxResults(50000);
		    List<Tweet> results = query.getResultList();
		    for (Object entity : results) {
		        em.remove(entity);
		    }
    	}
}
