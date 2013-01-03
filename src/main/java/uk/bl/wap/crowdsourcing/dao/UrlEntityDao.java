package uk.bl.wap.crowdsourcing.dao;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import uk.bl.wap.crowdsourcing.ReportUrlEntity;
import uk.bl.wap.crowdsourcing.UrlEntity;

@Component
public class UrlEntityDao {
	
	@SuppressWarnings("unused")
	private Logger log = Logger.getLogger("logtest");
	
    @PersistenceContext 
    private EntityManager em;
    
    @Transactional
    public void persist(UrlEntity urlEntity) {
        em.persist(urlEntity);
    }
    /*
    @Transactional
    public void deleteAllEntities() {
    	Query query = em.createQuery(
                "DELETE FROM UrlEntity", UrlEntity.class);
    	int deleted = query.executeUpdate();
    }
    */
    @Transactional
    public void deleteEntries() {
    	TypedQuery<UrlEntity> query = em.createQuery(
                "SELECT u FROM UrlEntity u", UrlEntity.class);
    	query.setFirstResult(0);
    	query.setMaxResults(50000);
	    List<UrlEntity> results = query.getResultList();
	    for (Object entity : results) {
	        em.remove(entity);
	    }
    }
    
    public List<UrlEntity> getAllUrlEntities() {
    	TypedQuery<UrlEntity> query = em.createQuery(
            "SELECT u FROM UrlEntity u ORDER BY u.id desc", UrlEntity.class);
    	query.setFirstResult(0);
    	query.setMaxResults(10);
    	return query.getResultList();
    }
    
    public List<ReportUrlEntity> getAllUrlEntitiesByCollection(long collectionId,String filterUrl,String filterDomain) {
    	String sql = "SELECT u.urlFull,u.urlDomain,u.collectionName, COUNT(u.id) FROM UrlEntity u WHERE u.collectionId = :collectionid";
    	if (filterUrl != null && !filterUrl.isEmpty()) {
    		if (!filterUrl.contains("%")) {
    			filterUrl = "%" + filterUrl + "%";
    		}
    		sql += " AND u.urlFull LIKE :filterUrl";
    	}
    	if (filterDomain != null && !filterDomain.isEmpty()) {
    		if (!filterDomain.contains("%")) {
    			filterDomain = "%" + filterDomain + "%";
    		}
    		sql += " AND u.urlDomain LIKE :filterDomain";
    	}
    	sql += " GROUP BY u.urlFull, u.urlDomain, u.collectionName ORDER BY COUNT(u.id) desc";
    	
    	TypedQuery<Object[]> query = em.createQuery(sql, Object[].class);
    	query.setParameter("collectionid", collectionId);
    	if (filterUrl != null && !filterUrl.isEmpty()) {
    		query.setParameter("filterUrl", filterUrl);
    	}
    	if (filterDomain != null && !filterDomain.isEmpty()) {
    		query.setParameter("filterDomain", filterDomain);
    	}
    	query.setFirstResult(0);
    	query.setMaxResults(100);

    	List<ReportUrlEntity> rue = new ArrayList<ReportUrlEntity>();
    	for (Object[] object : query.getResultList()) {
    		rue.add(new ReportUrlEntity((String) object[0],(String) object[1],(String) object[2],collectionId,(Long) object[3]));
    	}
    	return rue;
    }
    
    public List<UrlEntity> getEntitiesByUrl(Long collectionId,String url) {
    	TypedQuery<UrlEntity> query = em.createQuery(
            "SELECT u FROM UrlEntity u WHERE u.collectionId = :collectionid AND u.urlFull = :url ORDER BY u.popularity desc", UrlEntity.class);
    	query.setParameter("url", url);
    	query.setParameter("collectionid", collectionId);
    	query.setFirstResult(0);
    	query.setMaxResults(100);
    	return query.getResultList();
    }
    
    public List<UrlEntity> getEntitiesByDomain(Long collectionId,String domain) {
    	TypedQuery<UrlEntity> query = em.createQuery(
            "SELECT u FROM UrlEntity u WHERE u.collectionId = :collectionid AND u.urlDomain = :domain ORDER BY u.popularity desc", UrlEntity.class);
    	query.setParameter("domain", domain);
    	query.setParameter("collectionid", collectionId);
    	query.setFirstResult(0);
    	query.setMaxResults(100);
    	return query.getResultList();
    }
    
