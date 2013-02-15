package uk.bl.wap.crowdsourcing.dao;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import uk.bl.wap.crowdsourcing.ReportUrlEntity;
import uk.bl.wap.crowdsourcing.Tweet;
import uk.bl.wap.crowdsourcing.UrlEntity;

@Component
public class UrlEntityDao {

	@SuppressWarnings("unused")
	private Logger log = Logger.getLogger(UrlEntityDao.class);

	@PersistenceContext
	private EntityManager em;

	@Transactional
	public void persist(UrlEntity urlEntity) {
		em.merge(urlEntity);
	}

	/*
	 * @Transactional public void deleteAllEntities() { Query query =
	 * em.createQuery( "DELETE FROM UrlEntity", UrlEntity.class); int deleted =
	 * query.executeUpdate(); }
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
	
	@Transactional
	public void deleteFailedEntries() {
		TypedQuery<UrlEntity> query = em.createQuery(
				"SELECT u FROM UrlEntity u WHERE u.errors is not null ", UrlEntity.class);
		query.setFirstResult(0);
		query.setMaxResults(50000);
		List<UrlEntity> results = query.getResultList();
		for (UrlEntity entity : results) {
			Tweet tweet = entity.getTweet();
			em.remove(entity);
			em.remove(tweet);
		}
	}

	@Transactional()
	public void deleteEntry(Long urlEntityId) {
		UrlEntity entity = em.find(UrlEntity.class, urlEntityId);
		log.debug("Deleting UrlEntry with id: " + entity.getId());
		em.remove(entity);
	}

	public List<UrlEntity> getAllUrlEntities() {
		TypedQuery<UrlEntity> query = em
				.createQuery("SELECT u FROM UrlEntity u where urlOriginal is not null ORDER BY u.id desc",
						UrlEntity.class);
		query.setFirstResult(0);
		query.setMaxResults(10);
		return query.getResultList();
	}

	public List<UrlEntity> getUrlEntitiesByCollection(Long collectionId, Integer start, Integer rows, Boolean expanded) {
		
		String sql = null;
		if (collectionId != null) {
			sql = "SELECT u FROM UrlEntity u WHERE u.webCollection.id = :collectionId and expanded = :expanded ";
		} else {
			sql = "SELECT u FROM UrlEntity u WHERE u.webCollection.id is null and expanded = :expanded ";
		}
		TypedQuery<UrlEntity> query = em.createQuery(sql, UrlEntity.class);
		if (collectionId != null) {
			query.setParameter("collectionId", collectionId);
		}
		query.setParameter("expanded", expanded);
		query.setFirstResult(start);
		query.setMaxResults(rows);
		return query.getResultList();
	}
	
	public List<ReportUrlEntity> getAllUrlEntitiesByCollection(
			long webCollectionId, String filterUrl, String filterDomain) {
		String sql = "SELECT u.url_full, u.url_domain, c.name, COUNT(u.id) FROM url_entity u, web_collection c WHERE u.url_original is not null and  c.id = u.web_collection_id and u.web_collection_id = :collectionid";
		if (filterUrl != null && !filterUrl.isEmpty()) {
			if (!filterUrl.contains("%")) {
				filterUrl = "%" + filterUrl + "%";
			}
			sql += " AND u.url_full LIKE :filterUrl";
		}
		if (filterDomain != null && !filterDomain.isEmpty()) {
			if (!filterDomain.contains("%")) {
				filterDomain = "%" + filterDomain + "%";
			}
			sql += " AND u.url_Domain LIKE :filterDomain";
		}
		sql += " GROUP BY u.url_full, u.url_domain, c.name ORDER BY COUNT(u.id) desc";

		Query query = em.createNativeQuery(sql);
		query.setParameter("collectionid", webCollectionId);
		if (filterUrl != null && !filterUrl.isEmpty()) {
			query.setParameter("filterUrl", filterUrl);
		}
		if (filterDomain != null && !filterDomain.isEmpty()) {
			query.setParameter("filterDomain", filterDomain);
		}
		query.setFirstResult(0);
		query.setMaxResults(100);

		List<ReportUrlEntity> rue = new ArrayList<ReportUrlEntity>();
		List<Object[]> entities = query.getResultList();
		for (Object[] object : entities) {
			rue.add(new ReportUrlEntity((String) object[0], (String) object[1],
					(String) object[2], webCollectionId,
					((BigInteger) object[3]).longValue()));
		}
		return rue;
	}

	public List<UrlEntity> getEntitiesByUrl(Long collectionId, String url) {
		TypedQuery<UrlEntity> query = em
				.createQuery(
						"SELECT u FROM UrlEntity u WHERE u.urlOriginal is not null and u.webCollection.id = :collectionid AND u.urlFull = :url ORDER BY u.popularity desc",
						UrlEntity.class);
		query.setParameter("url", url);
		query.setParameter("collectionid", collectionId);
		query.setFirstResult(0);
		query.setMaxResults(100);
		return query.getResultList();
	}
	
	public List<UrlEntity> getEntitiesByUrl(String url) {
		String sql = "SELECT u FROM UrlEntity u WHERE u.urlOriginal is not null AND u.urlOriginal = :url ";
		TypedQuery<UrlEntity> query = em.createQuery(sql, UrlEntity.class);
		query.setParameter("url", url);
		query.setFirstResult(0);
		return query.getResultList();
	}

	public List<UrlEntity> getEntitiesByDomain(Long collectionId, String domain) {
		String sql;
		if (collectionId == null) {
			sql = "SELECT u FROM UrlEntity u WHERE u.urlOriginal is not null and u.webCollection.id is null AND u.urlDomain = :domain ORDER BY u.popularity desc";
		} else {
			sql = "SELECT u FROM UrlEntity u WHERE u.urlOriginal is not null and u.webCollection.id = :collectionid AND u.urlDomain = :domain ORDER BY u.popularity desc";
		}
		TypedQuery<UrlEntity> query = em.createQuery(sql, UrlEntity.class);
		query.setParameter("domain", domain);
		if (collectionId != null)
			query.setParameter("collectionid", collectionId);
		query.setFirstResult(0);
		query.setMaxResults(100);
		return query.getResultList();
	}

	public UrlEntity getTopUrl(Long collectionId) {

		String urlFull = null;
		UrlEntity urlEntity = null;
		Long count = 0L;

		String sql = "SELECT u FROM UrlEntity u WHERE u.urlFull = :urlFull";

		List<Object[]> topUrl = getTopUrlAsObjectArray(collectionId);

		// get the top url within the specified web collection
		if (topUrl != null && topUrl.size() > 0) {
			urlFull = (String) topUrl.get(0)[0];
			count = ((BigInteger) topUrl.get(0)[1]).longValue();
		}

		// retrieve the url entity and set the number of associated tweets as a
		// transient property
		if (count > 0) {
			TypedQuery<UrlEntity> entityQuery = em.createQuery(sql,
					UrlEntity.class);
			entityQuery.setParameter("urlFull", urlFull);
			entityQuery.setFirstResult(0);
			entityQuery.setMaxResults(1);

			urlEntity = entityQuery.getResultList().get(0);

			// fetch the number of tweets associated with this url
			String tweetCountsql = null;
			if (collectionId != null) {
				tweetCountsql = "SELECT COUNT(distinct t.id) FROM tweet t, url_entity u WHERE u.url_original is not null and t.id = u.tweet_id and u.web_collection_id = :collectionid";
			} else {
				tweetCountsql = "SELECT COUNT(distinct t.id) FROM tweet t, url_entity u WHERE u.url_original is not null and t.id = u.tweet_id and u.web_collection_id is null ";
			}
			tweetCountsql += " ORDER BY COUNT(t.id) desc";

			Query query = em.createNativeQuery(tweetCountsql);
			if (collectionId != null) {
				query.setParameter("collectionid", collectionId);
			}
			query.setFirstResult(0);
			query.setMaxResults(1);
			List<BigInteger> tuples = query.getResultList();

			Long tweetCount = (tuples.get(0)).longValue();
			urlEntity.setTotalTweets(tweetCount);

		}

		return urlEntity;

	}

	public List<Object[]> getTopUrlAsObjectArray(Long collectionId) {

		String sql = null;
		if (collectionId != null) {
			sql = "SELECT u.url_full, COUNT(u.id) FROM url_entity u WHERE u.url_original is not null and u.url_full is not null and u.web_collection_id = :collectionid";
		} else {
			sql = "SELECT u.url_full, COUNT(u.id) FROM url_entity u WHERE u.url_original is not null and u.url_full is not null and u.web_collection_id is null ";
		}
		sql += " GROUP BY u.url_full ORDER BY COUNT(u.id) desc";

		Query query = em.createNativeQuery(sql);
		if (collectionId != null) {
			query.setParameter("collectionid", collectionId);
		}
		query.setFirstResult(0);
		query.setMaxResults(1);
		List<Object[]> tuples = query.getResultList();
		return tuples;
	}
	
	/**
	 * Fetch the specified number of top URLs
	 * @param topTweets
	 * @return HashMap of tweetId and url
	 */
	  public Set<String> getTopUrls(Integer topUrls) {
	    	
		  	// fetch the top urls
	    	String sql = "SELECT u.url_original, COUNT(u.url_original) FROM url_entity u ";
	     	sql += " GROUP BY u.url_original ORDER BY COUNT(u.url_original) desc";
	    	
	     	Query query = em.createNativeQuery(sql);
	    	query.setFirstResult(0);
	    	query.setMaxResults(topUrls);
	    	List<Object[]> urls = query.getResultList();
	    	
	    	// fetch the tweet ids associated with the url
	     	Set<String> popUrls = new HashSet<String>();
	     	for (int i=0; i<urls.size(); i++) {
	     		String url = (String)(urls.get(i)[0]);
     			popUrls.add(url);
	     	}
	     	
	     	return popUrls;
	    }

