package uk.bl.wap.crowdsourcing.controller;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import uk.bl.wap.crowdsourcing.agent.TweetAnalyserService;

@Controller
public class JobController {


	private ModelAndView mv = new ModelAndView();
	
	@Autowired
	private TweetAnalyserService tweetAnalyserService; 
	
	private Integer jobNumber;
	private Boolean purge;
	private Boolean purgeAll;
	private Boolean purgeProcessed;
	private Boolean purgeFailed;
	
	private final Log log;
	
	public JobController() {
		log = LogFactory.getLog(getClass());
	}
	
	@RequestMapping(value="/runjob")
    public ModelAndView crowdsourcing(HttpServletRequest request) {

		Map<String, String> message = new HashMap<String, String>();
		
		if (request.getParameter("jobNumber") != null) {
			jobNumber = Integer.parseInt(request.getParameter("jobNumber"));
		} else {
			jobNumber = 0;
		}
		
		if (request.getParameter("purge") != null) {
			purge = Boolean.parseBoolean(request.getParameter("purge"));
		} else {
			purge = false;
		}
		
		if (request.getParameter("purgeAll") != null) {
			purgeAll = Boolean.parseBoolean(request.getParameter("purgeAll"));
		} else {
			purgeAll= false;
		}
		
		if (request.getParameter("purgeProcessed") != null) {
			purgeProcessed = Boolean.parseBoolean(request.getParameter("purgeProcessed"));
		} else {
			purgeProcessed = false;
		}
		
		if (request.getParameter("purgeFailed") != null) {
			purgeFailed = Boolean.parseBoolean(request.getParameter("purgeFailed"));
		} else {
			purgeFailed = false;
		}
		
		if (jobNumber > 0) {
			tweetAnalyserService.setJobNumber(jobNumber);
			tweetAnalyserService.run();
			message.put("message", ""+ tweetAnalyserService.getProcessCounter());	 
		} else if (purge) {
			//while (tweetAnalyserService.getTotalEntities().longValue() > 0) {
			//	tweetAnalyserService.deleteEntries();
			//}
		} else if (purgeAll) {
			//while (tweetAnalyserService.getTotalTweets().longValue() > 0) {
			//	tweetAnalyserService.deleteAllTweets();
			//}
		} else if (purgeProcessed) {
			long lastTweet;
			//try {
			//		lastTweet = (Long) tweetAnalyserService.getLastTweet();
			//} 	catch (Exception e) {
			//		lastTweet = 0;
			//}
			//while (tweetAnalyserService.getTotalProcessedTweets(lastTweet).longValue() > 0) {
			//	tweetAnalyserService.deleteProcessedTweets(lastTweet);
			//}
		} else if (purgeFailed) {
			//while (tweetAnalyserService.getTotalFailedEntities().longValue() > 0) {
			//	tweetAnalyserService.deleteFailedEntries();
			//}
		}
	
        mv.addObject("message",message);
		mv.addObject("tweetAnalyserService",tweetAnalyserService);
        mv.setViewName("jobs.jsp");
        return mv;

    }
	

}
