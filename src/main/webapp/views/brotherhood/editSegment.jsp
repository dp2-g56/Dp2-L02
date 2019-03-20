<%@page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>

<%@taglib prefix="jstl"	uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="security" uri="http://www.springframework.org/security/tags"%>
<%@taglib prefix="display" uri="http://displaytag.sf.net"%>
<%@ taglib prefix="acme" tagdir="/WEB-INF/tags" %>

<security:authorize access="hasRole('BROTHERHOOD')">

<form:form action="segment/brotherhood/edit.do?paradeId=${param.paradeId}" modelAttribute="segment" >
		<form:hidden path ="id"/>
	<jstl:if test="${isOrigin}">
		<acme:textbox code="segment.originLatitude" path="originLatitude"/>
		<acme:textbox code="segment.originLongitude" path="originLongitude"/>
	</jstl:if>
	
		<acme:textbox code="segment.destinationLatitude" path="destinationLatitude"/>
		<acme:textbox code="segment.destinationLongitude" path="destinationLongitude"/>
		<acme:textbox code="segment.time" path="time"/>

		<acme:submit name="save" code="segment.save"/>
		
	<jstl:if test="${segmentId!=0}">
		<acme:delete code="segment.delete" confirmationMessage="segment.confirmDelete"/>
	</jstl:if>
	
</form:form>
	
		<spring:url value="path/brotherhood/list.do" var="cancel">
		<spring:param name="paradeId" value="${param.paradeId}"/>
	</spring:url>
	<acme:cancel url="${cancel}" code="segment.cancel"/>


</security:authorize>