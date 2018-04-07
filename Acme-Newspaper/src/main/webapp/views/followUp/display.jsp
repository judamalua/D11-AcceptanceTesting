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
<jsp:useBean id="now" class="java.util.Date" />
<fmt:formatDate var="currentDate" value="${now}"
	pattern="yyyy-MM-dd HH:mm" />
<spring:message var="format" code="master.page.moment.format.out" />
<fmt:formatDate var="formatPublicationDate"
	value="${followUp.publicationDate}" pattern="${format}" />

<!-- Display -->

<h2>
	<jstl:out value="${followUp.title}" />
</h2>
<br>
<h4>
	<spring:message code="followUp.publicationDate" />:<jstl:out value="${formatPublicationDate}" />
</h4>
<br />
<h3>
	<jstl:out value="${followUp.text}" />
</h3>
<br>

<h4>
	<spring:message code="followUp.summary" />
</h4>
<p>
	<jstl:out value="${followUp.summary}" />
</p>
<br />

<h4>
	<spring:message code="followUp.user" />
</h4>
<p>
	<a href="user/display.do?userId=${creator.id}" ><jstl:out value="${creator.name}" /></a>
</p>

<!-- Displaying Pictures -->
<h4>
	<spring:message code="followUp.pictureUrls" />
</h4>

<jstl:forEach items="${followUp.pictureUrls}" var="picture">
	<jstl:if test="${picture != \"\"}">
	<div class="parallax-container">
		<div class="parallax">
			<img src="${picture}">
		</div>
	</div>
</jstl:if>   
</jstl:forEach>


<security:authorize access="hasRole('ADMIN')">
	<acme:button url="followUp/admin/delete.do" code="followUp.delete" />
</security:authorize>
