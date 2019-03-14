
package services;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.security.authentication.encoding.Md5PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Validator;

import domain.Box;
import domain.Brotherhood;
import domain.Enrolment;
import domain.Float;
import domain.Member;
import domain.Parade;
import domain.SocialProfile;
import domain.StatusEnrolment;
import forms.FormObjectBrotherhood;
import repositories.BrotherhoodRepository;
import security.Authority;
import security.LoginService;
import security.UserAccount;

@Service
@Transactional
public class BrotherhoodService {

	@Autowired
	private BrotherhoodRepository brotherhoodRepository;

	@Autowired
	private BoxService boxService;

	@Autowired
	private Validator validator;

	public List<Brotherhood> findAll() {
		return this.brotherhoodRepository.findAll();
	}

	public Brotherhood save(Brotherhood h) {
		return this.brotherhoodRepository.save(h);
	}

	public void delete(Brotherhood h) {
		this.brotherhoodRepository.delete(h);
	}

	public Brotherhood findOne(int id) {
		return this.brotherhoodRepository.findOne(id);
	}

	public List<Float> getFloatsByBrotherhood(Brotherhood b) {
		return this.brotherhoodRepository.getFloatsByBrotherhood(b.getId());
	}

	public List<Parade> getParadesByBrotherhood(Brotherhood b) {
		return this.brotherhoodRepository.getParadesByBrotherhood(b.getId());
	}

	public List<Parade> getParadesByBrotherhoodFinal(Brotherhood b) {
		return this.brotherhoodRepository.getParadesByBrotherhoodFinal(b.getId());
	}

	public Brotherhood createBrotherhood() {
		Brotherhood bro = new Brotherhood();

		// Se crean las listas vacias
		List<String> pictures = new ArrayList<String>();
		List<SocialProfile> socialProfiles = new ArrayList<SocialProfile>();
		List<Enrolment> enrolments = new ArrayList<Enrolment>();
		List<Box> boxes = new ArrayList<Box>();
		List<Parade> parades = new ArrayList<Parade>();
		List<Float> floats = new ArrayList<Float>();

		UserAccount userAccount = new UserAccount();
		List<Authority> authorities = new ArrayList<Authority>();
		userAccount.setUsername("");
		userAccount.setPassword("");

		Authority authority = new Authority();
		authority.setAuthority(Authority.BROTHERHOOD);
		authorities.add(authority);
		userAccount.setAuthorities(authorities);
		userAccount.setIsNotLocked(true);

		Date establishmentDate = new Date();
		establishmentDate.setTime(establishmentDate.getTime() - 1000);

		bro.setName("");
		bro.setSurname("");
		bro.setEstablishmentDate(establishmentDate);
		bro.setPictures(pictures);
		bro.setPolarity(0);
		bro.setHasSpam(false);
		bro.setPhoto(null);
		bro.setPhoneNumber("");
		bro.setAddress("");
		bro.setArea(null);
		bro.setBoxes(boxes);
		bro.setEmail("");
		bro.setEnrolments(enrolments);
		bro.setMiddleName("");
		bro.setSocialProfiles(socialProfiles);
		bro.setTitle("");
		bro.setUserAccount(userAccount);
		bro.setParades(parades);
		bro.setFloats(floats);

		return bro;
	}

