package uk.bl.wap.crowdsourcing.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import uk.bl.wap.crowdsourcing.Page;
import uk.bl.wap.crowdsourcing.SearchTerm;
import uk.bl.wap.crowdsourcing.UrlEntity;
import uk.bl.wap.crowdsourcing.WebCollection;
import uk.bl.wap.crowdsourcing.dao.TweetDao;
import uk.bl.wap.crowdsourcing.dao.UrlEntityDao;
import uk.bl.wap.crowdsourcing.dao.WebCollectionDao;

@Controller
public class UrlEntityController {
	
	@Autowired
    private UrlEntityDao urlEntityDao;
	
	@Autowired
	private WebCollectionDao webCollectionDao;
	
	@Autowired 
	TweetDao tweetDao;
	
	private enumReportType reportType = enumReportType.topUrl;
	private String filterUrl = "";
	private String filterDomain = "";
	private String u = "";  // Tweets by url
	private String d = "";  // Tweets by domain
	private String queryString = "";
	private String pagingString= "";
	private Integer rows = 100;
	private Integer start = 0;
	private Integer nextPage = 0;
	private Integer prevPage = 0;
	private Integer pageNumber = 1;
	/**
	 * The number of records that will appear on a page
	 */
	private Integer pageSize;
	private Integer collectionId = 0;
	
	private enum enumReportType {
		topUrl,
		popUrl,
		domain,
		failed,
		browseTopUrl
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
		if (request.getParameter("rows") != null) {
			rows = (int) Integer.parseInt(request.getParameter("rows"));
		} else {
			rows = 100;
		}
    	if (request.getParameter("pageNumber") != null) {
    		pageNumber = Integer.parseInt(request.getParameter("pageNumber"));
    	} else {
    		pageNumber = 1;
    	}
		start = (Integer)((pageNumber - 1) * pageSize);

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
		List<SearchTerm> searchTerms = null;
		
		if (!u.isEmpty() || !d.isEmpty()) {
			mv.addObject("urlEntityDao",urlEntityDao);
			mv.addObject("tweetDao",tweetDao);
			mv.setViewName("entity.jsp");
		} else if (this.collectionId > 0) {
			mv.addObject("urlEntityDao",urlEntityDao);
			mv.addObject("tweetDao",tweetDao);
			WebCollection webCollection = webCollectionDao.getCollectionById(collectionId);
			mv.addObject("webCollection", webCollection);
			mv.addObject("message", message);
			mv.setViewName("report.jsp");
			
		} else {
			mv.addObject("webCollectionDao",webCollectionDao);
			mv.setViewName("reports.jsp");
		}
		
		List<UrlEntity> failedUrlEntities = null;
		
		if (reportType.toString().equals("topUrl")) {
			rows = urlEntityDao.getTopUrl(collectionId.longValue(), filterUrl, filterDomain, null, null).size();
			List<Object[]> topUrls = urlEntityDao.getTopUrl(collectionId.longValue(), filterUrl, filterDomain, start, pageSize);
			mv.addObject("topUrls", topUrls);			
		} else if (reportType.toString().equals("domain")) {
			rows = urlEntityDao.getTopDomain(collectionId.longValue(), filterUrl, filterDomain, null, null).size();
			List<Object[]> topDomains = urlEntityDao.getTopDomain(collectionId.longValue(), filterUrl, filterDomain, start, pageSize);
			mv.addObject("topDomains", topDomains);
		} else if (reportType.toString().equals("popUrl")) {
			rows = urlEntityDao.getTopPopularity(collectionId.longValue(), filterUrl, filterDomain, null, null).size();
			List<Object[]> popularUrls = urlEntityDao.getTopPopularity(collectionId.longValue(), filterUrl, filterDomain, start, pageSize);
			mv.addObject("popularUrls", popularUrls);
		} else if (reportType.toString().equals("failed")) {
			rows = urlEntityDao.getTotalEntitiesFailedByCollection(collectionId.longValue()).intValue();
			failedUrlEntities = urlEntityDao.getUrlEntitiesFailedByCollection(collectionId.longValue(), start, pageSize);
			mv.addObject("failedUrlEntities", failedUrlEntities);
		}
		
       	Page page = new Page();
       	page.setTotal(rows.longValue());
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
       	mv.addObject("pageSize", pageSize);
       	mv.addObject("reportType", reportType);

		return mv;
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
