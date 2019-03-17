
package services;

import java.util.ArrayList;
import java.util.Collection;

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

	@Test
	public void driverAddSponsorship() {
		Parade parade = (new ArrayList<Parade>(this.paradeService.getAcceptedParades())).get(0);
		Parade parade2 = (new ArrayList<Parade>(this.paradeService.getDraftParades())).get(0);

		Object testingData[][] = {
				{ "https://www.imagen.com.mx/assets/img/imagen_share.png",
						"https://www.imagen.com.mx/assets/img/imagen_share.png", "David", "VISA", 4980003406100008L, 3,
						24, 778, parade, "sponsor1", null },
				{ "https://www.imagen.com.mx/assets/img/imagen_share.png",
						"https://www.imagen.com.mx/assets/img/imagen_share.png", "David", "VISA", 4980003406100008L, 3,
						24, 778, parade, "admin1", IllegalArgumentException.class },
				{ "https://www.imagen.com.mx/assets/img/imagen_share.png",
						"https://www.imagen.com.mx/assets/img/imagen_share.png", "David", "OTHERCARDTYPE",
						4980003406100008L, 3, 24, 778, parade, "sponsor1", IllegalArgumentException.class },
				{ "https://www.imagen.com.mx/assets/img/imagen_share.png",
						"https://www.imagen.com.mx/assets/img/imagen_share.png", "David", "VISA", 1L, 3, 24, 778,
						parade, "sponsor1", IllegalArgumentException.class },
				{ "https://www.imagen.com.mx/assets/img/imagen_share.png",
						"https://www.imagen.com.mx/assets/img/imagen_share.png", "David", "VISA", 4980003406100008L, 3,
						16, 778, parade, "sponsor1", IllegalArgumentException.class },
				{ "https://www.imagen.com.mx/assets/img/imagen_share.png",
						"https://www.imagen.com.mx/assets/img/imagen_share.png", "David", "VISA", 4980003406100008L, 3,
						24, 1, parade, "sponsor1", IllegalArgumentException.class },
				{ "https://www.imagen.com.mx/assets/img/imagen_share.png",
						"https://www.imagen.com.mx/assets/img/imagen_share.png", "David", "VISA", 4980003406100008L, 3,
						24, 778, parade2, "sponsor1", IllegalArgumentException.class } };

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
		spo.setGain(0f);
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

	@Test
	public void driverListSponsorships() {

		Object testingData[][] = { { null, "sponsor1", null }, { true, "sponsor1", null }, { false, "sponsor1", null },
				{ null, "admin1", IllegalArgumentException.class }, { true, "admin1", IllegalArgumentException.class },
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

	@Test
	public void driverUpdateSponsorship() {
		Sponsor sponsor = this.sponsorService.getSponsorByUsername("sponsor1");
		Sponsorship sponsorship = (new ArrayList<Sponsorship>(
				this.sponsorshipService.getActivatedSponsorshipsOfSponsor(sponsor.getId()))).get(0);

		Object testingData[][] = { { "https://www.imagen.com", sponsorship.getTargetURL(), "Ramon",
				sponsorship.getCreditCard().getBrandName(), sponsorship.getCreditCard().getNumber(),
				sponsorship.getCreditCard().getExpirationMonth(), sponsorship.getCreditCard().getExpirationYear(),
				sponsorship.getCreditCard().getCvvCode(), sponsorship.getId(), "sponsor1", null },
				{ "nourl", sponsorship.getTargetURL(), sponsorship.getCreditCard().getHolderName(),
						sponsorship.getCreditCard().getBrandName(), sponsorship.getCreditCard().getNumber(), 13,
						sponsorship.getCreditCard().getExpirationYear(), 1111111, sponsorship.getId(), "sponsor1",
						ConstraintViolationException.class },
				{ "https://www.imagen.com", sponsorship.getTargetURL(), "Ramon",
						sponsorship.getCreditCard().getBrandName(), sponsorship.getCreditCard().getNumber(),
						sponsorship.getCreditCard().getExpirationMonth(),
						sponsorship.getCreditCard().getExpirationYear(), sponsorship.getCreditCard().getCvvCode(),
						sponsorship.getId(), "admin1", IllegalArgumentException.class },
				{ "https://www.imagen.com", sponsorship.getTargetURL(), "Ramon",
						sponsorship.getCreditCard().getBrandName(), sponsorship.getCreditCard().getNumber(), 12, 11,
						sponsorship.getCreditCard().getCvvCode(), sponsorship.getId(), "sponsor1",
						IllegalArgumentException.class } };

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

	@Test
	public void driverDeleteSponsorship() {
		Sponsor sponsor = this.sponsorService.getSponsorByUsername("sponsor1");
		Sponsorship sponsorship = (new ArrayList<Sponsorship>(
				this.sponsorshipService.getActivatedSponsorshipsOfSponsor(sponsor.getId()))).get(0);
		Sponsorship sponsorship2 = (new ArrayList<Sponsorship>(
				this.sponsorshipService.getActivatedSponsorshipsOfSponsor(sponsor.getId()))).get(1);

		Object testingData[][] = { { sponsorship.getId(), "sponsor1", null },
				{ sponsorship.getId(), "admin1", IllegalArgumentException.class },
				{ sponsorship2.getId(), "sponsor1", null },
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

	@Test
	public void driverListSponsorshipsAsAdmin() {

		Object testingData[][] = { { null, "admin1", null }, { true, "admin1", null }, { false, "admin1", null },
				{ null, "sponsor1", IllegalArgumentException.class },
				{ true, "sponsor1", IllegalArgumentException.class },
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

	@Test
	public void driverCheckAndDeactivateSponsorshipsAsAdmin() {

		Object testingData[][] = { { "admin1", null }, { "sponsor1", IllegalArgumentException.class } };

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

}
