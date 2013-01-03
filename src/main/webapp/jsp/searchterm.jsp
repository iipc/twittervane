<%@page import="uk.bl.wap.crowdsourcing.*"%>
<jsp:useBean id="searchTerm" type="uk.bl.wap.crowdsourcing.SearchTerm" scope="request" />
<%@page import="java.text.SimpleDateFormat" %>
<%@ include file="header.jsp" %>
<div id="main">
		<h1>Search Term Edit</h1>
		<form method="POST" action="searchterm.html">
			<input type="hidden" name="id" value="<%= searchTerm.getId() %>" />
            <label>Term</label><input type="text" name="term" maxlength="50" value="<%= searchTerm.getTerm() %>" />
            <input type="submit" value="Save" />&nbsp;&nbsp;<font color="red">${message.message}</font>
        </form>
</div>
<%@ include file="footer.jsp" %>