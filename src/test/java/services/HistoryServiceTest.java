
package services;

import javax.validation.ConstraintViolationException;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import utilities.AbstractTest;
import domain.Brotherhood;
import domain.InceptionRecord;
import domain.LegalRecord;
import domain.LinkRecord;
import domain.MiscellaneousRecord;
import domain.PeriodRecord;
import forms.FormObjectLinkRecord;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
	"classpath:spring/junit.xml"
})
@Transactional
public class HistoryServiceTest extends AbstractTest {

	@Autowired
	private LinkRecordService			linkRecordService;

	@Autowired
	private MiscellaneousRecordService	miscellaneousRecordService;

	@Autowired
	private InceptionRecordService		inceptionRecordService;

	@Autowired
	private PeriodRecordService			periodRecordService;

	@Autowired
	private LegalRecordService			legalRecordService;

	@Autowired
	private BrotherhoodService			brotherhoodService;


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

	//-----------------------------INCEPTION RECORD----------------------------------
	//-------------------------------------------------------------------------------
	//CREATE
	/**
	 * The data of this driver is focused to test the creation of an Inception Record
	 * Inception record is the first record that must be created by brotherhoods if they want
	 * to register their histories
	 * 
	 */

	@Test
	public void driverCreateInceptionRecord() {
		Object testingData[][] = {
			{
				"brotherhood4", "title", "description", null
			/**
			 * a)Requirement 1
			 * b)There is no error expected here, a brotherhood without an history creates his Inception Record, after this,
			 * this brotherhood will be able to create the rest of records and edit his InceptionRecord
			 */
			}, {
				"brotherhood4", "", "description", ConstraintViolationException.class
			/**
			 * a)Requirement 1
			 * b)In this case, a Constraint Violation Exception is expected because we are trying to create an Inception
			 * record with a blank title
			 */
			}, {
				"brotherhood4", "title", "", ConstraintViolationException.class
			/**
			 * a)Requirement 1
			 * b)In this case, a Constraint Violation Exception is expected because we are trying to create an Inception
			 * record with a blank description
			 */
			}, {
				"brotherhood1", "title", "description", ConstraintViolationException.class
			/**
			 * a)Requirement 1
			 * b)In this case, a Constraint Violation Exception is expected because we are trying to create an Inception
			 * record logged as a Brotherhood with an Existing InceptionRecord
			 */
			}, {
				"member1", "title", "description", IllegalArgumentException.class
			/**
			 * a)Requirement 1
			 * b)In this case, a IllegalArgumentException is expected because we are trying to create an Inception
			 * record logged as a member that doesn't have the autority to create or register records
			 */
			}, {
				null, "title", "description", IllegalArgumentException.class
			/**
			 * a)Requirement 1
			 * b)In this case, a IllegalArgumentException is expected because we are trying to create an Inception
			 * record with an unauthenticated user
			 */
			}
		};
		for (int i = 0; i < testingData.length; i++)
			this.templateCreateInceptionRecord((String) testingData[i][0], (String) testingData[i][1], (String) testingData[i][2], (Class<?>) testingData[i][3]);

	}

	protected void templateCreateInceptionRecord(String username, String title, String description, Class<?> expected) {

		Class<?> caught = null;

		try {
			this.authenticate(username);
			InceptionRecord inceptionRecord = this.inceptionRecordService.create();

			inceptionRecord.setTitle(title);
			inceptionRecord.setDescription(description);

			this.inceptionRecordService.saveInceptionRecord(inceptionRecord);
			this.inceptionRecordService.flush();

			this.unauthenticate();

		} catch (Throwable oops) {
			caught = oops.getClass();
		}
		super.checkExceptions(expected, caught);
	}

	//EDIT

	/**
	 * The data of this driver is focused to test the edit of an Inception Record
	 */

	@Test
	public void driverEditInceptionRecord() {
		Object testingData[][] = {
			{
				"brotherhood1", "newTitle", "newDescription", null
			/**
			 * a)Requirement 1
			 * b)There is no error expected here, a brotherhood with an history edits his Inception Record
			 */
			}, {
				"brotherhood1", "", "newDescription", ConstraintViolationException.class
			/**
			 * a)Requirement 1
			 * b)In this case, a Constraint Violation Exception is expected because we are trying to edit an Inception
			 * record assigning it a blank title
			 */
			}, {
				"brotherhood1", "newTitle", "", ConstraintViolationException.class
			/**
			 * a)Requirement 1
			 * b)In this case, a Constraint Violation Exception is expected because we are trying to edit an Inception
			 * record assigning it a blank description
			 */
			}
		};
		for (int i = 0; i < testingData.length; i++)
			this.templateCreateInceptionRecord((String) testingData[i][0], (String) testingData[i][1], (String) testingData[i][2], (Class<?>) testingData[i][3]);

	}
	protected void templateEditInceptionRecord(String username, String newTitle, String newDescription, Class<?> expected) {

		Class<?> caught = null;

		try {

			super.authenticate(username);

			InceptionRecord inceptionRecord = this.inceptionRecordService.prepareEditInceptionRecord();

			inceptionRecord.setTitle(newTitle);
			inceptionRecord.setDescription(newDescription);

			this.inceptionRecordService.saveInceptionRecord(inceptionRecord);

			this.inceptionRecordService.flush();

			this.unauthenticate();

		} catch (Throwable oops) {
			caught = oops.getClass();
		}
		super.checkExceptions(expected, caught);
	}

	//---------------------------------PERIOD RECORD---------------------------------
	//-------------------------------------------------------------------------------

	//CREATE
	/**
	 * The data of this driver is focused to test the creation of a Period Record
	 * For each Period Record, the System must store a title, a description,
	 * a start year, an end year and some photos
	 * 
	 */

	@Test
	public void driverCreatePeriodRecord() {
		Object testingData[][] = {
			{
				"brotherhood1", "title", "description", 2001, 2005, null
			/**
			 * a)Requirement 1
			 * b)There is no error expected here, a brotherhood with an history creates a Period Record with valid data
			 */
			}, {
				"brotherhood1", "title", "description", 2010, 2010, null
			/**
			 * a)Requirement 1
			 * b)There is no error expected here, a brotherhood with an history creates a Period Record where startYear=endYear
			 */
			}, {
				"brotherhood1", "title2", "description2", 1, 2006, null
			/**
			 * a)Requirement 1
			 * b)There is no error expected here, a brotherhood with an history creates a Period Record with the minimum value
			 * for the start year
			 */
			}, {
				"brotherhood1", "title2", "description2", 1, 1, null
			/**
			 * a)Requirement 1
			 * b)There is no error expected here, a brotherhood with an history creates a Period Record with the minimum value
			 * for the end year
			 */
			}, {
				"brotherhood1", "title2", "description2", 2, 2005, null
			/**
			 * a)Requirement 1
			 * b)There is no error expected here, a brotherhood with an history creates a Period Record with the minimum value
			 * for the start year plus a delta value (minimum possible value that can be added to the variable)
			 */
			}, {
				"brotherhood1", "title2", "description2", 1, 2, null
			/**
			 * a)Requirement 1
			 * b)There is no error expected here, a brotherhood with an history creates a Period Record with the minimum value
			 * for the end year plus a delta value (minimum possible value that can be added to the variable)
			 */
			}, {
				"brotherhood1", "", "description", 2001, 2005, ConstraintViolationException.class
			/**
			 * a)Requirement 1
			 * b)In this case, a Constraint Violation Exception is expected because we are trying to Create a Period
			 * record assigning it a blank title
			 */
			}, {
				"brotherhood1", "title", "", 2001, 2005, ConstraintViolationException.class
			/**
			 * a)Requirement 1
			 * b)In this case, a Constraint Violation Exception is expected because we are trying to Create a Period
			 * record assigning it a blank description
			 */
			}, {
				"brotherhood1", "title", "description", null, 2005, ConstraintViolationException.class
			/**
			 * a)Requirement 1
			 * b)In this case, a Constraint Violation Exception is expected because we are trying to Create a Period
			 * record assigning it a null Start year
			 */
			}, {
				"brotherhood1", "title", "description", 2001, null, ConstraintViolationException.class
			/**
			 * a)Requirement 1
			 * b)In this case, a Constraint Violation Exception is expected because we are trying to Create a Period
			 * record assigning it a null End year
			 */
			}, {
				"brotherhood1", "title", "description", 0, 2005, ConstraintViolationException.class
			/**
			 * a)Requirement 1
			 * b)In this case, a Constraint Violation Exception is expected because we are trying to Create a Period
			 * record assigning a value to the start year that is below the minimum accepted value
			 */
			}, {
				"brotherhood1", "title", "description", 2005, 2001, IllegalArgumentException.class
			/**
			 * a)Requirement 1
			 * b)In this case, a Illegal Argument Exception is expected because we are trying to Create a Period
			 * record assigning a value to the start year that is bigger than the value assigned to the end year
			 */
			}, {
				"brotherhood4", "title", "description", 2001, 2005, IllegalArgumentException.class
			/**
			 * a)Requirement 1
			 * b)In this case, a Illegal Argument Exception is expected because we are trying to Create a Period
			 * record logged as a Brotherhood that hasn't created an History yet
			 */
			}, {
				null, "title", "description", 2001, 2005, IllegalArgumentException.class
			/**
			 * a)Requirement 1
			 * b)In this case, a Illegal Argument Exception is expected because an anonymous user is trying to create
			 * a Period record (Only Brotherhoods can do that)
			 */
			}, {
				"member1", "title", "description", 2001, 2005, IllegalArgumentException.class
			/**
			 * a)Requirement 1
			 * b)In this case, a Illegal Argument Exception is expected because an user registered as member is trying to create
			 * a Period record (Only Brotherhoods can do that)
			 */
			}, {
				"admin1", "title", "description", 2001, 2005, IllegalArgumentException.class
			/**
			 * a)Requirement 1
			 * b)In this case, a Illegal Argument Exception is expected because an user registered as admin is trying to create
			 * a Period record (Only Brotherhoods can do that)
			 */
			}, {
				"sponsor1", "title", "description", 2001, 2005, IllegalArgumentException.class
			/**
			 * a)Requirement 1
			 * b)In this case, a Illegal Argument Exception is expected because an user registered as sponsor is trying to create
			 * a Period record (Only Brotherhoods can do that)
			 */
			}, {
				"chapter1", "title", "description", 2001, 2005, IllegalArgumentException.class
			/**
			 * a)Requirement 1
			 * b)In this case, a Illegal Argument Exception is expected because an user registered as chapter is trying to create
			 * a Period record (Only Brotherhoods can do that)
			 */
			}

		};
		for (int i = 0; i < testingData.length; i++)
			this.templateCreatePeriodRecord((String) testingData[i][0], (String) testingData[i][1], (String) testingData[i][2], (Integer) testingData[i][3], (Integer) testingData[i][4], (Class<?>) testingData[i][5]);

	}

