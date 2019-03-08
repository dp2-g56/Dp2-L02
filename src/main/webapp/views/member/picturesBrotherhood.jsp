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
	<spring:message code="brotherhood.pictures" />
</p>

<security:authorize access="hasRole('MEMBER')">

	<display:table pagesize="5" name="pictures" id="row"
		requestURI="${requestURI}">
		<display:column titleKey="picture.url">
			<jstl:out value="${row}" />
		</display:column>
	</display:table>

</security:authorize>