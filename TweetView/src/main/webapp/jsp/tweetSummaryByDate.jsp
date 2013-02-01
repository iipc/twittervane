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

var chart1Series = [];
var chart1Ticks = [];
var chart1Max = 0;

var chart2Series = [];
var chart2Ticks = [];
var chart2Max = 0;

var chart3Series = [];
var chart3Ticks = [];
var chart3Max = 0;

$(document).ready(function(){
    $.jqplot.config.enablePlugins = true;

     
    plot1 = $.jqplot('chart1', [chart1Series], {
    	title: 'Tweets Received in Past Week',
        seriesDefaults:{
            renderer:$.jqplot.BarRenderer,
            pointLabels: { show: false }
        },
        axes: {
            xaxis: {
                renderer: $.jqplot.CategoryAxisRenderer,
                ticks: chart1Ticks
            },
            yaxis: {
            	max: chart1Max,
            	min: 0,
            	tickOptions: {formatString: '%d'}
            }
        },
        highlighter: { show: false }
    });
    
    plot2 = $.jqplot('chart2', [chart2Series], {
    	title: 'Tweets Received in Past Month',
        seriesDefaults:{
            renderer:$.jqplot.BarRenderer,
            pointLabels: { show: false }
        },
        axes: {
            xaxis: {
                renderer: $.jqplot.CategoryAxisRenderer,
                ticks: chart2Ticks,
                labelOptions: {
                    fontFamily: 'Georgia, Serif',
                    fontSize: '12pt'
                  }
            },
            yaxis: {
            	labelOptions: {
                    fontFamily: 'Georgia, Serif',
                    fontSize: '12pt'
                  },
         		max: chart2Max,
          		min: 0,
          		tickOptions: {formatString: '%d'}
          }
        },
        highlighter: { show: false }
    });
    
    plot3 = $.jqplot('chart3', [chart3Series], {
    	title: 'Tweets Received in Past Year',
        seriesDefaults:{
            renderer:$.jqplot.BarRenderer,
            pointLabels: { show: false }
        },
        axes: {
            xaxis: {
                renderer: $.jqplot.CategoryAxisRenderer,
                ticks: chart3Ticks
            },
            yaxis: {
            	max: chart3Max,
            	min: 0,
            	tickOptions: {formatString: '%d'}
            }
        },
        highlighter: { show: false }
    });
 
 });
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
        <table border="0" width="100%">
	        <tr>
	          <th style="vertical-align: middle;" class="first" >
					Past Week
	          </th>
			  <c:forEach items="${lastWeek}" var="day">
			  	<th style="vertical-align: middle;" class="first" >
			  		<c:out value="${day.key}" />
			  		<script>chart1Ticks.push('<c:out value="${day.key}" />'); chart1Series.push('<c:out value="${day.value}" />');</script>
			  		<script> if (<c:out value="${day.value}" /> > chart1Max) chart1Max = <c:out value="${day.value/10 + day.value}" />;</script>
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
      	
      	<div id="chart1" name="chart1" style="height:300px;width:570px; "></div> 
      	<br/>
      	
      	<table border="0" width="100%">
	        <tr>
	          <th style="vertical-align: middle;" class="first" >
					Past Month
	          </th>
	          <c:set var="count" value="0"/>
			  <c:forEach items="${lastMonth}" var="day">
			  	<c:if test="${count % 2 == 0}">
				  	<th style="vertical-align: middle;" class="first" >
				  		<c:out value="${day.key}" />
			  			<script>chart2Ticks.push('<c:out value="${day.key}" />'); chart2Series.push('<c:out value="${day.value}" />');</script>				  	</th>
			  			<script>if (<c:out value="${day.value}" /> > chart2Max) chart2Max = <c:out value="${day.value/10 + day.value}" />;</script>
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
      	<div id="chart2" name="chart2" style="height:300px;width:570px; "></div> 
      	<br/>
      	 
      	<table border="0" width="100%">
	        <tr>
	          <th style="vertical-align: middle;" class="first" >
					Past Year
	          </th>
			  <c:forEach items="${lastYear}" var="month">
			  	<th style="vertical-align: middle;" class="first" >
			  		<c:out value="${month.key}" />
			  		<script>chart3Ticks.push('<c:out value="${month.key}" />'); chart3Series.push('<c:out value="${month.value}" />');</script>
			  		<script> if (<c:out value="${month.value}" /> > chart3Max) chart3Max = <c:out value="${month.value/10 + month.value}" />;</script>
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
      	<div id="chart3" name="chart3" style="height:300px;width:570px; "></div> 
      	<br/>
 </div>

 </form>
 
<%@ include file="footer.jsp" %>

      	