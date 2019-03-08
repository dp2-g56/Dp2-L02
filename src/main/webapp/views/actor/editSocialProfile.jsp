<%@page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>

<%@taglib prefix="jstl"	uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="security" uri="http://www.springframework.org/security/tags"%>
<%@taglib prefix="display" uri="http://displaytag.sf.net"%>
<%@taglib prefix="acme" tagdir="/WEB-INF/tags" %>

<p><spring:message code="socialProfile.createEditSocialProfile" /></p>

<security:authorize access="isAuthenticated()">

<form:form modelAttribute="socialProfile" action="authenticated/socialProfile/create.do">
		
	<!--Hidden Attributes -->
	<form:hidden path ="id"/>
	<form:hidden path ="version"/>
	
	
	<!-- nick -->
	
	<acme:input code="socialProfile.nick" path="nick"/>	
	<br />

	
	<!-- name -->
	
	<acme:input code="socialProfile.name" path="name"/>	
	<br />
	
	<!-- profileLink -->
	
	<acme:input code="socialProfile.profileLink" path="profileLink"/>	
	<br />
	
	
	<!-- BOTONES -->	
	<acme:submit name="save" code="socialProfile.save" />  
	
	<input type="submit" <jstl:if test="${socialProfile.id == 0}">
		<jstl:out value="disabled='disabled'"/></jstl:if>
		name="delete" value="<spring:message code="socialProfile.delete" />"
		onclick="return confirm('<spring:message code="socialProfile.verificationDelete" />')" />
	
	

		<spring:url var="cancelURL"	value="/authenticated/showProfile.do" />
			<a href="${cancelURL}" style="text-decoration: none;">
    			<input type="button" value="<spring:message code="socialProfile.cancel" />" />
			</a>

	
</form:form>
</security:authorize>