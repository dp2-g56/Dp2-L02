<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
    
<%@taglib prefix="jstl"	uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="security" uri="http://www.springframework.org/security/tags"%>
<%@taglib prefix="display" uri="http://displaytag.sf.net"%>
<%@ taglib prefix="acme" tagdir="/WEB-INF/tags"%>



<br/>
<security:authorize access="hasAnyRole('ADMIN', 'MEMBER', 'SPONSOR')">

	<table>
		<tr>
			<td><spring:message code="actor.fullName" /></td>
			<td><jstl:out
					value="${actor.name} ${actor.middleName} ${actor.surname}" /></td>
		</tr>

		<tr>
			<td><spring:message code="actor.photo" /></td>
			<td><jstl:out value="${actor.photo}" /></td>
		</tr>

		<tr>
			<td><spring:message code="actor.email" /></td>
			<td><jstl:out value="${actor.email}" /></td>
		</tr>

		<tr>
			<td><spring:message code="actor.phoneNumber" /></td>
			<td><jstl:out value="${actor.phoneNumber}" /></td>
		</tr>

		<tr>
			<td><spring:message code="actor.address" /></td>
			<td><jstl:out value="${actor.address}" /></td>
		</tr>



	</table>

	<security:authorize access="hasAnyRole('ADMIN')">
		<acme:cancel url="/administrator/export.do?id=${actor.id}"
			code="export" />
	</security:authorize>

	<security:authorize access="hasAnyRole('MEMBER')">
		<acme:cancel url="/brotherhood/member/export.do?id=${actor.id}"
			code="export" />
	</security:authorize>

	<security:authorize access="hasAnyRole('SPONSOR')">
		<acme:cancel url="/sponsorship/sponsor/export.do?id=${actor.id}"
			code="export" />
	</security:authorize>

	<h2>
		<spring:message code="socialProfile.mySocialProfiles" />
	</h2>

	<display:table pagesize="5" name="socialProfiles" id="socialProfile"
		requestURI="${requestURI}">

		<display:column property="nick" titleKey="socialProfile.nick" />

		<display:column property="name" titleKey="socialProfile.name" />
	

		<display:column property="profileLink"
			titleKey="socialProfile.profileLink" />

		<display:column>

			<a
				href="authenticated/socialProfile/edit.do?socialProfileId=${socialProfile.id}">
				<spring:message code="socialProfile.edit" />
			</a>

		</display:column>


	</display:table>

	<a href="authenticated/socialProfile/create.do"><spring:message
			code="socialProfile.create" /></a>
<a href="authenticated/socialProfile/create.do"><spring:message code="socialProfile.create" /></a>


</security:authorize>



<security:authorize access="hasRole('BROTHERHOOD')">


