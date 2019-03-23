package services;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import domain.Finder;
import utilities.AbstractTest;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:spring/junit.xml" })
@Transactional
public class FinderServiceTest extends AbstractTest {

	@Autowired
	private FinderService finderService;
	@Autowired
	private MemberService memberService;

	/**
	 * We are going to test the Requirement R21
	 *
	 *
	 * R21. An actor who is authenticated as a member must be able to:
	 *
	 * 1. Manage his or her finder, which involves updating the search criteria,
	 * listing its contents, and clearing it.
	 *
	 **/

	@Test
	public void driverFinder() {

		Finder finderMember1 = this.memberService.getMemberByUsername("member1").getFinder();

		Object testingData[][] = {

				/**
				 * POSITIVE TEST: Member is filter his Finder
				 **/

				{ "member1", finderMember1, "parade1", null }

				/**
				 * NEGATIVE TEST: Member2 is trying to filter the member1's finder
				 **/
				, { "member2", finderMember1, "parade1", IllegalArgumentException.class }

				/**
				 * NEGATIVE TEST: Brotherhood1 is trying to filter the member1's finder, he is
				 * not a member
				 **/
				, { "brotherhood1", finderMember1, "parade1", IllegalArgumentException.class } };

		for (int i = 0; i < testingData.length; i++)
			this.finderTemplate((String) testingData[i][0], (Finder) testingData[i][1], (String) testingData[i][2],
					(Class<?>) testingData[i][3]);

	}

	private void finderTemplate(String member, Finder finder, String keyWord, Class<?> expected) {

		Class<?> caught = null;

		try {
			super.authenticate(member);
			finder.setKeyWord(keyWord);
			this.finderService.filterParadesByFinder(finder);
		} catch (Throwable oops) {
			caught = oops.getClass();
		}

		super.checkExceptions(expected, caught);

	}

}
