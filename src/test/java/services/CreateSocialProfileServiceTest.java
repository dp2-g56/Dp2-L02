
package services;

import java.util.List;

import javax.validation.ConstraintViolationException;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import utilities.AbstractTest;
import domain.Actor;
import domain.SocialProfile;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
	"classpath:spring/junit.xml"
})
@Transactional
public class CreateSocialProfileServiceTest extends AbstractTest {

	@Autowired
	private ActorService			actorService;

	@Autowired
	private SocialProfileService	socialProfileService;


	@Test
	public void driverCreateSocialprofile() {

		Object testingData[][] = {
			{
				//Positive test, all the params OK
				"member1", "nick", "name", "https://trello.com/b/Jsbt8NQy/acme-parade", null
			}, {
				//Negative test, Blank nick
				"member1", "", "name", "https://trello.com/b/Jsbt8NQy/acme-parade", ConstraintViolationException.class
			}, {
				//Negative test, Blank name 
				"member1", "nick", "", "https://trello.com/b/Jsbt8NQy/acme-parade", ConstraintViolationException.class
			}, {
				//Negative test, Blank URL
				"member1", "nick", "name", "", ConstraintViolationException.class
			}, {
				//Negative test, Not an URL
				"member1", "nick", "name", "fafafafa", ConstraintViolationException.class
			}
		};

		for (int i = 0; i < testingData.length; i++)
			this.templateCreateSocialProfile((String) testingData[i][0], (String) testingData[i][1], (String) testingData[i][2], (String) testingData[i][3], (Class<?>) testingData[i][4]);
	}

	protected void templateCreateSocialProfile(String username, String nick, String name, String profileLink, Class<?> expected) {

		Class<?> caught = null;

		try {

			//En cada iteraccion comenzamos una transaccion, de esya manera, no se toman valores residuales de otros test
			this.startTransaction();

			super.authenticate(username);

			Actor actor = this.actorService.loggedActor();

			SocialProfile socialProfile = this.socialProfileService.create();

			socialProfile.setNick(nick);
			socialProfile.setProfileLink(profileLink);
			socialProfile.setName(name);

			SocialProfile saved = this.socialProfileService.save(socialProfile);
			List<SocialProfile> socialProfiles = actor.getSocialProfiles();

			if (socialProfiles.contains(socialProfile)) {
				socialProfiles.remove(saved);
				socialProfiles.add(saved);
			} else
				socialProfiles.add(saved);

			actor.setSocialProfiles(socialProfiles);

			this.actorService.save(actor);

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
