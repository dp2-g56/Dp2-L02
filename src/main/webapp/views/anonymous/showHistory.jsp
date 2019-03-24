<%@page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="security"
	uri="http://www.springframework.org/security/tags"%>
<%@taglib prefix="display" uri="http://displaytag.sf.net"%>
<%@taglib prefix="acme" tagdir="/WEB-INF/tags"%>

<jstl:choose>
		<jstl:when test="${showHistory}">	
	
	<h3> <spring:message code="history.inceptionRecord"  /></h3>
	<display:table
			name="inceptionRecords" id="inceptionRecord"
			requestURI="${requestURI}">
			
			<display:column property="title" titleKey="inceptionRecord.title" />
			
			<display:column property="description" titleKey="inceptionRecord.description" />
			
			
			<display:column titleKey="inceptionRecord.photos">
			
					<jstl:set var="photosSize" value="${inceptionRecord.photos.size()}" />
					
					<spring:url var="inceptionRecordUrl"
							value="showAll/annonymous/history/inceptionPhotos/list.do?inceptionRecordId=${inceptionRecord.id}">
						
					</spring:url>
					
					<a href="${inceptionRecordUrl}"> <spring:message var="viewPhotos1"
						code="inceptionRecord.viewPhotos" /> <jstl:out
						value="${viewPhotos1}(${photosSize})" />
					</a>
		
			</display:column>
			
	</display:table>
	
	
	<h3> <spring:message code="history.periodRecord"  /></h3>
		<display:table
			pagesize="5" name="periodRecords" id="periodRecord"
			requestURI="${requestURI}">
	
			<display:column property="title" titleKey="periodRecord.title" />
			
			<display:column property="description" titleKey="periodRecord.description" />
				
			<display:column property="startYear" titleKey="periodRecord.startYear" />
			
			<display:column property="endYear" titleKey="periodRecord.endYear" />
			
			<display:column titleKey="periodRecord.photos">
			
					<jstl:set var="photosSize" value="${periodRecord.photos.size()}" />
					
					<spring:url var="periodRecordUrl"
							value="showAll/annonymous/history/periodPhotos/list.do?periodRecordId=${periodRecord.id}">
					</spring:url>
					
					<a href="${periodRecordUrl}"> <spring:message var="viewPhotos"
						code="periodRecord.viewPhotos" /> <jstl:out
						value="${viewPhotos}(${photosSize})" />
					</a>
		
			</display:column>
		
			
		</display:table>
	

	<h3> <spring:message code="history.legalRecord"  /></h3>
		<display:table
			pagesize="5" name="legalRecords" id="legalRecord"
			requestURI="${requestURI}">
	
			<display:column property="title" titleKey="legalRecord.title" />
			
			<display:column property="description" titleKey="legalRecord.description" />
			
			<display:column property="legalName" titleKey="legalRecord.legalName" />
			
			<display:column property="vatNumber" titleKey="legalRecord.vatNumber" />
			
			<display:column titleKey="legalRecord.laws">
			
					<jstl:set var="lawsSize" value="${legalRecord.laws.size()}" />
					
					<spring:url var="legalRecordUrl"
							value="showAll/annonymous/history/law/list.do?legalRecordId=${legalRecord.id}">
					</spring:url>
							
					
					<a href="${legalRecordUrl}"> <spring:message var="viewLaws"
						code="legalRecord.viewLaws" /> <jstl:out
						value="${viewLaws}(${lawsSize})" />
					</a>
		
			</display:column>
			
		</display:table>
		
		
	<h3> <spring:message code="history.linkRecord"  /></h3>
		<display:table
			pagesize="5" name="linkRecords" id="linkRecord"
			requestURI="${requestURI}">
	
			<display:column property="title" titleKey="linkRecord.title" />
			
			<display:column property="description" titleKey="linkRecord.description" />
			
			<display:column>
				
				<a href="${linkRecord.link}">
					<spring:message code="linkRecord.linkedBrotherhood" />
				</a>
				
			</display:column>
			
			
		</display:table>
				
		<h3> <spring:message code="history.miscellaneousRecord"  /></h3>
		<display:table
			pagesize="5" name="miscellaneousRecords" id="miscellaneousRecord"
			requestURI="${requestURI}">
			
			<display:column property="title" titleKey="miscellaneousRecord.title" />
			
			<display:column property="description" titleKey="miscellaneousRecord.description" />
					
		</display:table>
		


</jstl:when>
<jstl:otherwise>		<!-- SI NO TIENE UN HISTORY LE PERMITE CREAR UNO -->

		<spring:message code="anonymous.brotherhoodWithoutHistory" />
</jstl:otherwise>
	
</jstl:choose>	

<spring:url var="cancelar" value="/showAll/annonymous/brotherhood/list.do"/>

<p><a href="${cancelar}"><spring:message code="annonymous.cancel"/></a></p>


