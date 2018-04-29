<%--
 * action-2.jsp
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
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="acme" tagdir="/WEB-INF/tags"%>

<!-- Variable declaration -->
<spring:message code="master.page.moment.format" var="formatMoment" />
<spring:message code="newspaper.description" var="titleDescription" />
<spring:message code="newspaper.publicationDate" var="titlePublication" />

<spring:message code="article.title" var="titleArticle" />
<spring:message code="article.summary" var="titleSummaryArticle" />
<spring:message code="article.body" var="titleBodyArticle" />


<jsp:useBean id="now" class="java.util.Date" />
<fmt:formatDate var="currentDate" value="${now}"
	pattern="yyyy-MM-dd HH:mm" />
<spring:message var="format" code="master.page.moment.format.out" />
<fmt:formatDate var="formatMomentnewspaper"
	value="${newspaper.publicationDate}" pattern="${format}" />


<!-- Display -->

<jstl:if test="${advertisement != null}">
	<a href="${advertisement.additionalInfoLink}"> <img
		class="sponsorshipBannerUrl" src="${advertisement.bannerUrl}"
		alt="${advertisement.title}" />
	</a>
</jstl:if>

<jstl:if test="${newspaper.pictureUrl != \"\"}">
	<div class="parallax-container">
		<div class="parallax">
			<img src="${newspaper.pictureUrl}">
		</div>
	</div>
</jstl:if>
<h2>
	<jstl:out value="${newspaper.title}" />
</h2>
<br>
<h4>
	<spring:message code="newspaper.description" />
</h4>
<p>
	<jstl:out value="${newspaper.description}" />
</p>
<br />

<h4>
	<spring:message code="newspaper.publicationDate" />
</h4>
<p>
	<jstl:out value="${formatMomentnewspaper}" />
</p>

<!-- Button for joining the newspaper -->
<security:authorize access="hasRole('CUSTOMER')">
	<jstl:if test="${!subscriber  and !newspaper.publicNewspaper}">
		<a href="newspaper/customer/subscribe.do?newspaperId=${newspaper.id}">
			<button class="btn">
				<spring:message code="newspaper.subscribe" />
			</button>
		</a>
	</jstl:if>
	<br />
</security:authorize>


<!-- Displaying articles -->
<h4>
	<spring:message code="newspaper.articles.list" />
</h4>
<acme:pagination page="${page}" pageNum="${pageNum}"
	requestURI="newspaper/display.do?newspaperId=${newspaper.id}&pageArticle=" />

<display:table name="${articles}" id="articleList"
	requestURI="newspaper/display.do" pagesize="${pagesize}">

	<display:column title="${titleArticle}">
		<security:authorize access="hasRole('CUSTOMER')">
			<jstl:if
				test="${((ownArticle!=null and ownArticle[articleList_rowNum-1])  or (subscriber and !newspaper.publicNewspaper) or newspaper.publicNewspaper )}">
				<a href="article/display.do?articleId=${articleList.id}">${articleList.title}</a>
			</jstl:if>
			<jstl:if
				test="${(!subscriber) and !newspaper.publicNewspaper and !(ownArticle!=null and ownArticle[articleList_rowNum-1])}">
				<jstl:out value="${articleList.title}" />
			</jstl:if>
		</security:authorize>
		<security:authorize access="!hasRole('CUSTOMER')">
			<jstl:if test="${newspaper.publicNewspaper}">
				<a href="article/display.do?articleId=${articleList.id}">${articleList.title}</a>
			</jstl:if>
			<jstl:if test="${!newspaper.publicNewspaper}">
				<jstl:out value="${articleList.title}" />
			</jstl:if>
		</security:authorize>

	</display:column>

	<display:column title="${titleSummaryArticle}">

		<jstl:if test="${(fn:length(articleList.summary))<=40}">
			<jstl:out value="${articleList.summary}" />
		</jstl:if>
		<jstl:if test="${(fn:length(articleList.summary))>40}">
			<jstl:out value="${fn:substring(articleList.summary,0,40)}..." />
		</jstl:if>
	</display:column>

	<display:column>
		<security:authorize access="hasRole('ADMIN')">
			<acme:button
				url="article/admin/delete.do?articleId=${articleList.id}"
				code="article.delete" />
		</security:authorize>
	</display:column>

	<display:column>
		<security:authorize access="hasRole('USER')">
			<jstl:if
				test="${fn:length(ownArticle)>0 and ownArticle[articleList_rowNum-1] and !articleList.finalMode   and newspaper.publicationDate==null}">
				<acme:button url="article/user/edit.do?articleId=${articleList.id}"
					code="article.edit" />
			</jstl:if>
		</security:authorize>
	</display:column>

</display:table>
<security:authorize access="hasRole('USER')">
	<jstl:if test="${newspaper.publicationDate==null}">
		<acme:button url="article/user/create.do?newspaperId=${newspaper.id}"
			code="article.create" />
	</jstl:if>
</security:authorize>
<br />

