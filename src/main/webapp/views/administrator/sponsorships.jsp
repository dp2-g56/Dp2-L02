<%@page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>

<%@taglib prefix="jstl"	uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="security" uri="http://www.springframework.org/security/tags"%>
<%@taglib prefix="display" uri="http://displaytag.sf.net"%>

<security:authorize access="hasRole('ADMIN')">		 	

	<form name="refresh" action="sponsorship/administrator/filter.do" method="post">
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
					requestURI="${requestURI}">
					
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
		
		<display:column titleKey="sponsorship.status" style="color:${color}">
		<jstl:choose>
			<jstl:when test="${row.isActivated==true}">
				<spring:message code="sponsorship.status.activated" var="status"/>
				<jstl:out value="${status}"/>
			</jstl:when>
			
			<jstl:otherwise>
				<spring:message code="sponsorship.status.deactivated" var="status"/>
				<jstl:out value="${status}"/>
			</jstl:otherwise>
		</jstl:choose>
		</display:column>
					
		<display:column property="banner" titleKey="sponsorship.banner" style="color:${color}"/>
		
		<display:column property="targetURL" titleKey="sponsorship.targetURL" style="color:${color}"/>
		
		<display:column property="spentMoney" titleKey="sponsorship.spentMoney" style="color:${color}"/>
			
		<display:column titleKey="sponsorship.creditCard" style="color:${color}">
			<jstl:set var="number" value="${row.creditCard.number.toString()}"/>
			<jstl:out value="*${number.toString().charAt(number.toString().length() - 4)}${number.toString().charAt(number.toString().length() - 3)}${number.toString().charAt(number.toString().length() - 2)}${number.toString().charAt(number.toString().length() - 1)}"/>
		</display:column>
		
		<display:column titleKey="sponsorship.creditCard.isValid" style="color:${color}">
			<jstl:set var="valid" value="${isValid.get(row.id)}"/>
			<jstl:choose>
				<jstl:when test="${valid==true}">
					<spring:message code="sponsorship.creditCard.isValid.yes" var="valid"/>
					<jstl:out value="${valid}"/>
				</jstl:when>
				
				<jstl:otherwise>
					<spring:message code="sponsorship.creditCard.isValid.no" var="valid"/>
					<jstl:out value="${valid}"/>
				</jstl:otherwise>
			</jstl:choose>
		</display:column>
				
	</display:table>
	
	<spring:url var="process" value="/sponsorship/administrator/checkAndDeactivate.do" />
	<a href="${process}">
    	<input type="button" value="<spring:message code="sponsorship.process"/>" onClick="return confirm('<spring:message code="sponsorship.process.confirmation" />')"/>
	</a>
	
</security:authorize>