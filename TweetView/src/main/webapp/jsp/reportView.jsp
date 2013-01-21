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
		<h1>Report</h1>
		<table>
			<tr><td>Report Name:</td><td>Top URLs By Collection</td></tr>
			<tr><td>Report Date:</td><td><tv:date type="fullDateTime" value="${reportDate}"/></td></tr>
			<tr><td>Collection Name:</td><td>All</td></tr>
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
        <table border="0">
	        <tr>
	          <th style="vertical-align: middle;" class="first" >
					<tv:sortheader sort="${sort}" column="${column}" thisColumn="urlFull" displayName="URL"/>
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
		       <c:forEach items="${urlEntities}" var="urlEntity">
		      		<tr class="<c:out value='${trclass}' />">
		      		 			<td><a href="${urlEntity.urlFull}"><tv:ellipsis theString="${urlEntity.urlFull}" length="45" /></a></td>
		      		 			<td align="left" style="padding-left: 12px;	padding-right: 12px;">${urlEntity.collectionName}</td>
		      		 			<td align="center">${urlEntity.totalTweets}</td>
		      		 			<td align="center">${tweets[urlEntity.tweet.id].retweetCount}</td>
		      		</tr>
		      		<c:choose>
		      		<c:when test="${trclass eq 'row-a'}">
		      			<c:set var="trclass" value="row-b" />
		      		</c:when>
		      		<c:otherwise>
		      			<c:set var="trclass" value="row-a" />
		      		</c:otherwise>
		      		</c:choose>
		        </c:forEach>
        	<tr>
	          <th class="first" colspan="2">TOTAL</th>
	          <th class="first" align="center">${tweetTotal}</th>
	          <th class="first" align="center">${retweetTotal}</th>
	        </tr>
        
      	</table>
      	<br/>
 </div>

 </form>
<%@ include file="footer.jsp" %>