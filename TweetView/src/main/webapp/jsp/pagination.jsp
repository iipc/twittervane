<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<table cellpadding="0" cellspacing="0" border="0" width="100%">
  <tr>
    <th class="first" width="5%">
      <c:if test="${page.previousPage}"><img style="cursor: pointer;" title="Prev" src="./images/left-arrow.gif" alt="Prev" border="0" onclick="return setPageNumber(${page.pageNumber - 1});"/></c:if>
	</th>
	<th class="first" width="90%" align="center">
	  <p>Results ${page.firstResult} to ${page.lastResult} of ${page.total}
	  <br/>
	  Page <select onchange="setPageNumber(this.value);">
	  <c:forEach begin="1" end="${page.numberOfPages + 1}" varStatus="s">
		<option value="<c:out value="${s.index}"/>" <c:if test="${s.index == page.pageNumber }">selected</c:if>><c:out value="${s.index}"/></option>
	  </c:forEach>	  
	  </select>
	  of <c:out value="${page.numberOfPages+1}"/>
	  </p>
	</th>
	<th class="first" width="5%" align="right">
	  <c:if test="${page.nextPage}"><img style="cursor: pointer;" title="Next" src="./images/right-arrow.gif" alt="Next" border="0" onclick="return setPageNumber(${page.pageNumber + 1});"/><img src="images/x.gif" alt="" width="10" height="1" border="0" /></c:if>				
	</th>
  </tr>
</table>
