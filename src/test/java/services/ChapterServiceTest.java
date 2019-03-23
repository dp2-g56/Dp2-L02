
package services;

import java.util.ArrayList;
import java.util.List;

import javax.validation.ConstraintViolationException;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.authentication.encoding.Md5PasswordEncoder;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import security.Authority;
import security.UserAccount;
import utilities.AbstractTest;
import domain.Area;
import domain.Chapter;
import domain.Parade;
import domain.ParadeStatus;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
	"classpath:spring/junit.xml"
})
@Transactional
public class ChapterServiceTest extends AbstractTest {

	@Autowired
	private ChapterService	chapterService;
	@Autowired
	private ParadeService	paradeService;


	/**
	 * Driver for the method saveCreate from ChapterService. In this driver eight
	 * eight cases are tested and are, in order:
	 * 
	 * 1. A positive case where none of the parameters are wrong and no error is
	 * expected, which is indicated by "null" at the last parameter.
	 * 
	 * 2. A case where the username is blank and an ConstraintViolationException is
	 * expected.
	 * 
	 * 3. A case where the name is blank and an ConstraintViolationException is
	 * expected.
	 * 
	 * 4. A case where the surname is blank and an ConstraintViolationException is
	 * expected.
	 * 
	 * 5. A case where the URL is invalid and not blank, an
	 * ConstraintViolationException is expected.
	 * 
	 * 6. A case where the title is blank and an ConstraintViolationException is
	 * expected.
	 * 
	 * 7. A case where the assigned area is already assigned which is restricted by
	 * the requirement number 5 of Acme Parade. This restriction is checked by an
	 * Assert and thus an IllegalArgumentException is expected.
	 * 
	 * 8. A case where the email is invalid (it didn't follow the pattern) and a
	 * ConstraintViolationException is expected.
	 * 
	 * Note that the restrictions tested in cases 3, 4, 5 and 8 are from Actor; the
	 * restrictions tested in cases 6 and 7 are from Chapter; and the restriction
	 * tested in case 2 is from UserAccount.
	 * 
	 * Note as well the name of this driver, we encountered problem with the current
	 * driver and template methods for testing regarding adding new entities to the
	 * database multiple times on the same driver. This error occurs due to the
	 * database not rolling back its tested content until the driver has been fully
	 * executed.
	 * 
	 * This causes that the first added entity with an error remains on database
	 * when it tries to add the second. Due to the database keeping the tested
	 * entity with an id of 0 (because is new and being a test, its id is not
	 * changed) it will throw a Duplicate Key error instead of the expected one for
	 * every case after the first.
	 * 
	 * We managed to resolve this by forcing the database to rollback to a previous
	 * state without the new entity after every iteration of the template method.
	 * This will be explained later as there are 2 more drivers that shows an
	 * alternative workaround and the effects of not using either of this solutions.
	 **/

	@Test
	public void driverRegisterForcedRollBack() {
		Area areaTest = this.chapterService.listFreeAreas().get(0);
		Area areaTest2 = this.chapterService.listOccupiedAreas().get(0);
		Object testingData[][] = {

			{
				"ChapterTest1", "name1", "surname1", "emailTest@gmail.com", "titleTest1", "https://www.example.com/", null, null
			}, {
				"", "name1", "surname1", "emailTest@gmail.com", "titleTest1", "https://www.example.com/", null, ConstraintViolationException.class
			}, {
				"ChapterTest2", "", "surname2", "emailTest2@gmail.com", "titleTest2", "https://www.example2.com/", null, ConstraintViolationException.class
			}, {
				"ChapterTest3", "name3", "", "emailTest3@gmail.com", "titleTest3", "https://www.example3.com/", null, ConstraintViolationException.class
			}, {
				"ChapterTest4", "name1", "surname1", "emailTest@gmail.com", "titleTest1", "invalid url", areaTest, ConstraintViolationException.class
			}, {
				"ChapterTest6", "name1", "surname1", "emailTest@gmail.com", "", "https://www.example.com/", null, ConstraintViolationException.class
			}, {
				"ChapterTest7", "name1", "surname1", "emailTest@gmail.com", "titleTest1", "https://www.example.com/", areaTest2, IllegalArgumentException.class
			}, {
				"ChapterTest8", "name1", "surname1", "invalid email", "titleTest1", "https://www.example.com/", null, ConstraintViolationException.class
			}
		};

		for (int i = 0; i < testingData.length; i++) {
			this.templateRegister((String) testingData[i][0], (String) testingData[i][1], (String) testingData[i][2], (String) testingData[i][3], (String) testingData[i][4], (String) testingData[i][5], (Area) testingData[i][6],
				(Class<?>) testingData[i][7]);
		}
	}

