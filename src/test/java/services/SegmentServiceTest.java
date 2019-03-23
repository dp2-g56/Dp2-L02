package services;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BindingResult;

import domain.Brotherhood;
import domain.Segment;
import utilities.AbstractTest;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:spring/junit.xml" })
@Transactional
public class SegmentServiceTest extends AbstractTest {

	@Autowired
	private SegmentService segmentService;

	@Autowired
	private BrotherhoodService brotherhoodService;

	@Autowired
	private ParadeService paradeService;

	/**
	 * We are going to test de Requirment:
	 *
	 * 3. An actor who is authenticated as a brotherhood must be able to:
	 *
	 *
	 * 3.3.Manage the paths of their parades, which includes listing, showing,
	 * creating, updating, and deleting them.
	 *
	 */

	@Test
	public void driverList() {

		Brotherhood brotherhood1 = this.brotherhoodService.getBrotherhoodByUsername("brotherhood1");

		// Brotherhood1's Parade that contains a Path and is in Draft mode
		Integer paradeId = brotherhood1.getParades().get(0).getId();

		Object testingData[][] = {

				/**
				 * POSITIVE TEST: Brotherhood 1 is listing a path list from an own Parade in
				 * draft mode
				 **/

				{ "brotherhood1", paradeId, null }

				/**
				 * NEGATIVE TEST: Brotherhood 2 is listing a path list from a not own Parade in
				 * draft mode
				 **/
				, { "brotherhood2", paradeId, IllegalArgumentException.class } };

		for (int i = 0; i < testingData.length; i++)
			this.templateList((String) testingData[i][0], (Integer) testingData[i][1], (Class<?>) testingData[i][2]);

	}

	private void templateList(String brotherhood, Integer paradeId, Class<?> expected) {

		Class<?> caught = null;

		try {
			super.authenticate(brotherhood);
			this.segmentService.getSegmentByParade(paradeId);
			super.unauthenticate();
		} catch (Throwable oops) {
			caught = oops.getClass();
		}

		super.checkExceptions(expected, caught);

	}

	@Test
	public void driverCreateAndEdit() {
		Brotherhood brotherhood1 = this.brotherhoodService.getBrotherhoodByUsername("brotherhood1");

		// Brotherhood1's Parade that contains a Path and is in Draft mode
		Integer paradeId = brotherhood1.getParades().get(0).getId();

		Object testingData[][] = {
				/** POSITIVE TEST: A brotherhod is adding a segment to a own path **/
				{ "brotherhood1", paradeId, 0, 4.3, 5.4, 4, null },
				/**
				 * NEGATIVE TEST: A brothood is trying to adding a segment to a not own path
				 **/
				{ "brotherhood2", paradeId, 0, 4.3, 5.4, 4, IllegalArgumentException.class }

		};
		for (int i = 0; i < testingData.length; i++)
			this.templateCreateAndEdit((String) testingData[i][0], (Integer) testingData[i][1],
					(Integer) testingData[i][2], (Double) testingData[i][3], (Double) testingData[i][4],
					(Integer) testingData[i][5], (Class<?>) testingData[i][6]);

	}

	private void templateCreateAndEdit(String brotherhood, Integer paradeId, Integer segmentId,
			Double destinationLatitude, Double destinationLongitude, Integer time, Class<?> expected) {

		Class<?> caught = null;
		BindingResult binding = null;
		Segment segmentForm = new Segment();
		segmentForm.setId(segmentId);
		segmentForm.setDestinationLatitude(destinationLatitude);
		segmentForm.setDestinationLongitude(destinationLongitude);
		segmentForm.setTime(time);

		try {
			super.authenticate(brotherhood);
			Segment segment = this.segmentService.recontruct(segmentForm, paradeId, binding);
			this.segmentService.editSegment(segment, paradeId);
			super.unauthenticate();
		} catch (Throwable oops) {
			caught = oops.getClass();
		}

		super.checkExceptions(expected, caught);

	}

	@Test
	public void driverDelete() {
		Brotherhood brotherhood1 = this.brotherhoodService.getBrotherhoodByUsername("brotherhood1");

		// Brotherhood1's Parade that contains a Path and is in Draft mode
		Integer paradeId = brotherhood1.getParades().get(0).getId();

		Segment segment = brotherhood1.getParades().get(0).getPath().getSegments().get(0);

		Object testingData[][] = {
				/** POSITIVE TEST: A brotherhod is deleting a segment from a own path **/
				{ "brotherhood1", paradeId, segment, null },
				/**
				 * NEGATIVE TEST: A brothood is trying to delete a segment from a not own path
				 **/
				{ "brotherhood2", paradeId, segment, IllegalArgumentException.class },

		};

		for (int i = 0; i < testingData.length; i++)
			this.templateDelete((String) testingData[i][0], (Integer) testingData[i][1], (Segment) testingData[i][2],
					(Class<?>) testingData[i][3]);

	}

	private void templateDelete(String brotherhood, Integer paradeId, Segment segment, Class<?> expected) {

		Class<?> caught = null;

		try {
			super.authenticate(brotherhood);
			this.segmentService.deleteSegment(segment, paradeId);
			super.unauthenticate();
		} catch (Throwable oops) {
			caught = oops.getClass();
		}

		super.checkExceptions(expected, caught);

	}

}
