<%@page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>

<%@taglib prefix="jstl"	uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="security" uri="http://www.springframework.org/security/tags"%>
<%@taglib prefix="display" uri="http://displaytag.sf.net"%>


<%@ taglib prefix="acme" tagdir="/WEB-INF/tags" %>

<security:authorize access="hasRole('BROTHERHOOD')"> 

<form name="word" id="word" action="float/brotherhood/picture/save.do" method="post" >

<input type="text" name="picture" value="${picture}" required><br>
<input type="hidden" id="floatId" name="floatId" value="${floatId}">
<input type="hidden" id="procession" name="procession" value="${procession}">

<input type="submit" name="save" value="<spring:message code="borhterhoodFloat.create" />" />
</form>

<a href="float/brotherhood/picture/list.do?floatId=${floatId}&procession=true"><button><spring:message code="procession.back" /></button></a>
</security:authorize>