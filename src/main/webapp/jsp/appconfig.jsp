<%@page import="uk.bl.wap.crowdsourcing.*"%>
<%@page import="java.util.Iterator" %>

<jsp:useBean id="appConfig" type="uk.bl.wap.crowdsourcing.AppConfig" scope="request" />

<%@ include file="header.jsp" %>

<div id="main">
 <h1>Application Configuration</h1>
		<form method="POST" action="appconfig.html">
        <input type="hidden" value="<%= appConfig.getId() %>" name="id">
        <h2>Twitter API</h2>
  		<label>Consumer Key</label><input type="text" name="consumerKey" maxlength="100" value="<%= appConfig.getConsumerKey() %>"/>
  		<label>Consumer Secret</label><input type="text" name="consumerSecret" maxlength="100" value="<%= appConfig.getConsumerSecret() %>"/>
  		<label>Access Token</label><input type="text" name="accessToken" maxlength="100"  value="<%= appConfig.getAccessToken() %>"/>
        <label>Access Secret</label><input type="text" name="accessTokenSecret" maxlength="100" value="<%= appConfig.getAccessTokenSecret() %>"/>
		<h2>Bit.ly API</h2>
		<label>bit.ly Login</label><input type="text" name="bitlyLogin" maxlength="100" value="<%= appConfig.getBitlyLogin() %>"/>
		<label>bit.ly ApiKey</label><input type="text" name="bitlyApiKey" maxlength="100" value="<%= appConfig.getBitlyApiKey()%>"/>
		<h2>Http Proxy </h2>
		 <p>Optional - Leave blank not to use a proxy to connect to Twitter Streaming API,</p>
		<label>Proxy Host</label><input type="text" name="proxyHost" maxlength="100" value="<%= appConfig.getHttpProxyHost() %>"/>
  		<label>Proxy Port</label><input type="text" name="proxyPort" maxlength="100" value="<%= appConfig.getHttpProxyPort() %>"/>
  		<label>Proxy User</label><input type="text" name="proxyUser" maxlength="100"  value="<%= appConfig.getHttpProxyUser() %>"/>
        <label>Proxy Password</label><input type="text" name="proxyPass" maxlength="100" value="<%= appConfig.getHttpProxyPass() %>"/>
		
		
		<input type="submit" value="Save" />&nbsp;&nbsp;<font color="red">${message.message}</font>
        </form>
 </div>
<%@ include file="footer.jsp" %>