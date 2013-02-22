<%@page import="uk.bl.wap.crowdsourcing.*"%>
<%@taglib prefix = "c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@page import="java.util.Iterator" %>
<%@page import="java.text.SimpleDateFormat" %>

<jsp:useBean id="webCollectionDao" type="uk.bl.wap.crowdsourcing.dao.WebCollectionDao" scope="request" />
<%@ include file="header.jsp" %>
<script type="text/javascript">
function submitForm(collectionId, commandAction) {
	$('#formAction').val(commandAction);
	$('#id').val(collectionId);
	$('#collectionsForm').submit();
}
</script>
<div id="main">
       <form method="POST" action="collections.html" id="collectionsForm" name="collectionsForm">

		<h1>Collections</h1>
        <table border="0" width="100%">
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
	        	 if (!webcollection.getName().equals(webCollectionDao.getUnknownCollectionName())) {
	         %>
	          
	         <tr class="<%= trclass %>">
	         	<td class="first"><a href ="./collection.html?id=<%= webcollection.getId() %>"><%= webcollection.getName() %></a></td>
	          	<td><%= sdf.format(webcollection.getStartDate()) %></td>
	          	<td><%= sdf.format(webcollection.getEndDate()) %></td>
	         	<td>
	         	 <%
	         	 for (SearchTerm searchTerm : webcollection.getSearchTerms()) {
	         	 %>
	         		<a href="./searchterm.html?id=<%= searchTerm.getId() %>"><%= searchTerm.getTerm() %></a><br/>		 
	         	 <%
	         	 }
	         	 %>
	         	</td>
	         	<td><a style="cursor: pointer;" onclick="return submitForm(<%= webcollection.getId() %>, 'delete');">Delete</a></td>
	       	 </tr>
	  		<%
	  			if ( trclass.contentEquals("row-a")) {
	  				trclass = "row-b";
	  			} else {
	  				trclass = "row-a";
	  			}
	        	 }
	  		} 
	  		%>
      	</table>
      	<h1>Add New Collection</h1>
            <label>Name</label> <input type="text" name="name" maxlength="100" class="required" value="${name}" />&nbsp;&nbsp;&nbsp;&nbsp;
            <label>Description</label> <input type="text" name="description" maxlength="255" class="required" value="${description}" />
            <label>Start Date</label><input type="text" name="startDate" maxlength="10" class="date-pick" value="${startDate}" class="required">
            <label>End Date</label><input type="text" name="endDate" maxlength="10" class="date-pick" value="${endDate}" class="required">
            <label>Search Terms</label><textarea name="searchTerms" cols="40" rows="6" style="width:350px;"></textarea>(comma delimited list)
            <br/>
            <input type="button" value="Add Collection" onclick="return submitForm(0, 'add');"/><font color="red">${message.message}</font>
            <input type="hidden" name="formAction" id="formAction" value="" />
            <input type="hidden" name="id" id="id" value="" />
        </form>
 </div>
<%@ include file="footer.jsp" %>