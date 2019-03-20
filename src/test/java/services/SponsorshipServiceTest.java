
package services;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.validation.ConstraintViolationException;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import domain.CreditCard;
import domain.Parade;
import domain.Sponsor;
import domain.Sponsorship;
import utilities.AbstractTest;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:spring/junit.xml" })
@Transactional
public class SponsorshipServiceTest extends AbstractTest {

	@Autowired
	private SponsorshipService sponsorshipService;

	@Autowired
	private ParadeService paradeService;

	@Autowired
	private AdminService adminService;

	@Autowired
	private SponsorService sponsorService;

	/**
	 * Test the use case detailed in requirement 16.1: Manage his or her
	 * sponsorships, which includes creating them
	 */
	@Test
	public void driverAddSponsorship() {
		// Accepted parade
		Parade parade = (new ArrayList<Parade>(this.paradeService.getAcceptedParades())).get(0);
		// Draft parade
		Parade parade2 = (new ArrayList<Parade>(this.paradeService.getDraftParades())).get(0);

		Object testingData[][] = {
				// Positive test
				{ "https://www.imagen.com.mx/assets/img/imagen_share.png",
						"https://www.imagen.com.mx/assets/img/imagen_share.png", "David", "VISA", 4980003406100008L, 3,
						24, 778, parade, "sponsor1", null },
				// Negative test: Trying to create a sponsorship with a different role
				{ "https://www.imagen.com.mx/assets/img/imagen_share.png",
						"https://www.imagen.com.mx/assets/img/imagen_share.png", "David", "VISA", 4980003406100008L, 3,
						24, 778, parade, "admin1", IllegalArgumentException.class },
				// Negative test: Trying to create a sponsorship with a type of credit card not
				// allowed
				{ "https://www.imagen.com.mx/assets/img/imagen_share.png",
						"https://www.imagen.com.mx/assets/img/imagen_share.png", "David", "OTHERCARDTYPE",
						4980003406100008L, 3, 24, 778, parade, "sponsor1", IllegalArgumentException.class },
				// Negative test: Trying to create a sponsorship with a credit card whose number
				// is invalid
				{ "https://www.imagen.com.mx/assets/img/imagen_share.png",
						"https://www.imagen.com.mx/assets/img/imagen_share.png", "David", "VISA", 4980000011112222L, 3,
						24, 778, parade, "sponsor1", IllegalArgumentException.class },
				// Negative test: Trying to create a sponsorship with an expired credit card
				{ "https://www.imagen.com.mx/assets/img/imagen_share.png",
						"https://www.imagen.com.mx/assets/img/imagen_share.png", "David", "VISA", 4980003406100008L, 3,
						16, 778, parade, "sponsor1", IllegalArgumentException.class },
				// Negative test: Trying to create an sponsorship with a credit card whose CVV
				// is invalid
				{ "https://www.imagen.com.mx/assets/img/imagen_share.png",
						"https://www.imagen.com.mx/assets/img/imagen_share.png", "David", "VISA", 4980003406100008L, 3,
						24, 1, parade, "sponsor1", IllegalArgumentException.class },
				// Negative test: Trying to create a sponsorship to an unaccepted parade
				{ "https://www.imagen.com.mx/assets/img/imagen_share.png",
						"https://www.imagen.com.mx/assets/img/imagen_share.png", "David", "VISA", 4980003406100008L, 3,
						24, 778, parade2, "sponsor1", IllegalArgumentException.class },
				// Negative test: Trying to create a sponsorship with the banner and the
				// targetURL in blank
				{ "", "", "David", "VISA", 4980003406100008L, 3, 24, 778, parade, "sponsor1",
						ConstraintViolationException.class },
				// Negative test: Trying to create a sponsorship with holderName in blank
				{ "https://www.imagen.com.mx/assets/img/imagen_share.png",
						"https://www.imagen.com.mx/assets/img/imagen_share.png", "", "VISA", 4980003406100008L, 3, 24,
						778, parade, "sponsor1", ConstraintViolationException.class },
				// Negative test: Trying to create a sponsorship with number as null
				{ "https://www.imagen.com.mx/assets/img/imagen_share.png",
						"https://www.imagen.com.mx/assets/img/imagen_share.png", "David", "VISA", null, 3, 24, 778,
						parade, "sponsor1", ConstraintViolationException.class } };

		for (int i = 0; i < testingData.length; i++)
			this.templateAddSponsorship((String) testingData[i][0], (String) testingData[i][1],
					(String) testingData[i][2], (String) testingData[i][3], (Long) testingData[i][4],
					(Integer) testingData[i][5], (Integer) testingData[i][6], (Integer) testingData[i][7],
					(Parade) testingData[i][8], (String) testingData[i][9], (Class<?>) testingData[i][10]);
	}

