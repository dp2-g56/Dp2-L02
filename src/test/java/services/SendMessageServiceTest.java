
package services;

import java.util.Date;

import javax.validation.ConstraintViolationException;

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
public class SendMessageServiceTest extends AbstractTest {

	@Autowired
	private MessageService	messageService;

	@Autowired
	private ActorService	actorService;


	@Test
	public void driverSendMessage() {

		Object testingData[][] = {
			{
				//Positive test
				"member1", "member1", "subject", "body", "HIGH", "tags", "admin1", null
			}, {
				//Positive test Blank tags
				"member1", "member1", "subject", "body", "HIGH", "", "admin1", null
			}, {
				//Negative test not logged
				"", "member1", "subject", "body", "HIGH", "tags", "admin1", IllegalArgumentException.class
			}, {
				//Negative test Blank subject
				"member1", "member1", "", "body", "HIGH", "tags", "admin1", ConstraintViolationException.class
			}, {
				//Negative test Blank body
				"member1", "member1", "subject", "", "HIGH", "tags", "admin1", ConstraintViolationException.class
			}, {
				//Negative test Blank priority
				"member1", "member1", "subject", "body", "", "tags", "admin1", ConstraintViolationException.class
			}, {
				//Negative test no receiver
				"member1", "member1", "subject", "body", "HIGH", "tags", "", NullPointerException.class
			}, {
				//Negative test Logged with 1 actor, trying to send message with blank actor in message
				"member1", "", "subject", "body", "HIGH", "tags", "admin1", IllegalArgumentException.class
			}, {
				//Negative test Logged with 1 actor, trying to send message with null actor in message
				"member1", null, "subject", "body", "HIGH", "tags", "admin1", IllegalArgumentException.class
			}, {
				//Negative test trying to send message to null actor
				"member1", "member1", "subject", "body", "HIGH", "tags", null, NullPointerException.class
			}, {
				//Negative test null subject
				"member1", "member1", null, "body", "HIGH", "tags", "admin1", NullPointerException.class
			}, {
				//Negative test null subject
				"member1", "member2", "subject", "body", "HIGH", "tags", "admin1", IllegalArgumentException.class
			}
		};

		for (int i = 0; i < testingData.length; i++)
			this.templateSendMessage((String) testingData[i][0], (String) testingData[i][1], (String) testingData[i][2], (String) testingData[i][3], (String) testingData[i][4], (String) testingData[i][5], (String) testingData[i][6],
				(Class<?>) testingData[i][7]);
	}

	protected void templateSendMessage(String username, String usernameVerification, String subject, String body, String priority, String tags, String receiver, Class<?> expected) {

		Class<?> caught = null;

		try {

			//En cada iteraccion comenzamos una transaccion, de esya manera, no se toman valores residuales de otros test
			this.startTransaction();

			super.authenticate(username);

			Date thisMoment = new Date();
			thisMoment.setTime(thisMoment.getTime() - 1000);

			Message message = this.messageService.create();
			Actor sender = this.actorService.getActorByUsername(usernameVerification);
			Actor receiverActor = this.actorService.getActorByUsername(receiver);

			message.setMoment(thisMoment);
			message.setSubject(subject);
			message.setBody(body);
			message.setPriority(priority);
			message.setReceiver(receiverActor);
			message.setTags(tags);
			message.setSender(sender);

			this.messageService.sendMessage(message);
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
