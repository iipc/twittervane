package uk.bl.wap.crowdsourcing.controller;

import java.text.ParseException;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import uk.bl.wap.crowdsourcing.Tweet;
import uk.bl.wap.crowdsourcing.dao.TweetDao;

@Controller
public class TweetController {

	@Autowired
	private TweetDao tweetDao;  
	
    @RequestMapping(value="/tweets")
    public ModelAndView tweet(HttpServletRequest request) throws ParseException {
    	
    	String tweet;
		tweet = request.getParameter("tweet");
		
		if (tweet != null) {
			tweetDao.persist(new Tweet(tweet));
		}
		
		ModelAndView mv = new ModelAndView();
    	mv.addObject("tweetDao", tweetDao);
    	mv.setViewName("tweet.jsp");
    	return mv;
    	
    }
}
