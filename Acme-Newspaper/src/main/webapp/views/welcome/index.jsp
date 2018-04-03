<%--
 * index.jsp
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
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="security"
	uri="http://www.springframework.org/security/tags"%>
<%@taglib prefix="display" uri="http://displaytag.sf.net"%>
<%@ taglib prefix="acme" tagdir="/WEB-INF/tags"%>

<spring:message var="lang" code="language" />

<div class="slider fullscreen">
	<ul class="slides">
		<jstl:forEach var="slide" items="${sliders}">
			<acme:showSlide title_en="${slide.title_en}"
				title_es="${slide.title_es}" text_en="${slide.text_en}"
				text_es="${slide.text_es}" pictureUrl="${slide.pictureUrl}"
				align="${slide.align}" lang="${lang}" />
		</jstl:forEach>
	</ul>
</div>