	  /**
	   * Returns the top expanded URLs 
	   * @param collectionId The identifier of the require Web Collection
	   * @param filterUrl A URL query filter
	   * @param filterDomain A domain query filter
	   * @param start The position of the initial result
	   * @param rows The number of results to return
	   * @return
	   */
	public List<Object[]> getTopUrl(Long collectionId, String filterUrl,
			String filterDomain, Integer start, Integer rows) {

		String sql = "SELECT url_full, COUNT(url_full) FROM url_entity WHERE web_collection_id = :collectionid";

		if (filterUrl != null && !filterUrl.isEmpty()) {
			if (!filterUrl.contains("%")) {
				filterUrl = "%" + filterUrl + "%";
			}
			sql += " AND url_full LIKE :filterUrl";
		}
		if (filterDomain != null && !filterDomain.isEmpty()) {
			if (!filterDomain.contains("%")) {
				filterDomain = "%" + filterDomain + "%";
			}
			sql += " AND url_domain LIKE :filterDomain";
		}
		sql += " GROUP BY url_full ORDER BY COUNT(url_full) desc";

		Query query = em.createNativeQuery(sql);
		query.setParameter("collectionid", collectionId);
		if (filterUrl != null && !filterUrl.isEmpty()) {
			query.setParameter("filterUrl", filterUrl);
		}
		if (filterDomain != null && !filterDomain.isEmpty()) {
			query.setParameter("filterDomain", filterDomain);
		}
		if (start != null)
			query.setFirstResult(start);
		if (rows != null)
			query.setMaxResults(rows);
		return query.getResultList();
	}

