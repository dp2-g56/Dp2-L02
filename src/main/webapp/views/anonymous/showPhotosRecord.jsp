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
	<spring:message code="annonymous.record.photos" />
</p>

	<display:table pagesize="5" name="photos" id="row" requestURI="${requestURI}">
		
		<display:column titleKey="record.photo">
			<jstl:out value="${row}" />
		</display:column>
		
	</display:table>
	
		
<spring:url var="cancelar" value="showAll/annonymous/history/list.do?brotherhoodId=${brotherhoodId}"/>

<p><a href="${cancelar}"><spring:message code="annonymous.cancel"/></a></p>

