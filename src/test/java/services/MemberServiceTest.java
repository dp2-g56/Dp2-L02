
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
import domain.Member;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
	"classpath:spring/junit.xml"
})
@Transactional
public class MemberServiceTest extends AbstractTest {

	@Autowired
	private MemberService	memberService;


	/**
	 * This next driver will test 6 cases about registering as a Member, the data in this drivers will only be those which have restrictions
	 * about its persistence on database or business rules. This tested cases are:
	 * 
	 * 1. A positive case where no error is expected.
	 * 
	 * 2. A case where the new username is blank, a ConstraintViolationException is expected.
	 * 
	 * 3. A case where the new name is blank, a ConstraintViolationException is expected.
	 * 
	 * 4. A case where the new surname is blank, a ConstraintViolationException is expected.
	 * 
	 * 5. A case where the photo posted is not blank but neither a valid URL, a ConstraintViolationException is expected.
	 * 
	 * 6. A case where the email posted is not valid, a ConstraintViolationException is expected.
	 **/

	@Test
	public void driverRegister() {
		Object testingData[][] = {
			{
				"MemberTest1", "name1", "surname1", "emailTest@gmail.com", "https://www.example.com/", null
			}, {
				"", "name1", "surname1", "emailTest@gmail.com", "https://www.example.com/", ConstraintViolationException.class
			}, {
				"MemberTest2", "", "surname2", "emailTest2@gmail.com", "https://www.example2.com/", ConstraintViolationException.class
			}, {
				"MemberTest3", "name3", "", "emailTest3@gmail.com", "https://www.example3.com/", ConstraintViolationException.class
			}, {
				"MemberTest4", "name1", "surname1", "emailTest@gmail.com", "invalid url", ConstraintViolationException.class
			}, {
				"MemberTest8", "name1", "surname1", "invalid email", "https://www.example.com/", ConstraintViolationException.class
			}
		};

		for (int i = 0; i < testingData.length; i++) {
			this.templateRegister((String) testingData[i][0], (String) testingData[i][1], (String) testingData[i][2], (String) testingData[i][3], (String) testingData[i][4], (Class<?>) testingData[i][5]);
		}
	}

	/**
	 * This next driver will test 7 cases about editing personal data as a Member, the data in this drivers will only be those which have restrictions
	 * about its persistence on database or business rules. This tested cases are:
	 * 
	 * 1. A positive case where no error is expected.
	 * 
	 * 2. A case where the new name is blank, a ConstraintViolationException is expected.
	 * 
	 * 3. A case where the new surname is blank, a ConstraintViolationException is expected.
	 * 
	 * 4. A case where the new email is blank, a ConstraintViolationException is expected.
	 * 
	 * 5. A case where the email posted is not valid, a ConstraintViolationException is expected.
	 * 
	 * 6. A case where the photo posted is not blank but neither a valid URL, a ConstraintViolationException is expected.
	 * 
	 * 7. A case when an Actor tries to edit another actor personal data, an IllegalArgumentException.
	 **/

	@Test
	public void driverEditPersonalData() {
		Object testingData[][] = {

			{
				"Member1", "Member1", "name1", "surname1", "emailTest@gmail.com", "https://www.example.com/", null
			}, {
				"Member1", "Member1", "", "surname1", "emailTest@gmail.com", "https://www.example.com/", ConstraintViolationException.class
			}, {
				"Member1", "Member1", "name1", "", "emailTest@gmail.com", "https://www.example.com/", ConstraintViolationException.class
			}, {
				"Member1", "Member1", "name1", "surname1", "", "https://www.example.com/", ConstraintViolationException.class
			}, {
				"Member1", "Member1", "name1", "surname1", "invalid email", "https://www.example.com/", ConstraintViolationException.class
			}, {
				"Member1", "Member1", "name1", "surname1", "emailTest@gmail.com", "invalid url", ConstraintViolationException.class
			}, {
				"Member2", "Member1", "name1", "surname1", "emailTest@gmail.com", "https://www.example.com/", IllegalArgumentException.class
			}
		};

		for (int i = 0; i < testingData.length; i++) {
			this.templateEditPersonalData((String) testingData[i][0], (String) testingData[i][1], (String) testingData[i][2], (String) testingData[i][3], (String) testingData[i][4], (String) testingData[i][5], (Class<?>) testingData[i][6]);
		}
	}

	protected void templateRegister(String newUsername, String name, String surname, String email, String photo, Class<?> expected) {
		/**
		 * This is the first command used to force to rollback the database, it
		 * initialise a Transaction in this point, before we add the entity in order to
		 * set the rollback to this point.
		 **/
		this.startTransaction();
		/** End of first command. **/

		Member member = this.memberService.createMember();
		member.setAddress("");
		member.setMiddleName("");
		member.setPhoneNumber("");
		member.setEmail(email);
		member.setName(name);
		member.setPhoto(photo);
		member.setSurname(surname);

		UserAccount userAccount = new UserAccount();

		// Authorities
		List<Authority> authorities = new ArrayList<Authority>();
		Authority authority = new Authority();
		authority.setAuthority(Authority.MEMBER);
		authorities.add(authority);
		userAccount.setAuthorities(authorities);

		// locked
		userAccount.setIsNotLocked(true);

		// Username
		userAccount.setUsername(newUsername);

		// Password
		Md5PasswordEncoder encoder;
		encoder = new Md5PasswordEncoder();
		userAccount.setPassword(encoder.encodePassword("12345", null));

		member.setUserAccount(userAccount);

		Class<?> caught = null;

		try {
			this.memberService.saveCreate(member);
			/** It is necessary to call the flush method in order to force the database to transact the operation **/
			this.memberService.flush();
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

	protected void templateEditPersonalData(String username, String usernameEdit, String name, String surname, String email, String photo, Class<?> expected) {
		/**
		 * This is the first command used to force to rollback the database, it initialise a Transaction in this point, before we add the entity
		 * in order to set the rollback to this point.
		 **/

		this.startTransaction();

		/** End of first command. **/

		/**
		 * In this template we will use the first parameter as username to authenticate
		 * in order to test the case when one actor tries to edit the personal data of another.
		 **/

		super.authenticate(username);

		/**
		 * The second parameter will be use as username to get the member that will be edited
		 * , there is a query in order to get this member.
		 **/

		Member editMember = this.memberService.getMemberByUsername(usernameEdit);

		Class<?> caught = null;

		Member b = new Member();

		b.setAddress(editMember.getAddress());
		b.setBoxes(editMember.getBoxes());
		b.setHasSpam(editMember.getHasSpam());
		b.setMiddleName(editMember.getMiddleName());
		b.setPhoneNumber(editMember.getPhoneNumber());
		b.setPolarity(editMember.getPolarity());
		b.setSocialProfiles(editMember.getSocialProfiles());
		b.setUserAccount(editMember.getUserAccount());
		b.setVersion(editMember.getVersion());

		b.setSurname(surname);
		b.setPhoto(photo);
		b.setName(name);
		b.setEmail(email);
		b.setId(editMember.getId());

		try {
			this.memberService.updateMember(b);
			/** It is necessary to call the flush method in order to force the database to transact the operation **/
			this.memberService.flush();
		} catch (Throwable oops) {
			caught = oops.getClass();
		}
		super.checkExceptions(expected, caught);
		this.unauthenticate();
		/** This is the second command, it forces the database to rollback to the last transaction point that was set, in this case before we add the new entity. **/

		this.rollbackTransaction();

		/** End of second command. **/

	}

}
