
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
			super.authenticate(username);
			this.paradeService.listAcceptedParadeIfSponsor();
			super.unauthenticate();
		} catch (Throwable oops) {
			caught = oops.getClass();
		}

		super.checkExceptions(expected, caught);

	}

	/**
	 * Where are going to test the requirement 3.1
	 *
	 *
	 * 3. An actor who is authenticated as a brotherhood must be able to:
	 *
	 * 1. List their parades grouped by their status.
	 *
	 **/

	@Test
	public void driverListParadesBrotherhood() {

		Brotherhood brotherhood1 = this.brotherhoodService.getBrotherhoodByUsername("brotherhood1");

		Parade paradeSubmitted = brotherhood1.getParades().get(1);

		Brotherhood brotherhood2 = this.brotherhoodService.getBrotherhoodByUsername("brotherhood2");

		Object testingData[][] = {

				/** POSITIVE TEST: A brotherhood is filter his parades by Submitted. **/
				{ brotherhood1, paradeSubmitted, "SUBMITTED", null },

				/**
				 * NEGATIVE TEST: Brotherhood2 is trying to filter his parade and getting a
				 * parade that don't own.
				 **/
				{ brotherhood2, paradeSubmitted, "SUBMITTED", IllegalArgumentException.class } };
		for (int i = 0; i < testingData.length; i++)
			this.templateListParadesBrotherhood((Brotherhood) testingData[i][0], (Parade) testingData[i][1],
					(String) testingData[i][2], (Class<?>) testingData[i][3]);

	}

	private void templateListParadesBrotherhood(Brotherhood bro, Parade parade, String option, Class<?> expected) {

		Class<?> caught = null;

		try {
			super.authenticate(bro.getUserAccount().getUsername());
			List<Parade> paradesFiltered = this.paradeService.filterParadesBrotherhood(bro, option);
			Assert.isTrue(paradesFiltered.contains(parade));
			super.unauthenticate();
		} catch (Throwable oops) {
			caught = oops.getClass();
		}

		super.checkExceptions(expected, caught);

	}

}
