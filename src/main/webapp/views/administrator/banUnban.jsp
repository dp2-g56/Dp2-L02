
<%@page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@taglib prefix="jstl"	uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="security" uri="http://www.springframework.org/security/tags"%>
<%@taglib prefix="display" uri="http://displaytag.sf.net"%>

<p><spring:message code="administrator.suspicious" /></p>

<security:authorize access="hasRole('ADMIN')">

<display:table name="actors" id="actorList" requestURI="suspicious/administrator/list.do"
	pagesize="5" class="displaytag">

 	  <display:column>
 	  	<jstl:choose>
 	  	<jstl:when test="${actorList.userAccount.isNotLocked == true  && (actorList.hasSpam == true ||  actorList.polarity <= -0.5)}">
 	  		<a href="suspicious/administrator/ban.do?actorId=${actorList.id}">
					<spring:message code="administrator.ban"/>
 	  		</a>
 	  	</jstl:when>
 	  	
 	  	<jstl:when test="${actorList.userAccount.isNotLocked == false}">
 	  		<a href="suspicious/administrator/unban.do?actorId=${actorList.id}">
					<spring:message code="administrator.unban"/>
 	  		</a>
 	  	</jstl:when>
 	  	</jstl:choose>
 	  </display:column>
 	  
      <display:column titleKey="administrator.actors">
      
      		<jstl:out value="${actorList.userAccount.username}" />
      
      </display:column>
      
       <display:column titleKey="administrator.suspicious">
       <jstl:choose>
 	  	<jstl:when test="${actorList.hasSpam}">
					<spring:message code="administrator.yes" />
 	  	</jstl:when>
 	  	<jstl:otherwise>
 	  				<jstl:out value="N/A" />
 	  	</jstl:otherwise>
 	  	</jstl:choose>
     
      </display:column>
      
            <display:column titleKey="administrator.polarity">
            <jstl:choose>
            
      		<jstl:when test="${ actorList.polarity == 0.0}">
      		
      		<jstl:out value="N/A" />
      		</jstl:when>
      		
      		<jstl:otherwise>
      		<jstl:out value="${actorList.polarity}" />
      		</jstl:otherwise>
      		
      		</jstl:choose>
      
      </display:column>
</display:table>

</security:authorize>