<%@page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>

<%@taglib prefix="jstl"	uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="security" uri="http://www.springframework.org/security/tags"%>
<%@taglib prefix="display" uri="http://displaytag.sf.net"%>
<%@ taglib prefix="acme" tagdir="/WEB-INF/tags" %>

<security:authorize access="hasRole('ADMIN')">

<form:form action="priority/administrator/create.do">

	<jstl:if test="${error}">
	<font color=red>
	<jstl:choose>
		<jstl:when test="${locale=='en'}">

			 <jstl:out value="${messageErrorEng}"/> 
		
		</jstl:when>
		
		<jstl:when test="${locale=='es'}">
		
		<jstl:out value="${messageErrorSpa}"/>
		
			</jstl:when>
	
	</jstl:choose>
	</font>
	<br/>
	</jstl:if>
	
	 <spring:message code="prioriy.englishPriority" />: <input type="text" name="priorityName" required><br>
	
	 <spring:message code="prioriy.spanishPriority" />: <input type="text" name="spaPriorityName" required><br>	
	
	<acme:submit name="save" code="priority.save"/>

</form:form>

	<acme:cancel url="/priority/administrator/list.do" code="priorirty.cancel"/>

</security:authorize>