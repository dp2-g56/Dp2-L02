<%@page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>

<%@taglib prefix="jstl"	uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="security" uri="http://www.springframework.org/security/tags"%>
<%@taglib prefix="display" uri="http://displaytag.sf.net"%>


	
	<display:table pagesize="5" name="proclaims" id="row" requestURI="${requestURI}" >
	
	<display:column	property ="publishMoment" titleKey="chapter.moment"/>
	
	<display:column	property ="description" titleKey="chapter.description"/>
	
	</display:table>


		<spring:url var="cancelar" value="showAll/annonymous/chapter/list.do"/>

		

	<p><a href="${cancelar}"><spring:message code="annonymous.cancel"/></a></p>
