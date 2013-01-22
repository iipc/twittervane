package uk.bl.wap.crowdsourcing.controller;

import java.text.ParseException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import uk.bl.wap.crowdsourcing.AppConfig;
import uk.bl.wap.crowdsourcing.dao.AppConfigDao;

@Controller
public class AppConfigController {

	@Autowired
	private AppConfigDao appConfigDao;
	
    @RequestMapping(value="/appconfig")
    public ModelAndView appconfig(HttpServletRequest request) throws ParseException {
    	
    	boolean updateConfig = false;
    	String consumerKey = "";
    	String consumerSecret = "";
    	String accessToken = "";
    	String accessTokenSecret = "";
    	String bitlyLogin = "";
    	String bitlyApiKey = "";
    	String httpProxyHost = "";
    	String httpProxyPort = "";
    	String httpProxyUser = "";
    	String httpProxyPass = "";
    	
    	ModelAndView mv = new ModelAndView();
    	Map<String, String> message = new HashMap<String, String>();
		
    	if (request.getParameter("consumerKey") != null) {
    		consumerKey = request.getParameter("consumerKey");
    		updateConfig = true;
    	}
    	if (request.getParameter("consumerSecret") != null) {
    		consumerSecret = request.getParameter("consumerSecret");
    		updateConfig = true;
    	}
    	if (request.getParameter("accessToken") != null) {
    		accessToken = request.getParameter("accessToken");
    		updateConfig = true;
    	}
    	if (request.getParameter("accessTokenSecret") != null) {
    		accessTokenSecret = request.getParameter("accessTokenSecret");
    		updateConfig = true;
    	}
    	if (request.getParameter("bitlyLogin") != null) {
    		bitlyLogin = request.getParameter("bitlyLogin");
    		updateConfig = true;
    	}
    	if (request.getParameter("bitlyApiKey") != null) {
    		bitlyApiKey = request.getParameter("bitlyApiKey");
    		updateConfig = true;
    	}
    	if (request.getParameter("proxyHost") != null) {
    		httpProxyHost = request.getParameter("proxyHost");
    		updateConfig = true;
    	}
    	if (request.getParameter("proxyPort") != null) {
    		httpProxyPort = request.getParameter("proxyPort");
    		updateConfig = true;
    	}
    	if (request.getParameter("proxyUser") != null) {
    		httpProxyUser = request.getParameter("proxyUser");
    		updateConfig = true;
    	}
    	if (request.getParameter("proxyPass") != null) {
    		httpProxyPass = request.getParameter("proxyPass");
    		updateConfig = true;
    	}
    	
		if (!appConfigDao.configExists()) {
			AppConfig ac = new AppConfig();
			ac.setAccessToken("");
			ac.setAccessTokenSecret("");
			ac.setBitlyApiKey("");
			ac.setBitlyLogin("");
			ac.setConsumerKey("");
			ac.setConsumerSecret("");
			ac.setHttpProxyHost("");
			ac.setHttpProxyPort("");
			ac.setHttpProxyUser("");
			ac.setHttpProxyPass("");
			appConfigDao.persist(ac);
		}
		
		AppConfig appConfig = appConfigDao.getAppConfig();
		
		if (updateConfig) {	
			if (consumerKey != null && !consumerKey.isEmpty()) {
				appConfig.setConsumerKey(consumerKey);
			}
			if (consumerSecret != null && !consumerSecret.isEmpty()) {
				appConfig.setConsumerSecret(consumerSecret);
			}
			if (accessToken != null && !accessToken.isEmpty()) {
				appConfig.setAccessToken(accessToken);
			}
			if (accessTokenSecret != null && !accessTokenSecret.isEmpty()) {
				appConfig.setAccessTokenSecret(accessTokenSecret);
			}
			if (bitlyLogin != null && !bitlyLogin.isEmpty()) {
				appConfig.setBitlyLogin(bitlyLogin);
			}
			if (bitlyApiKey != null && !bitlyApiKey.isEmpty()) {
				appConfig.setBitlyApiKey(bitlyApiKey);
			}
			if (httpProxyHost != null && !httpProxyHost.isEmpty()) {
				appConfig.setHttpProxyHost(httpProxyHost);
			}
			if (httpProxyPort != null && !httpProxyPort.isEmpty()) {
				appConfig.setHttpProxyPort(httpProxyPort);
			}
			if (httpProxyUser != null && !httpProxyUser.isEmpty()) {
				appConfig.setHttpProxyUser(httpProxyUser);
			}
			if (httpProxyPass != null && !httpProxyPass.isEmpty()) {
				appConfig.setHttpProxyPass(httpProxyPass);
			}
			if (appConfigDao.updateAppconfig(consumerKey,consumerSecret,accessToken,accessTokenSecret,bitlyLogin,bitlyApiKey,httpProxyHost,httpProxyPort,
					httpProxyUser,httpProxyPass)) {
				message.put("message", "Saved");
			} else {
				message.put("message", "Problem Saving Config");
			}
			
		};
		
		mv.addObject("message",message);
    	mv.addObject("appConfig", appConfig);
    	mv.setViewName("appconfig.jsp");
    	return mv;
    }
}
