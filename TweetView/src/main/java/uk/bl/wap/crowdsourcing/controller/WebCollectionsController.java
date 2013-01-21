package uk.bl.wap.crowdsourcing.controller;

import java.text.ParseException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import uk.bl.wap.crowdsourcing.WebCollection;
import uk.bl.wap.crowdsourcing.dao.WebCollectionDao;

@Controller
public class WebCollectionsController {
	
	@Autowired
	private WebCollectionDao webCollectionDao;  
	 
    @RequestMapping(value="/collections")
    public ModelAndView crowdsourcing(HttpServletRequest request) throws ParseException {
    	
    	ModelAndView mv = new ModelAndView();
    	Map<String, String> message = new HashMap<String, String>();
    	
    	String jsp = "collections.jsp";
    	String name = request.getParameter("name");
    	String description = request.getParameter("description");
    	String startDate = request.getParameter("startDate");
    	String endDate = request.getParameter("endDate");
    	
    	Long id = 0L;
    	if (request.getParameter("id") != null) {
    		id = Long.parseLong(request.getParameter("id"));
    	}
        
        if (name != null) {
        	if (startDate != null && !startDate.isEmpty() && endDate != null && !endDate.isEmpty()) {
        		webCollectionDao.persist(new WebCollection(name,description,startDate,endDate)); 
        	} else {
        		message.put("message", "Start and end date required");
        	}
        } else if(id > 0) {
        	webCollectionDao.deleteWebCollection(id);
        }
        
        mv.addObject("message",message);
        mv.addObject("webCollectionDao", webCollectionDao);
        mv.setViewName(jsp);
        return mv;
    }
}
