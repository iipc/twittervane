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
		<table>
			<tr><td>Report Name:</td><td>Tweet Summary By Collection</td></tr>
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
        <table border="0" width="100%">
	        <tr>
	          <th style="vertical-align: middle;" class="first" >
					<tv:sortheader sort="${sort}" column="${column}" thisColumn="collectionName" displayName="Collection"/>
	          </th>
	          <th title="No of processed and unprocessed tweets" style="vertical-align: middle;" class="first" >
					<tv:sortheader sort="${sort}" column="${column}" thisColumn="totalTweets" displayName="Tweets"/>
	          </th>
	          <th title="No of non-expanded URLs" style="vertical-align: middle;" class="first" >
		          	<tv:sortheader sort="${sort}" column="${column}" thisColumn="totalUrlsOriginal" displayName="URLs"/>
	          </th>
	          <th title="Expanded URL count" style="vertical-align: middle;" class="first" >
					<tv:sortheader sort="${sort}" column="${column}" thisColumn="totalUrlsFull" displayName="Expanded"/>
	          </th>
	          <th title="No of analysis errors (eg: failure to resolve tweet to a web collection)" style="vertical-align: middle;" class="first" >
					<tv:sortheader sort="${sort}" column="${column}" thisColumn="totalUrlErrors" displayName="Errors"/>
	          </th>

	        </tr>
	        <c:set var="trclass" scope="page" value="row-a" />
	        <c:set var="unknownCollection" scope="page" value="" />
		       <c:forEach items="${webCollections}" var="webCollection">
		      		<tr class="<c:out value='${trclass}' />">
		      		 			<td><tv:ellipsis theString="${webCollection.name}" length="45" /></td>
		      		 			<td align="center"><a href="./reportView.html?report=tweetSummaryByDate&collection=${webCollection.id}">${webCollection.totalTweets}</a></td>
		      		 			<td align="center"><a href="./reportView.html?report=urlsInCollection&collection=${webCollection.id}">${webCollection.totalUrlsOriginal - webCollection.totalUrlsExpanded}</a></td>
		      		 			<td align="center"><a href="./reportView.html?report=expandedUrlsInCollection&collection=${webCollection.id}">${webCollection.totalUrlsExpanded}</a></td>
	      		 				<td align="center">${webCollection.totalUrlErrors}</td>
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
	          <th class="first">TOTAL</th>
	          <th class="first" align="center">${totalTweets}</th>
	          <th class="first" align="center">${totalUrlsOriginal}</th>
	          <th class="first" align="center">${totalUrlsExpanded}</th>
	          <th class="first" align="center">${totalUrlErrors}</th>
	        </tr>
        
      	</table>
      	<br/>
 </div>

 </form>
<%@ include file="footer.jsp" %>