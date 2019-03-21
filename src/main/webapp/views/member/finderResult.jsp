<%@page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>

<%@taglib prefix="jstl"	uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="security" uri="http://www.springframework.org/security/tags"%>
<%@taglib prefix="display" uri="http://displaytag.sf.net"%>
<%@taglib prefix="acme"  tagdir="/WEB-INF/tags"%>

<p><spring:message code="member.finder" /></p>

<security:authorize access="hasRole('MEMBER')">

	<form:form action="finder/member/clean.do">
	
		<acme:submit name="save" code="finder.cleanFilter"/>
			
	</form:form>
	
	<spring:url var="finderUrl" value="/finder/member/edit.do" />
	
	<a href="${finderUrl}">
	<button type="button" ><spring:message code="finder.edit" /></button>	
	</a>

	<display:table pagesize="5" name="parades" id="row" class="displaytag" 
					requestURI="/finder/member/list.do">
					
	<display:column titleKey="parade.request" >
		<jstl:set var="hasRequest" value="${0}"/>
		<jstl:forEach
			var="request"
			items="${row.requests}">
			<jstl:set var="counts" value="0"/>
			<jstl:if test="${request.getMember().equals(member)}">	
			<jstl:set var="counts" value="${counts+1}"/>
			<jstl:set var="hasRequest" value="${counts}"/>
			</jstl:if>
		</jstl:forEach>
		
		<jstl:if test="${hasRequest==0}">
		<%--
			<form name="newRequest" id="newRequest" action="request/member/create.do" method="post">
				<input type="hidden" name="paradeId" id="paradeId" value="${row.id}"/>
				<acme:submit name="saveRequest" code="request.create"/>
			</form>
		--%>
			
			<spring:url var="createRequest" value="/request/member/create.do">
				<spring:param name="paradeId" value="${row.id}" />
			</spring:url>
			<a href="${createRequest}" onclick="return confirm('<spring:message code="request.create.confirmation" />')">
				<spring:message code="request.create" />				
			</a>
			
		</jstl:if>
	</display:column> 
	
	<display:column property="ticker" titleKey="parade.ticker" /> 
		
	<display:column property="title" titleKey="parade.title" /> 
	
	<display:column property="description" titleKey="parade.description" /> 
	
	<display:column property="moment" titleKey="parade.moment" /> 
	
	<display:column titleKey="parade.path" > 
	
	<jstl:choose>
		<jstl:when test="${row.path==null}">
			N/A
		</jstl:when>
		<jstl:otherwise>
		
		<spring:url value="showAll/annonymous/path/list.do" var="pathUrl">
			<spring:param name="paradeId" value="${row.id}"/>
			<spring:param name="finder" value="yes"/>
		</spring:url>
			<a href="${pathUrl}">
				<spring:message code="parade.path" />
			</a>
		</jstl:otherwise>
		</jstl:choose>
	
	</display:column>
	
	<display:column titleKey="parade.sponsorship">
		<jstl:if test="${randomSpo.get(row.id).id>0}">
			<a href="${randomSpo.get(row.id).targetURL}"><img src="${randomSpo.get(row.id).banner}" style="width:auto; height:50px;" alt="<spring:message code='parade.sponsorship'/>"/></a>
		</jstl:if>
	</display:column>
	
	</display:table>
	
	<jstl:if test="${flag==false}"> 
		<p style="color:red"><spring:message code="request.create.error"/></p>
	</jstl:if>
	<jstl:if test="${flag==true}">
		<p style="color:red"><spring:message code="request.create.ok"/></p>
	</jstl:if>

</security:authorize>
