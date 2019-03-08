<%@page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>

<%@taglib prefix="jstl"	uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="security" uri="http://www.springframework.org/security/tags"%>
<%@taglib prefix="display" uri="http://displaytag.sf.net"%>


<jstl:choose>
	<jstl:when test="${hasArea}">
		<display:table pagesize="5" name="areas" id="row" requestURI="${requestURI}">
		
			<security:authorize access="hasRole('ADMIN')">
				<display:column >
					<spring:url var="editAreaUrl" value="area/administrator/edit.do">
						<spring:param name="areaId" value="${row.id}"/>
					</spring:url>
					<a href="${editAreaUrl}">
						<spring:message code="area.edit" />			
					</a>
				</display:column>
			</security:authorize>
		
			<display:column property="name" titleKey="area.name" sortable="true">
			</display:column>
			
			<display:column titleKey="area.pictures">
				<jstl:set var="picturesSize" value="${row.pictures.size()}" />
				
				<security:authorize access="hasRole('BROTHERHOOD')">
					<spring:url var="picturesUrl" value="area/brotherhood/showPictures.do">
						<spring:param name="areaId" value="${row.id}"/>
					</spring:url>
				</security:authorize>
				<security:authorize access="hasRole('ADMIN')">
					<spring:url var="picturesUrl" value="area/administrator/showPictures.do">
						<spring:param name="areaId" value="${row.id}"/>
					</spring:url>
				
				</security:authorize>
				<a href="${picturesUrl}">
					<spring:message var ="viewPic" code="area.pictures" />
					<jstl:out value="${viewPic}(${picturesSize})" />		
				</a>
			</display:column>

		</display:table>
	
	</jstl:when><jstl:otherwise>
		<security:authorize access="hasRole('BROTHERHOOD')">
			<spring:url var="selectArea" value="area/brotherhood/selectArea.do"/>
			<a href="${selectArea}">
				<spring:message var ="select" code="area.select" />
				<jstl:out value="${select}" />		
			</a>
		</security:authorize>
	</jstl:otherwise>
</jstl:choose>

<security:authorize access="hasRole('ADMIN')">

	<spring:url var="createAreaUrl" value="area/administrator/create.do"/>
	<a href="${createAreaUrl}">
		<spring:message code="area.create" />			
	</a>
					
</security:authorize>