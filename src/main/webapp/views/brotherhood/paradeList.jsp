<%@page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>

<%@taglib prefix="jstl"	uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="security" uri="http://www.springframework.org/security/tags"%>
<%@taglib prefix="display" uri="http://displaytag.sf.net"%>


<p><spring:message code="brotherhood.parade.list" /></p>

<security:authorize access="hasRole('BROTHERHOOD')">
	
	<jstl:choose>
	
	<jstl:when test="${!hasArea}">
		<b>	<spring:message code="parade.selectArea"/>	</b>
	</jstl:when>
	
	<jstl:otherwise>
	
	<display:table
	pagesize="5" name="parades" id="row"
	requestURI="${requestURI}" >
	
	
	
	<jstl:choose>
				<jstl:when test="${row.paradeStatus.toString() == 'SUBMITTED'}">
		
					<jstl:set var="color" value="grey" />
					<jstl:if test="${locale=='es'}">
					<jstl:set var="statusName" value="PRESENTADO" />
					</jstl:if>
		
				</jstl:when>
	
				<jstl:when test="${row.paradeStatus.toString() == 'REJECTED'}">
		
					<jstl:set var="color" value="red" />
					<jstl:if test="${locale=='es'}">
					<jstl:set var="statusName" value="RECHAZADO" />
					</jstl:if>
		
				</jstl:when>
		
				<jstl:when test="${row.paradeStatus.toString() == 'ACCEPTED'}">
		
					<jstl:set var="color" value="green" />
					<jstl:if test="${locale=='es'}">
					<jstl:set var="statusName" value="ACEPTADO" />
					</jstl:if>
		
				</jstl:when>
		
			</jstl:choose>
	
	<display:column titleKey="parade.ticker" >
		<font color="${color}"><jstl:out value="${row.ticker} "/> </font>
	</display:column>
	
	<display:column titleKey="parade.title">
			<font color="${color}"><jstl:out value="${row.title} "/> </font>
	</display:column>
	
	<display:column titleKey="parade.description" >
			<font color="${color}"><jstl:out value="${row.description} "/> </font>
	</display:column>
	
	<display:column titleKey="parade.moment" >
		<font color="${color}"><jstl:out value="${row.moment} "/> </font>
	</display:column>
	
	<display:column titleKey="parade.rowNumber" >
		<font color="${color}"><jstl:out value="${row.rowNumber} "/> </font>
	</display:column>
	
	<display:column  titleKey="parade.columnNumber" >
			<font color="${color}"><jstl:out value="${row.columnNumber} "/> </font>
	</display:column>
	
    
    <display:column titleKey="parade.isDraftMode">
		<jstl:if test="${row.isDraftMode}" >
			<font color="${color}">	<spring:message code="parade.draftMode" /> </font>
		</jstl:if>
		<jstl:if test="${!row.isDraftMode}" >
			<font color="${color}">	<spring:message code="parade.finalMode" /> </font>
		</jstl:if>
	</display:column>
	
	<display:column titleKey="parade.paradeStatus" >
		<jstl:choose>
			<jstl:when test="${locale=='es'}">
						<font color="${color}"><jstl:out value="${statusName} "/> </font>
			</jstl:when>
			<jstl:otherwise>
						<font color="${color}"><jstl:out value="${row.paradeStatus} "/> </font>
			</jstl:otherwise>
		
		</jstl:choose>

	</display:column>
	
		
	<display:column titleKey="parade.floats">
        <jstl:set var="floatsSize" value="${row.floats.size()}" />
        <spring:url var="floatsUrl" value="/parade/brotherhood/float/list.do?paradeId={paradeId}">
              <spring:param name="paradeId" value="${row.id}"/>
        </spring:url>
        <a href="${floatsUrl}">
              <spring:message var ="viewFloats1" code="parade.viewFloats" />
              <jstl:out value="${viewFloats1}(${floatsSize})" />    
        </a>
    </display:column>
    
    
    <display:column titleKey="parade.requests">
    	<jstl:if test="${!row.isDraftMode}">
    		<jstl:set var="requestsSize" value="${row.requests.size()}" />
    		
       		<spring:url var="requestsUrl" value="/parade/brotherhood/request/list.do?paradeId={paradeId}">
            	<spring:param name="paradeId" value="${row.id}"/>
        	</spring:url>
        	
        	<a href="${requestsUrl}">
              <spring:message var ="viewRequests1" code="parade.viewRequests" />
              <jstl:out value="${viewRequests1}(${requestsSize})" />    
        	</a>
        	
    	</jstl:if>
    </display:column>

	
	<display:column>
		<jstl:if test="${row.isDraftMode}">
			<a href="parade/brotherhood/editCheckbox.do?paradeId=${row.id}">
				<spring:message code="parade.edit" />
			</a>
		</jstl:if>
	</display:column>
	
		<display:column>
		
		<jstl:choose>
		<jstl:when test="${row.path==null}">
			<a href="path/brotherhood/create.do?paradeId=${row.id}">
				<spring:message code="parade.path.create" />
			</a>
		</jstl:when>
		<jstl:otherwise>
			<a href="path/brotherhood/list.do?paradeId=${row.id}">
				<spring:message code="parade.path" />
			</a>
		</jstl:otherwise>
		</jstl:choose>
	</display:column>
	
												
</display:table>

	</jstl:otherwise>

 </jstl:choose>
<br />

	<jstl:if test="${hasArea}">
		<a href="parade/brotherhood/create.do"><spring:message code="paradeAndFloat.create" /></a>
		
		<br/>
		<a href="parade/brotherhood/createCheckbox.do"><spring:message code="parade.create" /></a>
	</jstl:if>

	
</security:authorize>