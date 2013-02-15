package uk.bl.wap.crowdsourcing.controller;

import java.text.ParseException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import twitter4j.FilterQuery;
import uk.bl.wap.crowdsourcing.SearchTerm;
import uk.bl.wap.crowdsourcing.Util;
import uk.bl.wap.crowdsourcing.WebCollection;
import uk.bl.wap.crowdsourcing.dao.SearchTermDao;
import uk.bl.wap.crowdsourcing.dao.WebCollectionDao;

@Controller
public class WebCollectionController {
	
	@Autowired
	private WebCollectionDao webCollectionDao;  
	
	@Autowired
	private SearchTermDao searchTermDao;
	 
    @RequestMapping(value="/collection")
    public ModelAndView crowdsourcing(HttpServletRequest request) throws ParseException {

    	ModelAndView mv = new ModelAndView();
    	
    	Map<String, String> message = new HashMap<String, String>();
    	String jsp = "collection.jsp";
    	String name = request.getParameter("name");
        Long id = 0L;
        if (request.getParameter("id") != null) {
        	id = Long.parseLong(request.getParameter("id"));
        }
		
        String term = null;
        if (request.getParameter("term") != null && !request.getParameter("term").isEmpty()) {
        	term = request.getParameter("term");
        }
        String startDate = request.getParameter("startDate");
        String endDate = request.getParameter("endDate");
        
        String action = request.getParameter("action");
        String searchId = request.getParameter("searchTermId");
        Long searchTermId = null;
        if (searchId != null) {
        	searchTermId = Long.parseLong(searchId);
        }
        
        WebCollection collection = webCollectionDao.getCollectionById(id);
        
        if (action != null) {

        	// Update twitter Stream filter
        	long[] followArray = null;

        	if (term != null && action.equals("add")) {
        		String trackList = buildTrackList();
	        	SearchTerm searchTerm = new SearchTerm(term);
	        	searchTerm.setWebCollection(collection);
	        	collection.addSearchTerm(searchTerm);
	        	
	    		if (trackList.length() > 0) {
	    			trackList = trackList.substring(0, trackList.lastIndexOf(","));
	    		}
	    		String[] trackArray = {trackList};
	    		
	    		if (Util.twitterStream != null) {
	    			Util.twitterStream.filter(new FilterQuery(0, followArray, trackArray));
	    		}
	            if (name != null) {
	            	webCollectionDao.persist(collection);
	            	message.put("message", "Saved");
	            }
	        }
	        
	        if (action.equals("delete")) {
	        	// remove the search term from the web collection
	        	SearchTerm searchTerm = searchTermDao.getSearchTermByid(searchTermId);
	        	List<SearchTerm> items = collection.getSearchTerms();
	        	for (int i=0; i<items.size(); i++) {
	        		SearchTerm item = items.get(i);
	        		if (item.getId().equals(searchTerm.getId())) {
	        			items.remove(i);
	        		}
	        	}
	        	webCollectionDao.persist(collection);
	        	
	        	// remove the search term from the rack array
	        	String trackList = buildTrackList();
	    		if (trackList.length() > 0) {
	    			trackList = trackList.substring(0, trackList.lastIndexOf(","));
	    		}
	    		String[] trackArray = {trackList};
	    		
	    		if (Util.twitterStream != null) {
	    			Util.twitterStream.filter(new FilterQuery(0, followArray, trackArray));
	    		}

	        }
        }
        
        // re-fetch the web collection so that any search terms added will have an id allocated
        collection = webCollectionDao.getCollectionById(id);
        
        mv.addObject("searchTermDao",searchTermDao);
        mv.addObject("webCollection", collection);
        mv.addObject("message",message);
        mv.setViewName(jsp);
        return mv;
 
    }
    
    private String buildTrackList() {
    	String trackList = "";
		
		for (WebCollection webCollection: webCollectionDao.getAllCollectionsForStream()) {
			for (SearchTerm st : webCollection.getSearchTerms()) {
				trackList += st.getTerm() + ",";
			}
		}
		return trackList;
    }
}
