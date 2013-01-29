<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<script src="${pageContext.request.contextPath}/scripts/jquery.min.js" type="text/javascript"></script>
<script src="${pageContext.request.contextPath}/scripts/jquery.bgiframe.js" type="text/javascript"></script>
<script src="${pageContext.request.contextPath}/scripts/jquery.bt.min.js" type="text/javascript"></script>
<script src="${pageContext.request.contextPath}/scripts/date.js" type="text/javascript"></script>
<script src="${pageContext.request.contextPath}/scripts/jquery.datePicker.js" type="text/javascript"></script>
<script src="${pageContext.request.contextPath}/scripts/jquery_validate.js" type="text/javascript"></script>
<link rel="stylesheet" href="${pageContext.request.contextPath}/styles/styles.css" type="text/css" />
<link rel="stylesheet" href="${pageContext.request.contextPath}/styles/datePicker.css" type="text/css" />
<title>TwitterVane</title>
<script type="text/javascript">
$(document).ready(function() {
    Date.format = 'dd-mmm-yyyy';
    $('.date-pick').datePicker({startDate:'1996-01-01',clickInput:true});
});
</script>
</head>
<body>
<div id="wrap">
  <div id="header">
    <h1 id="logo-text">TwitterVane</h1>
    <h2 id="slogan">Crowd sourcing for Web Archiving</h2>
    <div id="header-links">
      <p> <a href="${pageContext.request.contextPath}/home.html">Home</a> | <a href="${pageContext.request.contextPath}/about.html">About</a></p>
    </div>
  </div>
  <div  id="menu">
    <ul>
	  	<li><a href="${pageContext.request.contextPath}/collections.html">Collections</a></li>
		<li><a href="${pageContext.request.contextPath}/report.html">Reports</a></li>
		<li class="last"><a href="${pageContext.request.contextPath}/tweets.html">Latest Tweets</a></li>
    </ul>
  </div>
  <div id="content-wrap">
  <%@ include file="sidebar.jsp" %>