	/**
	 * This second driver is the alternative to the forced rollback applied on the
	 * template. It consist of getting an exception before adding the new entity to
	 * the database through Asserts in the tested method.
	 * 
	 * By getting this exceptions we will only add one entity before the driver is
	 * fully executed the one on the positive case without exceptions. For every
	 * other iteration, an IllegalArgumentException will be expected as an Assert
	 * will be triggered instead of the ConstraintViolationException for the tags in
	 * the Domain.
	 * 
	 * This driver won't give errors only if the Asserts commented in the tested
	 * methods become uncommented.
	 **/

	public void driverRegisterNegativeWithAsserts() {
		Area areaTest = this.chapterService.listFreeAreas().get(0);
		Area areaTest2 = this.chapterService.listOccupiedAreas().get(0);
		Object testingData[][] = {
			{
				"ChapterTest1", "name1", "surname1", "emailTest@gmail.com", "titleTest1", "https://www.example.com/", null, null
			}, {
				"", "name1", "surname1", "emailTest@gmail.com", "titleTest1", "https://www.example.com/", null, IllegalArgumentException.class
			}, {
				"ChapterTest2", "", "surname2", "emailTest2@gmail.com", "titleTest2", "https://www.example2.com/", null, IllegalArgumentException.class
			}, {
				"ChapterTest3", "name3", "", "emailTest3@gmail.com", "titleTest3", "https://www.example3.com/", null, IllegalArgumentException.class
			}, {
				"ChapterTest4", "name1", "surname1", "emailTest@gmail.com", "titleTest1", "invalid url", areaTest, IllegalArgumentException.class
			}, {
				"ChapterTest6", "name1", "surname1", "emailTest@gmail.com", "", "https://www.example.com/", null, IllegalArgumentException.class
			}, {
				"ChapterTest7", "name1", "surname1", "emailTest@gmail.com", "titleTest1", "https://www.example.com/", areaTest2, IllegalArgumentException.class
			}, {
				"ChapterTest8", "name1", "surname1", "invalid email", "titleTest1", "https://www.example.com/", null, IllegalArgumentException.class
			}
		};

		for (int i = 0; i < testingData.length; i++) {
			this.templateRegister((String) testingData[i][0], (String) testingData[i][1], (String) testingData[i][2], (String) testingData[i][3], (String) testingData[i][4], (String) testingData[i][5], (Area) testingData[i][6],
				(Class<?>) testingData[i][7]);
		}
	}

	/**
	 * This driver shows the effect described in the commented section above the
	 * first driver. This effect takes place if no rollback is force and no
	 * additional Asserts are uncommented from the tested method. As you can see it
	 * is expected DataIntegrityViolationException after the second test for every
	 * consequent test because of the Duplicate Key from id = 0.
	 * 
	 * The downside effect is the inability to recognise if the tests throw the
	 * expected result beyond this test error, making this driver model unable to
	 * provide the information in this cases.
	 **/

	public void driverRegisterNoAssertsNoForcedRollBack() {
		Area areaTest = this.chapterService.listFreeAreas().get(0);
		Area areaTest2 = this.chapterService.listOccupiedAreas().get(0);
		Object testingData[][] = {
			{
				"ChapterTest1", "name1", "surname1", "emailTest@gmail.com", "titleTest1", "https://www.example.com/", null, null
			}, {
				"", "name1", "surname1", "emailTest@gmail.com", "titleTest1", "https://www.example.com/", null, ConstraintViolationException.class
			}, {
				"ChapterTest2", "", "surname2", "emailTest2@gmail.com", "titleTest2", "https://www.example2.com/", null, DataIntegrityViolationException.class
			}, {
				"ChapterTest3", "name3", "", "emailTest3@gmail.com", "titleTest3", "https://www.example3.com/", null, DataIntegrityViolationException.class
			}, {
				"ChapterTest4", "name1", "surname1", "emailTest@gmail.com", "titleTest1", "invalid url", areaTest, DataIntegrityViolationException.class
			}, {
				"ChapterTest6", "name1", "surname1", "emailTest@gmail.com", "", "https://www.example.com/", null, DataIntegrityViolationException.class
			}, {
				"ChapterTest7", "name1", "surname1", "emailTest@gmail.com", "titleTest1", "https://www.example.com/", areaTest2, DataIntegrityViolationException.class
			}, {
				"ChapterTest8", "name1", "surname1", "invalid email", "titleTest1", "https://www.example.com/", null, DataIntegrityViolationException.class
			}
		};

		for (int i = 0; i < testingData.length; i++) {
			this.templateRegister((String) testingData[i][0], (String) testingData[i][1], (String) testingData[i][2], (String) testingData[i][3], (String) testingData[i][4], (String) testingData[i][5], (Area) testingData[i][6],
				(Class<?>) testingData[i][7]);
		}
	}

