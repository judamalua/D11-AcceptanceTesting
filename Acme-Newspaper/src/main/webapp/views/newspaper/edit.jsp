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
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="security"
	uri="http://www.springframework.org/security/tags"%>
<%@taglib prefix="display" uri="http://displaytag.sf.net"%>
<%@ taglib prefix="acme" tagdir="/WEB-INF/tags"%>


<!-- Variables -->
<spring:message code="newspaper.moment.placeholder"
	var="momentPlaceholder" />

<!-- Form -->
<p>
	<em><spring:message code="form.required.params" /></em>
</p>
<div class="row">
	<form:form action="newspaper/user/edit.do" modelAttribute="newspaper">

		<form:hidden path="id" />
		<form:hidden path="version" />

		<acme:textbox code="newspaper.title" path="title" required="true" />
		<acme:textarea code="newspaper.description" path="description"
			required="true" />
		<acme:textbox code="newspaper.pictureUrl" path="pictureUrl" required="true" />
		
		<div class="cleared-div">
			<acme:checkbox code="newspaper.publicNewspaper"
				path="publicNewspaper" id="publicNewspaper" />
		</div>
		<acme:submit name="save" code="newspaper.save" />
		<jstl:if test="${newspaper.id!=0}">
			<acme:delete clickCode="newspaper.confirm.delete" name="delete"
				code="newspaper.delete" />
		</jstl:if>
		<acme:cancel url="newspaper/user/list.do" code="newspaper.cancel" />

	</form:form>
</div>
