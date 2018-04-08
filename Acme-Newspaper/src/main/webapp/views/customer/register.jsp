<%@page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="security"	uri="http://www.springframework.org/security/tags"%>
<%@taglib prefix="display" uri="http://displaytag.sf.net"%>
<%@ taglib prefix="acme" tagdir="/WEB-INF/tags" %>

<script type="text/javascript" src="scripts/checkboxTermsAndConditions.js"></script>

<div class="row">
<p><em><spring:message code = "form.required.params"/></em></p>

<form:form id = "form" action="actor/customer/register.do" modelAttribute ="customer" class="col s12">

	
	<form:hidden path="id"/>
	<form:hidden path="version"/>
	
	
	<acme:textbox code="customer.name" path="name" required="true"/>
	
	<acme:textbox code="customer.surname" path="surname" required="true"/>
	
	<acme:textbox code="customer.phoneNumber" path="phoneNumber"/>
	
	<acme:textbox code="customer.postalAddress" path="postalAddress"/>

	<acme:textbox code="customer.email" path="email" required="true"/>
	
	<acme:textbox code="customer.birthDate" path="birthDate" placeholder="dd/MM/yyyy" required="true"/>
	
	<acme:textbox code="customer.username" path="userAccount.username" required="true"/>

	<acme:password code="customer.password" path="userAccount.password" required="true"/>
	
	<acme:confirmPassword name="confirmPassword" code="customer.confirm.password" required = "true"/>
	
	<p>
		<input type="checkbox" name="check" id="check">
		<label for="check"><spring:message code="register.login.terms&conditions1"/><a href = "law/termsAndConditions.do" target="_blank"><spring:message code="register.login.terms&conditions2"/></a></label>
	</p>
	

	<div>
	<acme:submit name="save" code="customer.save"/>
	<acme:cancel url="/" code="customer.cancel" />
	</div>
	

</form:form>
</div>
