
package services;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import utilities.AbstractTest;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
	"classpath:spring/datasource.xml", "classpath:spring/config/packages.xml"
})
@Transactional
public class AdminServiceTest extends AbstractTest {

	@Autowired
	private AdminService	adminService;


	@Test
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

}