	protected void templateCreatePeriodRecord(String username, String title, String description, Integer startYear, Integer endYear, Class<?> expected) {

		Class<?> caught = null;

		try {
			this.authenticate(username);
			PeriodRecord periodRecord = this.periodRecordService.create();

			periodRecord.setDescription(description);
			periodRecord.setEndYear(endYear);
			periodRecord.setStartYear(startYear);
			periodRecord.setTitle(title);

			this.periodRecordService.savePeriodRecord(periodRecord);

			this.periodRecordService.flush();

			this.unauthenticate();

		} catch (Throwable oops) {
			caught = oops.getClass();
		}
		super.checkExceptions(expected, caught);

	}

	//EDIT

	/**
	 * The data of this driver is focused to test the edit of a Period Record
	 */

	@Test
	public void driverEditPeriodRecord() {
		Object testingData[][] = {
			{
				"brotherhood1", "periodRecord1", "newTitle", "newDescription", 2005, 2010, null
			/**
			 * a)Requirement 1
			 * b)There is no error expected here, a brotherhood with an history edits one of his Period Records
			 */
			}, {
				"brotherhood2", "periodRecord1", "newTitle", "newDescription", 2005, 2010, IllegalArgumentException.class
			/**
			 * a)Requirement 1
			 * b)In this case, a Illegal Argument Exception is expected because a Brotherhood with an History is trying to
			 * edit a period record that belongs to another user
			 */
			}, {
				null, "periodRecord1", "newTitle", "newDescription", 2005, 2010, IllegalArgumentException.class
			/**
			 * a)Requirement 1
			 * b)In this case, a Illegal Argument Exception is expected because an anonymous user is trying to edit
			 * a Period record (Only Brotherhoods can do that)
			 */
			}, {
				"member1", "periodRecord1", "newTitle", "newDescription", 2005, 2010, IllegalArgumentException.class
			/**
			 * a)Requirement 1
			 * b)In this case, a Illegal Argument Exception is expected because an user registered as member is trying to edit
			 * a Period record (Only Brotherhoods can do that)
			 */
			}, {
				"admin1", "periodRecord1", "newTitle", "newDescription", 2005, 2010, IllegalArgumentException.class
			/**
			 * a)Requirement 1
			 * b)In this case, a Illegal Argument Exception is expected because an user registered as admin is trying to edit
			 * a Period record (Only Brotherhoods can do that)
			 */
			}, {
				"sponsor1", "periodRecord1", "newTitle", "newDescription", 2005, 2010, IllegalArgumentException.class
			/**
			 * a)Requirement 1
			 * b)In this case, a Illegal Argument Exception is expected because an user registered as sponsor is trying to edit
			 * a Period record (Only Brotherhoods can do that)
			 */
			}, {
				"chapter1", "periodRecord1", "newTitle", "newDescription", 2005, 2010, IllegalArgumentException.class
			/**
			 * a)Requirement 1
			 * b)In this case, a Illegal Argument Exception is expected because an user registered as chapter is trying to edit
			 * a Period record (Only Brotherhoods can do that)
			 */
			}, {
				"brotherhood1", "periodRecord1", "", "newDescription", 2005, 2010, ConstraintViolationException.class
			/**
			 * a)Requirement 1
			 * b)In this case, a Constraint Violation Exception is expected because we are trying to Edit a Period
			 * record assigning it a blank title
			 */
			}, {
				"brotherhood1", "periodRecord1", "newTitle", "", 2005, 2010, ConstraintViolationException.class
			/**
			 * a)Requirement 1
			 * b)In this case, a Constraint Violation Exception is expected because we are trying to Edit a Period
			 * record assigning it a blank description
			 */
			}, {
				"brotherhood1", "periodRecord1", "newTitle", "newDescription", null, 2010, ConstraintViolationException.class
			/**
			 * a)Requirement 1
			 * b)In this case, a Constraint Violation Exception is expected because we are trying to Edit a Period
			 * record assigning it a null start year
			 */
			}, {
				"brotherhood1", "periodRecord1", "newTitle", "newDescription", 2005, null, ConstraintViolationException.class
			/**
			 * a)Requirement 1
			 * b)In this case, a Constraint Violation Exception is expected because we are trying to Edit a Period
			 * record assigning it a null end year
			 */
			}, {
				"brotherhood1", "periodRecord1", "newTitle", "newDescription", 2005, 2004, IllegalArgumentException.class
			/**
			 * a)Requirement 1
			 * b)In this case, a Illegal Argument Exception is expected because we are trying to Edit a Period
			 * record assigning it start year that is bigger than the end year
			 */
			}
		};
		for (int i = 0; i < testingData.length; i++)
			this.templateEditPeriodRecord((String) testingData[i][0], (String) testingData[i][1], (String) testingData[i][2], (String) testingData[i][3], (Integer) testingData[i][4], (Integer) testingData[i][5], (Class<?>) testingData[i][6]);

	}
	protected void templateEditPeriodRecord(String username, String beanPeriod, String newTitle, String newDescription, Integer newStartYear, Integer newEndYear, Class<?> expected) {

		Class<?> caught = null;

		try {
			int periodRecordId = super.getEntityId(beanPeriod);

			PeriodRecord periodRecord = this.periodRecordService.findOne(periodRecordId);
			super.authenticate(username);

			periodRecord.setTitle(newTitle);
			periodRecord.setDescription(newDescription);
			periodRecord.setStartYear(newStartYear);
			periodRecord.setEndYear(newEndYear);

			this.periodRecordService.savePeriodRecord(periodRecord);

			this.periodRecordService.flush();

			this.unauthenticate();

		} catch (Throwable oops) {
			caught = oops.getClass();
		}
		super.checkExceptions(expected, caught);
	}

	//DELETE
	/**
	 * The data of this driver is focused to test the edit of an Period Record
	 */

