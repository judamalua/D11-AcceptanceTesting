<%@page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="security"	uri="http://www.springframework.org/security/tags"%>
<%@taglib prefix="display" uri="http://displaytag.sf.net"%>
<%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>


<ul class="collapsible popout" data-collapsible="accordion">


<li class = "dashboard-expander">
<div class="collapsible-header"><spring:message code="dashboard.newspaper.user"/></div>

<div class="collapsible-body"><span>
<p class = "element"><b><spring:message code="dashboard.average"/>:</b> <jstl:out value="${newspapersInfoFromUsersAverage == \"null\" ? 0 : newspapersInfoFromUsersAverage}"></jstl:out></p>

<p class = "element"><b><spring:message code="dashboard.standardDeviation"/>:</b> <jstl:out value="${newspapersInfoFromUsersDeviation == \"null\" ? 0 : newspapersInfoFromUsersDeviation}"></jstl:out></p>
</span></div>
</li>


<li class = "dashboard-expander">
<div class="collapsible-header"><spring:message code="dashboard.article.user"/></div>

<div class="collapsible-body"><span>
<p class = "element"><b><spring:message code="dashboard.average"/>:</b> <jstl:out value="${articlesInfoFromUsersAverage == \"null\" ? 0 : articlesInfoFromUsersAverage}"></jstl:out></p>

<p class = "element"><b><spring:message code="dashboard.standardDeviation"/>:</b> <jstl:out value="${articlesInfoFromUsersDeviation == \"null\" ? 0 : articlesInfoFromUsersDeviation}"></jstl:out></p>
</span></div>
</li>


<li class = "dashboard-expander">
<div class="collapsible-header"><spring:message code="dashboard.article.newspaper"/></div>

<div class="collapsible-body"><span>
<p class = "element"><b><spring:message code="dashboard.average"/>:</b> <jstl:out value="${articlesInfoFromNewspapersAverage == \"null\" ? 0 : articlesInfoFromNewspapersAverage}"></jstl:out></p>

<p class = "element"><b><spring:message code="dashboard.standardDeviation"/>:</b> <jstl:out value="${articlesInfoFromNewspapersDeviation == \"null\" ? 0 : articlesInfoFromNewspapersDeviation}"></jstl:out></p>
</span></div>
</li>

<li class = "dashboard-expander">
<div class="collapsible-header"><spring:message code="dashboard.newspapers.tenPercentMoreArticles"/></div>

<div class="collapsible-body"><span>	
	<display:table id = "newspaper" name = "newspaperWith10PercentMoreArticlesThanAverage" requestURI="dashboard/admin/list.do" pagesize="${pagesize}">
		<spring:message var = "titleNewspaper" code = "dashboard.newspaper.title"/>
		<display:column title = "${titleNewspaper}">${newspaper.title}</display:column>
		<spring:message var = "titleNumArticles" code = "dashboard.newspaper.numArticles"/>
		<display:column title = "${titleNumArticles}">${fn:length(newspaper.articles)}</display:column>
	
	</display:table>
</span></div>
</li>


<li class = "dashboard-expander">
<div class="collapsible-header"><spring:message code="dashboard.newspapers.tenPercentLessArticles"/></div>

<div class="collapsible-body"><span>	
	<display:table id = "newspaper" name = "newspaperWith10PercentLessArticlesThanAverage" requestURI="dashboard/admin/list.do" pagesize="${pagesize}">
		<spring:message var = "titleNewspaper" code = "dashboard.newspaper.title"/>
		<display:column title = "${titleNewspaper}">${newspaper.title}</display:column>
		<spring:message var = "titleNumArticles" code = "dashboard.newspaper.numArticles"/>
		<display:column title = "${titleNumArticles}">${fn:length(newspaper.articles)}</display:column>
	
	</display:table>
</span></div>
</li>

<li class = "dashboard-expander">
<div class="collapsible-header"><spring:message code="dashboard.users.newspaper.create.ratio"/></div>

<div class="collapsible-body"><span>
<p class = "element"><b><spring:message code="dashboard.ratio"/>:</b> <jstl:out value="${ratioCreatedNewspapers == \"null\" ? 0 : ratioCreatedNewspapers}"></jstl:out></p>
<div class = "ratio element">
<div class="progress progress-striped
     active" aria-valuemin="0">
  <div class="bar"
       style="width: ${ratioCreatedNewspapers*100}%;"><jstl:out value="${ratioCreatedNewspapers*100}%" /></div>
</div>
</div>
</span></div>
</li>

<li class = "dashboard-expander">
<div class="collapsible-header"><spring:message code="dashboard.users.article.create.ratio"/></div>

<div class="collapsible-body"><span>
<p class = "element"><b><spring:message code="dashboard.ratio"/>:</b> <jstl:out value="${ratioCreatedArticles == \"null\" ? 0 : ratioCreatedArticles}"></jstl:out></p>
<div class = "ratio element">
<div class="progress progress-striped
     active" aria-valuemin="0">
  <div class="bar"
       style="width: ${ratioCreatedArticles*100}%;"><jstl:out value="${ratioCreatedArticles*100}%" /></div>
