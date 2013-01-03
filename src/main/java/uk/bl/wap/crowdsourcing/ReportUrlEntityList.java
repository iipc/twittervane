package uk.bl.wap.crowdsourcing;

import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;


@XmlRootElement(name = "report")
public class ReportUrlEntityList {
		private int count;
		public List<ReportUrlEntity> urlentity;
	
		public ReportUrlEntityList() {}
		
		public ReportUrlEntityList(List<ReportUrlEntity> reporturlentities) {
			this.urlentity = reporturlentities;
			this.count = reporturlentities.size();
		}
		
		public int getCount() {
			return count;
		}
		public void setCount(int count) {
			this.count = count;
		}
		
		@XmlElement(name="urlentity")
		public List<ReportUrlEntity> getReportUrlEntities() {
			return this.urlentity;
		}
		
		public void setReportUrlEntity(List<ReportUrlEntity> reporturlentities) {
			this.urlentity = reporturlentities;
		}
}