	/**
	 * On this driver we are testing that the Chapter must select an Area that is
	 * free and even more cases.
	 * 
	 * 1. This is a positive test, is simple.
	 * 
	 * 2. The Chapter 2 is trying to take an Area that is take up by another
	 * chapter.
	 * 
	 * 3. The Chapter 1 is trying to null his Area when he almost select one.
	 * 
	 * 4. The Chapter 1 is trying to change his area to a free area.
	 * 
	 * 5. The Chapter 1 is trying to change his area to a take up area.
	 * 
	 * 6. The Chapter 1 is trying to changing the Chapter 2's Area to a null.
	 * 
	 * 7. The Chapter 1 is trying to changing the Chapter 2's Area to a free area.
	 * 
	 * 8. The Chapter 1 is trying to changing the Chapter 2's Area to a take up
	 * area.
	 * 
	 * 
	 **/

	@Test
	public void driverSelectArea() {
		Area areaFree = this.chapterService.listFreeAreas().get(0);
		Area areaTakeUp = this.chapterService.listOccupiedAreas().get(0);
		Object testingData[][] = {

			{
				"Chapter2", "Chapter2", null, null
			}, {
				"Chapter2", "Chapter2", areaFree, null
			}, {
				"Chapter2", "Chapter2", areaTakeUp, IllegalArgumentException.class
			}, {
				"Chapter1", "Chapter1", null, IllegalArgumentException.class
			}, {
				"Chapter1", "Chapter1", areaFree, IllegalArgumentException.class
			}, {
				"Chapter1", "Chapter1", areaTakeUp, IllegalArgumentException.class
			}, {
				"Chapter1", "Chapter2", null, IllegalArgumentException.class
			}, {
				"Chapter1", "Chapter2", areaFree, IllegalArgumentException.class
			}, {
				"Chapter1", "Chapter2", areaTakeUp, IllegalArgumentException.class
			}
		};

		for (int i = 0; i < testingData.length; i++) {
			this.templateSelectArea((String) testingData[i][0], (String) testingData[i][1], (Area) testingData[i][2], (Class<?>) testingData[i][3]);
		}
	}

	@Test
	public void driverEditPersonalData() {
		Object testingData[][] = {

			{
				"Chapter1", "Chapter1", "name1", "surname1", "emailTest@gmail.com", "titleTest1", "https://www.example.com/", null
			}, {
				"Chapter1", "Chapter1", "", "surname1", "emailTest@gmail.com", "titleTest1", "https://www.example.com/", ConstraintViolationException.class
			}, {
				"Chapter1", "Chapter1", "name1", "", "emailTest@gmail.com", "titleTest1", "https://www.example.com/", ConstraintViolationException.class
			}, {
				"Chapter1", "Chapter1", "name1", "surname1", "", "titleTest1", "https://www.example.com/", ConstraintViolationException.class
			}, {
				"Chapter1", "Chapter1", "name1", "surname1", "invalid email", "titleTest1", "https://www.example.com/", ConstraintViolationException.class
			}, {
				"Chapter1", "Chapter1", "name1", "surname1", "emailTest@gmail.com", "", "https://www.example.com/", ConstraintViolationException.class
			}, {
				"Chapter1", "Chapter1", "name1", "surname1", "emailTest@gmail.com", "titleTest1", "invalid url", ConstraintViolationException.class
			}, {
				"Chapter2", "Chapter1", "name1", "surname1", "emailTest@gmail.com", "titleTest1", "https://www.example.com/", IllegalArgumentException.class
			}
		};

		for (int i = 0; i < testingData.length; i++) {
			this.templateEditPersonalData((String) testingData[i][0], (String) testingData[i][1], (String) testingData[i][2], (String) testingData[i][3], (String) testingData[i][4], (String) testingData[i][5], (String) testingData[i][6],
				(Class<?>) testingData[i][7]);
		}
	}

