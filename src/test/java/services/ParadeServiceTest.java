
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
import domain.ParadeStatus;
import domain.Path;
import domain.Segment;
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
	 * which can sponsor.
	 */
	@Test
	public void driverListParadesIfSponsor() {

		Object testingData[][] = {
				// Positive test: Listing all accepted parades if a sponsor is logged in
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
	 * Test the use case detailed in requirement 10.2 (Acme-Madruga) and 3.1
	 * (Acme-Parade): Manage their parades, which includes listing them
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
				{ "", "member1", IllegalArgumentException.class } };

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
				// Positive test: Creating a parade and a float with correct content, logged as
				// brotherhood and having an area
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

	/**
	 * Test the use case detailed in requirement 10.2 (Acme-Madruga): Manage their
	 * parades, which includes creating them
	 */
	@Test
	public void driverCreateParadeIfBrotherhood() {
		Calendar c = Calendar.getInstance();
		c.set(Calendar.DAY_OF_MONTH, 20);
		c.set(Calendar.MONTH, 10);
		Date futureDate = c.getTime();

		c.set(Calendar.MONTH, 2);
		Date pastDate = c.getTime();

		Brotherhood b1 = this.brotherhoodService.getBrotherhoodByUsername("brotherhood1");
		List<domain.Float> floats1 = b1.getFloats();

		Brotherhood b2 = this.brotherhoodService.getBrotherhoodByUsername("brotherhood2");
		List<domain.Float> floats2 = b2.getFloats();

		Brotherhood b4 = this.brotherhoodService.getBrotherhoodByUsername("brotherhood4");
		List<domain.Float> floats4 = b4.getFloats();

		Object testingData[][] = {
				// Positive test: Creating a parade with correct content, logged as brotherhood,
				// having an area and using floats of the brotherhood
				{ "Parade title", "Parade description", futureDate, true, 5, 10, floats1, "brotherhood1", null },
				// Negative test: Trying to create a parade with a past moment
				{ "Parade title", "Parade description", pastDate, true, 5, 10, floats1, "brotherhood1",
						ConstraintViolationException.class },
				// Negative test: Trying to create a parade with a blank parade title
				{ "", "Parade description", futureDate, true, 5, 10, floats1, "brotherhood1",
						ConstraintViolationException.class },
				// Negative test: Trying to create a parade with with a different role
				{ "Parade title", "Parade description", futureDate, true, 5, 10, floats1, "member1",
						IllegalArgumentException.class },
				// Negative test: Trying to create a parade with a brotherhood without area
				{ "Parade title", "Parade description", futureDate, true, 5, 10, floats4, "brotherhood4",
						IllegalArgumentException.class },
				// Negative test: Trying to create a parade with floats of other brotherhood
				{ "Parade title", "Parade description", futureDate, true, 5, 10, floats2, "brotherhood1",
						IllegalArgumentException.class } };

		for (int i = 0; i < testingData.length; i++)
			this.templateCreateParadeIfBrotherhood((String) testingData[i][0], (String) testingData[i][1],
					(Date) testingData[i][2], (Boolean) testingData[i][3], (Integer) testingData[i][4],
					(Integer) testingData[i][5], (List<domain.Float>) testingData[i][6], (String) testingData[i][7],
					(Class<?>) testingData[i][8]);
	}

	private void templateCreateParadeIfBrotherhood(String paradeTitle, String paradeDescription, Date paradeMoment,
			Boolean draftMode, Integer rowNumber, Integer columnNumber, List<domain.Float> floats, String username,
			Class<?> expected) {
		Class<?> caught = null;

		try {
			this.startTransaction();

			super.authenticate(username);

			Parade parade = this.paradeService.create();

			parade.setTitle(paradeTitle);
			parade.setDescription(paradeDescription);
			parade.setMoment(paradeMoment);
			parade.setIsDraftMode(draftMode);
			parade.setRowNumber(rowNumber);
			parade.setColumnNumber(columnNumber);

			this.paradeService.saveAssignList(parade, floats);

			this.paradeService.flush();

			super.unauthenticate();
		} catch (Throwable oops) {
			caught = oops.getClass();
		} finally {
			this.rollbackTransaction();
		}

		super.checkExceptions(expected, caught);

	}

	/**
	 * Test the use case detailed in requirement 10.2 (Acme-Madruga): Manage their
	 * parades, which includes updating them
	 */
	@Test
	public void driverUpdateParadeIfBrotherhood() {
		Calendar c = Calendar.getInstance();
		c.set(Calendar.DAY_OF_MONTH, 20);
		c.set(Calendar.MONTH, 10);
		Date futureDate = c.getTime();

		c.set(Calendar.MONTH, 2);
		Date pastDate = c.getTime();

		Parade p1Draft = this.paradeService.findOne(super.getEntityId("parade1"));
		Parade p1Final = this.paradeService.findOne(super.getEntityId("parade2"));

		Brotherhood b2 = this.brotherhoodService.getBrotherhoodByUsername("brotherhood2");
		List<domain.Float> floats2 = b2.getFloats();

		Object testingData[][] = {
				// Positive test: Updating a parade in draft mode, with the correct content and
				// logged in as a brotherhood with area
				{ p1Draft, "Parade title", p1Draft.getDescription(), futureDate, false, p1Draft.getRowNumber(),
						p1Draft.getColumnNumber(), p1Draft.getFloats(), "brotherhood1", null },
				// Negative test: Trying to update a parade with a past moment
				{ p1Draft, p1Draft.getTitle(), p1Draft.getDescription(), pastDate, p1Draft.getIsDraftMode(),
						p1Draft.getRowNumber(), p1Draft.getColumnNumber(), p1Draft.getFloats(), "brotherhood1",
						ConstraintViolationException.class },
				// Negative test: Trying to update a parade with a blank parade title
				{ p1Draft, "", p1Draft.getDescription(), futureDate, p1Draft.getIsDraftMode(), p1Draft.getRowNumber(),
						p1Draft.getColumnNumber(), p1Draft.getFloats(), "brotherhood1",
						ConstraintViolationException.class },
				// Negative test: Trying to update a parade with with a different role
				{ p1Draft, "Parade title", p1Draft.getDescription(), futureDate, p1Draft.getIsDraftMode(),
						p1Draft.getRowNumber(), p1Draft.getColumnNumber(), p1Draft.getFloats(), "member1",
						IllegalArgumentException.class },
				// Negative test: Trying to update a parade with floats of other brotherhood
				{ p1Draft, p1Draft.getTitle(), p1Draft.getDescription(), futureDate, p1Draft.getIsDraftMode(),
						p1Draft.getRowNumber(), p1Draft.getColumnNumber(), floats2, "brotherhood1",
						IllegalArgumentException.class },
				// Negative test: Trying to update a parade that is in final mode
				{ p1Final, "Parade title", p1Final.getDescription(), p1Final.getMoment(), p1Final.getIsDraftMode(),
						p1Final.getRowNumber(), p1Final.getColumnNumber(), p1Final.getFloats(), "brotherhood1",
						IllegalArgumentException.class } };

		for (int i = 0; i < testingData.length; i++)
			this.templateUpdateParadeIfBrotherhood((Parade) testingData[i][0], (String) testingData[i][1],
					(String) testingData[i][2], (Date) testingData[i][3], (Boolean) testingData[i][4],
					(Integer) testingData[i][5], (Integer) testingData[i][6], (List<domain.Float>) testingData[i][7],
					(String) testingData[i][8], (Class<?>) testingData[i][9]);
	}

	private void templateUpdateParadeIfBrotherhood(Parade parade, String paradeTitle, String paradeDescription,
			Date paradeMoment, Boolean draftMode, Integer rowNumber, Integer columnNumber, List<domain.Float> floats,
			String username, Class<?> expected) {
		Class<?> caught = null;

		try {
			this.startTransaction();

			super.authenticate(username);

			parade.setTitle(paradeTitle);
			parade.setDescription(paradeDescription);
			parade.setMoment(paradeMoment);
			parade.setIsDraftMode(draftMode);
			parade.setRowNumber(rowNumber);
			parade.setColumnNumber(columnNumber);

			this.paradeService.saveAssignList(parade, floats);

			this.paradeService.flush();

			super.unauthenticate();
		} catch (Throwable oops) {
			caught = oops.getClass();
		} finally {
			this.rollbackTransaction();
		}

		super.checkExceptions(expected, caught);

	}

	/**
	 * Test the use case detailed in requirement 10.2 (Acme-Madruga): Manage their
	 * parades, which includes deleting them
	 */
	@Test
	public void driverDeleteParadeIfBrotherhood() {
		Parade p1Draft = this.paradeService.findOne(super.getEntityId("parade1"));
		Parade p1Final = this.paradeService.findOne(super.getEntityId("parade2"));

		Object testingData[][] = {
				// Positive test: Deleting a parade in draft mode, logged in as brotherhood that
				// owns the parade
				{ p1Draft, "brotherhood1", null },
				// Negative test: Trying to delete a parade with a different role
				{ p1Draft, "member1", IllegalArgumentException.class },
				// Negative test: Trying to delete a parade with a different brotherhood
				{ p1Draft, "brotherhood2", IllegalArgumentException.class },
				// Negative test: Trying to delete a parade in final mode
				{ p1Final, "brotherhood1", IllegalArgumentException.class } };

		for (int i = 0; i < testingData.length; i++)
			this.templateDeleteParadeIfBrotherhood((Parade) testingData[i][0], (String) testingData[i][1],
					(Class<?>) testingData[i][2]);
	}

	private void templateDeleteParadeIfBrotherhood(Parade parade, String username, Class<?> expected) {
		Class<?> caught = null;

		try {
			this.startTransaction();

			super.authenticate(username);

			this.paradeService.deleteParadeWithId(parade.getId());

			this.paradeService.flush();

			super.unauthenticate();
		} catch (Throwable oops) {
			caught = oops.getClass();
		} finally {
			this.rollbackTransaction();
		}

		super.checkExceptions(expected, caught);

	}

	/**
	 * This test is a valid case of the use of the copy method in ParadeService, all
	 * Asserts in this test check of the copy has been made correctly.
	 *
	 **/

	@Test
	public void testCopy() {
		super.authenticate("brotherhood1");
		Parade parade = this.paradeService.findOne(super.getEntityId("parade1"));
		List<domain.Float> floats = parade.getFloats();
		Path path = parade.getPath();
		int numParades = this.brotherhoodService.getBrotherhoodByUsername("brotherhood1").getParades().size();

		this.paradeService.copy(parade);
		this.paradeService.flush();

		Parade paradeCopy = this.brotherhoodService.getBrotherhoodByUsername("brotherhood1").getParades()
				.get(numParades);

		Assert.isTrue(
				this.brotherhoodService.getBrotherhoodByUsername("brotherhood1").getParades().size() == numParades + 1);

		Assert.isTrue(paradeCopy.getFloats().containsAll(floats) && paradeCopy.getFloats().size() == floats.size());

		Assert.isTrue(paradeCopy.getRequests().isEmpty() && paradeCopy.getIsDraftMode()
				&& paradeCopy.getParadeStatus().equals(ParadeStatus.SUBMITTED)
				&& paradeCopy.getRejectedReason() == null);

		Assert.isTrue(paradeCopy.getColumnNumber() == parade.getColumnNumber()
				&& paradeCopy.getRowNumber() == parade.getRowNumber() && paradeCopy.getTitle() == parade.getTitle()
				&& paradeCopy.getDescription() == parade.getDescription()
				&& paradeCopy.getMoment().compareTo(parade.getMoment()) == 0);

		Assert.isTrue(paradeCopy.getTicker() != parade.getTicker() && paradeCopy.getId() != parade.getId());

		Assert.isTrue(paradeCopy.getPath().getId() != parade.getPath().getId()
				&& paradeCopy.getPath().getSegments().size() == parade.getPath().getSegments().size());

		for (Segment s : path.getSegments()) {
			Segment sCopy = paradeCopy.getPath().getSegments().get(path.getSegments().indexOf(s));
			Assert.isTrue(sCopy.getId() != s.getId());
			Assert.isTrue(sCopy.getDestinationLatitude() == s.getDestinationLatitude()
					&& sCopy.getDestinationLongitude() == s.getDestinationLongitude()
					&& sCopy.getOriginLatitude() == s.getOriginLatitude()
					&& sCopy.getOriginLongitude() == s.getOriginLongitude() && sCopy.getTime() == s.getTime());
		}

		super.unauthenticate();
	}

	/**
	 * This test is a invalid case of the use of the copy method in ParadeService,
	 * one actor tries to copy a parade that don't own. An IllegalArgumentException
	 * is expected.
	 *
	 **/

	@Test(expected = IllegalArgumentException.class)
	public void testCopyWrong() {
		super.authenticate("brotherhood2");
		Parade parade = this.paradeService.findOne(super.getEntityId("parade1"));

		this.paradeService.copy(parade);
		this.paradeService.flush();

		super.unauthenticate();
	}
}
