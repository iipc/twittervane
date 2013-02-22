<%@page import="uk.bl.wap.crowdsourcing.*"%>
<%@taglib prefix = "c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ include file="header.jsp" %>
<div id="main">
<h1>Jobs</h1>

		<form method="POST" action="runjob.html">
            <label>Number of tweets to process</label><input type="text" name="jobNumber" maxlength="10" />
            <input type="submit" value="Submit" />
        </form>
        
<h1>Job Results</h1>	
        <p>
        Total Processed: ${message.message}
        </p>
		<p>Tweet Analyser Service Status:
		<span style="color:
		<c:if test="${tweetAnalyserService.status eq 'RUNNING'}" >green</c:if>
		<c:if test="${tweetAnalyserService.status eq 'STOPPED'}" >red</c:if>
		<c:if test="${tweetAnalyserService.status eq 'PAUSED'}" >orange</c:if>
		">
		<c:out value="${tweetAnalyserService.status}" />
		</span>
		</p>
		<p>URL Expansion set to run for the top <c:out value="${tweetAnalyserService.topUrls}" /> tweets </p>
 
 <h1>Summary</h1>
 
 <p>Total Tweets: <c:out value="${tweetAnalyserService.totalTweets}" /></p>
 <p>Tweets Waiting for Analysis: <c:out value="${tweetAnalyserService.totalUnprocessed}" /></p>
 <p>URLs Analysed: <c:out value="${tweetAnalyserService.totalProcessedEntities}" /></p>
 <br/><br/>
 
 <h1>Tasks</h1>
 <p>
 <a href="runjob.html?purgeProcessed=true">Purge Processed Tweets</a> &nbsp;&nbsp;&nbsp;&nbsp;<span style="color:red"></span>
 </p>
 <p>
 	<a href="runjob.html?purgeFailed=true">Purge failed analysis</a> &nbsp;&nbsp;&nbsp;&nbsp;<span style="color:red"></span>
 </p>
 <br/>
 <p>
 	<a href="runjob.html?purge=true">Purge all analysis</a> &nbsp;&nbsp;&nbsp;&nbsp;<span style="color:red">(Warning, This will delete all current analysis and reports.)</span>
 </p>
 <p>
 <a href="runjob.html?purgeAll=true">Purge all tweets</a> &nbsp;&nbsp;&nbsp;&nbsp;<span style="color:red">(Warning, This will delete all tweets including unprocessed tweets.)</span>
 </p>
 
  </div>
<%@ include file="footer.jsp" %>