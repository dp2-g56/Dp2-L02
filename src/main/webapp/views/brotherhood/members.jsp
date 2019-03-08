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
	<spring:message code="brotherhood.members" />
</p>

<security:authorize access="hasRole('BROTHERHOOD')">

	<jstl:set var="cont" value="0" />

	<display:table pagesize="5" name="members" id="row" class="displaytag"
		requestURI="member/brotherhood/list.do">

		<display:column property="name" titleKey="member.name" />

		<display:column property="middleName" titleKey="member.middleName" />

		<display:column property="surname" titleKey="member.surname" />

		<display:column property="email" titleKey="member.email" />

		<display:column property="phoneNumber" titleKey="member.phoneNumber" />

		<display:column property="address" titleKey="member.address" />

		<display:column titleKey="member.position">
			<jstl:out value="${positions.get(cont)}"></jstl:out>
			<jstl:set var="cont" value="${cont+1}" />
		</display:column>

		<display:column>
			<spring:url var="expelledUrl"
				value="/member/brotherhood/expelled.do?memberId={memberId}">
				<spring:param name="memberId" value="${row.id}" />
			</spring:url>
			<form:form action="${expelledUrl}">
				<acme:submit code="member.expelled" name="save" />
			</form:form>
		</display:column>

	</display:table>

</security:authorize>