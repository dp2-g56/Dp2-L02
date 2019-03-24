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



	<display:table pagesize="5" name="brotherhoods" id="row"
		class="displaytag" requestURI="${requestURI}">

		<display:column property="title" titleKey="brotherhood.title" />

		<display:column property="establishmentDate"
			titleKey="brotherhood.establishmentDate" sortable="true"
			format="{0,date,dd/MM/yyyy HH:mm}" />

		<display:column titleKey="brotherhood.pictures">
			<jstl:set var="picturesSize" value="${row.pictures.size()}" />
			<spring:url var="picturesUrl"
				value="/showAll/annonymous/pictureBrother/list.do?brotherhoodId={broId}">
				<spring:param name="broId" value="${row.id}" />
			</spring:url>
			<a href="${picturesUrl}"> <spring:message var="viewPictures1"
					code="brotherhood.view.pictures" /> <jstl:out
					value="${viewPictures1}(${picturesSize})" />
			</a>
		</display:column>
		
		
		
		<display:column>
			<spring:url var="createUrl0"
				value="/showAll/annonymous/history/list.do?brotherhoodId={brotherhoodId}">
				<spring:param name="brotherhoodId" value="${row.id}" />
			</spring:url>
				<a href="${createUrl0}"><spring:message code="annonymous.histories"/></a>
		</display:column>
		
		
		<display:column>
			<spring:url var="createUrl1"
				value="/showAll/annonymous/parade/list.do?brotherhoodId={brotherhoodId}">
				<spring:param name="brotherhoodId" value="${row.id}" />
			</spring:url>
				<a href="${createUrl1}"><spring:message code="annonymous.parades"/></a>
		</display:column>
		
		<display:column>
			<spring:url var="createUrl2"
				value="/showAll/annonymous/member/list.do?brotherhoodId={brotherhoodId}">
				<spring:param name="brotherhoodId" value="${row.id}" />
			</spring:url>
				<a href="${createUrl2}"><spring:message code="annonymous.members"/></a>
		</display:column>
		
		<display:column>
			<spring:url var="createUrl3"
				value="/showAll/annonymous/float/list.do?brotherhoodId={brotherhoodId}">
				<spring:param name="brotherhoodId" value="${row.id}" />
			</spring:url>
				<a href="${createUrl3}"><spring:message code="annonymous.floats"/></a>
		</display:column>
		
		</display:table>
		
		<jstl:if test="${cancelButton}">
			<spring:url var="cancelar" value="/showAll/annonymous/brotherhood/list.do"/>
			<p><a href="${cancelar}"><spring:message code="annonymous.cancel"/></a></p>
		</jstl:if>

