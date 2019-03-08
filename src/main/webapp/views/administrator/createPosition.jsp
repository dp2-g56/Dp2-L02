<%@page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>

<%@taglib prefix="jstl"	uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="security" uri="http://www.springframework.org/security/tags"%>
<%@taglib prefix="display" uri="http://displaytag.sf.net"%>
<%@ taglib prefix="acme" tagdir="/WEB-INF/tags" %>

<security:authorize access="hasRole('ADMIN')">

<form:form action="position/administrator/save.do" modelAttribute="position">
		<!-- Hidden Attributes -->
		<form:hidden path="id"/>
		<form:hidden path="version" />
		
		<acme:input code="position.titleEnglish" path="titleEnglish"/>
		<acme:input code="position.titleSpanish" path="titleSpanish"/>
		
		<jstl:if test="${position.id==0}">
			<jstl:set var="submitButton" value="position.create.button"/>
		</jstl:if>
		<jstl:if test="${position.id!=0}">
			<jstl:set var="submitButton" value="position.update.button"/>
		</jstl:if>
		
		<br/>
		
		<acme:submit name="save" code="${submitButton}"/>
		
		<jstl:if test="${canBeDeleted}">
			<acme:delete code="position.delete.button" confirmationMessage="position.delete.confirmation"/>
		</jstl:if>
		
		<acme:cancel code="position.cancel.button" url="position/administrator/list.do"/>

</form:form>

</security:authorize>