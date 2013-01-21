package uk.bl.wap.crowdsourcing;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Transient;

@Entity
public class UrlEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	Long id;
	private String urlOriginal;
	private String urlFull;
	private String urlDomain;
	private String collectionName;
	private Tweet tweet;
	private WebCollection webCollection;
	private Long popularity;
	
	@Transient
	private Long totalTweets = 0L;
	@Transient
	private Long totalRetweets = 0L;

	public UrlEntity() {
	}

	public UrlEntity(String urlOriginal, String urlFull, String urlDomain,
			Tweet tweet, WebCollection webCollection, String collectionName,
			Long popularity) {
		this.setUrlOriginal(urlOriginal);
		this.setUrlFull(urlFull);
		this.setUrlDomain(urlDomain);
		this.setCollectionName(collectionName);
		this.setTweet(tweet);
		this.setWebCollection(webCollection);
		this.setPopularity(popularity);
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

    @ManyToOne  
    @JoinColumn(name="tweet_id") 
	public Tweet getTweet() {
		return tweet;
	}

	public void setTweet(Tweet tweet) {
		this.tweet = tweet;
	}

	@Column(length = 4000)
	public String getUrlFull() {
		return urlFull;
	}

	public void setUrlFull(String urlFull) {
		this.urlFull = urlFull;
	}

	@Column(length = 4000)
	public String getUrlDomain() {
		return urlDomain;
	}

	public void setUrlDomain(String urlDomain) {
		this.urlDomain = urlDomain;
	}

	public String getCollectionName() {
		return collectionName;
	}

	public void setCollectionName(String collectionName) {
		this.collectionName = collectionName;
	}

	/**
	 * @return the webCollection
	 */
    @ManyToOne  
    @JoinColumn(name="web_collection_id") 
	public WebCollection getWebCollection() {
		return webCollection;
	}

	public void setWebCollection(WebCollection webCollection) {
		this.webCollection = webCollection;
	}

	@Column(nullable = true)
	public Long getPopularity() {
		return popularity;
	}

	public void setPopularity(Long popularity) {
		this.popularity = popularity;
	}

	@Column(length = 4000)
	public String getUrlOriginal() {
		return urlOriginal;
	}

	public void setUrlOriginal(String urlOriginal) {
		this.urlOriginal = urlOriginal;
	}

	/**
	 * @return the count of tweets
	 */
	public Long getTotalTweets() {
		return totalTweets;
	}

	/**
	 * @param tweets
	 *            the count of tweets to set
	 */
	public void setTotalTweets(Long tweets) {
		this.totalTweets = tweets;
	}

	/**
	 * @return the count of retweets
	 */
	public Long getTotalRetweets() {
		return totalRetweets;
	}

	/**
	 * @param tweets
	 *            the count of retweets to set
	 */
	public void setTotalRetweets(Long retweets) {
		this.totalRetweets = retweets;
	}

	

}
