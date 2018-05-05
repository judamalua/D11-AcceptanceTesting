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
<%@ taglib prefix="acme" tagdir="/WEB-INF/tags" %>
<%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>


<!-- Pagination -->
<acme:pagination requestURI = "tag/admin/list.do?page=" pageNum = "${pageNum}" page = "${page}"/>

<display:table name="tags" id="tag" requestURI="tag/admin/list.do?page=${page}"
	class="displaytag">

	<spring:message code="tag.name" var="name" />
	<display:column property="name" title="${name}"  />

	<spring:message code="tag.keywords" var="keywords" />
	<display:column property="keywords" title="${keywords}"  />

	<display:column>
		<a href="tag/admin/edit.do?tagId=${tag.id}">
			<button class="btn">
				<spring:message code="tag.edit" />
			</button>
		</a>
	</display:column>

</display:table>

<acme:button url="tag/admin/create.do" code="tag.create" />