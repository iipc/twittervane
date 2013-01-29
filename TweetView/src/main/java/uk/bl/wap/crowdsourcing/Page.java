package uk.bl.wap.crowdsourcing;

public class Page {

	private Long numberOfPages = 0L;
	
	private Integer pageNumber = 0;
	
	private Integer firstResult = 0;
	
	private Integer lastResult = 0;
	
	private Long total = 0L;
	
	private Boolean previousPage = false;
	
	private Boolean nextPage = false;

	/**
	 * @return the numberOfPages
	 */
	public Long getNumberOfPages() {
		return numberOfPages;
	}

	/**
	 * @return the pageNumber
	 */
	public Integer getPageNumber() {
		return pageNumber;
	}

	/**
	 * @return the firstResult
	 */
	public Integer getFirstResult() {
		return firstResult;
	}

	/**
	 * @return the lastResult
	 */
	public Integer getLastResult() {
		return lastResult;
	}

	/**
	 * @return the previousPage
	 */
	public Boolean getPreviousPage() {
		return previousPage;
	}

	/**
	 * @return the nextPage
	 */
	public Boolean getNextPage() {
		return nextPage;
	}

	/**
	 * @param numberOfPages the numberOfPages to set
	 */
	public void setNumberOfPages(Long numberOfPages) {
		this.numberOfPages = numberOfPages;
	}

	/**
	 * @param pageNumber the pageNumber to set
	 */
	public void setPageNumber(Integer pageNumber) {
		this.pageNumber = pageNumber;
	}

	/**
	 * @param firstResult the firstResult to set
	 */
	public void setFirstResult(Integer firstResult) {
		this.firstResult = firstResult;
	}

	/**
	 * @param lastResult the lastResult to set
	 */
	public void setLastResult(Integer lastResult) {
		this.lastResult = lastResult;
	}

	/**
	 * @param previousPage the previousPage to set
	 */
	public void setPreviousPage(Boolean previousPage) {
		this.previousPage = previousPage;
	}

	/**
	 * @param nextPage the nextPage to set
	 */
	public void setNextPage(Boolean nextPage) {
		this.nextPage = nextPage;
	}

	/**
	 * @return the total
	 */
	public Long getTotal() {
		return total;
	}

	/**
	 * @param total the total to set
	 */
	public void setTotal(Long total) {
		this.total = total;
	}
}
