<%@page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>

<%@taglib prefix="jstl"	uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="security" uri="http://www.springframework.org/security/tags"%>
<%@taglib prefix="display" uri="http://displaytag.sf.net"%>


<%@ taglib prefix="acme" tagdir="/WEB-INF/tags" %>

<security:authorize access="hasRole('BROTHERHOOD')"> 

<form name="word" id="word" action="authenticated/periodRecord/photo/save.do?periodRecordId=${periodRecordId}" method="post" >

<input type="text" name="picture" value="${picture}" required><br>
<input type="hidden" id="periodRecordId" name="periodRecorId" value="${periodRecordId}"> 


<input type="submit" name="save" value="<spring:message code="borhterhoodFloat.create" />" />
</form>
</security:authorize>