<%@page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>

<%@taglib prefix="jstl"	uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="security" uri="http://www.springframework.org/security/tags"%>
<%@taglib prefix="display" uri="http://displaytag.sf.net"%>
<%@ taglib prefix="acme" tagdir="/WEB-INF/tags" %>


<security:authorize access="hasRole('CHAPTER')"> 

	<form:form modelAttribute="proclaim" action="chapter/edit.do">
    <!--Hidden Attributes -->
	<form:hidden path ="id"/>
	<form:hidden path ="version"/>

	
	<acme:textarea code="chapter.description" path="description" /> 
	
	<br/>
	
 	<input  type="submit" 
		 name = "save" value="<spring:message code="chapter.save"/>" onclick="return confirm('<spring:message code="chapter.saveConfirmation" />')"/> 
 	
 
	</form:form>
	
	<acme:cancel url="/chapter/list.do" code="float.cancel" />  
	
</security:authorize>