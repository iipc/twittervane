<%@page import="uk.bl.wap.crowdsourcing.*"%>
<%@taglib prefix = "c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ include file="header.jsp" %>
<div id="main">
		<h1>Reports</h1>
		
		<form method="POST" action="report.html">
            <label>Collection</label>
	            <select name="collectionId" id="collectionId">
	            <c:forEach items="${webCollections}" var="webCollection">
	           		<option value="${webCollection.id}"><c:out value="${webCollection.name}" /></option>
	            </c:forEach>
	            </select>
			<label>Report Type</label>
				<select name="reportType">
					<option value="domain">Top Domains</option>
					<option value="topUrl">Top URLs</option>
					<option value="popUrl">Top URL by Retweet</otpion>
					<!-- <option value="failed">Failed Analysis</otpion> -->
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
		<p></p>
		<h1>Browse Reports</h1>
		<div STYLE="font-size: 15px; color: black">
		<p>
		<a href="./reportView.html?report=tweetSummaryByCollection&sort=desc">Tweet Summary By Collection</a>
		</p>
		<p>
		<a href="./reportView.html?report=topUrlsByCollection&sort=desc">Top URL By Collection</a>
		</p>
		</div>
		<p></p>
		<script language="javascript">
			$(document).ready(function(){
				var str = "<%=request.getScheme()+"://"+request.getServerName() %>:<%= request.getLocalPort() %>${pageContext.request.contextPath}/rest/xmlreport/" + $("#collectionId").val();
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
					var str = "<%=request.getScheme()+"://"+request.getServerName() %>:<%= request.getLocalPort() %>${pageContext.request.contextPath}/rest/xmlreport/" + $("#collectionId").val();
					
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