</div>
</div>
</span></div>
</li>

<li class = "dashboard-expander">
<div class="collapsible-header"><spring:message code="dashboard.article.followUp"/></div>

<div class="collapsible-body"><span>
<p class = "element"><b><spring:message code="dashboard.average"/>:</b> <jstl:out value="${averageFollowUpsPerArticle == \"null\" ? 0 : averageFollowUpsPerArticle}"></jstl:out></p>

</span></div>
</li>


<li class = "dashboard-expander">
<div class="collapsible-header"><spring:message code="dashboard.article.followUp.oneWeek"/></div>

<div class="collapsible-body"><span>
<p class = "element"><b><spring:message code="dashboard.average"/>:</b> <jstl:out value="${averageFollowUpPerArticleOneWeek == \"null\" ? 0 : averageFollowUpPerArticleOneWeek}"></jstl:out></p>

</span></div>
</li>



<li class = "dashboard-expander">
<div class="collapsible-header"><spring:message code="dashboard.article.followUp.twoWeek"/></div>

<div class="collapsible-body"><span>
<p class = "element"><b><spring:message code="dashboard.average"/>:</b> <jstl:out value="${averageFollowUpPerArticleTwoWeek == \"null\" ? 0 : averageFollowUpPerArticleTwoWeek}"></jstl:out></p>

</span></div>
</li>





<li class = "dashboard-expander">
<div class="collapsible-header"><spring:message code="dashboard.chirp.user"/></div>

<div class="collapsible-body"><span>
<p class = "element"><b><spring:message code="dashboard.average"/>:</b> <jstl:out value="${chirpsInfoFromUsersAverage == \"null\" ? 0 : chirpsInfoFromUsersAverage}"></jstl:out></p>

<p class = "element"><b><spring:message code="dashboard.standardDeviation"/>:</b> <jstl:out value="${chirpsInfoFromUsersDeviation == \"null\" ? 0 : chirpsInfoFromUsersDeviation}"></jstl:out></p>
</span></div>
</li>


<li class = "dashboard-expander">
<div class="collapsible-header"><spring:message code="dashboard.chirp.user.ratio"/></div>

<div class="collapsible-body"><span>
<p class = "element"><b><spring:message code="dashboard.ratio"/>:</b> <jstl:out value="${ratioUsersPostedAbove75PercentAverageChirpsPerUser == \"null\" ? 0 : ratioUsersPostedAbove75PercentAverageChirpsPerUser}"></jstl:out></p>
<div class = "ratio element">
<div class="progress progress-striped
     active" aria-valuemin="0">
  <div class="bar"
       style="width: ${ratioUsersPostedAbove75PercentAverageChirpsPerUser*100}%;"><jstl:out value="${ratioUsersPostedAbove75PercentAverageChirpsPerUser*100}%" /></div>
</div>
</div>
</span></div>
</li>


<li class = "dashboard-expander">
<div class="collapsible-header"><spring:message code="dashboard.newspapers.public.ratio"/></div>

<div class="collapsible-body"><span>
<p class = "element"><b><spring:message code="dashboard.ratio"/>:</b> <jstl:out value="${ratioPublicNewspapers == \"null\" ? 0 : ratioPublicNewspapers}"></jstl:out></p>
<div class = "ratio element">
<div class="progress progress-striped
     active" aria-valuemin="0">
  <div class="bar"
       style="width: ${ratioPublicNewspapers*100}%;"><jstl:out value="${ratioPublicNewspapers*100}%" /></div>
</div>
</div>
</span></div>
</li>


<li class = "dashboard-expander">
<div class="collapsible-header"><spring:message code="dashboard.article.newspaper.private.average"/></div>

<div class="collapsible-body"><span>
<p class = "element"><b><spring:message code="dashboard.average"/>:</b> <jstl:out value="${averageArticlesPerPrivateNewspapers == \"null\" ? 0 : averageArticlesPerPrivateNewspapers}"></jstl:out></p>

</span></div>
</li>

<li class = "dashboard-expander">
<div class="collapsible-header"><spring:message code="dashboard.article.newspaper.public.average"/></div>

<div class="collapsible-body"><span>
<p class = "element"><b><spring:message code="dashboard.average"/>:</b> <jstl:out value="${averageArticlesPerPublicNewspapers == \"null\" ? 0 : averageArticlesPerPublicNewspapers}"></jstl:out></p>

</span></div>
</li>

<!--TODO A4 QUERY -->

<li class = "dashboard-expander">
<div class="collapsible-header"><spring:message code="dashboard.newspaper.public.private.average.ratio"/></div>

<div class="collapsible-body"><span>
<p class = "element"><b><spring:message code="dashboard.average"/>:</b> <jstl:out value="${averageRatioPrivateVSPublicNewspaperPublisher == \"null\" ? 0 : averageRatioPrivateVSPublicNewspaperPublisher}"></jstl:out></p>

</span></div>
</li>

</ul>
 