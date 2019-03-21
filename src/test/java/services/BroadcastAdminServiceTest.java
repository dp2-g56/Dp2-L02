
package services;

import java.util.Date;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import utilities.AbstractTest;
import domain.Actor;
import domain.Message;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
	"classpath:spring/junit.xml"
})
@Transactional
public class BroadcastAdminServiceTest extends AbstractTest {

	@Autowired
	private MessageService	messageService;

	@Autowired
	private AdminService	adminService;

	@Autowired
	private ActorService	actorService;


	@Test
	public void driverUpdateMessage() {

		Object testingData[][] = {
			{
				//Positive test
				"admin1", "admin1", "subject", "body", "HIGH", "tags", null
			}, {
				//Positive test Blank tags
				"member1", "member1", "subject", "body", "HIGH", "", IllegalArgumentException.class
			}, {
				//Negative test not logged
				"", "member1", "subject", "body", "HIGH", "tags", IllegalArgumentException.class
			}
		};

		for (int i = 0; i < testingData.length; i++)
			this.templateSendMessage((String) testingData[i][0], (String) testingData[i][1], (String) testingData[i][2], (String) testingData[i][3], (String) testingData[i][4], (String) testingData[i][5], (Class<?>) testingData[i][6]);
	}

	protected void templateSendMessage(String username, String usernameVerification, String subject, String body, String priority, String tags, Class<?> expected) {

		Class<?> caught = null;

		try {

			//En cada iteraccion comenzamos una transaccion, de esya manera, no se toman valores residuales de otros test
			this.startTransaction();

			super.authenticate(username);

			Date thisMoment = new Date();
			thisMoment.setTime(thisMoment.getTime() - 1000);

			Message message = this.messageService.create();
			Actor sender = this.actorService.getActorByUsername(usernameVerification);
			Actor receiverActor = this.actorService.getActorByUsername(usernameVerification);

			message.setMoment(thisMoment);
			message.setSubject(subject);
			message.setBody(body);
			message.setPriority(priority);
			message.setReceiver(receiverActor);
			message.setTags(tags);
			message.setSender(sender);

			this.adminService.broadcastMessage(message);
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
