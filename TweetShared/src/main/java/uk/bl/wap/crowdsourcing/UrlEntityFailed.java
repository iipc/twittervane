package uk.bl.wap.crowdsourcing;

import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
public class UrlEntityFailed implements Serializable {
	private static final long serialVersionUID = 1L;
	
   @Id @GeneratedValue
   Long id;
   private String url;
   private String errorMessage;
   private long tweetId;
   private String collectionName;
   private long collectionId;

   public UrlEntityFailed() {
   }
  
   public UrlEntityFailed(String url,long tweetId,String errorMessage,long collectionid,String collectionName) {
      this.setUrl(url);
      this.setTweetId(tweetId);
      this.setCollectionId(collectionid);
      this.setCollectionName(collectionName);
      this.setErrorMessage(errorMessage);
   }

	public Long getId() {
		return id;
	}

	public long getTweetId() {
		return tweetId;
	}

	public void setTweetId(long tweetId) {
		this.tweetId = tweetId;
	}

	public String getUrl() {
		return url;
	}


	public void setUrl(String url) {
		this.url = url;
	}

	public String getErrorMessage() {
		return errorMessage;
	}

	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}

	public String getCollectionName() {
		return collectionName;
	}

	public void setCollectionName(String collectionName) {
		this.collectionName = collectionName;
	}

	public long getCollectionId() {
		return collectionId;
	}

	public void setCollectionId(long collectionId) {
		this.collectionId = collectionId;
	}

}
