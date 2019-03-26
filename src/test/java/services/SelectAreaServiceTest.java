
package services;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import utilities.AbstractTest;
import domain.Area;
import domain.Brotherhood;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
	"classpath:spring/junit.xml"
})
@Transactional
public class SelectAreaServiceTest extends AbstractTest {

	@Autowired
	private BrotherhoodService	brotherhoodService;

	@Autowired
	private AreaService			areaService;


	/**
	 * The following tests are going to be documented according to the following especification:
	 * 
	 * On the top of each Driver there will be a little piece of text explaining the action that is going to be tested
	 * 
	 * For each testing data set, there will be an explanation with the following sections:
	 * a) Requirement tested
	 * b) In the case of negative tests, the business rule that is intended to be broken
	 * 
	 * Data and Sentence coverage will be explained in the attached document
	 */

	//---------------------------------SELECT AREA BROTHERHOOD-----------------------
	//-------------------------------------------------------------------------------

	//CREATE AN ENROLMENT

	/**
	 * The data of this driver is focused to test the selection of an area by a Brotherhood
	 * Requirement 20.1
	 */

	@Test
	public void driverSelectArea() {
		Object testingData[][] = {
			{
				"brotherhood4", "area1", null
			/**
			 * a)Requirement 201
			 * b)There is no error expected here, a brotherhood without an area assigns one to himself
			 */
			}, {
				"brotherhood1", "area3", IllegalArgumentException.class
			/**
			 * a)Requirement 20.1
			 * b)An anonymous user tries to select an area and an illegalArgumentexception is thrown
			 */
			}

		};
		for (int i = 0; i < testingData.length; i++)
			this.templateSelectArea((String) testingData[i][0], (String) testingData[i][1], (Class<?>) testingData[i][2]);

	}
	protected void templateSelectArea(String brotherhoodUsername, String areaBean, Class<?> expected) {

		Class<?> caught = null;

		try {
			this.authenticate(brotherhoodUsername);

			//int brotherhoodId = super.getEntityId(brotherhoodUsername);
			Brotherhood loggedBrotherhood = this.brotherhoodService.loggedBrotherhood();

			int areaId = super.getEntityId(areaBean);
			Area area = this.areaService.findOne(areaId);

			Brotherhood bro = new Brotherhood();
			bro.setArea(area);
			bro.setId(loggedBrotherhood.getId());

			Brotherhood saved = this.brotherhoodService.reconstructArea(bro, null);

			this.brotherhoodService.updateBrotherhood(saved);

			this.brotherhoodService.flush();
			Assert.isTrue(this.brotherhoodService.loggedBrotherhood().getArea().equals(area));

			this.unauthenticate();

		} catch (Throwable oops) {
			caught = oops.getClass();
		}
		super.checkExceptions(expected, caught);

	}

}
