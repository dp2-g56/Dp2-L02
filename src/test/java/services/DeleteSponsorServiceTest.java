
package services;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import utilities.AbstractTest;
import domain.Sponsor;

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

			Integer numActores = this.sponsorService.findAll().size();

			Sponsor sponsor = new Sponsor();

			sponsor = this.sponsorService.loggedSponsor();

			this.boxService.deleteAllBoxes();
			this.messageService.updateSendedMessageByLogguedActor();
			this.socialProfileService.deleteAllSocialProfiles();
			this.sponsorshipService.deleteAllSponsorships();
			this.sponsorService.delete(sponsor);

			Integer numActoresDeleted = this.sponsorService.findAll().size();

			Assert.isTrue(numActores != numActoresDeleted);

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
