<%@page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>

<%@taglib prefix="jstl"	uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="security" uri="http://www.springframework.org/security/tags"%>
<%@taglib prefix="display" uri="http://displaytag.sf.net"%>
<%@taglib prefix="acme" tagdir="/WEB-INF/tags" %>

<p><spring:message code="linkRecord.createEditLinkRecord" /></p>

<security:authorize access="hasRole('BROTHERHOOD')"> 

<form:form modelAttribute="formObjectLinkRecord" action="authenticated/linkRecord/edit.do">
		
	<!--Hidden Attributes -->
	<form:hidden path ="id"/> 
	
	
	<!-- title -->
	
	<acme:input code="linkRecord.title" path="title"/>	
	<br />

	
	<!-- description -->
	
	<acme:input code="linkRecord.description" path="description"/>	
	<br />
	
	<!-- Brotherhood -->
	
	<fieldset>	
		<legend>	<spring:message code="linkRecord.brotherhood" />	</legend>
	
			<%-- <acme:selectMap  path="brotherhoodId" map="${map}" id="brotherhoodId"/> --%>
			<acme:select code="linkRecord.brotherhood" path="brotherhood" items="${brotherhoods}" itemLabel="title" id="brotherhood"/>
			
	</fieldset>
	<br />
	
	
	<!-- BOTONES -->	
	<acme:submit name="save" code="linkRecord.save" />  
	
	<input type="submit" <jstl:if test="${formObjectLinkRecord.id == 0}">
		<jstl:out value="disabled='disabled'"/></jstl:if>
		name="delete" value="<spring:message code="linkRecord.delete" />"
		onclick="return confirm('<spring:message code="linkRecord.verificationDelete" />')" />
	
	<spring:url var="cancelURL"	value="/authenticated/showProfile.do" />
		<a href="${cancelURL}" style="text-decoration: none;">
    		<input type="button" value="<spring:message code="linkRecord.cancel" />" />
		</a>

	
</form:form>
</security:authorize>