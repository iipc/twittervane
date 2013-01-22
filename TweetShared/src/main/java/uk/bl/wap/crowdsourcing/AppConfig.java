package uk.bl.wap.crowdsourcing;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Id;


@Entity
public class AppConfig implements Serializable {
	private static final long serialVersionUID = 1L;
	
   @Id
   private int id = 1;
   
   private String consumerKey;
   private String consumerSecret;
   private String accessToken;
   private String accessTokenSecret;
   private String bitlyLogin;
   private String bitlyApiKey;
   private String httpProxyHost;
   private String httpProxyPort;
   private String httpProxyUser;
   private String httpProxyPass;

   public AppConfig() {
   }
   
   public AppConfig(long id) {
   }
   
   public int getId() {
		return id;
	}

	public String getConsumerKey() {
		return consumerKey;
	}
	
	public void setConsumerKey(String consumerKey) {
		this.consumerKey = consumerKey;
	}
	
	public String getConsumerSecret() {
		return consumerSecret;
	}
	
	public void setConsumerSecret(String consumerSecret) {
		this.consumerSecret = consumerSecret;
	}
	
	public String getAccessToken() {
		return accessToken;
	}
	
	public void setAccessToken(String accessToken) {
		this.accessToken = accessToken;
	}
	
	public String getAccessTokenSecret() {
		return accessTokenSecret;
	}
	
	public void setAccessTokenSecret(String accessTokenSecret) {
		this.accessTokenSecret = accessTokenSecret;
	}
	
	public String getBitlyLogin() {
		return bitlyLogin;
	}
	
	public void setBitlyLogin(String bitlyLogin) {
		this.bitlyLogin = bitlyLogin;
	}
	
	public String getBitlyApiKey() {
		return bitlyApiKey;
	}
	
	public void setBitlyApiKey(String bitlyApiKey) {
		this.bitlyApiKey = bitlyApiKey;
	}

	public String getHttpProxyHost() {
		return httpProxyHost;
	}

	public void setHttpProxyHost(String httpProxyHost) {
		this.httpProxyHost = httpProxyHost;
	}

	public String getHttpProxyPort() {
		return httpProxyPort;
	}

	public void setHttpProxyPort(String httpProxyPort) {
		this.httpProxyPort = httpProxyPort;
	}

	public String getHttpProxyUser() {
		return httpProxyUser;
	}

	public void setHttpProxyUser(String httpProxyUser) {
		this.httpProxyUser = httpProxyUser;
	}

	public String getHttpProxyPass() {
		return httpProxyPass;
	}

	public void setHttpProxyPass(String httpProxyPass) {
		this.httpProxyPass = httpProxyPass;
	}
}
