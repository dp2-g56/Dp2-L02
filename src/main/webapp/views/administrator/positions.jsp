<%@page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>

<%@taglib prefix="jstl"	uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="security" uri="http://www.springframework.org/security/tags"%>
<%@taglib prefix="display" uri="http://displaytag.sf.net"%>

<security:authorize access="hasRole('ADMIN')">

<display:table pagesize="5" name="positions" id="row" class="displaytag" 
					requestURI="position/administrator/list.do">
	
	<jstl:if test="${locale=='EN'}">
		<display:column property="titleEnglish" titleKey="position.title" />
	</jstl:if>
	<jstl:if test="${locale=='ES'}">
		<display:column property="titleSpanish" titleKey="position.title" />
	</jstl:if>
		
	<display:column>
		<spring:url var="editPosition" value="/position/administrator/edit.do">
			<spring:param name="positionId" value="${row.id}" />
		</spring:url>
		<a href="${editPosition}">
			<spring:message code="position.edit" />				
		</a>
	</display:column>
	
</display:table>

<spring:url var="createPosition" value="/position/administrator/create.do"/>
<p><a href="${createPosition}"><spring:message code="position.create" /></a></p>

</security:authorize>
