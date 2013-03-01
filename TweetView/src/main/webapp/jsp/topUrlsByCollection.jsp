<%@page import="java.util.Date"%>
<%@page import="uk.bl.wap.crowdsourcing.*"%>
<%@taglib prefix = "c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@taglib prefix="tv" uri="http://ukwa/tv" %>
<%@ include file="header.jsp" %>
<form name="report" id="report" action="reportView.html" method="post">
<script language="javascript">
function submitForm(column, sortOrder) {
	$('#column').val(column);
	$('#sort').val(sortOrder);
	$('#report').submit();
}
</script>
<div id="main">
 <input type="hidden" name="sort" id="sort" value="${sort}" />
 <input type="hidden" name="column" id="column" value="${column}" />
 <input type="hidden" name="report" id="column" value="${report}" />
		<h1>Report</h1>
		<table width="100%">
			<tr><td>Report Name:</td><td>Top URL By Collection</td></tr>
			<tr><td>Report Date:</td><td><tv:date type="fullDateTime" value="${reportDate}"/></td></tr>
		</table>
		<c:set var="nextSort" scope="page" value="" />
		<c:choose>
			<c:when test="${sort eq 'desc'}" >
				<c:set var="nextSort" value="asc" />
			</c:when>
			<c:otherwise>
				<c:set var="nextSort" value="desc" />
			</c:otherwise>
		</c:choose>
        <table width="100%" border="0">
	        <tr>
	          <th style="vertical-align: middle;" class="first" >
					<tv:sortheader sort="${sort}" column="${column}" thisColumn="topUrl" displayName="URL"/>
	          </th>
	          <th style="vertical-align: middle;" class="first" >
					<tv:sortheader sort="${sort}" column="${column}" thisColumn="collectionName" displayName="Collection"/>
	          </th>
	          <th style="vertical-align: middle;" class="first" >
		          	<tv:sortheader sort="${sort}" column="${column}" thisColumn="totalTweets" displayName="Tweets"/>
	          </th>
	          <th style="vertical-align: middle;" class="first" >
					<tv:sortheader sort="${sort}" column="${column}" thisColumn="totalRetweets" displayName="Reweets"/>
	          </th>

	        </tr>
	        <c:set var="trclass" scope="page" value="row-a" />
	        <c:set var="idx" scope="page" value="0" />
		       <c:forEach items="${webCollections}" var="webCollection">
		      		<tr class="<c:out value='${trclass}' />">
		      		 			<td><a href="${webCollection.topUrl}" target="_new"><tv:ellipsis theString="${webCollection.topUrl}" length="35" /></a></td>
		      		 			<td align="left" style="padding-left: 12px;	padding-right: 12px;">${webCollection.name}</td>
		      		 			<td align="center">${webCollection.topUrlTweets}</td>
		      		 			<td align="center">${webCollection.topUrlRetweets}</td>
		      		</tr>
		      		<c:choose>
		      		<c:when test="${trclass eq 'row-a'}">
		      			<c:set var="trclass" value="row-b" />
		      		</c:when>
		      		<c:otherwise>
		      			<c:set var="trclass" value="row-a" />
		      		</c:otherwise>
		      		</c:choose>
		      		<c:set var="idx" scope="page" value="${idx + 1}" />
		        </c:forEach>
        	<tr>
	          <th class="first" colspan="2">TOTAL</th>
	          <th class="first" align="center">${totalTopUrlTweets}</th>
	          <th class="first" align="center">${totalTopUrlRetweets}</th>
	        </tr>
        
      	</table>
      	<br/>
 </div>

 </form>
<%@ include file="footer.jsp" %>