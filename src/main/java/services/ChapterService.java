
package services;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.security.authentication.encoding.Md5PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

import repositories.ChapterRepository;
import security.Authority;
import security.UserAccount;
import domain.Area;
import domain.Box;
import domain.Chapter;
import domain.SocialProfile;
import forms.FormObjectChapter;

@Service
@Transactional
public class ChapterService {

	// Managed repository ------------------------------------------

	@Autowired
	private ChapterRepository	chapterRepository;

	@Autowired
	private BoxService			boxService;

	@Autowired
	private AreaService			areaService;


	// Simple CRUD methods ------------------------------------------

	public Chapter createChapter() {

		Chapter chapter = new Chapter();

		//Se crean las listas vacías
		//ACTOR
		List<SocialProfile> socialProfiles = new ArrayList<>();
		List<Box> boxes = new ArrayList<>();

		//CHAPTER
		//TODO Lista de Proclaim

		chapter.setArea(null);

		UserAccount userAccount = new UserAccount();
		userAccount.setUsername("");
		userAccount.setPassword("");

		//Actor
		chapter.setAddress("");
		chapter.setBoxes(boxes);
		chapter.setEmail("");
		chapter.setHasSpam(false);
		chapter.setMiddleName("");
		chapter.setName("");
		chapter.setPhoneNumber("");
		chapter.setPhoto("");
		chapter.setPolarity(0.0);
		chapter.setSocialProfiles(socialProfiles);
		chapter.setSurname("");

		List<Authority> authorities = new ArrayList<Authority>();

		Authority authority = new Authority();
		authority.setAuthority(Authority.CHAPTER);
		authorities.add(authority);

		userAccount.setAuthorities(authorities);
		userAccount.setIsNotLocked(true);

		chapter.setUserAccount(userAccount);

		return chapter;
	}

