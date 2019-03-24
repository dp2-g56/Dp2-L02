<%@page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>

<%@taglib prefix="jstl"	uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="security" uri="http://www.springframework.org/security/tags"%>
<%@taglib prefix="display" uri="http://displaytag.sf.net"%>


<%@ taglib prefix="acme" tagdir="/WEB-INF/tags" %>

<security:authorize access="hasRole('BROTHERHOOD')"> 

<form name="word" id="word" action="authenticated/legalRecord/law/save.do?legalRecordId=${legalRecordId}" method="post" >

<input type="text" name="law" value="${law}" required><br>
<input type="hidden" id="legalRecordId" name="legalRecorId" value="${legalRecordId}"> 


<input type="submit" name="save" value="<spring:message code="law.create" />" />
</form>
</security:authorize>