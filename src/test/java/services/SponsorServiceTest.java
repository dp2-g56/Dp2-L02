
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
import org.springframework.util.Assert;

import security.Authority;
import security.UserAccount;
import utilities.AbstractTest;
import domain.Sponsor;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
	"classpath:spring/junit.xml"
})
@Transactional
public class SponsorServiceTest extends AbstractTest {

	@Autowired
	private SponsorService	sponsorService;


	@Test
	public void driverRegister() {

		Object testingData[][] = {
			{
				//Positive test
				"SponsorTest0", "password1", "password1", "name1", "surname1", "emailTest@gmail.com", "https://www.example.com/", true, null
			}, {
				//Blank Username
				"", "password1", "password1", "name1", "surname1", "emailTest@gmail.com", "https://www.example.com/", true, ConstraintViolationException.class
			}, {
				//Password confirmation fail
				"SponsorTest1", "password", "password1", "name1", "surname1", "emailTest1@gmail.com", "https://www.example.com/", true, IllegalArgumentException.class
			}, {
				//Blank name
				"SponsorTest2", "password2", "password2", "", "surname2", "emailTest2@gmail.com", "https://www.example2.com/", true, ConstraintViolationException.class
			}, {
				//Blank Surname
				"SponsorTest3", "password3", "password3", "name3", "", "emailTest3@gmail.com", "https://www.example3.com/", true, ConstraintViolationException.class
			}, {
				//Invalid photo URL
				"SponsorTest4", "password1", "password1", "name1", "surname1", "emailTest@gmail.com", "invalid url", true, ConstraintViolationException.class
			}, {
				//Non accepted terms of use
				"SponsorTest5", "password1", "password1", "name1", "surname1", "emailTest@gmail.com", "https://www.example.com/", false, IllegalArgumentException.class
			}, {
				//Null email
				"SponsorTest6", "password1", "password1", "name1", "surname1", null, "https://www.example.com/", true, ConstraintViolationException.class
			}, {
				//Blank email
				"SponsorTest7", "password1", "password1", "name1", "surname1", "", "https://www.example.com/", true, ConstraintViolationException.class
			}, {
				//Null name
				"SponsorTest8", "password1", "password1", null, "surname1", "emailTest@gmail.com", "https://www.example.com/", true, ConstraintViolationException.class
			}, {
				//Null surname
				"SponsorTest9", "password1", "password1", "name1", null, "invalid email", "https://www.example.com/", true, ConstraintViolationException.class
			}, {
				//Invalid email
				"SponsorTest10", "password1", "password1", "name1", "surname10", "invalid email", "https://www.example.com/", true, ConstraintViolationException.class
			}
		};

		for (int i = 0; i < testingData.length; i++)
			this.templateRegister((String) testingData[i][0], (String) testingData[i][1], (String) testingData[i][2], (String) testingData[i][3], (String) testingData[i][4], (String) testingData[i][5], (String) testingData[i][6],
				(Boolean) testingData[i][7], (Class<?>) testingData[i][8]);
	}

	protected void templateRegister(String username, String password, String confirmPassword, String name, String surname, String email, String photo, Boolean termsAndConditions, Class<?> expected) {
		Class<?> caught = null;

		try {
			//En cada iteraccion comenzamos una transaccion, de esya manera, no se toman valores residuales de otros test
			this.startTransaction();

			Sponsor sponsor = new Sponsor();
			sponsor.setAddress("");
			sponsor.setMiddleName("");
			sponsor.setPhoneNumber("");

			sponsor.setEmail(email);
			sponsor.setName(name);
			sponsor.setPhoto(photo);
			sponsor.setSurname(surname);

			UserAccount userAccount = new UserAccount();

			//Authorities
			List<Authority> authorities = new ArrayList<Authority>();
			Authority authority = new Authority();
			authority.setAuthority(Authority.SPONSOR);
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

			sponsor.setUserAccount(userAccount);

			Assert.isTrue(termsAndConditions && password == confirmPassword);
			this.sponsorService.saveCreate(sponsor);
			this.sponsorService.flush();

		} catch (Throwable oops) {
			caught = oops.getClass();
		} finally {
			//Se fuerza el rollback para que no de ningun problema la siguiente iteracion
			this.rollbackTransaction();
		}
		super.checkExceptions(expected, caught);
	}
}
