package services;

import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import domain.Member;
import domain.Message;
import domain.Request;
import domain.Status;
import utilities.AbstractTest;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:spring/junit.xml" })
@Transactional
public class RequestServiceTest extends AbstractTest {

	@Autowired
	private RequestService requestService;

	@Autowired
	private MemberService memberService;

	/**
	 * We are going to test the Requirement R10.6
	 *
	 * R10. An actor who is authenticated as a brotherhood must be able to:
	 *
	 * 6. Manage the request to march on a parade, which includes listing them by
	 * status, showing them, and deciding on them. When the decision on a pending
	 * request is to accept it, the brotherhood must provide a position in the
	 * parade, which is identified by means of a row and a column; the system must
	 * check that no two members can march at the same row/column; the system must
	 * suggest a good position automatically, but the brotherhood may change it.
	 * When the decision is to reject it, the brotherhood must provide an
	 * explanation.
	 */

	@Test
	public void driverBrotherhoodRequest() {

		// Request pending from Brotherhood1
		Request requestPending = this.requestService.findOne(this.getEntityId("request2"));

		Object testingData[][] = {

				/** POSTIVE TEST: Brotherhood1 is approving a request from a member **/
				{ "brotherhood1", requestPending, Status.APPROVED, 1, 1, "", null },

				/**
				 * POSTIVE TEST: Brotherhood1 is rejecting a request from a member, writing a
				 * reason
				 **/
				{ "brotherhood1", requestPending, Status.REJECTED, 1, 1, "Yes", null },

				/**
				 * NEGATIVE TEST: Brotherhood2 is trying to approve a request from the
				 * brotherhood1's request list
				 **/
				{ "brotherhood2", requestPending, Status.APPROVED, 1, 1, "", IllegalArgumentException.class },

				/**
				 * NEGATIVE TEST: Brotherhood1 is trying to reject a request with no writing a
				 * reason
				 **/
				{ "brotherhood1", requestPending, Status.REJECTED, 1, 1, "", IllegalArgumentException.class },

				/**
				 * NEGATIVE TEST: Brotherhood1 is trying to approve a request from a member
				 * putting a invalid column and row
				 **/
				{ "brotherhood1", requestPending, Status.APPROVED, 2, 2, "", IllegalArgumentException.class },

		};

		for (int i = 0; i < testingData.length; i++)
			this.templateBrotherhoodRequest((String) testingData[i][0], (Request) testingData[i][1],
					(Status) testingData[i][2], (Integer) testingData[i][3], (Integer) testingData[i][4],
					(String) testingData[i][5], (Class<?>) testingData[i][6]);

	}

	private void templateBrotherhoodRequest(String brotherhood, Request request, Status status, Integer row,
			Integer column, String rejectedReason, Class<?> expected) {

		Class<?> caught = null;
		request.setStatus(status);
		request.setColumnNumber(column);
		request.setReasonDescription(rejectedReason);
		request.setRowNumber(row);
		this.startTransaction();

		try {
			super.authenticate(brotherhood);
			this.requestService.saveRequestWithPreviousChecking(request);
			this.requestService.flush();
			super.unauthenticate();
		} catch (Throwable oops) {
			caught = oops.getClass();
		}

		this.rollbackTransaction();
		super.checkExceptions(expected, caught);

	}

	/**
	 * We are going to test the requirement R11.1
	 *
	 *
	 * R11. An actor who is authenticated as a member must be able to:
	 *
	 * 1. Manage his or her requests to march on a procession, which includes
	 * listing them by status, showing, creating them, and deleting them. Note that
	 * the requests cannot be updated, but they can be deleted as long as they are
	 * in the pending status. Requests to march must be shown according to the
	 * following colour scheme: pending requests must be shown in grey; applications
	 * that are accepted must be shown in green; applications that are rejected must
	 * be shown in orange.
	 *
	 *
	 */
	@Test
	public void driverMemberCreateRequest() {

		Integer paradeIdDraftMode = this.getEntityId("parade1");

		// A Parade that Requested member1 and member4
		Integer paradeIdPublished = this.getEntityId("parade3");

		Object testingData[][] = {
				/** POSTIVE TEST: Member 3 is requesting a published parade **/
				{ "member3", paradeIdPublished, null },

				/**
				 * NEGATIVE TEST: Member 1 is trying to request a published parade that he
				 * requested yet
				 **/
				{ "member1", paradeIdPublished, IllegalArgumentException.class },

				/** NEGATIVE TEST: Member 1 is trying to request a not published parade **/
				{ "member1", paradeIdDraftMode, IllegalArgumentException.class } };

		for (int i = 0; i < testingData.length; i++)
			this.templateMemberRequest((String) testingData[i][0], (Integer) testingData[i][1],
					(Class<?>) testingData[i][2]);

	}

