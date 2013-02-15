package uk.bl.wap.crowdsourcing.agent;

import java.util.List;

import uk.bl.wap.crowdsourcing.AppConfig;
import uk.bl.wap.crowdsourcing.SearchTerm;

/**
 * Start and stops the <code>TweetStreamAgent</code>
 * @author twoods
 *
 */
public interface TweetStreamAgentService {

	/**
	 * Starts the <code>TweetStreamAgent</code> service
	 */
	public void start();
	
	/**
	 * Stops the <code>TweetStreamAgent</code> service
	 */
	public void stop();
	
	public AppConfig getAppConfig();
	
	/**
	 * @return the analysisTriggerValue
	 */
	public Integer getAnalysisTriggerValue();

	/**
	 * @param analysisTriggerValue the analysisTriggerValue to set
	 */
	public void setAnalysisTriggerValue(Integer analysisTriggerValue);

	/**
	 * @return the lastStreamError
	 */
	public List<String> getLastStreamErrors();
	
	/**
	 * @param lastStreamErrors the lastStreamErrors to set
	 */
	public void setLastStreamErrors(List<String> lastStreamErrors);
	
	/**
	 * @return the displayLastStreamErrors
	 */
	public Integer getDisplayLastStreamErrors();

	/**
	 * @param displayLastStreamErrors the displayLastStreamErrors to set
	 */
	public void setDisplayLastStreamErrors(Integer displayLastStreamErrors);
	
	public List<SearchTerm> getAllSearchTerms();

}
