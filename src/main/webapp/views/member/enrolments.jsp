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
	<spring:message code="member.enrolments" />
</p>

<security:authorize access="hasRole('MEMBER')">

	<jstl:if test="${res}">
		<script type="text/javascript">
			alert("<spring:message code="enrolment.create.error"/>");
		</script>
	</jstl:if>

	<display:table pagesize="5" name="enrolments" id="row"
		class="displaytag" requestURI="enrolment/member/list.do">

		<display:column property="brotherhood.title"
			titleKey="enrolment.brotherhood" />

		<display:column property="statusEnrolment" titleKey="enrolment.status" />

		<jstl:if test="${locale == 'EN'}">
			<display:column property="position.titleEnglish" titleKey="enrolment.position" />
		</jstl:if>
		<jstl:if test="${locale == 'ES'}">
			<display:column property="position.titleSpanish" titleKey="enrolment.position" />
		</jstl:if>

		<display:column property="creationMoment" titleKey="enrolment.moment"
			sortable="true" format="{0,date,dd/MM/yyyy HH:mm}" />

		<display:column property="dropOutDate"
			titleKey="enrolment.dropOutDate" sortable="true"
			format="{0,date,dd/MM/yyyy HH:mm}" />

		<display:column>
			<spring:url var="dropOutUrl"
				value="/enrolment/member/dropout.do?enrolmentId={enrolmentId}">
				<spring:param name="enrolmentId" value="${row.id}" />
			</spring:url>
			<jstl:if test="${row.statusEnrolment.toString()=='ACCEPTED'}">
				<form:form action="${dropOutUrl}">
					<acme:submit code="enrolment.dropOut" name="save" />
				</form:form>
			</jstl:if>
		</display:column>

	</display:table>

</security:authorize>