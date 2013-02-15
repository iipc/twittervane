package uk.bl.wap.crowdsourcing;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Entity
public class SearchTerm implements Serializable{
	
	private static final long serialVersionUID = -6838621632058900956L;
	
	private Long id;
	private WebCollection webCollection;
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
    
    /**
     * Gets id (primary key).
     */
    @Id  
    @GeneratedValue(strategy = GenerationType.AUTO) 
    public Long getId() {
		return id;
	}
    
    public void setId(Long id) {
    	  this.id = id;
    }

	/**
	 * @return the webCollection
	 */
    @ManyToOne  
    @JoinColumn(name="web_collection_id") 
	public WebCollection getWebCollection() {
		return webCollection;
	}

	/**
	 * @param webCollection the webCollection to set
	 */
	public void setWebCollection(WebCollection webCollection) {
		this.webCollection = webCollection;
	}
	
}