	@Test
	public void driverDeletePeriodRecord() {
		Object testingData[][] = {
			{
				"brotherhood1", "periodRecord1", null
			/**
			 * a)Requirement 1
			 * b)There is no error expected here, a brotherhood with an history deletes one of his Period Records
			 */
			}, {
				"brotherhood2", "periodRecord1", IllegalArgumentException.class
			/**
			 * a)Requirement 1
			 * b)In this case, a Illegal Argument Exception is expected because a Brotherhood with an History is trying to
			 * delete a period record that belongs to another user
			 */
			}, {
				null, "periodRecord1", IllegalArgumentException.class
			/**
			 * a)Requirement 1
			 * b)In this case, a Illegal Argument Exception is expected because an anonymous user is trying to delete
			 * a Period record (Only Brotherhoods can do that)
			 */
			}, {
				"member1", "periodRecord1", IllegalArgumentException.class
			/**
			 * a)Requirement 1
			 * b)In this case, a Illegal Argument Exception is expected because an user registered as member is trying to delete
			 * a Period record (Only Brotherhoods can do that)
			 */
			}, {
				"admin1", "periodRecord1", IllegalArgumentException.class
			/**
			 * a)Requirement 1
			 * b)In this case, a Illegal Argument Exception is expected because an user registered as admin is trying to delete
			 * a Period record (Only Brotherhoods can do that)
			 */
			}, {
				"chapter1", "periodRecord1", IllegalArgumentException.class
			/**
			 * a)Requirement 1
			 * b)In this case, a Illegal Argument Exception is expected because an user registered as chapter is trying to delete
			 * a Period record (Only Brotherhoods can do that)
			 */
			}, {
				"sponsor1", "periodRecord1", IllegalArgumentException.class
			/**
			 * a)Requirement 1
			 * b)In this case, a Illegal Argument Exception is expected because an user registered as sponsor is trying to delete
			 * a Period record (Only Brotherhoods can do that)
			 */
			}
		};
		for (int i = 0; i < testingData.length; i++)
			this.templateDeletePeriodRecord((String) testingData[i][0], (String) testingData[i][1], (Class<?>) testingData[i][2]);

	}

	protected void templateDeletePeriodRecord(String username, String beanPeriod, Class<?> expected) {

		Class<?> caught = null;

		try {
			int periodRecordId = super.getEntityId(beanPeriod);

			PeriodRecord periodRecord = this.periodRecordService.findOne(periodRecordId);
			super.authenticate(username);

			this.periodRecordService.deletePeriodRecord(periodRecord);

			this.periodRecordService.flush();

			this.unauthenticate();

		} catch (Throwable oops) {
			caught = oops.getClass();
		}
		super.checkExceptions(expected, caught);
	}

	//---------------------------------LEGAL RECORD----------------------------------
	//-------------------------------------------------------------------------------
	//CREATE

	/**
	 * The data of this driver is focused to test the creation of a Legal Record
	 * For each Legal Record, the System must store a title, a description,
	 * a legal name, a VAT number and some photos
	 * 
	 */

