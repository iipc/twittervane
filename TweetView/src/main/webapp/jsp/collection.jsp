<%@page import="uk.bl.wap.crowdsourcing.*"%>
<%@taglib prefix = "c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@page import="java.util.Iterator" %>
<%@page import="java.text.SimpleDateFormat" %>

<jsp:useBean id="webCollection" type="uk.bl.wap.crowdsourcing.WebCollection" scope="request" />
<jsp:useBean id="searchTermDao" type="uk.bl.wap.crowdsourcing.dao.SearchTermDao" scope="request" />

<%@ include file="header.jsp" %>
<script type="text/javascript">
function submitForm(searchTermId, action) {
	$('#action').val(action);
	$('#searchTermId').val(searchTermId);
	$('#collectionForm').submit();
}
</script>
<%
SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yyyy");
%>
<div id="main">
<h1>Edit Collection</h1>
        <form method="POST" action="collection.html" name="collectionForm" id="collectionForm">
        <input type="hidden" value="<%= request.getParameter("id") %>" name="id">
  		<label>Name</label><input type="text" name="name" maxlength="100" value="<%= webCollection.getName() %>"/>
  		<label>Description</label><input type="text" name="description" maxlength="255" value="<%= webCollection.getDescription() %>"/>
  		<label>Start Date</label><input type="text" name="startDate" maxlength="10" class="date-pick" value="<%= sdf.format(webCollection.getStartDate()) %>"/>
        <label>End Date</label><input type="text" name="endDate" maxlength="10" class="date-pick" value="<%= sdf.format(webCollection.getEndDate()) %>"/>
  		<label>Add Term</label><input type="text" name="term" maxlength="50" />

		<input type="button" value="Save" onclick="return submitForm(0, 'add')" />&nbsp;&nbsp;<font color="red">${message.message}</font>
		<input type="hidden" name="action" id="action" value="" />
		<input type="hidden" name="searchTermId" id="searchTermId" value="" />
        </form>
        
        <h1>Current Search Terms</h1>
        <c:set var="trclass" value="row-a" scope="page" />
        <table border="0" width="100%">
		<c:forEach items="${webCollection.searchTerms}" var="searchTerm">
		<tr class="${trclass}">
			<td>
			<a href="./searchterm.html?id=${searchTerm.id}"><c:out value="${searchTerm.term}" /></a>
			</td><td style="cursor: pointer;" width="20%">
			<a onclick="return submitForm(${searchTerm.id}, 'delete');" >Delete</a>
			</td>
			<c:choose>
		    	<c:when test="${trclass eq 'row-a' }" >
		    		<c:set var="trclass" value="row-b" scope="page" />
		    	</c:when>
		    	<c:otherwise>
		    		<c:set var="trclass" value="row-a" scope="page" />
		    	</c:otherwise>
	    	</c:choose>
	    </tr>
		</c:forEach>
		</table>
 </div>
<%@ include file="footer.jsp" %>