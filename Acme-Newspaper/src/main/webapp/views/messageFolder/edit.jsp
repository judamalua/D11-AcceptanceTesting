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

<form:form action="messageFolder/edit.do" modelAttribute="messageFolder">

	<form:hidden path="id" />
	<form:hidden path="version" />

	<acme:textbox code="messageFolder.name" path="name" />

	<acme:select items="${messageFolders}" itemLabel="name"
		code="messageFolder.messageFolderFather" path="messageFolderFather" />

	<acme:submit name="save" code="messageFolder.save" />

	<jstl:if test="${messageFolder.id!=0}">
		<acme:delete clickCode="messageFolder.confirm.delete" name="delete"
			code="messageFolder.delete" />
	</jstl:if>
	<acme:cancel url="messageFolder/list.do" code="messageFolder.cancel" />

</form:form>
