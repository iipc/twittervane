package uk.bl.wap.crowdsourcing;

public class TotalRetweetsComparator {
    public int compare(UrlEntity o1, UrlEntity o2)
    {
       return o1.getTotalRetweets().compareTo(o2.getTotalRetweets());
   }
}
