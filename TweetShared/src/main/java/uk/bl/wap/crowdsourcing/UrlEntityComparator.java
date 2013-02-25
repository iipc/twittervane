package uk.bl.wap.crowdsourcing;

import java.util.Comparator;

public class UrlEntityComparator implements Comparator<UrlEntity> {
	public enum Order {
		urlFull, totalTweets, totalRetweets, tweeter, tweet
	}
	
	public enum SortOrder {
		asc, desc
	}

	private Order sortingBy = Order.totalTweets;
	
	private SortOrder sortOrder = SortOrder.desc;

	public int compare(UrlEntity url1, UrlEntity url2) {
		switch (sortingBy) {
		case urlFull:
			if (url1.getUrlFull() == null || url2.getUrlFull() == null)
				return 0;
			if (this.sortOrder == SortOrder.asc) {
				return url1.getUrlFull().compareTo(url2.getUrlFull());
			} else {
				return url2.getUrlFull().compareTo(url1.getUrlFull());
			}
		case totalTweets:
			if (this.sortOrder == SortOrder.asc) {
				return url1.getTotalTweets().compareTo(url2.getTotalTweets());
			} else {
				return url2.getTotalTweets().compareTo(url1.getTotalTweets());
			}
		case totalRetweets:
			if (this.sortOrder == SortOrder.asc) {
				return url1.getTotalRetweets().compareTo(url2.getTotalRetweets());
			} else {
				return url2.getTotalRetweets().compareTo(url1.getTotalRetweets());
			}
		case tweeter:
			if (this.sortOrder == SortOrder.asc) {
				return url1.getTweet().getName().compareTo(url2.getTweet().getName());
			} else {
				return url2.getTweet().getName().compareTo(url1.getTweet().getName());
			}
		case tweet:
			if (this.sortOrder == SortOrder.asc) {
				return url1.getTweet().getText().compareTo(url2.getTweet().getText());
			} else {
				return url2.getTweet().getText().compareTo(url1.getTweet().getText());
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