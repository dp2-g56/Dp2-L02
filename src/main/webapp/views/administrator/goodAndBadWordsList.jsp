
<%@page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@taglib prefix="jstl"	uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="security" uri="http://www.springframework.org/security/tags"%>
<%@taglib prefix="display" uri="http://displaytag.sf.net"%>

<p><spring:message code="administrator.wordList" /></p>

<security:authorize access="hasRole('ADMIN')">


<display:table name="badWords" id="badWordList" requestURI="${requestURI}"
	pagesize="5" class="displaytag">
	  <display:column >
      		<a href="words/administrator/save.do?word=${badWordList}">
 	  			<spring:message code="administrator.edit" />
 	  		</a>
      </display:column>
      
       <display:column titleKey="administrator.badWords" sortable="true">
      		<jstl:out value="${badWordList}" />
      </display:column>
</display:table>


<display:table name="goodWords" id="goodWordList" requestURI="${requestURI}"
	pagesize="5" class="displaytag">
	  <display:column >
      		<a href="words/administrator/save.do?word=${goodWordList}">
 	  			<spring:message code="administrator.edit" />
 	  		</a>
      </display:column>
      
       <display:column titleKey="administrator.goodWords" sortable="true">
      		<jstl:out value="${goodWordList}" />
      </display:column>
</display:table>

<div>
	<a href="words/administrator/create.do">
		<spring:message code="administrator.create" />
	</a>
</div>






</security:authorize>