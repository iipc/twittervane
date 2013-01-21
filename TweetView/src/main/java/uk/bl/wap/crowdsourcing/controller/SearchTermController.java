package uk.bl.wap.crowdsourcing.controller;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import uk.bl.wap.crowdsourcing.dao.SearchTermDao;

@Controller
public class SearchTermController {
	
	@Autowired
    private SearchTermDao searchTermDao;
	
	@RequestMapping(value="/searchterm")
    public ModelAndView searchTerm(HttpServletRequest request) {
		
		ModelAndView mv = new ModelAndView();
    	
    	Map<String, String> message = new HashMap<String, String>();
		
		String term;
		term = request.getParameter("term");
		Long id = (long) 0;
		if (request.getParameter("id") != null) {
			id = (Long) Long.parseLong(request.getParameter("id"));
		}
		
		if (term != null && !term.isEmpty()) {
			searchTermDao.updateName(id, term);
			message.put("message", "Saved");
		}
		mv.addObject("searchTerm",searchTermDao.getSearchTermByid(id));
		mv.addObject("message",message);
		mv.setViewName("searchterm.jsp");
		return mv;
    }
}
