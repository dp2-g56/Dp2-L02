<%--
 * header.jsp
 *
 * Copyright (C) 2019 Universidad de Sevilla
 * 
 * The use of this project is hereby constrained to the conditions of the 
 * TDG Licence, a copy of which you may download from 
 * http://www.tdg-seville.info/License.html
 --%>

<%@page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>

<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="security" uri="http://www.springframework.org/security/tags"%>

<div>
	<a href="#"><img src="${imageURL}" height= 180px width= 700px alt="Acme Madruga Co., Inc." /></a>
</div>

<div>
	<ul id="jMenu">
		<!-- Do not forget the "fNiv" class for the first level links !! -->
		<security:authorize access="hasRole('ADMIN')">
			<li><a class="fNiv"><spring:message	code="master.page.administrator" /></a>
				<ul>
					<li class="arrow"></li>				
					<li><a href="area/administrator/showAreas.do"><spring:message code="master.page.administrator.showAreas" /></a></li>
					<li><a href="words/administrator/list.do"><spring:message code="master.page.administrator.goodAndBadWordsList" /></a></li>
					<li><a href="suspicious/administrator/list.do"><spring:message code="master.page.administrator.banUnban" /></a></li>
					<li><a href="configuration/administrator/list.do"><spring:message code="master.page.administrator.configuration" /></a></li>	
					<li><a href="position/administrator/list.do"><spring:message code="master.page.administrator.positions" /></a></li>																							
					<li><a href="statistics/administrator/show.do"><spring:message code="master.page.administrator.statistics" /></a></li>		
					<li><a href="priority/administrator/list.do"><spring:message code="master.page.administrator.priority" /></a></li>																								
					<li><a href="broadcast/administrator/send.do"><spring:message code="master.page.administrator.broadcast" /></a></li>																							
					<li><a href="broadcast/administrator/sendSecurityBreach.do"><spring:message code="master.page.administrator.broadcastSecurity" /></a></li>																							
					<li><a href="administrator/createAdmin.do"><spring:message code="master.page.administrator.createAdmin" /></a></li>																								
				</ul>
			</li>
		</security:authorize>
		
		<security:authorize access="hasRole('BROTHERHOOD')">
			<li><a class="fNiv"><spring:message	code="master.page.brotherhood" /></a>
				<ul>
					<li class="arrow"></li>
					<li><a href="parade/brotherhood/list.do"><spring:message code="master.page.brotherhood.parade.list" /></a></li>	
					<li><a href="float/brotherhood/list.do"><spring:message code="master.page.brotherhood.float.list" /></a></li>	
					<li><a href="area/brotherhood/showArea.do"><spring:message code="master.page.brotherhood.area" /></a></li>	
					<li><a href="request/brotherhood/list.do"><spring:message code="master.page.brotherhood.request.list" /></a></li>	
					<li><a href="member/brotherhood/list.do"><spring:message code="master.page.brotherhood.member.list" /></a></li>	
					<li><a href="enrolment/brotherhood/list.do"><spring:message code="master.page.brotherhood.enrolment.list" /></a></li>	
				</ul>
			</li>		
    </security:authorize>

		<security:authorize access="hasRole('MEMBER')">
			<li><a class="fNiv"><spring:message	code="master.page.member" /></a>
				<ul>
					<li class="arrow"></li>
					<li><a href="request/member/list.do"><spring:message code="master.page.member.request.list" /></a></li>
					<li><a href="enrolment/member/list.do"><spring:message code="master.page.member.enrolment" /></a></li>	
					<li><a href="finder/member/list.do"><spring:message code="master.page.member.finder" /></a></li>
					<li><a href="brotherhood/member/list.do"><spring:message code="master.pagebrotherhoods" /></a></li>			
				</ul>	
			</li>
		</security:authorize>
		
		<security:authorize access="isAnonymous()">
			<li><a class="fNiv" href="security/login.do"><spring:message code="master.page.login" /></a></li>
			<li><a class="fNiv" href="showAll/annonymous/brotherhood/list.do"><spring:message code="master.page.annonymous" /></a></li>
			<li><a class="fNiv"><spring:message	code="master.page.register" /></a>
				<ul>
					<li class="arrow"></li>	
					<li><a href="anonymous/createMember.do"><spring:message code="master.page.createMember" /> </a></li>
					<li><a href="anonymous/createBrotherhood.do"><spring:message code="master.page.createBrotherhood" /> </a></li>
					<li><a href="anonymous/createSponsor.do"><spring:message code="master.page.createSponsor" /> </a></li>
					<li><a href="anonymous/createChapter.do"><spring:message code="master.page.createChapter" /> </a></li>
				</ul>
			</li>
		</security:authorize>
		
		<security:authorize access="isAuthenticated()">
			<li>
				<a class="fNiv"> 
					<spring:message code="master.page.profile" /> 
			        (<security:authentication property="principal.username" />)
				</a>
				<ul>
					<li class="arrow"></li>		
					<li><a href="authenticated/showProfile.do"><spring:message code="master.page.myProfile" /> </a></li>
					
					<li><a href="authenticated/edit.do"><spring:message code="master.page.editPersonalData" /> </a></li>			
									
					<li><a href="box/actor/list.do"><spring:message code="master.page.mailSystem" /> </a></li>
					
					<li><a href="j_spring_security_logout"><spring:message code="master.page.logout" /> </a></li>
				</ul>
			</li>
			<li><a class="fNiv" href="showAll/annonymous/brotherhood/list.do"><spring:message code="master.page.annonymous" /></a></li>
		</security:authorize>
	</ul>
</div>

<div>
	<a href="?language=en">en</a> | <a href="?language=es">es</a>
</div>

