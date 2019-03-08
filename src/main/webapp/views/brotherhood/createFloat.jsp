<%@page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>

<%@taglib prefix="jstl"	uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="security" uri="http://www.springframework.org/security/tags"%>
<%@taglib prefix="display" uri="http://displaytag.sf.net"%>


<%@ taglib prefix="acme" tagdir="/WEB-INF/tags" %>

<p><spring:message code="float.create" /></p>

<security:authorize access="hasRole('BROTHERHOOD')"> 

	<form:form modelAttribute="floatt" action="float/brotherhood/edit.do">
    <!--Hidden Attributes -->
	<form:hidden path ="id"/>
	<form:hidden path ="version"/>


	<acme:textbox code="float.title" path="title" />
	
	
	<acme:textarea code="float.description" path="description" /> 
	
	<br/>
	
 	<acme:submit name="save" code="float.save" />  
 	
 	<jstl:if test="${floatt.id != 0 }">
 		<acme:submit name="delete" code="float.delete" />
 	</jstl:if> 
 	
 	
 	
	</form:form>
	
	<acme:cancel url="/float/brotherhood/list.do" code="float.cancel" />  
	
</security:authorize>