	private void templateAddSponsorship(String banner, String targetURL, String holderName, String brandName,
			Long number, Integer expirationMonth, Integer expirationYear, Integer cvvCode, Parade parade,
			String username, Class<?> expected) {

		CreditCard card = new CreditCard();

		card.setHolderName(holderName);
		card.setBrandName(brandName);
		card.setNumber(number);
		card.setExpirationMonth(expirationMonth);
		card.setExpirationYear(expirationYear);
		card.setCvvCode(cvvCode);

		Sponsorship spo = new Sponsorship();

		spo.setBanner(banner);
		spo.setTargetURL(targetURL);
		spo.setSpentMoney(0f);
		spo.setParade(parade);
		spo.setIsActivated(true);
		spo.setCreditCard(card);

		Class<?> caught = null;

		try {
			super.authenticate(username);
			this.sponsorshipService.addOrUpdateSponsorship(spo);
			this.sponsorshipService.flush();
			super.unauthenticate();
		} catch (Throwable oops) {
			caught = oops.getClass();
		}

		super.checkExceptions(expected, caught);

	}

	/**
	 * Test the use case detailed in requirement 16.1: Manage his or her
	 * sponsorships, which includes listing them
	 */
	@Test
	public void driverListSponsorships() {

		Object testingData[][] = {
				// Positive test: Listing all sponsorships of a sponsor
				{ null, "sponsor1", null },
				// Positive test: Listing activated sponsorships of a sponsor
				{ true, "sponsor1", null },
				// Positive test: Listing deactivated sponsorships of a sponsor
				{ false, "sponsor1", null },
				// Negative test: Trying to list all sponsorships of an actor with a different
				// role
				{ null, "admin1", IllegalArgumentException.class },
				// Negative test: Trying to list activated sponsorships of an actor with a
				// different role
				{ true, "admin1", IllegalArgumentException.class },
				// Negative test: Trying to list deactivated sponsorships of an actor with a
				// different role
				{ false, "admin1", IllegalArgumentException.class } };

		for (int i = 0; i < testingData.length; i++)
			this.templateListSponsorships((Boolean) testingData[i][0], (String) testingData[i][1],
					(Class<?>) testingData[i][2]);
	}

	private void templateListSponsorships(Boolean isActivated, String username, Class<?> expected) {

		Class<?> caught = null;

		try {
			super.authenticate(username);
			Collection<Sponsorship> sponsorships = this.sponsorshipService.getSponsorships(isActivated);
			Sponsor sponsor = this.sponsorService.securityAndSponsor();
			Collection<Sponsorship> sponsorshipsOfSpo = sponsor.getSponsorships();
			if (isActivated == null)
				Assert.isTrue(sponsorships.size() == sponsorshipsOfSpo.size());
			else if (isActivated == true) {
				int size = 0;
				for (Sponsorship s : sponsorshipsOfSpo)
					if (s.getIsActivated() == true)
						size = size + 1;
				Assert.isTrue(sponsorships.size() == size);
			} else {
				int size = 0;
				for (Sponsorship s : sponsorshipsOfSpo)
					if (s.getIsActivated() == false)
						size = size + 1;
				Assert.isTrue(sponsorships.size() == size);
			}
			super.unauthenticate();
		} catch (Throwable oops) {
			caught = oops.getClass();
		}

		super.checkExceptions(expected, caught);

	}

