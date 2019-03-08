
package services;

import java.util.ArrayList;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import repositories.ActorRepository;
import security.Authority;
import security.LoginService;
import security.UserAccount;
import domain.Actor;
import domain.Box;
import domain.Message;
import domain.SocialProfile;

@Service
@Transactional
public class ActorService {

	@Autowired
	private ActorRepository			actorRepository;

	@Autowired
	private SocialProfileService	socialProfileService;

	@Autowired
	private ConfigurationService	configurationService;


	public Actor flushSave(final Actor actor) {
		return this.actorRepository.saveAndFlush(actor);
	}

	public List<Actor> findAll() {
		return this.actorRepository.findAll();
	}

	public List<Actor> findAllExceptAdmin() {

		List<Actor> actors = new ArrayList<Actor>();
		final List<Actor> actorsNoAdmin = new ArrayList<Actor>();

		actors = this.actorRepository.findAll();

		for (final Actor a : actors) {
			List<Authority> authorities = new ArrayList<Authority>();

			authorities = (List<Authority>) a.getUserAccount().getAuthorities();
			if (!(authorities.get(0).toString().equals("ADMIN")))
				actorsNoAdmin.add(a);
		}

		return actorsNoAdmin;
	}

	public Actor findOne(final int id) {
		return this.actorRepository.findOne(id);
	}

	public Actor getActorByUsername(final String a) {
		return this.actorRepository.getActorByUserName(a);
	}

	public List<Actor> getActors() {
		return this.actorRepository.getActors();
	}

	public void loggedAsActor() {
		UserAccount userAccount;
		userAccount = LoginService.getPrincipal();
		Assert.isTrue(userAccount.getAuthorities().size() > 0);
	}

	public Boolean loggedAsActorBolean() {
		Boolean res = false;
		UserAccount userAccount;
		userAccount = LoginService.getPrincipal();
		if (userAccount.getAuthorities().size() > 0)
			res = true;
		else
			res = false;
		return res;
	}

	public Actor createActor() {

		final Actor actor = new Actor();
		final List<SocialProfile> socialProfiles = new ArrayList<SocialProfile>();
		final List<Box> boxes = new ArrayList<Box>();

		final UserAccount userAccountActor = new UserAccount();

		userAccountActor.setUsername("");
		userAccountActor.setPassword("");

		final Box spamBox = new Box();
		final List<Message> messages1 = new ArrayList<>();
		spamBox.setIsSystem(true);
		spamBox.setMessages(messages1);
		spamBox.setName("Spam");

		final Box trashBox = new Box();
		final List<Message> messages2 = new ArrayList<>();
		trashBox.setIsSystem(true);
		trashBox.setMessages(messages2);
		trashBox.setName("Trash");

		final Box sentBox = new Box();
		final List<Message> messages3 = new ArrayList<>();
		sentBox.setIsSystem(true);
		sentBox.setMessages(messages3);
		sentBox.setName("Sent messages");

		final Box receivedBox = new Box();
		final List<Message> messages4 = new ArrayList<>();
		receivedBox.setIsSystem(true);
		receivedBox.setMessages(messages4);
		receivedBox.setName("Received messages");

		boxes.add(receivedBox);
		boxes.add(sentBox);
		boxes.add(spamBox);
		boxes.add(trashBox);

		actor.setName("");
		actor.setMiddleName("");
		actor.setSurname("");
		actor.setPhoto("");
		actor.setEmail("");
		actor.setPhoneNumber("");
		actor.setAddress("");
		actor.setSocialProfiles(socialProfiles);
		actor.setBoxes(boxes);
		actor.setUserAccount(userAccountActor);

		return actor;
	}

