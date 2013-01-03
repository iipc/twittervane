package uk.bl.wap.crowdsourcing;

import java.io.Serializable;
import java.util.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;

@Entity
public class WebCollection implements Serializable {
	private static final long serialVersionUID = 1L;
	
	 // Persistent Fields:
    @Id @GeneratedValue
    Long id;
    private String name;
    private String description;
    private Date creationDate;
    private Date startDate;
    private Date endDate;
    @OneToMany(fetch=FetchType.EAGER) Collection<SearchTerm> searchterms;
   
    // Constructors:
    public WebCollection() {
    }
 
    public WebCollection(String name) {
        this.name = name;
        this.creationDate = new Date(System.currentTimeMillis());
    }
    
    public WebCollection(String name,String description,String startDate,String endDate) {
        this.name = name;
        this.description = description;
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MMM-yyyy"); 
		Date convertedDate;
		try {
			convertedDate = dateFormat.parse(startDate);
			this.startDate = convertedDate;
		} catch (ParseException e) {
			//log.severe("Error converting date: " + e.getMessage());
			//System.err.print(e.getMessage());
		} 
		
		try {
			convertedDate = dateFormat.parse(endDate);
			this.endDate = convertedDate;
		} catch (ParseException e) {
		//	log.severe("Error converting date: " + e.getMessage());
		//	System.err.print(e.getMessage());
		} 
        this.creationDate = new Date(System.currentTimeMillis());
    }
 
    // String Representation:
    @Override
    public String toString() {
        return name + " (signed on " + creationDate + ")";
    }

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Date getCreationDate() {
		return creationDate;
	}

	public void setCreationDate(Date creationDate) {
		this.creationDate = creationDate;
	}

	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	public Date getEndDate() {
		return this.endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	public Collection<SearchTerm> getSearchterms() {
		if (searchterms == null) {
			searchterms = new ArrayList<SearchTerm>();
		}
		return searchterms;
	}

	public void setSearchterms(Collection<SearchTerm> searchterms) {
		this.searchterms = searchterms;
	}
	
	public void addSearchTerm(final SearchTerm st) {
        getSearchterms().add(st);
    }
 
    public void fire(final SearchTerm st) {
        getSearchterms().remove(st);
    }

	public Long getId() {
		return id;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
}