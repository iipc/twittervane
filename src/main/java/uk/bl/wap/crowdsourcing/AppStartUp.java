package uk.bl.wap.crowdsourcing;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

public class AppStartUp implements ServletContextListener{

	public void contextDestroyed(ServletContextEvent arg0) {

		if (Util.twitterStream != null) {
			Util.log.info("Cleanup activity: Twitter Stream");
			Util.twitterStream.shutdown();
			Util.twitterStream.cleanUp();
		}
		Util.log.info("Cleanup activity Complete.");
	}
	
	
	public void  contextInitialized(ServletContextEvent arg0) {
	
	}

}

