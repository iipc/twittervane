package uk.bl.wap.crowdsourcing;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

@Entity
public class WebCollection implements Serializable {
	
	private static final long serialVersionUID = -7930913195790169565L;
	// Persistent Fields:
    private Long id;
    private String name;
    private String description;
    private Date creationDate;
    private Date startDate;
    private Date endDate;
    private List<SearchTerm> searchTerms;
    private List<UrlEntity> urlEntities;

    private Long totalTweets = 0L;
    private Long totalUrlsOriginal = 0L;
    private Long totalUrlsExpanded = 0L;
    private Long totalUrlErrors = 0L;
    private String topUrl = "";
    private Long topUrlTweets = 0L;
    private Long topUrlRetweets = 0L;
    private Long totalTweetsProcessed = 0L;
    private Long totalTweetsUnprocessed = 0L;
    private Long totalUrlsProcessed = 0L;
    private Long totalUrlsUnprocessed = 0L;
    
    public WebCollection() {
    }
 
    public WebCollection(String name) {
        this.name = name;
        this.creationDate = new Date(System.currentTimeMillis());
    }
    
    public WebCollection(String name,String description,String startDate,String endDate) throws ParseException {
        this.name = name;
        this.description = description;
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MMM-yyyy"); 
		Date convertedDate;
		convertedDate = dateFormat.parse(startDate);
		this.startDate = convertedDate;
	
		convertedDate = dateFormat.parse(endDate);
		this.endDate = convertedDate;
			
        this.creationDate = new Date(System.currentTimeMillis());
    }
    
    @Id 
    @GeneratedValue(strategy = GenerationType.AUTO)
	public Long getId() {
		return id;
	}
    
    public void setId(Long id) {
    	this.id = id;
    }
    
	@OneToMany (
			mappedBy="webCollection",    
            targetEntity=SearchTerm.class,   
            cascade = CascadeType.ALL,
            orphanRemoval = true
	) 
	@LazyCollection(LazyCollectionOption.FALSE)
	public List<SearchTerm> getSearchTerms() {
		return searchTerms;
	}
	
	public void setSearchTerms(List<SearchTerm> searchTerms) {
		this.searchTerms = searchTerms;
	}
	
	@OneToMany (
			mappedBy="webCollection",    
            targetEntity=UrlEntity.class,   
            cascade = CascadeType.ALL  
	) 
	@LazyCollection(LazyCollectionOption.TRUE)
	public List<UrlEntity> getUrlEntities() {
		return urlEntities;
	}

	public void setUrlEntities(List<UrlEntity> urlEntities) {
		this.urlEntities = urlEntities;
	}
	
	@Override
    public String toString() {
        return name + " (signed on " + creationDate + ")";
    }

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Date getCreationDate() {
		return creationDate;
	}

	public void setCreationDate(Date creationDate) {
		this.creationDate = creationDate;
	}

	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	public Date getEndDate() {
		return this.endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	public void addSearchTerm(final SearchTerm st) {
		getSearchTerms().add(st);
    }
 
    public void fire(final SearchTerm st) {
    	getSearchTerms().remove(st);
    }

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	/**
	 * @return the serialversionuid
	 */
	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	/**
	 * @return the totalTweets
	 */
	public Long getTotalTweets() {
		return totalTweets;
	}

	/**
	 * @return the totalUrlsOriginal
	 */
	public Long getTotalUrlsOriginal() {
		return totalUrlsOriginal;
	}

	/**
	 * @return the totalUrlsExpanded
	 */
	public Long getTotalUrlsExpanded() {
		return totalUrlsExpanded;
	}

	/**
	 * @return the totalUrlErrors
	 */
	public Long getTotalUrlErrors() {
		return totalUrlErrors;
	}

	/**
	 * @param totalTweets the totalTweets to set
	 */
	public void setTotalTweets(Long totalTweets) {
		this.totalTweets = totalTweets;
	}

	/**
	 * @param totalUrlsOriginal the totalUrlsOriginal to set
	 */
	public void setTotalUrlsOriginal(Long totalUrlsOriginal) {
		this.totalUrlsOriginal = totalUrlsOriginal;
	}

	/**
	 * @param totalUrlsExpanded the totalUrlsExpanded to set
	 */
	public void setTotalUrlsExpanded(Long totalUrlsExpanded) {
		this.totalUrlsExpanded = totalUrlsExpanded;
	}

	/**
	 * @param totalUrlErrors the totalUrlErrors to set
	 */
	public void setTotalUrlErrors(Long totalUrlErrors) {
		this.totalUrlErrors = totalUrlErrors;
	}

	/**
	 * @return the topUrl
	 */
	public String getTopUrl() {
		return topUrl;
	}

	/**
	 * @return the topUrlTweets
	 */
	public Long getTopUrlTweets() {
		return topUrlTweets;
	}

	/**
	 * @param topUrl the topUrl to set
	 */
	public void setTopUrl(String topUrl) {
		this.topUrl = topUrl;
	}

	/**
	 * @param topUrlTweets the topUrlTweets to set
	 */
	public void setTopUrlTweets(Long topUrlTweets) {
		this.topUrlTweets = topUrlTweets;
	}

	/**
	 * @return the topUrlRetweets
	 */
	public Long getTopUrlRetweets() {
		return topUrlRetweets;
	}

	/**
	 * @param topUrlRetweets the topUrlRetweets to set
	 */
	public void setTopUrlRetweets(Long topUrlRetweets) {
		this.topUrlRetweets = topUrlRetweets;
	}

	/**
	 * @return the totalTweetsProcessed
	 */
	public Long getTotalTweetsProcessed() {
		return totalTweetsProcessed;
	}

	/**
	 * @return the totalTweetsUnprocessed
	 */
	public Long getTotalTweetsUnprocessed() {
		return totalTweetsUnprocessed;
	}

	/**
	 * @param totalTweetsProcessed the totalTweetsProcessed to set
	 */
	public void setTotalTweetsProcessed(Long totalTweetsProcessed) {
		this.totalTweetsProcessed = totalTweetsProcessed;
	}

	/**
	 * @param totalTweetsUnprocessed the totalTweetsUnprocessed to set
	 */
	public void setTotalTweetsUnprocessed(Long totalTweetsUnprocessed) {
		this.totalTweetsUnprocessed = totalTweetsUnprocessed;
	}

	/**
	 * @return the totalUrlsProcessed
	 */
	public Long getTotalUrlsProcessed() {
		return totalUrlsProcessed;
	}

	/**
	 * @return the totalUrlsUnprocessed
	 */
	public Long getTotalUrlsUnprocessed() {
		return totalUrlsUnprocessed;
	}

	/**
	 * @param totalUrlsProcessed the totalUrlsProcessed to set
	 */
	public void setTotalUrlsProcessed(Long totalUrlsProcessed) {
		this.totalUrlsProcessed = totalUrlsProcessed;
	}

	/**
	 * @param totalUrlsUnprocessed the totalUrlsUnprocessed to set
	 */
	public void setTotalUrlsUnprocessed(Long totalUrlsUnprocessed) {
		this.totalUrlsUnprocessed = totalUrlsUnprocessed;
	}

}