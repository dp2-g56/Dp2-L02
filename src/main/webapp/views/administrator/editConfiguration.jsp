<%@page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>

<%@taglib prefix="jstl"	uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="security" uri="http://www.springframework.org/security/tags"%>
<%@taglib prefix="display" uri="http://displaytag.sf.net"%>		
<%@ taglib prefix="acme" tagdir="/WEB-INF/tags" %>

<security:authorize access="hasRole('ADMIN')">

<form:form action="configuration/administrator/save.do" modelAttribute="configuration">
		<!-- Hidden Attributes -->
		<form:hidden path="id"/>
		<form:hidden path="version" />
		
		<acme:input code="configuration.finderResult" path="finderResult"/>
		
		<acme:input code="configuration.minFinderResults" path="minFinderResults"/>
		
		<acme:input code="configuration.maxFinderResults" path="maxFinderResults"/>
		
		<acme:input code="configuration.timeFinder" path="timeFinder"/>
		
		<acme:input code="configuration.minTimeFinder" path="minTimeFinder"/>
		
		<acme:input code="configuration.maxTimeFinder" path="maxTimeFinder"/>
		
		<acme:input code="configuration.spainTelephoneCode" path="spainTelephoneCode"/>
		
		<acme:input code="configuration.welcomeMessageEnglish" path="welcomeMessageEnglish"/>
		
		<acme:input code="configuration.welcomeMessageSpanish" path="welcomeMessageSpanish"/>
		
		<acme:input code="configuration.systemName" path="systemName"/>
		
		<acme:input code="configuration.imageURL" path="imageURL"/>
		
		<acme:input code="configuration.VAT" path="VAT"/>
		
		<acme:input code="configuration.fare" path="fare"/>
		
		<acme:input code="configuration.cardType" path="cardType"/>
		
		<acme:submit name="save" code="configuration.save.button"/>
		
		<acme:cancel url="/configuration/administrator/list.do" code="configuration.cancel.button"/>
		
</form:form>

</security:authorize>