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

<spring:message code="master.page.moment.format" var="formatDate"/>
<spring:message code="master.page.moment.format.out" var="formatDateOut"/>
<spring:message code="language" var="language"/>



<!-- Pagination -->
<acme:pagination requestURI = "review/admin/listCreated.do?page=" pageNum = "${pageNum}" page = "${page}"/>


<jstl:set var="warningColor" value="" />


<display:table name="reviews" id="review" requestURI="review/admin/listCreated.do?page=${page}"
	class="displaytag">
	
	
	<jstl:if test="${review.gauge ==1}">
		<jstl:set var="warningColor" value="lightyellow" />
	</jstl:if>

	<jstl:if test="${review.gauge ==2}">
		<jstl:set var="warningColor" value="moccasin" />
	</jstl:if>

	<jstl:if test="${review.gauge ==3}">
		<jstl:set var="warningColor" value="blue" />
	</jstl:if>
	
	<spring:message code="review.ticker" var="ticker" />
	<display:column property="ticker" title="${ticker}" />
	
	<spring:message code="review.moment" var="moment" />
	<display:column property="moment" title="${moment}" sortable="false"
		format="${formatDate}" />

	<spring:message code="review.title" var="title" />
	<display:column property="title" title="${title}" style ="background-color:${warningColor};" />

	<spring:message code="review.description" var="description" />
	<display:column property="description" title="${description}"  />
	
	<spring:message code="review.gauge" var="gauge" />
	<display:column property="gauge" title="${gauge}"  />
	
	
	


<jstl:if test="${review.draf == true}">
<display:column>
		<a href="review/admin/edit.do?reviewId=${review.id}&&newspaperId=${review.newspaper.id}">
			<button class="btn">
				<spring:message code="review.edit" />
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



