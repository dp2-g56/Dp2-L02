<%@page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="security"
	uri="http://www.springframework.org/security/tags"%>
<%@taglib prefix="display" uri="http://displaytag.sf.net"%>
<%@taglib prefix="acme" tagdir="/WEB-INF/tags"%>

<p>
	<spring:message code="annonymous.chapters" />
</p>



	<display:table pagesize="5" name="chapters" id="row"
		class="displaytag" requestURI="${requestURI}">

		<display:column property="title" titleKey="chapter.title" />


		<display:column titleKey="chapter.proclaims">
			<jstl:set var="proclaimsSize" value="${row.proclaims.size()}" />
			<spring:url var="proclaimsUrl"
				value="/showAll/annonymous/proclaim/list.do">
				<spring:param name="chapterId" value="${row.id}" />
			</spring:url>
			<a href="${proclaimsUrl}"> <spring:message var="proclaims"
					code="chapter.proclaims" /> <jstl:out
					value="${proclaims}(${proclaimsSize})" />
			</a>
		</display:column>
		
		<display:column>
		
			<spring:url var="brotherhoodsUrl"
				value="/showAll/annonymous/brotherhood/listByArea.do">
				<spring:param name="areaId" value="${row.area.id}" />
			</spring:url>
				<a href="${brotherhoodsUrl}">
				<spring:message code="annonymous.brotherhoods"/>
				 (<jstl:out value="${row.area.name}"></jstl:out> )</a>
		</display:column>
	
		
		</display:table>

