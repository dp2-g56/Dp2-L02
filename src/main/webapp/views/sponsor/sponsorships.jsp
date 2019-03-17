<%@page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>

<%@taglib prefix="jstl"	uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="security" uri="http://www.springframework.org/security/tags"%>
<%@taglib prefix="display" uri="http://displaytag.sf.net"%>

<security:authorize access="hasRole('SPONSOR')">		 	

	<form name="refresh" action="sponsorship/sponsor/filter.do" method="post">
		<label for="filter"><spring:message code="sponsorship.filter"/></label>
	
		<br/>
	
		<select name="fselect">
  			<option value="ALL"><spring:message code="sponsorship.status.all"/></option>
  			<option value="ACTIVATED"><spring:message code="sponsorship.status.activated"/></option>
 			<option value="DEACTIVATED"><spring:message code="sponsorship.status.deactivated"/></option>
		</select>
		
		<input type="submit" name="refresh" id="refresh" value="<spring:message code ="sponsorship.filter.button"/>"/>
	
	</form>
	
	<br/>

	<display:table pagesize="5" name="sponsorships" id="row" class="displaytag" 
					requestURI="sponsorship/sponsor/list.do">
					
		<jstl:choose>
			<jstl:when test="${row.isActivated==true}">
				<jstl:set var="color" value="green" />
			</jstl:when>
			
			<jstl:otherwise>
				<jstl:set var="color" value="black" />
			</jstl:otherwise>
		</jstl:choose>
		
		<display:column titleKey="sponsorship.parade" style="color:${color}">
			<jstl:out value="${row.parade.title}"/>
		</display:column>
					
		<display:column property="banner" titleKey="sponsorship.banner" style="color:${color}"/>
		
		<display:column property="targetURL" titleKey="sponsorship.targetURL" style="color:${color}"/>
		
		<display:column property="gain" titleKey="sponsorship.gain" style="color:${color}"/>
			
		<display:column titleKey="sponsorship.creditCard" style="color:${color}">
			<jstl:set var="number" value="${row.creditCard.number.toString()}"/>
			<jstl:out value="*${number.toString().charAt(number.toString().length() - 4)}${number.toString().charAt(number.toString().length() - 3)}${number.toString().charAt(number.toString().length() - 2)}${number.toString().charAt(number.toString().length() - 1)}"/>
		</display:column>
		
		<display:column titleKey="action">
			<jstl:if test="${row.isActivated==true}">
				<spring:url var="changeStatus" value="/sponsorship/sponsor/deactivate.do">
					<spring:param name="sponsorshipId" value="${row.id}" />
					<spring:message code="sponsorship.deactivate" var="status"/>
				</spring:url>
			</jstl:if>
			<jstl:if test="${row.isActivated==false}">
				<spring:url var="changeStatus" value="/sponsorship/sponsor/activate.do">
					<spring:param name="sponsorshipId" value="${row.id}" />
					<spring:message code="sponsorship.activate" var="status"/>
				</spring:url>
			</jstl:if>
			
			<spring:url var="updateSponsorship" value="/sponsorship/sponsor/edit.do">
				<spring:param name="sponsorshipId" value="${row.id}" />
			</spring:url>
			
			<a href="${updateSponsorship}"><spring:message code="sponsorship.update" /></a> / <a href="${changeStatus}" onClick="return confirm('<spring:message code="sponsorship.changeStatus.confirmation" />')"><jstl:out value="${status}"/></a>
			
		</display:column><a href="${changeStatus}"><spring:message code="sponsorship.update" /></a>
				
	</display:table>
	
</security:authorize>