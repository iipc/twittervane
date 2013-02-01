<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<link class="include" rel="stylesheet" type="text/css" href="./styles/jquery.jqplot.min.css" />
<link type="text/css" rel="stylesheet" href="./scripts/syntaxhighlighter/styles/shCoreDefault.min.css" />
<link type="text/css" rel="stylesheet" href="./scripts/syntaxhighlighter/styles/shThemejqPlot.min.css" />
    
<script src="./scripts/jquery.min.js" type="text/javascript"></script>
<script src="./scripts/jquery.bgiframe.js" type="text/javascript"></script>
<script src="./scripts/jquery.bt.min.js" type="text/javascript"></script>
<script src="./scripts/date.js" type="text/javascript"></script>
<script src="./scripts/jquery.datePicker.js" type="text/javascript"></script>
<script src="./scripts/jquery_validate.js" type="text/javascript"></script>

<script type="text/javascript" src="./scripts/jquery.jqplot.min.js"></script>
<script type="text/javascript" src="./scripts/syntaxhighlighter/scripts/shCore.min.js"></script>
<script type="text/javascript" src="./scripts/syntaxhighlighter/scripts/shBrushJScript.min.js"></script>
<script type="text/javascript" src="./scripts/syntaxhighlighter/scripts/shBrushXml.min.js"></script>
<script type="text/javascript" src="./scripts/plugins/jqplot.barRenderer.min.js"></script>
<script type="text/javascript" src="./scripts/plugins/jqplot.categoryAxisRenderer.min.js"></script>
<script type="text/javascript" src="./scripts/plugins/jqplot.pointLabels.min.js"></script>
<script type="text/javascript" src="./scripts/plugins/jqplot.canvasTextRenderer.min.js"></script>
<script type="text/javascript" src="./scripts/plugins/jqplot.canvasAxisLabelRenderer.min.js"></script>

<link rel="stylesheet" type="text/css" href="./styles/jquery.jqplot.css" />


<link rel="stylesheet" href="./styles/styles.css" type="text/css" />
<link rel="stylesheet" href="./styles/datePicker.css" type="text/css" />

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
      <p> <a href="./home.html">Home</a> | <a href="./about.html">About</a></p>
    </div>
  </div>
  <div  id="menu">
    <ul>
	  	<li><a href="./collections.html">Collections</a></li>
		<li><a href="./report.html">Reports</a></li>
		<li class="last"><a href="./tweets.html">Streamed Tweets</a></li>
    </ul>
  </div>
  <div id="content-wrap">
  <%@ include file="sidebar.jsp" %>