    public List<UrlEntity[]> getTopUrl(long collectionId,String filterUrl,String filterDomain,int start,int rows) {
    	String sql = "SELECT u.urlFull, COUNT(u.id) FROM UrlEntity u WHERE u.collectionId = :collectionid";
    	if (filterUrl != null && !filterUrl.isEmpty()) {
    		if (!filterUrl.contains("%")) {
    			filterUrl = "%" + filterUrl + "%";
    		}
    		sql += " AND u.urlFull LIKE :filterUrl";
    	}
    	if (filterDomain != null && !filterDomain.isEmpty()) {
    		if (!filterDomain.contains("%")) {
    			filterDomain = "%" + filterDomain + "%";
    		}
    		sql += " AND u.urlDomain LIKE :filterDomain";
    	}
    	sql += " GROUP BY u.urlFull ORDER BY COUNT(u.id) desc";
    	
    	TypedQuery<UrlEntity[]> query = em.createQuery(sql, UrlEntity[].class);
    	query.setParameter("collectionid", collectionId);
    	if (filterUrl != null && !filterUrl.isEmpty()) {
    		query.setParameter("filterUrl", filterUrl);
    	}
    	if (filterDomain != null && !filterDomain.isEmpty()) {
    		query.setParameter("filterDomain", filterDomain);
    	}
    	query.setFirstResult(start);
    	query.setMaxResults(rows);
    	return query.getResultList();
    }
    
    public Number getTotalTweets(long collectionId,String filterUrl,String filterDomain) {
    	Number countResult;
    	try {
    		String sql = "SELECT COUNT(DISTINCT u.tweetId) FROM UrlEntity u WHERE u.collectionId = :collectionid";
        	if (filterUrl != null && !filterUrl.isEmpty()) {
        		if (!filterUrl.contains("%")) {
        			filterUrl = "%" + filterUrl + "%";
        		}
        		sql += " AND u.urlFull LIKE :filterUrl";
        	}
        	if (filterDomain != null && !filterDomain.isEmpty()) {
        		if (!filterDomain.contains("%")) {
        			filterDomain = "%" + filterDomain + "%";
        		}
        		sql += " AND u.urlDomain LIKE :filterDomain";
        	}

    		Query query = em.createQuery(sql);
    		query.setParameter("collectionid", collectionId);
    		if (filterUrl != null && !filterUrl.isEmpty()) {
        		query.setParameter("filterUrl", filterUrl);
        	}
        	if (filterDomain != null && !filterDomain.isEmpty()) {
        		query.setParameter("filterDomain", filterDomain);
        	}
        	countResult=(Number) query.getSingleResult();
        	
    	} catch (Exception e)  {
    		countResult = 0;
    	}
    	return countResult;
    }
    
    public Number getTotalURL(long collectionId,String filterUrl,String filterDomain) {
    	Number countResult;
    	try {
    		String sql = "SELECT COUNT(DISTINCT u.urlFull) FROM UrlEntity u WHERE u.collectionId = :collectionid";
        	if (filterUrl != null && !filterUrl.isEmpty()) {
        		if (!filterUrl.contains("%")) {
        			filterUrl = "%" + filterUrl + "%";
        		}
        		sql += " AND u.urlFull LIKE :filterUrl";
        	}
        	if (filterDomain != null && !filterDomain.isEmpty()) {
        		if (!filterDomain.contains("%")) {
        			filterDomain = "%" + filterDomain + "%";
        		}
        		sql += " AND u.urlDomain LIKE :filterDomain";
        	}

    		Query query = em.createQuery(sql);
    		query.setParameter("collectionid", collectionId);
    		if (filterUrl != null && !filterUrl.isEmpty()) {
        		query.setParameter("filterUrl", filterUrl);
        	}
        	if (filterDomain != null && !filterDomain.isEmpty()) {
        		query.setParameter("filterDomain", filterDomain);
        	}
        	countResult=(Number) query.getSingleResult();
    	} catch (Exception e)  {
    		countResult = 0;
    	}
    	return countResult;
    }
    
    public Number getTotalDomain(long collectionId,String filterUrl,String filterDomain) {
    	Number countResult;
    	try {
    		String sql = "SELECT COUNT(DISTINCT u.urlDomain) FROM UrlEntity u WHERE u.collectionId = :collectionid";
        	if (filterUrl != null && !filterUrl.isEmpty()) {
        		if (!filterUrl.contains("%")) {
        			filterUrl = "%" + filterUrl + "%";
        		}
        		sql += " AND u.urlFull LIKE :filterUrl";
        	}
        	if (filterDomain != null && !filterDomain.isEmpty()) {
        		if (!filterDomain.contains("%")) {
        			filterDomain = "%" + filterDomain + "%";
        		}
        		sql += " AND u.urlDomain LIKE :filterDomain";
        	}

    		Query query = em.createQuery(sql);
    		query.setParameter("collectionid", collectionId);
    		if (filterUrl != null && !filterUrl.isEmpty()) {
        		query.setParameter("filterUrl", filterUrl);
        	}
        	if (filterDomain != null && !filterDomain.isEmpty()) {
        		query.setParameter("filterDomain", filterDomain);
        	}
        	countResult=(Number) query.getSingleResult();
    	} catch (Exception e)  {
    		countResult = 0;
    	}
    	return countResult;
    }
    
