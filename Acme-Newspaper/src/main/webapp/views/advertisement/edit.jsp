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

<spring:message code="request.creditcard.expirationYear.placeholder"
	var="expirationYearPlaceholder" />
<spring:message code="request.creditcard.info" var="creditCardInfo" />
<!-- Form -->
<p>
	<em><spring:message code="form.required.params" /></em>
</p>

<div class="row">

	<form:form id="form" action="advertisement/agent/edit.do"
		modelAttribute="advertisement">
		<form:hidden path="id" />
		<form:hidden path="version" />

		<acme:textbox code="advertisement.title" path="title" required="true" />
		<acme:textbox code="advertisement.bannerURL" path="bannerURL"
			required="true" />
		<acme:textbox code="advertisement.additionalInfoLink"
			path="additionalInfoLink" required="true" />

		<acme:select items="${tags}" itemLabel="name"
			code="advertisement.tags" path="tags" multiple="true" />
		<br />
		<jstl:if test="${advertisement.id == 0}">
			<div class="cleared-div">
				<h4>
					<jstl:out value="${creditCardInfo}" />
				</h4>
			</div>
			<div class="cardForm">
				<acme:textbox code="advertisement.holderName" path="holderName"
					required="true" />

				<acme:textbox code="advertisement.brandName" path="brandName"
					required="true" />

				<acme:textbox code="advertisement.number" path="number"
					required="true" />

				<acme:textbox code="advertisement.expirationMonth"
					path="expirationMonth" required="true" placeholder="MM" />

				<acme:textbox code="advertisement.expirationYear"
					path="expirationYear" required="true"
					placeholder="${expirationYearPlaceholder}" />

				<acme:textbox code="advertisement.cvv" path="cvv" required="true" />

			</div>
		</jstl:if>
		<div class="cleared-div">
			<button type="submit" name="save" class="btn">
				<spring:message code="advertisement.save" />
			</button>
			<jstl:if test="${advertisement.id != 0}">
				<acme:delete clickCode="advertisement.confirm.delete" name="delete"
					code="advertisement.delete" />
			</jstl:if>
			<acme:cancel url="advertisement/agent/list.do"
				code="advertisement.cancel" />
		</div>
	</form:form>
</div>
