package uk.bl.wap.crowdsourcing;

public class TotalTweetsComparator {
    public int compare(UrlEntity o1, UrlEntity o2)
    {
       return o1.getTotalTweets().compareTo(o2.getTotalTweets());
   }
}
