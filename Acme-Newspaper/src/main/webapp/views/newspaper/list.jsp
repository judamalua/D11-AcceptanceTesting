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

<!-- Variable declaration -->
<spring:message code="newspaper.title" var="titleName" />
<spring:message code="newspaper.description" var="titleDescription" />
<spring:message code="newspaper.publicationDate" var="titlePublication" />
<spring:message code="master.page.moment.format" var="formatMoment" />
<jsp:useBean id="now" class="java.util.Date" />
<fmt:formatDate var="currentDate" value="${now}"
	pattern="yyyy-MM-dd HH:mm" />

<!-- Pagination -->
<acme:pagination requestURI="${requestURI}page=" pageNum="${pageNum}"
	page="${page}" />

<!-- Table -->

<display:table name="newspapers" id="newspaper"
	requestURI="${requestUri}">

	<display:column>
		<img src="${newspaper.pictureUrl}" class="newspaperImg" />
	</display:column>
	<display:column property="title" title="${titleName}" sortable="true" />
	<display:column property="publicationDate" title="${titlePublication}"
		format="${formatMoment}" sortable="true" />
	<display:column>
		<acme:button url="newspaper/display.do?newspaperId=${newspaper.id}"
			code="newspaper.details" />
	</display:column>


	<display:column>
		<security:authorize access="hasRole('USER')">
			<jstl:if
				test="${(owner or ownNewspaper[newspaper_rowNum-1]) and newspaper.publicationDate==null}">
				<acme:button
					url="newspaper/user/edit.do?newspaperId=${newspaper.id}"
					code="newspaper.edit" />
			</jstl:if>
		</security:authorize>
	</display:column>

	<display:column>
		<security:authorize access="hasRole('ADMIN')">
			<acme:button
				url="newspaper/admin/delete.do?newspaperId=${newspaper.id}"
				code="newspaper.delete" />
		</security:authorize>
	</display:column>
</display:table>

<!-- Creating a new newspaper -->

<security:authorize access="hasRole('USER')">
	<acme:button url="newspaper/user/create.do" code="newspaper.create" />
</security:authorize>
