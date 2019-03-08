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
	<spring:message code="member.brotherhoods" />
</p>

<security:authorize access="hasRole('MEMBER')">

	<display:table pagesize="5" name="brotherhoods" id="row"
		class="displaytag" requestURI="brotherhood/member/list.do">

		<display:column property="title" titleKey="brotherhood.title" />

		<display:column property="establishmentDate"
			titleKey="brotherhood.establishmentDate" sortable="true"
			format="{0,date,dd/MM/yyyy HH:mm}" />

		<display:column titleKey="brotherhood.pictures">
			<jstl:set var="picturesSize" value="${row.pictures.size()}" />
			<spring:url var="picturesUrl"
				value="/picture/member/listPerBrotherhood.do">
				<spring:param name="broId" value="${row.id}" />
			</spring:url>
			<a href="${picturesUrl}"> <spring:message var="viewPictures1"
					code="brotherhood.view.pictures" /> <jstl:out
					value="${viewPictures1}(${picturesSize})" />
			</a>
		</display:column>

		<display:column>
			<spring:url var="createUrl"
				value="/enrolment/member/create.do?brotherhoodId={brotherhoodId}">
				<spring:param name="brotherhoodId" value="${row.id}" />
			</spring:url>
				<form:form action="${createUrl}">
					<acme:submit code="enrolment.create" name="save" />
				</form:form>
		</display:column>

	</display:table>

</security:authorize>