
package services;

import java.util.List;

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

	// AQUI

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

}
