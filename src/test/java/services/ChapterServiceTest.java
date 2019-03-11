
package services;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.encoding.Md5PasswordEncoder;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

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


	@Test
	public void testCreatePositive() {
		Chapter chapter = new Chapter();
		Area area = this.chapterService.listFreeAreas().get(0);

		chapter = this.chapterService.createChapter();

		chapter.setTitle("ddd");
		chapter.setName("aaa");
		chapter.setSurname("aaa");

		chapter.setMiddleName("");

		chapter.setPolarity(0);
		chapter.setHasSpam(false);
		chapter.setPhoto("");
		chapter.setPhoneNumber("");
		chapter.setAddress("");
		chapter.setArea(area);
		chapter.setEmail("ejemplo@gmail.com");
		chapter.getUserAccount().setUsername("kfjsnk");
		chapter.getUserAccount().setPassword("12345");

		Chapter saved = new Chapter();
		saved = this.chapterService.saveCreate(chapter);
		this.chapterService.flush();
		Assert.isTrue(this.chapterService.findAll().contains(saved));

	}

	@Test
	public void driverRegisterNegative() {
		Area areaTest = this.chapterService.listFreeAreas().get(0);
		Area areaTest2 = this.chapterService.listOccupiedAreas().get(0);
		Object testingData[][] = {
			{
				"", "password1", "password1", "name1", "surname1", "emailTest@gmail.com", "titleTest1", "https://www.example.com/", true, null, IllegalArgumentException.class
			}, {
				"ChapterTest1", "password", "password1", "name1", "surname1", "emailTest1@gmail.com", "titleTest1", "https://www.example.com/", true, null, IllegalArgumentException.class
			}, {
				"ChapterTest2", "password2", "password2", "", "surname2", "emailTest2@gmail.com", "titleTest2", "https://www.example2.com/", true, null, IllegalArgumentException.class
			}, {
				"ChapterTest3", "password3", "password3", "name3", "", "emailTest3@gmail.com", "titleTest3", "https://www.example3.com/", true, null, IllegalArgumentException.class
			}, {
				"ChapterTest4", "password1", "password1", "name1", "surname1", "emailTest@gmail.com", "titleTest1", "invalid url", true, areaTest, IllegalArgumentException.class
			}, {
				"ChapterTest5", "password1", "password1", "name1", "surname1", "emailTest@gmail.com", "titleTest1", "https://www.example.com/", false, areaTest, IllegalArgumentException.class
			}, {
				"ChapterTest6", "password1", "password1", "name1", "surname1", "emailTest@gmail.com", "", "https://www.example.com/", true, null, IllegalArgumentException.class
			}, {
				"ChapterTest7", "password1", "password1", "name1", "surname1", "emailTest@gmail.com", "titleTest1", "https://www.example.com/", true, areaTest2, IllegalArgumentException.class
			}, {
				"ChapterTest8", "password1", "password1", "name1", "surname1", "invalid email", "titleTest1", "https://www.example.com/", true, null, IllegalArgumentException.class
			}
		};

		for (int i = 0; i < testingData.length; i++) {
			this.templateRegister((String) testingData[i][0], (String) testingData[i][1], (String) testingData[i][2], (String) testingData[i][3], (String) testingData[i][4], (String) testingData[i][5], (String) testingData[i][6],
				(String) testingData[i][7], (Boolean) testingData[i][8], (Area) testingData[i][9], (Class<?>) testingData[i][10]);
		}
	}

	protected void templateRegister(String username, String password, String confirmPassword, String name, String surname, String email, String title, String photo, Boolean termsAndConditions, Area area, Class<?> expected) {
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
		userAccount.setPassword(encoder.encodePassword(password, null));

		chapter.setUserAccount(userAccount);

		Class<?> caught = null;

		try {
			this.chapterService.saveCreate(chapter);
			Assert.isTrue(termsAndConditions && password == confirmPassword);
			this.chapterService.flush();
		} catch (Throwable oops) {
			caught = oops.getClass();
		}
		super.checkExceptions(expected, caught);
	}
}
