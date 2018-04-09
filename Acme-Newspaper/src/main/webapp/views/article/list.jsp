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
<%@ taglib prefix="acme" tagdir="/WEB-INF/tags"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>

<!-- Variable declaration -->
<spring:message code="article.title" var="titleTitle" />
<spring:message code="article.summary" var="summaryTitle" />
<spring:message code="article.display" var="displayTitle" />
<spring:message code="master.page.moment.format" var="formatMoment" />

<jsp:useBean id="now" class="java.util.Date" />
<fmt:formatDate var="currentDate" value="${now}"
	pattern="yyyy-MM-dd HH:mm" />

<!-- Pagination -->
<acme:pagination requestURI="${requestUri}page=" pageNum="${pageNum}"
	page="${page}" />

<!-- Table -->
<display:table name="articles" id="article"
	requestURI="${requestUri}">

	<display:column property="title" title="${titleTitle}" sortable="true" />

	<display:column property="summary" title="${summaryTitle}" sortable="true" />
	
	<display:column title="${displayTitle}">
		<acme:button
				url="article/display.do?articleId=${article.id}"
				code="article.display" />
	</display:column>
	<display:column>
		<security:authorize access="hasRole('ADMIN')">
			<acme:button
				url="article/admin/delete.do?articleId=${article.id}"
				code="article.delete" />
		</security:authorize>
	</display:column>
</display:table>
