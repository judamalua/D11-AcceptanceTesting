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
<spring:message code="volume.title" var="titleTitle" />
<spring:message code="volume.description" var="titleDescription" />
<spring:message code="volume.year" var="titleYear" />
<spring:message code="volume.newspapers" var="titleNewspapers" />
<spring:message code="volume.newspapers.list" var="listNewspapers" />


<!-- Pagination -->
<acme:pagination requestURI="${requestUri}page=" pageNum="${pageNum}"
	page="${page}" />
	
	
<!-- Table -->
<display:table name="${volumes}" id="volumeList"
	requestURI="${requestUri}page=${page}">

	<display:column property="title" title="${titleTitle}"  />
	<display:column property="description" title="${titleDescription}"/>
	<display:column property="year" title="${titleYear}"  />

	<display:column>
		<acme:button url="volume/display.do?volumeId=${volumeList.id}"
			code="volume.display" />
	</display:column>
	
	<display:column>
		<jstl:if test="${volumeList.user.id == userId}">
				<acme:button url="volume/user/edit.do?volumeId=${volumeList.id}"
					code="volume.edit" />	
		</jstl:if>
	</display:column>	
	
</display:table>

<security:authorize access="hasRole('USER')">
	<acme:button url="volume/user/create.do" code="volume.create" />
</security:authorize>