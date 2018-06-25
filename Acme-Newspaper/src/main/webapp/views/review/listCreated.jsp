<%@page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="security"
	uri="http://www.springframework.org/security/tags"%>
<%@taglib prefix="display" uri="http://displaytag.sf.net"%>
<%@ taglib prefix="acme" tagdir="/WEB-INF/tags" %>
<%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>


<!-- Pagination -->
<acme:pagination requestURI = "review/admin/list.do?page=" pageNum = "${pageNum}" page = "${page}"/>



<jstl:if test="${review.gauge == 1}">
		<jstl:set var="warningColor" value="#ff0000" />
	</jstl:if>

	<jstl:if test="${review.gauge == 2}">
		<jstl:set var="warningColor" value="#00ff00" />
	</jstl:if>

	<jstl:if test="${review.gauge == 3}">
		<jstl:set var="warningColor" value="#0000ff" />
	</jstl:if>

<display:table name="reviews" id="review" requestURI="review/admin/list.do?page=${page}"
	class="displaytag">
	
	
	

	<spring:message code="review.title" var="title" />
	<display:column property="title" title="${title}" style ="background-color:${warnigColor};" />

	<spring:message code="review.description" var="description" />
	<display:column property="description" title="${description}"  />
	
	<spring:message code="review.gauge" var="gauge" />
	<display:column property="gauge" title="${gauge}"  />


<jstl:if test="${review.draf == true}">
<display:column>
		<a href="review/admin/edit.do?reviewId=${review.id}">
			<button class="btn">
				<spring:message code="tag.edit" />
			</button>
		</a>
</display:column></jstl:if>

<display:column>
		<a href="review/admin/delete.do?reviewId=${review.id}">
			<button class="btn">
				<spring:message code="review.delete" />
			</button>
		</a>
	</display:column>
	
	

</display:table>

