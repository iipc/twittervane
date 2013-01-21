<%@page import="uk.bl.wap.crowdsourcing.*"%>
<jsp:useBean id="searchTermDao" type="uk.bl.wap.crowdsourcing.dao.SearchTermDao" scope="request" />
<%@ include file="header.jsp" %>
<div id="main">
		<h1>Search Terms</h1>
		
		<table>
	        <tr>
	          <th class="first">Term</th>
	        </tr>
	    <%
	    String trclass = "row-a";
        for (SearchTerm searchterm : searchTermDao.getAllSearchTerms()) { 
        %>
        		<tr class="<%= trclass %>">
      				<td><a href="${pageContext.request.contextPath}/searctherm.html?id=<%= searchterm.getId() %>"><%= searchterm.getTerm() %></a> &nbsp;&nbsp;&nbsp;&nbsp;</td>
					<!--  <td><a href="searchterms.html?id=<%= searchterm.getId() %>">Delete</a></td>-->
	    		</tr>
	  	<%
	  		if ( trclass.contentEquals("row-a")) {
	  			trclass = "row-b";
	  		} else {
	  			trclass = "row-a";
	  		}
	  	} 
	  	%>
      	</table>
 </div>
<%@ include file="footer.jsp" %>