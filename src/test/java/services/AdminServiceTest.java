
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

import repositories.AdminRepository;
import security.Authority;
import security.UserAccount;
import utilities.AbstractTest;
import domain.Admin;
import domain.History;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
	"classpath:spring/junit.xml"
})
@Transactional
public class AdminServiceTest extends AbstractTest {

	@Autowired
	private AdminService	adminService;

	@Autowired
	private AdminRepository	adminRepository;

	@Autowired
	private HistoryService	historyService;


	//@Test
	public void testCreate() {

		System.out.println("Estadisticas varias");
		System.out.println(this.adminService.showStatistics());
		System.out.println("");
		System.out.println("Hermandades mas pequeñas");
		System.out.println(this.adminService.smallestBrotherhoods());
		System.out.println("");
		System.out.println("Hermandades mas grandes");
		System.out.println(this.adminService.largestBrotherhoods());
		System.out.println("");
		System.out.println("Ratio de peticiones pendientes por hermandad");
		System.out.println(this.adminService.ratioRequestPendingByParade());
		System.out.println("");
		System.out.println("Ratio de peticiones aceptadas por hermandad");
		System.out.println(this.adminService.ratioRequestApprovedByParade());
		System.out.println("");
		System.out.println("Ratio de peticiones rechazadas por hermandad");
		System.out.println(this.adminService.ratioRequestRejectedByParade());
		System.out.println("");
		System.out.println("Desfiles en un mes");
		System.out.println(this.adminService.paradesOfNextMonth());
		System.out.println("");
		System.out.println("Lista de miembros con al menos un 10% de request aceptadas");
		System.out.println(this.adminService.membersAtLeastTenPercentRequestsApproved());
		System.out.println("");
		System.out.println("Conteo de Hermandades por Area");
		System.out.println(this.adminService.countBrotherhoodsArea());
		System.out.println("");
		System.out.println("Ratio de Hermandades por Area");
		System.out.println(this.adminService.ratioBrotherhoodPerArea());

	}

	//@Test
	public void testMinRecordsPerHistory() {
		Float res = this.adminRepository.minNumberRecordsPerHistory();
		Float com = Float.MAX_VALUE;
		List<History> h = this.historyService.findAll();
		for (History a : h) {
			if ((1 + a.getLegalRecords().size() + a.getLinkRecords().size() + a.getMiscellaneousRecords().size() + a.getPeriodRecords().size()) < com) {
				com = (float) (1 + a.getLegalRecords().size() + a.getLinkRecords().size() + a.getMiscellaneousRecords().size() + a.getPeriodRecords().size());
			}
		}
		Assert.isTrue(com.equals(res));
	}

	//********************************************************************************

	@Test
	public void driverRegister() {
		Object testingData[][] = {
			{
				"admin1", "AdminTest1", "name1", "surname1", "emailTest@gmail.com", "https://www.example.com/", null
			}, {
				"admin1", "", "name1", "surname1", "emailTest@gmail.com", "https://www.example.com/", ConstraintViolationException.class
			}, {
				"admin1", "AdminTest2", "", "surname2", "emailTest2@gmail.com", "https://www.example2.com/", ConstraintViolationException.class
			}, {
				"admin1", "AdminTest3", "name3", "", "emailTest3@gmail.com", "https://www.example3.com/", ConstraintViolationException.class
			}, {
				"admin1", "AdminTest4", "name1", "surname1", "emailTest@gmail.com", "invalid url", ConstraintViolationException.class
			}, {
				"admin1", "AdminTest8", "name1", "surname1", "invalid email", "https://www.example.com/", ConstraintViolationException.class
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
				"Admin1", "Admin1", "name1", "surname1", "emailTest@gmail.com", "https://www.example.com/", null
			}, {
				"Admin1", "Admin1", "", "surname1", "emailTest@gmail.com", "https://www.example.com/", ConstraintViolationException.class
			}, {
				"Admin1", "Admin1", "name1", "", "emailTest@gmail.com", "https://www.example.com/", ConstraintViolationException.class
			}, {
				"Admin1", "Admin1", "name1", "surname1", "", "https://www.example.com/", ConstraintViolationException.class
			}, {
				"Admin1", "Admin1", "name1", "surname1", "invalid email", "https://www.example.com/", ConstraintViolationException.class
			}, {
				"Admin1", "Admin1", "name1", "surname1", "emailTest@gmail.com", "invalid url", ConstraintViolationException.class
			}, {
				"Admin2", "Admin1", "name1", "surname1", "emailTest@gmail.com", "https://www.example.com/", IllegalArgumentException.class
			}
		};

		for (int i = 0; i < testingData.length; i++) {
			this.templateEditPersonalData((String) testingData[i][0], (String) testingData[i][1], (String) testingData[i][2], (String) testingData[i][3], (String) testingData[i][4], (String) testingData[i][5], (Class<?>) testingData[i][6]);
		}
	}

	protected void templateRegister(String username, String newUsername, String name, String surname, String email, String photo, Class<?> expected) {
		this.startTransaction();
		super.authenticate(username);
		Admin admin = this.adminService.createAdmin();
		admin.setAddress("");
		admin.setMiddleName("");
		admin.setPhoneNumber("");
		admin.setEmail(email);
		admin.setName(name);
		admin.setPhoto(photo);
		admin.setSurname(surname);

		UserAccount userAccount = new UserAccount();

		// Authorities
		List<Authority> authorities = new ArrayList<Authority>();
		Authority authority = new Authority();
		authority.setAuthority(Authority.ADMIN);
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

		admin.setUserAccount(userAccount);

		Class<?> caught = null;

		/**
		 * This is the first command used to force to rollback the database, it
		 * initialise a Transaction in this point, before we add the entity in order to
		 * set the rollback to this point.
		 **/

		/** End of first command. **/

		try {
			this.adminService.saveCreate(admin);
			this.adminService.flush();
		} catch (Throwable oops) {
			caught = oops.getClass();
		}
		super.checkExceptions(expected, caught);
		super.unauthenticate();

		/**
		 * This is the second command, it forces the database to rollback to the last
		 * transaction point that was set, in this case before we add the new entity.
		 **/

		this.rollbackTransaction();

		/** End of second command. **/

	}

	protected void templateEditPersonalData(String username, String usernameEdit, String name, String surname, String email, String photo, Class<?> expected) {
		this.startTransaction();
		super.authenticate(username);
		Admin editAdmin = this.adminService.getAdminByUsername(usernameEdit);

		Class<?> caught = null;

		Admin b = new Admin();

		b.setAddress(editAdmin.getAddress());
		b.setBoxes(editAdmin.getBoxes());
		b.setHasSpam(editAdmin.getHasSpam());
		b.setMiddleName(editAdmin.getMiddleName());
		b.setPhoneNumber(editAdmin.getPhoneNumber());
		b.setPolarity(editAdmin.getPolarity());
		b.setSocialProfiles(editAdmin.getSocialProfiles());
		b.setUserAccount(editAdmin.getUserAccount());
		b.setVersion(editAdmin.getVersion());

		b.setSurname(surname);
		b.setPhoto(photo);
		b.setName(name);
		b.setEmail(email);
		b.setId(editAdmin.getId());

		/**
		 * This is the first command used to force to rollback the database, it initialise a Transaction in this point, before we add the entity
		 * in order to set the rollback to this point.
		 **/

		/** End of first command. **/

		try {
			this.adminService.updateAdmin(b);
			this.adminService.flush();
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
