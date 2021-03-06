<%@page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="security"
	uri="http://www.springframework.org/security/tags"%>
<%@taglib prefix="display" uri="http://displaytag.sf.net"%>
<%@taglib prefix="acme" tagdir="/WEB-INF/tags"%>

<p>
	<spring:message code="annonymous.brotherhoods" />
</p>

<display:table
	pagesize="5" name="parades" id="row"
	requestURI="${requestURI}">
	
	<display:column property="ticker" titleKey="parade.ticker" />
	
	<display:column property="title" titleKey="parade.title" />
	
	<display:column property="description" titleKey="parade.description" />
	
	<display:column property="moment" titleKey="parade.moment" />
	
	<display:column property="rowNumber" titleKey="parade.rowNumber" />
	
	<display:column property="columnNumber" titleKey="parade.columnNumber" />
	
	<display:column titleKey="parade.path" > 
	
	<jstl:choose>
		<jstl:when test="${row.path==null}">
			N/A
		</jstl:when>
		<jstl:otherwise>
		
		<spring:url value="showAll/annonymous/path/list.do" var="pathUrl">
			<spring:param name="paradeId" value="${row.id}"/>
			<spring:param name="brotherhoodId" value="${param.brotherhoodId}"/>
		</spring:url>
			<a href="${pathUrl}">
				<spring:message code="parade.path" />
			</a>
		</jstl:otherwise>
		</jstl:choose>
	
	</display:column>
	
	<security:authorize access="hasRole('SPONSOR')">
		<display:column titleKey="sponsorship.create.action">
			<spring:url var="createSponsorship" value="/sponsorship/sponsor/create.do">
				<spring:param name="paradeId" value="${row.id}" />
			</spring:url>
			<a href="${createSponsorship}">
				<spring:message code="parade.create.sponsorship" />				
			</a>
		</display:column>
	</security:authorize>
	
	<display:column titleKey="parade.sponsorship">
		<jstl:if test="${randomSpo.get(row.id).id>0}">
			<a href="${randomSpo.get(row.id).targetURL}"><img src="${randomSpo.get(row.id).banner}" style="width:auto; height:50px;" alt="<spring:message code='parade.sponsorship'/>"/></a>
		</jstl:if>
	</display:column>
	
</display:table>	

<spring:url var="cancelar" value="/showAll/annonymous/brotherhood/list.do"/>

<p><a href="${cancelar}"><spring:message code="annonymous.cancel"/></a></p>