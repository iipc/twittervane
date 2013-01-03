package uk.bl.wap.crowdsourcing;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
public class SearchTerm {
	@SuppressWarnings("unused")
	private static final long serialVersionUID = 1L;
	
	
	@Id @GeneratedValue
	    Long id;
	private String term;
	
	public SearchTerm() {
	}

	public String getTerm() {
		return term;
	}

	public void setTerm(String term) {
		this.term = term;
	}
	
	public SearchTerm(String term) {
		this.term = term;
	}

    @Override
    public String toString() {
       return term;
    }
    
    public Long getId() {
		return id;
	}
	
}