	@Test
	public void driverCreateLegalRecord() {
		Object testingData[][] = {
			{
				"brotherhood1", "title", "description", "legalName", "U99999999", null
			/**
			 * a)Requirement 1
			 * b)There is no error expected here, a brotherhood with an history creates a Legal Record with valid data
			 * VAT number type tested: Austria
			 */
			//Correcto (Patron Austria)
			}, {
				"brotherhood1", "title", "description", "legalName", "0999999999", null
			/**
			 * a)Requirement 1
			 * b)There is no error expected here, a brotherhood with an history creates a Legal Record with valid data
			 * VAT number type tested: Belgium
			 */
			//Correcto (Patron Belgica)
			}, {
				"brotherhood1", "title", "description", "legalName", "0999999999", null
			/**
			 * a)Requirement 1
			 * b)There is no error expected here, a brotherhood with an history creates a Legal Record with valid data
			 * VAT number type tested: Belgica
			 */
			//Correcto (Patron Belgica)
			}, {
				"brotherhood1", "title", "description", "legalName", "999999999", null
			/**
			 * a)Requirement 1
			 * b)There is no error expected here, a brotherhood with an history creates a Legal Record with valid data
			 * VAT number type tested: Bulgaria
			 */
			//Correcto (Patron Bulgaria1)
			}, {
				"brotherhood1", "title", "description", "legalName", "9999999999", null
			/**
			 * a)Requirement 1
			 * b)There is no error expected here, a brotherhood with an history creates a Legal Record with valid data
			 * VAT number type tested: Bulgaria
			 */
			//Correcto (Patron Bulgaria2)
			}, {
				"brotherhood1", "title", "description", "legalName", "99999999L", null
			/**
			 * a)Requirement 1
			 * b)There is no error expected here, a brotherhood with an history creates a Legal Record with valid data
			 * VAT number type tested: Chipre
			 */
			//Correcto (Patron Chipre)
			}, {
				"brotherhood1", "title", "description", "legalName", "99999999", null
			/**
			 * a)Requirement 1
			 * b)There is no error expected here, a brotherhood with an history creates a Legal Record with valid data
			 * VAT number type tested: Republica Checa
			 */
			//Correcto (Patron Republica Checa1)
			}, {
				"brotherhood1", "title", "description", "legalName", "999999999", null
			/**
			 * a)Requirement 1
			 * b)There is no error expected here, a brotherhood with an history creates a Legal Record with valid data
			 * VAT number type tested: Republica Checa
			 */
			//Correcto (Patron Republica Checa2)
			}, {
				"brotherhood1", "title", "description", "legalName", "9999999999", null
			/**
			 * a)Requirement 1
			 * b)There is no error expected here, a brotherhood with an history creates a Legal Record with valid data
			 * VAT number type tested: Republica Checa
			 */
			//Correcto (Patron Republica Checa3)
			}, {
				"brotherhood1", "title", "description", "legalName", "999999999", null
			/**
			 * a)Requirement 1
			 * b)There is no error expected here, a brotherhood with an history creates a Legal Record with valid data
			 * VAT number type tested: Alemania
			 */
			//Correcto (Patron Alemania)
			}, {
				"brotherhood1", "title", "description", "legalName", "99999999", null
			/**
			 * a)Requirement 1
			 * b)There is no error expected here, a brotherhood with an history creates a Legal Record with valid data
			 * VAT number type tested: Dinamarca
			 */
			//Correcto (Patron Dinamarca)
			}, {
				"brotherhood1", "title", "description", "legalName", "999999999", null
			/**
			 * a)Requirement 1
			 * b)There is no error expected here, a brotherhood with an history creates a Legal Record with valid data
			 * VAT number type tested: Estonia
			 */
			//Correcto (Patron Estonia)
			}, {
				"brotherhood1", "title", "description", "legalName", "999999999", null
			/**
			 * a)Requirement 1
			 * b)There is no error expected here, a brotherhood with an history creates a Legal Record with valid data
			 * VAT number type tested: Grecia
			 */
			//Correcto (Patron Grecia)
			}, {
				"brotherhood1", "title", "description", "legalName", "X9999999X", null
			/**
			 * a)Requirement 1
			 * b)There is no error expected here, a brotherhood with an history creates a Legal Record with valid data
			 * VAT number type tested: Espana
			 */
			//Correcto (Patron Espana)
			}, {
				"brotherhood1", "title", "description", "legalName", "99999999", null
			/**
			 * a)Requirement 1
			 * b)There is no error expected here, a brotherhood with an history creates a Legal Record with valid data
			 * VAT number type tested: Finlandia
			 */
			//Correcto (Patron Finlandia)
			}, {
				"brotherhood1", "title", "description", "legalName", "XX999999999", null
			/**
			 * a)Requirement 1
			 * b)There is no error expected here, a brotherhood with an history creates a Legal Record with valid data
			 * VAT number type tested: Francia
			 */
			//Correcto (Patron Francia)
			}, {
				"brotherhood1", "title", "description", "legalName", "999999999", null
			/**
			 * a)Requirement 1
			 * b)There is no error expected here, a brotherhood with an history creates a Legal Record with valid data
			 * VAT number type tested: Reino Unido
			 */
			//Correcto (Patron Reino Unido1)
			}, {
				"brotherhood1", "title", "description", "legalName", "999999999999", null
			/**
			 * a)Requirement 1
			 * b)There is no error expected here, a brotherhood with an history creates a Legal Record with valid data
			 * VAT number type tested: Reino Unido
			 */
			//Correcto (Patron Reino Unido2)
			}, {
				"brotherhood1", "title", "description", "legalName", "XX999", null
			/**
			 * a)Requirement 1
			 * b)There is no error expected here, a brotherhood with an history creates a Legal Record with valid data
			 * VAT number type tested: Reino Unido
			 */
			//Correcto (Patron Reino Unido3)
			}, {
				"brotherhood1", "title", "description", "legalName", "99999999", null
			/**
			 * a)Requirement 1
			 * b)There is no error expected here, a brotherhood with an history creates a Legal Record with valid data
			 * VAT number type tested: Hungria
			 */
			//Correcto (Patron Hungria)
			}, {
				"brotherhood1", "title", "description", "legalName", "9S99999L", null
			/**
			 * a)Requirement 1
			 * b)There is no error expected here, a brotherhood with an history creates a Legal Record with valid data
			 * VAT number type tested: Irlanda
			 */
			//Correcto (Patron Irlanda)
			}, {
				"brotherhood1", "title", "description", "legalName", "99999999999", null
			/**
			 * a)Requirement 1
			 * b)There is no error expected here, a brotherhood with an history creates a Legal Record with valid data
			 * VAT number type tested: Italia
			 */
			//Correcto (Patron Italia)
			}, {
				"brotherhood1", "title", "description", "legalName", "999999999", null
			/**
			 * a)Requirement 1
			 * b)There is no error expected here, a brotherhood with an history creates a Legal Record with valid data
			 * VAT number type tested: Lituania
			 */
			//Correcto (Patron Lituania1)
			}, {
				"brotherhood1", "title", "description", "legalName", "99999999999", null
			/**
			 * a)Requirement 1
			 * b)There is no error expected here, a brotherhood with an history creates a Legal Record with valid data
			 * VAT number type tested: Lituania
			 */
			//Correcto (Patron Lituania2)
			}, {
				"brotherhood1", "title", "description", "legalName", "99999999", null
			/**
			 * a)Requirement 1
			 * b)There is no error expected here, a brotherhood with an history creates a Legal Record with valid data
			 * VAT number type tested: Luxemburgo
			 */
			//Correcto (Patron Luxemburgo)
			}, {
				"brotherhood1", "title", "description", "legalName", "99999999999", null
			/**
			 * a)Requirement 1
			 * b)There is no error expected here, a brotherhood with an history creates a Legal Record with valid data
			 * VAT number type tested: Latvia
			 */
			//Correcto (Patron Latvia)
			}, {
				"brotherhood1", "title", "description", "legalName", "99999999", null
			/**
			 * a)Requirement 1
			 * b)There is no error expected here, a brotherhood with an history creates a Legal Record with valid data
			 * VAT number type tested: Malta
			 */
			//Correcto (Patron Malta)
			}, {
				"brotherhood1", "title", "description", "legalName", "999999999B99", null
			/**
			 * a)Requirement 1
			 * b)There is no error expected here, a brotherhood with an history creates a Legal Record with valid data
			 * VAT number type tested: Holanda
			 */
			//Correcto (Patron Holanda)
			}, {
				"brotherhood1", "title", "description", "legalName", "999999999", null
			/**
			 * a)Requirement 1
			 * b)There is no error expected here, a brotherhood with an history creates a Legal Record with valid data
			 * VAT number type tested: Polonia
			 */
			//Correcto (Patron Polonia)
			}, {
				"brotherhood1", "title", "description", "legalName", "999999999", null
			/**
			 * a)Requirement 1
			 * b)There is no error expected here, a brotherhood with an history creates a Legal Record with valid data
			 * VAT number type tested: Portugal
			 */
			//Correcto (Patron Portugal)
			}, {
				"brotherhood1", "title", "description", "legalName", "999999999", null
			/**
			 * a)Requirement 1
			 * b)There is no error expected here, a brotherhood with an history creates a Legal Record with valid data
			 * VAT number type tested: Portugal
			 */
			//Correcto (Patron Portugal)
			}, {
				"brotherhood1", "title", "description", "legalName", "99", null
			/**
			 * a)Requirement 1
			 * b)There is no error expected here, a brotherhood with an history creates a Legal Record with valid data
			 * VAT number type tested: Rumania
			 */
			//Correcto (Patron Rumania1)
			}, {
				"brotherhood1", "title", "description", "legalName", "999", null
			/**
			 * a)Requirement 1
			 * b)There is no error expected here, a brotherhood with an history creates a Legal Record with valid data
			 * VAT number type tested: Rumania
			 */
			//Correcto (Patron Rumania2)
			}, {
				"brotherhood1", "title", "description", "legalName", "9999", null
			/**
			 * a)Requirement 1
			 * b)There is no error expected here, a brotherhood with an history creates a Legal Record with valid data
			 * VAT number type tested: Rumania
			 */
			//Correcto (Patron Rumania3)
			}, {
				"brotherhood1", "title", "description", "legalName", "99999", null
			/**
			 * a)Requirement 1
			 * b)There is no error expected here, a brotherhood with an history creates a Legal Record with valid data
			 * VAT number type tested: Rumania
			 */
			//Correcto (Patron Rumania4)
			}, {
				"brotherhood1", "title", "description", "legalName", "999999", null
			/**
			 * a)Requirement 1
			 * b)There is no error expected here, a brotherhood with an history creates a Legal Record with valid data
			 * VAT number type tested: Rumania
			 */
			//Correcto (Patron Rumania5)
			}, {
				"brotherhood1", "title", "description", "legalName", "9999999", null
			/**
			 * a)Requirement 1
			 * b)There is no error expected here, a brotherhood with an history creates a Legal Record with valid data
			 * VAT number type tested: Rumania
			 */
			//Correcto (Patron Rumania6)
			}, {
				"brotherhood1", "title", "description", "legalName", "99999999", null
			/**
			 * a)Requirement 1
			 * b)There is no error expected here, a brotherhood with an history creates a Legal Record with valid data
			 * VAT number type tested: Rumania
			 */
			//Correcto (Patron Rumania7)
			}, {
				"brotherhood1", "title", "description", "legalName", "999999999", null
			/**
			 * a)Requirement 1
			 * b)There is no error expected here, a brotherhood with an history creates a Legal Record with valid data
			 * VAT number type tested: Rumania
			 */
			//Correcto (Patron Rumania8)
			}, {
				"brotherhood1", "title", "description", "legalName", "9999999999", null
			/**
			 * a)Requirement 1
			 * b)There is no error expected here, a brotherhood with an history creates a Legal Record with valid data
			 * VAT number type tested: Rumania
			 */
			//Correcto (Patron Rumania9)
			}, {
				"brotherhood1", "title", "description", "legalName", "99999999999", null
			/**
			 * a)Requirement 1
			 * b)There is no error expected here, a brotherhood with an history creates a Legal Record with valid data
			 * VAT number type tested: Suecia
			 */
			//Correcto (Patron Suecia)
			}, {
				"brotherhood1", "title", "description", "legalName", "99999999", null
			/**
			 * a)Requirement 1
			 * b)There is no error expected here, a brotherhood with an history creates a Legal Record with valid data
			 * VAT number type tested: Eslovenia
			 */
			//Correcto (Patron Eslovenia)
			}, {
				"brotherhood1", "title", "description", "legalName", "999999999", null
			/**
			 * a)Requirement 1
			 * b)There is no error expected here, a brotherhood with an history creates a Legal Record with valid data
			 * VAT number type tested: Eslovaquia
			 */
			//Correcto (Patron Eslovaquia)
			}, {
				null, "title", "description", "legalName", "U99999999", IllegalArgumentException.class
			/**
			 * a)Requirement 1
			 * b)In this case, a Illegal Argument Exception is expected because an anonymous user is trying to create
			 * a Legal record (Only Brotherhoods can do that)
			 */
			}, {
				"member1", "title", "description", "legalName", "U99999999", IllegalArgumentException.class
			/**
			 * a)Requirement 1
			 * b)In this case, a Illegal Argument Exception is expected because an user registered as member is trying to Create
			 * a Legal record (Only Brotherhoods can do that)
			 */
			}, {
				"admin1", "title", "description", "legalName", "U99999999", IllegalArgumentException.class
			/**
			 * a)Requirement 1
			 * b)In this case, a Illegal Argument Exception is expected because an user registered as admin is trying to Create
			 * a Legal record (Only Brotherhoods can do that)
			 */
			}, {
				"sponsor1", "title", "description", "legalName", "U99999999", IllegalArgumentException.class
			/**
			 * a)Requirement 1
			 * b)In this case, a Illegal Argument Exception is expected because an user registered as sponsor is trying to Create
			 * a Legal record (Only Brotherhoods can do that)
			 */
			}, {
				"chapter1", "title", "description", "legalName", "U99999999", IllegalArgumentException.class
			/**
			 * a)Requirement 1
			 * b)In this case, a Illegal Argument Exception is expected because an user registered as chapter is trying to Create
			 * a Legal record (Only Brotherhoods can do that)
			 */

			}, {
				"brotherhood1", "", "description", "legalName", "U99999999", ConstraintViolationException.class
			/**
			 * a)Requirement 1
			 * b)In this case, a Constraint Violation Exception is expected because we are trying to create a Legal Record
			 * record with a blank title
			 */
			}, {
				"brotherhood1", "title", "", "legalName", "U99999999", ConstraintViolationException.class
			/**
			 * a)Requirement 1
			 * b)In this case, a Constraint Violation Exception is expected because we are trying to create a Legal Record
			 * record with a blank description
			 */
			}, {
				"brotherhood1", "title", "description", "", "U99999999", ConstraintViolationException.class
			/**
			 * a)Requirement 1
			 * b)In this case, a Constraint Violation Exception is expected because we are trying to create a Legal Record
			 * record with a blank legal name
			 */
			}, {
				"brotherhood1", "title", "description", "legalName", "", ConstraintViolationException.class
			/**
			 * a)Requirement 1
			 * b)In this case, a Constraint Violation Exception is expected because we are trying to create a Legal Record
			 * record with a blank VAT number
			 */
			}, {
				"brotherhood4", "title", "description", "legalName", "U99999999", IllegalArgumentException.class
			/**
			 * a)Requirement 1
			 * b)In this case, a Illegal Argument Exception is expected because we are trying to Create a Legal
			 * record logged as a Brotherhood that hasn't created an History yet
			 */
			}, {
				"brotherhood1", "title", "description", "legalName", "incorrecto", ConstraintViolationException.class
			/**
			 * a)Requirement 1
			 * b)In this case, a Constraint Violation Exception is expected because we are trying to Create a Legal
			 * record with an invalid pattern
			 */
			}
		};
		for (int i = 0; i < testingData.length; i++)
			this.templateCreateLegalRecord((String) testingData[i][0], (String) testingData[i][1], (String) testingData[i][2], (String) testingData[i][3], (String) testingData[i][4], (Class<?>) testingData[i][5]);
	}
	protected void templateCreateLegalRecord(String username, String title, String description, String legalName, String vatNumber, Class<?> expected) {

		Class<?> caught = null;

		try {
			this.authenticate(username);
			LegalRecord legalRecord = this.legalRecordService.create();

			legalRecord.setTitle(title);
			legalRecord.setDescription(description);
			//legalRecord.setLaws(laws) en el reconstruct
			legalRecord.setLegalName(legalName);

			legalRecord.setVatNumber(vatNumber);

			this.legalRecordService.saveLegalRecord(legalRecord);

			this.legalRecordService.flush();

			this.unauthenticate();

		} catch (Throwable oops) {
			caught = oops.getClass();
		}
		super.checkExceptions(expected, caught);
	}

