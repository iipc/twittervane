package uk.bl.wap.crowdsourcing.tags;


import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;

public class SortHeaderTag extends TagSupport {


	/**
	 * to support serialisation
	 */
	private static final long serialVersionUID = 1;
	
	/**
	 * the column name to sort
	 */
	private String column;
	
	/**
	 * the column name
	 */
	private String thisColumn;
	
	/**
	 * the sort order (asc/desc)
	 */
	private String sort;
	
	/**
	 * the name displayed for the column
	 */
	private String displayName;
	
	private static final String QUOTE = "\"";
    
 	@Override
	public int doStartTag() throws JspException  {
		
 		StringBuilder output = new StringBuilder();
 		String nextSort = null;

 		if (sort.equals("desc")) {
 			nextSort = "asc";
 		} else {
 			nextSort = "desc";
 		}
 		
		output.append("<table style='cursor: pointer;' border='0' cellpadding='0' cellspacing='0'><tr ");
 		output.append("onclick=");
    	output.append(QUOTE);
    	output.append("submitForm('");
    	output.append(thisColumn);
    	output.append("', '");
    	output.append(nextSort);
    	output.append("');");
    	output.append(QUOTE);
    	output.append(">");
 		output.append("<td>");
		
	    if (sort.equals("desc") && thisColumn.equals(column)) {
	    	output.append("<img src='./public-resources/images/sort-arrow-down.gif' onclick=");
	    } else if (thisColumn.equals(column)) {
	    	output.append("<img src='./public-resources/images/sort-arrow-up.gif' onclick=");
	    };
	    if (thisColumn.equals(column)) {
	    	output.append(QUOTE);
	    	output.append("submitForm('");	    	
	    	output.append(column);
	    	output.append("', '");
	    	output.append(nextSort);
	    	output.append("');");
	    	output.append(QUOTE);
	    	output.append(" />");
	    }
    	output.append("</td><td>");
      	output.append(displayName);
    	output.append("</td></tr></table>");

 		try {
			pageContext.getOut().print(output);
		} catch (IOException e) {
			throw new JspException(e);
		}
		
		// release resources to prevent memory leak
		column = null;
		sort = null;
		
		return TagSupport.SKIP_BODY;
	}

	/**
	 * @return the column
	 */
	public String getColumn() {
		return column;
	}

	/**
	 * @return the sort
	 */
	public String getSort() {
		return sort;
	}

	/**
	 * @param column the column to set
	 */
	public void setColumn(String column) {
		this.column = column;
	}

	/**
	 * @param sort the sort to set
	 */
	public void setSort(String sort) {
		this.sort = sort;
	}

	/**
	 * @return the displayName
	 */
	public String getDisplayName() {
		return displayName;
	}

	/**
	 * @param displayName the displayName to set
	 */
	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}

	/**
	 * @return the thisColumn
	 */
	public String getThisColumn() {
		return thisColumn;
	}

	/**
	 * @param thisColumn the thisColumn to set
	 */
	public void setThisColumn(String thisColumn) {
		this.thisColumn = thisColumn;
	}
	
 	
}
