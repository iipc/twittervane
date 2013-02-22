package uk.bl.wap.crowdsourcing;

import java.util.Comparator;

public class WebCollectionComparator implements Comparator<WebCollection> {
	public enum Order {
		collectionName, totalTweets, totalUrlsOriginal, totalUrlsFull, totalUrlErrors, totalTopUrlTweets, totalTopUrlRetweets, topUrl
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
		case topUrl:
			if (this.sortOrder == SortOrder.asc) {
				if (wc1.getTopUrl() != null && wc2.getTopUrl() != null){
					return wc1.getTopUrl().compareTo(wc2.getTopUrl());
				} else if (wc1.getTopUrl() == null) {
					return -1;
				} else {
					return 1;
				}
			} else {
				if (wc1.getTopUrl() != null && wc2.getTopUrl() != null) {
					return wc2.getTopUrl().compareTo(wc1.getTopUrl());
				} else if (wc1.getTopUrl() == null) {
					return 1;
				} else {
					return -1;
				}
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
		case totalTopUrlTweets:
			if (this.sortOrder == SortOrder.asc) {
				return wc1.getTopUrlTweets().compareTo(wc2.getTopUrlTweets());
			} else {
				return wc2.getTopUrlTweets().compareTo(wc1.getTopUrlTweets());
			}
		case totalTopUrlRetweets:
			if (this.sortOrder == SortOrder.asc) {
				return wc1.getTopUrlRetweets().compareTo(wc2.getTopUrlRetweets());
			} else {
				return wc2.getTopUrlRetweets().compareTo(wc1.getTopUrlRetweets());
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