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
<spring:message code="advertisement.title" var="titleTitle" />
<spring:message code="advertisement.bannerURL" var="bannerURLTitle" />
<spring:message code="advertisement.additionalInfoLink"
	var="additionalInfoLinkTitle" />

<!-- Pagination -->
<jstl:if test="${!adminList}">
	<acme:pagination
		requestURI="${requestUri}newspaperId=${newspaper.id}&page="
		page="${page}" pageNum="${pageNum}" />
</jstl:if>
<jstl:if test="${adminList}">
	<acme:pagination
		requestURI="${requestUri}taboo=${taboo}&page="
		page="${page}" pageNum="${pageNum}" />
</jstl:if>
<!-- Table -->
<display:table name="advertisements" id="advertisementList"
	requestURI="${requestUri}">

	<display:column property="title" title="${titleTitle}" class="col s3" />

	<display:column title="${bannerURLTitle}" class="col s3">
		<img class="materialboxed" width="100"
			src="${advertisementList.bannerURL}" />
	</display:column>

	<display:column title="${additionalInfoLinkTitle}" class="col s3">
		<a href="${advertisementList.additionalInfoLink}">${advertisementList.additionalInfoLink}</a>
	</display:column>

	<security:authorize access="hasRole('AGENT')">
		<display:column class="col s3">
			<acme:button
				url="advertisement/agent/edit.do?advertisementId=${advertisementList.id}"
				code="advertisement.edit" />
		</display:column>
		<display:column class="col s3">
			<acme:button
				url="newspaper/agent/list.do?advertisementId=${advertisementList.id}&hasAdvertisement=true"
				code="advertisement.newspaperList" />
		</display:column>
		<display:column class="col s3">
			<acme:button
				url="newspaper/agent/list.do?advertisementId=${advertisementList.id}&hasAdvertisement=false"
				code="advertisement.newspaperNoList" />
		</display:column>
	</security:authorize>
	<security:authorize access="hasRole('ADMIN')">
		<display:column class="col s3">
			<acme:button
				url="advertisement/admin/delete.do?advertisementId=${advertisementList.id}"
				code="advertisement.delete" />
		</display:column>
	</security:authorize>

	<!-- To add an ad to a newspaper -->
	<jstl:if test="${newspaper != null}">
		<display:column class="col s3">
			<jstl:if test="${!isAdvertised[advertisementList_rowNum-1]}">
				<acme:button
					url="advertisement/agent/advertise.do?advertisementId=${advertisementList.id}&newspaperId=${newspaper.id}"
					code="advertisement.advertise" />
			</jstl:if>
			<jstl:if test="${isAdvertised[advertisementList_rowNum-1]}">
				<acme:button
					url="advertisement/agent/unadvertise.do?advertisementId=${advertisementList.id}&newspaperId=${newspaper.id}"
					code="advertisement.unadvertise" />
			</jstl:if>
		</display:column>

	</jstl:if>
</display:table>

<security:authorize access="hasRole('AGENT')">
	<acme:button url="advertisement/agent/create.do"
		code="advertisement.create" />
</security:authorize>
