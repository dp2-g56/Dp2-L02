
package services;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import utilities.AbstractTest;
import domain.Float;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
	"classpath:spring/datasource.xml", "classpath:spring/config/packages.xml"
})
@Transactional
public class FloatServiceTest extends AbstractTest {

	@Autowired
	private FloatService	floatService;


	@Test
	public void testCreate() {
		Float floatt = new Float();

		floatt = this.floatService.create();

		floatt.setTitle("sasa");
		floatt.setDescription("sasa");

		Float saved = new Float();
		saved = this.floatService.save(floatt);
		Assert.isTrue(this.floatService.findAll().contains(saved));

	}

}
