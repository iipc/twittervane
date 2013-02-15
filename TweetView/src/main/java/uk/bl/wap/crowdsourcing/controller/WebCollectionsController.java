package uk.bl.wap.crowdsourcing.controller;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import uk.bl.wap.crowdsourcing.SearchTerm;
import uk.bl.wap.crowdsourcing.WebCollection;
import uk.bl.wap.crowdsourcing.dao.SearchTermDao;
import uk.bl.wap.crowdsourcing.dao.WebCollectionDao;

@Controller
public class WebCollectionsController {
	
	@Autowired
	private WebCollectionDao webCollectionDao;  
	
	@Autowired
    private SearchTermDao searchTermDao;
	 
    @RequestMapping(value="/collections")
    public ModelAndView crowdsourcing(HttpServletRequest request) throws ParseException {
    	
    	ModelAndView mv = new ModelAndView();
    	Map<String, String> message = new HashMap<String, String>();
    	
    	String jsp = "collections.jsp";
    	String name = request.getParameter("name");
    	String description = request.getParameter("description");
    	String startDate = request.getParameter("startDate");
    	String endDate = request.getParameter("endDate");
    	String searchTerms = request.getParameter("searchTerms");
    	String action = request.getParameter("formAction");
    	
    	Long id = 0L;
    	if (request.getParameter("id") != null) {
    		id = Long.parseLong(request.getParameter("id"));
    	}
        
    	Boolean showFormValues = false;
    	
    	if (action != null) {
	        if (action.equals("add")) {
	        	if (name != null && !name.trim().equals("") && startDate != null && !startDate.isEmpty() && endDate != null && !endDate.isEmpty()) {
	        		WebCollection webCollection = new WebCollection(name, description, startDate, endDate);
	            	String[] terms = null;
	            	if (searchTerms != null) {
	            		terms = searchTerms.split(",");
	            	}
	            	List<SearchTerm> tweetSearchTerms = new ArrayList<SearchTerm>();
	            	webCollection.setSearchTerms(tweetSearchTerms);
	        		if (terms != null && terms.length > 0) {
	        			for (String term : terms) {
	        				SearchTerm searchTerm = new SearchTerm();
	        				searchTerm.setTerm(term.trim());
	        				webCollection.getSearchTerms().add(searchTerm);
	        				searchTerm.setWebCollection(webCollection);
	        			}
	        		}
	        		webCollectionDao.persist(webCollection);
	
	        	} 
	        	if (startDate == null || startDate.trim() == "" || endDate == null || endDate.trim() == "") {
	        		message.put("message", "Start and end date required");
	        		showFormValues = true;
	        	}
	        }
	        if (name == null || (name.trim() == "")) {
	        	message.put("message", "Name is a required field");
	        }
	        if (action.equals("delete")) {
	        	webCollectionDao.deleteWebCollection(id);
	    	}
    	}  
        
        mv.addObject("message",message);
        mv.addObject("webCollectionDao", webCollectionDao);
        if (showFormValues) {
	        mv.addObject("name", name);
	        mv.addObject("description", description);
	        mv.addObject("startDate", startDate);
	        mv.addObject("endDate", endDate);
        }
        mv.setViewName(jsp);
        return mv;
    }
}
