<%@page import="uk.bl.wap.crowdsourcing.*"%>
<jsp:useBean id="webCollectionDao" type="uk.bl.wap.crowdsourcing.dao.WebCollectionDao" scope="request" />
<%@ include file="header.jsp" %>
<div id="main">
		<h1>Reports</h1>
		
		<form method="POST" action="report.html">
            <label>Collection</label>
	            <select name="collectionId" id="collectionId">
	            	<%
	        		for (WebCollection wc : webCollectionDao.getAllCollections()) { 
	        		%>
	        			<option value="<%= wc.getId()%>"><%= wc.getName() %></option>
		  			<%
		  			} 
		  			%>
	            	
	            </select>
			<label>Report Type</label>
				<select name="reportType">
					<option value="topUrl">Top URLs</option>
					<option value="domain">Top Domains</option>
					<option value="popUrl">Top URL by Retweet</otpion>
					<option value="failed">Failed Analysis</otpion>
				</select>
				<input type="hidden" name="reportFormat" value="html">
<!--			<label>Report Format</label>
				<select name="reportFormat">
					<option value="html">HTML</option>
					<option value="xml">XML</option>
					<option value="json">JSON</otpion>
				</select>
-->
			<label>Filter URL</label><input type="text" name="filterUrl" id="filterUrl"></input>
			<label>Filter Domain</label><input type="text" name="filterDomain" id="filterDomain"></input>
				
				<br/><br/>
            <input type="submit" value="Generate Report" />
        </form>
		<br/>
		<br/>
		XML request: <a href="" id="a_xml" target="_new"></a>
		<script language="javascript">
			$(document).ready(function(){
				var str = "http://<%= request.getServerName() %>:<%= request.getLocalPort() %>${pageContext.request.contextPath}/rest/xmlreport/" + $("#collectionId").val();
				$("#a_xml").attr("href",str);
				$('#a_xml').html(str);
				
				$('#collectionId').change(function() {
					updateXml();
				});
				$('#filterDomain').keyup(function() {
					updateXml();
				});
				$('#filterUrl').keyup(function() {
					updateXml();
				});

				function updateXml() {
					var str = "http://<%= request.getServerName() %>:<%= request.getLocalPort() %>${pageContext.request.contextPath}/rest/xmlreport/" + $("#collectionId").val();
					
					if ( $('#filterDomain').val() !== "" && $('#filterUrl').val() !== "" ) {
						str += "/filterDomain/" + $('#filterDomain').val().replace(/\./g,"__") + "/filterURl/" + $('#filterUrl').val().replace(/\./g,"__");
					} else {
						if ($('#filterDomain').val() !== "") {
							str += "/filterDomain/" + $('#filterDomain').val().replace(/\./g,"__");
						}
						if ($('#filterUrl').val() !== "") {
							str += "/filterUrl/" + $('#filterUrl').val().replace(/\./g,"__");
						}
					}
					
					$("#a_xml").attr("href",str);
					$('#a_xml').html(str);
				}
			});
		</script>
 </div>
<%@ include file="footer.jsp" %>