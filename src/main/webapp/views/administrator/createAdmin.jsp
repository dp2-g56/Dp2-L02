<%@page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>

<%@taglib prefix="jstl"	uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="security" uri="http://www.springframework.org/security/tags"%>
<%@taglib prefix="display" uri="http://displaytag.sf.net"%>
<%@taglib prefix="acme" tagdir="/WEB-INF/tags" %>

<p><spring:message code="admin.createAdmin" /></p>

<security:authorize access="hasRole('ADMIN')">

<script type="text/javascript">

  function phonenumberval() {
	  
  var phoneNumber;
  phoneNumber = document.getElementById("phoneNumber").value;

		
  var res = false;
 
  if (/(\+[0-9]{1,3})(\([0-9]{1,3}\))([0-9]{4,})$/.test(phoneNumber)) {
    res = true;
  }
  if (/(\+[0-9]{3})([0-9]{4,})$/.test(phoneNumber)) {
	    res = true;
  }
  if(phoneNumber == ""){
	  alert("<spring:message code="admin.alertSave" />");
  }
  if(res == false && phoneNumber != "") {
	  
    confirm("<spring:message code="admin.confirmationPhone" />");
  }
 
}
   </script>
<form:form modelAttribute="formObjectMember" action="administrator/createAdmin.do">

	<!-- ELECCIÓN DEL FORMATO DE LA FECHA -->
	<jstl:if test="${locale =='EN'}">
		<jstl:set var="url" value ="anonymous/termsAndConditionsEN.do"/>		
	</jstl:if>
	
	<jstl:if test="${locale =='ES'}">
		<jstl:set var="url" value ="anonymous/termsAndConditionsES.do"/>
	</jstl:if>
	

	<!-- User Account Attributes -->
	<fieldset>
    	<legend> <spring:message code="admin.userAccountData" /> </legend>
	
	<acme:textbox path="username" code="admin.username" />
	<br />
	
	<acme:password path="password" code="admin.password" />
	<br />
	
	<acme:password path="confirmPassword" code="admin.confirmPassword" />
	<br />
	
		</fieldset>
	<br />
	
	<!-- Actor Attributes -->
	<fieldset>
    	<legend> <spring:message code="admin.personalData" /> </legend>
	<acme:textbox path="name" code="admin.name" />
	<br />
	
	<acme:textbox path="middleName" code="admin.middleName" />
	<br />
	
	<acme:textbox path="surname" code="admin.surname" />
	<br />
	
	<acme:textbox path="photo" code="admin.photo" />
	<br />
	
	<acme:textbox path="email" code="admin.email" />
	<br />
	
	<acme:textbox path="phoneNumber" code="admin.phoneNumber" />
	<br />
	
	<acme:textbox path="address" code="admin.address" />
	<br />	
	</fieldset>
	<br />
	<br />
	
	<!-- TERMS AND CONDITIONS -->
	<fieldset>
    	<legend> <spring:message code="admin.termsAndConditions" /> </legend>
    
    <form:checkbox path="termsAndConditions" /> 
			<spring:message code="admin.acceptTemsConditions" />
					<a href="${url}" target="_blank"> 
							<spring:message code="admin.termsAndConditions" /> </a>
								<form:errors path="termsAndConditions" cssClass="error" />
	<br />
	</fieldset>
	<br />

	<!-- BOTONES -->	
	<input type="submit" name="save" value="<spring:message code="admin.save" />" 
	onclick="phonenumberval();validateEmail();"/> 
	
	<acme:cancel url="/" code="admin.cancel" /> 
	
	</form:form>
	
	
	
</security:authorize>