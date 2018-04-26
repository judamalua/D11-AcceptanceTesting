<%--
 * display.jsp
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
<spring:message code="volume.title" var="titleTitle" />
<spring:message code="volume.description" var="titleDescription" />
<spring:message code="volume.year" var="titleYear" />

<spring:message code="volume.newspaper.title" var="titleNewspaper"/>
<spring:message code="volume.newspaper.publicationDate" var="titlePublication" />
<spring:message code="master.page.moment.format" var="formatMoment" />
<spring:message code="volume.newspaper.publicNewspaper" var="titlePublic" />

<h2>
	<jstl:out value="${volume.title}" />
</h2>

<br>
<h4>
	<spring:message code="volume.description" />
</h4>
<p>
	<jstl:out value="${volume.description}" />
</p>
<br />

<h4>
	<spring:message code="volume.year" />
</h4>
<p>
	<jstl:out value="${volume.year}" />
</p>

<!-- Button for joining the volume -->
<security:authorize access="hasRole('CUSTOMER')">
	<jstl:if test="${!subscriber}">
		<a href="volume/customer/subscribe.do?volumeId=${volume.id}">
			<button class="btn">
				<spring:message code="volume.subscribe" />
			</button>
		</a>
	</jstl:if>
	<br />
</security:authorize>

<!-- Displaying articles -->
<h4>
	<spring:message code="volume.newspapers.list" />
</h4>

<acme:pagination page="${page}" pageNum="${pageNum}"
	requestURI="volume/display.do?volumeId=${volume.id}&pageNewspaper=" />
	
<display:table name="${newspapers}" id="newspaper"
	requestURI="volume/display.do" pagesize="${pagesize}">
	
	<display:column>
		<jstl:if
			test="${newspaper.pictureUrl!=\"\" and newspaper.pictureUrl!=null}">
			<img src="${newspaper.pictureUrl}" class="newspaperImg" />
		</jstl:if>
	</display:column>
	
	<display:column property="title" title="${titleNewspaper}" sortable="true" />
	<display:column property="publicationDate" title="${titlePublication}"
		format="${formatMoment}" sortable="true" />
		
	<display:column title="${titlePublic}" sortable="true">
		<jstl:if test="${newspaper.publicNewspaper}">
			<i class="material-icons">public</i>
		</jstl:if>
		<jstl:if test="${!newspaper.publicNewspaper}">
			<i class="material-icons">not_interested</i>
		</jstl:if>
	</display:column>
	
	<display:column>
		<acme:button url="newspaper/display.do?newspaperId=${newspaper.id}"
			code="volume.newspaper.details" />
	</display:column>
		
</display:table>