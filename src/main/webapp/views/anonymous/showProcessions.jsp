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

<display:table
	pagesize="5" name="processions" id="row"
	requestURI="${requestURI}" >
	
	<display:column property="ticker" titleKey="procession.ticker" />
	
	<display:column property="title" titleKey="procession.title" />
	
	<display:column property="description" titleKey="procession.description" />
	
	<display:column property="moment" titleKey="procession.moment" />
	
	<display:column property="rowNumber" titleKey="procession.rowNumber" />
	
	<display:column property="columnNumber" titleKey="procession.columnNumber" />
	
</display:table>	

<spring:url var="cancelar" value="/showAll/annonymous/brotherhood/list.do"/>

<p><a href="${cancelar}"><spring:message code="annonymous.cancel"/></a></p>