
package services;

import java.util.ArrayList;
import java.util.Collection;

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
			this.templateAddOrUpdateSponsorship((String) testingData[i][0], (String) testingData[i][1],
					(String) testingData[i][2], (String) testingData[i][3], (Long) testingData[i][4],
					(Integer) testingData[i][5], (Integer) testingData[i][6], (Integer) testingData[i][7],
					(Parade) testingData[i][8], (String) testingData[i][9], (Class<?>) testingData[i][10]);
	}

	private void templateAddOrUpdateSponsorship(String banner, String targetURL, String holderName, String brandName,
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
				for (Sponsorship s : sponsorshipsOfSpo) {
					if (s.getIsActivated() == true)
						size = size + 1;
					Assert.isTrue(sponsorships.size() == size);
				}
			} else {
				int size = 0;
				for (Sponsorship s : sponsorshipsOfSpo) {
					if (s.getIsActivated() == false)
						size = size + 1;
					Assert.isTrue(sponsorships.size() == size);
				}
			}
			super.unauthenticate();
		} catch (Throwable oops) {
			caught = oops.getClass();
		}

		super.checkExceptions(expected, caught);

	}
}
