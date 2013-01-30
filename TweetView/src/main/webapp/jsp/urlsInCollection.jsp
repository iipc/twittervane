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
function setPageNumber(pageNo) {
	$('#pageNumber').val(pageNo);
	$('#report').submit();
}
</script>
<div id="main">
 <input type="hidden" name="sort" id="sort" value="${sort}" />
 <input type="hidden" name="column" id="column" value="${column}" />
 <input type="hidden" name="report" id="column" value="${report}" />
 <input type="hidden" name="collection" id="column" value="${collection}" />
 <input type="hidden" name="pageNumber" id="pageNumber" value="${page.pageNumber}" />
		<h1>Report</h1>
		<table>
			<tr><td>Report Name:</td><td>URLs In Collection</td></tr>
			<tr><td>Report Date:</td><td><tv:date type="fullDateTime" value="${reportDate}"/></td></tr>
			<tr><td>Collection Name:</td><td><c:out value="${webCollection.name}"/></td></tr>
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
		          	<tv:sortheader sort="${sort}" column="${column}" thisColumn="tweeter" displayName="Tweeter"/>
	          </th>
	          <th align="center" style="vertical-align: middle;" class="first" >
					<tv:sortheader sort="${sort}" column="${column}" thisColumn="tweet" displayName="Tweet"/>
	          </th>
	        </tr>
	        <c:set var="trclass" scope="page" value="row-a" />
	        <c:set var="idx" scope="page" value="0" />
		       <c:forEach items="${urlEntities}" var="urlEntity">
		      		<tr class="<c:out value='${trclass}' />">
		      		 			<td align="left"><a href="${urlEntity.urlFull}"><tv:ellipsis theString="${urlEntity.urlFull}" length="25" /></a></td>
		      		 			<td align="left">${urlEntity.tweet.name}</td>
		      		 			<td align="left">${urlEntity.tweet.text}</td>
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
		        <tr><td colspan="3">
		        <%@ include file="pagination.jsp" %>
		        </td></tr>
      	</table>
      		
     </table>
      	<br/>
 </div>

 </form>
<%@ include file="footer.jsp" %>