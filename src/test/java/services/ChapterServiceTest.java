
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

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
	"classpath:spring/datasource.xml", "classpath:spring/config/packages.xml"
})
@Transactional
public class ChapterServiceTest extends AbstractTest {

	@Autowired
	private ChapterService	chapterService;


	//Driver for the method saveCreate from ChapterService. In this driver eight eight cases are tested and are, in order:

	//1. A positive case where none of the parameters are wrong and no error is expected, which is indicated by "null" at the last parameter.

	//2. A case where the username is blank and an ConstraintViolationException is expected.

	//3. A case where the name is blank and an ConstraintViolationException is expected.

	//4. A case where the surname is blank and an ConstraintViolationException is expected.

	//5. A case where the URL is invalid and not blank, an ConstraintViolationException is expected.

	//6. A case where the title is  blank and an ConstraintViolationException is expected.

	//7. A case where the assigned area is already assigned which is restricted by the requirement number 5 of Acme Parade.
	//   	This restriction is checked by an Assert and thus an IllegalArgumentException is expected.

	//8. A case where the email is invalid (it didn't follow the pattern) and a ConstraintViolationException is expected.

	// Note that the restrictions tested in cases 3, 4, 5 and 8 are from Actor; the restrictions tested in cases 6 and 7 are from Chapter;
	// 	and the restriction tested in case 2 is from UserAccount.

	// Note as well the name of this driver, we encountered problem with the current driver and template methods for testing regarding adding new entities
	// 	to the database multiple times on the same driver. This error occurs due to the database not rolling back its tested content until the driver has been
	// 	fully executed.

	// This causes that the first added entity with an error remains on database when it tries to add the second. Due to the database keeping the tested
	// 	entity with an id of 0 (because is new and being a test, its id is not changed) it will  throw a Duplicate Key error instead of the expected one for
	// 	every case after the first.

	// We managed to resolve this by forcing the database  to rollback to a previous state without the new entity after every iteration
	// 	of the template method. This will be explained later as there are 2 more drivers that shows an alternative workaround and the effects of
	//	not using either of this solutions.

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

	// This second driver is the alternative to the forced rollback applied on the template. It consist of getting an exception before adding the new
	//	entity to the database through Asserts in the tested method.

	// By getting this exceptions we will only add one entity before the driver is fully executed
	//	the one on the positive case without exceptions. For every other iteration, an IllegalArgumentException will be expected as an Assert will be triggered
	//	instead of the ConstraintViolationException for the tags in the Domain.

	//	This driver won't give errors only if the Asserts commented in the tested methods become uncommented.

	@Test
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

	// This last driver shows the effect described in the commented section above the first driver. This effect takes place if no rollback is force
	//	and no additional Asserts are uncommented from the tested method. As you can see it is expected DataIntegrityViolationException after the second 
	// 	test for every consequent test because of the Duplicate Key from id = 0.

	// The downside effect is the inability to recognise if the tests throw the expected result beyond this test error,
	//	making this driver model unable to provide the information in this cases.

	@Test
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

	//This is the template method used to process the data contained inside the drivers, the forced rollback is also located in here.

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

		//Authorities
		List<Authority> authorities = new ArrayList<Authority>();
		Authority authority = new Authority();
		authority.setAuthority(Authority.CHAPTER);
		authorities.add(authority);
		userAccount.setAuthorities(authorities);

		//locked
		userAccount.setIsNotLocked(true);

		//Username
		userAccount.setUsername(username);

		//Password
		Md5PasswordEncoder encoder;
		encoder = new Md5PasswordEncoder();
		userAccount.setPassword(encoder.encodePassword("12345", null));

		chapter.setUserAccount(userAccount);

		Class<?> caught = null;

		// This is the first command used to force to rollback the database, it initialise a Transaction in this point, before we add the entity
		//	in order to set the rollback to this point.

		this.startTransaction();

		//End of first command.

		try {
			this.chapterService.saveCreate(chapter);
			this.chapterService.flush();
		} catch (Throwable oops) {
			caught = oops.getClass();
		}
		super.checkExceptions(expected, caught);

		//This is the second command, it forces the database to rollback to the last transaction point that was set, in this case before we add the new entity.
		this.rollbackTransaction();

		//End of second command.

	}
}
