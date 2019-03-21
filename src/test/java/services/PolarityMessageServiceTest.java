
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
import domain.Message;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
	"classpath:spring/junit.xml"
})
@Transactional
public class PolarityMessageServiceTest extends AbstractTest {

	@Autowired
	private MessageService	messageService;

	@Autowired
	private ActorService	actorService;

	@Autowired
	private BoxService		boxService;


	@Test
	public void driverPolarity() {

		Object testingData[][] = {
			{
				//Positive test, bad polarity
				"member1", "member1", "subject", "no", "HIGH", "tags", "admin1", null
			}, {
				//Positive test, bad polarity
				"member1", "member1", "subject", "good", "HIGH", "tags", "admin1", null
			}, {
				//Positive test, bad polarity
				"member1", "member1", "subject", "body", "HIGH", "tags", "admin1", IllegalArgumentException.class
			}
		};

		for (int i = 0; i < testingData.length; i++)
			this.templatePolarity((String) testingData[i][0], (String) testingData[i][1], (String) testingData[i][2], (String) testingData[i][3], (String) testingData[i][4], (String) testingData[i][5], (String) testingData[i][6],
				(Class<?>) testingData[i][7]);
	}

	protected void templatePolarity(String username, String usernameVerification, String subject, String body, String priority, String tags, String receiver, Class<?> expected) {

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

			Assert.isTrue(sender.getPolarity() != 0);

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
