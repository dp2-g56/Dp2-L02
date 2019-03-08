<%@page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>

<%@taglib prefix="jstl"	uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="security" uri="http://www.springframework.org/security/tags"%>
<%@taglib prefix="display" uri="http://displaytag.sf.net"%>
<%@ taglib prefix="acme" tagdir="/WEB-INF/tags" %>

<security:authorize access="hasRole('BROTHERHOOD')">

<spring:message code="max.positions.procession" var="maxPos"/>
<p><jstl:out value="${maxPos}"/></p>

<display:table name="procession" requestURI="request/brotherhood/decide.do">
	<display:column titleKey="procession.rows">
		<jstl:out value="${procession.rowNumber}"/>
	</display:column>
	
	<display:column titleKey="procession.columns">
		<jstl:out value="${procession.columnNumber}"/>
	</display:column>
</display:table>

<spring:message code="procession.occuped" var="posOcupadas"/>
<p><jstl:out value="${posOcupadas}"/></p>

<display:table pagesize="5" name="requests" id="row" class="displaytag" 
					requestURI="request/brotherhood/decide.do">
		<display:column property="rowNumber" titleKey="request.rowNumber"/>
		
		<display:column property="columnNumber" titleKey="request.columnNumber"/>
</display:table>

<hr/><br/>

<form:form action="request/brotherhood/save.do" modelAttribute="request" >
	<form:hidden path="id"/>
	<form:hidden path="version"/>
	
	<jstl:if test="${approved == false}">
		<form:label path="status">
			<spring:message code="request.status" />	
		</form:label>
		<form:select path="status">
			<spring:message var="approved" code="request.status.approved"/>
			<form:option label="${approved}" value="APPROVED" />		
			<spring:message var="pending" code="request.status.pending"/>							
			<form:option label="${pending}" value="PENDING" />	
			<spring:message var="rejected" code="request.status.rejected"/>
			<form:option label="${rejected}" value="REJECTED" />
		</form:select>
		<form:errors cssClass="error" path="status" />
		
		<acme:input code="request.reasonDescription" path="reasonDescription"/>
		
		<acme:input code="request.rowNumber" path="rowNumber" value="${freePosition.get(0)}"/>
		<acme:input code="request.columnNumber" path="columnNumber" value="${freePosition.get(1)}"/>
	</jstl:if>
	
	<jstl:if test="${approved == true}">
		<acme:input code="request.rowNumber" path="rowNumber"/>
		<acme:input code="request.columnNumber" path="columnNumber"/>
	</jstl:if>
	
	
	<br/>
	
	<acme:submit name="edit" code="request.save"/>
	<acme:cancel url="request/brotherhood/list.do" code="procession.cancel"/>

</form:form>

</security:authorize>
