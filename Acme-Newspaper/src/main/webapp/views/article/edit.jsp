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
<spring:message code="article.moment.placeholder"
	var="momentPlaceholder" />

<!-- Form -->
<p>
	<em><spring:message code="form.required.params" /></em>
</p>
<div class="row">
	<form:form action="article/user/edit.do" modelAttribute="article">

		<form:hidden path="id" />
		<form:hidden path="version" />

		<acme:textbox code="article.title" path="title" required="true" />
		<acme:textarea code="article.summary" path="summary" required="true" />

		<div class="cleared-div">
			<acme:checkbox code="article.finalMode" path="finalMode"
				id="finalMode" />
		</div>
		<acme:submit name="save" code="article.save" />
		<jstl:if test="${article.id!=0}">
			<acme:delete clickCode="article.confirm.delete" name="delete"
				code="article.delete" />
		</jstl:if>
		<acme:cancel url="article/user/list.do" code="article.cancel" />

	</form:form>
</div>
