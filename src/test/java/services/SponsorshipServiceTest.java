
package services;

import java.util.ArrayList;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import domain.CreditCard;
import domain.Parade;
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
			this.sponsorshipService.addSponsorship(spo);
			this.sponsorshipService.flush();
			super.unauthenticate();
		} catch (Throwable oops) {
			caught = oops.getClass();
		}

		super.checkExceptions(expected, caught);

	}
}
