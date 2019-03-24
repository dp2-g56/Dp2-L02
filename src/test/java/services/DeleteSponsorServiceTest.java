
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
import domain.Actor;
import domain.Box;
import domain.Message;
import domain.SocialProfile;
import domain.Sponsor;
import domain.Sponsorship;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
	"classpath:spring/junit.xml"
})
@Transactional
public class DeleteSponsorServiceTest extends AbstractTest {

	@Autowired
	private ActorService			actorService;

	@Autowired
	private SponsorService			sponsorService;

	@Autowired
	private SocialProfileService	socialProfileService;

	@Autowired
	private SponsorshipService		sponsorshipService;

	@Autowired
	private MessageService			messageService;

	@Autowired
	private BoxService				boxService;


	@Test
	public void driverDeleteAllBoxes() {

		Object testingData[][] = {
			{
				//Positive test, deleting a no system box that admin1 own
				"sponsor1", null
			}, {
				//Positive test, deleting a no system box that admin1 own
				"", IllegalArgumentException.class
			}, {
				//Positive test, deleting a no system box that admin1 own
				"brotherhood1", IllegalArgumentException.class
			},

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

			this.sponsorService.loggedAsSponsor();
			Integer numActores = this.sponsorService.findAll().size();

			Sponsor sponsor = new Sponsor();
			sponsor = this.sponsorService.loggedSponsor();

			Actor deleted = this.actorService.getActorByUsername("DELETED");

			List<Box> boxes = new ArrayList<Box>();
			List<SocialProfile> socialProfiles = new ArrayList<SocialProfile>();
			List<Sponsorship> sponsorships = new ArrayList<Sponsorship>();
			List<Message> messages = new ArrayList<Message>();

			List<Box> boxesDeleted = new ArrayList<Box>();
			List<SocialProfile> socialProfilesDeleted = new ArrayList<SocialProfile>();
			List<Sponsorship> sponsorshipsDeleted = new ArrayList<Sponsorship>();
			List<Message> messagesDeleted = new ArrayList<Message>();

			boxes = sponsor.getBoxes();
			socialProfiles = sponsor.getSocialProfiles();
			sponsorships = sponsor.getSponsorships();
			messages = this.messageService.allMessagesOfActor(sponsor);

			sponsor = this.sponsorService.loggedSponsor();

			this.sponsorService.deleteSponsor();

			Integer numActoresDeleted = this.sponsorService.findAll().size();

			Assert.isTrue(numActores != numActoresDeleted);

			boxesDeleted = this.boxService.findAll();
			socialProfilesDeleted = this.socialProfileService.findAll();
			sponsorshipsDeleted = this.sponsorshipService.findAll();
			messagesDeleted = this.messageService.findAll();

			Assert.isTrue(!boxesDeleted.contains(boxes));
			Assert.isTrue(!socialProfilesDeleted.contains(socialProfiles));
			Assert.isTrue(!sponsorshipsDeleted.contains(sponsorships));
			Assert.isTrue(!messagesDeleted.contains(messages));

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