	//EDIT

	/**
	 * The data of this driver is focused to test the edit of a Legal Record
	 */

	@Test
	public void driverEdiLegalRecord() {
		Object testingData[][] = {
			{
				"brotherhood1", "legalRecord1", "newTitle", "newDescription", "newLegalName", "X77777777", null
			/**
			 * a)Requirement 1
			 * b)There is no error expected here, a brotherhood with an history edits one of his Legal Records
			 */
			}, {
				"brotherhood1", "legalRecord1", "", "newDescription", "newLegalName", "X77777777", ConstraintViolationException.class
			/**
			 * a)Requirement 1
			 * b)In this case, a Constraint Violation Exception is expected because we are trying to edit a Legal Record
			 * record with a blank title
			 */
			}, {
				"brotherhood1", "legalRecord1", "newTitle", "", "newLegalName", "X77777777", ConstraintViolationException.class
			/**
			 * a)Requirement 1
			 * b)In this case, a Constraint Violation Exception is expected because we are trying to edit a Legal Record
			 * record with a blank description
			 */
			}, {
				"brotherhood1", "legalRecord1", "newTitle", "newDescription", "", "X77777777", ConstraintViolationException.class
			/**
			 * a)Requirement 1
			 * b)In this case, a Constraint Violation Exception is expected because we are trying to edit a Legal Record
			 * record with a blank legal name
			 */
			}, {
				"brotherhood1", "legalRecord1", "newTitle", "newDescription", "newLegalName", "", ConstraintViolationException.class
			/**
			 * a)Requirement 1
			 * b)In this case, a Constraint Violation Exception is expected because we are trying to edit a Legal Record
			 * record with a blank VAT number
			 */
			}, {
				"brotherhood1", "legalRecord1", "newTitle", "newDescription", "newLegalName", "incorrecto", ConstraintViolationException.class
			/**
			 * a)Requirement 1
			 * b)In this case, a Constraint Violation Exception is expected because we are trying to edit a Legal Record
			 * record with a wrong pattern for the VAT number
			 */
			}, {
				"brotherhood2", "legalRecord1", "newTitle", "newDescription", "newLegalName", "X77777777", IllegalArgumentException.class
			/**
			 * a)Requirement 1
			 * b)In this case, a Illegal Argument Exception is expected because a Brotherhood with an History is trying to
			 * edit a legal record that belongs to another user
			 */
			}, {
				"member1", "legalRecord1", "newTitle", "newDescription", "newLegalName", "X77777777", IllegalArgumentException.class
			/**
			 * a)Requirement 1
			 * b)In this case, a Illegal Argument Exception is expected because an user registered as member is trying to edit
			 * a legal record (Only Brotherhoods can do that)
			 */
			}, {
				"admin1", "legalRecord1", "newTitle", "newDescription", "newLegalName", "X77777777", IllegalArgumentException.class
			/**
			 * a)Requirement 1
			 * b)In this case, a Illegal Argument Exception is expected because an user registered as admin is trying to edit
			 * a legal record (Only Brotherhoods can do that)
			 */
			}, {
				"sponsor1", "legalRecord1", "newTitle", "newDescription", "newLegalName", "X77777777", IllegalArgumentException.class
			/**
			 * a)Requirement 1
			 * b)In this case, a Illegal Argument Exception is expected because an user registered as sponsor is trying to edit
			 * a legal record (Only Brotherhoods can do that)
			 */
			}, {
				"chapter1", "legalRecord1", "newTitle", "newDescription", "newLegalName", "X77777777", IllegalArgumentException.class
			/**
			 * a)Requirement 1
			 * b)In this case, a Illegal Argument Exception is expected because an user registered as chapter is trying to edit
			 * a legal record (Only Brotherhoods can do that)
			 */
			}, {
				null, "legalRecord1", "newTitle", "newDescription", "newLegalName", "X77777777", IllegalArgumentException.class
			/**
			 * a)Requirement 1
			 * b)In this case, a Illegal Argument Exception is expected because an anonymous user is trying to edit
			 * a legal record (Only Brotherhoods can do that)
			 */
			}
		};

		for (int i = 0; i < testingData.length; i++)
			this.templateEditLegalRecord((String) testingData[i][0], (String) testingData[i][1], (String) testingData[i][2], (String) testingData[i][3], (String) testingData[i][4], (String) testingData[i][5], (Class<?>) testingData[i][6]);

	}
	protected void templateEditLegalRecord(String username, String beanLegal, String newTitle, String newDescription, String newLegalName, String newVatNumber, Class<?> expected) {

		Class<?> caught = null;

		try {
			int legalRecordId = super.getEntityId(beanLegal);

			LegalRecord legalRecord = this.legalRecordService.findOne(legalRecordId);
			super.authenticate(username);

			legalRecord.setTitle(newTitle);
			legalRecord.setDescription(newDescription);
			legalRecord.setLegalName(newLegalName);
			legalRecord.setVatNumber(newVatNumber);

			this.legalRecordService.saveLegalRecord(legalRecord);

			this.legalRecordService.flush();

			this.unauthenticate();

		} catch (Throwable oops) {
			caught = oops.getClass();
		}
		super.checkExceptions(expected, caught);
	}

	//DELETE
	/**
	 * The data of this driver is focused to test the edit of a Legal Record
	 */

	@Test
	public void driverDeleteLegalRecord() {
		Object testingData[][] = {
			{
				"brotherhood1", "legalRecord1", null
			/**
			 * a)Requirement 1
			 * b)There is no error expected here, a brotherhood with an history deletes one of his Legal Records
			 */
			}, {
				"brotherhood2", "legalRecord1", IllegalArgumentException.class
			/**
			 * a)Requirement 1
			 * b)In this case, a Illegal Argument Exception is expected because a Brotherhood with an History is trying to
			 * delete a legal record that belongs to another user
			 */
			}, {
				null, "legalRecord1", IllegalArgumentException.class
			/**
			 * a)Requirement 1
			 * b)In this case, a Illegal Argument Exception is expected because an anonymous user is trying to delete
			 * a legal record (Only Brotherhoods can do that)
			 */
			}, {
				"member1", "legalRecord1", IllegalArgumentException.class
			/**
			 * a)Requirement 1
			 * b)In this case, a Illegal Argument Exception is expected because an user registered as member is trying to delete
			 * a legal record (Only Brotherhoods can do that)
			 */
			}, {
				"admin1", "legalRecord1", IllegalArgumentException.class
			/**
			 * a)Requirement 1
			 * b)In this case, a Illegal Argument Exception is expected because an user registered as admin is trying to delete
			 * a legal record (Only Brotherhoods can do that)
			 */
			}, {
				"chapter1", "legalRecord1", IllegalArgumentException.class
			/**
			 * a)Requirement 1
			 * b)In this case, a Illegal Argument Exception is expected because an user registered as chapter is trying to delete
			 * a legal record (Only Brotherhoods can do that)
			 */
			}, {
				"sponsor1", "legalRecord1", IllegalArgumentException.class
			/**
			 * a)Requirement 1
			 * b)In this case, a Illegal Argument Exception is expected because an user registered as sponsor is trying to delete
			 * a legal record (Only Brotherhoods can do that)
			 */
			}
		};
		for (int i = 0; i < testingData.length; i++)
			this.templateDeleteLegalRecord((String) testingData[i][0], (String) testingData[i][1], (Class<?>) testingData[i][2]);

	}

