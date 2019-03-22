
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
public class DeleteMessageServiceTest extends AbstractTest {

	@Autowired
	private MessageService	messageService;

	@Autowired
	private BoxService		boxService;

	@Autowired
	private ActorService	actorService;


	@Test
	public void driverDeleteMessage() {

		Object testingData[][] = {
			{
				//Positive test, message in the INBOX and it goes to the trashBox
				"admin1", "message3", null
			}, {
				//Positive test, message in the TrashBox and is deleted
				"admin1", "message1", null
			}, {
				//Negative test, not logged
				"", "message1", IllegalArgumentException.class
			}, {
				//Negative test, deleting a message that is not yours
				"admin1", "message2", IllegalArgumentException.class
			}
		};

		for (int i = 0; i < testingData.length; i++)
			this.templateDeleteMessage((String) testingData[i][0], (String) testingData[i][1], (Class<?>) testingData[i][2]);
	}

	protected void templateDeleteMessage(String username, String messageRe, Class<?> expected) {

		Class<?> caught = null;

		try {

			//En cada iteraccion comenzamos una transaccion, de esya manera, no se toman valores residuales de otros test
			this.startTransaction();

			super.authenticate(username);

			Message message = new Message();
			Message copyMessage = new Message();
			Box thrashBox = new Box();

			message = this.messageService.findOne(super.getEntityId(messageRe));
			copyMessage = message;

			thrashBox = this.boxService.getTrashBoxByActor(this.actorService.loggedActor());

			if (thrashBox.getMessages().contains(copyMessage)) {
				this.messageService.deleteMessageToTrashBox(message);
				this.messageService.flush();
				Assert.isTrue(!(thrashBox.getMessages().contains(message)));
			} else {
				this.messageService.deleteMessageToTrashBox(message);
				this.messageService.flush();
				Assert.isTrue(thrashBox.getMessages().contains(message));
			}

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