	public Brotherhood saveCreate(Brotherhood bro) {

		// SOCIAL PROFILES
		List<SocialProfile> socialProfiles = new ArrayList<>();
		bro.setSocialProfiles(socialProfiles);

		// BOXES
		List<Box> boxes = new ArrayList<>();
		Box box1 = this.boxService.createSystem();
		box1.setName("SPAMBOX");
		Box saved1 = this.boxService.saveSystem(box1);
		boxes.add(saved1);

		Box box2 = this.boxService.createSystem();
		box2.setName("TRASHBOX");
		Box saved2 = this.boxService.saveSystem(box2);
		boxes.add(saved2);

		Box box3 = this.boxService.createSystem();
		box3.setName("OUTBOX");
		Box saved3 = this.boxService.saveSystem(box3);
		boxes.add(saved3);

		Box box4 = this.boxService.createSystem();
		box4.setName("NOTIFICATIONBOX");
		Box saved4 = this.boxService.saveSystem(box4);
		boxes.add(saved4);

		Box box5 = this.boxService.createSystem();
		box5.setName("INBOX");
		Box saved5 = this.boxService.saveSystem(box5);
		boxes.add(saved5);

		bro.setBoxes(boxes);

		// ENROLMENTS
		List<Enrolment> enrolments = new ArrayList<>();
		bro.setEnrolments(enrolments);

		// Establishment Date
		Date date = new Date();
		date.setTime(date.getTime() - 1);
		bro.setEstablishmentDate(date);

		// Pictures
		List<String> pictures = new ArrayList<>();
		bro.setPictures(pictures);

		// Floats
		List<domain.Float> floats = new ArrayList<>();
		bro.setFloats(floats);

		// Parades
		List<Parade> parades = new ArrayList<>();
		bro.setParades(parades);

		return this.brotherhoodRepository.save(bro);
	}

	public Brotherhood reconstruct(FormObjectBrotherhood formObjectBrotherhood, BindingResult binding) {

		Brotherhood result = new Brotherhood();

		result.setAddress(formObjectBrotherhood.getAddress());
		// result.setBoxes(boxes);
		result.setEmail(formObjectBrotherhood.getEmail());
		// result.setEnrolments(enrolments)
		// result.setFinder(finder)
		result.setHasSpam(false);
		result.setMiddleName(formObjectBrotherhood.getMiddleName());
		result.setName(formObjectBrotherhood.getName());
		result.setPhoneNumber(formObjectBrotherhood.getPhoneNumber());
		result.setPhoto(formObjectBrotherhood.getPhoto());
		// result.setRequests(requests);
		// result.setPolarity(polarity);
		// result.setSocialProfiles(socialProfiles);
		result.setSurname(formObjectBrotherhood.getSurname());

		result.setTitle(formObjectBrotherhood.getTitle());

		// USER ACCOUNT
		UserAccount userAccount = new UserAccount();

		// Authorities
		List<Authority> authorities = new ArrayList<Authority>();
		Authority authority = new Authority();
		authority.setAuthority(Authority.BROTHERHOOD);
		authorities.add(authority);
		userAccount.setAuthorities(authorities);

		// locked
		userAccount.setIsNotLocked(true);

		// Username
		userAccount.setUsername(formObjectBrotherhood.getUsername());

		// Password
		Md5PasswordEncoder encoder;
		encoder = new Md5PasswordEncoder();
		userAccount.setPassword(encoder.encodePassword(formObjectBrotherhood.getPassword(), null));

		result.setUserAccount(userAccount);

		return result;
	}

	public void loggedAsBrotherhood() {
		UserAccount userAccount;
		userAccount = LoginService.getPrincipal();
		final List<Authority> authorities = (List<Authority>) userAccount.getAuthorities();
		Assert.isTrue(authorities.get(0).toString().equals("BROTHERHOOD"));
	}

	public Brotherhood loggedBrotherhood() {
		Brotherhood brother = new Brotherhood();
		UserAccount userAccount;
		userAccount = LoginService.getPrincipal();
		brother = this.brotherhoodRepository.getBrotherhoodByUsername(userAccount.getUsername());
		return brother;
	}

	public Brotherhood securityAndBrotherhood() {
		UserAccount userAccount = LoginService.getPrincipal();
		String username = userAccount.getUsername();

		Brotherhood loggedBrotherhood = this.brotherhoodRepository.getBrotherhoodByUsername(username);
		List<Authority> authorities = (List<Authority>) loggedBrotherhood.getUserAccount().getAuthorities();
		Assert.isTrue(authorities.get(0).toString().equals("BROTHERHOOD"));

		return loggedBrotherhood;
	}

	public Boolean hasArea(Brotherhood brotherhood) {
		try {
			Assert.notNull(brotherhood.getArea());
			return true;
		} catch (Throwable oops) {
			return false;
		}
	}

