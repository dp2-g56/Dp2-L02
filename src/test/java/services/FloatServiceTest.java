
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

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:spring/junit.xml" })
@Transactional
public class FloatServiceTest extends AbstractTest {

	private static final String paradeService = null;

	@Autowired
	private FloatService floatService;

	@Autowired
	private BrotherhoodService brotherhoodService;

	/**
	 * Test the use case detailed in requirement 10.1 (Acme-Madruga project): Manage
	 * their floats, which includes listing them
	 */
	@Test
	public void driverListFloatsAndPicturesIfBrotherhood() {

		Object testingData[][] = {
				// Positive test
				{ "brotherhood1", true, null },
				// Negative test: Trying to access with a different role
				{ "member1", true, IllegalArgumentException.class },
				// Negative test: Trying to list pictures with the parade in draft mode
				{ "brotherhood1", false, IllegalArgumentException.class } };
		for (int i = 0; i < testingData.length; i++)
			this.templateListFloatsAndPicturesIfBrotherhood((String) testingData[i][0], (Boolean) testingData[i][1],
					(Class<?>) testingData[i][2]);
	}

	private void templateListFloatsAndPicturesIfBrotherhood(String username, Boolean paradeInFinal, Class<?> expected) {

		Class<?> caught = null;

		try {
			super.startTransaction();

			super.authenticate(username);

			List<domain.Float> floats = new ArrayList<>();
			floats = this.floatService.showBrotherhoodFloats();

			Assert.isTrue(floats.size() > 0);

			Integer floatId = floats.get(0).getId();

			List<String> pictures = this.floatService.getPicturesOfFloat(floatId, paradeInFinal);

			Assert.isTrue(pictures.size() > 0);

			super.unauthenticate();
		} catch (Throwable oops) {
			caught = oops.getClass();
		} finally {
			super.rollbackTransaction();
		}

		super.checkExceptions(expected, caught);

	}

}
