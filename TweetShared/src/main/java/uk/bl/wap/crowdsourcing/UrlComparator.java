package uk.bl.wap.crowdsourcing;

public class UrlComparator {
    public int compare(UrlEntity o1, UrlEntity o2)
    {
       return o1.getUrlFull().compareTo(o2.getUrlFull());
   }
}
