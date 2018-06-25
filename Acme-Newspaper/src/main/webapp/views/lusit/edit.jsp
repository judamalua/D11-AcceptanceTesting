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
<div class="form-group">
	<div class="row">
		<form:form action="lusit/admin/edit.do" modelAttribute="lusit">

			<form:hidden path="id" />
			<form:hidden path="version" />

			<p>
				<em><spring:message code="configuration.all.fields.required" /></em>
			</p>

			<acme:textbox code="lusit.title" path="title" required="true" />
			<acme:textbox code="lusit.description" path="description"
				required="true" />
			<div class="cleared-div">
				<div class="cleared-div">
					<form:label path="gauge">
						<spring:message code="lusit.gauge" />*
						</form:label>
				</div>
				<div class="input-field col s3">

					<form:select path="gauge">
						<form:option value="1" />
						<form:option value="2" />
						<form:option value="3" />
					</form:select>
					<form:errors cssClass="error" path="gauge" />
				</div>
			</div>
			
			<acme:htmlSelect items="${newspapers}" name="newspaper" code="lusit.newspaper"/>
			
			<div class="cleared-div">
				<acme:textbox code="lusit.publicationDate" path="publicationDate"
					placeholder="dd/MM/yyyy HH:mm" />
				<acme:checkbox code="lusit.finalMode" path="finalMode"
					id="finalMode" />
			</div>
			<acme:submit name="save" code="lusit.save" />

			<jstl:if test="${lusit.id!=0}">
				<acme:delete clickCode="lusit.delete.confirm" name="delete"
					code="tag.delete" />
			</jstl:if>

			<acme:cancel url="lusit/admin/list.do?finalMode=false"
				code="lusit.cancel" />
		</form:form>
	</div>
</div>