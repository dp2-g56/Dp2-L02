
package services;

import java.util.ArrayList;
import java.util.List;

import javax.validation.ConstraintViolationException;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.encoding.Md5PasswordEncoder;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import security.Authority;
import security.UserAccount;
import utilities.AbstractTest;
import domain.Brotherhood;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
	"classpath:spring/junit.xml"
})
@Transactional
public class BrotherhoodServiceTest extends AbstractTest {

	@Autowired
	private BrotherhoodService	brotherhoodService;


	@Test
	public void driverRegister() {
		Object testingData[][] = {
			{
				"BrotherhoodTest1", "name1", "surname1", "emailTest@gmail.com", "titleTest1", "https://www.example.com/", null
			}, {
				"", "name1", "surname1", "emailTest@gmail.com", "titleTest1", "https://www.example.com/", ConstraintViolationException.class
			}, {
				"BrotherhoodTest2", "", "surname2", "emailTest2@gmail.com", "titleTest2", "https://www.example2.com/", ConstraintViolationException.class
			}, {
				"BrotherhoodTest3", "name3", "", "emailTest3@gmail.com", "titleTest3", "https://www.example3.com/", ConstraintViolationException.class
			}, {
				"BrotherhoodTest4", "name1", "surname1", "emailTest@gmail.com", "titleTest1", "invalid url", ConstraintViolationException.class
			}, {
				"BrotherhoodTest6", "name1", "surname1", "emailTest@gmail.com", "", "https://www.example.com/", ConstraintViolationException.class
			}, {
				"BrotherhoodTest8", "name1", "surname1", "invalid email", "titleTest1", "https://www.example.com/", ConstraintViolationException.class
			}
		};

		for (int i = 0; i < testingData.length; i++) {
			this.templateRegister((String) testingData[i][0], (String) testingData[i][1], (String) testingData[i][2], (String) testingData[i][3], (String) testingData[i][4], (String) testingData[i][5], (Class<?>) testingData[i][6]);
		}
	}

	@Test
	public void driverEditPersonalData() {
		Object testingData[][] = {

			{
				"Brotherhood1", "Brotherhood1", "name1", "surname1", "emailTest@gmail.com", "titleTest1", "https://www.example.com/", null
			}, {
				"Brotherhood1", "Brotherhood1", "", "surname1", "emailTest@gmail.com", "titleTest1", "https://www.example.com/", ConstraintViolationException.class
			}, {
				"Brotherhood1", "Brotherhood1", "name1", "", "emailTest@gmail.com", "titleTest1", "https://www.example.com/", ConstraintViolationException.class
			}, {
				"Brotherhood1", "Brotherhood1", "name1", "surname1", "", "titleTest1", "https://www.example.com/", ConstraintViolationException.class
			}, {
				"Brotherhood1", "Brotherhood1", "name1", "surname1", "invalid email", "titleTest1", "https://www.example.com/", ConstraintViolationException.class
			}, {
				"Brotherhood1", "Brotherhood1", "name1", "surname1", "emailTest@gmail.com", "", "https://www.example.com/", ConstraintViolationException.class
			}, {
				"Brotherhood1", "Brotherhood1", "name1", "surname1", "emailTest@gmail.com", "titleTest1", "invalid url", ConstraintViolationException.class
			}, {
				"Brotherhood2", "Brotherhood1", "name1", "surname1", "emailTest@gmail.com", "titleTest1", "https://www.example.com/", IllegalArgumentException.class
			}
		};

		for (int i = 0; i < testingData.length; i++) {
			this.templateEditPersonalData((String) testingData[i][0], (String) testingData[i][1], (String) testingData[i][2], (String) testingData[i][3], (String) testingData[i][4], (String) testingData[i][5], (String) testingData[i][6],
				(Class<?>) testingData[i][7]);
		}
	}

	protected void templateRegister(String username, String name, String surname, String email, String title, String photo, Class<?> expected) {

		Brotherhood brotherhood = this.brotherhoodService.createBrotherhood();
		brotherhood.setAddress("");
		brotherhood.setMiddleName("");
		brotherhood.setPhoneNumber("");
		brotherhood.setEmail(email);
		brotherhood.setName(name);
		brotherhood.setPhoto(photo);
		brotherhood.setSurname(surname);
		brotherhood.setTitle(title);

		UserAccount userAccount = new UserAccount();

		// Authorities
		List<Authority> authorities = new ArrayList<Authority>();
		Authority authority = new Authority();
		authority.setAuthority(Authority.BROTHERHOOD);
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

		brotherhood.setUserAccount(userAccount);

		Class<?> caught = null;

		/**
		 * This is the first command used to force to rollback the database, it
		 * initialise a Transaction in this point, before we add the entity in order to
		 * set the rollback to this point.
		 **/

		this.startTransaction();

		/** End of first command. **/

		try {
			this.brotherhoodService.saveCreate(brotherhood);
			this.brotherhoodService.flush();
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

	protected void templateEditPersonalData(String username, String usernameEdit, String name, String surname, String email, String title, String photo, Class<?> expected) {
		this.startTransaction();
		super.authenticate(username);
		Brotherhood editBrotherhood = this.brotherhoodService.getBrotherhoodByUsername(usernameEdit);

		Class<?> caught = null;

		Brotherhood b = new Brotherhood();

		b.setAddress(editBrotherhood.getAddress());
		b.setArea(editBrotherhood.getArea());
		b.setBoxes(editBrotherhood.getBoxes());
		b.setHasSpam(editBrotherhood.getHasSpam());
		b.setMiddleName(editBrotherhood.getMiddleName());
		b.setPhoneNumber(editBrotherhood.getPhoneNumber());
		b.setPolarity(editBrotherhood.getPolarity());
		b.setSocialProfiles(editBrotherhood.getSocialProfiles());
		b.setEnrolments(editBrotherhood.getEnrolments());
		b.setEstablishmentDate(editBrotherhood.getEstablishmentDate());
		b.setFloats(editBrotherhood.getFloats());
		b.setHistory(editBrotherhood.getHistory());
		b.setParades(editBrotherhood.getParades());
		b.setPictures(editBrotherhood.getPictures());
		b.setUserAccount(editBrotherhood.getUserAccount());
		b.setVersion(editBrotherhood.getVersion());

		b.setSurname(surname);
		b.setPhoto(photo);
		b.setTitle(title);
		b.setName(name);
		b.setEmail(email);
		b.setId(editBrotherhood.getId());

		/**
		 * This is the first command used to force to rollback the database, it initialise a Transaction in this point, before we add the entity
		 * in order to set the rollback to this point.
		 **/

		/** End of first command. **/

		try {
			this.brotherhoodService.updateBrotherhood(b);
			this.brotherhoodService.flush();
		} catch (Throwable oops) {
			caught = oops.getClass();
		}
		super.checkExceptions(expected, caught);

		/** This is the second command, it forces the database to rollback to the last transaction point that was set, in this case before we add the new entity. **/
		this.unauthenticate();
		this.rollbackTransaction();

		/** End of second command. **/

	}

}
