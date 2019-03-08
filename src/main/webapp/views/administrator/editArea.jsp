<%@page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>

<%@taglib prefix="jstl"	uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="security" uri="http://www.springframework.org/security/tags"%>
<%@taglib prefix="display" uri="http://displaytag.sf.net"%>
<%@ taglib prefix="acme" tagdir="/WEB-INF/tags" %>

<security:authorize access="hasRole('ADMIN')">

<form:form action="area/administrator/edit.do" modelAttribute="area" >
	<form:hidden path="id"/>
	<form:hidden path="version" />
		
		
	<acme:textbox code="area.name" path="name"/>	
	<br />
	<spring:message code="area.pictures" />:
	<br />
	<input type="hidden" name="newPictures" value=""/>
	<textarea rows="12" cols="50" name="newPictures" id="newPictures" placeholder="<spring:message code="pictures.placeholder"/>" ><jstl:forEach items="${area.pictures}" var="picture"><jstl:out value="${picture},"></jstl:out></jstl:forEach></textarea>
	<br />
	
	<jstl:choose><jstl:when test="${area.id == 0}">
		<acme:submit name="save" code="area.save"/>
	</jstl:when><jstl:otherwise>
		<acme:submit name="edit" code="area.edit"/>
		<jstl:if test="${delete}">
			<acme:submit name="delete" code="area.delete"/>
		</jstl:if>
	</jstl:otherwise>
	 </jstl:choose>
	 
	 <acme:cancel url="area/administrator/showAreas.do" code="area.cancel"/>
</form:form>






</security:authorize>
