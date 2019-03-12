<%@page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>

<%@taglib prefix="jstl"	uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="security" uri="http://www.springframework.org/security/tags"%>
<%@taglib prefix="display" uri="http://displaytag.sf.net"%>
<%@taglib prefix="acme" tagdir="/WEB-INF/tags" %>

<security:authorize access="hasRole('SPONSOR')">

<form:form action="sponsorship/sponsor/create.do" modelAttribute="formObject" >

<fieldset>
  <legend> <spring:message code="sponsorship.data" /> </legend>
  
  	<br />

	<acme:input code="sponsorship.banner" path="banner"/>	
	<br />
	
	<acme:input code="sponsorship.targetURL" path="targetURL"/>	
	<br />
	
	<input type="text" hidden = "true" name="paradeId" value="${paradeId}"/>
	
</fieldset>

<fieldset>
  <legend> <spring:message code="creditCard.data" /> </legend>
  
  	<br />
  
	<acme:input code="creditCard.holderName" path="holderName"/>	
	<br />
	
	<acme:input code="creditCard.brandName" path="brandName"/>	
	<br />
	
	<acme:input code="creditCard.number" path="number"/>	
	<br />
	
	<acme:input code="creditCard.expirationMonth" path="expirationMonth"/>	
	<br />
	
	<acme:input code="creditCard.expirationYear" path="expirationYear"/>	
	<br />
	
	<acme:input code="creditCard.cvvCode" path="cvvCode"/>	
	<br />
	
</fieldset>

	<br />
	
	<acme:submit code="sponsorship.createButton" name="save" />
	<acme:cancel url="/parade/sponsor/list.do" code="sponsorship.cancel" /> 
	
</form:form> 

</security:authorize>