	public Number getTotalTweets(long collectionId, String filterUrl,
			String filterDomain) {
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
			countResult = (Number) query.getSingleResult();

		} catch (Exception e) {
			countResult = 0;
		}
		return countResult;
	}

	/**
	 * Total expanded urls
	 * 
	 * @param collectionId
	 * @param filterUrl
	 * @param filterDomain
	 * @return
	 */
	public Number getTotalURL(Long collectionId, String filterUrl,
			String filterDomain) {
		Number countResult;
		String sql = null;
		if (collectionId != null) {
			sql = "SELECT COUNT(u.urlFull) FROM UrlEntity u WHERE u.urlOriginal is not null and u.webCollection.id = :collectionid and u.expanded = true  ";
		} else {
			sql = "SELECT COUNT(u.urlFull) FROM UrlEntity u WHERE u.urlOriginal is not null and u.webCollection.id is null and u.expanded = true  ";
		}
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
		if (collectionId != null) {
			query.setParameter("collectionid", collectionId);
		}
		if (filterUrl != null && !filterUrl.isEmpty()) {
			query.setParameter("filterUrl", filterUrl);
		}
		if (filterDomain != null && !filterDomain.isEmpty()) {
			query.setParameter("filterDomain", filterDomain);
		}
		countResult = (Number) query.getSingleResult();
		return countResult;
	}

	/**
	 * Total original urls
	 * 
	 * @param collectionId
	 * @param filterUrl
	 * @param filterDomain
	 * @return
	 */
	public Number getTotalOriginalURL(Long collectionId, String filterUrl,
			String filterDomain) {
		Number countResult;
		String sql = null;
		if (collectionId != null) {
			sql = "SELECT COUNT(u.urlOriginal) FROM UrlEntity u WHERE u.webCollection.id = :collectionid ";
		} else {
			sql = "SELECT COUNT(u.urlOriginal) FROM UrlEntity u WHERE u.webCollection.id is null ";
		}
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
		if (collectionId != null) {
			query.setParameter("collectionid", collectionId);
		}
		if (filterUrl != null && !filterUrl.isEmpty()) {
			query.setParameter("filterUrl", filterUrl);
		}
		if (filterDomain != null && !filterDomain.isEmpty()) {
			query.setParameter("filterDomain", filterDomain);
		}
		countResult = (Number) query.getSingleResult();
		return countResult;
	}

	public Number getTotalDomain(long collectionId, String filterUrl,
			String filterDomain) {
		Number countResult;
		try {
			String sql = "SELECT COUNT(DISTINCT u.urlDomain) FROM UrlEntity u WHERE u.urlOriginal is not null and u.collectionId = :collectionid";
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
			countResult = (Number) query.getSingleResult();
		} catch (Exception e) {
			countResult = 0;
		}
		return countResult;
	}

	/**
	 * Fetches URL domains and the number of URLs in the domain sorted in reverse order
	 * @param collectionId The identifier of the required Web Collection
	 * @param filterUrl A URL query filter
	 * @param filterDomain A URL domain query filter
	 * @param start The position of the initial record
	 * @param rows The number of rows to return
	 * @return
	 */
	public List<Object[]> getTopDomain(Long collectionId, String filterUrl,
			String filterDomain, Integer start, Integer rows) {
		String sql = "SELECT u.url_domain, COUNT(u.id) FROM url_entity u WHERE u.url_original is not null and u.web_collection_id = :collectionid";
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
			sql += " AND u.url_domain LIKE :filterDomain";
		}
		sql += " GROUP BY u.url_domain ORDER BY COUNT(u.id) desc";
		Query query = em.createNativeQuery(sql);
		query.setParameter("collectionid", collectionId);
		if (filterUrl != null && !filterUrl.isEmpty()) {
			query.setParameter("filterUrl", filterUrl);
		}
		if (filterDomain != null && !filterDomain.isEmpty()) {
			query.setParameter("filterDomain", filterDomain);
		}
		if (start != null)
			query.setFirstResult(start);
		if (rows != null)
			query.setMaxResults(rows);
		return query.getResultList();
	}

	/**
	 * Returns the most popular expanded URL for a specific <code>WebCollection</code>
	 * @param collectionId The identifier of the required <code>WebCollection</code>
	 * @param filterUrl A URL SQL query filter
	 * @param filterDomain A domain SQL query filter
	 * @param start The position of the initial result
	 * @param rows The number of results to return
	 * @return
	 */
	public List<Object[]> getTopPopularity(Long collectionId, String filterUrl,
			String filterDomain, Integer start, Integer rows) {
		String sql = "SELECT u.url_full, max(u.popularity) FROM url_entity u WHERE u.web_collection_id = :collectionid AND u.popularity > 0";
		if (filterUrl != null && !filterUrl.isEmpty()) {
			if (!filterUrl.contains("%")) {
				filterUrl = "%" + filterUrl + "%";
			}
			sql += " AND u.url_full LIKE :filterUrl";
		}
		if (filterDomain != null && !filterDomain.isEmpty()) {
			if (!filterDomain.contains("%")) {
				filterDomain = "%" + filterDomain + "%";
			}
			sql += " AND u.url_domain LIKE :filterDomain";
		}
		sql += " GROUP BY u.url_full ORDER BY COUNT(u) desc";
		Query query = em.createNativeQuery(sql);
		query.setParameter("collectionid", collectionId);
		if (filterUrl != null && !filterUrl.isEmpty()) {
			query.setParameter("filterUrl", filterUrl);
		}
		if (filterDomain != null && !filterDomain.isEmpty()) {
			query.setParameter("filterDomain", filterDomain);
		}
		if (start != null)
			query.setFirstResult(start);
		if (rows != null)
			query.setMaxResults(rows);
		return query.getResultList();
	}

	public Number getLastTweet() {
		Number countResult;
		try {
			Query query = em
					.createQuery("SELECT max(u.tweetId) FROM UrlEntity u");
			countResult = (Number) query.getSingleResult();
		} catch (Exception e) {
			countResult = 0;
		}
		return countResult;

	}

	public Number getTotalEntities() {
		Number countResult;
		try {
			Query query = em.createQuery("SELECT count(u.id) FROM UrlEntity u where u.urlOriginal is not null ");
			countResult = (Number) query.getSingleResult();
		} catch (Exception e) {
			countResult = 0;
		}
		return countResult;
	}

	public Number getTotalUnprocessedEntities() {
		Number countResult;
		try {
			String sql = "SELECT count(u.id) FROM UrlEntity u where u.processed = false u.urlOriginal is not null";
			Query query = em.createQuery(sql);
			countResult = (Number) query.getSingleResult();
		} catch (Exception e) {
			e.printStackTrace();
			countResult = 0;
		}
		return countResult;
	}

	public Number getTotalProcessedEntities() {
		Number countResult;
		String sql = "SELECT count(u.id) FROM UrlEntity u where u.processed = true and u.urlOriginal is not null";
		Query query = em.createQuery(sql);
		countResult = (Number) query.getSingleResult();

		return countResult;
	}

	public List<UrlEntity> getEntitiesByTopUrl(Long collectionId) {
		TypedQuery<UrlEntity> query = em
				.createQuery(
						"SELECT u FROM UrlEntity u WHERE u.urlOriginal is not null and u.collectionId = :collectionid ORDER BY u.popularity desc",
						UrlEntity.class);
		query.setParameter("collectionid", collectionId);
		query.setFirstResult(0);
		query.setMaxResults(100);
		return query.getResultList();
	}

	public Number getTotalEntitiesFailedByCollection(Long collectionId) {
		Number countResult;
		String sql = null;
		if (collectionId != null) {
			sql = "SELECT count(u.id) FROM UrlEntity u WHERE u.urlOriginal is not null and u.webCollection.id = :collectionId and errors is not null ";
		} else {
			sql = "SELECT count(u.id) FROM UrlEntity u WHERE u.urlOriginal is not null and u.webCollection.id is null and errors is not null ";
		}
		Query query = em.createQuery(sql);
		if (collectionId != null) {
			query.setParameter("collectionId", collectionId);
		}
		countResult = (Number) query.getSingleResult();
		return countResult;
	}
	
	public List<UrlEntity> getAllUrlEntitiesFailed() {
		Number countResult;
		String sql = "SELECT u FROM UrlEntity u WHERE urlOriginal is not null and errors is not null ";
		Query query = em.createQuery(sql);
		return query.getResultList();
	}
	
	public List<UrlEntity> getUrlEntitiesFailedByCollection(Long collectionId, Integer start, Integer rows) {
		Number countResult;
		String sql = "SELECT u FROM UrlEntity u WHERE u.webCollection.id = :collectionId and urlOriginal is not null and errors is not null ";
		Query query = em.createQuery(sql);
		query.setParameter("collectionId", collectionId);
		query.setFirstResult(start);
		query.setMaxResults(rows);
		return query.getResultList();
	}
}