
package services;

import java.util.ArrayList;
import java.util.List;

import javax.validation.ConstraintViolationException;

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

	/**
	 * Test the use case detailed in requirement 10.1 (Acme-Madruga project): Manage
	 * their floats, which includes creating them
	 */
	@Test
	public void driverCreateFloatAndPictureIfBrotherhood() {

		Object testingData[][] = {
				// Positive test
				{ "brotherhood1", "Float title", "Float description", "https://www.url.com", null },
				// Negative test: Trying to save a float with a different role
				{ "admin1", "Float title", "Float description", "https://www.url.com", IllegalArgumentException.class },
				// Negative test: Trying to save a float with a brotherhood without area
				{ "brotherhood4", "Float title", "Float description", "https://www.url.com",
						IllegalArgumentException.class },
				// Negative test: Trying to save a float with a brotherhood with a blank title
				{ "brotherhood1", "", "Float description", "https://www.url.com", ConstraintViolationException.class },
				// Negative test: Trying to save a float with a invalid picture
				{ "brotherhood1", "Float title", "Float description", "", IllegalArgumentException.class } };
		for (int i = 0; i < testingData.length; i++)
			this.templateCreateFloatAndPictureIfBrotherhood((String) testingData[i][0], (String) testingData[i][1],
					(String) testingData[i][2], (String) testingData[i][3], (Class<?>) testingData[i][4]);
	}

	private void templateCreateFloatAndPictureIfBrotherhood(String username, String title, String description,
			String picture, Class<?> expected) {
		domain.Float floatt = this.floatService.create();

		floatt.setTitle(title);
		floatt.setDescription(description);

		Class<?> caught = null;

		try {
			super.startTransaction();

			super.authenticate(username);

			this.floatService.addPicture(picture, floatt);

			this.floatService.flush();

			super.unauthenticate();
		} catch (Throwable oops) {
			caught = oops.getClass();
		} finally {
			super.rollbackTransaction();
		}

		super.checkExceptions(expected, caught);

	}

	/**
	 * Test the use case detailed in requirement 10.1 (Acme-Madruga project): Manage
	 * their floats, which includes updating them
	 */
	@Test
	public void driverUpdateFloatIfBrotherhood() {
		// Float of brotherhood2 that is not assigned to any parade
		domain.Float floatt = this.floatService.findOne(super.getEntityId("float3"));
		// Float of brotherhood1 that is assigned to one parade
		domain.Float floatt2 = this.floatService.findOne(super.getEntityId("float1"));

		Object testingData[][] = {
				// Positive test
				{ "brotherhood2", "Float title", floatt.getDescription(), floatt, null },
				// Negative test: Trying to update a float with a different brotherhood
				{ "brotherhood1", "Float title", floatt.getDescription(), floatt, IllegalArgumentException.class },
				// Negative test: Trying to update a float with a different role
				{ "member1", "Float title", floatt.getDescription(), floatt, IllegalArgumentException.class },
				// Negative test: Trying to update a float that is assigned to one parade
				{ "brotherhood1", "Float title", floatt.getDescription(), floatt2, IllegalArgumentException.class },
				// Negative test: Trying to update a float with a blank title
				{ "brotherhood2", "", floatt.getDescription(), floatt, ConstraintViolationException.class } };
		for (int i = 0; i < testingData.length; i++)
			this.templateUpdateFloatIfBrotherhood((String) testingData[i][0], (String) testingData[i][1],
					(String) testingData[i][2], (domain.Float) testingData[i][3], (Class<?>) testingData[i][4]);
	}

	private void templateUpdateFloatIfBrotherhood(String username, String title, String description,
			domain.Float floatt, Class<?> expected) {
		floatt.setTitle(title);
		floatt.setDescription(description);

		Class<?> caught = null;

		try {
			super.startTransaction();

			super.authenticate(username);

			this.floatService.save(floatt);

			this.floatService.flush();

			super.unauthenticate();
		} catch (Throwable oops) {
			caught = oops.getClass();
		} finally {
			super.rollbackTransaction();
		}

		super.checkExceptions(expected, caught);

	}

	/**
	 * Test the use case detailed in requirement 10.1 (Acme-Madruga project): Manage
	 * their floats, which includes removing them
	 */
	@Test
	public void driverRemoveFloatIfBrotherhood() {
		// Float of brotherhood2 that is not assigned to any parade
		domain.Float floatt = this.floatService.findOne(super.getEntityId("float3"));
		// Float of brotherhood1 that is assigned to one parade
		domain.Float floatt2 = this.floatService.findOne(super.getEntityId("float1"));

		Object testingData[][] = {
				// Positive test
				{ "brotherhood2", floatt, null },
				// Negative test: Trying to remove a float with a different brotherhood
				{ "brotherhood1", floatt, IllegalArgumentException.class },
				// Negative test: Trying to remove a float with a different role
				{ "member1", floatt, IllegalArgumentException.class },
				// Negative test: Trying to remove a float that is assigned to one parade
				{ "brotherhood1", floatt2, IllegalArgumentException.class } };
		for (int i = 0; i < testingData.length; i++)
			this.templateRemoveFloatIfBrotherhood((String) testingData[i][0], (domain.Float) testingData[i][1],
					(Class<?>) testingData[i][2]);
	}

	private void templateRemoveFloatIfBrotherhood(String username, domain.Float floatt, Class<?> expected) {

		Class<?> caught = null;

		try {
			super.startTransaction();

			super.authenticate(username);

			this.floatService.remove(floatt);

			this.floatService.flush();

			super.unauthenticate();
		} catch (Throwable oops) {
			caught = oops.getClass();
		} finally {
			super.rollbackTransaction();
		}

		super.checkExceptions(expected, caught);

	}

}
