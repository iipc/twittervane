<%@page import="uk.bl.wap.crowdsourcing.*"%>
<jsp:useBean id="urlEntityDao" type="uk.bl.wap.crowdsourcing.dao.UrlEntityDao" scope="request" />
<jsp:useBean id="tweetDao" type="uk.bl.wap.crowdsourcing.dao.TweetDao" scope="request" />
<%@page import="java.util.Iterator" %>
<%@ include file="header.jsp" %>
<div id="main">
		<h1>Reports</h1>
		<%
	    long collectionId =  (long) Long.parseLong(request.getParameter("collectionId"));
	    String trclass = "row-a";
	    String u = request.getParameter("u");
	    String d = request.getParameter("d");
	   	%>
	   	
	   	<%
	   	if ( u != null && !u.isEmpty()) {
	   	%>
	   		<Label>URL: </Label> <%= u %><br/>
	   	<% 
	   	} else if ( d != null && !d.isEmpty()) {
	   	%>
	   		<Label>Domain: </Label> <%= d %><br/>
	   	<%
	   	}
	   	%>
	   	
	   	<table>
	        <tr>
	          <th class="first">Retweets</th>
	          <th>Tweet</th>
	        </tr>
		<%
		Tweet tweet = null;
		
		if ( u != null && !u.isEmpty()) {
			
			for (UrlEntity urlEntity : urlEntityDao.getEntitiesByUrl(collectionId, u)) {
				
				tweet = tweetDao.getTweet(urlEntity.getTweetId());
				%>
				<tr>
					<td><%= tweet.getRetweetCount() %></td>
					<td><%= tweet.getText() %></td>
				</tr>
				<%
			}
		  	if ( trclass.contentEquals("row-a")) {
		  		trclass = "row-b";
		  	} else {
		  		trclass = "row-a";
		  	}
		} else if (d != null && !d.isEmpty()) {
			for (UrlEntity urlEntity : urlEntityDao.getEntitiesByDomain(collectionId, d)) {
				
				tweet = tweetDao.getTweet(urlEntity.getTweetId());
				%>
				<tr>
					<td><%= tweet.getRetweetCount() %></td>
					<td><%= tweet.getText() %></td>
				</tr>
				<%
			}
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