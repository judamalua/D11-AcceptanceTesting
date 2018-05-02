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
<spring:message code="article.title" var="titleTitle" />
<spring:message code="article.summary" var="summaryTitle" />
<spring:message code="article.display" var="displayTitle" />
<spring:message code="master.page.moment.format" var="formatMoment" />

<jsp:useBean id="now" class="java.util.Date" />
<fmt:formatDate var="currentDate" value="${now}"
	pattern="yyyy-MM-dd HH:mm" />

<jstl:if test="${generalView}">
	<div class="row">
		<form action="article/search.do" method="get">
			<div class="input-field col s3">
				<input id="page" type="hidden" name="page" value="0" /> <input
					id="search" type="search" name="search" value="${search}" /> <label
					for="search"><i class="material-icons">search</i></label> <i
					class="material-icons">close</i>
			</div>
		</form>
	</div>
</jstl:if>

<!-- Pagination -->
<acme:pagination requestURI="${requestUri}search=${search}&page="
	pageNum="${pageNum}" page="${page}" />

<!-- Table -->
<display:table name="articles" id="articleList"
	requestURI="${requestUri}">

	<display:column property="title" title="${titleTitle}" />

	<display:column property="summary" title="${summaryTitle}" />

	<display:column title="${newspaper.publicNewspaper}">
		<acme:button url="article/display.do?articleId=${articleList.id}"
			code="article.display" />
	</display:column>
	<display:column>
		<security:authorize access="hasRole('ADMIN')">
			<acme:button
				url="article/admin/delete.do?articleId=${articleList.id}"
				code="article.delete" />
		</security:authorize>
	</display:column>
</display:table>
