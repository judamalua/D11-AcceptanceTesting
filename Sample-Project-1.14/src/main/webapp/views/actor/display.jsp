
<%@page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="security"
	uri="http://www.springframework.org/security/tags"%>
<%@taglib prefix="display" uri="http://displaytag.sf.net"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="acme" tagdir="/WEB-INF/tags"%>

<!-- Variables -->
<spring:message code="master.page.moment.format" var="formatDate" />
<spring:message var="format" code="master.page.moment.format.out" />
<spring:message var="birthDateFormat" code="master.page.birthDate" />

<!-- Personal data -->
<br />
<h4>
	<jstl:out value="${actor.name}" />
	<jstl:out value="${actor.surname}" />
</h4>
<br />

<strong><spring:message code="actor.postalAddress" />:</strong>
<jstl:out value="${actor.postalAddress}" />
<br />
<strong><spring:message code="actor.phoneNumber" />:</strong>
<jstl:out value="${actor.phoneNumber}" />
<br />
<strong><spring:message code="actor.email" />:</strong>
<jstl:out value="${actor.email}" />
<br />
<strong><spring:message code="actor.birthDate" />:</strong>
<fmt:formatDate value="${actor.birthDate}" pattern="${birthDateFormat}" />
<br />
<br />
