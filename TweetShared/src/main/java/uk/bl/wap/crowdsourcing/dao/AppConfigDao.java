package uk.bl.wap.crowdsourcing.dao;

import java.util.List;
import java.util.logging.Logger;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import uk.bl.wap.crowdsourcing.AppConfig;

@Component
public class AppConfigDao {
	
	@SuppressWarnings("unused")
	private Logger log = Logger.getLogger("logtest");
	
    @PersistenceContext private EntityManager em;
    
    @Transactional
    public void persist(AppConfig appConfig) {
    	em.persist(appConfig);  
    }

    public List<AppConfig> getAppConfigs() {
    	TypedQuery<AppConfig> query = em.createQuery(
            "SELECT a FROM AppConfig a ORDER BY a.id desc", AppConfig.class);
    	query.setFirstResult(0);
    	query.setMaxResults(10);
    	return query.getResultList();
    }
    
    public AppConfig getAppConfig() {
    	AppConfig appConfig = em.find(AppConfig.class,1);
    	return appConfig;
    }
    
    @Transactional
    public boolean updateAppconfig(String consumerKey,String consumerSecret,String accessToken,String accessTokenSecret,String bitlyLogin,
    		String bitlyApiKey,String httpProxyHost,String httpProxyPort,String httpProxyUser,String httpProxyPass) {
    	  try{
    		  AppConfig appConfig = em.find(AppConfig.class,1);
    		  appConfig.setConsumerKey(consumerKey);
    		  appConfig.setConsumerSecret(consumerSecret);
    		  appConfig.setAccessToken(accessToken);
    		  appConfig.setAccessTokenSecret(accessTokenSecret);
    		  appConfig.setBitlyApiKey(bitlyApiKey);
    		  appConfig.setBitlyLogin(bitlyLogin);
    		  appConfig.setHttpProxyHost(httpProxyHost);
    		  appConfig.setHttpProxyPort(httpProxyPort);
    		  appConfig.setHttpProxyUser(httpProxyUser);
    		  appConfig.setHttpProxyPass(httpProxyPass);
    	  } catch (Exception e) {
    	    return false;
    	  }
    	  return true;
    	}
    
    public boolean configExists() {
    	boolean results = true;
    	try{
    	    AppConfig appConfig = em.find(AppConfig.class,1);
    	    if (appConfig == null) {
    	    	results = false;
    	    }
    	  } catch (Exception e) {
    		  results = false;
    	  }
    	  return results;
    }
    
}
