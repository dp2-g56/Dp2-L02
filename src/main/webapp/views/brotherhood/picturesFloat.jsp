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
	<spring:message code="brotherhood.pictures" />
</p>

<security:authorize access="hasRole('BROTHERHOOD')">

	<display:table pagesize="5" name="pictures" id="row"
		requestURI="${requestURI}">
		<display:column titleKey="picture.url">
			<jstl:out value="${row}" />
		</display:column>
	</display:table>
	
	<spring:url var="picturesURL" value="/float/brotherhood/picture/create.do?floatId=${floatId}&procession=${procession}"> 
	  </spring:url>
	  <a href="${picturesURL}">
          <spring:message code="pictures.create" /></a>
	
	<jstl:choose>
		<jstl:when test="${!procession}" >
			<input type="button" name="cancel" value="<spring:message code="area.back"/>" onclick="javascript:relativeRedir('procession/brotherhood/list.do');" />
		</jstl:when>																				
		<jstl:otherwise>			
			<input type="button" name="cancel" value="<spring:message code="area.back"/>" onclick="javascript:relativeRedir('float/brotherhood/list.do');" />
		</jstl:otherwise>
		
	</jstl:choose>

</security:authorize>