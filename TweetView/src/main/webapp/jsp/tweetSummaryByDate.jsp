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
			<tr><td>Report Name:</td><td>Tweet Summary By Date</td></tr>
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
        <table border="0">
	        <tr>
	          <th style="vertical-align: middle;" class="first" >
					Last Week
	          </th>
			  <c:forEach items="${lastWeek}" var="day">
			  	<th style="vertical-align: middle;" class="first" >
			  		<c:out value="${day.key}" />
			  	</th>
			  </c:forEach>
	        </tr>
   	        <tr>
	          <td><c:out value="${webCollection.name}" /></td>
			  <c:forEach items="${lastWeek}" var="day">
			  	<td align="center" >
			  		<c:out value="${day.value}" />
			  	</td>
			  </c:forEach>
	        </tr>   
      	</table>
      	
      	<br/>
      	
      	<table border="0">
	        <tr>
	          <th style="vertical-align: middle;" class="first" >
					Last Month
	          </th>
	          <c:set var="count" value="0"/>
			  <c:forEach items="${lastMonth}" var="day">
			  	<c:if test="${count % 2 == 0}">
				  	<th style="vertical-align: middle;" class="first" >
				  		<c:out value="${day.key}" />
				  	</th>
			  	</c:if>
			  	<c:set var="count" value="${count+1}"/>
			  </c:forEach>
	        </tr>
   	        <tr>
   	          <c:set var="count" value="0"/>
   	          <c:set var="itemTotal" value="0" />
	          <td><c:out value="${webCollection.name}" /></td>
			  <c:forEach items="${lastMonth}" var="day">
			   <c:set var="itemTotal" value="${itemTotal + day.value}" />
			  	<c:if test="${count % 2 == 0}">
				  	<td align="center" >
				  		<c:out value="${itemTotal}" />
				  		<c:set var="itemTotal" value="0" />
				  	</td>
			  	</c:if>
			  	<c:set var="count" value="${count+1}"/>
			  </c:forEach>
	        </tr>   
      	</table>
      	<br/>
      	 
      	<table border="0">
	        <tr>
	          <th style="vertical-align: middle;" class="first" >
					Last Year
	          </th>
			  <c:forEach items="${lastYear}" var="month">
			  	<th style="vertical-align: middle;" class="first" >
			  		<c:out value="${month.key}" />
			  	</th>
			  </c:forEach>
	        </tr>
   	        <tr>
	          <td><c:out value="${webCollection.name}" /></td>
			  <c:forEach items="${lastYear}" var="month">
			  	<td align="center" >
			  		<c:out value="${month.value}" />
			  	</td>
			  </c:forEach>
	        </tr>   
      	</table>
 </div>

 </form>
<%@ include file="footer.jsp" %>