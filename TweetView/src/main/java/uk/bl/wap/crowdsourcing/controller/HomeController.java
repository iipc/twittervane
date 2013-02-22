package uk.bl.wap.crowdsourcing.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class HomeController {
	
	private Boolean upgradeNotice = false;
	
	@RequestMapping(value="/home")
    public ModelAndView crowdsourcing(HttpServletRequest request) {
		if (!upgradeNotice) {
			return new ModelAndView("home.jsp");
		} else {
			return new ModelAndView("upgrade.jsp");
		}
    }

	/**
	 * @return the upgradeNotice
	 */
	public Boolean getUpgradeNotice() {
		return upgradeNotice;
	}

	/**
	 * @param upgradeNotice the upgradeNotice to set
	 */
	public void setUpgradeNotice(Boolean upgradeNotice) {
		this.upgradeNotice = upgradeNotice;
	}
}