	/**
	 * Test the use case detailed in requirement 16.1: Manage his or her
	 * sponsorships, which includes updating them
	 */
	@Test
	public void driverUpdateSponsorship() {
		Sponsor sponsor = this.sponsorService.getSponsorByUsername("sponsor1");
		// Activated sponsorship of sponsor1
		Sponsorship sponsorship = (new ArrayList<Sponsorship>(
				this.sponsorshipService.getActivatedSponsorshipsOfSponsor(sponsor.getId()))).get(0);

		Object testingData[][] = {
				// Positive test
				{ "https://www.imagen.com", sponsorship.getTargetURL(), "Ramon", "VISA",
						sponsorship.getCreditCard().getNumber(), sponsorship.getCreditCard().getExpirationMonth(),
						sponsorship.getCreditCard().getExpirationYear(), sponsorship.getCreditCard().getCvvCode(),
						sponsorship.getId(), "sponsor1", null },
				// Negative test: Trying to update a sponsorship with a different role
				{ "https://www.imagen.com", sponsorship.getTargetURL(), sponsorship.getCreditCard().getHolderName(),
						sponsorship.getCreditCard().getBrandName(), sponsorship.getCreditCard().getNumber(),
						sponsorship.getCreditCard().getExpirationMonth(),
						sponsorship.getCreditCard().getExpirationYear(), sponsorship.getCreditCard().getCvvCode(),
						sponsorship.getId(), "admin1", IllegalArgumentException.class },
				// Negative test: Trying to update a sponsorship with a type of credit card not
				// allowed
				{ sponsorship.getBanner(), sponsorship.getTargetURL(), sponsorship.getCreditCard().getHolderName(),
						"OTHERCARDTYPE", sponsorship.getCreditCard().getNumber(),
						sponsorship.getCreditCard().getExpirationMonth(),
						sponsorship.getCreditCard().getExpirationYear(), sponsorship.getCreditCard().getCvvCode(),
						sponsorship.getId(), "sponsor1", IllegalArgumentException.class },
				// Negative test: Trying to update a sponsorship with a credit card whose number
				// is invalid
				{ sponsorship.getBanner(), sponsorship.getTargetURL(), sponsorship.getCreditCard().getHolderName(),
						sponsorship.getCreditCard().getBrandName(), 4980000011112222L,
						sponsorship.getCreditCard().getExpirationMonth(),
						sponsorship.getCreditCard().getExpirationYear(), sponsorship.getCreditCard().getCvvCode(),
						sponsorship.getId(), "sponsor1", IllegalArgumentException.class },
				// Negative test: Trying to update a sponsorship with an expired credit card
				{ sponsorship.getBanner(), sponsorship.getTargetURL(), sponsorship.getCreditCard().getHolderName(),
						sponsorship.getCreditCard().getBrandName(), sponsorship.getCreditCard().getNumber(), 3, 16,
						sponsorship.getCreditCard().getCvvCode(), sponsorship.getId(), "sponsor1",
						IllegalArgumentException.class },
				// Negative test: Trying to update an sponsorship with a credit card whose CVV
				// is invalid
				{ sponsorship.getBanner(), sponsorship.getTargetURL(), sponsorship.getCreditCard().getHolderName(),
						sponsorship.getCreditCard().getBrandName(), sponsorship.getCreditCard().getNumber(),
						sponsorship.getCreditCard().getExpirationMonth(),
						sponsorship.getCreditCard().getExpirationYear(), 1, sponsorship.getId(), "sponsor1",
						IllegalArgumentException.class },
				// Negative test: Trying to update a sponsorship with the banner and the
				// targetURL in blank
				{ "", "", sponsorship.getCreditCard().getHolderName(), sponsorship.getCreditCard().getBrandName(),
						sponsorship.getCreditCard().getNumber(), sponsorship.getCreditCard().getExpirationMonth(),
						sponsorship.getCreditCard().getExpirationYear(), sponsorship.getCreditCard().getCvvCode(),
						sponsorship.getId(), "sponsor1", ConstraintViolationException.class },
				// Negative test: Trying to update a sponsorship with number as null
				{ sponsorship.getBanner(), sponsorship.getTargetURL(), sponsorship.getCreditCard().getHolderName(),
						sponsorship.getCreditCard().getBrandName(), null,
						sponsorship.getCreditCard().getExpirationMonth(),
						sponsorship.getCreditCard().getExpirationYear(), sponsorship.getCreditCard().getCvvCode(),
						sponsorship.getId(), "sponsor1", NullPointerException.class } };

		for (int i = 0; i < testingData.length; i++)
			this.templateUpdateSponsorship((String) testingData[i][0], (String) testingData[i][1],
					(String) testingData[i][2], (String) testingData[i][3], (Long) testingData[i][4],
					(Integer) testingData[i][5], (Integer) testingData[i][6], (Integer) testingData[i][7],
					(Integer) testingData[i][8], (String) testingData[i][9], (Class<?>) testingData[i][10]);
	}

