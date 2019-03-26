
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

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
	"classpath:spring/junit.xml"
})
@Transactional
public class DeleteBoxServiceTest extends AbstractTest {

	@Autowired
	private BoxService		boxService;

	@Autowired
	private ActorService	actorService;


	@Test
	public void driverDeleteBox() {

		Object testingData[][] = {
			{
				//Positive test, deleting a no system box that admin1 own
				"admin1", "noSystemBoxAdmin1", null
			}, {
				//Negative test, deleting a system box
				"admin1", "spamBoxAdmin1", IllegalArgumentException.class
			}, {
				//Negative test, not logged
				"", "noSystemBoxAdmin1", IllegalArgumentException.class
			}, {
				//Negative test, deleting a no system box that is not yours
				"admin1", "noSystemBoxAdmin2", IllegalArgumentException.class
			}

		};

		for (int i = 0; i < testingData.length; i++)
			this.templateDeleteBox((String) testingData[i][0], (String) testingData[i][1], (Class<?>) testingData[i][2]);
	}

	protected void templateDeleteBox(String username, String boxName, Class<?> expected) {

		Class<?> caught = null;

		try {

			//En cada iteraccion comenzamos una transaccion, de esya manera, no se toman valores residuales de otros test
			this.startTransaction();

			super.authenticate(username);

			Actor actor = this.actorService.loggedActor();
			Box box = new Box();
			List<Box> sonBoxes = new ArrayList<Box>();

			box = this.boxService.findOne(super.getEntityId(boxName));

			sonBoxes = this.boxService.getSonsBox(box);

			this.boxService.deleteBox(box);

			Assert.isTrue(!actor.getBoxes().contains(box));
			Assert.isTrue(!actor.getBoxes().contains(sonBoxes));

			this.boxService.flush();

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