	protected void templateDeleteLegalRecord(String username, String beanLegal, Class<?> expected) {

		Class<?> caught = null;

		try {
			int legalRecordId = super.getEntityId(beanLegal);

			LegalRecord legalRecord = this.legalRecordService.findOne(legalRecordId);
			super.authenticate(username);

			this.legalRecordService.deleteLegalRecord(legalRecord);

			this.legalRecordService.flush();

			this.unauthenticate();

		} catch (Throwable oops) {
			caught = oops.getClass();
		}
		super.checkExceptions(expected, caught);
	}

	//---------------------------------LINK RECORD-----------------------------------
	//-------------------------------------------------------------------------------

	//CREATE
	/**
	 * The data of this driver is focused to test the creation of a link Record
	 * For each Period Record, the System must store a title, a description and a
	 * link to another brotherhoods, note that there are no linkRecords in the file
	 * PopulateDatabase.xml because the links are calculated from the linked brotherhood id
	 * 
	 */

	@Test
	public void driverCreateLinkRecord() {
		Object testingData[][] = {
			{
				"brotherhood1", "title", "description", "brotherhood2", null
			/**
			 * a)Requirement 1
			 * b)There is no error expected here, a brotherhood with an history creates a link Record with valid data
			 */
			}, {
				"brotherhood1", "", "description", "brotherhood2", ConstraintViolationException.class
			/**
			 * a)Requirement 1
			 * b)In this case, a Constraint Violation Exception is expected because we are trying to Create a link
			 * record assigning it a blank title
			 */
			}, {
				"brotherhood1", "title", "", "brotherhood2", ConstraintViolationException.class
			/**
			 * a)Requirement 1
			 * b)In this case, a Constraint Violation Exception is expected because we are trying to Create a link
			 * record assigning it a blank description
			 */
			}, {
				"brotherhood1", "title", "description", "member1", IllegalArgumentException.class
			/**
			 * a)Requirement 1
			 * b)In this case, an Illegal Argument Exception is expected because we are trying to Create a link
			 * record assigning it a member instead of a Brotherhood
			 */
			}, {
				null, "title", "description", "brotherhood2", IllegalArgumentException.class
			/**
			 * a)Requirement 1
			 * b)In this case, a Illegal Argument Exception is expected because an anonymous user is trying to create
			 * a Link record (Only Brotherhoods can do that)
			 */
			}, {
				"member1", "title", "description", "brotherhood2", IllegalArgumentException.class
			/**
			 * a)Requirement 1
			 * b)In this case, a Illegal Argument Exception is expected because an user authenticated as member is trying to create
			 * a Link record (Only Brotherhoods can do that)
			 */
			}, {
				"sponsor1", "title", "description", "brotherhood2", IllegalArgumentException.class
			/**
			 * a)Requirement 1
			 * b)In this case, a Illegal Argument Exception is expected because an user authenticated as sponsor is trying to create
			 * a Link record (Only Brotherhoods can do that)
			 */
			}, {
				"admin1", "title", "description", "brotherhood2", IllegalArgumentException.class
			/**
			 * a)Requirement 1
			 * b)In this case, a Illegal Argument Exception is expected because an user authenticated as admin is trying to create
			 * a Link record (Only Brotherhoods can do that)
			 */
			}, {
				"chapter1", "title", "description", "brotherhood2", IllegalArgumentException.class
			/**
			 * a)Requirement 1
			 * b)In this case, a Illegal Argument Exception is expected because an user authenticated as chapter is trying to create
			 * a Link record (Only Brotherhoods can do that)
			 */
			}, {
				"brotherhood1", "title", "description", "brotherhood1", IllegalArgumentException.class
			/**
			 * a)Requirement 1
			 * b)In this case, a Illegal Argument Exception is expected because the logged brotherhood is trying to reference
			 * himself in the link of the record
			 */
			}, {
				"brotherhood4", "title", "description", "brotherhood2", IllegalArgumentException.class
			/**
			 * a)Requirement 1
			 * b)In this case, a Illegal Argument Exception is expected because we are trying to Create a link
			 * record logged as a Brotherhood that hasn't created an History yet
			 */
			}

		};

		for (int i = 0; i < testingData.length; i++)
			this.templateCreateLinkRecord((String) testingData[i][0], (String) testingData[i][1], (String) testingData[i][2], (String) testingData[i][3], (Class<?>) testingData[i][4]);
	}

	protected void templateCreateLinkRecord(String username, String title, String description, String linkedBrotherhoodUsername, Class<?> expected) {

		Class<?> caught = null;

		try {
			this.authenticate(username);

			FormObjectLinkRecord formObjectLinkRecord = this.linkRecordService.createFormObjectLinkRecord();

			formObjectLinkRecord.setTitle(title);
			formObjectLinkRecord.setDescription(description);

			int linkedBrotherhoodId = super.getEntityId(linkedBrotherhoodUsername);
			Brotherhood linkedBrotherhood = this.brotherhoodService.findOne(linkedBrotherhoodId);

			formObjectLinkRecord.setBrotherhood(linkedBrotherhood);

			LinkRecord linkRecord = this.linkRecordService.create();

			linkRecord = this.linkRecordService.reconstructFormObject(formObjectLinkRecord, null);

			this.linkRecordService.saveLinkRecord(linkRecord);

			this.linkRecordService.flush();
			this.unauthenticate();

		} catch (Throwable oops) {
			caught = oops.getClass();
		}
		super.checkExceptions(expected, caught);
	}

	//EDIT

	/**
	 * The data of this driver is focused to test the edit of an link Record, note that there are no link records
	 * in the PopulateDatabase.xml, so we have to create a new link record in the test before testing the editing
	 */

	@Test
	public void driverEditLinkRecord() {
		Object testingData[][] = {
			{
				"brotherhood1", "newTitle", "newDescription", "brotherhood2", null
			/**
			 * a)Requirement 1
			 * b)There is no error expected here, a brotherhood with an history edits one of his Link Records
			 */
			}, {
				"brotherhood1", "", "newDescription", "brotherhood2", ConstraintViolationException.class
			/**
			 * a)Requirement 1
			 * b)In this case, a Constraint Violation Exception is expected because we are trying to Edit a Link
			 * record assigning it a blank title
			 */
			}, {
				"brotherhood1", "newTitle", "", "brotherhood2", ConstraintViolationException.class
			/**
			 * a)Requirement 1
			 * b)In this case, a Constraint Violation Exception is expected because we are trying to Edit a Link
			 * record assigning it a blank description
			 */
			}, {
				null, "newTitle", "newDescription", "brotherhood2", IllegalArgumentException.class
			/**
			 * a)Requirement 1
			 * b)In this case, a Illegal Argument Exception is expected because an anonymous user is trying to edit
			 * a link record (Only Brotherhoods can do that)
			 */
			}, {
				"member1", "newTitle", "newDescription", "brotherhood2", IllegalArgumentException.class
			/**
			 * a)Requirement 1
			 * b)In this case, a Illegal Argument Exception is expected because an user logged as member is trying to edit
			 * a link record (Only Brotherhoods can do that)
			 */
			}, {
				"admin1", "newTitle", "newDescription", "brotherhood2", IllegalArgumentException.class
			/**
			 * a)Requirement 1
			 * b)In this case, a Illegal Argument Exception is expected because an user logged as admin is trying to edit
			 * a link record (Only Brotherhoods can do that)
			 */
			}, {
				"sponsor1", "newTitle", "newDescription", "brotherhood2", IllegalArgumentException.class
			/**
			 * a)Requirement 1
			 * b)In this case, a Illegal Argument Exception is expected because an user logged as sponsor is trying to edit
			 * a link record (Only Brotherhoods can do that)
			 */
			}, {
				"chapter1", "newTitle", "newDescription", "brotherhood2", IllegalArgumentException.class
			/**
			 * a)Requirement 1
			 * b)In this case, a Illegal Argument Exception is expected because an user logged as chapter is trying to edit
			 * a link record (Only Brotherhoods can do that)
			 */
			}, {
				"brotherhood2", "newTitle", "newDescription", "brotherhood3", IllegalArgumentException.class
			/**
			 * a)Requirement 1
			 * b)In this case, a Illegal Argument Exception is expected because an user logged as a brotherhood is trying
			 * to edit a link record that belongs to another brotherhood
			 */
			}, {
				"brotherhood1", "newTitle", "newDescription", "brotherhood1", IllegalArgumentException.class
			/**
			 * a)Requirement 1
			 * b)In this case, a Illegal Argument Exception is expected because the logged brotherhood is trying to reference
			 * himself in the link of the record
			 */
			}, {
				"brotherhood1", "newTitle", "newDescription", "member1", IllegalArgumentException.class
			/**
			 * a)Requirement 1
			 * b)In this case, an Illegal Argument Exception is expected because we are trying to edit a link
			 * record assigning it a member instead of a Brotherhood
			 */
			}
		};

		for (int i = 0; i < testingData.length; i++)
			this.templateEditLinkRecord((String) testingData[i][0], (String) testingData[i][1], (String) testingData[i][2], (String) testingData[i][3], (Class<?>) testingData[i][4]);
	}