	/**
	 * This is the template method used to process the data contained inside the
	 * drivers, the forced rollback is also located in here.
	 **/

	protected void templateRegister(String username, String name, String surname, String email, String title, String photo, Area area, Class<?> expected) {

		Chapter chapter = new Chapter();
		chapter.setAddress("");
		chapter.setMiddleName("");
		chapter.setPhoneNumber("");
		chapter.setArea(area);
		chapter.setEmail(email);
		chapter.setName(name);
		chapter.setPhoto(photo);
		chapter.setSurname(surname);
		chapter.setTitle(title);

		UserAccount userAccount = new UserAccount();

		// Authorities
		List<Authority> authorities = new ArrayList<Authority>();
		Authority authority = new Authority();
		authority.setAuthority(Authority.CHAPTER);
		authorities.add(authority);
		userAccount.setAuthorities(authorities);

		// locked
		userAccount.setIsNotLocked(true);

		// Username
		userAccount.setUsername(username);

		// Password
		Md5PasswordEncoder encoder;
		encoder = new Md5PasswordEncoder();
		userAccount.setPassword(encoder.encodePassword("12345", null));

		chapter.setUserAccount(userAccount);

		Class<?> caught = null;

		/**
		 * This is the first command used to force to rollback the database, it
		 * initialise a Transaction in this point, before we add the entity in order to
		 * set the rollback to this point.
		 **/

		this.startTransaction();

		/** End of first command. **/

		try {
			this.chapterService.saveCreate(chapter);
			this.chapterService.flush();
		} catch (Throwable oops) {
			caught = oops.getClass();
		}
		super.checkExceptions(expected, caught);

		/**
		 * This is the second command, it forces the database to rollback to the last
		 * transaction point that was set, in this case before we add the new entity.
		 **/

		this.rollbackTransaction();

		/** End of second command. **/

	}

	protected void templateSelectArea(String username, String usernameEdit, Area area, Class<?> expected) {
		this.startTransaction();
		super.authenticate(username);
		Chapter editChapter = this.chapterService.getChapterByUsername(usernameEdit);

		Class<?> caught = null;

		Chapter c = new Chapter();

		c.setAddress(editChapter.getAddress());
		c.setArea(area);
		c.setBoxes(editChapter.getBoxes());
		c.setEmail(editChapter.getEmail());
		c.setHasSpam(editChapter.getHasSpam());
		c.setMiddleName(editChapter.getMiddleName());
		c.setName(editChapter.getName());
		c.setPhoneNumber(editChapter.getPhoneNumber());
		c.setPhoto(editChapter.getPhoto());
		c.setPolarity(editChapter.getPolarity());
		c.setProclaims(editChapter.getProclaims());
		c.setSocialProfiles(editChapter.getSocialProfiles());
		c.setSurname(editChapter.getSurname());
		c.setTitle(editChapter.getTitle());
		c.setUserAccount(editChapter.getUserAccount());
		c.setVersion(editChapter.getVersion());
		c.setId(editChapter.getId());

		/**
		 * This is the first command used to force to rollback the database, it
		 * initialise a Transaction in this point, before we add the entity in order to
		 * set the rollback to this point.
		 **/

		/** End of first command. **/

		try {
			this.chapterService.updateChapterArea(c);
		} catch (Throwable oops) {
			caught = oops.getClass();
		}
		super.checkExceptions(expected, caught);

		/**
		 * This is the second command, it forces the database to rollback to the last
		 * transaction point that was set, in this case before we add the new entity.
		 **/
		this.unauthenticate();
		this.rollbackTransaction();

		/** End of second command. **/

	}

	/**
	 * This test will tests the Requirment 2.2 of the level B: An actor who is
	 * authenticated as a chapter must be able to: Manage the parades that are
	 * published by the brotherhoods in the area that they co-ordinate. This
	 * includes listing them grouped by status and making decisions on the parades
	 * that have status submitted. When a parade is rejected by a chapter, the
	 * chapter must jot down the reason why
	 **/

