
<%@page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="security"
	uri="http://www.springframework.org/security/tags"%>
<%@taglib prefix="display" uri="http://displaytag.sf.net"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="acme" tagdir="/WEB-INF/tags"%>

<!-- Variables -->
<spring:message code="master.page.moment.format" var="formatDate" />
<spring:message var="format" code="master.page.moment.format.out" />
<spring:message var="birthDateFormat" code="master.page.birthDate" />

<!-- Personal data -->
<br />
<h4>
	<jstl:out value="${actor.name}" />
	<jstl:out value="${actor.surname}" />
</h4>
<br />

<strong><spring:message code="actor.postalAddress" />:</strong>
<jstl:out value="${actor.postalAddress}" />
<br />
<strong><spring:message code="actor.phoneNumber" />:</strong>
<jstl:out value="${actor.phoneNumber}" />
<br />
<strong><spring:message code="actor.email" />:</strong>
<jstl:out value="${actor.email}" />
<br />
<strong><spring:message code="actor.birthDate" />:</strong>
<fmt:formatDate value="${actor.birthDate}" pattern="${birthDateFormat}" />
<br />
<br />

<!-- Display created Rendezvouses-->
<jstl:if test="${isUserProfile}">
	<h4>
		<spring:message code="actor.published.articles" />
	</h4>

	
	<!-- Pagination -->
	<acme:pagination pageNum="${articlePageNum}" requestURI="user/display.do?actorId=${actor.id}&chirpPage=${chirpPage}&articlePage=" page = "${articlePage}"/>

	<display:table name="${articles}" id="article"
		requestURI="user/display.do">

		<spring:message code = "article.title" var = "articleTitle"/>
		<display:column property="title" title="${articleTitle}" sortable="true" />
		<spring:message code = "article.summary" var = "articleSummary"/>
		<display:column property="summary" title="${articleSummary}" />
		
	</display:table>
	
	<h4>
		<spring:message code="actor.published.chirps" />
	</h4>

	
	<!-- Pagination -->
	<acme:pagination pageNum="${chirpPageNum}" requestURI="user/display.do?actorId=${actor.id}&articlePage=${articlePage}&chirpPage=" page = "${chirpPage}"/>

	<display:table name="${chirps}" id="chirp"
		requestURI="user/display.do">

		<spring:message code = "chirp.title" var = "chirpTitle"/>
		<display:column property="title" title="${chirpTitle}"/>
		<spring:message code = "chirp.description" var = "chirpDescription"/>
		<display:column property="description" title="${chirpDescription}" />
		<spring:message code = "chirp.moment" var = "chirpMoment"/>
		<display:column property="moment" title="${chirpMoment}" sortable = "true" format = "${formatDate}"/>
		
		<spring:message code="actor.delete" var="titleDelete" />
		<security:authorize access="hasRole('ADMIN')">
			<display:column title="${titleDelete}">
				<acme:button url="chirp/admin/delete.do?chirpId=${chirp.id}" code="actor.delete"/>
			</display:column>
		</security:authorize>
		
	</display:table>
 </jstl:if>