package uk.bl.wap.crowdsourcing.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import uk.bl.wap.crowdsourcing.ReportUrlEntityList;
import uk.bl.wap.crowdsourcing.dao.UrlEntityDao;

@Controller
public class XmlReportController {
	
	@Autowired
	private UrlEntityDao urlEntityDao;  
	
	@RequestMapping(value="/xmlreport/{collectionId}", method = RequestMethod.GET)
	public @ResponseBody ReportUrlEntityList getUrlEntities1(@PathVariable String collectionId) {
		
		long cid = Long.parseLong(collectionId);
		String filterUrl = null;
		String filterDomain = null;
		
		ReportUrlEntityList urlEntityList = new ReportUrlEntityList(urlEntityDao.getAllUrlEntitiesByCollection(cid, filterUrl, filterDomain));
 
		return urlEntityList;
	}
	
	@RequestMapping(value="/xmlreport/{collectionId}/filterDomain/{filterDomain}", method = RequestMethod.GET)
	public @ResponseBody ReportUrlEntityList getUrlEntities2(@PathVariable String collectionId,@PathVariable String filterDomain) {
		
		long cid = Long.parseLong(collectionId);
		String filterUrl = null;
		if (filterDomain != null) {
			filterDomain = filterDomain.replaceAll("__", ".");
		}
		
		ReportUrlEntityList urlEntityList = new ReportUrlEntityList(urlEntityDao.getAllUrlEntitiesByCollection(cid, filterUrl, filterDomain));
 
		return urlEntityList;
	}
	
	@RequestMapping(value="/xmlreport/{collectionId}/filterUrl/{filterUrl}", method = RequestMethod.GET)
	public @ResponseBody ReportUrlEntityList getUrlEntities3(@PathVariable String collectionId,@PathVariable String filterUrl) {
		
		long cid = Long.parseLong(collectionId);
		String filterDomain = null;
		if (filterUrl != null) {
			filterUrl = filterUrl.replaceAll("__", ".");
		}
		
		ReportUrlEntityList urlEntityList = new ReportUrlEntityList(urlEntityDao.getAllUrlEntitiesByCollection(cid, filterUrl, filterDomain));
 
		return urlEntityList;
	}
	
	@RequestMapping(value="/xmlreport/{collectionId}/filterDomain/{filterDomain}/filterUrl/{filterUrl}", method = RequestMethod.GET)
	public @ResponseBody ReportUrlEntityList getUrlEntities4(@PathVariable String collectionId,@PathVariable String filterDomain,@PathVariable String filterUrl) {
		
		long cid = Long.parseLong(collectionId);
		
		ReportUrlEntityList urlEntityList = new ReportUrlEntityList(urlEntityDao.getAllUrlEntitiesByCollection(cid, filterUrl.replaceAll("__", "."), filterDomain.replaceAll("__", ".")));
 
		return urlEntityList;
	}
	
	//@RequestMapping(value="", method=RequestMethod.GET)
	//public @ResponseBody

}
