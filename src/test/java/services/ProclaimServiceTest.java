
package services;

import java.util.Date;

import javax.validation.ConstraintViolationException;

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
public class ProclaimServiceTest extends AbstractTest {

	@Autowired
	private ProclaimService	proclaimService;


	@Test
	public void driverRegister() {

		Date thisMoment = new Date();
		thisMoment.setTime(thisMoment.getTime() - 1000);

		Object testingData[][] = {
			{
				//Positive test
				"chapter1", thisMoment, "testPositivo", null
			}, {
				//Null moment
				"chapter1", null, "testNegativo", ConstraintViolationException.class
			}, {
				//Blank description
				"chapter1", thisMoment, "", ConstraintViolationException.class
			}, {
				//Not logged
				"", thisMoment, "testNegativo", IllegalArgumentException.class
			}, {
				//Wrong actor
				"brotherhood1", thisMoment, "testNegativo", IllegalArgumentException.class
			}, {
				//Null description
				"chapter1", thisMoment, null, ConstraintViolationException.class
			}
		};

		for (int i = 0; i < testingData.length; i++)
			this.templateRegister((String) testingData[i][0], (Date) testingData[i][1], (String) testingData[i][2], (Class<?>) testingData[i][3]);
	}

	protected void templateRegister(String username, Date moment, String descripcion, Class<?> expected) {
		Class<?> caught = null;

		try {
			//En cada iteraccion comenzamos una transaccion, de esya manera, no se toman valores residuales de otros test
			this.startTransaction();

			super.authenticate(username);
			Proclaim proclaim = this.proclaimService.createProclaim();

			proclaim.setDescription(descripcion);
			proclaim.setPublishMoment(moment);

			this.proclaimService.saveProclaim(proclaim);
			this.proclaimService.flush();

			super.unauthenticate();

		} catch (Throwable oops) {
			caught = oops.getClass();
		} finally {
			//Se fuerza el rollback para que no de ningun problema la siguiente iteracion
			this.rollbackTransaction();
		}
		super.checkExceptions(expected, caught);
	}
}
