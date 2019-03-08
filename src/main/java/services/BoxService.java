
package services;

import java.util.ArrayList;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Validator;

import repositories.BoxRepository;
import security.LoginService;
import security.UserAccount;
import domain.Actor;
import domain.Box;
import domain.Message;

@Transactional
@Service
public class BoxService {

	@Autowired
	private BoxRepository	boxRepository;

	@Autowired
	private MessageService	messageService;

	@Autowired
	private ActorService	actorService;

	@Autowired
	private Validator		validator;


	public Box flushSave(Box box) {
		return this.boxRepository.saveAndFlush(box);
	}

	public Box create() {

		this.actorService.loggedAsActor();
		//UserAccount userAccount;
		//userAccount = LoginService.getPrincipal();
		//Actor actor = this.actorService.getActorByUsername(userAccount.getUsername());
		final Box box = new Box();
		final List<Message> messages = new ArrayList<Message>();

		box.setName("");
		box.setIsSystem(false);
		box.setMessages(messages);
		box.setFatherBox(null);

		//actor.getBoxes().add(box);
		return box;
	}

	public Box createSystem() {		//Crear cajas del sistema
		final Box box = new Box();
		final List<Message> messages = new ArrayList<Message>();

		box.setName("");
		box.setIsSystem(true);
		box.setMessages(messages);
		box.setFatherBox(null);

		return box;
	}

	public Box create(final String name, final Box fatherBox) {

		this.actorService.loggedAsActor();
		UserAccount userAccount;
		userAccount = LoginService.getPrincipal();
		final Actor actor = this.actorService.getActorByUsername(userAccount.getUsername());

		final Box box = new Box();
		final List<Message> messages = new ArrayList<Message>();
		box.setName(name);
		box.setIsSystem(false);
		box.setMessages(messages);
		box.setFatherBox(fatherBox);

		final List<Box> newBoxes = actor.getBoxes();
		newBoxes.add(box);
		actor.setBoxes(newBoxes);

		return box;
	}

	public Box saveSystem(final Box box) {
		return this.boxRepository.save(box);
	}

	public Box save(Box box) {
		Assert.isTrue(!box.getIsSystem());
		this.actorService.loggedAsActor();

		UserAccount userAccount;
		userAccount = LoginService.getPrincipal();
		final Actor actor = this.actorService.getActorByUsername(userAccount.getUsername());

		Box savedBox = new Box();
		savedBox = this.boxRepository.save(box);
		actor.getBoxes().add(savedBox);
		this.actorService.save(actor);

		return savedBox;
	}

	public Box updateBox(Box box) {
		this.actorService.loggedAsActor();
		UserAccount userAccount;
		userAccount = LoginService.getPrincipal();
		final Actor actor = this.actorService.getActorByUsername(userAccount.getUsername());

		Assert.isTrue(!box.getIsSystem());

		Box savedBox = new Box();

		savedBox = this.boxRepository.save(box);
		if (actor.getBoxes().contains(savedBox)) {
			actor.getBoxes().remove(savedBox);
			actor.getBoxes().add(savedBox);
		} else
			actor.getBoxes().add(savedBox);

		this.actorService.save(actor);
		return savedBox;
	}

	public void deleteBox(final Box box) {
		this.actorService.loggedAsActor();
		Assert.isTrue(!box.getIsSystem());
		UserAccount userAccount;
		userAccount = LoginService.getPrincipal();
		final Actor actor = this.actorService.getActorByUsername(userAccount.getUsername());

		final List<Box> sonBoxes = this.boxRepository.getSonsBox(box);
		if (sonBoxes.size() == 0) {
			for (final Message m : box.getMessages())
				this.messageService.delete(m);
			box.getMessages().removeAll(box.getMessages());

			actor.getBoxes().remove(box);
			this.boxRepository.delete(box);
			this.actorService.save(actor);

		} else
			for (final Box sonBox : sonBoxes)
				this.deleteBox(sonBox);
		// this.actorService.save(actor);

	}

	public List<Box> findAll() {
		return this.boxRepository.findAll();
	}

	public Box findOne(final int id) {
		return this.boxRepository.findOne(id);
	}

	public Box getRecievedBoxByActor(final Actor a) {
		return this.boxRepository.getRecievedBoxByActor(a);
	}

	public Box getSpamBoxByActor(final Actor a) {
		return this.boxRepository.getSpamBoxByActor(a);
	}

	public Box getTrashBoxByActor(final Actor a) {
		return this.boxRepository.getTrashBoxByActor(a);
	}

	public Box getNotificationBoxByActor(final Actor a) {
		return this.boxRepository.getNotificationBoxByActor(a);
	}

	public Box getSentBoxByActor(final Actor a) {
		return this.boxRepository.getSentBoxByActor(a);
	}

	public List<Box> getCurrentBoxByMessage(final Message m) {
		return this.boxRepository.getCurrentBoxByMessage(m);

	}

	public List<Integer> getActorBoxesId() {

		this.actorService.loggedAsActor();
		final List<Integer> idBoxes = new ArrayList<Integer>();

		UserAccount userAccount;
		userAccount = LoginService.getPrincipal();
		final Actor actor = this.actorService.getActorByUsername(userAccount.getUsername());
		for (int i = 0; i < actor.getBoxes().size(); i++)
			idBoxes.add(actor.getBoxes().get(i).getId());

		return idBoxes;
	}

	public List<Box> getActorBoxes() {
		this.actorService.loggedAsActor();
		UserAccount userAccount;
		userAccount = LoginService.getPrincipal();
		final Actor actor = this.actorService.getActorByUsername(userAccount.getUsername());
		return actor.getBoxes();
	}

	public Box reconstruct(Box box, BindingResult binding) {

		Box result;
		List<Message> messages = new ArrayList<Message>();
		Box pururu;

		if (box.getId() == 0) {
			result = box;
			result.setIsSystem(false);
			result.setMessages(messages);
		} else {
			pururu = this.boxRepository.findOne(box.getId());
			result = box;

			result.setIsSystem(pururu.getIsSystem());
			result.setMessages(pururu.getMessages());

		}

		this.validator.validate(result, binding);
		return result;

	}
}
