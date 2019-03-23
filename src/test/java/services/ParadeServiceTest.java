
package services;

import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import utilities.AbstractTest;
import domain.Parade;
import domain.ParadeStatus;
import domain.Path;
import domain.Segment;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
	"classpath:spring/junit.xml"
})
@Transactional
public class ParadeServiceTest extends AbstractTest {

	@Autowired
	private ParadeService		paradeService;
	@Autowired
	private BrotherhoodService	brotherhoodService;


	/**
	 * Test the specific list that is shown to the sponsor of the accepted parades,
	 * which may sponsor.
	 */
	@Test
	public void driverListParadesIfSponsor() {

		Object testingData[][] = {
			// Positive test
			{
				"sponsor1", null
			},
			// Negative test: Trying to access with a different role
			{
				"member1", IllegalArgumentException.class
			}
		};
		for (int i = 0; i < testingData.length; i++) {
			this.templateListParadesIfSponsor((String) testingData[i][0], (Class<?>) testingData[i][1]);
		}
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
	 * This test is a valid case of the use of the copy method in ParadeService, all Asserts in this test
	 * check of the copy has been made correctly.
	 * 
	 * **/

	@Test
	public void testCopy() {
		super.authenticate("brotherhood1");
		Parade parade = this.paradeService.findOne(super.getEntityId("parade1"));
		List<domain.Float> floats = parade.getFloats();
		Path path = parade.getPath();
		int numParades = this.brotherhoodService.getBrotherhoodByUsername("brotherhood1").getParades().size();

		this.paradeService.copy(parade);
		this.paradeService.flush();

		Parade paradeCopy = this.brotherhoodService.getBrotherhoodByUsername("brotherhood1").getParades().get(numParades);

		Assert.isTrue(this.brotherhoodService.getBrotherhoodByUsername("brotherhood1").getParades().size() == numParades + 1);

		Assert.isTrue(paradeCopy.getFloats().containsAll(floats) && paradeCopy.getFloats().size() == floats.size());

		Assert.isTrue(paradeCopy.getRequests().isEmpty() && paradeCopy.getIsDraftMode() && paradeCopy.getParadeStatus().equals(ParadeStatus.SUBMITTED) && paradeCopy.getRejectedReason() == null);

		Assert.isTrue(paradeCopy.getColumnNumber() == parade.getColumnNumber() && paradeCopy.getRowNumber() == parade.getRowNumber() && paradeCopy.getTitle() == parade.getTitle() && paradeCopy.getDescription() == parade.getDescription()
			&& paradeCopy.getMoment().compareTo(parade.getMoment()) == 0);

		Assert.isTrue(paradeCopy.getTicker() != parade.getTicker() && paradeCopy.getId() != parade.getId());

		Assert.isTrue(paradeCopy.getPath().getId() != parade.getPath().getId() && paradeCopy.getPath().getSegments().size() == parade.getPath().getSegments().size());

		for (Segment s : path.getSegments()) {
			Segment sCopy = paradeCopy.getPath().getSegments().get(path.getSegments().indexOf(s));
			Assert.isTrue(sCopy.getId() != s.getId());
			Assert.isTrue(sCopy.getDestinationLatitude() == s.getDestinationLatitude() && sCopy.getDestinationLongitude() == s.getDestinationLongitude() && sCopy.getOriginLatitude() == s.getOriginLatitude()
				&& sCopy.getOriginLongitude() == s.getOriginLongitude() && sCopy.getTime() == s.getTime());
		}

		super.unauthenticate();
	}

	/**
	 * This test is a invalid case of the use of the copy method in ParadeService, one actor tries to copy a parade
	 * that don't own. An IllegalArgumentException is expected.
	 * 
	 * **/

	@Test(expected = IllegalArgumentException.class)
	public void testCopyWrong() {
		super.authenticate("brotherhood2");
		Parade parade = this.paradeService.findOne(super.getEntityId("parade1"));

		this.paradeService.copy(parade);
		this.paradeService.flush();

		super.unauthenticate();
	}
}
