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
import uk.bl.wap.crowdsourcing.UrlEntityFailed;

@Component
public class UrlEntityFailedDao {
	
	@SuppressWarnings("unused")
	private Logger log = Logger.getLogger("logtest");
	
    @PersistenceContext 
    private EntityManager em;
    
    @Transactional
    public void persist(UrlEntityFailed urlEntityFailed) {
        em.persist(urlEntityFailed);
    }

    @Transactional
    public void deleteFailedEntries() {
    	TypedQuery<UrlEntityFailed> query = em.createQuery(
                "SELECT u FROM UrlEntityFailed u", UrlEntityFailed.class);
    	query.setFirstResult(0);
    	query.setMaxResults(50000);
	    List<UrlEntityFailed> results = query.getResultList();
	    for (Object entity : results) {
	        em.remove(entity);
	    }
    }
    
    public List<UrlEntityFailed> getAllUrlEntitiesFailed() {
    	TypedQuery<UrlEntityFailed> query = em.createQuery(
            "SELECT u FROM UrlEntityFailed u ORDER BY u.id desc", UrlEntityFailed.class);
    	query.setFirstResult(0);
    	query.setMaxResults(100);
    	return query.getResultList();
    }
    
    public List<UrlEntityFailed> getEntitiesFailedByUrl(Long collectionId,String url) {
    	TypedQuery<UrlEntityFailed> query = em.createQuery(
            "SELECT u FROM UrlEntityFailed u WHERE u.collectionId = :collectionid AND u.urlFull = :url ORDER BY u.popularity desc", UrlEntityFailed.class);
    	query.setParameter("url", url);
    	query.setParameter("collectionid", collectionId);
    	query.setFirstResult(0);
    	query.setMaxResults(100);
    	return query.getResultList();
    }
     
    public Number getTotalFailedEntities() {
    	Number countResult;
    	try {
    	Query query = em.createQuery("SELECT count(u.id) FROM UrlEntityFailed u");
    		countResult=(Number) query.getSingleResult();
    	} catch (Exception e)  {
    		countResult = 0;
    	}
    	return countResult;
    }
    
}