	protected void templateEditLinkRecord(String username, String newTitle, String newDescription, String newLinkedBrotherhoodUsername, Class<?> expected) {

		Class<?> caught = null;

		try {

			//Creacion (no se puede incluir ninguno en populate
			this.authenticate("brotherhood1");

			FormObjectLinkRecord oldFormObjectLinkRecord = this.linkRecordService.createFormObjectLinkRecord();

			oldFormObjectLinkRecord.setTitle("title");					//Los valores iniciales no son relevantes, suponemos create exitoso
			oldFormObjectLinkRecord.setDescription("description");

			int oldLinkedBrotherhoodId = super.getEntityId("brotherhood3");	//Suponemos que ha sido creada para 3
			Brotherhood oldLinkedBrotherhood = this.brotherhoodService.findOne(oldLinkedBrotherhoodId);

			oldFormObjectLinkRecord.setBrotherhood(oldLinkedBrotherhood);

			LinkRecord oldLinkRecord = this.linkRecordService.create();

			oldLinkRecord = this.linkRecordService.reconstructFormObject(oldFormObjectLinkRecord, null);

			LinkRecord created = this.linkRecordService.saveLinkRecord(oldLinkRecord);

			this.unauthenticate();

			///////////////////////////////////////////////////////////////////////////////////////
			int linkRecordId = created.getId();

			int newLinkedBrotherhoodId = super.getEntityId(newLinkedBrotherhoodUsername);
			Brotherhood newLinkedBrotherhood = this.brotherhoodService.findOne(newLinkedBrotherhoodId);

			LinkRecord edited = new LinkRecord();

			//LinkRecord linkRecord = this.linkRecordService.findOne(linkRecordId);
			super.authenticate(username);

			FormObjectLinkRecord newFormObjectLinkRecord = this.linkRecordService.prepareFormObjectLinkRecord(linkRecordId);

			newFormObjectLinkRecord.setTitle(newTitle);
			newFormObjectLinkRecord.setDescription(newDescription);
			newFormObjectLinkRecord.setBrotherhood(newLinkedBrotherhood);

			edited = this.linkRecordService.reconstructFormObject(newFormObjectLinkRecord, null);

			this.linkRecordService.saveLinkRecord(edited);

			this.linkRecordService.flush();

			this.unauthenticate();

		} catch (Throwable oops) {
			caught = oops.getClass();
		}
		super.checkExceptions(expected, caught);
	}

	//DELETE

	@Test
	public void driverDeleteLinkRecord() {
		Object testingData[][] = {
			{
				"brotherhood1", null
			/**
			 * a)Requirement 1
			 * b)There is no error expected here, a brotherhood with an history deletes one of his Link Records
			 */
			}, {
				null, IllegalArgumentException.class
			/**
			 * a)Requirement 1
			 * b)In this case, a Illegal Argument Exception is expected because an anonymous user is trying to delete
			 * a legal record (Only Brotherhoods can do that)
			 */
			}, {
				"member1", IllegalArgumentException.class
			/**
			 * a)Requirement 1
			 * b)In this case, a Illegal Argument Exception is expected because an user registered as member is trying to delete
			 * a legal record (Only Brotherhoods can do that)
			 */
			}, {
				"chapter1", IllegalArgumentException.class
			/**
			 * a)Requirement 1
			 * b)In this case, a Illegal Argument Exception is expected because an user registered as chapter is trying to delete
			 * a legal record (Only Brotherhoods can do that)
			 */
			}, {
				"sponsor1", IllegalArgumentException.class
			/**
			 * a)Requirement 1
			 * b)In this case, a Illegal Argument Exception is expected because an user registered as sponsor is trying to delete
			 * a legal record (Only Brotherhoods can do that)
			 */
			}, {
				"admin1", IllegalArgumentException.class
			/**
			 * a)Requirement 1
			 * b)In this case, a Illegal Argument Exception is expected because an user registered as admin is trying to delete
			 * a legal record (Only Brotherhoods can do that)
			 */
			}, {
				"brotherhood2", IllegalArgumentException.class
			/**
			 * a)Requirement 1
			 * b)In this case, a Illegal Argument Exception is expected because a Brotherhood with an History is trying to
			 * delete a link record that belongs to another user
			 */
			}
		};
		for (int i = 0; i < testingData.length; i++)
			this.templateDeleteLinkRecord((String) testingData[i][0], (Class<?>) testingData[i][1]);

	}
	protected void templateDeleteLinkRecord(String username, Class<?> expected) {

		Class<?> caught = null;

		try {

			//Creacion (no se puede incluir ninguno en populate
			this.authenticate("brotherhood1");

			FormObjectLinkRecord oldFormObjectLinkRecord = this.linkRecordService.createFormObjectLinkRecord();

			oldFormObjectLinkRecord.setTitle("title");					//Los valores iniciales no son relevantes, suponemos create exitoso
			oldFormObjectLinkRecord.setDescription("description");

			int oldLinkedBrotherhoodId = super.getEntityId("brotherhood3");	//Suponemos que ha sido creada para 3
			Brotherhood oldLinkedBrotherhood = this.brotherhoodService.findOne(oldLinkedBrotherhoodId);

			oldFormObjectLinkRecord.setBrotherhood(oldLinkedBrotherhood);

			LinkRecord oldLinkRecord = this.linkRecordService.create();

			oldLinkRecord = this.linkRecordService.reconstructFormObject(oldFormObjectLinkRecord, null);

			LinkRecord created = this.linkRecordService.saveLinkRecord(oldLinkRecord);

			this.unauthenticate();

			///////////////////////////////////////////////////////////////////////////////////////
			int linkRecordId = created.getId();

			LinkRecord linkRecord = this.linkRecordService.findOne(linkRecordId);
			super.authenticate(username);

			this.linkRecordService.deleteLinkRecord(linkRecord);
			this.unauthenticate();

		} catch (Throwable oops) {
			caught = oops.getClass();
		}
		super.checkExceptions(expected, caught);
	}

	//-------------------------------MISCELLANEOUS RECORD----------------------------
	//-------------------------------------------------------------------------------

	//CREATE

	/**
	 * The data of this driver is focused to test the creation of a Miscellaneous Record
	 * For each Miscellaneous Record, the System must store a title and a Description
	 * 
	 */

	@Test
	public void driverCreateMiscellaneousRecord() {
		Object testingData[][] = {
			{
				"brotherhood1", "title", "description", null
			/**
			 * a)Requirement 1
			 * b)There is no error expected here, a brotherhood with an history creates a Miscellaneous Record with valid data
			 */
			}, {
				"brotherhood1", "", "description", ConstraintViolationException.class
			/**
			 * a)Requirement 1
			 * b)In this case, a Constraint Violation Exception is expected because we are trying to Create a Miscellaneous
			 * record assigning it a blank title
			 */
			}, {
				"brotherhood1", "title", "", ConstraintViolationException.class
			/**
			 * a)Requirement 1
			 * b)In this case, a Constraint Violation Exception is expected because we are trying to Create a Miscellaneous
			 * record assigning it a blank description
			 */
			}, {
				"member1", "title", "description", IllegalArgumentException.class
			/**
			 * a)Requirement 1
			 * b)In this case, a Illegal Argument Exception is expected because an user registered as member is trying to create
			 * a Miscellaneous record (Only Brotherhoods can do that)
			 */
			}, {
				"sponsor1", "title", "description", IllegalArgumentException.class
			/**
			 * a)Requirement 1
			 * b)In this case, a Illegal Argument Exception is expected because an user registered as sponsor is trying to create
			 * a Miscellaneous record (Only Brotherhoods can do that)
			 */
			}, {
				"admin1", "title", "description", IllegalArgumentException.class
			/**
			 * a)Requirement 1
			 * b)In this case, a Illegal Argument Exception is expected because an user registered as admin is trying to create
			 * a Miscellaneous record (Only Brotherhoods can do that)
			 */
			}, {
				"chapter1", "title", "description", IllegalArgumentException.class
			/**
			 * a)Requirement 1
			 * b)In this case, a Illegal Argument Exception is expected because an user registered as chapter is trying to create
			 * a Miscellaneous record (Only Brotherhoods can do that)
			 */
			}, {
				null, "title", "description", IllegalArgumentException.class
			/**
			 * a)Requirement 1
			 * b)In this case, a Illegal Argument Exception is expected because an anonymous user is trying to create
			 * a Miscellaneous record (Only Brotherhoods can do that)
			 */
			}, {
				"brotherhood4", "title", "description", IllegalArgumentException.class
			/**
			 * a)Requirement 1
			 * b)In this case, a Illegal Argument Exception is expected because we are trying to Create a Miscellaneous
			 * record logged as a Brotherhood that hasn't created an History yet
			 */
			}
		};

		for (int i = 0; i < testingData.length; i++)
			this.templateCreateMiscellaneousRecord((String) testingData[i][0], (String) testingData[i][1], (String) testingData[i][2], (Class<?>) testingData[i][3]);
	}

