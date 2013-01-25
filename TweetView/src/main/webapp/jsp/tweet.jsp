<%@page import="uk.bl.wap.crowdsourcing.*"%>
<%@ include file="header.jsp" %>
<jsp:useBean id="tweetDao" type="uk.bl.wap.crowdsourcing.dao.TweetDao" scope="request" />
<div id="main">
		<h1>Latest Tweets</h1>
		<!-- 
		 <form method="POST" action="tweetedit.html">
            Tweet: <input type="text" name="tweet" />
            <input type="submit" value="Add" />
        </form>
		 -->
        <table>
	        <tr>
	          <th class="first">Tweeter</th>
	          <th>Date</th>
	          <th>Retweet</th>
	          <th>URL Entities</th>
	          <th>Tweet</th>
	        </tr>
	    <%
	        String trclass = "row-a";
	    %>
        <% for (Tweet tweet : tweetDao.getAllTweets()) { %>
      		<tr class="<%= trclass %>">
      			<td><%= tweet.getName()%></td>
      			<td><%= tweet.getCreationDate()%></td>
      			<td><%= tweet.getRetweetCount()%>
      			<td>
      			
	      		<%
	      	//	if ( tweet.getUrlEntities().size() > 0) {
	      			for (int i = 0; i < tweet.getUrlEntities().size(); i++) {
	      			%>
	      				<a id="a_url_<%= i %>_<%= tweet.getId() %>">View URL(s)</a>
	      				<div id="div_url_<%= i %>_<%= tweet.getId() %>"  style="display:none;"><%= tweet.getUrlEntities().get(i).getUrlOriginal() %></div>
	      				<script type="text/javascript">
							$(document).ready(function(){
								$('#a_url_<%= i %>_<%= tweet.getId() %>').bt({
									  positions: 'right,top',
									  contentSelector: "$('#div_url_<%= i %>_<%= tweet.getId() %>')",
									  width: 300,
									  centerPointX: .9,
									  spikeLength: 25,
									  spikeGirth: 15,
									  padding: 10,
									  cornerRadius: 20,
									  fill: '#FFF',
									  strokeStyle: '#ABABAB',
									  strokeWidth: 1
									});
							});
						</script>
		      		<%
	      			}
	      //		} else {
	      	//	}
	      		%>
      			</td>
      			<td>
      				<a id="a_tweet_<%= tweet.getId() %>">View Tweet</a>
      				<div id="div_tweet_<%= tweet.getId() %>" style="display:none;"><%= tweet.getText() %></div>
      			</td>
      		</tr>
      		
        	<%
        	
	  			if ( trclass.contentEquals("row-a")) {
	  				trclass = "row-b";
	  			} else {
	  				trclass = "row-a";
	  			}
        	%>
        		  <script type="text/javascript">
					$(document).ready(function(){
						$('#a_tweet_<%= tweet.getId() %>').bt({
							 	positions: 'right,top',
							  contentSelector: "$('#div_tweet_<%= tweet.getId() %>')",
							  width: 300,
							  centerPointX: .9,
							  spikeLength: 25,
							  spikeGirth: 15,
							  padding: 10,
							  cornerRadius: 20,
							  fill: '#FFF',
							  strokeStyle: '#ABABAB',
							  strokeWidth: 1
							});
						
					});
				</script>
        	<%
	  		} 
	  		%>
      	</table>
      	<br/>
      	Total Tweets: <%= tweetDao.getTotalTweets() %>
 </div>
<%@ include file="footer.jsp" %>