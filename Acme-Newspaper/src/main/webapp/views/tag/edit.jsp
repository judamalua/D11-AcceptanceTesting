<%--
 * action-1.jsp
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
<%@taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="security"
	uri="http://www.springframework.org/security/tags"%>
<%@taglib prefix="display" uri="http://displaytag.sf.net"%>
<%@ taglib prefix="acme" tagdir="/WEB-INF/tags"%>

<form:form action="tag/admin/edit.do" modelAttribute="tag">

	<form:hidden path="id" />
	<form:hidden path="version" />

	<p>
		<em><spring:message code="configuration.all.fields.required" /></em>
	</p>

	<acme:textbox code="tag.name" path="name" required="true" />

	<spring:message code="tag.keywords.info" />
	<acme:textarea code="tag.keywords" path="keywords" />

	<acme:submit name="save" code="configuration.save" />

	<jstl:if test="${tag.id!=0}">
		<acme:delete clickCode="tag.delete.confirm" name="delete"
			code="tag.delete" />
	</jstl:if>

	<acme:cancel url="configuration/admin/list.do"
		code="configuration.cancel" />


</form:form>
