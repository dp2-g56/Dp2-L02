
package services;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import utilities.AbstractTest;
import domain.Brotherhood;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
	"classpath:spring/datasource.xml", "classpath:spring/config/packages.xml"
})
@Transactional
public class BrotherhoodServiceTest extends AbstractTest {

	@Autowired
	private BrotherhoodService	brotherhoodService;

	@Autowired
	private BoxService			boxService;


	@Test
	public void testCreate() {
		Brotherhood bro = new Brotherhood();

		bro = this.brotherhoodService.createBrotherhood();

		bro.setTitle("ddd");
		bro.setName("aaa");
		bro.setSurname("aaa");

		bro.setMiddleName("");

		bro.setPolarity(0);
		bro.setHasSpam(false);
		bro.setPhoto(null);
		bro.setPhoneNumber("");
		bro.setAddress("");
		bro.setArea(null);
		bro.setEmail("dada@gmail.com");
		bro.getUserAccount().setUsername("Quimi");
		bro.getUserAccount().setPassword("12345");

		Brotherhood saved = new Brotherhood();
		saved = this.brotherhoodService.saveCreate(bro);
		Assert.isTrue(this.brotherhoodService.findAll().contains(saved));

	}

}
