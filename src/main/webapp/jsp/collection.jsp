<%@page import="uk.bl.wap.crowdsourcing.*"%>
<%@page import="java.util.Iterator" %>
<%@page import="java.text.SimpleDateFormat" %>

<jsp:useBean id="webCollection" type="uk.bl.wap.crowdsourcing.WebCollection" scope="request" />
<jsp:useBean id="searchTermDao" type="uk.bl.wap.crowdsourcing.dao.SearchTermDao" scope="request" />

<%@ include file="header.jsp" %>
<%
SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yyyy");
%>
<div id="main">
<h1>Edit Collection</h1>
        <form method="POST" action="collection.html">
        <input type="hidden" value="<%= request.getParameter("id") %>" name="id">
  		<label>Name</label><input type="text" name="name" maxlength="100" value="<%= webCollection.getName() %>"/>
  		<label>Description</label><input type="text" name="description" maxlength="500" value="<%= webCollection.getDescription() %>"/>
  		<label>Start Date</label><input type="text" name="startDate" maxlength="10" class="date-pick" value="<%= sdf.format(webCollection.getStartDate()) %>"/>
        <label>End Date</label><input type="text" name="endDate" maxlength="10" class="date-pick" value="<%= sdf.format(webCollection.getEndDate()) %>"/>
  		<label>Add Term</label><input type="text" name="term" maxlength="50" />

		<input type="submit" value="Save" />&nbsp;&nbsp;<font color="red">${message.message}</font>
        </form>
        
        <h1>Current Search Terms</h1>
        <%
			Iterator<SearchTerm> iterator = webCollection.getSearchterms().iterator();
         		while (iterator.hasNext()) {
         			SearchTerm st = searchTermDao.getSearchTermByid((Long) iterator.next().getId());
		%>
				<a href="${pageContext.request.contextPath}/searchterm.html?id=<%= st.getId() %>"><%=  st.getTerm() %></a><br/>
		<%
         	}
		%>
 </div>
<%@ include file="footer.jsp" %>