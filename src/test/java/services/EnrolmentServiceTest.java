
package services;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import utilities.AbstractTest;
import domain.Brotherhood;
import domain.Enrolment;
import domain.Member;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
	"classpath:spring/junit.xml"
})
@Transactional
public class EnrolmentServiceTest extends AbstractTest {

	@Autowired
	private MemberService		memberService;

	@Autowired
	private EnrolmentService	enrolmentService;

	@Autowired
	private BrotherhoodService	brotherhoodService;


	/**
	 * The following tests are going to be documented according to the following especification:
	 * 
	 * On the top of each Driver there will be a little piece of text explaining the action that is going to be tested
	 * 
	 * For each testing data set, there will be an explanation with the following sections:
	 * a) Requirement tested
	 * b) In the case of negative tests, the business rule that is intended to be broken
	 * 
	 * Data and Sentence coverage will be explained in the attached document
	 */

	//---------------------------------MEMBER----------------------------------------
	//-------------------------------------------------------------------------------

	//CREATE AN ENROLMENT

	/**
	 * The data of this driver is focused to test the creation of a pending enrolment by
	 * a member that is not part of a brotherhood. Requirement 11.2 from Acme-Madruga
	 */

	@Test
	public void driverCreatePendingEnrolment() {
		Object testingData[][] = {
			{
				"member1", "brotherhood3", null
			/**
			 * a)Requirement 11.2
			 * b)There is no error expected here, a member creates a enrolment to a brotherhood he is not enroled in
			 */
			}, {
				null, "brotherhood3", IllegalArgumentException.class
			/**
			 * a)Requirement 11.2
			 * b)An anonymous user tries to create an enrolment to a brotherhood, an Illegal Argument Exception is expected
			 */
			}, {
				"member3", "brotherhood3", null
			/**
			 * a)Requirement 11.2
			 * b)There is no error expected here, a member creates a enrolment to a brotherhood in which he has
			 * another pending or accepted enrolment
			 * If a member with an accepted or pending enrolment to a brotherhood tries to create a new enrolment,
			 * it won't be created and the system will advertise him, but no exception will be thrown
			 */
			}
		};
		for (int i = 0; i < testingData.length; i++)
			this.templateCreatePendingEnrolment((String) testingData[i][0], (String) testingData[i][1], (Class<?>) testingData[i][2]);

	}

	protected void templateCreatePendingEnrolment(String memberUsername, String brotherhoodUsername, Class<?> expected) {

		Class<?> caught = null;

		try {
			this.authenticate(memberUsername);

			int brotherhoodId = super.getEntityId(brotherhoodUsername);
			Brotherhood brotherhood = this.brotherhoodService.findOne(brotherhoodId);
			Member member = this.memberService.loggedMember();
			Enrolment enrolment = new Enrolment();

			boolean res = this.enrolmentService.enrolmentMemberComprobation(brotherhood);

			if (!res)
				this.enrolmentService.createEnrolment(brotherhood, enrolment, member);

			this.enrolmentService.flush();

			this.unauthenticate();

		} catch (Throwable oops) {
			caught = oops.getClass();
		}
		super.checkExceptions(expected, caught);

	}

	//DROPOUT FROM A BROTHERHOOD

	/**
	 * The data of this driver is focused to test the dropout of a Brotherhood by
	 * a member that is part of a brotherhood. Requirement 11.2 from Acme-Madruga
	 */

	@Test
	public void driverDropout() {
		Object testingData[][] = {
			{
				"member1", "enrolment2", null
			/**
			 * a)Requirement 11.2
			 * b)There is no error expected here, a member drops out from a brotherhood in which he
			 * is enroled (enrolment2 is an accepted enrolment between member1 and brotherhood1)
			 */

			}, {
				"member2", "enrolment2", IllegalArgumentException.class
			/**
			 * a)Requirement 11.2
			 * b)A member tries to change the status of an enrolment that belongs to
			 * another member and an IllegalArgumentException is thrown.
			 */
			}, {
				null, "enrolment2", IllegalArgumentException.class
			/**
			 * a)Requirement 11.2
			 * b)An unauthenticated member tries to change the status of an enrolment
			 */
			}
		};

		for (int i = 0; i < testingData.length; i++)
			this.templateDropout((String) testingData[i][0], (String) testingData[i][1], (Class<?>) testingData[i][2]);
	}

	protected void templateDropout(String memberUsername, String enrolmentBean, Class<?> expected) {

		Class<?> caught = null;

		try {
			this.authenticate(memberUsername);

			int enrolmentId = super.getEntityId(enrolmentBean);

			this.enrolmentService.dropout(enrolmentId);

			this.enrolmentService.flush();

			this.unauthenticate();

		} catch (Throwable oops) {
			caught = oops.getClass();
		}
		super.checkExceptions(expected, caught);

	}

	//---------------------------------BROTHERHOOD-----------------------------------
	//-------------------------------------------------------------------------------

	//REJECT A PENDING ENROLMENT
	/**
	 * The data of this driver is focused to test the reject of a pending enrolment
	 * by a brotherhood. Requirement 10.3 from Acme-Madruga
	 * There are no pending enrolments in PopulateDatabase.xml, so we will create a
	 * new Pending enrolment logged as Brotherhood1 in the first place
	 */

	@Test
	public void driverRejectPendingEnrolment() {
		Object testingData[][] = {
			{
				"brotherhood3", null
			/**
			 * a)Requirement 10.3
			 * b)A brotherhood rejects a pending record, no Exception Expected
			 */
			}, {
				"brotherhood4", IllegalArgumentException.class
			/**
			 * a)Requirement 10.3
			 * b)Illegal Argument Exception
			 * A brotherhood tries to reject a Pending enrolment that refers to another brotherhood
			 */
			}, {
				null, IllegalArgumentException.class
			/**
			 * a)Requirement 10.3
			 * b)Illegal Argument Exception
			 * An anonymous user tries to reject a pending Enrolment
			 */
			}

		};
		for (int i = 0; i < testingData.length; i++)
			this.templateRejectPendingEnrolment((String) testingData[i][0], (Class<?>) testingData[i][1]);
	}

	protected void templateRejectPendingEnrolment(String brotherhoodUsername, Class<?> expected) {

		Class<?> caught = null;

		try {
			//////////////////////////////////////////////////
			this.authenticate("member1");

			int brotherhoodId = super.getEntityId("brotherhood3");
			Brotherhood brotherhood = this.brotherhoodService.findOne(brotherhoodId);
			Member member = this.memberService.loggedMember();
			Enrolment enrolment = new Enrolment();

			Enrolment saved = this.enrolmentService.createEnrolment(brotherhood, enrolment, member);

			this.unauthenticate();
			/////////////////////////////////////////////////
			this.authenticate(brotherhoodUsername);

			this.enrolmentService.rejectEnrolment(saved);

			this.enrolmentService.flush();

			this.unauthenticate();

		} catch (Throwable oops) {
			caught = oops.getClass();
		}
		super.checkExceptions(expected, caught);

	}

}
