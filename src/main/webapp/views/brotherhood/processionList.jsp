<%@page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>

<%@taglib prefix="jstl"	uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="security" uri="http://www.springframework.org/security/tags"%>
<%@taglib prefix="display" uri="http://displaytag.sf.net"%>


<p><spring:message code="brotherhood.procession.list" /></p>

<security:authorize access="hasRole('BROTHERHOOD')">
	
	<jstl:choose>
	
	<jstl:when test="${!hasArea}">
		<b>	<spring:message code="procession.selectArea"/>	</b>
	</jstl:when>
	
	<jstl:otherwise>
	
	<display:table
	pagesize="5" name="processions" id="row"
	requestURI="${requestURI}" >
	
	<display:column property="ticker" titleKey="procession.ticker" />
	
	<display:column property="title" titleKey="procession.title" />
	
	<display:column property="description" titleKey="procession.description" />
	
	<display:column property="moment" titleKey="procession.moment" />
	
	<display:column property="rowNumber" titleKey="procession.rowNumber" />
	
	<display:column property="columnNumber" titleKey="procession.columnNumber" />
	
    
    <display:column titleKey="procession.isDraftMode">
		<jstl:if test="${row.isDraftMode}" >
				<spring:message code="procession.draftMode" />
		</jstl:if>
		<jstl:if test="${!row.isDraftMode}" >
				<spring:message code="procession.finalMode" />
		</jstl:if>
	</display:column>
		
	<display:column titleKey="procession.floats">
        <jstl:set var="floatsSize" value="${row.floats.size()}" />
        <spring:url var="floatsUrl" value="/procession/brotherhood/float/list.do?processionId={processionId}">
              <spring:param name="processionId" value="${row.id}"/>
        </spring:url>
        <a href="${floatsUrl}">
              <spring:message var ="viewFloats1" code="procession.viewFloats" />
              <jstl:out value="${viewFloats1}(${floatsSize})" />    
        </a>
    </display:column>
    
    
    <display:column titleKey="procession.requests">
    	<jstl:if test="${!row.isDraftMode}">
    		<jstl:set var="requestsSize" value="${row.requests.size()}" />
    		
       		<spring:url var="requestsUrl" value="/procession/brotherhood/request/list.do?processionId={processionId}">
            	<spring:param name="processionId" value="${row.id}"/>
        	</spring:url>
        	
        	<a href="${requestsUrl}">
              <spring:message var ="viewRequests1" code="procession.viewRequests" />
              <jstl:out value="${viewRequests1}(${requestsSize})" />    
        	</a>
        	
    	</jstl:if>
    </display:column>

	
	<display:column>
		<jstl:if test="${row.isDraftMode}">
			<a href="procession/brotherhood/editCheckbox.do?processionId=${row.id}">
				<spring:message code="procession.edit" />
			</a>
		</jstl:if>
	</display:column>
	
												
</display:table>

	</jstl:otherwise>

 </jstl:choose>
<br />

	<jstl:if test="${hasArea}">
		<a href="procession/brotherhood/create.do"><spring:message code="processionAndFloat.create" /></a>
		
		</br>
		<a href="procession/brotherhood/createCheckbox.do"><spring:message code="procession.create" /></a>
	</jstl:if>

	
</security:authorize>