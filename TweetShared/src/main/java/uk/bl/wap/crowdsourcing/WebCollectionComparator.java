package uk.bl.wap.crowdsourcing;

import java.util.Comparator;

public class WebCollectionComparator implements Comparator<WebCollection> {
	public enum Order {
		collectionName, totalTweets, totalUrlsOriginal, totalUrlsFull, totalUrlErrors
	}
	
	public enum SortOrder {
		asc, desc 
	}

	private Order sortingBy = Order.totalTweets;
	
	private SortOrder sortOrder = SortOrder.desc;

	public int compare(WebCollection wc1, WebCollection wc2) {
		switch (sortingBy) {
		case collectionName:
			if (this.sortOrder == SortOrder.asc) {
			return wc1.getName().compareTo(wc2.getName());
			} else {
				return wc2.getName().compareTo(wc1.getName());
			}
		case totalTweets:
			if (this.sortOrder == SortOrder.asc) {
				return wc1.getTotalTweets().compareTo(wc2.getTotalTweets());
			} else {
				return wc2.getTotalTweets().compareTo(wc1.getTotalTweets());
			}
		case totalUrlsOriginal:
			if (this.sortOrder == SortOrder.asc) {
				return wc1.getTotalUrlsOriginal().compareTo(wc2.getTotalUrlsOriginal());
			} else {
				return wc2.getTotalUrlsOriginal().compareTo(wc1.getTotalUrlsOriginal());
			}
		case totalUrlsFull:
			if (this.sortOrder == SortOrder.asc) {
				return wc1.getTotalUrlsExpanded().compareTo(wc2.getTotalUrlsExpanded());
			} else {
				return wc2.getTotalUrlsExpanded().compareTo(wc1.getTotalUrlsExpanded());
			}
		case totalUrlErrors:
			if (this.sortOrder == SortOrder.asc) {
				return wc1.getTotalUrlErrors().compareTo(wc2.getTotalUrlErrors());
			} else {
				return wc2.getTotalUrlErrors().compareTo(wc1.getTotalUrlErrors());
			}
		}
		throw new RuntimeException(
				"Practically unreachable code, can't be thrown");
	}

	public void setSortingBy(Order sortBy) {
		this.sortingBy = sortBy;
	}
	
	public void setSortOrder(SortOrder sortOrder) {
		this.sortOrder = sortOrder;
	}

}