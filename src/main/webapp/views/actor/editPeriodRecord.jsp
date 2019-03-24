<%@page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>

<%@taglib prefix="jstl"	uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="security" uri="http://www.springframework.org/security/tags"%>
<%@taglib prefix="display" uri="http://displaytag.sf.net"%>
<%@taglib prefix="acme" tagdir="/WEB-INF/tags" %>

<p><spring:message code="periodRecord.createEditPeriodRecord" /></p>

<security:authorize access="hasRole('BROTHERHOOD')"> 

<form:form modelAttribute="periodRecord" action="authenticated/periodRecord/edit.do">
		
	<!--Hidden Attributes -->
	<form:hidden path ="id"/>
	<form:hidden path ="version"/>
	
	
	<!-- nick -->
	
	<acme:input code="periodRecord.title" path="title"/>	
	<br />

	
	<!-- description -->
	
	<acme:input code="periodRecord.description" path="description"/>	
	<br />
	
	<!-- Start year -->
	
	<acme:input code="periodRecord.startYear" path="startYear"/>	
	<br />
	
	<!-- End year -->
	
	<acme:input code="periodRecord.endYear" path="endYear"/>
	<br />
	
	
	<!-- BOTONES -->	
	<acme:submit name="save" code="periodRecord.save" />  
	
	<input type="submit" <jstl:if test="${periodRecord.id == 0}">
		<jstl:out value="disabled='disabled'"/></jstl:if>
		name="delete" value="<spring:message code="periodRecord.delete" />"
		onclick="return confirm('<spring:message code="periodRecord.verificationDelete" />')" />
	
	

		<spring:url var="cancelURL"	value="/authenticated/showProfile.do" />
			<a href="${cancelURL}" style="text-decoration: none;">
    			<input type="button" value="<spring:message code="periodRecord.cancel" />" />
			</a>

	
</form:form>
</security:authorize>