
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

import domain.Actor;
import domain.Admin;
import domain.Area;
import domain.Brotherhood;
import domain.Chapter;
import domain.Member;
import domain.Position;
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

	@Autowired
	private PositionService positionService;

	@Autowired
	private ConfigurationService configurationService;

	@Autowired
	private ActorService actorService;

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
	 * This next driver will test 7 cases about registering as a Administrator, the
	 * data in this drivers will only be those which have restrictions about its
	 * persistence on database or business rules. This tested cases are:
	 *
	 * 1. A positive case where no error is expected.
	 *
	 * 2. A case where the new username is blank, a ConstraintViolationException is
	 * expected.
	 *
	 * 3. A case where the new name is blank, a ConstraintViolationException is
	 * expected.
	 *
	 * 4. A case where the new surname is blank, a ConstraintViolationException is
	 * expected.
	 *
	 * 5. A case where the photo posted is not blank but neither a valid URL, a
	 * ConstraintViolationException is expected.
	 *
	 * 6. A case where the email posted is not valid, a ConstraintViolationException
	 * is expected.
	 *
	 * 7. A case where the actor trying to register a new Administrator account is
	 * not an Administrator, an IllegalArgumentException is expected.
	 **/
	@Test
	public void driverRegister() {
		Object testingData[][] = {
				{ "admin1", "AdminTest1", "name1", "surname1", "emailTest@gmail.com", "https://www.example.com/",
						null },
				{ "admin1", "", "name1", "surname1", "emailTest@gmail.com", "https://www.example.com/",
						ConstraintViolationException.class },
				{ "admin1", "AdminTest2", "", "surname2", "emailTest2@gmail.com", "https://www.example2.com/",
						ConstraintViolationException.class },
				{ "admin1", "AdminTest3", "name3", "", "emailTest3@gmail.com", "https://www.example3.com/",
						ConstraintViolationException.class },
				{ "admin1", "AdminTest4", "name1", "surname1", "emailTest@gmail.com", "invalid url",
						ConstraintViolationException.class },
				{ "admin1", "AdminTest8", "name1", "surname1", "invalid email", "https://www.example.com/",
						ConstraintViolationException.class },
				{ "chapter1", "AdminTest8", "name1", "surname1", "emailTest@gmail.com", "https://www.example.com/",
						IllegalArgumentException.class } };

		for (int i = 0; i < testingData.length; i++)
			this.templateRegister((String) testingData[i][0], (String) testingData[i][1], (String) testingData[i][2],
					(String) testingData[i][3], (String) testingData[i][4], (String) testingData[i][5],
					(Class<?>) testingData[i][6]);
	}

	/**
	 * This next driver will test 7 cases about editing personal data as a
	 * Administrator, the data in this drivers will only be those which have
	 * restrictions about its persistence on database or business rules. This tested
	 * cases are:
	 *
	 * 1. A positive case where no error is expected.
	 *
	 * 2. A case where the new name is blank, a ConstraintViolationException is
	 * expected.
	 *
	 * 3. A case where the new surname is blank, a ConstraintViolationException is
	 * expected.
	 *
	 * 4. A case where the new email is blank, a ConstraintViolationException is
	 * expected.
	 *
	 * 5. A case where the email posted is not valid, a ConstraintViolationException
	 * is expected.
	 *
	 * 6. A case where the photo posted is not blank but neither a valid URL, a
	 * ConstraintViolationException is expected.
	 *
	 * 7. A case when an Actor tries to edit another actor personal data, an
	 * IllegalArgumentException.
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

	protected void templateRegister(String username, String newUsername, String name, String surname, String email,
			String photo, Class<?> expected) {
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
		Float query = this.adminRepository.maxNumberRecordsPerHistory();
		Assert.isTrue(query == 4.0);
	}

	@Test
	public void testMinNumberRecordsPerHistory() {
		Float query = this.adminRepository.minNumberRecordsPerHistory();
		Assert.isTrue(query == 4.0);
	}

	@Test
	public void testAvgNumberRecordsPerHistory() {
		Float query = this.adminRepository.avgRecordsPerHistory();
		Assert.isTrue(query == 4.0);
	}

	@Test
	public void testStddevNumberRecordsPerHistory() {
		Float query = this.adminRepository.stddevRecordsPerHistory();
		Assert.isTrue(query == 0.0);
	}

	@Test
	public void testBroLargestHistory() {
		List<Brotherhood> query = this.adminRepository.broLargestHistory();
		Assert.isTrue(query.size() == 3);
	}

	@Test
	public void testBroHistoryLargerThanAvg() {
		List<Brotherhood> query = this.adminRepository.broHistoryLargerThanAvg();
		// Compare
		Assert.isTrue(query.isEmpty());
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
		Assert.isTrue(query > 0.16);
	}

	@Test
	public void testRatioParadesAcceptedRequests() {
		Float query = this.adminRepository.ratioParadesAcceptedRequests();
		// Compare the query result with the expected result
		Assert.isTrue(query > 33.0);
	}

	@Test
	public void testRatioParadesSubmittedRequests() {
		Float query = this.adminRepository.ratioParadesSubmittedRequests();
		// Compare the query result with the expected result
		Assert.isTrue(query > 66.0);
	}

	@Test
	public void testRatioParadesRejectedRequests() {
		Float query = this.adminRepository.ratioParadesRejectedRequests();
		// Compare the query result with the expected result
		Assert.isTrue(query > 16.0);
	}

	@Test
	public void testRatioActiveSponsorships() {
		Float query = this.adminRepository.ratioActiveSponsorships();
		// Compare the query result with the expected result
		Assert.isTrue(query > 66);
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

	@Test
	public void testMaxNumberMembersPerBrotherhood() {
		Float query = this.adminRepository.maxNumberMembersPerBrotherhood();
		Assert.isTrue(query == 4.0);
	}

	@Test
	public void testMinNumberMembersPerBrotherhood() {
		Float query = this.adminRepository.minNumberMembersPerBrotherhood();
		Assert.isTrue(query == 0.0);
	}

	@Test
	public void testAvgNumberMembersPerBrotherhood() {
		Float query = this.adminRepository.avgMembersPerBrotherhood();
		Assert.isTrue(query == 1.5);
	}

	@Test
	public void testStddevNumberMembersPerBrotherhood() {
		Float query = this.adminRepository.stddevMembersPerBrotherhood();
		Assert.isTrue(query == 1.5);
	}

	@Test
	public void testLargestBrotherhoods() {
		List<String> query = this.adminRepository.largestBrotherhoods();
		Assert.isTrue(query.size() == 1);
	}

	@Test
	public void testSmallestBrotherhoods() {
		List<String> query = this.adminRepository.smallestBrotherhoods();
		Assert.isTrue(query.size() == 1);
	}

	@Test
	public void testRatioApprovedRequestsByParades() {
		List<Float> query = this.adminRepository.ratioApprovedRequestsByParades();
		Assert.isTrue(query.size() == 7);
	}

	@Test
	public void testRatioPendingRequestsByParades() {
		List<Float> query = this.adminRepository.ratioPendingRequestsByParades();
		Assert.isTrue(query.size() == 7);
	}

	@Test
	public void testRatioRejectedRequestsByParades() {
		List<Float> query = this.adminRepository.ratioRejectedRequestsByParades();
		Assert.isTrue(query.size() == 7);
	}

	@Test
	public void testListParadeNext30Days() {
		List<String> query = this.adminRepository.listParadeNext30Days();
		Assert.isTrue(query.size() == 0);
	}

	@Test
	public void testRatioApprovedRequests() {
		Float query = this.adminRepository.ratioApprovedRequests();
		Assert.isTrue(query > 33);
	}

	@Test
	public void testRatioPendingRequests() {
		Float query = this.adminRepository.ratioPendingRequests();
		Assert.isTrue(query > 33);
	}

	@Test
	public void testRatioRejectedRequests() {
		Float query = this.adminRepository.ratioRejectedRequests();
		Assert.isTrue(query > 33);
	}

	@Test
	public void testListMembersAtLeastTenPercentRequestApproved() {
		List<Member> query = this.adminRepository.listMembersAtLeastTenPercentRequestApproved();
		Assert.isTrue(query.size() == 2);
	}

	@Test
	public void testNumberPositions() {
		List<Float> query = this.adminRepository.numberPositions();
		Assert.isTrue(query.size() == 7);
	}

	@Test
	public void testRatioBrotherhoodPerArea() {
		List<Float> query = this.adminRepository.ratioBrotherhoodPerArea();
		Assert.isTrue(query.size() == 3);
	}

	@Test
	public void testNumberBrotherhoodsPerArea() {
		List<Float> query = this.adminRepository.numberBrotherhoodsPerArea();
		Assert.isTrue(query.size() == 3);
	}

	@Test
	public void testAvgNumberBrotherhoodPerArea() {
		Float query = this.adminRepository.avgNumberBrotherhoodPerArea();
		Assert.isTrue(query == 1.0);
	}

	@Test
	public void testMinNumberBrotherhoodPerArea() {
		Float query = this.adminRepository.minNumberBrotherhoodPerArea();
		Assert.isTrue(query == 0.0);
	}

	@Test
	public void testMaxNumberBrotherhoodPerArea() {
		Float query = this.adminRepository.maxNumberBrotherhoodPerArea();
		Assert.isTrue(query == 2.0);
	}

	@Test
	public void testStddevNumberBrotherhoodPerArea() {
		Float query = this.adminRepository.stddevNumberBrotherhoodPerArea();
		Assert.isTrue(query > 0.8);
	}

	@Test
	public void driverCreatePositions() {
		Object testingData[][] = {
				// Positive test
				{ "Owner", "Jefe", "admin1", null },
				// Negative test: Trying to create a position with a different role
				{ "President", "Presidente", "brotherhood1", IllegalArgumentException.class },
				// Negative test: Trying to create a position with a different role
				{ "President", "Presidente", "chapter1", IllegalArgumentException.class },
				// Negative test: Trying to create a position with a different role
				{ "President", "Presidente", "member1", IllegalArgumentException.class },
				// Negative test: Trying to create a position with a blank titleSpanish
				{ "President", "", "admin1", ConstraintViolationException.class },
				// Negative test: Trying to create a position with a blank titleEnglish
				{ "", "Presidente", "admin1", ConstraintViolationException.class } };

		for (int i = 0; i < testingData.length; i++)
			this.templateCreatePositions((String) testingData[i][0], (String) testingData[i][1],
					(String) testingData[i][2], (Class<?>) testingData[i][3]);
	}

	private void templateCreatePositions(String titleEnglish, String titleSpanish, String username, Class<?> expected) {
		Class<?> caught = null;

		try {
			this.startTransaction();

			super.authenticate(username);

			Position position = this.positionService.createPosition();

			position.setTitleEnglish(titleEnglish);
			position.setTitleSpanish(titleSpanish);

			this.positionService.save(position);
			this.positionService.flush();

			super.unauthenticate();
		} catch (Throwable oops) {
			caught = oops.getClass();
		} finally {
			this.rollbackTransaction();
		}

		super.checkExceptions(expected, caught);
	}

	@Test
	public void driverUpdatePositions() {
		Position p = this.positionService.findOne(super.getEntityId("position1"));

		Object testingData[][] = {
				// Positive test
				{ p, "Owner", p.getTitleSpanish(), "admin1", null },
				// Negative test: Trying to create a position with a blank titleEnglish
				{ p, "", p.getTitleSpanish(), "admin1", ConstraintViolationException.class },
				// Negative test: Trying to create a position with a blank titleSpanish
				{ p, p.getTitleEnglish(), "", "admin1", ConstraintViolationException.class },
				// Negative test: Trying to create a position with a different role
				{ p, "Owner", p.getTitleSpanish(), "member1", IllegalArgumentException.class },
				// Negative test: Trying to create a position with a different role
				{ p, "Owner", p.getTitleSpanish(), "brotherhood1", IllegalArgumentException.class },
				// Negative test: Trying to create a position with a different role
				{ p, "Owner", p.getTitleSpanish(), "chapter1", IllegalArgumentException.class } };

		for (int i = 0; i < testingData.length; i++)
			this.templateUpdatePositions((Position) testingData[i][0], (String) testingData[i][1],
					(String) testingData[i][2], (String) testingData[i][3], (Class<?>) testingData[i][4]);
	}

	private void templateUpdatePositions(Position position, String titleEnglish, String titleSpanish, String username,
			Class<?> expected) {
		Class<?> caught = null;

		try {
			this.startTransaction();

			super.authenticate(username);

			position.setTitleEnglish(titleEnglish);
			position.setTitleSpanish(titleSpanish);

			this.positionService.savePosition(position);
			this.positionService.flush();

			super.unauthenticate();
		} catch (Throwable oops) {
			caught = oops.getClass();
		} finally {
			this.rollbackTransaction();
		}

		super.checkExceptions(expected, caught);
	}

	@Test
	public void driverDeletePositions() {
		Position p1 = this.positionService.findOne(super.getEntityId("position1"));
		Position p2 = this.positionService.findOne(super.getEntityId("position2"));

		Object testingData[][] = {
				// Positive test
				{ p2, "admin1", null },
				// Negative test
				{ p2, "member1", IllegalArgumentException.class },
				// Negative test
				{ p2, "brotherhood1", IllegalArgumentException.class },
				// Negative test
				{ p1, "admin1", IllegalArgumentException.class } };

		for (int i = 0; i < testingData.length; i++)
			this.templateDeletePositions((Position) testingData[i][0], (String) testingData[i][1],
					(Class<?>) testingData[i][2]);
	}

	private void templateDeletePositions(Position position, String username, Class<?> expected) {
		Class<?> caught = null;

		try {
			this.startTransaction();

			super.authenticate(username);

			this.positionService.deletePosition(position);

			this.positionService.flush();

			super.unauthenticate();
		} catch (Throwable oops) {
			caught = oops.getClass();
		} finally {
			this.rollbackTransaction();
		}

		super.checkExceptions(expected, caught);
	}

	@Test
	public void driverCreateArea() {
		Object testingData[][] = {
				// Positive test
				{ "AreaEj", "http://www.image.com", "admin1", null },
				// Negative test: Trying to create an area with a blank area name
				{ "", "http://www.image.com", "admin1", ConstraintViolationException.class },
				// Negative test: Trying to create an area with with a different role
				{ "AreaEj", "http://www.image.com", "brotherhood1", IllegalArgumentException.class } };

		for (int i = 0; i < testingData.length; i++)
			this.templateCreateArea((String) testingData[i][0], (String) testingData[i][1], (String) testingData[i][2],
					(Class<?>) testingData[i][3]);
	}

	private void templateCreateArea(String name, String picture, String username, Class<?> expected) {
		Class<?> caught = null;

		try {
			this.startTransaction();

			super.authenticate(username);

			Area area = this.areaService.createArea();
			List<String> pictures = new ArrayList<String>();
			pictures.add(picture);

			area.setName(name);
			area.setPictures(pictures);

			this.areaService.save(area);

			this.areaService.flush();

			super.unauthenticate();
		} catch (Throwable oops) {
			caught = oops.getClass();
		} finally {
			this.rollbackTransaction();
		}

		super.checkExceptions(expected, caught);
	}

	@Test
	public void driverUpdateArea() {
		Area a = this.areaService.findOne(super.getEntityId("area1"));

		Object testingData[][] = {
				// Positive test
				{ a, "a", a.getPictures(), "admin1", null },
				// Negative test: Trying to uptade an area with a blank area name
				{ a, "", a.getPictures(), "admin1", ConstraintViolationException.class },
				// Negative test: Trying to update an area with with a different role
				{ a, a.getName(), a.getPictures(), "brotherhood1", IllegalArgumentException.class } };

		for (int i = 0; i < testingData.length; i++)
			this.templateUpdateArea((Area) testingData[i][0], (String) testingData[i][1],
					(List<String>) testingData[i][2], (String) testingData[i][3], (Class<?>) testingData[i][4]);
	}

	private void templateUpdateArea(Area area, String name, List<String> pictures, String username, Class<?> expected) {
		Class<?> caught = null;

		try {
			this.startTransaction();

			super.authenticate(username);

			area.setName(name);
			area.setPictures(pictures);

			this.areaService.updateArea(area);

			this.areaService.flush();

			super.unauthenticate();
		} catch (Throwable oops) {
			caught = oops.getClass();
		} finally {
			this.rollbackTransaction();
		}

		super.checkExceptions(expected, caught);
	}

	@Test
	public void driverDeleteArea() {
		Area area3 = this.areaService.findOne(super.getEntityId("area3"));
		Area area1 = this.areaService.findOne(super.getEntityId("area1"));

		Object testingData[][] = {
				// Positive test
				{ area3, "admin1", null },
				// Negative test: Trying to delete an area with different role
				{ area3, "brotherhood1", IllegalArgumentException.class },
				// Negative test: Trying to delete an area that is assigned to a brotherhood
				{ area1, "admin1", IllegalArgumentException.class } };

		for (int i = 0; i < testingData.length; i++)
			this.templateDeleteArea((Area) testingData[i][0], (String) testingData[i][1], (Class<?>) testingData[i][2]);

	}

	private void templateDeleteArea(Area area, String username, Class<?> expected) {
		Class<?> caught = null;

		try {
			this.startTransaction();

			super.authenticate(username);

			this.areaService.deleteArea(area);

			this.areaService.flush();

			super.unauthenticate();
		} catch (Throwable oops) {
			caught = oops.getClass();
		} finally {
			this.rollbackTransaction();
		}

		super.checkExceptions(expected, caught);

	}

	@Test
	public void driverCreatePositiveNegativeWords() {
		Object testingData[][] = {
				// Positive test
				{ "bad", "badword", "admin1", null },
				// Negative test: Trying to add a word with a different role
				{ "good", "goodword", "member1", IllegalArgumentException.class } };

		for (int i = 0; i < testingData.length; i++)
			this.templateCreatePositiveNegativeWords((String) testingData[i][0], (String) testingData[i][1],
					(String) testingData[i][2], (Class<?>) testingData[i][3]);
	}

	private void templateCreatePositiveNegativeWords(String word, String wordType, String username, Class<?> expected) {
		Class<?> caught = null;

		try {
			this.startTransaction();

			super.authenticate(username);

			if (wordType.equals("goodword"))
				this.configurationService.addGoodWords(word);
			else
				this.configurationService.addBadWords(word);

			super.unauthenticate();
		} catch (Throwable oops) {
			caught = oops.getClass();
		} finally {
			this.rollbackTransaction();
		}

		super.checkExceptions(expected, caught);
	}

	@Test
	public void driverUpdatePositiveNegativeWords() {
		Object testingData[][] = {
				// Positive test
				{ "bad", "badword", "admin1", null },
				// Negative test: Trying to update a word with a different role
				{ "good", "goodword", "member1", IllegalArgumentException.class } };

		for (int i = 0; i < testingData.length; i++)
			this.templateUpdatePositiveNegativeWords((String) testingData[i][0], (String) testingData[i][1],
					(String) testingData[i][2], (Class<?>) testingData[i][3]);
	}

	private void templateUpdatePositiveNegativeWords(String word, String originalWord, String username,
			Class<?> expected) {
		Class<?> caught = null;

		try {
			this.startTransaction();

			super.authenticate(username);

			this.configurationService.editWord(word, originalWord);

			super.unauthenticate();
		} catch (Throwable oops) {
			caught = oops.getClass();
		} finally {
			this.rollbackTransaction();
		}

		super.checkExceptions(expected, caught);
	}

	@Test
	public void driverDeletePositiveNegativeWords() {
		Object testingData[][] = {
				// Positive test
				{ "bad", "admin1", null },
				// Negative test: Trying to delete a word with a different role
				{ "good", "member1", IllegalArgumentException.class } };

		for (int i = 0; i < testingData.length; i++)
			this.templateDeletePositiveNegativeWords((String) testingData[i][0], (String) testingData[i][1],
					(Class<?>) testingData[i][2]);
	}

	private void templateDeletePositiveNegativeWords(String word, String username, Class<?> expected) {
		Class<?> caught = null;

		try {
			this.startTransaction();

			super.authenticate(username);

			List<String> goodWords = new ArrayList<String>();

			goodWords = this.configurationService.showGoodWordsList();

			if (goodWords.contains(word))
				this.configurationService.deleteGoodWord(word);
			else
				this.configurationService.deleteBadWord(word);

			super.unauthenticate();
		} catch (Throwable oops) {
			caught = oops.getClass();
		} finally {
			this.rollbackTransaction();
		}

		super.checkExceptions(expected, caught);
	}

	@Test
	public void driverBan() {
		Object testingData[][] = {
				// Positive test
				{ "chapter1", "admin1", null },
				// Negative test
				{ "admin2", "admin1", IllegalArgumentException.class },
				// Negative test
				{ "chapter1", "chapter2", IllegalArgumentException.class } };

		for (int i = 0; i < testingData.length; i++)
			this.templateBan((String) testingData[i][0], (String) testingData[i][1], (Class<?>) testingData[i][2]);
	}

	private void templateBan(String user, String username, Class<?> expected) {
		Class<?> caught = null;

		try {
			this.startTransaction();

			super.authenticate(username);

			Actor usern = this.actorService.getActorByUsername(user);
			if (usern.getHasSpam())
				this.adminService.banSuspiciousActor(usern);
			else
				throw new IllegalArgumentException();

			super.unauthenticate();
		} catch (Throwable oops) {
			caught = oops.getClass();
		} finally {
			this.rollbackTransaction();
		}

		super.checkExceptions(expected, caught);

	}

	@Test
	public void driverUnBan() {
		Object testingData[][] = {
				// Positive test
				{ "DELETED", "admin1", null },
				// Negative test
				{ "admin2", "admin1", IllegalArgumentException.class },
				// Negative test
				{ "chapter1", "chapter2", IllegalArgumentException.class } };

		for (int i = 0; i < testingData.length; i++)
			this.templateUnBan((String) testingData[i][0], (String) testingData[i][1], (Class<?>) testingData[i][2]);
	}

	private void templateUnBan(String user, String username, Class<?> expected) {
		Class<?> caught = null;

		try {
			this.startTransaction();

			super.authenticate(username);

			Actor usern = this.actorService.getActorByUsername(user);
			if (usern.getUserAccount().getIsNotLocked() == false)
				this.adminService.unBanSuspiciousActor(usern);
			else
				throw new IllegalArgumentException();

			super.unauthenticate();
		} catch (Throwable oops) {
			caught = oops.getClass();
		} finally {
			this.rollbackTransaction();
		}

		super.checkExceptions(expected, caught);

	}

}
