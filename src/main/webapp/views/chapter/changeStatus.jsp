<%--
 * action-2.jsp
 *
 * Copyright (C) 2019 Universidad de Sevilla
 * 
 * The use of this project is hereby constrained to the conditions of the 
 * TDG Licence, a copy of which you may download from 
 * http://www.tdg-seville.info/License.html
 --%>

<%@page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>

<%@taglib prefix="jstl"	uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="security" uri="http://www.springframework.org/security/tags"%>
<%@taglib prefix="display" uri="http://displaytag.sf.net"%>
<%@taglib prefix="acme" tagdir="/WEB-INF/tags" %>
<%@ page import="java.util.Collection" %>

<form:form action="parade/chapter/changeStatus.do" modelAttribute="parade" >

	<form:hidden path="id"/>
	
	<h2>	<spring:message code="parade.warning" />	 </h2>
	
	<acme:radio items="${paradeStatus}" itemsName="${statusName}" code="parade.paradeStatus" path="paradeStatus"/>
	
	<acme:textarea code="parade.rejectedReason" path="rejectedReason"/>
	
	<acme:submit name="save" code="parade.save"/>
	
	<acme:cancel url="parade/chapter/list.do" code="parade.cancel"/>

</form:form>