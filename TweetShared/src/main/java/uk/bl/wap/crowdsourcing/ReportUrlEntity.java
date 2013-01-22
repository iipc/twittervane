package uk.bl.wap.crowdsourcing;

public class ReportUrlEntity {

   private String urlFull;
   private String urlDomain;
   private String collectionName;
   private long collectionId;
   private long popularity;
   
   public ReportUrlEntity() {}

   public ReportUrlEntity(String urlFull,String urlDomain,String collectionName,long collectionId,long popularity) {
	   this.setUrlFull(urlFull);
	   this.setUrlDomain(urlDomain);
	   this.setCollectionId(collectionId);
	   this.setCollectionName(collectionName);
	   this.setPopularity(popularity);
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

}
