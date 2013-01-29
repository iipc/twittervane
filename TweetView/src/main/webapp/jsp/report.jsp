<%@page import="uk.bl.wap.crowdsourcing.*"%>
<%@taglib prefix = "c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@taglib prefix="tv" uri="http://ukwa/tv" %>
<jsp:useBean id="urlEntityDao" type="uk.bl.wap.crowdsourcing.dao.UrlEntityDao" scope="request" />
<jsp:useBean id="webCollection" type="uk.bl.wap.crowdsourcing.WebCollection" scope="request" />
<%@page import="java.util.Iterator" %>
<%@ include file="header.jsp" %>
<div id="main">
		<%
	    long collectionId =  (long) Long.parseLong(request.getParameter("collectionId"));
	    String trclass = "row-a";
	    String reportType = request.getParameter("reportType");
	    String filterDomain = request.getParameter("filterDomain");
	    String filterUrl = request.getParameter("filterUrl");
	    int start = 0;
	    int rows = 100;
	    if (request.getParameter("start") != null) {
	    	start = (int) Integer.parseInt(request.getParameter("start"));
	    }
	    if (request.getParameter("rows") != null) {
	    	rows = (int) Integer.parseInt(request.getParameter("rows"));
	    }
	   	%>
	   	<script language="javascript">
			function submitForm(str,type) {
				if (type == "url") {
					document.form1.u.value = str;
				} else {
					document.form1.d.value = str;
				}
				document.form1.submit();
			}
	   	</script>
		<form action="report.html" id="form1" name="form1" method="post">
			<input type="hidden" id="collectionId" name="collectionId" value="<%= collectionId%>">
			<input type="hidden" id="u" name="u" value="">
			<input type="hidden" id="d" name="d" value="">
		</form>
		
		<h1>Reports</h1>
		
	   	<table>
	   		<tr>
	   			<td><Label>Total Tweets:</Label></td><td><%= urlEntityDao.getTotalTweets(collectionId, filterUrl, filterDomain) %></td>
	   			<td rowspan="3" valign="top"><Label>KeyWords:</Label></td>
	   			<td rowspan="3" valign="top">
	   				<%
					Iterator<SearchTerm> iterator = webCollection.getSearchTerms().iterator();
		         		while (iterator.hasNext()) {
		         			%>
		         			<%= iterator.next().getTerm() %><br/>
			        <%
			         	}
					%>
				</td>
	   		</tr>
	   		<tr>
	   			<td><Label>Total URLs:</Label></td><td><%= urlEntityDao.getTotalURL(collectionId, filterUrl, filterDomain) %></td>
	   		</tr>
	   		<tr>
	   			<td><Label>Total Domains:</Label></td><td><%=urlEntityDao.getTotalDomain(collectionId, filterUrl, filterDomain) %></td>
	   		</tr>
	   		<!--
	   		<tr>
	   			<td>Keywords:</td>
	   			<td>
	   			
	   			</td>
	   		</tr>
	   		-->
	   	</table>
	   	<p>${message.paging}</p>
		<table>
	        <tr>
	          <th>No.</th>
	          <th></th>
	          <th class="first">
	          <%
	          if (reportType.contentEquals("topUrl")) { 
	          %>
	          	Url
	          <%
	          } else {
	          %>
	       		Domain
	       	  <%
	          }
	          %>
	          
	          </th>
	        </tr>
		<%
	    if (reportType.contentEquals("topUrl")) { 
	        for (Object[] o : urlEntityDao.getTopUrl(collectionId,filterUrl,filterDomain,start,rows)) { 
	    	 %>
	        	<tr class="<%= trclass %>">
	        		<td><%= o[1] %></td>
	        		<%String value = "";
	        		if (o[0] != null)
	        		value = (String)o[0];%>
	        		<td>
	        			<a onclick="submitForm('<%= o[0] %>','url')"><img src="${pageContext.request.contextPath}/images/list.png" border="0" title="View Tweets" /></a>
	        		</td>
	        		<td><span style="white-space: pre;white-space: pre-wrap;white-space: pre-line;white-space: -pre-wrap;white-space: -o-pre-wrap;white-space: -moz-pre-wrap;white-space: -hp-pre-wrap;
							word-wrap: break-word;">
	        			<a href="<%= o[0] %>" target="_new"><tv:ellipsis theString='<%=value%>' length='85' /></a>
	        			</span>
	        		</td>
	        	</tr>
	        <%
	        }
	    } else if (reportType.contentEquals("domain")) {
	    	 for (Object[] o : urlEntityDao.getTopDomain(collectionId,filterUrl,filterDomain,start,rows)) { 
	    	 %>
	        	<tr class="<%= trclass %>">
	        		<td><%= o[1] %></td>
	        		<td>
	        			<a onclick="submitForm('<%= o[0] %>','domain')"><img src="${pageContext.request.contextPath}/images/list.png" border="0" title="View Tweets" /></a>
	        		</td>
	        		<td><a href="http://<%= o[0] %>" target="_new"><%= o[0] %></a></td>
	        	</tr>
	        <%
	        }

		} else if (reportType.contentEquals("popUrl")) {
	    	 for (Object[] o : urlEntityDao.getTopPopularity(collectionId,filterUrl,filterDomain,start,rows)) { 
	    	 %>
	        	<tr class="<%= trclass %>">
	        		<td><%= o[1] %></td>
	        		<td>
	        			<a onclick="submitForm('<%= o[0] %>','url')"><img src="${pageContext.request.contextPath}/images/list.png" border="0" title="View Tweets" /></a>
	        		</td>
	        		<td><a href="<%= o[0] %>" target="_new"><%= o[0] %></a></td>
	        	</tr>
	        <%
	        }

		} else if (reportType.contentEquals("failed")) {
	    	 for (UrlEntity entity : urlEntityDao.getAllUrlEntitiesFailed()) { 
	    	 %>
	        	<tr class="<%= trclass %>">
	        		<td><%= entity.getId() %></td>
	        		<td></td>
	        		<td><a href="<%= entity.getUrlOriginal() %>" target="_new"><%= entity.getUrlOriginal() %></a></td>
	        	</tr>
	        <%
	        }

		} else if (reportType.contentEquals("browseTopUrl")) {
			%>
			<tr class="<%= trclass %>">
    		<td>To be implemented</td>
    		</tr>
    		<%
		}
	  	if ( trclass.contentEquals("row-a")) {
	  		trclass = "row-b";
	  	} else {
	  		trclass = "row-a";
	  	}
	  	%>
      	</table>
 </div>
<%@ include file="footer.jsp" %>