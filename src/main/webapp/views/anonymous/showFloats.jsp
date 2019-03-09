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
	<spring:message code="annonymous.float" />
</p>

<display:table
	pagesize="5" name="floats" id="row"
	requestURI="${requestURI}" >
	
	<display:column property="title" titleKey="float.title" />
	
	<display:column property="description" titleKey="float.description" />
		
	<display:column titleKey="float.pictures">
        <jstl:set var="picturesSize" value="${row.pictures.size()}" />
        <spring:url var="picturesURL" value="/showAll/annonymous/picture/list.do?floatId={floatId}&parade={parade}">      		
        	<spring:param name="floatId" value="${row.id}"/>
        	<spring:param name="parade" value="${!restriction}"/>
        </spring:url>
        <a href="${picturesURL}">
              <spring:message var ="viewPictures1" code="float.viewPictures" />
              <jstl:out value="${viewPictures1}(${picturesSize})" />    
        </a>
    </display:column>
  	

												
</display:table>

<spring:url var="cancelar" value="/showAll/annonymous/brotherhood/list.do"/>

<p><a href="${cancelar}"><spring:message code="annonymous.cancel"/></a></p>

