
package services;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import utilities.AbstractTest;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:spring/junit.xml" })
@Transactional
public class DeleteMemberServiceTest extends AbstractTest {

	@Autowired
	private MemberService memberService;
	@Autowired
	private ActorService actorService;

	/**
	 * Test the member deletion method
	 */
	@Test
	public void driverDeleteMember() {

		Object testingData[][] = {
				// Positive case
				{ "member1", null },
				// Negative case: Trying to delete an user with a different role
				{ "sponsor1", NullPointerException.class } };

		for (int i = 0; i < testingData.length; i++)
			this.templateDeleteMember((String) testingData[i][0], (Class<?>) testingData[i][1]);
	}

	private void templateDeleteMember(String username, Class<?> expected) {

		Class<?> caught = null;

		try {
			this.startTransaction();
			super.authenticate(username);
			Assert.notNull(this.actorService.getActorByUsername(username));
			this.memberService.deleteLoggedMember();
			Assert.isNull(this.actorService.getActorByUsername(username));
			super.unauthenticate();
			this.memberService.flush();
		} catch (Throwable oops) {
			caught = oops.getClass();
		} finally {
			this.rollbackTransaction();
		}

		super.checkExceptions(expected, caught);

	}

}
