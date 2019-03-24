<%@page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>

<%@taglib prefix="jstl"	uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="security" uri="http://www.springframework.org/security/tags"%>
<%@taglib prefix="display" uri="http://displaytag.sf.net"%>
<%@taglib prefix="acme" tagdir="/WEB-INF/tags" %>

<p><spring:message code="miscellaneousRecord.createEditMiscellaneousRecord" /></p>

<security:authorize access="hasRole('BROTHERHOOD')"> 

<form:form modelAttribute="miscellaneousRecord" action="authenticated/miscellaneousRecord/edit.do">
		
	<!--Hidden Attributes -->
	<form:hidden path ="id"/>
	<form:hidden path ="version"/>
	
	
	<!-- Title -->
	
	<acme:input code="miscellaneousRecord.title" path="title"/>	
	<br />

	
	<!-- name -->
	
	<acme:input code="miscellaneousRecord.description" path="description"/>	
	<br />
	
	
	
	<!-- BOTONES -->	
	<acme:submit name="save" code="miscellaneousRecord.save" />  
	
	<input type="submit" <jstl:if test="${miscellaneousRecord.id == 0}">
		<jstl:out value="disabled='disabled'"/></jstl:if>
		name="delete" value="<spring:message code="miscellaneousRecord.delete" />"
		onclick="return confirm('<spring:message code="miscellaneousRecord.verificationDelete" />')" />
	
	

		<spring:url var="cancelURL"	value="/authenticated/showProfile.do" />
			<a href="${cancelURL}" style="text-decoration: none;">
    			<input type="button" value="<spring:message code="miscellaneousRecord.cancel" />" />
			</a>

	
</form:form>
</security:authorize>