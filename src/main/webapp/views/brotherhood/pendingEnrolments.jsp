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
	<spring:message code="brotherhood.pending.enrolments" />
</p>

<security:authorize access="hasRole('BROTHERHOOD')">
	
	<jstl:if test="${!hasArea}">
		<h2> 
			<font color="red"> 
				<spring:message code="brotherhood.select.Area" /> 
			</font> 
		</h2>
	</jstl:if>

	<display:table pagesize="5" name="enrolments" id="row"
		class="displaytag" requestURI="enrolment/brotherhood/list.do">

		<display:column property="member.name" titleKey="member.name" />

		<display:column property="member.middleName"
			titleKey="member.middleName" />

		<display:column property="member.surname" titleKey="member.surname" />

		<display:column property="creationMoment" titleKey="enrolment.moment"
			sortable="true" format="{0,date,dd/MM/yyyy HH:mm}" />

	<jstl:if test="${hasArea}">
		<display:column>
			<spring:url var="rejectUrl"
				value="/enrolment/brotherhood/reject.do?enrolmentId={enrolmentId}">
				<spring:param name="enrolmentId" value="${row.id}" />
			</spring:url>
			<form:form action="${rejectUrl}">
				<acme:submit code="enrolment.reject" name="save" />
			</form:form>
		</display:column>

		<display:column>
			<spring:url var="assignPositionUrl"
				value="/enrolment/brotherhood/assignPosition.do?enrolmentId={enrolmentId}">
				<spring:param name="enrolmentId" value="${row.id}" />
			</spring:url>
			<a href="${assignPositionUrl}"> <spring:message
					code="enrolment.assign.position" />
			</a>
		</display:column>
	</jstl:if>
	
	
	
	
	</display:table>

</security:authorize>