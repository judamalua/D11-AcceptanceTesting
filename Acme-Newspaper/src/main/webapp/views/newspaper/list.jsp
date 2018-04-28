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
<spring:message code="newspaper.title" var="titleName" />
<spring:message code="newspaper.publicNewspaper" var="titlePublic" />
<spring:message code="newspaper.description" var="titleDescription" />
<spring:message code="newspaper.publicationDate" var="titlePublication" />
<spring:message code="master.page.moment.format" var="formatMoment" />
<jsp:useBean id="now" class="java.util.Date" />
<fmt:formatDate var="currentDate" value="${now}"
	pattern="yyyy-MM-dd HH:mm" />

<div class="row">
	<form action="newspaper/search.do" method="get">
		<div class="input-field col s3">
			<input id="page" type="hidden" name="page" value="0" /> <input
				id="search" type="search" name="search" > <label
				 for="search"><i class="material-icons">search</i></label>
			<i class="material-icons">close</i>
		</div>
	</form>
</div>


<!-- Pagination -->
<acme:pagination requestURI="${requestUri}page=" pageNum="${pageNum}"
	page="${page}" />

<!-- Table -->
<display:table name="newspapers" id="newspaperList"
	requestURI="${requestUri}page=${page}">

	<display:column>
		<jstl:if
			test="${newspaperList.pictureUrl!=\"\" and newspaperList.pictureUrl!=null}">
			<img src="${newspaperList.pictureUrl}" class="newspaperImg" />
		</jstl:if>
	</display:column>
	<display:column property="title" title="${titleName}" />
	<display:column property="publicationDate" title="${titlePublication}"
		format="${formatMoment}"/>
	<display:column title="${titlePublic}" >
		<jstl:if test="${newspaperList.publicNewspaper}">
			<i class="material-icons">public</i>
		</jstl:if>
		<jstl:if test="${!newspaperList.publicNewspaper}">
			<i class="material-icons">not_interested</i>
		</jstl:if>
	</display:column>
	<display:column>
		<acme:button url="newspaper/display.do?newspaperId=${newspaperList.id}"
			code="newspaper.details" />
	</display:column>


	<display:column>
		<security:authorize access="hasRole('USER')">
			<jstl:if
				test="${(owner or ownNewspaper[newspaperList_rowNum-1]) and newspaperList.publicationDate==null}">
				<acme:button
					url="newspaper/user/edit.do?newspaperId=${newspaperList.id}"
					code="newspaper.edit" />
			</jstl:if>
		</security:authorize>
	</display:column>

	<display:column>
		<security:authorize access="hasRole('USER')">
			<jstl:if
				test="${(owner or ownNewspaper[newspaperList_rowNum-1]) and newspaperList.publicationDate==null and canPublicate!=null and  canPublicate[newspaperList_rowNum-1] }">
				<acme:button
					url="newspaper/user/publish.do?newspaperId=${newspaperList.id}"
					code="newspaper.publish" />
			</jstl:if>
		</security:authorize>
	</display:column>

	<display:column>
		<security:authorize access="hasRole('ADMIN')">
			<acme:button
				url="newspaper/admin/delete.do?newspaperId=${newspaperList.id}"
				code="newspaper.delete" />
		</security:authorize>
	</display:column>
</display:table>

<!-- Creating a new newspaper -->

<security:authorize access="hasRole('USER')">
	<acme:button url="newspaper/user/create.do" code="newspaper.create" />
</security:authorize>