	private void templateMemberRequest(String member, Integer paradeId, Class<?> expected) {

		Class<?> caught = null;
		this.startTransaction();

		try {
			super.authenticate(member);
			Member memberL = this.memberService.getMemberByUsername(member);
			this.requestService.createRequestAsMember(memberL, paradeId);
			this.requestService.flush();
			super.unauthenticate();
		} catch (Throwable oops) {
			caught = oops.getClass();
		}

		this.rollbackTransaction();

		super.checkExceptions(expected, caught);

	}

	@Test
	public void driverMemberDeleteRequest() {

		// Member1's prending request
		Integer requestId = this.getEntityId("request2");

		Object testingData[][] = {
				/** POSTIVE TEST: Member 1 is deleting his pending request **/
				{ "member1", requestId, null },

				/**
				 * NEGATIVE TEST: Member 2 is trying to deleting the member1's pending request
				 **/
				{ "member2", requestId, IllegalArgumentException.class } };

		for (int i = 0; i < testingData.length; i++)
			this.templateMemberDeleteRequest((String) testingData[i][0], (Integer) testingData[i][1],
					(Class<?>) testingData[i][2]);

	}

	private void templateMemberDeleteRequest(String member, Integer requestId, Class<?> expected) {

		Class<?> caught = null;
		this.startTransaction();

		try {
			super.authenticate(member);
			Member memberL = this.memberService.getMemberByUsername(member);
			this.requestService.deleteRequestAsMember(memberL, requestId);
			this.requestService.flush();
			super.unauthenticate();
		} catch (Throwable oops) {
			caught = oops.getClass();
		}

		this.rollbackTransaction();

		super.checkExceptions(expected, caught);

	}

	/**
	 *
	 * Finally we are going to test the requirement R32:
	 *
	 *
	 * R32. The system must generate automatic notifications on the following
	 * events: a request to march changes its status; a brotherhood enrols a member;
	 * a member drops out of a brotherhood; a procession is published.
	 *
	 */

	@Test
	public void driverNotificationRequest() {
		// Request pending from Brotherhood1
		Request requestPending = this.requestService.findOne(this.getEntityId("request2"));

		Object testingData[][] = {

				/** POSTIVE TEST: Brotherhood1 is approving a request from a member **/
				{ "brotherhood1", requestPending, Status.APPROVED, 1, 1, "", null },

				/**
				 * NEGATIVE TEST: Brotherhood1 is rejecting a request from a member, writing a
				 * reason.
				 **/
				{ "brotherhood1", requestPending, Status.REJECTED, 1, 1, "Yes", null },

				/**
				 * NEGATIVE TEST: Brotherhood2 is trying to approve a request from the
				 * brotherhood1's request list, so the member does't receive a notification
				 * message
				 **/
				{ "brotherhood2", requestPending, Status.APPROVED, 1, 1, "", IllegalArgumentException.class },

		};

		for (int i = 0; i < testingData.length; i++)
			this.templateNotificationRequest((String) testingData[i][0], (Request) testingData[i][1],
					(Status) testingData[i][2], (Integer) testingData[i][3], (Integer) testingData[i][4],
					(String) testingData[i][5], (Class<?>) testingData[i][6]);

	}

	private void templateNotificationRequest(String brotherhood, Request request, Status status, Integer row,
			Integer column, String rejectedReason, Class<?> expected) {

		Class<?> caught = null;
		request.setStatus(status);
		request.setColumnNumber(column);
		request.setReasonDescription(rejectedReason);
		request.setRowNumber(row);
		this.startTransaction();
		Member member = request.getMember();
		List<Message> before = member.getBoxes().get(4).getMessages();

		try {
			super.authenticate(brotherhood);
			this.requestService.saveRequestWithPreviousChecking(request);
			this.requestService.flush();
			Member memberAfter = this.memberService.getMemberByUsername(member.getUserAccount().getUsername());
			List<Message> after = memberAfter.getBoxes().get(4).getMessages();
			Assert.isTrue(before.size() + 1 == after.size());
			super.unauthenticate();
		} catch (Throwable oops) {
			caught = oops.getClass();
		}

		this.rollbackTransaction();
		super.checkExceptions(expected, caught);

	}

}