	private void templateUpdateSponsorship(String banner, String targetURL, String holderName, String brandName,
			Long number, Integer expirationMonth, Integer expirationYear, Integer cvvCode, Integer sponsorshipId,
			String username, Class<?> expected) {

		Sponsorship sponsorship = this.sponsorshipService.findOne(sponsorshipId);
		CreditCard card = sponsorship.getCreditCard();

		card.setHolderName(holderName);
		card.setBrandName(brandName);
		card.setNumber(number);
		card.setExpirationMonth(expirationMonth);
		card.setExpirationYear(expirationYear);
		card.setCvvCode(cvvCode);

		sponsorship.setCreditCard(card);

		sponsorship.setBanner(banner);
		sponsorship.setTargetURL(targetURL);

		Class<?> caught = null;

		try {
			super.authenticate(username);
			this.sponsorshipService.addOrUpdateSponsorship(sponsorship);
			this.sponsorshipService.flush();
			super.unauthenticate();
		} catch (Throwable oops) {
			caught = oops.getClass();
		}

		super.checkExceptions(expected, caught);

	}

	/**
	 * Test the use case detailed in requirement 16.1: Manage his or her
	 * sponsorships, which includes removing them. Note that removing a sponsorship
	 * does not actually delete it from the system, but de-activates it. A
	 * de-activated sponsorship can be re-activated later.
	 */
	@Test
	public void driverDeleteSponsorship() {
		Sponsor sponsor = this.sponsorService.getSponsorByUsername("sponsor1");
		// Activated sponsorship with a valid credit card
		Sponsorship sponsorship = (new ArrayList<Sponsorship>(
				this.sponsorshipService.getActivatedSponsorshipsOfSponsor(sponsor.getId()))).get(0);
		// Activated sponsorship with an expired credit card
		Sponsorship sponsorship2 = (new ArrayList<Sponsorship>(
				this.sponsorshipService.getActivatedSponsorshipsOfSponsor(sponsor.getId()))).get(1);

		Object testingData[][] = {
				// Positive case: Deactivating an active sponsorship
				{ sponsorship.getId(), "sponsor1", null },
				// Negative case: Trying to activate the sponsorship that we have deactivated in
				// the last test with a different role
				{ sponsorship.getId(), "admin1", IllegalArgumentException.class },
				// Positive case: Deactivating an active sponsorship
				{ sponsorship2.getId(), "sponsor1", null },
				// Negative case: Trying to activate the sponsorship that we have deactivated in
				// the last test, but the credit card has expired
				{ sponsorship2.getId(), "sponsor1", IllegalArgumentException.class } };

		for (int i = 0; i < testingData.length; i++)
			this.templateDeleteSponsorship((Integer) testingData[i][0], (String) testingData[i][1],
					(Class<?>) testingData[i][2]);
	}

	private void templateDeleteSponsorship(Integer sponsorshipId, String username, Class<?> expected) {

		Class<?> caught = null;

		try {
			super.authenticate(username);
			this.sponsorshipService.changeStatus(sponsorshipId);
			this.sponsorshipService.flush();
			super.unauthenticate();
		} catch (Throwable oops) {
			caught = oops.getClass();
		}

		super.checkExceptions(expected, caught);

	}

	/**
	 * Test the specific list that is shown to the admin of the sponsorships
	 */
	@Test
	public void driverListSponsorshipsAsAdmin() {

		Object testingData[][] = {
				// Positive test: Listing all sponsorships
				{ null, "admin1", null },
				// Positive test: Listing activated sponsorships
				{ true, "admin1", null },
				// Positive test: Listing deactivated sponsorships
				{ false, "admin1", null },
				// Negative test: Trying to list all sponsorships with a
				// different role
				{ null, "sponsor1", IllegalArgumentException.class },
				// Negative test: Trying to list activated sponsorships with a
				// different role
				{ true, "sponsor1", IllegalArgumentException.class },
				// Negative test: Trying to list deactivated sponsorships with a
				// different role
				{ false, "sponsor1", IllegalArgumentException.class } };

		for (int i = 0; i < testingData.length; i++)
			this.templateListSponsorshipsAsAdmin((Boolean) testingData[i][0], (String) testingData[i][1],
					(Class<?>) testingData[i][2]);
	}

