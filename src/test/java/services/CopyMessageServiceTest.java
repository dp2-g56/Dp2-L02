
package services;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import utilities.AbstractTest;
import domain.Box;
import domain.Message;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
	"classpath:spring/junit.xml"
})
@Transactional
public class CopyMessageServiceTest extends AbstractTest {

	@Autowired
	private MessageService	messageService;

	@Autowired
	private BoxService		boxService;


	@Test
	public void driverCopyMessage() {

		Object testingData[][] = {
			{
				//Positive test, message in Inbox and you copy to Outbox
				"admin1", "message3", "outBoxAdmin1", null
			}, {
				//Negative test, copy message to a box is not yours
				"admin1", "message1", "inBoxAdmin2", IllegalArgumentException.class
			}
		};

		for (int i = 0; i < testingData.length; i++)
			this.templateCopyMessage((String) testingData[i][0], (String) testingData[i][1], (String) testingData[i][2], (Class<?>) testingData[i][3]);
	}

	protected void templateCopyMessage(String username, String messageRe, String boxRe, Class<?> expected) {

		Class<?> caught = null;

		try {

			//En cada iteraccion comenzamos una transaccion, de esya manera, no se toman valores residuales de otros test
			this.startTransaction();

			super.authenticate(username);

			Message message = new Message();
			Box box = new Box();

			message = this.messageService.findOne(super.getEntityId(messageRe));

			box = this.boxService.findOne(super.getEntityId(boxRe));

			this.messageService.copyMessage(message, box);

			this.messageService.flush();

			Assert.isTrue(box.getMessages().contains(message));

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
