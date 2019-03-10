<%@page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>

<%@taglib prefix="jstl"	uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="security" uri="http://www.springframework.org/security/tags"%>
<%@taglib prefix="display" uri="http://displaytag.sf.net"%>
<%@taglib prefix="acme"  tagdir="/WEB-INF/tags"%>


<security:authorize access="hasRole('SPONSOR')">


	<display:table pagesize="5" name="parades" id="row" class="displaytag" 
					requestURI="/parade/sponsor/list.do">
					
		<display:column property="ticker" titleKey="parade.ticker" /> 
		
		<display:column property="title" titleKey="parade.title" /> 
		
		<display:column property="description" titleKey="parade.description" /> 
		
		<display:column property="moment" titleKey="parade.moment" />
		
		<display:column titleKey="sponsorship.create.action">
			<spring:url var="createSponsorship" value="/sponsorship/sponsor/create.do">
				<spring:param name="paradeId" value="${row.id}" />
			</spring:url>
			<a href="${createSponsorship}">
				<spring:message code="parade.create.sponsorship" />				
			</a>
		</display:column>
	
	</display:table>

</security:authorize>
