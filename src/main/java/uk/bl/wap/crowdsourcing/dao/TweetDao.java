package uk.bl.wap.crowdsourcing.dao;

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
        em.persist(tweet);
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
    
    public Number getTotalProcessedTweets(long lastTweet) {
    	Number countResult;
    	try {
    		Query query = em.createQuery(
    				"SELECT count(t.id) FROM Tweet t WHERE t.id <= :lastTweet", Tweet.class);
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
    
    public List<Tweet[]> getAllTweetsExpanded() {
    	TypedQuery<Tweet[]> query = em.createQuery(
            "SELECT t.name,t.text FROM Tweet t join t.urlEntities te ORDER BY t.id", Tweet[].class);
    	return query.getResultList();
    }
    
    public List<Tweet> getUnprocessedTweets(int jobNumber, long lastTweet) {
    	TypedQuery<Tweet> query = em.createQuery(
                "SELECT t FROM Tweet t WHERE t.id > :lastTweet ORDER BY t.id asc", Tweet.class);
    	query.setParameter("lastTweet", lastTweet);
    	query.setFirstResult(0);
    	query.setMaxResults(jobNumber);
        return query.getResultList();
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
    public void deleteProcessedTweets(long lastTweet) {
	   	Query query = em.createQuery(
	   			"SELECT t FROM Tweet t WHERE t.id <= :lastTweet ORDER BY t.id asc", Tweet.class);
	   		query.setParameter("lastTweet", lastTweet);
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