<table>
	<tr>
		<td><spring:message code="actor.fullName" /></td> 
		<td><jstl:out value="${broherhood.name} ${broherhood.middleName} ${broherhood.surname}" /> </td>
	</tr>
	
	<tr>
		<td><spring:message code="actor.photo"/></td> 
		<td><jstl:out value="${broherhood.photo}"/> </td>
	</tr>
	
	<tr>
		<td><spring:message code="actor.email"/></td> 
		<td><jstl:out value="${broherhood.email}" /> </td>
	</tr>
	
	<tr>
		<td><spring:message code="actor.address"/></td> 
		<td><jstl:out value="${broherhood.address}" /> </td>
	</tr>
	 
	<tr>
		<td><spring:message code="actor.phoneNumber"/></td> 
		<td><jstl:out value="${broherhood.phoneNumber}" /> </td>
	</tr>
	
	<tr>
		<td><spring:message code="actor.title"/></td> 
		<td><jstl:out value="${broherhood.title}" /> </td>
	</tr>
	
	<tr>
		<td><spring:message code="actor.establishmentDate"/></td> 
		<td><jstl:out value="${broherhood.establishmentDate}" /> </td>
	</tr>
	
	
	<tr>
		<td>
	<jstl:set var="picturesSize" value="${brotherhood.pictures.size()}" />
			<spring:url var="picturesUrl"
				value="/authenticated/picture/list.do?brotherhoodId={broId}">
				<spring:param name="broId" value="${broherhood.id}" />
			</spring:url>
			<a href="${picturesUrl}"> <spring:message var="viewPictures1"
					code="brotherhood.view.pictures" /> <jstl:out
					value="${viewPictures1}(${picturesSize})" />
			</a></td> 
	</tr>

	<table>
		<tr>
			<td><spring:message code="actor.fullName" /></td>
			<td><jstl:out
					value="${broherhood.name} ${broherhood.middleName} ${broherhood.surname}" />
			</td>
		</tr>

		<tr>
			<td><spring:message code="actor.photo" /></td>
			<td><jstl:out value="${broherhood.photo}" /></td>
		</tr>

		<tr>
			<td><spring:message code="actor.email" /></td>
			<td><jstl:out value="${broherhood.email}" /></td>
		</tr>

		<tr>
			<td><spring:message code="actor.phoneNumber" /></td>
			<td><jstl:out value="${broherhood.phoneNumber}" /></td>
		</tr>

		<tr>
			<td><spring:message code="actor.title" /></td>
			<td><jstl:out value="${broherhood.title}" /></td>
		</tr>

		<tr>
			<td><spring:message code="actor.establishmentDate" /></td>
			<td><jstl:out value="${broherhood.establishmentDate}" /></td>
		</tr>


		<tr>
			<td><jstl:set var="picturesSize"
					value="${brotherhood.pictures.size()}" /> <spring:url
					var="picturesUrl"
					value="/authenticated/picture/list.do?brotherhoodId={broId}">
					<spring:param name="broId" value="${broherhood.id}" />
				</spring:url> <a href="${picturesUrl}"> <spring:message var="viewPictures1"
						code="brotherhood.view.pictures" /> <jstl:out
						value="${viewPictures1}(${picturesSize})" />
			</a></td>
		</tr>



	</table>

	<acme:cancel url="/float/brotherhood/export.do?id=${broherhood.id}"
		code="export" />

	<h2>
		<spring:message code="socialProfile.mySocialProfiles" />
	</h2>

	<display:table pagesize="5" name="socialProfiles" id="socialProfile"
		requestURI="${requestURI}">

		<display:column property="nick" titleKey="socialProfile.nick" />

		<display:column property="name" titleKey="socialProfile.name" />

		<display:column property="profileLink"
			titleKey="socialProfile.profileLink" />

		<display:column>

			<a
				href="authenticated/socialProfile/edit.do?socialProfileId=${socialProfile.id}">
				<spring:message code="socialProfile.edit" />
			</a>

		</display:column>


	</display:table>

	<a href="authenticated/socialProfile/create.do"><spring:message
			code="socialProfile.create" /></a>

</security:authorize>

<security:authorize access="hasRole('CHAPTER')">



	<table>
		<tr>
			<td><spring:message code="actor.fullName" /></td>
			<td><jstl:out
					value="${chapter.name} ${chapter.middleName} ${chapter.surname}" />
			</td>
		</tr>

		<tr>
			<td><spring:message code="actor.photo" /></td>
			<td><jstl:out value="${chapter.photo}" /></td>
		</tr>

		<tr>
			<td><spring:message code="actor.email" /></td>
			<td><jstl:out value="${chapter.email}" /></td>
		</tr>

		<tr>
			<td><spring:message code="actor.phoneNumber" /></td>
			<td><jstl:out value="${chapter.phoneNumber}" /></td>
		</tr>

		<tr>
			<td><spring:message code="actor.title" /></td>
			<td><jstl:out value="${chapter.title}" /></td>
		</tr>

	</table>
	
	<acme:cancel url="/chapter/export.do?id=${chapter.id}"
		code="export" />

	<h2>
		<spring:message code="socialProfile.mySocialProfiles" />
	</h2>

	<display:table pagesize="5" name="socialProfiles" id="socialProfile"
		requestURI="${requestURI}">

		<display:column property="nick" titleKey="socialProfile.nick" />

		<display:column property="name" titleKey="socialProfile.name" />

		<display:column property="profileLink"
			titleKey="socialProfile.profileLink" />

		<display:column>

			<a
				href="authenticated/socialProfile/edit.do?socialProfileId=${socialProfile.id}">
				<spring:message code="socialProfile.edit" />
			</a>
	
