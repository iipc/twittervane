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

import uk.bl.wap.crowdsourcing.Page;
import uk.bl.wap.crowdsourcing.Tweet;
import uk.bl.wap.crowdsourcing.UrlEntity;
import uk.bl.wap.crowdsourcing.UrlEntityComparator;
import uk.bl.wap.crowdsourcing.WebCollection;
import uk.bl.wap.crowdsourcing.WebCollectionComparator;
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
	
	private Integer pageNumber = 1;
	
	/**
	 * The number of records that will appear on a page
	 */
	private Integer pageSize;
	
    @RequestMapping(value="/reportView")
    public ModelAndView buildReport(HttpServletRequest request) throws ParseException {
    	
    	String sort = request.getParameter("sort");
    	String column = request.getParameter("column");
    	String report = request.getParameter("report");
    	String collection = request.getParameter("collection");
    	
    	if (request.getParameter("pageNumber") != null) {
    		pageNumber = Integer.parseInt(request.getParameter("pageNumber"));
    	} else {
    		pageNumber = 1;
    	}
		ModelAndView mv = new ModelAndView();
    	Date reportDate = new Date(); 
    	
    	if (report == null) {
    		report = "topUrlsByCollection";
    	}
    	if (sort == null) {
    		sort = "desc";
    	}
     	
    	if (report.equals("topUrlsByCollection")) {
        	if (column == null) {
        		column = "totalTweets";
        	}
    		mv = buildTopUrlsByCollectionReport(mv, sort, column);
    	} else if (report.equals("tweetSummaryByCollection")) {
        	if (column == null) {
        		column = "totalTweets";
        	}
    		mv = buildTweetSummaryByCollectionReport(mv, sort, column);
    	} else if (report.equals("urlsInCollection")) {
        	if (collection.equals("")) {
        		collection = "0";
        	} 
        	Long collectionId = Long.parseLong(collection);

        	if (column == null) {
        		column = "collectionName";
        	} 		
        	mv = buildUrlsInCollectionReport(mv, sort, column, collectionId);
    	}
    	
    	// pass the generic report information to the view
    	mv.addObject("reportDate", reportDate);
    	mv.addObject("sort", sort);
    	mv.addObject("column", column);
    	mv.addObject("report", report);
    	
    	mv.setViewName(report + ".jsp");
    	return mv;
    	
    }
    
    private ModelAndView buildTweetSummaryByCollectionReport(ModelAndView mv, String sort, String column) {
    	
    	// fetch the list of collections
    	List<WebCollection> webCollections = webCollectionDao.getAllCollections();
    	
    	Long totalTweets = 0L;
    	Long totalUrls = 0L;
    	Long totalUrlsExpanded = 0L;
    	Long totalUrlErrors = 0L;
    	
    	WebCollectionComparator.SortOrder sortOrder = null;
    	if (sort.equals("desc")) {
    		sortOrder = WebCollectionComparator.SortOrder.desc;
    	} else {
    		sortOrder = WebCollectionComparator.SortOrder.asc;
    	}
    	if (column == null) {
    		column = "totalTweets";
    	}

    	// fetch the tweets for each collection
    	for (WebCollection webCollection : webCollections) {
    		webCollection.setTotalTweets(tweetDao.getTotalTweetsByCollection(webCollection.getId()).longValue());
    		webCollection.setTotalUrlsOriginal(urlEntityDao.getTotalOriginalURL(webCollection.getId(), null, null).longValue()
    				+ urlEntityDao.getTotalURL(webCollection.getId(), null, null).longValue());
       		webCollection.setTotalUrlsExpanded(urlEntityDao.getTotalURL(webCollection.getId(), null, null).longValue());
       		webCollection.setTotalUrlErrors(urlEntityDao.getTotalEntitiesFailedByCollection(webCollection.getId()).longValue());
       		totalTweets = totalTweets + webCollection.getTotalTweets();
       		totalUrls = totalUrls + webCollection.getTotalUrlsOriginal();
       		totalUrlsExpanded = totalUrlsExpanded + webCollection.getTotalUrlsExpanded();
       		totalUrlErrors = totalUrlErrors + webCollection.getTotalUrlErrors();
       		mv.addObject("webCollections", webCollections);
    	}
    	
    	mv.addObject("totalTweets", totalTweets);
    	mv.addObject("totalUrlsOriginal", totalUrls);
    	mv.addObject("totalUrlsExpanded", totalUrlsExpanded);
    	mv.addObject("totalUrlErrors", totalUrlErrors);
    	
    	// sort the web collection
    	if (column.equals("collectionName")) {
    		this.sortWebCollectionsBy(webCollections, WebCollectionComparator.Order.collectionName, sortOrder);
    	} else if (column.equals("totalTweets")) {
    		this.sortWebCollectionsBy(webCollections, WebCollectionComparator.Order.totalTweets, sortOrder);
    	} else if (column.equals("totalUrlsOriginal")) {
    		this.sortWebCollectionsBy(webCollections, WebCollectionComparator.Order.totalUrlsOriginal, sortOrder);
    	} else if (column.equals("totalUrlsFull")) {
    		this.sortWebCollectionsBy(webCollections, WebCollectionComparator.Order.totalUrlsFull, sortOrder);
    	} else if (column.equals("totalUrlErrors")) {
    		this.sortWebCollectionsBy(webCollections, WebCollectionComparator.Order.totalUrlErrors, sortOrder);
    	}
    	
    	return mv;
    }
    
    private ModelAndView buildTopUrlsByCollectionReport(ModelAndView mv, String sort, String column) {
    	
    	List<UrlEntity> urlEntities = new ArrayList<UrlEntity>();
    	HashMap<Long, Tweet> tweets = new HashMap<Long, Tweet>();
    	Long tweetTotal = 0L;
    	Long retweetTotal = 0L;
    	
    	UrlEntityComparator.SortOrder urlEntitySortOrder = null;
       	if (column == null) {
    		column = "totalTweets";
       	}
       	
    	if (sort.equals("desc")) {
    		urlEntitySortOrder = UrlEntityComparator.SortOrder.desc;
    	} else {
    		urlEntitySortOrder = UrlEntityComparator.SortOrder.asc;
    	}
       	
    	WebCollectionComparator.SortOrder webCollectionsSortOrder = null;
    	if (sort.equals("desc")) {
    		webCollectionsSortOrder = WebCollectionComparator.SortOrder.desc;
    	} else {
    		webCollectionsSortOrder = WebCollectionComparator.SortOrder.asc;
    	}
     	
    	// fetch the list of collections
    	List<WebCollection> webCollections = webCollectionDao.getAllCollections();
    	
    	Long totalTweets = 0L;
    	Long totalUrls = 0L;
    	Long totalUrlsExpanded = 0L;
    	Long totalUrlErrors = 0L;
    	
    	for (WebCollection webCollection : webCollections) {
    		webCollection.setTotalTweets(tweetDao.getTotalTweetsByCollection(webCollection.getId()).longValue());
    		webCollection.setTotalUrlsOriginal(urlEntityDao.getTotalOriginalURL(webCollection.getId(), null, null).longValue());
       		webCollection.setTotalUrlsExpanded(urlEntityDao.getTotalURL(webCollection.getId(), null, null).longValue());
       		webCollection.setTotalUrlErrors(urlEntityDao.getTotalEntitiesFailedByCollection(webCollection.getId()).longValue());
       		totalTweets = totalTweets + webCollection.getTotalTweets();
       		totalUrls = totalUrls + webCollection.getTotalUrlsOriginal();
       		totalUrlsExpanded = totalUrlsExpanded + webCollection.getTotalUrlsExpanded();
       		totalUrlErrors = totalUrlErrors + webCollection.getTotalUrlErrors();
       		mv.addObject("webCollections", webCollections);
    	}
    	
    	// sort the web collection
    	if (column.equals("collectionName")) {
    		this.sortWebCollectionsBy(webCollections, WebCollectionComparator.Order.collectionName, webCollectionsSortOrder);
    	} else if (column.equals("totalTweets")) {
    		this.sortWebCollectionsBy(webCollections, WebCollectionComparator.Order.totalTweets, webCollectionsSortOrder);
    	} else if (column.equals("totalUrlsOriginal")) {
    		this.sortWebCollectionsBy(webCollections, WebCollectionComparator.Order.totalUrlsOriginal, webCollectionsSortOrder);
    	} else if (column.equals("totalUrlsFull")) {
    		this.sortWebCollectionsBy(webCollections, WebCollectionComparator.Order.totalUrlsFull, webCollectionsSortOrder);
    	} else if (column.equals("totalUrlErrors")) {
    		this.sortWebCollectionsBy(webCollections, WebCollectionComparator.Order.totalUrlErrors, webCollectionsSortOrder);
    	}
    	
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
	    	} else {
	    		UrlEntity newUrlEntity = new UrlEntity();
	    		newUrlEntity.setExpanded(false);
	    		newUrlEntity.setUrlFull("");
	    		newUrlEntity.setUrlOriginal("");
	    		newUrlEntity.setWebCollection(webCollection);
	    		newUrlEntity.setTotalTweets(0L);
	    		newUrlEntity.setTotalRetweets(0L);
	    		Tweet tweet = new Tweet();
	    		tweet.setId(-1L);
	    		tweet.setRetweetCount(0L);
	    		tweets.put(-1L, tweet);
	    		
	    		newUrlEntity.setTweet(tweet);
	    		urlEntities.add(newUrlEntity);
	    	}
	    	
    	}
    	
    	// sort the url entities
    	if (column.equals("urlFull")) {
    		this.sortUrlEntitiesBy(urlEntities, UrlEntityComparator.Order.urlFull, urlEntitySortOrder);
    	} else if (column.equals("totalTweets")) {
    		this.sortUrlEntitiesBy(urlEntities, UrlEntityComparator.Order.totalTweets, urlEntitySortOrder);
    	} else if (column.equals("totalRetweets")) {
    		this.sortUrlEntitiesBy(urlEntities, UrlEntityComparator.Order.totalRetweets, urlEntitySortOrder);
    	}
    	
    	mv.addObject("urlEntities", urlEntities);
    	mv.addObject("tweets", tweets);
    	mv.addObject("tweetTotal", tweetTotal);
    	mv.addObject("retweetTotal", retweetTotal);

    	return mv;
    }
    
    private ModelAndView buildUrlsInCollectionReport(ModelAndView mv, String sort, String column, Long collectionId) {
    	
    	UrlEntityComparator.SortOrder urlEntitySortOrder = null;
       	
    	if (sort.equals("desc")) {
    		urlEntitySortOrder = UrlEntityComparator.SortOrder.desc;
    	} else {
    		urlEntitySortOrder = UrlEntityComparator.SortOrder.asc;
    	}
       	
    	// fetch the collection
    	WebCollection webCollection = webCollectionDao.getWebCollection(collectionId);
    	
       	// fetch the url entities
       	List<UrlEntity> urlEntities = urlEntityDao.getUrlEntitiesByCollection(webCollection.getId(), (this.pageNumber - 1) * this.pageSize, this.pageSize);
    	
    	// sort the url entities
    	if (column.equals("urlFull")) {
    		this.sortUrlEntitiesBy(urlEntities, UrlEntityComparator.Order.urlFull, urlEntitySortOrder);
    	} else if (column.equals("tweeter")) {
       		this.sortUrlEntitiesBy(urlEntities, UrlEntityComparator.Order.tweeter, urlEntitySortOrder);
    	} else if (column.equals("tweet")) {
       		this.sortUrlEntitiesBy(urlEntities, UrlEntityComparator.Order.tweet, urlEntitySortOrder);
    	}
    	
    	mv.addObject("urlEntities", urlEntities);
       	mv.addObject("webCollection", webCollection);
       	mv.addObject("collection", collectionId);

       	Page page = new Page();
       	Long totalOriginal = urlEntityDao.getTotalURL(collectionId, null, null).longValue();
       	Long totalExpanded = urlEntityDao.getTotalOriginalURL(collectionId, null, null).longValue();
       	page.setTotal(totalOriginal + totalExpanded);
       	Integer firstResult = (this.pageNumber - 1) * this.pageSize + 1;
       	Integer lastResult = this.pageNumber * this.pageSize;
       	if (lastResult > page.getTotal()) {
       		lastResult = page.getTotal().intValue();
       	}
       	page.setFirstResult(firstResult);
       	page.setLastResult(lastResult);
       	page.setPageNumber(this.pageNumber);
       	page.setNumberOfPages(page.getTotal() / this.pageSize);
       	page.setPreviousPage((firstResult > this.pageSize ? true : false));
       	page.setNextPage((lastResult < page.getTotal() ? true : false));
       	
       	mv.addObject("page", page);

    	return mv;
    }

    
    
    public void sortUrlEntitiesBy(List<UrlEntity> urlEntities, UrlEntityComparator.Order sortingBy, UrlEntityComparator.SortOrder sortOrder) {
    		UrlEntityComparator comparator = new UrlEntityComparator();
    		comparator.setSortOrder(sortOrder);
    		comparator.setSortingBy(sortingBy);
    		Collections.sort(urlEntities, comparator); // now we have a sorted list
    	}
    
    public void sortWebCollectionsBy(List<WebCollection> urlEntities, WebCollectionComparator.Order sortingBy, WebCollectionComparator.SortOrder sortOrder) {
    	WebCollectionComparator comparator = new WebCollectionComparator();
		comparator.setSortOrder(sortOrder);
		comparator.setSortingBy(sortingBy);
		Collections.sort(urlEntities, comparator); // now we have a sorted list
	}

	/**
	 * @return the pageSize
	 */
	public Integer getPageSize() {
		return pageSize;
	}

	/**
	 * @param pageSize the pageSize to set
	 */
	public void setPageSize(Integer pageSize) {
		this.pageSize = pageSize;
	}
}
