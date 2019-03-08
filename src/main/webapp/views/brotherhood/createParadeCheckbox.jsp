<%@page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>

<%@taglib prefix="jstl"	uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="security" uri="http://www.springframework.org/security/tags"%>
<%@taglib prefix="display" uri="http://displaytag.sf.net"%>
<%@taglib prefix="acme" tagdir="/WEB-INF/tags" %>

<p><spring:message code="parade.create" /></p>



<form:form action="parade/brotherhood/editCheckbox.do" modelAttribute="formObjectParadeFloatCheckbox" >



<fieldset>
  <legend> <spring:message code="parade.data" /> </legend>
  	
  	<form:hidden path ="id"/>

	<acme:textbox code="parade.title" path="titleParade"/>	
	<br />
	
	<acme:textarea code="parade.description" path="descriptionParade"/>	
	<br />
		
	<acme:datebox code="parade.moment" path="moment"/>	
	<br />
	
	<acme:boolean code="parade.isDraftMode" trueCode="parade.true" falseCode="parade.false" path="isDraftMode"/>	
	<br />
	
	<acme:input code="parade.rowNumber" path="rowNumber"/>	
	<br />
	
	<acme:input code="parade.columnNumber" path="columnNumber"/>	
	<br />
	
</fieldset>

<fieldset>
  <legend> <spring:message code="float.data" /> </legend>
  
	<acme:checkbox  path="floats" map="${map}" />
	

	
</fieldset>
	<br />
	
	<acme:submit code="float.createButton" name="save" />
	<br />
	<jstl:if test="${paradeId != 0 }">
 		<acme:submit name="delete" code="float.delete" />
 	</jstl:if> 
	<br />
</form:form> 
	
	<acme:cancel url="/parade/brotherhood/list.do" code="float.cancel" /> 
	
	