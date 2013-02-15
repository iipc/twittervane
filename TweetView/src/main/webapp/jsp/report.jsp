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
	    String reportType = request.getParameter("reportType");
	    String filterDomain = request.getParameter("filterDomain");
	    String filterUrl = request.getParameter("filterUrl");
	    int start = 0;
	    int rows = 100;
	    Integer pageSize = 5;
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
			function setPageNumber(pageNo) {
				$('#pageNumber').val(pageNo);
				$('#form1').submit();
			}
	   	</script>
		<form action="report.html" id="form1" name="form1" method="post">
			<input type="hidden" id="collectionId" name="collectionId" value="<%= collectionId%>">
			<input type="hidden" id="u" name="u" value="">
			<input type="hidden" id="d" name="d" value="">
			<input type="hidden" name="pageNumber" id="pageNumber" value="${page.pageNumber}" />
			<input type="hidden" name="reportType" id="reportType" value="${reportType}" />
		</form>
		
		<h1>Reports</h1>
		
	   	<table>
	   		<tr>
	   			<td><Label>Total Tweets:</Label></td><td><%= urlEntityDao.getTotalTweets(collectionId, filterUrl, filterDomain) %></td>
	   			<td rowspan="3" valign="top"><Label>KeyWords:</Label></td>
	   			<td rowspan="3" valign="top">
	   			<c:forEach items="${webCollection.searchTerms}" var="searchTerm">
	   				<c:out value="${searchTerm.term}" /><br/>
	   			</c:forEach>
				</td>
	   		</tr>
	   		<tr>
	   			<td><Label>Total URLs:</Label></td><td><%= urlEntityDao.getTotalURL(collectionId, filterUrl, filterDomain) %></td>
	   		</tr>
	   		<tr>
	   			<td><Label>Total Domains:</Label></td><td><%=urlEntityDao.getTotalDomain(collectionId, filterUrl, filterDomain) %></td>
	   		</tr>
	   	</table>
	 
		<table>
	        <tr>
	          <th>No.</th>
	          <th></th>
	          <th class="first">
	          
	        <c:choose>
				<c:when test="${reportType eq 'topUrl' }" >
		          	Url
				</c:when>
				<c:otherwise>
		       		Domain
				</c:otherwise>
			</c:choose>
	          
	          </th>
	        </tr>
	        <c:set var="trclass" value="row-a" scope="page" />
		<c:if test="${reportType eq 'topUrl' }" >
			<c:forEach items="${topUrls}" var="entity">
	        	<tr class="<c:out value='${trclass}' />">
	        		<td><c:out value='${entity[1]}' /></td>
	        		<td>
	        			<a onclick="submitForm('${entity[0]}','url')"><img src="./images/list.png" border="0" title="View Tweets" /></a>
	        		</td>
	        		<td><span style="white-space: pre;white-space: pre-wrap;white-space: pre-line;white-space: -pre-wrap;white-space: -o-pre-wrap;white-space: -moz-pre-wrap;white-space: -hp-pre-wrap;
							word-wrap: break-word;">
	        			<a href="${entity[0]}" target="_new"><tv:ellipsis theString="${entity[0]}" length="85" /></a>
	        			</span>
	        		</td>
	        	</tr>
	        </c:forEach>
		</c:if>
    	<c:if test="${reportType eq 'domain' }" >
    		<c:forEach items="${topDomains}" var="entity">
        	<tr class="<c:out value='${trclass}' />">
        		<td><c:out value='${entity[1]}' /></td>
        		<td>
        			<a onclick="submitForm('${entity[0]}','domain')"><img src="./images/list.png" border="0" title="View Tweets" /></a>
        		</td>
        		<td><a href="http://${entity[0]}" target="_new"><tv:ellipsis theString="${entity[0]}" length="85" /></a></td>
        	</tr>
        	</c:forEach>
        </c:if>

    	<c:if test="${reportType eq 'popUrl' }" >
    		<c:forEach items="${popularUrls}" var="entity">
        	<tr class="<c:out value='${trclass}' />">
        		<td><c:out value='${entity[1]}' /></td>
        		<td>
        			<a onclick="submitForm('${entity[0]}','url')"><img src="./images/list.png" border="0" title="View Tweets" /></a>
        		</td>
        		<td><a href="${entity[0]}" target="_new"><tv:ellipsis theString="${entity[0]}" length="85" /></a></td>
        	</tr>
        	</c:forEach>
        </c:if>
	        
	   <c:if test="${reportType eq 'failed' }" >
			<c:forEach items="${failedUrlEntities}" var="entity">
	        	<tr class="<c:out value='${trclass}' />">
	        		<td><c:out value='${entity.id}' /></td>
	        		<td></td>
	        		<td><a href="${entity.urlOriginal}" target="_new"><tv:ellipsis theString="${entity.urlOriginal}" length="85" /></a></td>
	        	</tr>
	        </c:forEach>
	    </c:if>
	    <c:choose>
	    	<c:when test="${trclass eq 'row-a' }" >
	    		<c:set var="trclass" value="row-b" scope="page" />
	    	</c:when>
	    	<c:otherwise>
	    		<c:set var="trclass" value="row-a" scope="page" />
	    	</c:otherwise>
	    </c:choose>
				<tr><td colspan="3">
	        		<%@ include file="pagination.jsp" %>
	        </td></tr>
      	</table>
 </div>
<%@ include file="footer.jsp" %>