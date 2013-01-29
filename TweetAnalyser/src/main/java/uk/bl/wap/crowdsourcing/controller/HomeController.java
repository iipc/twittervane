package uk.bl.wap.crowdsourcing.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class HomeController {
	
	@RequestMapping(value="/home")
    public ModelAndView crowdsourcing(HttpServletRequest request) {
        return new ModelAndView("jobs.jsp");
    }
}
