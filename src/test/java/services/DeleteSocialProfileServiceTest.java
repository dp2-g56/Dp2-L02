
package services;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import utilities.AbstractTest;
import domain.SocialProfile;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
	"classpath:spring/junit.xml"
})
@Transactional
public class DeleteSocialProfileServiceTest extends AbstractTest {

	@Autowired
	private ActorService			actorService;

	@Autowired
	private SocialProfileService	socialProfileService;


	@Test
	public void driverDeleteSocialprofile() {

		Object testingData[][] = {
			{
				//Positive test, its your social profile
				"member1", "socialProfile10", null
			}, {
				//Positive test, its your social profile
				"member1", "socialProfile11", null
			}, {
				//Negative test, try to delete a social profile that is not yours
				"member1", "socialProfile1", IllegalArgumentException.class
			}, {
				//Negative test, try to delete a social profile when you are not logged
				"", "socialProfile1", IllegalArgumentException.class
			}
		};

		for (int i = 0; i < testingData.length; i++)
			this.templateDeleteSocialProfile((String) testingData[i][0], (String) testingData[i][1], (Class<?>) testingData[i][2]);
	}

	protected void templateDeleteSocialProfile(String username, String socialProfileRe, Class<?> expected) {

		Class<?> caught = null;

		try {

			//En cada iteraccion comenzamos una transaccion, de esya manera, no se toman valores residuales de otros test
			this.startTransaction();

			super.authenticate(username);

			SocialProfile socialProfile = this.socialProfileService.findOne(super.getEntityId(socialProfileRe));

			this.socialProfileService.deleteSocialProfile(socialProfile);
			this.socialProfileService.flush();

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
