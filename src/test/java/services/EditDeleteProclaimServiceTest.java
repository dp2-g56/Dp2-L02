
package services;

import java.util.Date;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import utilities.AbstractTest;
import domain.Proclaim;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
	"classpath:spring/junit.xml"
})
@Transactional
public class EditDeleteProclaimServiceTest extends AbstractTest {

	@Autowired
	private ProclaimService	proclaimService;


	@Test
	public void driverPublishProclaim() {

		Date thisMoment = new Date();
		thisMoment.setTime(thisMoment.getTime() - 1000);

		Object testingData[][] = {
			{
				// Positive test
				"chapter1", "proclaim1", IllegalArgumentException.class
			}, {
				// Positive test
				"chapter1", "proclaim4", IllegalArgumentException.class
			},
		};

		for (int i = 0; i < testingData.length; i++)
			this.templateEditProclaim((String) testingData[i][0], (String) testingData[i][1], (Class<?>) testingData[i][2]);
	}

	protected void templateEditProclaim(String username, String proclaimRe, Class<?> expected) {
		Class<?> caught = null;

		try {
			// En cada iteraccion comenzamos una transaccion, de esta manera, no se toman
			// valores residuales de otros test
			this.startTransaction();

			super.authenticate(username);
			Proclaim proclaim = this.proclaimService.findOne(super.getEntityId(proclaimRe));

			this.proclaimService.saveProclaim(proclaim);
			this.proclaimService.flush();

			super.unauthenticate();

		} catch (Throwable oops) {
			caught = oops.getClass();
		} finally {
			// Se fuerza el rollback para que no de ningun problema la siguiente iteracion
			this.rollbackTransaction();
		}
		super.checkExceptions(expected, caught);
	}

}
