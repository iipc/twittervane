package uk.bl.wap.crowdsourcing.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import uk.bl.wap.crowdsourcing.SearchTerm;
import uk.bl.wap.crowdsourcing.dao.SearchTermDao;

@Controller
public class SearchTermsController {
	
	@Autowired
    private SearchTermDao searchTermDao;
	
	@RequestMapping(value="/searchterms")
    public ModelAndView crowdsourcing(HttpServletRequest request) {
		
		String term;

		term = request.getParameter("term");
		Long id = (long) 0;
		if (request.getParameter("id") != null) {
			id = (Long) Long.parseLong(request.getParameter("id"));
		}
		
		if (term != null) {
			searchTermDao.persist(new SearchTerm(term));
		} else if(id > 0 )  {
			searchTermDao.deleteSearchTerm(id);
		}

		return new ModelAndView("searchterms.jsp", "searchTermDao", searchTermDao);
    }
}
