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

<form:form action="review/admin/edit.do" modelAttribute="review">

	<form:hidden path="id" />
	<form:hidden path="version" />
	<form:hidden path="draf" />

	<p>
		<em><spring:message code="configuration.all.fields.required" /></em>
	</p>

	<acme:textbox code="review.title" path="title" required="true" />
	<acme:textbox code="review.description" path="description" required="true" />
	
	
	<form:label path="gauge">
		<spring:message code="review.gauge"/>
	</form:label>
	<form:select path="gauge">
		<form:option value="1">1</form:option>
		<form:option value="2">2</form:option>
		<form:option value="3">3</form:option>
	</form:select>
	<form:errors cssClass="error" path="gauge"/>
	<br/>
	
	

	<acme:submit name="saveAsDraf" code="configuration.save" />
	<acme:submit name="saveAsFinal" code="configuration.save" />

	<jstl:if test="${review.id!=0}">
		<acme:delete clickCode="tag.delete.confirm" name="delete"
			code="tag.delete" />
	</jstl:if>

	<acme:cancel url="configuration/admin/list.do"
		code="configuration.cancel" />


</form:form>
