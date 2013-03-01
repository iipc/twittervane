<%@page import="uk.bl.wap.crowdsourcing.*"%>
<%@taglib prefix = "c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@taglib prefix="tv" uri="http://ukwa/tv" %>
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
	   	<table width="100%" border="0" style="border: 2px solid #93BC0C; background: #EFEFEF;">
	   		<tr>
	   			<td width="5%"><Label>Total&nbsp;Tweets:</Label></td><td width="10%"><c:out value="${totalTweets}" /></td>
	   			<td width="5%"><Label>Report&nbsp;Type:</Label></td>
	   			<td align="left" width="30%">
	   			<c:choose>
	   			<c:when test="${reportType eq 'domain'}">Top Domains</c:when>
	   			<c:when test="${reportType eq 'topUrl'}">Top URLs</c:when>
	   			<c:when test="${reportType eq 'popUrl'}">Top URL by Retweet</c:when>
	   			</c:choose>
	   			</td>
	   		</tr>
	   		<tr>
	   			<td width="5%"><Label>Total&nbsp;URLs:</Label></td><td width="10%"><c:out value="${totalURLs}"/></td>
	   			<td width="5%"><Label>Collection&nbsp;Name:</Label></td>
	   			<td align="left" width="30%">
	   			<c:out value="${webCollection.name}" />			
	   			</td>
	   		</tr>
	   		<tr>
	   			<td width="5%"><Label>Total&nbsp;Domains:&nbsp;</Label></td><td width="10%"><c:out value="${totalDomains}"/></td>
	   			<td width="5%"><Label>Key&nbsp;Words:</Label></td>
	   			<td align="left" width="30%">
	   			<c:set var="count" scope="page" value="0" />
	   			<c:forEach items="${webCollection.searchTerms}" var="searchTerm">
	   				<c:set var="count" scope="page" value="${count + 1}" />
	   				<c:if test="${count gt 1 && count le fn:length(webCollection.searchTerms)}">, </c:if>
	   				<c:out value="${searchTerm.term}" />
	   			</c:forEach>
				</td>
	   			
	   		</tr>
	   	</table>
	 
		<table width="100%">
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
	        	<c:choose>
		    	<c:when test="${trclass eq 'row-a' }" >
		    		<c:set var="trclass" value="row-b" scope="page" />
		    	</c:when>
		    	<c:otherwise>
		    		<c:set var="trclass" value="row-a" scope="page" />
		    	</c:otherwise>
		    </c:choose>
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
        	<c:choose>
		    	<c:when test="${trclass eq 'row-a' }" >
		    		<c:set var="trclass" value="row-b" scope="page" />
		    	</c:when>
		    	<c:otherwise>
		    		<c:set var="trclass" value="row-a" scope="page" />
		    	</c:otherwise>
		    </c:choose>
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
	        <c:choose>
		    	<c:when test="${trclass eq 'row-a' }" >
		    		<c:set var="trclass" value="row-b" scope="page" />
		    	</c:when>
		    	<c:otherwise>
		    		<c:set var="trclass" value="row-a" scope="page" />
		    	</c:otherwise>
		    </c:choose>
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

				<tr><td colspan="3">
	        		<%@ include file="pagination.jsp" %>
	        </td></tr>
      	</table>
 </div>
<%@ include file="footer.jsp" %>