<%@page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>

<%@taglib prefix="jstl"	uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="security" uri="http://www.springframework.org/security/tags"%>
<%@taglib prefix="display" uri="http://displaytag.sf.net"%>
<%@taglib prefix="acme" tagdir="/WEB-INF/tags" %>

<p><spring:message code="legalRecord.createEditLegalRecord" /></p>

<security:authorize access="hasRole('BROTHERHOOD')"> 

<form:form modelAttribute="legalRecord" action="authenticated/legalRecord/edit.do">
		
	<!--Hidden Attributes -->
	<form:hidden path ="id"/>
	<form:hidden path ="version"/>
	
	
	<!-- nick -->
	
	<acme:input code="legalRecord.title" path="title"/>	
	<br />

	
	<!-- description -->
	
	<acme:input code="legalRecord.description" path="description"/>	
	<br />
	
	<!-- legal name -->
	
	<acme:input code="legalRecord.legalName" path="legalName"/>	
	<br />
	
	<!-- VAT NUMBER -->
	
	<acme:placeholder code="legalRecord.vatNumber" path="vatNumber" placeholder="ES123456789"/>	
	<br />
	
	
	<!-- BOTONES -->	
	<acme:submit name="save" code="legalRecord.save" />  
	
	<input type="submit" <jstl:if test="${legalRecord.id == 0}">
		<jstl:out value="disabled='disabled'"/></jstl:if>
		name="delete" value="<spring:message code="legalRecord.delete" />"
		onclick="return confirm('<spring:message code="legalRecord.verificationDelete" />')" />
	
	

		<spring:url var="cancelURL"	value="/authenticated/showProfile.do" />
			<a href="${cancelURL}" style="text-decoration: none;">
    			<input type="button" value="<spring:message code="legalRecord.cancel" />" />
			</a>

	
</form:form>
</security:authorize>