
package services;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.validation.ConstraintViolationException;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import domain.Brotherhood;
import domain.Parade;
import utilities.AbstractTest;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:spring/junit.xml" })
@Transactional
public class ParadeServiceTest extends AbstractTest {

	@Autowired
	private ParadeService paradeService;
	@Autowired
	private BrotherhoodService brotherhoodService;
	@Autowired
	private FloatService floatService;

	/**
	 * Test the specific list that is shown to the sponsor of the accepted parades,
	 * which may sponsor.
	 */
	@Test
	public void driverListParadesIfSponsor() {

		Object testingData[][] = {
				// Positive test
				{ "sponsor1", null },
				// Negative test: Trying to access with a different role
				{ "member1", IllegalArgumentException.class } };
		for (int i = 0; i < testingData.length; i++)
			this.templateListParadesIfSponsor((String) testingData[i][0], (Class<?>) testingData[i][1]);
	}

	private void templateListParadesIfSponsor(String username, Class<?> expected) {

		Class<?> caught = null;

		try {
			super.startTransaction();

			super.authenticate(username);
			this.paradeService.listAcceptedParadeIfSponsor();
			super.unauthenticate();
		} catch (Throwable oops) {
			caught = oops.getClass();
		} finally {
			super.rollbackTransaction();
		}

		super.checkExceptions(expected, caught);

	}

	/**
	 * Test the use case detailed in requirement 10.2 (Acme-Madruga): Manage their
	 * parades, which includes listing them
	 */
	@Test
	public void driverListParadesIfBrotherhood() {

		Object testingData[][] = {
				// Positive test: Listing all parades of a brotherhood
				{ "", "brotherhood1", null },
				// Positive test: Listing rejected parades of a brotherhood
				{ "REJECTED", "brotherhood1", null },
				// Positive test: Listing accepted parades of a brotherhood
				{ "ACCEPTED", "brotherhood1", null },
				// Positive test: Listing submitted parades of a brotherhood
				{ "SUBMITTED", "brotherhood1", null },
				// Positive test: Listing draft parades of a brotherhood
				{ "DRAFT", "brotherhood1", null },
				// Negative test: Trying to list all parades with a different role
				{ "", "member1", IllegalArgumentException.class },
				// Negative test: Trying to list rejected parades with a different role
				{ "REJECTED", "member1", IllegalArgumentException.class },
				// Negative test: Trying to list accepted parades with a different role
				{ "ACCEPTED", "member1", IllegalArgumentException.class },
				// Negative test: Trying to list submitted parades with a different role
				{ "SUBMITTED", "member1", IllegalArgumentException.class },
				// Negative test: Trying to list draft parades with a different role
				{ "DRAFT", "member1", IllegalArgumentException.class } };

		for (int i = 0; i < testingData.length; i++)
			this.templateListParadesIfBrotherhood((String) testingData[i][0], (String) testingData[i][1],
					(Class<?>) testingData[i][2]);
	}

	private void templateListParadesIfBrotherhood(String select, String username, Class<?> expected) {

		Class<?> caught = null;

		try {
			this.startTransaction();

			super.authenticate(username);

			Brotherhood bro = this.brotherhoodService.loggedBrotherhood();

			List<Parade> filterParades = this.paradeService.filterParadesBrotherhood(bro, select);
			List<Parade> allParades = bro.getParades();

			if (select.contentEquals(""))
				Assert.isTrue(allParades.size() == filterParades.size());
			else
				Assert.isTrue(allParades.size() == (filterParades.size() + (allParades.size() - filterParades.size())));

			super.unauthenticate();
		} catch (Throwable oops) {
			caught = oops.getClass();
		} finally {
			this.rollbackTransaction();
		}

		super.checkExceptions(expected, caught);

	}

	// AQUI

	/**
	 * Test the use case detailed in requirement 10.2 (Acme-Madruga): Manage their
	 * parades, which includes creating them (New parade and new float at the same
	 * time)
	 */
	@Test
	public void driverCreateParadeAndFloatIfBrotherhood() {
		Calendar c = Calendar.getInstance();
		c.set(Calendar.DAY_OF_MONTH, 20);
		c.set(Calendar.MONTH, 10);
		Date futureDate = c.getTime();

		c.set(Calendar.MONTH, 2);
		Date pastDate = c.getTime();

		Object testingData[][] = {
				// Positive test
				{ "Parade title", "Parade description", futureDate, true, 5, 10, "Float title", "Float description",
						"brotherhood1", null },
				// Negative test: Trying to create a parade and a float with a past moment
				{ "Parade title", "Parade description", pastDate, true, 5, 10, "Float title", "Float description",
						"brotherhood1", ConstraintViolationException.class },
				// Negative test: Trying to create a parade and a float with a blank parade
				// title
				{ "", "Parade description", futureDate, true, 5, 10, "Float title", "Float description", "brotherhood1",
						ConstraintViolationException.class },
				// Negative test: Trying to create a parade and a float with with a different
				// role
				{ "Parade title", "Parade description", futureDate, true, 5, 10, "Float title", "Float description",
						"member1", IllegalArgumentException.class },
				// Negative test: Trying to create a parade and a float with a brotherhood
				// without area
				{ "Parade title", "Parade description", futureDate, true, 5, 10, "Float title", "Float description",
						"brotherhood4", IllegalArgumentException.class } };

		for (int i = 0; i < testingData.length; i++)
			this.templateCreateParadeAndFloatIfBrotherhood((String) testingData[i][0], (String) testingData[i][1],
					(Date) testingData[i][2], (Boolean) testingData[i][3], (Integer) testingData[i][4],
					(Integer) testingData[i][5], (String) testingData[i][6], (String) testingData[i][7],
					(String) testingData[i][8], (Class<?>) testingData[i][9]);
	}

	private void templateCreateParadeAndFloatIfBrotherhood(String paradeTitle, String paradeDescription,
			Date paradeMoment, Boolean draftMode, Integer rowNumber, Integer columnNumber, String floatTitle,
			String floatDescription, String username, Class<?> expected) {
		Class<?> caught = null;

		try {
			this.startTransaction();

			super.authenticate(username);

			Parade parade = this.paradeService.create();
			domain.Float floatt = this.floatService.create();

			parade.setTitle(paradeTitle);
			parade.setDescription(paradeDescription);
			parade.setMoment(paradeMoment);
			parade.setIsDraftMode(draftMode);
			parade.setRowNumber(rowNumber);
			parade.setColumnNumber(columnNumber);

			floatt.setTitle(floatTitle);
			floatt.setDescription(floatDescription);

			this.paradeService.saveFloatAndAssignToParade(floatt, parade);

			this.paradeService.flush();

			super.unauthenticate();
		} catch (Throwable oops) {
			caught = oops.getClass();
		} finally {
			this.rollbackTransaction();
		}

		super.checkExceptions(expected, caught);

	}

}
