<%@page import="uk.bl.wap.crowdsourcing.*"%>
<%@page import="java.util.Iterator" %>
<%@page import="java.text.SimpleDateFormat" %>
<jsp:useBean id="webCollectionDao" type="uk.bl.wap.crowdsourcing.dao.WebCollectionDao" scope="request" />
<%@ include file="header.jsp" %>
<div id="main">
		<h1>Collections</h1>
        <table>
	        <tr>
	          <th class="first">Collection</th>
	          <th>Start Date</th>
	          <th>End Date</th>
	          <th>Search Terms</th>
	          <th></th>
	        </tr>
	        <%
	        String trclass = "row-a";
	        %>
	         <%
	         SimpleDateFormat sdf = new SimpleDateFormat("MMMM d, yyyy");
	         for (WebCollection webcollection : webCollectionDao.getAllCollections()) { 
	         %>
	          
	         <tr class="<%= trclass %>">
	         	<td class="first"><a href ="${pageContext.request.contextPath}/collection.html?id=<%= webcollection.getId() %>"><%= webcollection.getName() %></a></td>
	          	<td><%= sdf.format(webcollection.getStartDate()) %></td>
	          	<td><%= sdf.format(webcollection.getEndDate()) %></td>
	         	<td>
	         	 <%
	         	 Iterator<SearchTerm> iterator = webcollection.getSearchterms().iterator();
	         	 SearchTerm searchterm = null;
	         	 while (iterator.hasNext()) {
	         		 searchterm = (SearchTerm) iterator.next();
	         	 %>
	         		<a href="${pageContext.request.contextPath}/searchterm.html?id=<%= searchterm.getId() %>"><%= searchterm.getTerm() %></a><br/>		 
	         	 <%
	         	 }
	         	 %>
	         	</td>
	         	<td><a href="${pageContext.request.contextPath}/collections.html?id=<%= webcollection.getId() %>">Delete</a></td>
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
      	<h1>Add New Collection</h1>
       <form method="POST" action="collections.html">
            <label>Name</label> <input type="text" name="name" maxlength="100" class="required" />&nbsp;&nbsp;&nbsp;&nbsp;
            <label>Start Date</label><input type="text" name="startDate" maxlength="10" class="date-pick" class="required">
            <label>End Date</label><input type="text" name="endDate" maxlength="10" class="date-pick" class="required">
            <input type="submit" value="Add" /><font color="red">${message.message}</font>
        </form>
 </div>
<%@ include file="footer.jsp" %>