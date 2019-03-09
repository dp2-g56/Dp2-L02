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
	pagesize="5" name="parades" id="row"
	requestURI="${requestURI}" >
	
	<display:column property="ticker" titleKey="parade.ticker" />
	
	<display:column property="title" titleKey="parade.title" />
	
	<display:column property="description" titleKey="parade.description" />
	
	<display:column property="moment" titleKey="parade.moment" />
	
	<display:column property="rowNumber" titleKey="parade.rowNumber" />
	
	<display:column property="columnNumber" titleKey="parade.columnNumber" />
	
</display:table>	

<spring:url var="cancelar" value="/showAll/annonymous/brotherhood/list.do"/>

<p><a href="${cancelar}"><spring:message code="annonymous.cancel"/></a></p>