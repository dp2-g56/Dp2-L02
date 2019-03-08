<%@page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>


<%@taglib prefix="jstl"	uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="security" uri="http://www.springframework.org/security/tags"%>
<%@taglib prefix="display" uri="http://displaytag.sf.net"%>
<%@taglib prefix="acme"  tagdir="/WEB-INF/tags"%>

<p><spring:message code="mail.writeMessage" /></p>	

<security:authorize access="isAuthenticated()">



<form:form modelAttribute="messageTest" action="message/actor/edit.do">

	<form:hidden path="id"/>
	<form:hidden path="version"/>


	<acme:input code="mail.message.subject" path="subject"/>

	<acme:select items="${actors}" itemLabel="userAccount.username" code="mail.message.receiver" path="receiver"/>

	<acme:input code="mail.message.tags" path="tags"/>
	
	<acme:radio items="${priority}" itemsName="${priorityName}" code="mail.message.priority" path="priority"/>

	<acme:textarea code="mail.message" path="body"/>
	
	
	<jstl:if test="${messageTest.id == 0}">
	
    <acme:submit name="save" code="mail.message.send"/>
    
    </jstl:if>
    
    <jstl:if test="${messageTest.id != 0}">
    
    <acme:delete code="mail.message.delete" confirmationMessage="mail.message.confirmation"/>
    
    </jstl:if>
	
	
	

	

</form:form>

<acme:cancel url="/box/actor/list.do" code="mail.cancel"/>

</security:authorize>