</display:table>

		</display:column>
<a href="authenticated/socialProfile/create.do"><spring:message code="socialProfile.create" /></a>

<br />
<h2> <spring:message code="history.myHistory"  /></h2>

	<!-- SI TIENE UN HISTORY MUESTRA SUS RECORDS -->
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
							value="authenticated/inceptionRecord/photo/list.do">
						
					</spring:url>
					
					<a href="${inceptionRecordUrl}"> <spring:message var="viewPhotos1"
						code="inceptionRecord.viewPhotos" /> <jstl:out
						value="${viewPhotos1}(${photosSize})" />
					</a>
		
			</display:column>
		
			<display:column>
				<a href="authenticated/inceptionRecord/edit.do">
					<spring:message code="inceptionRecord.edit" />
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
							value="authenticated/periodRecord/photo/list.do?periodRecordId=${periodRecord.id}">
							
					</spring:url>
					
					<a href="${periodRecordUrl}"> <spring:message var="viewPhotos"
						code="periodRecord.viewPhotos" /> <jstl:out
						value="${viewPhotos}(${photosSize})" />
					</a>
		
			</display:column>
		
			<display:column>
				<a href="authenticated/periodRecord/edit.do?periodRecordId=${periodRecord.id}">
					<spring:message code="periodRecord.edit" />
				</a>
			</display:column>
			
		</display:table>
		
		<a href="authenticated/periodRecord/create.do"><spring:message code="periodRecord.create" /></a>
	
	

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
							value="authenticated/legalRecord/law/list.do?legalRecordId=${legalRecord.id}">
					</spring:url>
					
					<a href="${legalRecordUrl}"> <spring:message var="viewLaws"
						code="legalRecord.viewLaws" /> <jstl:out
						value="${viewLaws}(${lawsSize})" />
					</a>
		
			</display:column>
		
			<display:column>
				<a href="authenticated/legalRecord/edit.do?legalRecordId=${legalRecord.id}">
					<spring:message code="legalRecord.edit" />
				</a>
			</display:column>
			
		</display:table>
		
		<a href="authenticated/legalRecord/create.do"><spring:message code="legalRecord.create" /></a>
		
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
			
			<display:column>	
				<a href="authenticated/linkRecord/edit.do?linkRecordId=${linkRecord.id}">
					<spring:message code="linkRecord.edit" />
				</a>
			</display:column>
			
		</display:table>
		
		<a href="authenticated/linkRecord/create.do"><spring:message code="linkRecord.create" /></a>
		
		<h3> <spring:message code="history.miscellaneousRecord"  /></h3>
		<display:table
			pagesize="5" name="miscellaneousRecords" id="miscellaneousRecord"
			requestURI="${requestURI}">
			
			<display:column property="title" titleKey="miscellaneousRecord.title" />
			
			<display:column property="description" titleKey="miscellaneousRecord.description" />
						
			<display:column>
				
				<a href="authenticated/miscellaneousRecord/edit.do?miscellaneousRecordId=${miscellaneousRecord.id}">
					<spring:message code="miscellaneousRecord.edit" />
				</a>
			
			</display:column>
			
		</display:table>
		
		<a href="authenticated/miscellaneousRecord/create.do"><spring:message code="miscellaneousRecord.create" /></a>


</jstl:when>
<jstl:otherwise>		<!-- SI NO TIENE UN HISTORY LE PERMITE CREAR UNO -->

			<a href="authenticated/inceptionRecord/create.do"><spring:message code="inceptionRecord.create" /></a>
	
</jstl:otherwise>
	
</jstl:choose>		

</security:authorize>
