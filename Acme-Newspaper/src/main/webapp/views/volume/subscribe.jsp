<%--
 * edit.jsp
 *
 * Copyright (C) 2017 Universidad de Sevilla
 * 
 * The use of this project is hereby constrained to the conditions of the 
 * TDG Licence, a copy of which you may download from 
 * http://www.tdg-seville.info/License.html
 --%>

<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="security"
	uri="http://www.springframework.org/security/tags"%>
<%@taglib prefix="display" uri="http://displaytag.sf.net"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="acme" tagdir="/WEB-INF/tags"%>

<spring:message code="request.creditcard.expirationYear.placeholder" var="expirationYearPlaceholder" />
<spring:message code="request.creditcard.info" var="creditCardInfo" />
<spring:message code="request.select.error" var="selectError" />

<p>
	<em><spring:message code="form.required.params" /></em>
</p>

<div class="row">

	<form:form id="form" action="volume/customer/subscribe.do"
		modelAttribute="creditCard">
		<input type="hidden" id="volumeId" name="volumeId" value="${volume.id}"/>
		
		<div class="cleared-div">
			<h4>
				<jstl:out value="${creditCardInfo}" />
			</h4>
		</div>

		<div class="cookieCard"></div>
		<p class="creditCardCookieTokenNew" hidden="true"></p>
		<div class="cardForm">
		
			<form:hidden path="id" />
			<form:hidden path="version" />
			<form:hidden path="customer" />
			<form:hidden path="cookieToken"
				class="creditCardCookieToken" />
			<form:hidden path="volumes" />

			<acme:textbox code="request.creditcard.holderName"
				path="holderName" required="true" />

			<acme:textbox code="request.creditcard.brandName"
				path="brandName" required="true" />

			<acme:textbox code="request.creditcard.number"
				path="number" required="true" />

			<acme:textbox code="request.creditcard.expirationMonth"
				path="expirationMonth" required="true" placeholder="MM" />

			<acme:textbox code="request.creditcard.expirationYear"
				path="expirationYear" required="true"
				placeholder="${expirationYearPlaceholder}" />

			<acme:textbox code="request.creditcard.cvv" path="cvv"
				required="true" />

		</div>

	
			<button type="submit" name="save" class="btn">
				<spring:message code="request.save" />
			</button>

		<acme:cancel url="volume/list.do"
			code="request.cancel" />
	</form:form>

</div>
<script src="scripts/creditCardAjax.js"></script>
<script type="text/javascript">
	window.onload = function() {
		checkCreditCard();
		initialize();
	};
</script>
