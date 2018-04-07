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
<%@ taglib prefix="acme" tagdir="/WEB-INF/tags" %>

<p><em><spring:message code = "form.required.params"/></em></p>

<form:form id="form" action="${requestURI}" modelAttribute="followUp">
	<form:hidden path="id"/>
	<form:hidden path="version"/>
	

	
	<acme:textbox code="followUp.title" path="title" required="true"/>
	
	<acme:textbox code="followUp.summary" path="summary" required="true"/>
	
	
	
	<div class="form-group">
			<div class="row">
				<div class="input-field col s9">
					<form:textarea path="text" class="widgEditor"/>
					<form:errors path="text" cssClass="error" />
				</div>
			</div>
	</div>
	
	

	<acme:submit name="save" code="followUp.save"/>
	<acme:cancel url="followUp/user/list.do" code="followUp.cancel"/>
	
	
	
		
</form:form>

