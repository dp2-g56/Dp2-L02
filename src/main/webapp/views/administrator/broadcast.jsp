<%@page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="security" uri="http://www.springframework.org/security/tags"%>
<%@taglib prefix="display" uri="http://displaytag.sf.net"%>
<%@taglib prefix="acme"  tagdir="/WEB-INF/tags"%>

<p>
	<spring:message code="administrator.broadcast" />
</p>

<security:authorize access="hasRole('ADMIN')">

<form:form modelAttribute="messageSend" action="broadcast/administrator/send.do">

	<form:hidden path="id"/>
	<form:hidden path="version"/>
	
	<jstl:out value="${confirmation}" />	
	
	<acme:input code="broadcast.subject" path="subject"/>

	<acme:input code="broadcast.tags" path="tags"/>
	
	<acme:radio items="${priority}" itemsName="${priorityName}" code="broadcast.priority" path="priority"/>

	<acme:textarea code="broadcast.message" path="body"/>
	
	<acme:submit name="send" code="broadcast.send"/>
		
</form:form>

</security:authorize>