	protected void templateCreateMiscellaneousRecord(String username, String title, String description, Class<?> expected) {

		Class<?> caught = null;

		try {
			this.authenticate(username);
			MiscellaneousRecord miscellaneousRecord = this.miscellaneousRecordService.create();

			miscellaneousRecord.setTitle(title);
			miscellaneousRecord.setDescription(description);

			this.miscellaneousRecordService.saveMiscellaneousRecord(miscellaneousRecord);
			this.miscellaneousRecordService.flush();

			this.unauthenticate();

		} catch (Throwable oops) {
			caught = oops.getClass();
		}
		super.checkExceptions(expected, caught);
	}

	//EDIT

	@Test
	public void driverEditMiscellaneousRecord() {
		Object testingData[][] = {
			{
				"brotherhood1", "miscellaneousRecord1", "newTitle", "newDescription", null
			/**
			 * a)Requirement 1
			 * b)There is no error expected here, a brotherhood with an history edits one of his Miscellaneous Records
			 */
			}, {
				"brotherhood1", "miscellaneousRecord1", "", "newDescription", ConstraintViolationException.class
			/**
			 * a)Requirement 1
			 * b)In this case, a Constraint Violation Exception is expected because we are trying to edit a Miscellaneous
			 * record assigning it a blank title
			 */
			}, {
				"brotherhood1", "miscellaneousRecord1", "newTitle", "", ConstraintViolationException.class
			/**
			 * a)Requirement 1
			 * b)In this case, a Constraint Violation Exception is expected because we are trying to edit a Miscellaneous
			 * record assigning it a blank description
			 */
			}, {
				"member1", "miscellaneousRecord1", "newTitle", "newDescription", IllegalArgumentException.class
			/**
			 * a)Requirement 1
			 * b)In this case, a Illegal Argument Exception is expected because an user registered as member is trying to edit
			 * a Miscellaneous record (Only Brotherhoods can do that)
			 */
			}, {
				"chapter1", "miscellaneousRecord1", "newTitle", "newDescription", IllegalArgumentException.class
			/**
			 * a)Requirement 1
			 * b)In this case, a Illegal Argument Exception is expected because an user registered as chapter is trying to edit
			 * a Miscellaneous record (Only Brotherhoods can do that)
			 */
			}, {
				"admin1", "miscellaneousRecord1", "newTitle", "newDescription", IllegalArgumentException.class
			/**
			 * a)Requirement 1
			 * b)In this case, a Illegal Argument Exception is expected because an user registered as admin is trying to edit
			 * a Miscellaneous record (Only Brotherhoods can do that)
			 */
			}, {
				"sponsor1", "miscellaneousRecord1", "newTitle", "newDescription", IllegalArgumentException.class
			/**
			 * a)Requirement 1
			 * b)In this case, a Illegal Argument Exception is expected because an user registered as sponsor is trying to edit
			 * a Miscellaneous record (Only Brotherhoods can do that)
			 */
			}, {
				null, "miscellaneousRecord1", "newTitle", "newDescription", IllegalArgumentException.class
			/**
			 * a)Requirement 1
			 * b)In this case, a Illegal Argument Exception is expected because an anonymous user is trying to edit
			 * a Miscellaneous record (Only Brotherhoods can do that)
			 */
			}, {
				"brotherhood2", "miscellaneousRecord1", "newTitle", "newDescription", IllegalArgumentException.class
			/**
			 * a)Requirement 1
			 * b)In this case, a Illegal Argument Exception is expected because a Brotherhood with an History is trying to
			 * edit a Miscellaneous record that belongs to another user
			 */
			}, {
				"brotherhood4", "miscellaneousRecord1", "newTitle", "newDescription", IllegalArgumentException.class
			/**
			 * a)Requirement 1
			 * b)In this case, a Illegal Argument Exception is expected because we are trying to edit a Miscellaneous
			 * record logged as a Brotherhood that hasn't created an History yet
			 */
			}
		};

		for (int i = 0; i < testingData.length; i++)
			this.templateEditMiscellaneousRecord((String) testingData[i][0], (String) testingData[i][1], (String) testingData[i][2], (String) testingData[i][3], (Class<?>) testingData[i][4]);
	}

	protected void templateEditMiscellaneousRecord(String username, String beanMiscellaneous, String newTitle, String newDescription, Class<?> expected) {

		Class<?> caught = null;

		try {
			int miscellaneousRecordId = super.getEntityId(beanMiscellaneous);

			MiscellaneousRecord miscellaneousRecord = this.miscellaneousRecordService.findOne(miscellaneousRecordId);
			super.authenticate(username);

			miscellaneousRecord.setTitle(newTitle);
			miscellaneousRecord.setDescription(newDescription);

			this.miscellaneousRecordService.saveMiscellaneousRecord(miscellaneousRecord);

			this.miscellaneousRecordService.flush();

			this.unauthenticate();

		} catch (Throwable oops) {
			caught = oops.getClass();
		}
		super.checkExceptions(expected, caught);
	}

	//DELETE
	/**
	 * The data of this driver is focused to test the edit of a Miscellaneous Record
	 */

	@Test
	public void driverDeleteMiscellaneousRecord() {
		Object testingData[][] = {
			{
				"brotherhood1", "miscellaneousRecord1", null
			/**
			 * a)Requirement 1
			 * b)There is no error expected here, a brotherhood with an history deletes one of his Miscellaneous Records
			 */
			}, {
				null, "miscellaneousRecord1", IllegalArgumentException.class
			/**
			 * a)Requirement 1
			 * b)In this case, a Illegal Argument Exception is expected because an anonymous user is trying to delete
			 * a miscellaneous record (Only Brotherhoods can do that)
			 */
			}, {
				"member1", "miscellaneousRecord1", IllegalArgumentException.class
			/**
			 * a)Requirement 1
			 * b)In this case, a Illegal Argument Exception is expected because an user registered as member is trying to delete
			 * a Period record (Only Brotherhoods can do that)
			 */
			}, {
				"admin1", "miscellaneousRecord1", IllegalArgumentException.class
			/**
			 * a)Requirement 1
			 * b)In this case, a Illegal Argument Exception is expected because an user registered as admin is trying to delete
			 * a Period record (Only Brotherhoods can do that)
			 */
			}, {
				"sponsor1", "miscellaneousRecord1", IllegalArgumentException.class
			/**
			 * a)Requirement 1
			 * b)In this case, a Illegal Argument Exception is expected because an user registered as sponsor is trying to delete
			 * a Period record (Only Brotherhoods can do that)
			 */
			}, {
				"chapter1", "miscellaneousRecord1", IllegalArgumentException.class
			/**
			 * a)Requirement 1
			 * b)In this case, a Illegal Argument Exception is expected because an user registered as chapter is trying to delete
			 * a Period record (Only Brotherhoods can do that)
			 */
			}, {
				"brotherhood2", "miscellaneousRecord1", IllegalArgumentException.class
			/**
			 * a)Requirement 1
			 * b)In this case, a Illegal Argument Exception is expected because a Brotherhood with an History is trying to
			 * delete a miscellaneous record that belongs to another user
			 */
			}
		};

		for (int i = 0; i < testingData.length; i++)
			this.templateDeleteMiscellaneousRecord((String) testingData[i][0], (String) testingData[i][1], (Class<?>) testingData[i][2]);
	}

	protected void templateDeleteMiscellaneousRecord(String username, String beanMiscellaneous, Class<?> expected) {

		Class<?> caught = null;

		try {
			int miscellaneousRecordId = super.getEntityId(beanMiscellaneous);

			MiscellaneousRecord miscellaneousRecord = this.miscellaneousRecordService.findOne(miscellaneousRecordId);
			super.authenticate(username);

			this.miscellaneousRecordService.deleteMiscellaneousRecord(miscellaneousRecord);

			this.miscellaneousRecordService.flush();

			this.unauthenticate();

		} catch (Throwable oops) {
			caught = oops.getClass();
		}
		super.checkExceptions(expected, caught);
	}

}
