<%@page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>

<%@taglib prefix="jstl"	uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="security" uri="http://www.springframework.org/security/tags"%>
<%@taglib prefix="display" uri="http://displaytag.sf.net"%>

<security:authorize access="hasRole('ADMIN')">

<display:table pagesize="5" name="priority" id="row" class="displaytag" 
					requestURI="priority/administrator/list.do">
					
	<display:column titleKey="prioriy.englishPriority">
		<jstl:out value="${row}"/>
	</display:column>
	
	<display:column titleKey="prioriy.spanishPriority">
		<jstl:out value="${prioritySpa.get(priority.lastIndexOf(row))}"/>
	</display:column>
	
	</display:table>
	
<spring:url var="createUrl" value="/priority/administrator/create.do"/>

<p><a href="${createUrl}"><spring:message code="priority.create" /></a></p>

</security:authorize>