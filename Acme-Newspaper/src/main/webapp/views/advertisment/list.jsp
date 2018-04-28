<%--
 * list.jsp
 *
 * Copyright (C) 2017 Universidad de Sevilla
 * 
 * The use of this project is hereby constrained to the conditions of the 
 * TDG Licence, a copy of which you may download from 
 * http://www.tdg-seville.info/License.html
 --%>

<%@page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="security"
	uri="http://www.springframework.org/security/tags"%>
<%@taglib prefix="display" uri="http://displaytag.sf.net"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="acme" tagdir="/WEB-INF/tags"%>

<!-- Variable declaration -->
<spring:message code="advertisment.title" var="titleTitle" />
<spring:message code="advertisment.bannerURL" var="bannerURLTitle" />
<spring:message code="advertisment.additionalInfoLink"
	var="additionalInfoLinkTitle" />

<!-- Pagination -->
<acme:pagination requestURI="${requestUri}" page="${page}"
	pageNum="${pageNum}" />

<!-- Table -->
<display:table name="advertisments" id="advertisment"
	requestURI="${requestUri}">

	<display:column property="title" title="${titleTitle}" sortable="true" />

	<display:column title="${bannerURLTitle}">
		<img src="${bannerURL}" />
	</display:column>

	<display:column title="${additionalInfoLinkTitle}">
		<a href="${additionalInfoLink}">${additionalInfoLink}</a>
	</display:column>

	<security:authorize access="hasRole('AGENT')">
		<display:column>
			<acme:button
				url="advertisment/agent/edit.do?advertismentId=${advertisment.id}"
				code="advertisment.edit" />
		</display:column>
	</security:authorize>
	<security:authorize access="hasRole('ADMIN')">
		<display:column>
			<acme:button
				url="advertisment/admin/delete.do?advertismentId=${advertisment.id}"
				code="advertisment.delete" />
		</display:column>
	</security:authorize>
</display:table>
