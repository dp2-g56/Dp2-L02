<%@page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="security"
	uri="http://www.springframework.org/security/tags"%>
<%@taglib prefix="display" uri="http://displaytag.sf.net"%>
<%@taglib prefix="acme" tagdir="/WEB-INF/tags"%>

<security:authorize access="hasRole('BROTHERHOOD')">

	<form:form action="enrolment/brotherhood/save.do"
		modelAttribute="enrolment">
		
		<form:hidden path="id" />
		<form:hidden path="version" />
		<jstl:if test="${locale == 'EN' }">
			<acme:select items="${positions}" itemLabel="titleEnglish"
				code="enrolment.position" path="position" />
		</jstl:if>
		<jstl:if test="${locale == 'ES' }">
			<acme:select items="${positions}" itemLabel="titleSpanish"
				code="enrolment.position" path="position" />
		</jstl:if>

		<acme:submit name="save" code="enrolment.assign.position" />

		<acme:cancel url="enrolment/brotherhood/list.do" code="enrolment.cancel"/>
		
	</form:form>
</security:authorize>