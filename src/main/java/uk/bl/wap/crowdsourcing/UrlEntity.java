package uk.bl.wap.crowdsourcing;

import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
public class UrlEntity implements Serializable {
	private static final long serialVersionUID = 1L;
	
   @Id @GeneratedValue
   Long id;
   private String urlOriginal;
   private String urlFull;
   private String urlDomain;
   private String collectionName;
   private long tweetId;
   private long collectionId;
   private long popularity;
  //@ElementCollection private List<String> urlEntities = new ArrayList<String>();

   // Constructors:
   public UrlEntity() {
   }
  
   
   public UrlEntity(String urlOriginal,String urlFull,String urlDomain,Long tweetId,Long collectionId,String collectionName,long popularity) {
      this.setUrlOriginal(urlOriginal);
	  this.setUrlFull(urlFull);
      this.setUrlDomain(urlDomain);
      this.setCollectionName(collectionName);
      this.setTweetId(tweetId);
      this.setCollectionId(collectionId);
      this.setPopularity(popularity);
   }
   
   /*
	public List<String> getUrlEntities() {
		return urlEntities;
	}
	
	public void setUrlEntities(List<String> urlEntities) {
		this.urlEntities = urlEntities;
	}
*/

	public Long getId() {
		return id;
	}

	public long getTweetId() {
		return tweetId;
	}

	public void setTweetId(long tweetId) {
		this.tweetId = tweetId;
	}


	public String getUrlFull() {
		return urlFull;
	}


	public void setUrlFull(String urlFull) {
		this.urlFull = urlFull;
	}


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


	public long getCollectionId() {
		return collectionId;
	}


	public void setCollectionId(long collectionId) {
		this.collectionId = collectionId;
	}


	public long getPopularity() {
		return popularity;
	}


	public void setPopularity(long popularity) {
		this.popularity = popularity;
	}


	public String getUrlOriginal() {
		return urlOriginal;
	}


	public void setUrlOriginal(String urlOriginal) {
		this.urlOriginal = urlOriginal;
	}

}
