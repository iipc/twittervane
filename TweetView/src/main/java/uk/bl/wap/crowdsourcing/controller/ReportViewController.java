package uk.bl.wap.crowdsourcing.controller;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import uk.bl.wap.crowdsourcing.Tweet;
import uk.bl.wap.crowdsourcing.UrlEntity;
import uk.bl.wap.crowdsourcing.UrlEntityComparator;
import uk.bl.wap.crowdsourcing.WebCollection;
import uk.bl.wap.crowdsourcing.dao.TweetDao;
import uk.bl.wap.crowdsourcing.dao.UrlEntityDao;
import uk.bl.wap.crowdsourcing.dao.WebCollectionDao;

@Controller
public class ReportViewController {

	@Autowired
    private UrlEntityDao urlEntityDao;
	
	@Autowired
	private WebCollectionDao webCollectionDao;
	
	@Autowired
	private TweetDao tweetDao;
	
    @RequestMapping(value="/reportView")
    public ModelAndView tweet(HttpServletRequest request) throws ParseException {
    	
    	String sort = request.getParameter("sort");
    	String column = request.getParameter("column");
    	
    	List<UrlEntity> urlEntities = new ArrayList<UrlEntity>();
    	HashMap<Long, Tweet> tweets = new HashMap<Long, Tweet>();
    	Long tweetTotal = 0L;
    	Long retweetTotal = 0L;
    	
    	UrlEntityComparator.SortOrder sortOrder = null;
    	if (sort.equals("desc")) {
    		sortOrder = UrlEntityComparator.SortOrder.desc;
    	} else {
    		sortOrder = UrlEntityComparator.SortOrder.asc;
    	}
    	if (column == null) {
    		column = "totalTweets";
    	}
    	
    	// fetch the list of collections
    	List<WebCollection> webCollections = webCollectionDao.getAllCollections();
    	
    	// obtain the top url for each collection
    	for (WebCollection webCollection : webCollections) {
	    	UrlEntity urlEntity = urlEntityDao.getTopUrl(webCollection.getId());

	    	if (urlEntity != null) {
		    	urlEntities.add(urlEntity);
		    	tweetTotal = tweetTotal + urlEntity.getTotalTweets();
		    	
	        	long tweetId = urlEntity.getTweet().getId();
	        	Tweet tweet = tweetDao.getTweet(tweetId);
	        	tweets.put(tweetId, tweet);
	        	retweetTotal = retweetTotal + tweet.getRetweetCount();
	        	urlEntity.setTotalRetweets(tweet.getRetweetCount());
	    	}
	    	
    	}
    	
    	// sort the url entities
    	if (column.equals("urlFull")) {
    		this.sortUrlEntitiesBy(urlEntities, UrlEntityComparator.Order.urlFull, sortOrder);
    	} else if (column.equals("collectionName")) {
    		this.sortUrlEntitiesBy(urlEntities, UrlEntityComparator.Order.collectionName, sortOrder);
    	} else if (column.equals("totalTweets")) {
    		this.sortUrlEntitiesBy(urlEntities, UrlEntityComparator.Order.totalTweets, sortOrder);
    	} else if (column.equals("totalRetweets")) {
    		this.sortUrlEntitiesBy(urlEntities, UrlEntityComparator.Order.totalRetweets, sortOrder);
    	}
        	
		ModelAndView mv = new ModelAndView();
    	Date reportDate = new Date(); 
    	mv.addObject("reportDate", reportDate);
    	mv.addObject("urlEntities", urlEntities);
    	mv.addObject("tweets", tweets);
    	mv.addObject("tweetTotal", tweetTotal);
    	mv.addObject("retweetTotal", retweetTotal);
    	mv.addObject("sort", sort);
    	mv.addObject("column", column);
    	
    	mv.setViewName("reportView.jsp");
    	return mv;
    	
    }
    
    
    public void sortUrlEntitiesBy(List<UrlEntity> urlEntities, UrlEntityComparator.Order sortingBy, UrlEntityComparator.SortOrder sortOrder) {
    		UrlEntityComparator comparator = new UrlEntityComparator();
    		comparator.setSortOrder(sortOrder);
    		comparator.setSortingBy(sortingBy);
    		Collections.sort(urlEntities, comparator); // now we have a sorted list
    	}
}
