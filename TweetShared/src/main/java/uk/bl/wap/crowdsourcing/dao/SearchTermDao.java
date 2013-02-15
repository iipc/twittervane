package uk.bl.wap.crowdsourcing.dao;

import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import uk.bl.wap.crowdsourcing.SearchTerm;

@Component
public class SearchTermDao {

    @PersistenceContext private EntityManager em;
 
    @Transactional
    public void persist(SearchTerm searchterm) {
        em.persist(searchterm);
    }

    public List<SearchTerm> getAllSearchTerms() {
    	TypedQuery<SearchTerm> query = em.createQuery(
            "SELECT st FROM SearchTerm st ORDER BY st.id", SearchTerm.class);
    	return query.getResultList();
    }

    public List<SearchTerm[]> getAllSearchTermsExpanded() {
    	TypedQuery<SearchTerm[]> query = em.createQuery(
            "SELECT st.id,st.term FROM SearchTerm st ORDER BY st.id", SearchTerm[].class);
    	return query.getResultList();
    }
    
	public SearchTerm getSearchTermByid(Long id) {
		SearchTerm searchterm = em.find(SearchTerm.class, id);
		return searchterm;
	}
	
	@Transactional
	public boolean updateName(Long id, String term) {
		try {
			SearchTerm searchTerm = em.find(SearchTerm.class, id);
			searchTerm.setTerm(term);
		} catch (Exception e) {
			return false;
		}
		return true;
	}
	
	@Transactional
    public void deleteSearchTerm(Long id) {
    	    SearchTerm searchTerm = em.find(SearchTerm.class,id);
    	    em.remove(searchTerm);
    	}
	
}
