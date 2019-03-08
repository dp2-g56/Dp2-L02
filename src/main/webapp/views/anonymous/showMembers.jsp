<%@page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="security"
	uri="http://www.springframework.org/security/tags"%>
<%@taglib prefix="display" uri="http://displaytag.sf.net"%>
<%@taglib prefix="acme" tagdir="/WEB-INF/tags"%>

<p>
	<spring:message code="annonymous.brotherhoods" />
</p>
<display:table pagesize="5" name="members" id="row" class="displaytag"
		requestURI="${requestURI}">

		<display:column property="name" titleKey="member.name" />

		<display:column property="middleName" titleKey="member.middleName" />
		
		<display:column property="surname" titleKey="member.surname" />
		
		<display:column property="email" titleKey="member.email" />
		
		<display:column property="phoneNumber" titleKey="member.phoneNumber" />
		
		<display:column property="address" titleKey="member.address" />
</display:table>

<spring:url var="cancelar" value="/showAll/annonymous/brotherhood/list.do"/>

<p><a href="${cancelar}"><spring:message code="annonymous.cancel"/></a></p>