	public Actor createActor(final String name, final String middleName, final String surname, final String photo, final String email, final String phoneNumber, final String address, final String userName, final String password) {

		final Actor actor = new Actor();
		final List<SocialProfile> socialProfiles = new ArrayList<SocialProfile>();
		final List<Box> boxes = new ArrayList<Box>();

		final UserAccount userAccountActor = new UserAccount();

		userAccountActor.setUsername(userName);
		userAccountActor.setPassword(password);

		final Box spamBox = new Box();
		final List<Message> messages1 = new ArrayList<>();
		spamBox.setIsSystem(true);
		spamBox.setMessages(messages1);
		spamBox.setName("Spam");

		final Box trashBox = new Box();
		final List<Message> messages2 = new ArrayList<>();
		trashBox.setIsSystem(true);
		trashBox.setMessages(messages2);
		trashBox.setName("Trash");

		final Box sentBox = new Box();
		final List<Message> messages3 = new ArrayList<>();
		sentBox.setIsSystem(true);
		sentBox.setMessages(messages3);
		sentBox.setName("Sent messages");

		final Box receivedBox = new Box();
		final List<Message> messages4 = new ArrayList<>();
		receivedBox.setIsSystem(true);
		receivedBox.setMessages(messages4);
		receivedBox.setName("Received messages");

		boxes.add(receivedBox);
		boxes.add(sentBox);
		boxes.add(spamBox);
		boxes.add(trashBox);

		actor.setName(name);
		actor.setMiddleName(middleName);
		actor.setSurname(surname);
		actor.setPhoto(photo);
		actor.setEmail(email);
		actor.setPhoneNumber(phoneNumber);
		actor.setAddress(address);
		actor.setSocialProfiles(socialProfiles);
		actor.setBoxes(boxes);
		actor.setUserAccount(userAccountActor);

		return actor;
	}

	public Actor updateActor(final Actor actor, final String name, final String middleName, final String surname, final String photo, final String email, final String phoneNumber, final String address) {

		// LA COMPROBACION DE QUE ESTAS LOGUEADO SE HACE EN EL ACTOR
		actor.setName(name);
		actor.setMiddleName(middleName);
		actor.setSurname(surname);
		actor.setPhoto(photo);
		actor.setEmail(email);
		actor.setPhoneNumber(phoneNumber);
		actor.setAddress(address);

		this.actorRepository.save(actor);

		return actor;
	}

	public Actor save(final Actor a) {
		return this.actorRepository.save(a);
	}

	/*
	 * public Actor save(Actor actor) { Assert.isTrue(actor.getId() == 0 ||
	 * userAccount.equals(actor.getUserAccount()));
	 * this.actorRepository.save(actor); }
	 */

	public SocialProfile updateSocialProfiles(final SocialProfile socialProfile, final String nick, final String name, final String profileLink) {
		/*
		 * UserAccount userAccount; userAccount = LoginService.getPrincipal();
		 * Actor actor = new Actor(); actor =
		 * ActorService.getActorByUsername(userAccount.getUsername());
		 */

		// List<SocialProfile> socialProfiles = new ArrayList<SocialProfile>();
		// socialProfiles = actor.getSocialProfiles();

		// COMPROBAR EN CADA ACTOR QUE ES ESE EL ACTOR QUE ESTA LOGUEADO
		socialProfile.setName(name);
		socialProfile.setNick(nick);
		socialProfile.setProfileLink(profileLink);

		return this.socialProfileService.save(socialProfile);

	}

	/*
	 * 2. Browse the catalogue of tutorials in the system and display any of
	 * them. Note that actors must be able to see the profile of the
	 * corresponding handy workers, which includes his or her personal data and
	 * the list of tutorials that he or she’s written.
	 */

	public List<Box> getlistOfBoxes(final Actor actor) {

		return this.actorRepository.listOfBoxes(actor);
	}

	public void updateActorSpam(final Actor a) {

		if (this.configurationService.isActorSuspicious(a)) {
			a.setHasSpam(true);
			this.actorRepository.save(a);
		}
	}

	public Actor loggedActor() {
		Actor actor = new Actor();
		UserAccount userAccount;
		userAccount = LoginService.getPrincipal();
		actor = this.actorRepository.getActorByUserName(userAccount.getUsername());
		return actor;
	}

}
