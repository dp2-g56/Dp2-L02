<%@page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>

<%@taglib prefix="jstl"	uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="security" uri="http://www.springframework.org/security/tags"%>
<%@taglib prefix="display" uri="http://displaytag.sf.net"%>
<%@taglib prefix="acme" tagdir="/WEB-INF/tags" %>

<p><spring:message code="inceptionRecord.createEditInceptionRecord" /></p>

<security:authorize access="hasRole('BROTHERHOOD')"> 

<form:form modelAttribute="inceptionRecord" action="authenticated/inceptionRecord/edit.do">
		
	<!--Hidden Attributes -->
	<form:hidden path ="id"/>
	<form:hidden path ="version"/>
	
	
	<!-- title -->
	
	<acme:input code="inceptionRecord.title" path="title"/>	
	<br />

	
	<!-- description -->
	
	<acme:input code="inceptionRecord.description" path="description"/>	
	<br />
	
	<!-- BOTONES -->	
	<acme:submit name="save" code="inceptionRecord.save" />  


	<spring:url var="cancelURL"	value="/authenticated/showProfile.do" />
		<a href="${cancelURL}" style="text-decoration: none;">
    		<input type="button" value="<spring:message code="inceptionRecord.cancel" />" />
		</a>

	
</form:form>
</security:authorize>