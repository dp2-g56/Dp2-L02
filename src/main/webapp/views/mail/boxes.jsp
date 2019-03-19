<%@page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>

<%@taglib prefix="jstl"	uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="security" uri="http://www.springframework.org/security/tags"%>
<%@taglib prefix="display" uri="http://displaytag.sf.net"%>

<p><spring:message code="mail.myBoxes" /></p>		

<security:authorize access="isAuthenticated()">


<display:table
	pagesize="10" name="boxes" id="row"
	requestURI="box/actor/list.do" >
	
	<display:column titleKey="mail.box.name">
		<spring:url var="showBox" value="/message/actor/list.do?boxId={boxId}">
			<spring:param name="boxId" value="${row.id}"/>
		</spring:url>
		
		<a href="${showBox}"><jstl:out value="${row.name}"/></a>
	</display:column>
		
	<display:column	titleKey="mail.messages">
		<jstl:out value="${row.messages.size()}" />
	</display:column>	
	
		
		<display:column>
			<jstl:if test="${!row.isSystem}">
				<spring:url var="editBox" value="/box/actor/edit.do?boxId={boxId}">
					<spring:param name="boxId" value="${row.id}"/>
				</spring:url>
		
				<a href="${editBox}"><spring:message code="mail.box.edit"/></a>
			</jstl:if>	
		</display:column>	
		
	
		<display:column	property ="fatherBox.name"
				titleKey="mail.box.fatherBox"/>	
			


															
</display:table>

<spring:url var="newBox" value="/box/actor/create.do"/>

<p><a href="${newBox}"><spring:message code="mail.box.new" /></a></p>

</security:authorize> 