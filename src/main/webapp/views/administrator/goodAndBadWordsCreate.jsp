
<%@page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@taglib prefix="jstl"	uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="security" uri="http://www.springframework.org/security/tags"%>
<%@taglib prefix="display" uri="http://displaytag.sf.net"%>

<p><spring:message code="administrator.GoodAndBadWords" /></p>

<security:authorize access="hasRole('ADMIN')">
<form name="word" id="word" action="words/administrator/save.do" method="post" >


  <spring:message code="administrator.word" /> <input type="text" name="word" value="${word}" required><br>

<jstl:choose> 
  <jstl:when test="${word == null}">
  <input type="radio" name="wordType" value="goodword" checked> <spring:message code="administrator.goodWord" /> <br>
  <input type="radio" name="wordType" value="badword"> <spring:message code="administrator.badWord" /> <br>
 </jstl:when>
 
 <jstl:otherwise>
  <input type="hidden" id="originalWord" name="originalWord" value="${word}">
  </jstl:otherwise>
</jstl:choose>


<jstl:choose>
	
	<jstl:when test="${word == null}">

	<input type="submit" name="save" value="<spring:message code="administrator.create" />" />
	<br />
	
	</jstl:when>
	
	<jstl:otherwise>
	
	<input type="submit" name="editWord" value="<spring:message code="administrator.edit" />" />
	<input type="submit" name="delete" value="<spring:message code="administrator.deleteWord" />" />
	
	</jstl:otherwise>
	
</jstl:choose>

	

</form>
<br />
<spring:url var="listWords" value="/words/administrator/list.do"/>
<a href="${listWords}">
		<spring:message code="administrator.cancel" />			
	</a>


</security:authorize>