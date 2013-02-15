package uk.bl.wap.crowdsourcing;

import java.io.Serializable;
import java.sql.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

@Entity
public class Tweet implements Serializable {
	private static final long serialVersionUID = 1L;
	
   Long id;
   private String name;
   private String text;
   private Long retweetCount;
   private Long tweetId;
   private Date creationDate;
   private String twitterDate;
   @ElementCollection 
   private List<UrlEntity> urlEntities;
   private Boolean processed = false;

   // Constructors:
   public Tweet() {
   }
   
   public Tweet(String name) {
       this.setName(name);
       this.setCreationDate(new Date(System.currentTimeMillis()));
   }
   
   public Tweet(String name, String text) {
       this.setName(name);
       this.setText(text);
       this.setCreationDate(new Date(System.currentTimeMillis()));
   }
   
   public Tweet(String name, String text,long retweetCount, Long tweetId) {
       this.setName(name);
       this.setText(text);
       this.setRetweetCount(retweetCount);
       this.setTweetiD(tweetId);
       this.setCreationDate(new Date(System.currentTimeMillis()));
   }
   
   public Tweet(String name, String text, List<UrlEntity> list) {
       this.setName(name);
       this.setText(text);
   }

   public Tweet(String name,String twitterDate, String text) {
       this.setName(name);
       this.setText(text);
       this.setTwitterDate(twitterDate);
       this.setCreationDate(new Date(System.currentTimeMillis()));
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
			mappedBy="tweet",    
            targetEntity=UrlEntity.class,   
            cascade = {CascadeType.MERGE, CascadeType.REMOVE, CascadeType.REFRESH}  
	) 
	@LazyCollection(LazyCollectionOption.FALSE)
	public List<UrlEntity> getUrlEntities() {
		return urlEntities;
	}
	
	public void setUrlEntities(List<UrlEntity> urlEntities) {
		this.urlEntities = urlEntities;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public Date getCreationDate() {
		return creationDate;
	}

	public void setCreationDate(Date creationDate) {
		this.creationDate = creationDate;
	}

	public String getTwitterDate() {
		return twitterDate;
	}

	public void setTwitterDate(String twitterDate) {
		this.twitterDate = twitterDate;
	}

	public Long getRetweetCount() {
		return retweetCount;
	}

	public void setRetweetCount(Long retweetCount) {
		this.retweetCount = retweetCount;
	}

	public Long getTweetiD() {
		return tweetId;
	}

	public void setTweetiD(Long tweetiD) {
		this.tweetId = tweetiD;
	}

	/**
	 * @return the processed
	 */
	public Boolean getProcessed() {
		return processed;
	}

	/**
	 * @param processed the processed to set
	 */
	public void setProcessed(Boolean processed) {
		// also set the processed flag on the associated UrlEntities to improve performance
		if (urlEntities != null) {
			for (UrlEntity urlEntity : urlEntities) {
				urlEntity.setProcessed(processed);
			}
		}
		this.processed = processed;
	}
}
