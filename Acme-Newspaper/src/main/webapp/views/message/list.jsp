<%@page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="security"	uri="http://www.springframework.org/security/tags"%>
<%@taglib prefix="display" uri="http://displaytag.sf.net"%>
<%@ taglib prefix="acme" tagdir="/WEB-INF/tags"%>


<jstl:if test="${messageFolder.messageFolderFather != null}">
<h1><a href="messageFolder/list.do?messageFolderId=${messageFolder.messageFolderFather.id}" ><jstl:out value="${messageFolder.name}"/></a></h1>
</jstl:if>

<jstl:if test="${messageFolder.messageFolderFather == null}">
<h1><a href="messageFolder/list.do" ><jstl:out value="${messageFolder.name}"/></a></h1>
</jstl:if>

<jstl:set var = "messageFolderName" value = "${messageFolder.name}"/>

<jstl:if test="${not empty messages}">
<spring:message code="master.page.moment.format" var="formatDate"/>

<acme:pagination page="${page}" pageNum="${pageNum}" requestURI="message/list.do?messageFolderId=${messageFolder.id}&page="/>

<display:table 
	name="messages"
	id="message"
	requestURI="message/list.do">
	
	<spring:message code="message.priority" var="priority"/>
	<display:column property="priority" title="${priority}"/>

	<spring:message code="message.subject" var="subject"/>
	<display:column property="subject" title="${subject}" />
	

	<spring:message code="message.body" var="body"/>
	<display:column property="body" title="${body}" />

	<spring:message code="message.receptionDate" var="receptionDate" />
	<display:column property="receptionDate" title="${receptionDate}"  format="${formatDate}"/>
	
	<spring:message code="message.sender" var="sender"/>
	<display:column property="sender.email" title="${sender}"/>
	
	<jstl:if test="${messageFolder.name == \"out box\" && messageFolder.isDefault == true}">
	
		<spring:message code="message.receiver" var="receiver"/>
		<display:column property="receiver.email" title="${receiver}"/>
	
	</jstl:if>
	
	<display:column >
		<a href="message/delete.do?messageId=${message.id}&messageFolderId=${messageFolder.id}">
			<button class = "btn">
				<spring:message code="message.delete"/>
			</button>
		</a>
		
		<a href="message/move.do?messageId=${message.id}">
			<button class = "btn">
				<spring:message code="message.move"/>
			</button>
		</a>
	</display:column>
	
</display:table>
</jstl:if>

<a href="message/create.do">
	<button class = "btn">
		<spring:message code="message.create"/>
	</button>
</a>