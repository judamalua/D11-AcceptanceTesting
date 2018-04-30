<%--
 * header.jsp
 *
 * Copyright (C) 2017 Universidad de Sevilla
 * 
 * The use of this project is hereby constrained to the conditions of the 
 * TDG Licence, a copy of which you may download from 
 * http://www.tdg-seville.info/License.html
 --%>

<%@page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>

<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="security"
	uri="http://www.springframework.org/security/tags"%>

<!-- <div class = "crop">
	<a href="welcome/index.do"> <img class="banner img-responsive" src="https://i.ytimg.com/vi/BzX6jMalUck/maxresdefault.jpg"
		alt="Acme Co., Inc." />
	</a>
</div> -->

<nav>
	<div class="nav-wrapper">
		<a class="brand-logo" href="#">&#160;&#160;Acme&#160;<img
			width="24" src="images/people.png" />&#160;Template
		</a>


		<ul id="nav-mobile" class="right hide-on-med-and-down">
			<!-- id="jMenu" -->
			<!-- Do not forget the "fNiv" class for the first level links !! -->

			<security:authorize access="hasRole('USER')">

				<!-- Dropdown Structure -->
				<ul id="dropdownUserFunctions" class="dropdown-content">
					<li><a href="newspaper/user/list.do?published=true"><spring:message
								code="master.page.publishedNewspapers" /></a></li>
					<li class="divider"></li>
					<li><a href="newspaper/user/list.do?published=false"><spring:message
								code="master.page.notPublishedNewspapers" /></a></li>
					<li class="divider"></li>
					<li><a href="actor/user/list-followed.do"><spring:message
								code="master.page.list.followed" /></a></li>
					<li class="divider"></li>
					<li><a href="followUp/user/list-created.do"><spring:message
								code="master.page.followUp.list-created" /></a></li>
					<li class="divider"></li>
					<li><a href="actor/user/list-followers.do"><spring:message
								code="master.page.list.followers" /></a></li>
					<li class="divider"></li>
					<li><a href="chirp/user/stream.do"><spring:message
								code="master.page.chirp.stream" /></a></li>
					<li class="divider"></li>
					<li><a href="chirp/user/list.do"><spring:message
								code="master.page.chirp.yourlist" /></a></li>
					<li class="divider"></li>
					<li><a href="volume/user/list.do"><spring:message
								code="master.page.volume.user.list" /></a></li>
				</ul>

				<!-- Dropdown Trigger -->
				<li><a class="dropdown-button" href="#!"
					data-activates="dropdownUserFunctions"><spring:message
							code="master.page.user" /><i class="material-icons right">arrow_drop_down</i></a></li>

			</security:authorize>

			<security:authorize access="hasRole('ADMIN')">
				<!-- Dropdown Structure -->
				<ul id="dropdownAdminFunctions" class="dropdown-content">
					<li><a href="actor/admin/register.do"><spring:message
								code="master.page.createAdmin" /></a></li>
					<li class="divider"></li>
					<li><a href="dashboard/admin/list.do"><spring:message
								code="master.page.dashboardList" /></a></li>
					<li class="divider"></li>
					<li><a href="configuration/admin/list.do"><spring:message
								code="master.page.configuration" /></a></li>
					<li class="divider"></li>
					<li><a href="chirp/admin/list.do"><spring:message
								code="master.page.chirp.list.taboo" /></a></li>
					<li class="divider"></li>
					<li><a href="newspaper/admin/list.do"><spring:message
								code="master.page.newspaper.list.taboo" /></a></li>
					<li class="divider"></li>
					<li><a href="article/admin/list.do"><spring:message
								code="master.page.article.list.taboo" /></a></li>
					<li class="divider"></li>
					<li><a href="advertisement/admin/list.do"><spring:message
								code="master.page.advertisement.list.taboo" /></a></li>
				
				</ul>

				<!-- Dropdown Trigger -->
				<li><a class="dropdown-button" href="#!"
					data-activates="dropdownAdminFunctions"><spring:message
							code="master.page.admin" /><i class="material-icons right">arrow_drop_down</i></a></li>
			</security:authorize>

			<security:authorize access="hasRole('AGENT')">
				<!-- Dropdown Structure -->
				<ul id="dropdownAgentFunctions" class="dropdown-content">
					<li><a href="advertisement/agent/list.do"><spring:message
								code="master.page.listAdvertisment" /></a></li>
				</ul>

				<!-- Dropdown Trigger -->
				<li><a class="dropdown-button" href="#!"
					data-activates="dropdownAgentFunctions"><spring:message
							code="master.page.agent" /><i class="material-icons right">arrow_drop_down</i></a></li>
			</security:authorize>

			<security:authorize access="hasRole('CUSTOMER')">
				<!-- Dropdown Structure -->
				<ul id="dropdownCustomerFunctions" class="dropdown-content">
					<li><a class="fNiv" href="newspaper/customer/list.do"> <spring:message
								code="master.page.subscribedList" /></a></li>
					<li class="divider"></li>
					<li><a class="fNiv" href="volume/customer/list.do"> <spring:message
								code="master.page.volume.customer.list" /></a></li>
					<li class="divider"></li>
				</ul>

				<!-- Dropdown Trigger -->
				<li><a class="dropdown-button" href="#!"
					data-activates="dropdownCustomerFunctions"><spring:message
							code="master.page.customer" /><i class="material-icons right">arrow_drop_down</i></a></li>
			</security:authorize>

			<security:authorize access="isAnonymous()">
				<li><a class="fNiv" href="security/login.do"> <spring:message
							code="master.page.login" /></a></li>
				<li><a class="fNiv" href="actor/register.do"> <spring:message
							code="master.page.registerUser" /></a></li>
				<li><a class="fNiv" href="actor/customer/register.do"> <spring:message
							code="master.page.registerCustomer" /></a></li>
				<li><a class="fNiv" href="actor/agent/register.do"> <spring:message
							code="master.page.registerAgent" /></a></li>
				<li><a class="fNiv" href="user/list.do"> <spring:message
							code="master.page.userList" />
				</a></li>
				<li><a href="newspaper/list.do"><spring:message
							code="master.page.newspaperList" /></a></li>
				<li><a href="volume/list.do"><spring:message
							code="master.page.volumeList" /></a></li>
				<li><a href="article/search.do"><spring:message
							code="master.page.articleSearch" /></a></li>

			</security:authorize>

			<security:authorize access="isAuthenticated()">
				<li><a class="fNiv" href="user/list.do"> <spring:message
							code="master.page.userList" /></a></li>

				<li><a class="fNiv" href="newspaper/list.do"><spring:message
							code="master.page.newspaperList" /></a></li>
				</a>
				</li>
				<li><a href="volume/list.do"><spring:message
							code="master.page.volumeList" /></a></li>
				<li><a href="article/search.do"><spring:message
							code="master.page.articleSearch" /></a></li>

				<li><a href="messageFolder/list.do"><spring:message
							code="master.page.messageFolderList" /></a></li>

				<security:authorize access="hasRole('ADMIN')">
					<!-- Dropdown Structure -->
					<ul id="dropdownAdminProfile" class="dropdown-content">
						<li><a href="actor/admin/edit.do"><spring:message
									code="master.page.actorEdit" /></a></li>
						<li class="divider"></li>
						<li><a href="actor/display.do"><spring:message
									code="master.page.actorProfile" /></a></li>
						<li class="divider"></li>
						<li><a href="j_spring_security_logout"><spring:message
									code="master.page.logout" /> </a></li>
					</ul>

					<!-- Dropdown Trigger -->
					<li><a class="dropdown-button" href="#!"
						data-activates="dropdownAdminProfile"><security:authentication
								property="principal.username" /><i class="material-icons right">arrow_drop_down</i></a></li>

				</security:authorize>

				<security:authorize access="hasRole('USER')">

					<!-- Dropdown Structure -->
					<ul id="dropdownUserProfile" class="dropdown-content">
						<li><a href="actor/user/edit.do"><spring:message
									code="master.page.actorEdit" /></a></li>
						<li class="divider"></li>
						<li><a href="user/display.do"><spring:message
									code="master.page.actorProfile" /></a></li>
						<li class="divider"></li>
						<li><a href="j_spring_security_logout"><spring:message
									code="master.page.logout" /> </a></li>
					</ul>

					<!-- Dropdown Trigger -->
					<li><a class="dropdown-button" href="#!"
						data-activates="dropdownUserProfile"><security:authentication
								property="principal.username" /><i class="material-icons right">arrow_drop_down</i></a></li>

				</security:authorize>

				<security:authorize access="hasRole('CUSTOMER')">

					<!-- Dropdown Structure -->
					<ul id="dropdownCustomerProfile" class="dropdown-content">
						<li><a href="actor/customer/edit.do"><spring:message
									code="master.page.actorEdit" /></a></li>
						<li class="divider"></li>
						<li><a href="actor/display.do"><spring:message
									code="master.page.actorProfile" /></a></li>
						<li class="divider"></li>
						<li><a href="j_spring_security_logout"><spring:message
									code="master.page.logout" /> </a></li>
					</ul>

					<!-- Dropdown Trigger -->
					<li><a class="dropdown-button" href="#!"
						data-activates="dropdownCustomerProfile"><security:authentication
								property="principal.username" /><i class="material-icons right">arrow_drop_down</i></a></li>

				</security:authorize>

				<security:authorize access="hasRole('AGENT')">

					<!-- Dropdown Structure -->
					<ul id="dropdownCustomerProfile" class="dropdown-content">
						<li><a href="actor/agent/edit.do"><spring:message
									code="master.page.actorEdit" /></a></li>
						<li class="divider"></li>
						<li><a href="actor/display.do"><spring:message
									code="master.page.actorProfile" /></a></li>
						<li class="divider"></li>
						<li><a href="j_spring_security_logout"><spring:message
									code="master.page.logout" /> </a></li>
					</ul>

					<!-- Dropdown Trigger -->
					<li><a class="dropdown-button" href="#!"
						data-activates="dropdownCustomerProfile"><security:authentication
								property="principal.username" /><i class="material-icons right">arrow_drop_down</i></a></li>

				</security:authorize>

			</security:authorize>
		</ul>
	</div>
</nav>

<p></p>


