
package services;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import utilities.AbstractTest;
import domain.Box;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
	"classpath:spring/junit.xml"
})
@Transactional
public class CreateBoxServiceTest extends AbstractTest {

	@Autowired
	private BoxService	boxService;


	@Test
	public void driverCreateBox() {

		Object testingData[][] = {
			{
				//Positive test, create a box with father Inbox
				"admin1", "box", "inBoxAdmin1", null
			}, {
				//Positive test, create a box with no father box
				"admin1", "box", "", null
			}, {
				//Negative test, null fatherBox
				"admin1", "box", null, NullPointerException.class
			}, {
				//Negative test, Strange fatherBoc
				"admin1", "box", "fafafa", AssertionError.class
			}, {
				//Negative test, not logged
				"", "box", "", IllegalArgumentException.class
			}, {
				//Negative test, not logged
				"admin1", "box", "inBoxAdmin2", IllegalArgumentException.class
			}
		};

		for (int i = 0; i < testingData.length; i++)
			this.templateCreateBox((String) testingData[i][0], (String) testingData[i][1], (String) testingData[i][2], (Class<?>) testingData[i][3]);
	}

	protected void templateCreateBox(String username, String name, String fatherbox, Class<?> expected) {

		Class<?> caught = null;

		try {

			//En cada iteraccion comenzamos una transaccion, de esya manera, no se toman valores residuales de otros test
			this.startTransaction();

			super.authenticate(username);

			Box fatherBox = new Box();
			Box box = this.boxService.create();

			if (!fatherbox.isEmpty()) {
				fatherBox = this.boxService.findOne(super.getEntityId(fatherbox));
				box.setFatherBox(fatherBox);
			}

			box.setName(name);

			this.boxService.save(box);

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
