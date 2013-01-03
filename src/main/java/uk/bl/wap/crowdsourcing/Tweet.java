package uk.bl.wap.crowdsourcing;

import java.io.Serializable;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
public class Tweet implements Serializable {
	private static final long serialVersionUID = 1L;
	
   @Id @GeneratedValue
   Long id;
   private String name;
   private String text;
   private long retweetCount;
   private long tweetId;
   private Date creationDate;
   private String twitterDate;
   @ElementCollection private List<String> urlEntities = new ArrayList<String>();

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
   
   public Tweet(String name, String text, List<String> list) {
       this.setName(name);
       this.setText(text);
   }

   public Tweet(String name,String twitterDate, String text) {
       this.setName(name);
       this.setText(text);
       this.setTwitterDate(twitterDate);
       this.setCreationDate(new Date(System.currentTimeMillis()));
   }

	public List<String> getUrlEntities() {
		return urlEntities;
	}
	
	public void setUrlEntities(List<String> urlEntities) {
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

	public Long getId() {
		return id;
	}

	public long getRetweetCount() {
		return retweetCount;
	}

	public void setRetweetCount(long retweetCount) {
		this.retweetCount = retweetCount;
	}

	public Long getTweetiD() {
		return tweetId;
	}

	public void setTweetiD(Long tweetiD) {
		this.tweetId = tweetiD;
	}
}
