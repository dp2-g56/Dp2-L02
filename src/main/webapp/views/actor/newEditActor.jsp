<%--
 * action-1.jsp
 *
 * Copyright (C) 2018 Universidad de Sevilla
 * 
 * The use of this project is hereby constrained to the conditions of the 
 * TDG Licence, a copy of which you may download from 
 * http://www.tdg-seville.info/License.html
 --%>

<%@page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>

<%@taglib prefix="jstl"	uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="security" uri="http://www.springframework.org/security/tags"%>
<%@taglib prefix="display" uri="http://displaytag.sf.net"%>

<jstl:choose>
	<!-- Editing -->
	<jstl:when test = "${actor.id>0}">
	
	
		<form:form modelAttribute="actor" action="/actor/authenticated/edit.do">
			<h2><spring:message code="actor.userAccount"/></h2>
		
			<form:label path="username">
				<spring:message code="actor.username"/>:
			</form:label>
			<form:input path="username" value="${actor.userAccount.username}" required/>
			<form:errors cssClass="error" path="username"/>
			
			<form:label path="password">
				<spring:message code="actor.password"/>:
			</form:label>
			<form:password path="password" value="${actor.userAccount.password}" required/>
			<form:errors cssClass="error" path="password"/>
			
			<h2><spring:message code="actor.personalInformation"/></h2>
		
			<form:label path="name">
				<spring:message code="actor.name"/>:
			</form:label>
			<form:input path="name" value="${actor.name}" required/>
			<form:errors cssClass="error" path="name"/> 
			
			<form:label path="middleName">
				<spring:message code="actor.middleName"/>:
			</form:label>
			<form:input path="middleName" value="${actor.middleName}"/>
			<form:errors cssClass="error" path="middleName"/>
			
			<form:label path="surname">
				<spring:message code="actor.surname"/>:
			</form:label>
			<form:input path="surname" value="${actor.surname}" required/>
			<form:errors cssClass="error" path="surname"/>
			
			<!-- HandyWorker -->
			<security:authorize access = "hasRole('HANDYWORKER')">
				<form:label path="make">
					<spring:message code="handyworker.make"/>:
				</form:label>
				<form:input path="make" value="${actor.make}"/>
				<form:errors cssClass="error" path="make"/>			
				
				<form:hidden path="score"/>
			</security:authorize>
			<!-- Customer -->
			<security:authorize access = "hasRole('CUSTOMER')">
				<form:hidden path="score"/>
			</security:authorize>
			
			<form:label path="photo">
				<spring:message code="actor.photo"/>:
			</form:label>
			<form:input path="photo" value="${actor.photo}"/>
			<form:errors cssClass="error" path="photo"/>
			
			<form:label path="email">
				<spring:message code="actor.email"/>:
			</form:label>
			<form:input path="email" value="${actor.email}" required/>
			<form:errors cssClass="error" path="email"/>
			
			<form:label path="phoneNumber">
				<spring:message code="actor.phoneNumber"/>:
			</form:label>
			<form:input path="phoneNumber" value="${actor.phoneNumber}"/>
			<form:errors cssClass="error" path="phoneNumber"/>
			
			<form:label path="address">
				<spring:message code="actor.address"/>:
			</form:label>
			<form:input path="address" value="${actor.address}"/>
			<form:errors cssClass="error" path="address"/>
			
			<form:hidden path="version"/>
			<form:hidden path="id"/>
			<form:hidden path="hasSpam"/>
			<form:hidden path="socialProfiles"/>
			<form:hidden path="boxes"/>
			
			<input type="submit" name = "save" value="<spring:message code="actor.save"/>" onclick="return confirm('<spring:message code="actor.save" />')"/>
		</form:form>
	
	
	</jstl:when>
	<!-- Creating -->
	<jstl:otherwise>
		<!-- Anonymous -->
		<security:authorize access = "isAnonymous()">
		
		
		<form:form modelAttribute="actor" action="/actor/anonymous/create.do">
			<h2><spring:message code="actor.userAccount"/></h2>
		
			<form:label path="username">
				<spring:message code="actor.username"/>:
			</form:label>
			<form:input path="username" required/>
			<form:errors cssClass="error" path="username"/>
			
			<form:label path="password">
				<spring:message code="actor.password"/>:
			</form:label>
			<form:password path="password" required/>
			<form:errors cssClass="error" path="password"/>
			
			<form:label path="type">
				<spring:message code="actor.type"/>:
			</form:label>
			<form:select path="type" required>
				<form:option label="<spring:message code="actor.customer"/>" value="0" />
				<form:option label="<spring:message code="actor.sponsor"/>" value="1" />
				<form:option label="<spring:message code="actor.handyworker"/>" value="2" />
			</form:select>
			<form:errors cssClass="error" path="type"/>
			
			<h2><spring:message code="actor.personalInformation"/></h2>
		
			<form:label path="name">
				<spring:message code="actor.name"/>:
			</form:label>
			<form:input path="name" required/> 
			<form:errors cssClass="error" path="name"/>
			
			<form:label path="middleName">
				<spring:message code="actor.middleName"/>:
			</form:label>
			<form:input path="middleName"/>
			<form:errors cssClass="error" path="middleName"/>
			
			<form:label path="surname">
				<spring:message code="actor.surname"/>:
			</form:label>
			<form:input path="surname" required/>
			<form:errors cssClass="error" path="surname"/>
			
			<form:label path="photo">
				<spring:message code="actor.photo"/>:
			</form:label>
			<form:input path="photo"/>
			<form:errors cssClass="error" path="photo"/>
			
			<form:label path="email">
				<spring:message code="actor.email"/>:
			</form:label>
			<form:input path="email" required/>
			<form:errors cssClass="error" path="email"/>
			
			<form:label path="phoneNumber">
				<spring:message code="actor.phoneNumber"/>:
			</form:label>
			<form:input path="phoneNumber"/>
			<form:errors cssClass="error" path="phoneNumber"/>
			
			<form:label path="address">
				<spring:message code="actor.address"/>:
			</form:label>
			<form:input path="address"/>
			<form:errors cssClass="error" path="address"/>
			
			<form:hidden path="score"/>
			<form:hidden path="make"/>
			<form:hidden path="version"/>
			<form:hidden path="id"/>
			<form:hidden path="hasSpam"/>
			<form:hidden path="socialProfiles"/>
			<form:hidden path="boxes"/>
			
			<input type="submit" name = "save" value="<spring:message code="actor.save"/>"/>
		</form:form>
		
		
		</security:authorize>
		<!-- Admin -->
		<security:authorize access = "hasRole('ADMIN')">
		
		
		<form:form modelAttribute="actor" action="/actor/admin/create.do">
			<h2><spring:message code="actor.userAccount"/></h2>
			
			<form:label path="name">
			</form:label>
			<form:errors cssClass="error" path="name"/>
			<spring:message code="actor.username"/>:<form:input path="username" required/>
			
			<form:label path="name">
			</form:label>
			<form:errors cssClass="error" path="name"/>
			<spring:message code="actor.password"/>:<form:password path="password" required/>
			
			<form:label path="name">
			</form:label>
			<form:errors cssClass="error" path="name"/>
			<spring:message code="actor.type"/>:
			<form:select path="type" required>
				<form:option label="<spring:message code="actor.referee"/>" value="0" />
				<form:option label="<spring:message code="actor.admin"/>" value="1" />
			</form:select>
			
			<h2><spring:message code="actor.personalInformation"/></h2>
		
			<form:label path="name">
			</form:label>
			<form:errors cssClass="error" path="name"/>
			<spring:message code="actor.name"/>:<form:input path="name" required/> 
			
			<form:label path="name">
			</form:label>
			<form:errors cssClass="error" path="name"/>
			<spring:message code="actor.middleName"/>:<form:input path="middleName"/>
			
			<form:label path="name">
			</form:label>
			<form:errors cssClass="error" path="name"/>
			<spring:message code="actor.surname"/>:<form:input path="surname" required/>
			
			<form:label path="name">
			</form:label>
			<form:errors cssClass="error" path="name"/>
			<spring:message code="actor.photo"/>:<form:input path="photo"/>
			
			<form:label path="name">
			</form:label>
			<form:errors cssClass="error" path="name"/>
			<spring:message code="actor.email"/>:<form:input path="email" required/>
			
			<form:label path="name">
			</form:label>
			<form:errors cssClass="error" path="name"/>
			<spring:message code="actor.phoneNumber"/>:<form:input path="phoneNumber"/>
			
			<form:label path="name">
			</form:label>
			<form:errors cssClass="error" path="name"/>
			<spring:message code="actor.address"/>:<form:input path="address"/>
			
			<form:hidden path="version"/>
			<form:hidden path="id"/>
			<form:hidden path="hasSpam"/>
			<form:hidden path="socialProfiles"/>
			<form:hidden path="boxes"/>
			
			<input type="submit" name = "save" value="<spring:message code="actor.save"/>"/>
		</form:form>
		
		
		</security:authorize>
	</jstl:otherwise>
</jstl:choose>