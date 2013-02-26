package uk.bl.wap.crowdsourcing.controller;


import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import twitter4j.FilterQuery;
import twitter4j.Status;
import twitter4j.StatusDeletionNotice;
import twitter4j.StatusListener;
import twitter4j.TwitterStreamFactory;
import twitter4j.URLEntity;
import twitter4j.conf.ConfigurationBuilder;
import twitter4j.json.DataObjectFactory;
import uk.bl.wap.crowdsourcing.AppConfig;
import uk.bl.wap.crowdsourcing.SearchTerm;
import uk.bl.wap.crowdsourcing.Tweet;
import uk.bl.wap.crowdsourcing.UrlEntity;
import uk.bl.wap.crowdsourcing.Util;
import uk.bl.wap.crowdsourcing.WebCollection;
import uk.bl.wap.crowdsourcing.agent.TweetAnalyserService;
import uk.bl.wap.crowdsourcing.agent.TweetStreamAgentService;
import uk.bl.wap.crowdsourcing.dao.AppConfigDao;
import uk.bl.wap.crowdsourcing.dao.SearchTermDao;
import uk.bl.wap.crowdsourcing.dao.TweetDao;
import uk.bl.wap.crowdsourcing.dao.WebCollectionDao;
import uk.bl.wap.crowdsourcing.logger.JsonLogger;

@Controller
public class TwitterStreamDaemonController {

	ModelAndView mv = new ModelAndView();
	
	@Autowired
	private TweetStreamAgentService tweetStreamAgentService;
	
	@RequestMapping(value="/twitterstream")
    public ModelAndView crowdsourcing(HttpServletRequest request) {

		String action = null;
		
		if (request.getParameter("action") != null) {
        	action = request.getParameter("action");
        }
		
		if (action != null && !action.isEmpty()) {
			if (action.contentEquals("start") && tweetStreamAgentService != null) {
				if (Util.twitterStream == null) {
					tweetStreamAgentService.start();
				}
			} else if (action.contentEquals("stop")) {
				if (Util.twitterStream != null && tweetStreamAgentService != null) {
					tweetStreamAgentService.stop();
				}
			}
		}
	
		mv.addObject("appConfig",tweetStreamAgentService.getAppConfig());
		mv.addObject("allSearchTerms",tweetStreamAgentService.getAllSearchTerms());
		mv.addObject("analysisTriggerValue", tweetStreamAgentService.getAnalysisTriggerValue());
		mv.addObject("lastStreamErrors", tweetStreamAgentService.getLastStreamErrors());
		mv.addObject("displayLastStreamErrors", tweetStreamAgentService.getDisplayLastStreamErrors());
		mv.addObject("status", tweetStreamAgentService.getStatus());
        mv.setViewName("twitterstream.jsp");
        return mv;

    }
	
	public final void run() {
		tweetStreamAgentService.start();
	}
	
	public void stop() {
		tweetStreamAgentService.stop();
	}

}
