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
<%@ taglib prefix="acme" tagdir="/WEB-INF/tags"%>
<script type="text/javascript" src="scripts/checkboxMessage.js"></script>

<form:form action="message/edit.do" modelAttribute="modelMessage">

	<form:hidden path="id" />
	<form:hidden path="version" />

	<spring:message code="message.priority.low" var="low" />
	<spring:message code="message.priority.neutral" var="neutral" />
	<spring:message code="message.priority.high" var="high" />

	<div class="form-group">
		<div class="row">

			<div class="input-field col s3">
				<form:label path="priority">
					<spring:message code="message.priority" />
				</form:label>
				<form:select path="priority">

					<form:option title="${low}" value="LOW" />
					<form:option title="${neutral}" value="NEUTRAL" />
					<form:option title="${high}" value="HIGH" />
				</form:select>
				<form:errors cssClass="error" path="priority" />
			</div>
		</div>
	</div>

	<acme:textbox code="message.subject" path="subject" />

	<acme:textarea code="message.body" path="body" />


	<div class="form-group">
		<div class="row">
			<acme:select items="${actors}" id="receiver" itemLabel="email"
				code="message.receiver" path="receiver" />
		</div>
	</div>

	<security:authorize access="hasRole('ADMIN')">
		<div class="cleared-div">
			<input type="checkbox" id="broadcast" name="broadcast" /> <label
				for="broadcast"> <spring:message code="message.broadcast" />
			</label>
		</div>
	</security:authorize>
	<br />

	<acme:submit name="save" code="message.save" />
	<acme:cancel url="/messageFolder/list.do" code="message.cancel" />

</form:form>
