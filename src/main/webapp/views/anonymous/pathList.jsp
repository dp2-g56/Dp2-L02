<%@page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>

<%@taglib prefix="jstl"	uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="security" uri="http://www.springframework.org/security/tags"%>
<%@taglib prefix="display" uri="http://displaytag.sf.net"%>


	
	<display:table pagesize="5" name="segments" id="row" requestURI="${requestURI}" >
	
	<display:column titleKey="segment.origin">
		<jstl:out value="${row.originLatitude}, ${row.originLongitude}"/> (<spring:message code="segment.latitude"/>, <spring:message code="segment.longitude"/>)
	</display:column>
	
	<display:column titleKey="segment.destination">
		<jstl:out value="${row.destinationLatitude}, ${row.destinationLongitude}"/> (<spring:message code="segment.latitude"/>, <spring:message code="segment.longitude"/>)
	</display:column>
	
	<display:column titleKey="segment.time" property="time"/>
	
	
	</display:table>


		<spring:url var="cancelar" value="showAll/annonymous/parade/list.do">
			<spring:param name="brotherhoodId" value="${param.brotherhoodId}"></spring:param>
		</spring:url>
		

	<p><a href="${cancelar}"><spring:message code="annonymous.cancel"/></a></p>