	public Chapter saveCreate(Chapter chapter) {

		List<Box> boxes = new ArrayList<>();

		//Se crean las listas vacías
		//ACTOR
		List<SocialProfile> socialProfiles = new ArrayList<>();
		chapter.setSocialProfiles(socialProfiles);

		//CHAPTER
		//TODO Lista de Proclaim

		//Boxes
		Box box1 = this.boxService.createSystem();
		box1.setName("INBOX");
		Box saved1 = this.boxService.saveSystem(box1);
		boxes.add(saved1);

		Box box2 = this.boxService.createSystem();
		box2.setName("OUTBOX");
		Box saved2 = this.boxService.saveSystem(box2);
		boxes.add(saved2);

		Box box3 = this.boxService.createSystem();
		box3.setName("TRASHBOX");
		Box saved3 = this.boxService.saveSystem(box3);
		boxes.add(saved3);

		Box box4 = this.boxService.createSystem();
		box4.setName("SPAMBOX");
		Box saved4 = this.boxService.saveSystem(box4);
		boxes.add(saved4);

		Box box5 = this.boxService.createSystem();
		box5.setName("NOTIFICATIONBOX");
		Box saved5 = this.boxService.saveSystem(box5);
		boxes.add(saved5);

		chapter.setBoxes(boxes);

		Chapter saved = new Chapter();
		Assert.isTrue(chapter.getArea() == null || this.listFreeAreas().contains(chapter.getArea()));
		Assert.isTrue(!chapter.getName().trim().isEmpty());
		Assert.isTrue(!chapter.getUserAccount().getUsername().trim().isEmpty());
		Assert.isTrue(!chapter.getTitle().trim().isEmpty());
		Assert.isTrue(!chapter.getSurname().trim().isEmpty());
		Assert.isTrue(chapter.getPhoto().isEmpty() || this.isUrl(chapter.getPhoto()));
		Assert.isTrue(chapter.getEmail().matches("[\\w.%-]+\\@[-.\\w]+\\.[A-Za-z]{2,4}|[\\w.%-]+\\<+[\\w.%-]+\\@[-.\\w]+\\.[A-Za-z]{2,4}|[\\w.%-]+\\<[\\w.%-]+\\@+\\>|[\\w.%-]+"));

		saved = this.chapterRepository.save(chapter);

		return saved;
	}
	public Chapter reconstruct(FormObjectChapter formObjectChapter, BindingResult binding) {

		Chapter result = new Chapter();

		result.setAddress(formObjectChapter.getAddress());
		result.setEmail(formObjectChapter.getEmail());
		result.setHasSpam(false);
		result.setArea(formObjectChapter.getArea());
		result.setMiddleName(formObjectChapter.getMiddleName());
		result.setName(formObjectChapter.getName());
		result.setPhoneNumber(formObjectChapter.getPhoneNumber());
		result.setPhoto(formObjectChapter.getPhoto());
		result.setSurname(formObjectChapter.getSurname());

		result.setTitle(formObjectChapter.getTitle());

		//USER ACCOUNT
		UserAccount userAccount = new UserAccount();

		//Authorities
		List<Authority> authorities = new ArrayList<Authority>();
		Authority authority = new Authority();
		authority.setAuthority(Authority.CHAPTER);
		authorities.add(authority);
		userAccount.setAuthorities(authorities);

		//locked
		userAccount.setIsNotLocked(true);

		//Username
		userAccount.setUsername(formObjectChapter.getUsername());

		//Password
		Md5PasswordEncoder encoder;
		encoder = new Md5PasswordEncoder();
		userAccount.setPassword(encoder.encodePassword(formObjectChapter.getPassword(), null));

		result.setUserAccount(userAccount);

		if (formObjectChapter.getEmail().matches("[\\w.%-]+\\<[\\w.%-]+\\@+\\>|[\\w.%-]+")) {
			if (LocaleContextHolder.getLocale().getLanguage().toUpperCase().contains("ES")) {
				binding.addError(new FieldError("chapter", "email", formObjectChapter.getEmail(), false, null, null, "No sigue el patron ejemplo@dominio.asd o alias <ejemplo@dominio.asd>"));
			} else {
				binding.addError(new FieldError("chapter", "email", formObjectChapter.getEmail(), false, null, null, "Dont follow the pattern example@domain.asd or alias <example@domain.asd>"));
			}
		}

		if (!formObjectChapter.getTermsAndConditions()) {
			if (LocaleContextHolder.getLocale().getLanguage().toUpperCase().contains("ES")) {
				binding.addError(new FieldError("formObjectChapter", "termsAndConditions", formObjectChapter.getTermsAndConditions(), false, null, null, "Debe aceptar los terminos y condiciones"));
			} else {
				binding.addError(new FieldError("formObjectChapter", "termsAndConditions", formObjectChapter.getTermsAndConditions(), false, null, null, "You must accept the terms and conditions"));
			}
		}

		if (!formObjectChapter.getPassword().equals(formObjectChapter.getConfirmPassword())) {
			if (LocaleContextHolder.getLocale().getLanguage().toUpperCase().contains("ES")) {
				binding.addError(new FieldError("formObjectChapter", "password", formObjectChapter.getPassword(), false, null, null, "Las contraseñas no coinciden"));
			} else {
				binding.addError(new FieldError("formObjectChapter", "password", formObjectChapter.getPassword(), false, null, null, "Passwords don't match"));
			}
		}

		return result;
	}
	public void flush() {
		this.chapterRepository.flush();
	}

	public List<Area> listFreeAreas() {
		return this.chapterRepository.getFreeAreas();
	}

	public List<Area> listOccupiedAreas() {

		List<Area> areas = this.areaService.findAll();
		areas.removeAll(this.chapterRepository.getFreeAreas());
		return areas;
	}
	public List<Chapter> findAll() {
		return this.chapterRepository.findAll();
	}

	public Chapter findOne(int id) {
		return this.chapterRepository.findOne(id);
	}

	public Chapter save(Chapter chapter) {
		return this.chapterRepository.save(chapter);
	}
	public void delete(Chapter chapter) {
		this.chapterRepository.delete(chapter);
	}

	public Boolean isUrl(String url) {
		try {
			new URL(url).toURI();
			return true;
		} catch (Exception e) {
			return false;
		}
	}

}
