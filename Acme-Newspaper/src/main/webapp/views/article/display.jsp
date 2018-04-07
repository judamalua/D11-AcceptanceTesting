<%--
 * action-2.jsp
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
<spring:message code="master.page.moment.format" var="formatMoment" />


<spring:message code="followUp.title" var="titleFollowUp" />
<spring:message code="followUp.summary" var="titleSummaryFollowUp" />
<spring:message code="followUp.text" var="titleTextFollowUp" />
<spring:message code="followUp.publicationDate" var="titlePublicationFollowUp" />


<jsp:useBean id="now" class="java.util.Date" />
<fmt:formatDate var="currentDate" value="${now}"
	pattern="yyyy-MM-dd HH:mm" />
<spring:message var="format" code="master.page.moment.format.out" />

<!-- Display -->

<h2>
	<jstl:out value="${article.title}" />
</h2>
<br>
<h4>
	<spring:message code="article.summary" />
</h4>
<p>
	<jstl:out value="${article.summary}" />
</p>
<br />

<h4>
	<spring:message code="article.body" />
</h4>
<p>
	<jstl:out value="${article.body}" />
</p>
<h4>
	<spring:message code="article.writer" />
</h4>
<p>
	<a href="user/display.do?userId=${writer.id}" ><jstl:out value="${writer.name}" /></a>
</p>

<!-- Displaying followUps -->
<h4>
	<spring:message code="article.followUp.list" />
</h4>
<acme:pagination page="${page}" pageNum="${pageNum}"
	requestURI="newspaper/display.do?newspaperId=${newspaper.id}&page=" />
	
<display:table name="${followUps}" id="article"
	requestURI="newspaper/display.do" pagesize="${pagesize}">
	<display:column title="${titleFollowUp}" property="title" sortable="true"/>
	<display:column property="publicationDate" format="${formatMoment}" title="${titlePublicationFollowUp}" />
	<display:column property="summary" title="${titleSummaryFollowUp}" />
	<display:column property="text" title="${titleTextFollowUp}" />
	<security:authorize access="hasRole('ADMIN')">
		<display:column>
			<acme:button url="followUp/admin/delete.do?followUpId=${followUp.id}"
				code="followUp.delete" />
		</display:column>
	</security:authorize>
</display:table>

<acme:button url="followUp/user/create.do?articleId=${article.id}" code="followUp.create" />
<br />
<security:authorize access="hasRole('ADMIN')">
	<acme:button url="newspaper/admin/delete.do" code="newspaper.delete" />
</security:authorize>
