<%@page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>

<%@taglib prefix="jstl"	uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="security" uri="http://www.springframework.org/security/tags"%>
<%@taglib prefix="display" uri="http://displaytag.sf.net"%>
<%@ page import="domain.ParadeStatus" %>

<security:authorize access="hasRole('CHAPTER')">


	
	<display:table pagesize="5" name="parades" id="row" >
	
		<jstl:choose>
		<jstl:when test="${row.paradeStatus.toString() == 'SUBMITTED'}">
		
		<jstl:set var="color" value="grey" />
		
		</jstl:when>
		
		<jstl:when test="${row.paradeStatus.toString() == 'REJECTED'}">
		
		<jstl:set var="color" value="red" />
		
		</jstl:when>
		
		<jstl:when test="${row.paradeStatus.toString() == 'ACCEPTED'}">
		
		<jstl:set var="color" value="green" />
		
		</jstl:when>
		
		</jstl:choose>
	
	<display:column titleKey="parade.ticker" >

		<font color="${color}"><jstl:out value="${row.ticker}"/></font>	
	
	</display:column>

	<display:column titleKey="parade.title">
	
		<font color="${color}"><jstl:out value="${row.title}"/></font>	
	
	</display:column>
	
	<display:column titleKey="parade.description" >
		
		<font color="${color}"><jstl:out value="${row.description}"/></font>	
		
	</display:column>
	
	<display:column titleKey="parade.moment" >
	
		<font color="${color}"><jstl:out value="${row.moment}"/></font>	
		
	</display:column>
	
	<display:column titleKey="parade.rowNumber" >
	
		<font color="${color}"><jstl:out value="${row.rowNumber}"/></font>	
	
	</display:column>
	
	<display:column titleKey="parade.columnNumber" >
	
		<font color="${color}"><jstl:out value="${row.columnNumber}"/></font>
	
	</display:column>

	<display:column titleKey ="parade.paradeStatus">
	
		<font color="${color}"><jstl:out value="${row.paradeStatus}"/></font>
	
	</display:column>
	
	<display:column>
	
	<jstl:if test="${row.paradeStatus.toString() == 'SUBMITTED' }">
	
		<spring:url var="statusUrl" value="/parade/chapter/changeStatus.do">
            	<spring:param name="paradeId" value="${row.id}"/>
        	</spring:url>
        	
        	<a href="${statusUrl}">
				<spring:message code="parade.changeStatus" />				
			</a>
        	
  	</jstl:if>
	</display:column>
	
									
</display:table>

</security:authorize>