package uk.bl.wap.crowdsourcing.agent;

public interface TweetAnalyserService {

	/**
	 * Run the Tweet analysis
	 * @param jobNumber The number of tweets to process
	 */
	public void run();
	 
	/**
	 * Abort a running Tweet analysis
	 */
	public void abort();
	
	/**
	 * Pause a running Tweet analysis
	 */
	public void pause();
	
	/**
	 * Resume a paused Tweet analysis
	 */
	public void resume();
	
	/**
	 * Fetch the current status of the Tweet analyser
	 * @return TweetAnalyserStatusEnum the analyser status
	 */
	public TweetAnalyserStatusEnum getStatus();
	
	/**
	 * Fetch the number of tweets that have been processed
	 * @return
	 */
	public Integer getProcessCounter();
	
	/**
	 * Fetch the total number of stored tweets
	 */
	public Number getTotalTweets();
	
	/**
	 * Fetch the number of unprocessed tweets
	 * @return
	 */
	public Number getTotalUnprocessed();
	
	/**
	 * Fetch the number of processed UrlEntities
	 */
	public Number getTotalProcessedEntities();
	
	/**
	 * @param jobNumber The number of tweets to process

	 */
	public void setJobNumber(Integer jobNumber);
	
	/**
	 * @param topUrls the topUrls to set
	 */
	public void setTopUrls(Integer topUrls);
	
	/**
	 * Delete all URL Entities
	 */
	public void purgeUrlEntities();
	
	/**
	 * Delete all Tweets
	 */
	public void purgeTweets();
	
	/**
	 * Delete all processed Tweets
	 */
	public void purgeProcessedTweets();
	
	/**
	 * Delete all URL entries that have failed analysis
	 */
	public void purgeFailedUrls();
}
