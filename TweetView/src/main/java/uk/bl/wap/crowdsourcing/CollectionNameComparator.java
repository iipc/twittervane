package uk.bl.wap.crowdsourcing;

public class CollectionNameComparator {
    public int compare(UrlEntity o1, UrlEntity o2)
    {
       return o1.getCollectionName().compareTo(o2.getCollectionName());
   }
}
