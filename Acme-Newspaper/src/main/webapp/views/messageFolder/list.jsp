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
<%@taglib prefix="acme" tagdir="/WEB-INF/tags"%>

<jstl:if test="${father!=null}">
	<jstl:if test="${ father.messageFolderFather==null}">
		<h2>
			<a href="messageFolder/list.do"> <jstl:out value="${father.name}" />
			</a>
		</h2>
	</jstl:if>

	<jstl:if test="${ father.messageFolderFather!=null}">
		<h2>
			<a
				href="messageFolder/list.do?messageFolderId=${father.messageFolderFather.id}">
				<jstl:out value="${father.name}" />
			</a>
		</h2>
	</jstl:if>
</jstl:if>
	<jstl:if test="${father==null}">
			<jstl:set value="messageFolder/list.do?page=" var="requestURI"/>
	</jstl:if>
		<jstl:if test="${ father!=null}">
			<jstl:set value="messageFolder/list.do?messageFolderId=${father.id}&page=" var="requestURI"/>
	</jstl:if>
<acme:pagination page="${page}" pageNum="${pageNum}" requestURI="${requestURI}"/>

<jstl:if test="${not empty messageFolders}">
	<display:table name="messageFolders" id="messageFolderList"
		requestURI="messageFolder/list.do" >

		<spring:message code="messageFolder.name" var="name" />
		<display:column title="${name}" property="name"/>

		<spring:message code="messageFolder.messageFolderChildren"
			var="messageFolderChildren" />
		<display:column title="${messageFolderChildren}">
			<a href="messageFolder/list.do?messageFolderId=${messageFolderList.id}">
				<button class = "btn">
					<spring:message code="messageFolder.messageFolderChildrenLink" />
				</button>
			</a>
		</display:column>

		<spring:message code="messageFolder.messages" var="messages" />
		<display:column title="${messages}">
			<a href="message/list.do?messageFolderId=${messageFolderList.id}">
				<button class = "btn">
					<spring:message code="messageFolder.messagesLink" />
				</button>
			</a>
		</display:column>
		<display:column>
<%-- 			<jstl:if test="${!messageFolderList.isDefault}"> --%>
				<a href="messageFolder/edit.do?messageFolderId=${messageFolderList.id}">
					<button class = "btn">
						<spring:message code="messageFolder.edit" />
					</button>
				</a>
<%-- 			</jstl:if> --%>
		</display:column>


	</display:table>
</jstl:if>

<a href="messageFolder/create.do">
	<button class = "btn">
		<spring:message code="messageFolder.create" />
	</button>
</a>

<a href="message/create.do">
	<button class = "btn">
		<spring:message code="messageFolder.message.create" />
	</button>
</a>