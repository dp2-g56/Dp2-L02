<%@page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>

<%@taglib prefix="jstl"	uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="security" uri="http://www.springframework.org/security/tags"%>
<%@taglib prefix="display" uri="http://displaytag.sf.net"%>

<p><spring:message code="mail.myMessages" /></p>

<security:authorize access="isAuthenticated()">


<display:table
	pagesize="4" name="messages" id="row"
	requestURI="message/actor/list.do" >
	
	<!-- Date -->
	<display:column	property ="moment"
					titleKey="mail.message.moment"/>
	
	
	<display:column	titleKey="mail.message.subject">
	
		<spring:url var="showMessage" value="/message/actor/edit.do?rowId={rowId}&boxId=${boxId}">
			<spring:param name="rowId" value="${row.id}"/>
		</spring:url>
	
		<a href="${showMessage}">
			<jstl:out value="${row.subject}" />
		</a>
	</display:column>
	
	<display:column	property ="tags"
				titleKey="mail.message.tags"/>
							
	<display:column	titleKey="mail.message.sender">
		<jstl:out value="${row.sender.userAccount.username}" />
	</display:column>	
	
	<display:column	titleKey="mail.message.receiver">
		<jstl:out value="${row.receiver.userAccount.username}" />
	</display:column>
	
	<display:column titleKey="mail.message.priority" >
	<jstl:choose>
		<jstl:when test="${locale=='en'}">
			<jstl:out value="${row.priority}" />
		</jstl:when>
		<jstl:when test="${locale=='es'}">
			<jstl:out value="${priorityName.get(priority.lastIndexOf(row.priority))}" />
		</jstl:when>
			
	</jstl:choose>
	</display:column>
	
	<display:column titleKey="mail.message.move">
	
<jstl:forEach items="${boxes}" var="box">
 
 	<spring:url var="moveMessage" value="/message/actor/move.do?messageId=${row.id}&boxId=${box.id}">
			<spring:param name="messageId" value="${row.id}"/>
	</spring:url>
	
 
 		<a href="${moveMessage}">
			<jstl:out value="${box.name} " />
			<br />
		</a>
		
	</jstl:forEach>
 

	</display:column>	
	
	<display:column titleKey="mail.message.copy">
	
<jstl:forEach items="${boxes}" var="box">
 
 	<spring:url var="copyMessage" value="/message/actor/copy.do?messageId=${row.id}&boxId=${box.id}">
			<spring:param name="messageId" value="${row.id}"/>
	</spring:url>
	
 
 		<a href="${copyMessage}">
			<jstl:out value="${box.name} " />
			<br />
		</a>
		
	</jstl:forEach>
 

	</display:column>	
	
	
	

															
</display:table>

<!-- Enlaces parte inferior -->
<spring:url var="newMessage" value="/message/actor/create.do"/>

<p><a href="${newMessage}"><spring:message code="mail.message.new" /></a></p>

<spring:url var="mail" value="/box/actor/list.do"/>

<p><a href="${mail}"><spring:message code="mail.back" /></a></p>

</security:authorize>