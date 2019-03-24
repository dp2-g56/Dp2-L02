
package services;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import utilities.AbstractTest;
import domain.Box;
import domain.Message;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
	"classpath:spring/junit.xml"
})
@Transactional
public class UpdateMessageServiceTest extends AbstractTest {

	@Autowired
	private MessageService	messageService;

	@Autowired
	private BoxService		boxService;


	@Test
	public void driverUpdateMessage() {

		Object testingData[][] = {
			{
				//Positive test, message in OUTBOX moving to INBOX
				"admin1", "message1", "inBoxAdmin1", null
			}, {
				//Negative test, move message to a box is not yours
				"admin1", "message1", "inBoxAdmin2", IllegalArgumentException.class
			}, {
				//Negative test, move message to a box is not yours
				"admin1", "message4", "inBoxAdmin1", IllegalArgumentException.class
			}
		};

		for (int i = 0; i < testingData.length; i++)
			this.templateUpdateMessage((String) testingData[i][0], (String) testingData[i][1], (String) testingData[i][2], (Class<?>) testingData[i][3]);
	}

	protected void templateUpdateMessage(String username, String messageRe, String boxRe, Class<?> expected) {

		Class<?> caught = null;

		try {

			//En cada iteraccion comenzamos una transaccion, de esya manera, no se toman valores residuales de otros test
			this.startTransaction();

			super.authenticate(username);

			Message message = new Message();
			Box box = new Box();

			message = this.messageService.findOne(super.getEntityId(messageRe));

			box = this.boxService.findOne(super.getEntityId(boxRe));

			this.messageService.updateMessage(message, box);

			this.messageService.flush();

			super.authenticate(null);

		} catch (Throwable oops) {
			caught = oops.getClass();
		} finally {
			//Se fuerza el rollback para que no de ningun problema la siguiente iteracion
			this.rollbackTransaction();
		}

		super.checkExceptions(expected, caught);

	}

}
