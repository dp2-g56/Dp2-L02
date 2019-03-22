<%@page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>

<%@taglib prefix="jstl"	uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="security" uri="http://www.springframework.org/security/tags"%>
<%@taglib prefix="display" uri="http://displaytag.sf.net"%>


<security:authorize access="hasRole('BROTHERHOOD')">

		<h3>	<a href="path/brotherhood/delete.do?paradeId=${param.paradeId}">
			<spring:message code="parade.path.delete" /> 
			</a></h3>
	
	<display:table pagesize="5" name="segments" id="row" requestURI="${requestURI}" >
	
	<display:column titleKey="segment.origin">
		<jstl:out value="${row.originLatitude}, ${row.originLongitude}"/> (<spring:message code="segment.latitude"/>, <spring:message code="segment.longitude"/>)
	</display:column>
	
	<display:column titleKey="segment.destination">
		<jstl:out value="${row.destinationLatitude}, ${row.destinationLongitude}"/> (<spring:message code="segment.latitude"/>, <spring:message code="segment.longitude"/>)
	</display:column>
	
	<display:column titleKey="segment.time" property="time"/>
	
	<display:column >
		<spring:url value="segment/brotherhood/edit.do" var="edit">
			<spring:param name="paradeId" value="${param.paradeId}"/>
			<spring:param name="segmentId" value="${row.id}"/>
		</spring:url>
		<a href="${edit}">
			<spring:message code="segment.edit"/>
		</a>
	</display:column>
	
	
	</display:table>
	<spring:url value="segment/brotherhood/edit.do" var="create">
		<spring:param name="paradeId" value="${param.paradeId}"/>
		<spring:param name="segmentId" value="0"/>
	</spring:url>
	<a href="${create}">
		<spring:message code="segment.create"/>
	</a> 

</security:authorize>