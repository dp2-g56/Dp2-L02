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
	
	<display:column property="ticker" titleKey="parade.ticker" />
	
	<display:column property="title" titleKey="parade.title" />
	
	<display:column property="description" titleKey="parade.description" />
	
	<display:column property="moment" titleKey="parade.moment" />
	
	<display:column property="rowNumber" titleKey="parade.rowNumber" />
	
	<display:column property="columnNumber" titleKey="parade.columnNumber" />
	
    
    <display:column titleKey="parade.isDraftMode">
		<jstl:if test="${row.isDraftMode}" >
				<spring:message code="parade.draftMode" />
		</jstl:if>
		<jstl:if test="${!row.isDraftMode}" >
				<spring:message code="parade.finalMode" />
		</jstl:if>
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
	
												
</display:table>

	</jstl:otherwise>

 </jstl:choose>
<br />

	<jstl:if test="${hasArea}">
		<a href="parade/brotherhood/create.do"><spring:message code="paradeAndFloat.create" /></a>
		
		</br>
		<a href="parade/brotherhood/createCheckbox.do"><spring:message code="parade.create" /></a>
	</jstl:if>

	
</security:authorize>