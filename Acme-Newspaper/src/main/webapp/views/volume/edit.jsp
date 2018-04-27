<%--
 * edit.jsp
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

<!-- Form -->
<p>
	<em><spring:message code="form.required.params" /></em>
</p>

<div class="row">
	<form:form action="volume/user/edit.do" modelAttribute="volume">

		<form:hidden path="id" />
		<form:hidden path="version" />

		<acme:textbox code="volume.title" path="title" required="true" />
		<acme:textarea code="volume.description" path="description"
			required="true" />
		<acme:textbox code="volume.year" path="year" required="true" />
		
		<acme:select code="volume.newspapers" path="newspapers"
		items="${elegibleNewspapers}" itemLabel="title" multiple="true" />
		
		<br/>
		
		<acme:submit name="save" code="volume.save" />
		<jstl:if test="${volume.id!=0}">
			<acme:delete clickCode="volume.confirm.delete" name="delete"
				code="volume.delete" />
		</jstl:if>
		<acme:cancel url="volume/list.do" code="volume.cancel" />

	</form:form>
</div>