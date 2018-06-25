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
<%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>


<!-- Pagination -->
<acme:pagination requestURI="${requestUri}" pageNum="${pageNum}"
	page="${page}" />

<display:table name="lusits" id="lusit"
	requestURI="${requestUri}${page}" class="displaytag">

	<jstl:if test="${lusit.gauge==1}">
		<jstl:set var="cssClass" value="point1" />
	</jstl:if>
	<jstl:if test="${lusit.gauge==2}">
		<jstl:set var="cssClass" value="point2" />
	</jstl:if>
	<jstl:if test="${lusit.gauge==3}">
		<jstl:set var="cssClass" value="point3" />
	</jstl:if>

	<spring:message code="lusit.title" var="title" />
	<display:column property="title" title="${title}" class="${cssClass}" />

	<spring:message code="lusit.description" var="description" />
	<display:column property="description" title="${description}"
		class="${cssClass}" />

	<display:column class="${cssClass}" >
		<jstl:if test="${!lusit.finalMode}">
			<a href="lusit/admin/edit.do?lusitId=${lusit.id}">
				<button class="btn">
					<spring:message code="tag.edit" />
				</button>
			</a>
		</jstl:if>
	</display:column>

</display:table>

<acme:button url="lusit/admin/create.do" code="tag.create" />