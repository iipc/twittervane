package uk.bl.wap.crowdsourcing.controller;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import uk.bl.wap.crowdsourcing.dao.TweetDao;
import uk.bl.wap.crowdsourcing.dao.UrlEntityDao;
import uk.bl.wap.crowdsourcing.dao.UrlEntityFailedDao;
import uk.bl.wap.crowdsourcing.dao.WebCollectionDao;

@Controller
public class UrlEntityController {
	
	@Autowired
    private UrlEntityDao urlEntityDao;
	
	@Autowired
	private WebCollectionDao webCollectionDao;
	
	@Autowired 
	TweetDao tweetDao;
	
	@Autowired
	UrlEntityFailedDao urlEntityFailedDao;
	
	
	private enumReportType reportType = enumReportType.topUrl;
	private String filterUrl = "";
	private String filterDomain = "";
	private String u = "";  // Tweets by url
	private String d = "";  // Tweets by domain
	private String queryString = "";
	private String pagingString= "";
	private int rows = 100;
	private int start = 0;
	private int nextPage = 0;
	private int prevPage = 0;
	private int collectionId = 0;
	
	private enum enumReportType {
		topUrl,
		popUrl,
		domain,
		failed
	}
	/*
	@RequestMapping(
		    value = "/{id}.json",
		    method = RequestMethod.GET,
		    produces = "application/json")
		@ResponseBody
		public Person getDetailsAsJson(@PathVariable Long id) {
		    return personRepo.findOne(id);
		}
	*/
	@RequestMapping(value="/report")
    public ModelAndView crowdsourcing(HttpServletRequest request) {
		
		if (request.getParameter("u") != null) {
			u = request.getParameter("u");
		} else {
			u = "";
		}
		if (request.getParameter("d") != null) {
			d = request.getParameter("d");
		} else {
			d = "";
		}
		if (request.getParameter("reportType") != null) {
			reportType = enumReportType.valueOf(request.getParameter("reportType"));
		} else {
			reportType = enumReportType.topUrl;
		}
		if (request.getParameter("collectionId") != null) {
			collectionId = Integer.parseInt(request.getParameter("collectionId"));
		} else {
			collectionId = 0;
		}
		if (request.getParameter("filterUrl") != null) {
			filterUrl = request.getParameter("filterUrl");
		} else {
			filterUrl = null;
		}
		if (request.getParameter("filterDomain") != null) {
			filterUrl = request.getParameter("filterDomain");
		} else {
			filterUrl = null;
		}
		if (request.getParameter("start") != null) {
			start = (int) Integer.parseInt(request.getParameter("start"));
		} else {
			start = 0;
		}
		if (request.getParameter("rows") != null) {
			rows = (int) Integer.parseInt(request.getParameter("rows"));
		} else {
			rows = 100;
		}
		
		nextPage = start + rows;
		if (start - rows >= 0) {
			prevPage = start - rows;
		} else {
			prevPage = 0;
		}
		
		pagingString = "";
		queryString = "";
		queryString += "&collectionId=" + collectionId + "&reportType=" + reportType + "&filterUrl=" + filterUrl;
		queryString += "&filterDomain=" + filterDomain;
		
		ModelAndView mv = new ModelAndView();
		Map<String, String> message = new HashMap<String, String>();
		
		if (start > 0) {
			pagingString += "<a href=\"report.html?start=0&rows=" + rows + queryString + "\">Beginning</a>&nbsp;&nbsp;&nbsp;&nbsp;";
			pagingString += "<a href=\"report.html?start=" + prevPage + "&rows=" + rows + queryString + "\">Previous</a>";
		}
		pagingString += "&nbsp;&nbsp;&nbsp;&nbsp;<a href=\"report.html?start=" + nextPage + "&rows=" + rows + queryString + "\">Next</a>";
		
		message.put("paging",pagingString);
		
		if (!u.isEmpty() || !d.isEmpty()) {
			mv.addObject("urlEntityDao",urlEntityDao);
			mv.addObject("tweetDao",tweetDao);
			mv.setViewName("entity.jsp");
		} else if (this.collectionId > 0) {
			mv.addObject("urlEntityFailedDao",urlEntityFailedDao);
			mv.addObject("urlEntityDao",urlEntityDao);
			mv.addObject("tweetDao",tweetDao);
			mv.addObject("webCollection",webCollectionDao.getCollectionById(collectionId));
			mv.addObject("message", message);
			mv.setViewName("report.jsp");
			
		} else {
			mv.addObject("webCollectionDao",webCollectionDao);
			mv.setViewName("reports.jsp");
		}

		return mv;
    }
}