	/**
	 * 1. This case is a positive case where a chapter accept a parade that is in
	 * his area.
	 * 
	 * 2. This caso is also a positive case where a chapter reject a parade that is
	 * in his area, writing a rejected reason.
	 * 
	 * 3. In this case the chapter is trying to change the status of a parade that
	 * is not in final mode.
	 * 
	 * 4. The chapter is trying to reject a valid parade but not writing a reject
	 * reason
	 * 
	 * 5. On this case the chapter is trying to change the status of an parade that
	 * is accepted
	 * 
	 * 6. Finally, the chapter is trying to change the status of a parade that is
	 * not in his area
	 * 
	 **/
	@Test
	public void driverChangeStatus() {

		ParadeStatus rejected = ParadeStatus.REJECTED;
		ParadeStatus accepted = ParadeStatus.ACCEPTED;

		// Chapter located in Area1
		Chapter chapter1 = this.chapterService.findOne(super.getEntityId("chapter1"));

		// Parade that is in Draft Mode
		Parade paradeDF = this.paradeService.findOne(super.getEntityId("parade4"));

		// Parade that is Submitted and is in Area1
		Parade paradeS = this.paradeService.findOne(super.getEntityId("parade2"));

		// Parade that is Accepted
		Parade paradeAccepted = this.paradeService.findOne(super.getEntityId("parade3"));

		// Parade that is not in the Chapter's Area

		Parade paradeAreaD = this.paradeService.findOne(super.getEntityId("parade6"));

		Object testingData[][] = {
			{
				chapter1, paradeS, accepted, "", null
			}, {
				chapter1, paradeS, rejected, "Yes", null
			}, {
				chapter1, paradeDF, rejected, "Yes", IllegalArgumentException.class
			}, {
				chapter1, paradeS, rejected, "", IllegalArgumentException.class
			}, {
				chapter1, paradeAccepted, rejected, "Yes", IllegalArgumentException.class
			}, {
				chapter1, paradeAreaD, rejected, "Yes", IllegalArgumentException.class
			}
		};

		for (int i = 0; i < testingData.length; i++) {
			System.out.println(i);
			this.templateChangeStatus((Chapter) testingData[i][0], (Parade) testingData[i][1], (ParadeStatus) testingData[i][2], (String) testingData[i][3], (Class<?>) testingData[i][4]);
		}
	}

	protected void templateChangeStatus(Chapter chapter, Parade parade, ParadeStatus paradeStatus, String rejectedReason, Class<?> expected) {

		Parade newParade = new Parade();

		newParade.setId(parade.getId());
		newParade.setParadeStatus(paradeStatus);
		newParade.setRejectedReason(rejectedReason);
		Class<?> caught = null;
		this.startTransaction();

		try {

			super.authenticate(chapter.getUserAccount().getUsername());
			Parade result = this.paradeService.reconstrucParadeStatus(newParade);
			this.chapterService.changeParadeStatus(result);
			this.paradeService.flush();

		} catch (Throwable oops) {
			caught = oops.getClass();
		}

		super.checkExceptions(expected, caught);

		this.rollbackTransaction();

	}

	protected void templateEditPersonalData(String username, String usernameEdit, String name, String surname, String email, String title, String photo, Class<?> expected) {
		this.startTransaction();
		super.authenticate(username);
		Chapter editChapter = this.chapterService.getChapterByUsername(usernameEdit);

		Class<?> caught = null;

		Chapter c = new Chapter();

		c.setAddress(editChapter.getAddress());
		c.setArea(editChapter.getArea());
		c.setBoxes(editChapter.getBoxes());
		c.setHasSpam(editChapter.getHasSpam());
		c.setMiddleName(editChapter.getMiddleName());
		c.setPhoneNumber(editChapter.getPhoneNumber());
		c.setPolarity(editChapter.getPolarity());
		c.setProclaims(editChapter.getProclaims());
		c.setSocialProfiles(editChapter.getSocialProfiles());
		c.setUserAccount(editChapter.getUserAccount());
		c.setVersion(editChapter.getVersion());

		c.setSurname(surname);
		c.setPhoto(photo);
		c.setTitle(title);
		c.setName(name);
		c.setEmail(email);
		c.setId(editChapter.getId());

		/**
		 * This is the first command used to force to rollback the database, it initialise a Transaction in this point, before we add the entity
		 * in order to set the rollback to this point.
		 **/

		/** End of first command. **/

		try {
			this.chapterService.update(c);
			this.chapterService.flush();
		} catch (Throwable oops) {
			caught = oops.getClass();
		}
		super.checkExceptions(expected, caught);

		/** This is the second command, it forces the database to rollback to the last transaction point that was set, in this case before we add the new entity. **/
		this.unauthenticate();
		this.rollbackTransaction();

		/** End of second command. **/

	}

	@Test
	public void testDelete() {
		this.chapterService.delete(this.chapterService.getChapterByUsername("Chapter1"));
	}
}
