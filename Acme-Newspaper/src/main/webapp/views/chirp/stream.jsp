<%@page language="java" contentType="title/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="security"
	uri="http://www.springframework.org/security/tags"%>
<%@taglib prefix="display" uri="http://displaytag.sf.net"%>
<%@ taglib prefix="acme" tagdir="/WEB-INF/tags"%>



<acme:pagination requestURI="${requestURI}page=" pageNum="${pageNum}"
	page="${page}" />

<display:table name="chirps" id="chirpList" requestURI="${requestURI}"
	class="displayTag">

	<spring:message code="chirp.author" var="author" />
	<display:column title="${author}">
		<jstl:out value="${authors[chirpList_rowNum-1]}" />
	</display:column>

	<spring:message code="chirp.moment.format" var="momentFormat" />
	<spring:message code="chirp.moment" var="chirpMoment" />
	<display:column property="moment" title="${chirpMoment}"
		format="${momentFormat}" />

	<spring:message code="chirp.title" var="title" />
	<display:column property="title" title="${title}" />

	<spring:message code="chirp.description" var="description" />
	<display:column property="description" title="${description}" />

	<security:authorize access="hasRole('ADMIN')">
		<spring:message code="chirp.delete" var="titleDelete" />
	</security:authorize>

	<display:column title="${titleDelete}">
		<security:authorize access="hasRole('ADMIN')">
			<acme:button url="chirp/admin/delete.do?chirpId=${chirpList.id}"
				code="chirp.delete" />
		</security:authorize>
	</display:column>


</display:table>

<jstl:if test="${requestURI == 'chirp/user/list.do'}">
	<security:authorize access="hasRole('USER')">
		<acme:button url="chirp/user/create.do" code="chirp.create" />
	</security:authorize>
</jstl:if>
