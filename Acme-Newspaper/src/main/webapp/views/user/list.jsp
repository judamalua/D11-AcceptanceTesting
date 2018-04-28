
<%@page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="security"
	uri="http://www.springframework.org/security/tags"%>
<%@taglib prefix="display" uri="http://displaytag.sf.net"%>
<%@ taglib prefix="acme" tagdir="/WEB-INF/tags"%>

<spring:message code="master.page.moment.format" var="formatDate" />
<spring:message code="master.page.birthDate.format" var="formatBirthDate" />

<!-- Pagination -->

<acme:pagination requestURI = "${requestURI}?page=" pageNum = "${pageNum}" page = "${page}"/>

<display:table name="users" id="userList" requestURI="${requestURI}"
	class="displaytag">

	<spring:message code="user.name" var="name" />
	<display:column property="name" title="${name}"  />

	<spring:message code="user.surname" var="surname" />
	<display:column property="surname" title="${surname}"  />

	<spring:message code="user.postalAddress" var="postalAddress" />
	<display:column property="postalAddress" title="${postalAddress}"/>

	<spring:message code="user.phoneNumber" var="phoneNumber" />
	<display:column property="phoneNumber" title="${phoneNumber}"/>

	<spring:message code="user.email" var="email" />
	<display:column property="email" title="${email}"  />

	<spring:message code="user.birthDate" var="birthDate" />
	<display:column property="birthDate" title="${birthDate}" format="${formatBirthDate}" />
		
	<display:column>
		<security:authorize access="hasRole('USER')">
		<jstl:if test="${!principal.users.contains(userList) && principal.id != userList.id && followersView}">
			<acme:button
				url="actor/user/follow.do?userId=${userList.id}&followersView=true"
				code="user.follow" />
		</jstl:if>
		<jstl:if test="${!principal.users.contains(user) && principal.id != userList.id && !followedView && !followersView}">
			<acme:button
				url="actor/user/follow.do?userId=${userList.id}"
				code="user.follow" />
		</jstl:if>
		</security:authorize>
	</display:column>
	
	<display:column>
		<security:authorize access="hasRole('USER')">
		<jstl:if test="${principal.users.contains(userList) && principal.id != userList.id && followedView}">
			<acme:button
				url="actor/user/follow.do?userId=${userList.id}&followedView=true"
				code="user.unfollow" />
		</jstl:if>
		<jstl:if test="${principal.users.contains(userList) && principal.id != userList.id && followersView}">
			<acme:button
				url="actor/user/follow.do?userId=${userList.id}&followersView=true"
				code="user.unfollow" />
		</jstl:if>
		<jstl:if test="${principal.users.contains(userList) && principal.id != userList.id && !followedView && !followersView}">
			<acme:button
				url="actor/user/follow.do?userId=${userList.id}"
				code="user.unfollow" />
		</jstl:if>
		</security:authorize>
	</display:column>

	<display:column>
		<a href="user/display.do?actorId=${userList.id}">
			<button class="btn">
				<spring:message code="user.display" />
			</button>
		</a>
	</display:column>

</display:table>
