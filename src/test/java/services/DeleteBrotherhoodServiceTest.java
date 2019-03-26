
package services;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import utilities.AbstractTest;
import domain.Box;
import domain.Brotherhood;
import domain.Enrolment;
import domain.Message;
import domain.Parade;
import domain.SocialProfile;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
	"classpath:spring/junit.xml"
})
@Transactional
public class DeleteBrotherhoodServiceTest extends AbstractTest {

	@Autowired
	private BrotherhoodService		brotherhoodService;

	@Autowired
	private SocialProfileService	socialProfileService;

	@Autowired
	private SponsorshipService		sponsorshipService;

	@Autowired
	private MessageService			messageService;

	@Autowired
	private BoxService				boxService;

	@Autowired
	private ParadeService			paradeService;

	@Autowired
	private EnrolmentService		enrolmentService;


	@Test
	public void driverDeleteAllBoxes() {

		Object testingData[][] = {
			{
				//Positive test, deleting a no system box that admin1 own
				"brotherhood1", null
			}

		};

		for (int i = 0; i < testingData.length; i++)
			this.templateDeleteAllBoxes((String) testingData[i][0], (Class<?>) testingData[i][1]);
	}

	protected void templateDeleteAllBoxes(String username, Class<?> expected) {

		Class<?> caught = null;

		try {

			//En cada iteraccion comenzamos una transaccion, de esya manera, no se toman valores residuales de otros test
			this.startTransaction();

			super.authenticate(username);

			this.brotherhoodService.loggedAsBrotherhood();

			Brotherhood brotherhood = new Brotherhood();
			brotherhood = this.brotherhoodService.loggedBrotherhood();

			List<Box> boxes = new ArrayList<Box>();
			List<SocialProfile> socialProfiles = new ArrayList<SocialProfile>();
			List<Parade> parades = new ArrayList<Parade>();
			List<Message> messages = new ArrayList<Message>();
			List<Enrolment> enrolments = new ArrayList<Enrolment>();

			List<Box> boxesDeleted = new ArrayList<Box>();
			List<SocialProfile> socialProfilesDeleted = new ArrayList<SocialProfile>();
			List<Parade> paradesDeleted = new ArrayList<Parade>();
			List<Message> messagesDeleted = new ArrayList<Message>();
			List<Enrolment> enrolmentsDeleted = new ArrayList<Enrolment>();

			boxes = brotherhood.getBoxes();
			socialProfiles = brotherhood.getSocialProfiles();
			parades = brotherhood.getParades();
			messages = this.messageService.allMessagesOfActor(brotherhood);
			enrolments = brotherhood.getEnrolments();

			Integer numActores = this.brotherhoodService.findAll().size();

			this.brotherhoodService.deleteBrotherhood();

			Integer numActoresDeleted = this.brotherhoodService.findAll().size();

			Assert.isTrue(numActores != numActoresDeleted);

			boxesDeleted = this.boxService.findAll();
			socialProfilesDeleted = this.socialProfileService.findAll();
			paradesDeleted = this.paradeService.findAll();
			messagesDeleted = this.messageService.findAll();
			enrolmentsDeleted = this.enrolmentService.findAll();

			Assert.isTrue(!boxesDeleted.contains(boxes));
			Assert.isTrue(!socialProfilesDeleted.contains(socialProfiles));
			Assert.isTrue(!paradesDeleted.contains(parades));
			Assert.isTrue(!messagesDeleted.contains(messages));
			Assert.isTrue(!enrolmentsDeleted.contains(enrolments));

			super.authenticate(null);

		} catch (Throwable oops) {
			caught = oops.getClass();
		} finally {
			//Se fuerza el rollback para que no de ningun problema la siguiente iteracion
			this.rollbackTransaction();
		}

		super.checkExceptions(expected, caught);

	}
}
