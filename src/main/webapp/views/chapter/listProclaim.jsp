<%@page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>

<%@taglib prefix="jstl"	uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="security" uri="http://www.springframework.org/security/tags"%>
<%@taglib prefix="display" uri="http://displaytag.sf.net"%>

<p><spring:message code="chapter.proclaims" /></p>

<security:authorize access="hasRole('CHAPTER')">


<display:table
	pagesize="4" name="proclaims" id="row"
	requestURI="chapter/list.do" >
	
	<!-- Date -->
	<display:column	property ="publishMoment" titleKey="chapter.moment"/>
	
	<display:column	property ="description" titleKey="chapter.description"/>
	

															
</display:table>

<a href="chapter/create.do"><spring:message code="proclaim.create" /></a>

</security:authorize>