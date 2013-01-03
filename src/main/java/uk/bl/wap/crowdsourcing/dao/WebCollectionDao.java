package uk.bl.wap.crowdsourcing.dao;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import uk.bl.wap.crowdsourcing.SearchTerm;
import uk.bl.wap.crowdsourcing.WebCollection;

@Component
public class WebCollectionDao {
	// Injected database connection:
    @PersistenceContext private EntityManager em;
 
    // Stores a new collection:
    @Transactional
    public void persist(WebCollection webCollection) {
        em.persist(webCollection);
    }
    
    @Transactional
    public boolean updateName(int id,String name) {
    	  try{
    	    WebCollection collection = em.find(WebCollection.class,id);
    	    collection.setName(name); 
    	  } catch (Exception e) {
    	    return false;
    	  }
    	  return true;
    	}
    
    @Transactional
    public boolean updateCollection(int id,String name,String startDate,String endDate ) {
    	  try{
    	    WebCollection collection = em.find(WebCollection.class,id);
    	    collection.setName(name); 
    	    SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MMM-yyyy"); 
    		Date convertedDate;
    		try {
    			convertedDate = dateFormat.parse(startDate);
    			collection.setStartDate(convertedDate);
    		} catch (ParseException e) {
    			//log.severe("Error converting date: " + e.getMessage());
    			//System.err.print(e.getMessage());
    		} 
    		try {
    			convertedDate = dateFormat.parse(endDate);
    			collection.setEndDate(convertedDate);
    		} catch (ParseException e) {
    		//	log.severe("Error converting date: " + e.getMessage());
    		//	System.err.print(e.getMessage());
    		} 
    	  } catch (Exception e) {
    	    return false;
    	  }
    	  return true;
    	}
    
    @Transactional
    public boolean deleteWebCollection(int id) {
    	  try{
    	    WebCollection collection = em.find(WebCollection.class,id);
    	    em.remove(collection);
    	  } catch (Exception e) {
    	    return false;
    	  }
    	  return true;
    	}
    
    @Transactional
    public boolean addTerm(int id,SearchTerm searchterm) {
  	  try{
  	    	WebCollection collection = em.find(WebCollection.class,id);
  	    	collection.getSearchterms().add(searchterm);
  	  } catch (Exception e) {
  	    return false;
  	  }
  	  return true;
    }
    
    @Transactional
    public boolean updateTerms(int id,String[] strArray) {

    	SearchTerm searchterm = null;
    	  try{
    	    	WebCollection collection = em.find(WebCollection.class,id);
    	    	collection.getSearchterms().clear();
    	    	for (int i=0;i<strArray.length;i++) {
    	    			searchterm = em.find(SearchTerm.class, (int) Integer.parseInt(strArray[i])); 
    	    			collection.getSearchterms().add(searchterm);
    	    	}
    	  } catch (Exception e) {
    	    return false;
    	  }
    	  return true;
    	}
 

    public List<WebCollection> getAllCollections() {
    	TypedQuery<WebCollection> query = em.createQuery(
            "SELECT c FROM WebCollection c ORDER BY c.id", WebCollection.class);
    	return query.getResultList();
    }
    
    public List<WebCollection> getAllCollectionsForStream() {
    	TypedQuery<WebCollection> query = em.createQuery(
            " SELECT wc FROM WebCollection wc WHERE wc.startDate < CURRENT_TIMESTAMP AND wc.endDate >  CURRENT_TIMESTAMP", WebCollection.class);
    	return query.getResultList();
    }
    
    
   
    
    public List<WebCollection[]> getCollection(int id) {
    	TypedQuery<WebCollection[]> query = em.createQuery(
            "SELECT c.name,c.id,c.creationDate,c.startDate,c.endDate FROM WebCollection c WHERE c.id = :id", WebCollection[].class).setParameter("id", id);
    	return query.getResultList();
    }
    
    public List<WebCollection[]> getAllCollectionsExpanded() {
    	TypedQuery<WebCollection[]> query = em.createQuery(
            "SELECT c.name,c.id,c.creationDate,c.startDate,c.endDate FROM WebCollection c ORDER BY c.id", WebCollection[].class);
    	return query.getResultList();
    }
    
    public WebCollection getCollectionById(int id) {
    	WebCollection collection =  em.find(WebCollection.class, id);
    	return collection;
    }
}