    public List<Object[]> getTopDomain(long collectionId,String filterUrl,String filterDomain,int start,int rows) {
    	String sql = "SELECT u.urlDomain, COUNT(u.id) FROM UrlEntity u WHERE u.collectionId = :collectionid";
    	if (filterUrl != null && !filterUrl.isEmpty()) {
    		if (!filterUrl.contains("%")) {
    			filterUrl = "%" + filterUrl + "%";
    		}
    		sql += " AND u.urlFull LIKE :filterUrl";
    	}
    	if (filterDomain != null && !filterDomain.isEmpty()) {
    		if (!filterDomain.contains("%")) {
    			filterDomain = "%" + filterDomain + "%";
    		}
    		sql += " AND u.urlDomain LIKE :filterDomain";
    	}
    	sql += " GROUP BY u.urlDomain ORDER BY COUNT(u.id) desc";
    	TypedQuery<Object[]> query = em.createQuery(sql, Object[].class);
    	query.setParameter("collectionid", collectionId);
    	if (filterUrl != null && !filterUrl.isEmpty()) {
    		query.setParameter("filterUrl", filterUrl);
    	}
    	if (filterDomain != null && !filterDomain.isEmpty()) {
    		query.setParameter("filterDomain", filterDomain);
    	}
    	query.setFirstResult(start);
    	query.setMaxResults(rows);
    	return query.getResultList();
    }
    
    public List<Object[]> getTopPopularity(long collectionId,String filterUrl,String filterDomain,int start,int rows) {
    	String sql = "SELECT u.urlFull, max(u.popularity) FROM UrlEntity u WHERE u.collectionId = :collectionid AND u.popularity > 0";
    	if (filterUrl != null && !filterUrl.isEmpty()) {
    		if (!filterUrl.contains("%")) {
    			filterUrl = "%" + filterUrl + "%";
    		}
    		sql += " AND u.urlFull LIKE :filterUrl";
    	}
    	if (filterDomain != null && !filterDomain.isEmpty()) {
    		if (!filterDomain.contains("%")) {
    			filterDomain = "%" + filterDomain + "%";
    		}
    		sql += " AND u.urlDomain LIKE :filterDomain";
    	}
    	sql += " GROUP BY u.urlFull ORDER BY COUNT(u) desc";
    	TypedQuery<Object[]> query = em.createQuery(sql, Object[].class);
    	query.setParameter("collectionid", collectionId);
    	if (filterUrl != null && !filterUrl.isEmpty()) {
    		query.setParameter("filterUrl", filterUrl);
    	}
    	if (filterDomain != null && !filterDomain.isEmpty()) {
    		query.setParameter("filterDomain", filterDomain);
    	}
    	query.setFirstResult(start);
    	query.setMaxResults(rows);
    	return query.getResultList();
    }

    public Number getLastTweet() {
    	Number countResult;
    	try {
    		Query query = em.createQuery("SELECT max(u.tweetId) FROM UrlEntity u");
        	countResult=(Number) query.getSingleResult();
    	} catch (Exception e)  {
    		countResult = 0;
    	}
    	return countResult;
    	
    }
    
    public Number getTotalUnprocessed() {
    	Number countResult;
    	try {
	    	Query query = em.createQuery("SELECT max(u.tweetId) FROM UrlEntity u");
	    	long lastTweetId = (Long) query.getSingleResult();
	    	
	    	Query query2 = em.createQuery("SELECT COUNT(t.id) FROM Tweet t WHERE t.id > :lastTweetId");
	    	query2.setParameter("lastTweetId", lastTweetId);
	    	countResult=(Number) query2.getSingleResult();
    	} catch (Exception e)  {
    		countResult = 0;
    	}
    	return countResult;
    }
    
    public Number getTotalEntities() {
    	Number countResult;
    	try {
    	Query query = em.createQuery("SELECT count(u.id) FROM UrlEntity u");
    		countResult=(Number) query.getSingleResult();
    	} catch (Exception e)  {
    		countResult = 0;
    	}
    	return countResult;
    }
    
    
}