
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

import domain.Admin;
import domain.Brotherhood;
import domain.Chapter;
import domain.History;
import domain.LegalRecord;
import domain.Sponsor;
import repositories.AdminRepository;
import security.Authority;
import security.UserAccount;
import utilities.AbstractTest;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:spring/junit.xml" })
@Transactional
public class AdminServiceTest extends AbstractTest {

	@Autowired
	private AdminService adminService;

	@Autowired
	private AdminRepository adminRepository;

	@Autowired
	private HistoryService historyService;

	@Autowired
	private BrotherhoodService brotherhoodService;

	@Autowired
	private ChapterService chapterService;

	@Autowired
	private AreaService areaService;

	@Autowired
	private ParadeService paradeService;

	@Autowired
	private FloatService floatService;

	// @Test
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

	// ********************************************************************************

	/**
	 * This next driver will test 7 cases about registering as a Administrator, the data in this drivers will only be those which have restrictions
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
	 * 
	 * 7. A case where the actor trying to register a new Administrator account is not an Administrator, an IllegalArgumentException is expected.
	 **/
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
			}, {
				"chapter1", "AdminTest8", "name1", "surname1", "emailTest@gmail.com", "https://www.example.com/", IllegalArgumentException.class
			}
		};

		for (int i = 0; i < testingData.length; i++) {
			this.templateRegister((String) testingData[i][0], (String) testingData[i][1], (String) testingData[i][2], (String) testingData[i][3], (String) testingData[i][4], (String) testingData[i][5], (Class<?>) testingData[i][6]);
		}
	}

	/**
	 * This next driver will test 7 cases about editing personal data as a Administrator, the data in this drivers will only be those which have restrictions
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

				{ "Admin1", "Admin1", "name1", "surname1", "emailTest@gmail.com", "https://www.example.com/", null },
				{ "Admin1", "Admin1", "", "surname1", "emailTest@gmail.com", "https://www.example.com/",
						ConstraintViolationException.class },
				{ "Admin1", "Admin1", "name1", "", "emailTest@gmail.com", "https://www.example.com/",
						ConstraintViolationException.class },
				{ "Admin1", "Admin1", "name1", "surname1", "", "https://www.example.com/",
						ConstraintViolationException.class },
				{ "Admin1", "Admin1", "name1", "surname1", "invalid email", "https://www.example.com/",
						ConstraintViolationException.class },
				{ "Admin1", "Admin1", "name1", "surname1", "emailTest@gmail.com", "invalid url",
						ConstraintViolationException.class },
				{ "Admin2", "Admin1", "name1", "surname1", "emailTest@gmail.com", "https://www.example.com/",
						IllegalArgumentException.class } };

		for (int i = 0; i < testingData.length; i++)
			this.templateEditPersonalData((String) testingData[i][0], (String) testingData[i][1],
					(String) testingData[i][2], (String) testingData[i][3], (String) testingData[i][4],
					(String) testingData[i][5], (Class<?>) testingData[i][6]);
	}
	protected void templateRegister(String username, String newUsername, String name, String surname, String email, String photo, Class<?> expected) {
		/**
		 * This is the first command used to force to rollback the database, it
		 * initialise a Transaction in this point, before we add the entity in order to
		 * set the rollback to this point.
		 **/
		this.startTransaction();

		/** End of first command. **/
		super.authenticate(username);
		Class<?> caught = null;

		try {

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

	protected void templateEditPersonalData(String username, String usernameEdit, String name, String surname,
			String email, String photo, Class<?> expected) {
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
		 * This is the first command used to force to rollback the database, it
		 * initialise a Transaction in this point, before we add the entity in order to
		 * set the rollback to this point.
		 **/

		/** End of first command. **/

		try {
			this.adminService.updateAdmin(b);
			this.adminService.flush();
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

	@Test
	public void testMaxNumberRecordsPerHistory() {
		List<History> lh = this.historyService.findAll();
		// Delete a record
		History h = lh.get(0);
		h.getLegalRecords().remove(0);
		// Create a new record
		LegalRecord l = new LegalRecord();
		l.setTitle("title");
		l.setDescription("description");
		l.setLegalName("legalName");
		l.setVatNumber("ES85274196A");
		// Add the new record to the respective list of records
		h.getLegalRecords().add(l);
		// Obtain the query result
		Float query = this.adminRepository.maxNumberRecordsPerHistory();
		// Compare the query result with the expected result
		Assert.isTrue(query == 5);
	}

	@Test
	public void testMinNumberRecordsPerHistory() {
		List<History> lh = this.historyService.findAll();
		History h = lh.get(2);
		// Create a new record
		LegalRecord l = new LegalRecord();
		l.setTitle("title");
		l.setDescription("description");
		l.setLegalName("legalName");
		l.setVatNumber("ES85274196A");
		// Add the new record to the respective list of records
		h.getLegalRecords().add(l);
		// Delete the record
		h.getLegalRecords().remove(0);
		// Obtain the query result
		Float query = this.adminRepository.minNumberRecordsPerHistory();
		// Compare the query result with the expected result
		Assert.isTrue(query == 1);
	}

	@Test
	public void testAvgNumberRecordsPerHistory() {
		List<History> lh = this.historyService.findAll();
		// Delete a record
		History h = lh.get(0);
		h.getLegalRecords().remove(0);
		// Create a new record
		LegalRecord l = new LegalRecord();
		l.setTitle("title");
		l.setDescription("description");
		l.setLegalName("legalName");
		l.setVatNumber("ES85274196A");
		// Add the new record to the respective list of records
		h.getLegalRecords().add(l);
		// Obtain the query result
		Float query = this.adminRepository.avgRecordsPerHistory();
		// Compare the query result with the expected result
		Assert.isTrue(query > 3);
	}

	@Test
	public void testStddevNumberRecordsPerHistory() {
		List<History> lh = this.historyService.findAll();
		// Delete a record
		History h = lh.get(0);
		h.getLegalRecords().remove(0);
		// Create a new record
		LegalRecord l = new LegalRecord();
		l.setTitle("title");
		l.setDescription("description");
		l.setLegalName("legalName");
		l.setVatNumber("ES85274196A");
		// Add the new record to the respective list of records
		h.getLegalRecords().add(l);
		// Obtain the query result
		Float query = this.adminRepository.stddevRecordsPerHistory();
		// Compare the query result with the expected result
		Assert.isTrue(query > 1);
	}

	@Test
	public void testBroLargestHistory() {
		// Obtain all the brotherhoods
		List<Brotherhood> lb = this.brotherhoodService.findAll();
		// Obtain the brotherhood we expect as a result
		Brotherhood b = lb.get(0);
		List<Brotherhood> query = this.adminRepository.broLargestHistory();
		// Compare
		Assert.isTrue(query.contains(b));
		Assert.isTrue(query.size() == 1);
	}

	@Test
	public void testBroHistoryLargerThanAvg() {
		List<Brotherhood> query = this.adminRepository.broHistoryLargerThanAvg();
		// Compare
		Assert.isTrue(query.size() == 2);
	}

	@Test
	public void testRatioAreasNotCoordinated() {
		Float query = this.adminRepository.ratioAreasNotCoordinated();
		Assert.isTrue(query == 66);
	}

	@Test
	public void testMaxParadesCoordinated() {
		Float query = this.adminService.maxParadesCoordinated();
		// Compare the query result with the expected result
		Assert.isTrue(query == 3);
	}

	@Test
	public void testMinParadesCoordinated() {
		Float query = this.adminRepository.minParadesCoordinated();
		// Compare the query result with the expected result
		Assert.isTrue(query == 2);
	}

	@Test
	public void testAvgParadesCoordinated() {
		Float query = this.adminRepository.avgParadesCoordinated();
		// Compare the query result with the expected result
		Assert.isTrue(query == 2.5);
	}

	@Test
	public void testStddevParadesCoordinated() {
		Float query = this.adminRepository.stddevParadesCoordinated();
		// Compare the query result with the expected result
		Assert.isTrue(query == 0.5);
	}

	@Test
	public void testChaptersThatCoordinateAtLeast() {
		List<Chapter> query = this.adminRepository.chaptersThatCoordinateAtLeast();
		Assert.isTrue(query.size() == 1);
	}

	@Test
	public void testParadesDraftVSFinal() {
		Float query = this.adminRepository.paradesDraftVSFinal();
		// Compare the query result with the expected result
		Assert.isTrue(query == 0.5);
	}

	@Test
	public void testRatioParadesAcceptedRequests() {
		Float query = this.adminRepository.ratioParadesAcceptedRequests();
		// Compare the query result with the expected result
		Assert.isTrue(query == 20.0);
	}

	@Test
	public void testRatioParadesSubmittedRequests() {
		Float query = this.adminRepository.ratioParadesSubmittedRequests();
		// Compare the query result with the expected result
		Assert.isTrue(query == 60.0);
	}

	@Test
	public void testRatioParadesRejectedRequests() {
		Float query = this.adminRepository.ratioParadesRejectedRequests();
		// Compare the query result with the expected result
		Assert.isTrue(query == 20.0);
	}

	@Test
	public void testRatioActiveSponsorships() {
		Float query = this.adminRepository.ratioActiveSponsorships();
		// Compare the query result with the expected result
		Assert.isTrue(query == 100.0);
	}

	@Test
	public void testMaxSponsorshipsPerSponsor() {
		Float query = this.adminRepository.maxSponsorshipsPerSponsor();
		// Compare the query result with the expected result
		Assert.isTrue(query == 2.0);
	}

	@Test
	public void testMinSponsorshipsPerSponsor() {
		Float query = this.adminRepository.minSponsorshipsPerSponsor();
		// Compare the query result with the expected result
		Assert.isTrue(query == 2.0);
	}

	@Test
	public void testAvgSponsorshipsPerSponsor() {
		Float query = this.adminRepository.avgSponsorshipsPerSponsor();
		// Compare the query result with the expected result
		Assert.isTrue(query == 2.0);
	}

	@Test
	public void testStddevSponsorshipsPerSponsor() {
		Float query = this.adminRepository.stddevSponsorshipsPerSponsor();
		// Compare the query result with the expected result
		Assert.isTrue(query == 0.0);
	}

	@Test
	public void testTop5SponsorNumberActiveSponsorships() {
		List<Sponsor> query = this.adminRepository.top5SponsorNumberActiveSponsorships();
		// Compare the query result with the expected result
		Assert.isTrue(query.size() == 1);
	}

}
