<%@page import="uk.bl.wap.crowdsourcing.*"%>
<%@taglib prefix = "c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@page import="java.util.Iterator" %>

<%@ include file="header.jsp" %>

<div id="main">
 <h1>Twitter Stream</h1>

		 <p><b>Twitter Stream Status:</b> ${message.message}</p>
		 <p><b>Twitter Stream Manager:</b>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
		 <a href="twitterstream.html?action=start">Start</a>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
		 <a href="twitterstream.html?action=stop">Stop</a></p>
		 <p><b>Tweet analysis run for every <c:out value="${analysisTriggerValue}" /> tweets recieved</b></p>
		 <p><b>Last <c:out value="${displayLastStreamErrors}" /> Twitter stream errors:</b><br/>
		 <c:forEach items="${lastStreamErrors}" var="error">
		 	<c:out value="${error}" /><br/>
		 </c:forEach>
		 </p>
		  <br/>
		 <h2>Twitter Stream Required</h2>
		 <p>If any of the below are in red the twitter stream will not start.</p>
		 <p><b>Terms:</b>
		 <c:set var="searchTermText" value="" />
		 <c:set var="termsFound" value="false" />
		 <c:forEach items="${allSearchTerms}" var="searchTerm">
		    <c:if test="${termsFound eq 'true'}">
		    	<c:out value=", " />
		    </c:if>
		 	<c:out value="${searchTerm}" />
		 	<c:set var="termsFound" value="true" />
		 </c:forEach>
	 	<c:if test="${termsFound eq 'false'}">
	 		<span style="color:red">Required</span>
	 	</c:if>
		  </p>
		 <p>
		 <b>Twitter Api</b>
			<ul>
				<li><b>Consumer Key:</b> 
				<c:choose>
					<c:when test="${appConfig.consumerKey ne ''}">
						<c:out value="${appConfig.consumerKey}" />
					</c:when>
					<c:otherwise>
						<span style="color:red">Required</span>
					</c:otherwise>
				</c:choose>
				</li>
				<li><b>Consumer Secret:</b>
				<c:choose>
					<c:when test="${appConfig.consumerSecret ne ''}">
						<c:out value="${appConfig.consumerSecret}" />
					</c:when>
					<c:otherwise>
						<span style="color:red">Required</span>
					</c:otherwise>
				</c:choose>
				</li>
				<li><b>Access Token:</b>
				<c:choose>
					<c:when test="${appConfig.accessToken ne ''}">
						<c:out value="${appConfig.accessToken}" />
					</c:when>
					<c:otherwise>
						<span style="color:red">Required</span>
					</c:otherwise>
				</c:choose>
				</li>
				<li><b>Access Key:</b>
				<c:choose>
					<c:when test="${appConfig.accessTokenSecret ne ''}">
						<c:out value="${appConfig.accessTokenSecret}" />
					</c:when>
					<c:otherwise>
						<span style="color:red">Required</span>
					</c:otherwise>
				</c:choose>
				</li>
			</ul>
		</p>
 </div>
<%@ include file="footer.jsp" %>