	public Brotherhood reconstructArea(Brotherhood brotherhood, BindingResult binding) {
		Brotherhood result;

		if (brotherhood.getId() == 0)
			result = brotherhood;
		else {
			result = this.brotherhoodRepository.findOne(brotherhood.getId());

			result.setArea(brotherhood.getArea());

			this.validator.validate(result, binding);
		}

		return result;
	}

	public Brotherhood updateBrotherhood(Brotherhood brotherhood) {
		this.loggedAsBrotherhood();
		Assert.isTrue(brotherhood.getId() != 0);
		return this.brotherhoodRepository.save(brotherhood);
	}

	public List<Member> getMembersOfBrotherhood() {
		Brotherhood bro = new Brotherhood();
		bro = this.loggedBrotherhood();
		List<Member> members = new ArrayList<Member>();
		List<Enrolment> enrolmentsBro = bro.getEnrolments();
		for (Enrolment e : enrolmentsBro)
			if (e.getStatusEnrolment() == StatusEnrolment.ACCEPTED)
				members.add(e.getMember());
		return members;
	}

	public List<String> getPositions() {
		String locale = LocaleContextHolder.getLocale().getLanguage().toUpperCase();
		Brotherhood bro = new Brotherhood();
		bro = this.loggedBrotherhood();
		List<String> positions = new ArrayList<String>();
		List<Enrolment> enrolmentsBro = bro.getEnrolments();
		for (Enrolment e : enrolmentsBro)
			if (e.getStatusEnrolment() == StatusEnrolment.ACCEPTED)
				if (locale.contains("EN"))
					positions.add(e.getPosition().getTitleEnglish());
				else if (locale.contains("ES"))
					positions.add(e.getPosition().getTitleSpanish());
		return positions;
	}

	public Enrolment getEnrolment(Member m) {
		Enrolment en = null;
		Brotherhood bro = this.loggedBrotherhood();
		List<Enrolment> broEn = bro.getEnrolments();
		List<Enrolment> memEn = m.getEnrolments();
		broEn.retainAll(memEn);
		for (Enrolment e : broEn)
			if (e.getStatusEnrolment() == StatusEnrolment.ACCEPTED) {
				en = e;
				break;
			}
		return en;
	}

	public List<Enrolment> getPengingEnrolments() {
		Brotherhood bro = this.loggedBrotherhood();
		List<Enrolment> enrolments = bro.getEnrolments();
		Assert.notNull(bro);
		List<Enrolment> res = new ArrayList<Enrolment>();
		for (Enrolment e : enrolments)
			if (e.getStatusEnrolment() == StatusEnrolment.PENDING)
				res.add(e);
		return res;
	}

	public List<Member> getMembersOfBrotherhood(Brotherhood bro) {

		List<Member> members = new ArrayList<Member>();
		List<Enrolment> enrolmentsBro = bro.getEnrolments();
		for (Enrolment e : enrolmentsBro)
			if (e.getStatusEnrolment() == StatusEnrolment.ACCEPTED)
				members.add(e.getMember());
		return members;
	}

	public Brotherhood reconstructBrotherhood(Brotherhood brotherhood, BindingResult binding) {

		Brotherhood result;
		Brotherhood pururu;

		result = brotherhood;
		pururu = this.brotherhoodRepository.findOne(brotherhood.getId());

		result.setUserAccount(pururu.getUserAccount());
		result.setBoxes(pururu.getBoxes());
		result.setHasSpam(pururu.getHasSpam());
		result.setSocialProfiles(pururu.getSocialProfiles());
		result.setPolarity(pururu.getPolarity());
		result.setEstablishmentDate(pururu.getEstablishmentDate());

		result.setEnrolments(pururu.getEnrolments());
		result.setFloats(pururu.getFloats());
		result.setArea(pururu.getArea());
		result.setParades(pururu.getParades());
		result.setPictures(pururu.getPictures());

		this.validator.validate(result, binding);

		return result;
	}

	public Brotherhood addPicture(String picture, Brotherhood brotherhood) {
		this.loggedAsBrotherhood();
		brotherhood.getPictures().add(picture);
		return this.save(brotherhood);
	}
}
