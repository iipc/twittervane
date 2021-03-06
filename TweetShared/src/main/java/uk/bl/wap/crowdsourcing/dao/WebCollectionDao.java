package uk.bl.wap.crowdsourcing.dao;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import uk.bl.wap.crowdsourcing.SearchTerm;
import uk.bl.wap.crowdsourcing.WebCollection;

@Component
public class WebCollectionDao {
	// Injected database connection:
    @PersistenceContext private EntityManager em;
 
    // the display name allocated to the "UNKNOWN" bucket web collection (when a tweet cant be resolved to a web collection based on a search term)
    private String unknownCollectionName;
    
    // Stores a new collection:
    @Transactional
    public void persist(WebCollection webCollection) {
        em.merge(webCollection);
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
    public boolean updateCollection(Long id,String name,String startDate,String endDate ) {
    	  try{
    	    WebCollection collection = em.find(WebCollection.class,id);
    	    collection.setName(name); 
    	    SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MMM-yyyy"); 
    		Date convertedDate;
    		try {
    			convertedDate = dateFormat.parse(startDate);
    			collection.setStartDate(convertedDate);
    		} catch (ParseException e) {
    			e.printStackTrace();
    		} 
    		try {
    			convertedDate = dateFormat.parse(endDate);
    			collection.setEndDate(convertedDate);
    		} catch (ParseException e) {
    			e.printStackTrace();
    		} 
    	  } catch (Exception e) {
    	    return false;
    	  }
    	  return true;
    	}
    
    @Transactional
    public boolean deleteWebCollection(Long id) {
    	  try{
    	    WebCollection collection = em.find(WebCollection.class,id);
    	    em.remove(collection);
    	  } catch (Exception e) {
    	    return false;
    	  }
    	  return true;
    	}
    
    @Transactional
    public void addTerm(Long id,SearchTerm searchTerm) {
  	  try{
  	    	WebCollection collection = em.find(WebCollection.class,id);
  	    	searchTerm.setWebCollection(collection);
  	    	collection.getSearchTerms().add(searchTerm);
  	    	em.persist(collection);
  	  } catch (Exception e) {
  		e.printStackTrace();
  	  }
    }
    
    @Transactional
    public boolean updateTerms(int id,String[] strArray) {

    	SearchTerm searchterm = null;
    	  try{
    	    	WebCollection collection = em.find(WebCollection.class,id);
    	    	collection.getSearchTerms().clear();
    	    	for (int i=0;i<strArray.length;i++) {
    	    			searchterm = em.find(SearchTerm.class, (int) Integer.parseInt(strArray[i])); 
    	    			collection.getSearchTerms().add(searchterm);
    	    	}
    	  } catch (Exception e) {
    	    return false;
    	  }
    	  return true;
    	}
 

    public List<WebCollection> getAllCollections() {
    	TypedQuery<WebCollection> query = em.createQuery(
            "SELECT c FROM WebCollection c ORDER BY c.id", WebCollection.class);
    	List<WebCollection> webCollections = query.getResultList();
    	return webCollections;
    }
    
    public List<WebCollection> getAllCollectionsForStream() {
    	TypedQuery<WebCollection> query = em.createQuery(
            " SELECT wc FROM WebCollection wc WHERE CURRENT_TIMESTAMP BETWEEN wc.startDate AND wc.endDate ", WebCollection.class);
    	return query.getResultList();
    }
    
    public WebCollection getWebCollection(Long collectionId) {
    	TypedQuery<WebCollection> query = em.createQuery(
            "SELECT c FROM WebCollection c WHERE c.id = :collectionId ORDER BY c.id", WebCollection.class);
    	query.setParameter("collectionId", collectionId);
    	List<WebCollection> webCollections = query.getResultList();

    	return webCollections.get(0);
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
    
    public WebCollection getCollectionById(long id) {
    	WebCollection collection =  em.find(WebCollection.class, id);
    	return collection;
    }

	/**
	 * @return the unknownCollection
	 */
	public String getUnknownCollectionName() {
		return unknownCollectionName;
	}

	/**
	 * @param unknownCollection the unknownCollection to set
	 */
	public void setUnknownCollectionName(String unknownCollectionName) {
		this.unknownCollectionName = unknownCollectionName;
	}
	
	public WebCollection getUnknownCollection() {
		return getCollectionByName(unknownCollectionName);
	}
	
    public WebCollection getCollectionByName(String webCollectionName) {
       	TypedQuery<WebCollection> query = em.createQuery(
                "SELECT c FROM WebCollection c WHERE name = :webCollectionName", WebCollection.class);
       	query.setParameter("webCollectionName", webCollectionName);
       	query.setMaxResults(1);
        List<WebCollection> webCollections = query.getResultList();

    	return webCollections.get(0);
    }
}
