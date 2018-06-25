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
	<input type="hidden" name="newspaperId" id="newspaperId" value="${newspaperId}"/>
	

	<p>
		<em><spring:message code="configuration.all.fields.required" /></em>
	</p>

	<acme:textbox code="review.title" path="title" required="true" />
	<acme:textbox code="review.description" path="description" required="true" />
	
	
	<form:label path="moment">
		<spring:message code="review.moment"/>
	</form:label>
	<form:input path="moment" placeholder="dd/MM/yyyy HH:mm"/>
	<form:errors cssClass="error" path="moment"/>
	<br/>
	
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
	
	

	
	<input 
		type="submit"
		name="saveAsDraf"
		class = "btn"
		value="<spring:message code="review.saveAsDraf" />"/>
	<acme:submit name="saveAsFinal" code="review.saveAsFinal" />

	<jstl:if test="${review.id!=0}">
		<a href="review/admin/delete.do?reviewId=${review.id}">
			<button class="btn">
				<spring:message code="review.delete" />
			</button>
		</a>
	</jstl:if>

	<acme:cancel url="configuration/admin/list.do"
		code="configuration.cancel" />


</form:form>
