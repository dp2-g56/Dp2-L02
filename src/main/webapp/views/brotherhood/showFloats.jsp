<%@page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>

<%@taglib prefix="jstl"	uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="security" uri="http://www.springframework.org/security/tags"%>
<%@taglib prefix="display" uri="http://displaytag.sf.net"%>
<%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>


<p><spring:message code="brotherhood.float.list" /></p>

<security:authorize access="hasRole('BROTHERHOOD')">
	
	<jstl:choose>
	
	<jstl:when test="${!hasArea}">
		<b>	<spring:message code="float.selectArea"/>	</b>
	</jstl:when>
	
	<jstl:otherwise>
	
	<display:table
	pagesize="5" name="allFloats" id="row"
	requestURI="${requestURI}" >
	
	<display:column property="title" titleKey="float.title" />
	
	<display:column property="description" titleKey="float.description" />
		
	<display:column titleKey="float.pictures">
        <jstl:set var="picturesSize" value="${row.pictures.size()}" />
        <spring:url var="picturesURL" value="/float/brotherhood/picture/list.do?floatId={floatId}&parade={parade}">      		
        	<spring:param name="floatId" value="${row.id}"/>
        	<spring:param name="parade" value="${!restriction}"/>
        </spring:url>
        <a href="${picturesURL}">
              <spring:message var ="viewPictures1" code="float.viewPictures" />
              <jstl:out value="${viewPictures1}(${picturesSize})" />    
        </a>
    </display:column>
  	
  	<jstl:if test="${!restriction}">
  		<display:column>	
			<jstl:if test="${not fn:containsIgnoreCase(floatFinalMode, row)}">
				<a href="float/brotherhood/edit.do?floatId=${row.id}">
					<spring:message code="float.edit" />
				</a>
		</jstl:if>
	</display:column>
	</jstl:if>
	
												
</display:table>

	</jstl:otherwise>

 </jstl:choose>
<br />
	
	<jstl:if test="${!restriction}">
		<jstl:if test="${hasArea}">
			<a href="float/brotherhood/create.do"><spring:message code="float.create" /></a>
		</jstl:if>
	</jstl:if>
	<jstl:if test="${restriction}">
		<a href="parade/brotherhood/list.do"><spring:message code="parade.back" /></a>
	</jstl:if>
	
</security:authorize>