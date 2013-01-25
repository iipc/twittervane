<%@page import="uk.bl.wap.crowdsourcing.*"%>
<%@taglib prefix = "c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<jsp:useBean id="appConfig" type="uk.bl.wap.crowdsourcing.AppConfig" scope="request" />
<jsp:useBean id="searchTermDao" type="uk.bl.wap.crowdsourcing.dao.SearchTermDao" scope="request" />
<%@page import="java.util.Iterator" %>

<%@ include file="header.jsp" %>

<div id="main">
 <h1>Twitter Stream</h1>

		 <p><b>Twitter Stream Status:</b> ${message.message}</p>
		 <p><b>Twitter Stream Manager:</b>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
		 <a href="twitterstream.html?action=start">Start</a>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
		 <a href="twitterstream.html?action=stop">Stop</a></p>
		 <p><b>Tweet Analysis run for every <c:out value="${analysisTriggerValue}" /> tweets recieved</b></p>
		  <br/>
		 <h2>Twitter Stream Required</h2>
		 <p>If any of the below are in red the twitter stream will not start.</p>
		 <p><b>Terms:</b>
		   <%
		boolean termsFound = false;
		String searchTerms = "";
        for (SearchTerm searchterm : searchTermDao.getAllSearchTerms()) { 
        	searchTerms += searchterm.getTerm() + ", ";
	  	termsFound = true;
        }
        if (!termsFound) {
        %>
			<span style="color:red">Required</span>
		<%
        } else {
        	if (searchTerms.length() > 0) {
        		searchTerms = searchTerms.substring(0, searchTerms.lastIndexOf(","));
    		}
        %>
 			<%= searchTerms %>
 		<%
        }
	  	%>
		  </p>
		 <p>
		 <b>Twitter Api</b>
			<ul>
				<li><b>Consumer Key:</b> 
				<%
				if (appConfig.getConsumerKey() != null && !appConfig.getConsumerKey().isEmpty()) {
				%>
					<%= appConfig.getConsumerKey() %>
				<%
				} else {
				%>
					<span style="color:red">Required</span>
				<%
				}
				%> 
				</li>
				<li><b>Consumer Secret:</b>
				<%
				if (appConfig.getConsumerSecret() != null && !appConfig.getConsumerSecret().isEmpty()) {
				%>
					<%= appConfig.getConsumerSecret() %>
				<%
				} else {
				%>
					<span style="color:red">Required</span>
				<%
				}
				%> 
				</li>
				<li><b>Access Token:</b>
				<%
				if (appConfig.getAccessToken() != null && !appConfig.getAccessToken().isEmpty()) {
				%>
					<%= appConfig.getAccessToken() %>
				<%
				} else {
				%>
					<span style="color:red">Required</span>
				<%
				}
				%>
				</li>
				<li><b>Access Key:</b>
				<%
				if (appConfig.getAccessTokenSecret() != null && !appConfig.getAccessTokenSecret().isEmpty()) {
				%>
					<%= appConfig.getAccessTokenSecret() %>
				<%
				} else {
				%>
					<span style="color:red">Required</span>
				<%
				}
				%>
				</li>
			</ul>
		</p>
 </div>
<%@ include file="footer.jsp" %>