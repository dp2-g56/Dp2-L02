
package services;

import java.util.Date;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import utilities.AbstractTest;
import domain.Actor;
import domain.Box;
import domain.Message;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
	"classpath:spring/junit.xml"
})
@Transactional
public class SpamMessageServiceTest extends AbstractTest {

	@Autowired
	private MessageService	messageService;

	@Autowired
	private ActorService	actorService;

	@Autowired
	private BoxService		boxService;


	@Test
	public void driverSpam() {

		Object testingData[][] = {
			{
				//Positive test, SPAM in subject
				"member1", "member1", "nigeria", "body", "HIGH", "tags", "admin1", null
			}, {
				//Positive test, SPAM in body
				"member1", "member1", "subject", "nigeria", "HIGH", "tags", "admin1", null
			}, {
				//Positive test, SPAM in tags
				"member1", "member1", "subject", "body", "HIGH", "nigeria", "admin1", null
			}, {
				//Negative test, no spam words
				"member1", "member1", "subject", "body", "HIGH", "tags", "admin1", IllegalArgumentException.class
			}
		};

		for (int i = 0; i < testingData.length; i++)
			this.templateSpam((String) testingData[i][0], (String) testingData[i][1], (String) testingData[i][2], (String) testingData[i][3], (String) testingData[i][4], (String) testingData[i][5], (String) testingData[i][6], (Class<?>) testingData[i][7]);
	}

	protected void templateSpam(String username, String usernameVerification, String subject, String body, String priority, String tags, String receiver, Class<?> expected) {

		Class<?> caught = null;

		try {

			//En cada iteraccion comenzamos una transaccion, de esya manera, no se toman valores residuales de otros test
			this.startTransaction();

			super.authenticate(username);

			Date thisMoment = new Date();
			thisMoment.setTime(thisMoment.getTime() - 1000);
			Box spamBoxReceiver = new Box();

			Message message = this.messageService.create();
			Actor sender = this.actorService.getActorByUsername(usernameVerification);
			Actor receiverActor = this.actorService.getActorByUsername(receiver);

			spamBoxReceiver = this.boxService.getSpamBoxByActor(receiverActor);
			Integer numMessages = spamBoxReceiver.getMessages().size();

			message.setMoment(thisMoment);
			message.setSubject(subject);
			message.setBody(body);
			message.setPriority(priority);
			message.setReceiver(receiverActor);
			message.setTags(tags);
			message.setSender(sender);

			this.messageService.sendMessage(message);
			this.messageService.flush();
			Integer numMessagesSentSpam = spamBoxReceiver.getMessages().size();

			Assert.isTrue(numMessagesSentSpam != numMessages);

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
