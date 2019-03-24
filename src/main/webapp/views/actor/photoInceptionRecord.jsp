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
	<spring:message code="brotherhood.photos" />
</p>


	<display:table pagesize="5" name="photos" id="row"
		requestURI="${requestURI}">
		<display:column titleKey="picture.url">
			<jstl:out value="${row}" />
		</display:column>
		
		<display:column>
			<!-- Boton de eliminacion -->
			<form name="word" id="word" action="authenticated/inceptionRecord/photo/delete.do" method="post" >

				<input type="hidden" name="picture" value="${row}" required><br>
				<input type="submit" name="delete" value="<spring:message code="photo.delete" />" />
			
			</form>
			
		</display:column>
		
		
		
	</display:table>
	
	
<spring:url var="add" value="/authenticated/inceptionRecord/photo/create.do"/>

<p><a href="${add}"><spring:message code="annonymous.addPicture"/></a></p>	
	
	
<spring:url var="cancelar" value="/authenticated/showProfile.do"/>

<p><a href="${cancelar}"><spring:message code="annonymous.cancel"/></a></p>

