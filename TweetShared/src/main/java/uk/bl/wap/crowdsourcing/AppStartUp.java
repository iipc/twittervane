package uk.bl.wap.crowdsourcing;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class AppStartUp implements ServletContextListener{

	private final Log log;
	
	public AppStartUp() {
		log = LogFactory.getLog(getClass());
	}
	
	public void contextDestroyed(ServletContextEvent arg0) {
/*
		if (Util.twitterStream != null) {
			log.info("Cleanup activity: Twitter Stream");
			Util.twitterStream.shutdown();
			Util.twitterStream.cleanUp();
		}
		log.info("Cleanup activity Complete.");
		*/
	}
	
	
	public void  contextInitialized(ServletContextEvent arg0) {
		log.info("Context Initialised");
	}

}

