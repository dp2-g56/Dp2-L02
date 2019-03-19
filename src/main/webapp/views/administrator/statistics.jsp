<%@page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>

<style>
td, th {
	border: 1px solid #FFFFFF;
	text-align: left;
	padding: 8px;
}

table {
	background-color: #ffeeaa;
	font-family: arial, sans-serif;
	border-collapse: collapse;
	width: 50%;
}

tr:nth-child(even) {
	background-color: #FFFFFF;
}
</style>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="security"
	uri="http://www.springframework.org/security/tags"%>
<%@taglib prefix="display" uri="http://displaytag.sf.net"%>

<security:authorize access="hasRole('ADMIN')">

	<strong><spring:message code="statistics.spammers" />:</strong>
	<div class="ct-chart-spammersAndNotSpammers ct-perfect-fourth"
		style="width: 30%"></div>
	<script>
		new Chartist.Pie('.ct-chart-spammersAndNotSpammers', {
			series : [ "${statistics.get(16)}", "${statistics.get(17)}" ]
		}, {
			donut : false,
			donutWidth : 60,
			donutSolid : true,
			startAngle : 270,
			showLabel : true
		});
	</script>
	<ul>
		<li><spring:message code="actor.spammers" />: <jstl:out
				value="${statistics.get(16)}" />%</li>
		<li><spring:message code="actor.nonSpammers" />: <jstl:out
				value="${statistics.get(17)}" />%</li>
	</ul>

	<strong><spring:message code="statistics.avgPolarity" />:</strong>
	<div class="ct-chart-polarityAvgPerActor ct-perfect-fourth"
		style="width: 30%"></div>
	<script>
		new Chartist.Pie('.ct-chart-polarityAvgPerActor', {
			series : [ "${statistics.get(18)}", "${statistics.get(19)}",
					"${statistics.get(20)}" ]
		}, {
			donut : false,
			donutWidth : 60,
			donutSolid : true,
			startAngle : 270,
			showLabel : true
		});
	</script>
	<ul>
		<li><spring:message code="actor.admin" />: <jstl:out
				value="${statistics.get(18)}" /></li>
		<li><spring:message code="actor.member" />: <jstl:out
				value="${statistics.get(19)}" /></li>
		<li><spring:message code="actor.brotherhood" />: <jstl:out
				value="${statistics.get(20)}" /></li>
	</ul>

	<strong><spring:message
			code="statistics.ratioBrotherhoodsPerArea" />:</strong>
	<div class="ct-chart-ratioBrotherhoodsPerArea ct-perfect-fourth"
		style="width: 30%"></div>
	<script>
		var data = {
			labels : [],
			series : []
		};

		<jstl:forEach items="${areaNames}" var="aName">
		data.labels.push("${aName}");
		data.series.push("${ratioBrotherhoodsPerArea.get(aName)}");
		</jstl:forEach>

		new Chartist.Bar('.ct-chart-ratioBrotherhoodsPerArea', data, {
			distributeSeries : true
		});
	</script>

	<strong><spring:message
			code="statistics.countBrotherhoodsPerArea" />:</strong>
	<div class="ct-chart-countBrotherhoodsPerArea ct-perfect-fourth"
		style="width: 30%"></div>
	<script>
		var data = {
			labels : [],
			series : []
		};

		<jstl:forEach items="${areaNames}" var="aName">
		data.labels.push("${aName}");
		data.series.push("${countBrotherhoodsPerArea.get(aName)}");
		</jstl:forEach>

		new Chartist.Bar('.ct-chart-countBrotherhoodsPerArea', data, {
			distributeSeries : true
		});
	</script>

	<strong><spring:message code="statistics.brotherhoodsPerArea" />:</strong>
	<div
		class="ct-chart-minMaxAvgStdvBrotherhoodsPerArea ct-perfect-fourth"
		style="width: 30%"></div>
	<script>
		var data = {
			labels : [ '<spring:message code="statistics.minimum"/>',
					'<spring:message code="statistics.maximum"/>',
					'<spring:message code="statistics.average"/>',
					'<spring:message code="statistics.standardDeviation"/>' ],
			series : [ "${statistics.get(7)}", "${statistics.get(8)}",
					"${statistics.get(9)}", "${statistics.get(10)}" ]
		};

		new Chartist.Bar('.ct-chart-minMaxAvgStdvBrotherhoodsPerArea', data, {
			distributeSeries : true
		});
	</script>

	<strong><spring:message
			code="statistics.membersPerBrotherhood" />:</strong>
	<div
		class="ct-chart-minMaxAvgStdvMembersPerBrotherhood ct-perfect-fourth"
		style="width: 30%"></div>
	<script>
		var data = {
			labels : [ '<spring:message code="statistics.minimum"/>',
					'<spring:message code="statistics.maximum"/>',
					'<spring:message code="statistics.average"/>',
					'<spring:message code="statistics.standardDeviation"/>' ],
			series : [ "${statistics.get(1)}", "${statistics.get(2)}",
					"${statistics.get(0)}", "${statistics.get(3)}" ]
		};

		new Chartist.Bar('.ct-chart-minMaxAvgStdvMembersPerBrotherhood', data,
				{
					distributeSeries : true
				});
	</script>

	<br />
	<hr />
	<br />

	<strong><spring:message
			code="statistics.membersPerBrotherhood" />:</strong>
	<br />

	<table style="width: 100%">
		<tr>
			<td><b><spring:message code="statistics.average" /></b></td>
			<td><jstl:out value="${statistics.get(0)}" /></td>
		</tr>
		<tr>
			<td><b><spring:message code="statistics.minimum" /></b></td>
			<td><jstl:out value="${statistics.get(1)}" /></td>
		</tr>
		<tr>
			<td><b><spring:message code="statistics.maximum" /></b></td>
			<td><jstl:out value="${statistics.get(2)}" /></td>
		</tr>
		<tr>
			<td><b><spring:message code="statistics.standardDeviation" /></b></td>
			<td><jstl:out value="${statistics.get(3)}" /></td>
		</tr>
	</table>
	<br />

	<strong><spring:message
			code="statistics.largestAndSmallestBrotherhoods" />:</strong>
	<br />
	<table style="width: 100%">
		<tr>
			<td><b><spring:message code="statistics.largestBrotherhoods" />:</b></td>
		</tr>
		<jstl:forEach items="${largestBrotherhoods}" var="lBrotherhood">
			<tr>
				<td><jstl:out value="${lBrotherhood}" /></td>
			</tr>
		</jstl:forEach>
		<tr>
			<td><b><spring:message
						code="statistics.smallestBrotherhoods" />:</b></td>
		</tr>
		<jstl:forEach items="${smallestBrotherhoods}" var="sBrotherhood">
			<tr>
				<td><jstl:out value="${sBrotherhood}" /></td>
			</tr>
		</jstl:forEach>

	</table>
	<br />

	<strong><spring:message
			code="statistics.ratiosRequestByParade" />:</strong>
	<br />
	<table style="width: 100%">
		<tr>
			<td><h4>
					<spring:message code="statistics.ratioRequestApprovedByParade" />
					:
				</h4></td>
			<td></td>
		</tr>
		<jstl:forEach items="${paradeNames}" var="nameP">
			<tr>
				<td><jstl:out value="${nameP}" /></td>
				<td><jstl:out
						value="${ratioRequestApprovedByParade.get(nameP)}" /></td>

			</tr>



		</jstl:forEach>
		<tr>
			<td><h4>
					<spring:message code="statistics.ratioRequestPendingByParade" />
					:
				</h4></td>
			<td></td>
		</tr>
		<jstl:forEach items="${paradeNames}" var="nameP">
			<tr>
				<td><jstl:out value="${nameP}" /></td>
				<td><jstl:out value="${ratioRequestPendingByParade.get(nameP)}" /></td>
			</tr>
		</jstl:forEach>

		<tr>
			<td><h4>
					<spring:message code="statistics.ratioRequestRejectedByParade" />
					:
				</h4></td>
			<td></td>
		</tr>
		<jstl:forEach items="${paradeNames}" var="nameP">
			<tr>
				<td><jstl:out value="${nameP}" /></td>
				<td><jstl:out
						value="${ratioRequestRejectedByParade.get(nameP)}" /></td>
			</tr>
		</jstl:forEach>
	</table>
	<br />

	<strong><spring:message code="statistics.paradesNextMonth" />:</strong>
	<br />
	<table style="width: 100%">
		<jstl:choose>
			<jstl:when test="${paradesOfNextMonth.isEmpty()}">
				<td><spring:message code="statistics.none" /></td>
			</jstl:when>
			<jstl:otherwise>
				<jstl:forEach items="${paradesOfNextMonth}"
					var="nameParadeNextMonth">
					<tr>
						<td><jstl:out value="${nameParadeNextMonth}" /></td>
					</tr>
				</jstl:forEach>
			</jstl:otherwise>
		</jstl:choose>
	</table>
	<br />

	<strong><spring:message code="statistics.ratiosRequest" />:</strong>
	<br />
	<table style="width: 100%">
		<tr>
			<td><b><spring:message
						code="statistics.ratioRequestApproved" /></b></td>
			<td><jstl:out value="${statistics.get(4)}" /></td>
		</tr>
		<tr>
			<td><b><spring:message code="statistics.ratioRequestPending" /></b></td>
			<td><jstl:out value="${statistics.get(5)}" /></td>
		</tr>
		<tr>
			<td><b><spring:message
						code="statistics.ratioRequestRejected" /></b></td>
			<td><jstl:out value="${statistics.get(6)}" /></td>
		</tr>
	</table>
	<br />

	<strong><spring:message code="statistics.membersTenPercent" />:</strong>
	<br />
	<table style="width: 100%">
		<tr>
			<jstl:choose>
				<jstl:when test="${membersTenPercent.isEmpty()}">
					<td><spring:message code="statistics.none" /></td>
				</jstl:when>
				<jstl:otherwise>
					<jstl:forEach items="${membersTenPercent}" var="membersTenPercent">
						<tr>
							<td><jstl:out
									value="${membersTenPercent.name} ${membersTenPercent.middleName} ${membersTenPercent.surname}" /></td>
						</tr>
					</jstl:forEach>
				</jstl:otherwise>
			</jstl:choose>
		</tr>
	</table>
	<br />

	<strong><spring:message code="statistics.positions" />:</strong>
	<br />
	<table style="width: 100%">
		<jstl:choose>
			<jstl:when test="${locale == 'EN'}">
				<jstl:forEach items="${positions}" var="position">
					<tr>
						<td><jstl:out value="${position.titleEnglish}" /></td>
						<td><jstl:out
								value="${countPositions.get(position).intValue()}" /></td>
					</tr>
				</jstl:forEach>
			</jstl:when>
			<jstl:otherwise>
				<jstl:forEach items="${positions}" var="position">
					<tr>
						<td><jstl:out value="${position.titleSpanish}" /></td>
						<td><jstl:out
								value="${countPositions.get(position).intValue()}" /></td>
					</tr>
				</jstl:forEach>
			</jstl:otherwise>
		</jstl:choose>
	</table>
	<br />

	<!-- GRAFICO 3 -->
	<strong><spring:message
			code="statistics.ratioBrotherhoodsPerArea" />:</strong>
	<br />
	<table style="width: 100%">
		<jstl:forEach items="${areaNames}" var="aName">
			<tr>
				<td><jstl:out value="${aName}" /></td>
				<td><jstl:out value="${ratioBrotherhoodsPerArea.get(aName)}" /></td>
			</tr>
		</jstl:forEach>
	</table>
	<br />

	<strong><spring:message
			code="statistics.countBrotherhoodsPerArea" />:</strong>
	<br />
	<table style="width: 100%">
		<jstl:forEach items="${areaNames}" var="aName">
			<tr>
				<td><jstl:out value="${aName}" /></td>
				<td><jstl:out
						value="${countBrotherhoodsPerArea.get(aName).intValue()}" /></td>
			</tr>
		</jstl:forEach>
	</table>
	<br />

	<strong><spring:message code="statistics.brotherhoodsPerArea" />:</strong>
	<br />
	<table style="width: 100%">
		<tr>
			<td><b><spring:message code="statistics.minimum" /></b></td>
			<td><jstl:out value="${statistics.get(7)}" /></td>
		</tr>
		<tr>
			<td><b><spring:message code="statistics.maximum" /></b></td>
			<td><jstl:out value="${statistics.get(8)}" /></td>
		</tr>
		<tr>
			<td><b><spring:message code="statistics.average" /></b></td>
			<td><jstl:out value="${statistics.get(9)}" /></td>
		</tr>
		<tr>
			<td><b><spring:message code="statistics.standardDeviation" /></b></td>
			<td><jstl:out value="${statistics.get(10)}" /></td>
		</tr>
	</table>
	<br />

	<strong><spring:message code="statistics.finderResults" />:</strong>
	<br />
	<table style="width: 100%">
		<tr>
			<td><b><spring:message code="statistics.minimum" /></b></td>
			<td><jstl:out value="${statistics.get(11)}" /></td>
		</tr>
		<tr>
			<td><b><spring:message code="statistics.maximum" /></b></td>
			<td><jstl:out value="${statistics.get(12)}" /></td>
		</tr>
		<tr>
			<td><b><spring:message code="statistics.average" /></b></td>
			<td><jstl:out value="${statistics.get(13)}" /></td>
		</tr>
		<tr>
			<td><b><spring:message code="statistics.standardDeviation" /></b></td>
			<td><jstl:out value="${statistics.get(14)}" /></td>
		</tr>
	</table>
	<br />

	<table style="width: 100%">
		<tr>
			<jstl:choose>
				<jstl:when test="${statistics.get(19) == -1}">
					<td><strong><spring:message
								code="statistics.ratioNonEmptyVsEmpty" />:</strong></td>
					<td><spring:message code="statistics.infinite" /></td>
				</jstl:when>
				<jstl:otherwise>
					<td><strong><spring:message
								code="statistics.ratioNonEmptyVsEmpty" />:</strong></td>
					<td><jstl:out value="${statistics.get(19)}" /></td>
				</jstl:otherwise>
			</jstl:choose>
		</tr>
	</table>

	<strong><spring:message code="statistics.history" />:</strong>
	<br />
	<table style="width: 100%">
		<tr>
			<td><b><spring:message code="statistics.minimum" /></b></td>
			<td><jstl:out value="${statistics.get(15)}" /></td>
		</tr>
		<tr>
			<td><b><spring:message code="statistics.maximum" /></b></td>
			<td><jstl:out value="${statistics.get(16)}" /></td>
		</tr>
		<tr>
			<td><b><spring:message code="statistics.average" /></b></td>
			<td><jstl:out value="${statistics.get(17)}" /></td>
		</tr>
		<tr>
			<td><b><spring:message code="statistics.standardDeviation" /></b></td>
			<td><jstl:out value="${statistics.get(18)}" /></td>
		</tr>
	</table>
	<br />

	<table style="width: 100%">
		<tr>
			<td><b><spring:message code="statistics.broLargestHistory" /></b></td>
			<td><jstl:out value="${broLargestHistory}"></jstl:out>
		</tr>
	</table>

	<strong><spring:message
			code="statistics.broHistoryLargerThanAvg" />:</strong>
	<br />
	<table style="width: 100%">
		<jstl:forEach items="${broHistoryLargerThanAvg}" var="bro">
			<tr>
				<td><jstl:out value="${bro.name}" /></td>
			</tr>
		</jstl:forEach>
	</table>
	<br />

	<table style="width: 100%">
		<tr>
			<td><b><spring:message
						code="statistics.ratioAreasNotCoordinated" /></b></td>
			<td><jstl:out value="${statistics.get(19)}"></jstl:out>
		</tr>
	</table>

	<strong><spring:message code="statistics.paradesCoordinated" />:</strong>
	<br />
	<table style="width: 100%">
		<tr>
			<td><b><spring:message code="statistics.minimum" /></b></td>
			<td><jstl:out value="${statistics.get(20)}" /></td>
		</tr>
		<tr>
			<td><b><spring:message code="statistics.maximum" /></b></td>
			<td><jstl:out value="${statistics.get(21)}" /></td>
		</tr>
		<tr>
			<td><b><spring:message code="statistics.average" /></b></td>
			<td><jstl:out value="${statistics.get(22)}" /></td>
		</tr>
		<tr>
			<td><b><spring:message code="statistics.standardDeviation" /></b></td>
			<td><jstl:out value="${statistics.get(23)}" /></td>
		</tr>
	</table>
	<br />

	<strong><spring:message
			code="statistics.chaptersThatCoordinateAtLeast" />:</strong>
	<br />
	<table style="width: 100%">
		<jstl:choose>
			<jstl:when test="${chaptersThatCoordinateAtLeast.isEmpty()}">
				<td><spring:message code="statistics.none" /></td>
			</jstl:when>
			<jstl:otherwise>
				<jstl:forEach items="${chaptersThatCoordinateAtLeast}" var="chap">
					<tr>
						<td><jstl:out value="${chap.name}" /></td>
					</tr>
				</jstl:forEach>
			</jstl:otherwise>
		</jstl:choose>
	</table>
	<br />

	<table style="width: 100%">
		<tr>
			<td><b><spring:message code="statistics.paradesDraftVSFinal" /></b></td>
			<td><jstl:out value="${statistics.get(24)}"></jstl:out>
		</tr>
	</table>

	<strong><spring:message code="statistics.ratioParadesRequest" />:</strong>
	<br />
	<table style="width: 100%">
		<tr>
			<td><b><spring:message code="statistics.accepted" /></b></td>
			<td><jstl:out value="${statistics.get(25)}" /></td>
		</tr>
		<tr>
			<td><b><spring:message code="statistics.rejected" /></b></td>
			<td><jstl:out value="${statistics.get(26)}" /></td>
		</tr>
		<tr>
			<td><b><spring:message code="statistics.submitted" /></b></td>
			<td><jstl:out value="${statistics.get(27)}" /></td>
		</tr>
	</table>
	<br />
	
	<table style="width: 100%">
		<tr>
			<td><b><spring:message code="statistics.ratioActiveSponsorships" /></b></td>
			<td><jstl:out value="${statistics.get(28)}"></jstl:out>
		</tr>
	</table>
	
	<strong><spring:message code="statistics.sponsorshipsPerSponsor" />:</strong>
	<br />
	<table style="width: 100%">
		<tr>
			<td><b><spring:message code="statistics.minimum" /></b></td>
			<td><jstl:out value="${statistics.get(29)}" /></td>
		</tr>
		<tr>
			<td><b><spring:message code="statistics.maximum" /></b></td>
			<td><jstl:out value="${statistics.get(30)}" /></td>
		</tr>
		<tr>
			<td><b><spring:message code="statistics.average" /></b></td>
			<td><jstl:out value="${statistics.get(31)}" /></td>
		</tr>
		<tr>
			<td><b><spring:message code="statistics.standardDeviation" /></b></td>
			<td><jstl:out value="${statistics.get(32)}" /></td>
		</tr>
	</table>
	<br />
	
	<strong><spring:message
			code="statistics.top5Sponsor" />:</strong>
	<br />
	<table style="width: 100%">
		<jstl:choose>
			<jstl:when test="${top5Sponsor.isEmpty()}">
				<td><spring:message code="statistics.none" /></td>
			</jstl:when>
			<jstl:otherwise>
				<jstl:forEach items="${top5Sponsor}" var="sponsor">
					<tr>
						<td><jstl:out value="${sponsor}" /></td>
					</tr>
				</jstl:forEach>
			</jstl:otherwise>
		</jstl:choose>
	</table>
	<br />

</security:authorize>
