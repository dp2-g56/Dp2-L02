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
	<spring:message code="brotherhood.laws" />
</p>


	<display:table pagesize="5" name="laws" id="row"
		requestURI="${requestURI}">
		<display:column titleKey="legalRecord.law">
			<jstl:out value="${row}" />
		</display:column>
		
		<display:column>
			<!-- Boton de eliminacion -->
			<form name="word" id="word" action="authenticated/legalRecord/law/delete.do?legalRecordId=${legalRecordId}" method="post" >

				<input type="hidden" name="law" value="${row}" required><br>
				<input type="submit" name="delete" value="<spring:message code="law.delete" />" />
			
			</form>
			
		</display:column>
		
		
		
	</display:table>
	
	
<spring:url var="add" value="/authenticated/legalRecord/law/create.do?legalRecordId=${legalRecordId}"/>

<p><a href="${add}"><spring:message code="annonymous.addLaw"/></a></p>	
	
	
<spring:url var="cancelar" value="/authenticated/showProfile.do"/>

<p><a href="${cancelar}"><spring:message code="annonymous.cancel"/></a></p>