	private void templateListSponsorshipsAsAdmin(Boolean isActivated, String username, Class<?> expected) {

		Class<?> caught = null;

		try {
			super.authenticate(username);
			Collection<Sponsorship> sponsorships = this.sponsorshipService.findAll();
			Collection<Sponsorship> sponsorshipsAsAdmin = this.sponsorshipService.getSponsorshipsAsAdmin(isActivated);
			if (isActivated == null)
				Assert.isTrue(sponsorships.size() == sponsorshipsAsAdmin.size());
			else
				Assert.isTrue(sponsorships
						.size() == (sponsorshipsAsAdmin.size() + (sponsorships.size() - sponsorshipsAsAdmin.size())));
			super.unauthenticate();
		} catch (Throwable oops) {
			caught = oops.getClass();
		}

		super.checkExceptions(expected, caught);

	}

	/**
	 * Test the use case detailed in requirement 18.1: Launch a process that
	 * automatically de-activates the sponsorships whose credit cards have expired.
	 */
	@Test
	public void driverCheckAndDeactivateSponsorshipsAsAdmin() {

		Object testingData[][] = {
				// Positive case
				{ "admin1", null },
				// Negative case: Trying to launch the process
				{ "sponsor1", IllegalArgumentException.class } };

		for (int i = 0; i < testingData.length; i++)
			this.templateCheckAndDeactivateSponsorshipsAsAdmin((String) testingData[i][0],
					(Class<?>) testingData[i][1]);
	}

	private void templateCheckAndDeactivateSponsorshipsAsAdmin(String username, Class<?> expected) {

		Class<?> caught = null;

		try {
			super.authenticate(username);
			Integer activeSponsorships = this.sponsorshipService.getActivatedSponsorshipsAsAdmin().size();
			this.sponsorshipService.checkAndDeactivateSponsorships();
			Assert.isTrue(this.sponsorshipService.getActivatedSponsorshipsAsAdmin().size() + 1 == activeSponsorships);
			super.unauthenticate();
			this.sponsorshipService.flush();
		} catch (Throwable oops) {
			caught = oops.getClass();
		}

		super.checkExceptions(expected, caught);

	}

	/**
	 * Test the non-functional requirement detailed in 20: Every time a
	 * sponsorship's selected, the system must charge a flat fare to the
	 * corresponding sponsorship. The fare must include the current VAT percentage
	 */
	@Test
	public void driverUpdateSpentMoneyOfSponsorship() {
		Sponsor sponsor = this.sponsorService.getSponsorByUsername("sponsor1");
		List<Sponsorship> sponsorships = (List<Sponsorship>) this.sponsorshipService
				.getActivatedSponsorshipsOfSponsor(sponsor.getId());

		Sponsorship sponsorship = sponsorships.get(0);
		Parade parade = sponsorship.getParade();

		Integer anotherParadeId = 637;

		Object testingData[][] = {
				// Positive test
				{ parade.getId(), sponsorship.getId(), null },
				// Negative test: The parade of the sponsorship does not agree with the
				// indicated parade
				{ anotherParadeId, sponsorship.getId(), IllegalArgumentException.class } };

		for (int i = 0; i < testingData.length; i++)
			this.templateUpdateSpentMoneyOfSponsorship((Integer) testingData[i][0], (Integer) testingData[i][1],
					(Class<?>) testingData[i][2]);
	}

	private void templateUpdateSpentMoneyOfSponsorship(int paradeId, int sponsorshipId, Class<?> expected) {

		Class<?> caught = null;

		try {
			this.sponsorshipService.updateSpentMoneyOfSponsorship(paradeId, sponsorshipId);
			this.sponsorshipService.flush();
		} catch (Throwable oops) {
			caught = oops.getClass();
		}

		super.checkExceptions(expected, caught);

	}

}
