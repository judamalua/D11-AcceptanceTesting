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

<form action="message/move.do" method="post">

	<input type="hidden" name="messageId" value="${messageId}" /> 
	<select name="selectedMessageFolder">
		<jstl:forEach var="messageFolder" items="${messageFolders}">
			<option value="${messageFolder.id}">
				<jstl:out value="${messageFolder.name}" />
			</option>
		</jstl:forEach>
	</select> 
	<input type="submit" name="save"
		class = "btn"
		value="<spring:message code="message.save" />"> 
	<input
		type="button" name="cancel"
		class = "btn"
		value="<spring:message code="message.cancel" />"
		onclick="javascript: relativeRedir('messageFolder/list.do')">

</form>
