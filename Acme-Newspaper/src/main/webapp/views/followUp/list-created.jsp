<%--
 * list.jsp
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
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="security"
	uri="http://www.springframework.org/security/tags"%>
<%@taglib prefix="display" uri="http://displaytag.sf.net"%>
<%@ taglib prefix="acme" tagdir="/WEB-INF/tags"%>


<!-- Pagination -->
<acme:pagination requestURI="${requestUri}page=" pageNum="${pageNum}"
	page="${page}" />

<!-- Table -->

<display:table name="${ownFollowUps}" id="followUpList"
	requestURI="${requestUri}">

	<display:column property="title" title="${titleName}" />
	<display:column property="publicationDate" title="${titlePublication}"
		format="${formatMoment}" />
	<display:column>
		<acme:button url="followUp/display.do?followUpId=${followUpList.id}"
			code="followUp.details" />
	</display:column>


	<display:column>
				<acme:button
					url="followUp/user/edit.do?followUpId=${followUpList.id}"
					code="followUp.edit" />
	</display:column>

</display:table>


