
package services;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import utilities.AbstractTest;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
	"classpath:spring/junit.xml"
})
@Transactional
public class DeleteAllBoxesServiceTest extends AbstractTest {

	@Autowired
	private BoxService		boxService;

	@Autowired
	private ActorService	actorService;


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

			this.boxService.deleteAllBoxes();

			this.boxService.flush();

			Assert.isTrue(this.actorService.loggedActor().getBoxes().size() == 0);

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
