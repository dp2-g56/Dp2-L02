
package services;

import java.util.ArrayList;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.encoding.Md5PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Validator;

import domain.Box;
import domain.SocialProfile;
import domain.Sponsor;
import domain.Sponsorship;
import forms.FormObjectSponsor;
import repositories.SponsorRepository;
import security.Authority;
import security.LoginService;
import security.UserAccount;

@Service
@Transactional
public class SponsorService {

	@Autowired
	private SponsorRepository SponsorRepository;

	@Autowired
	private BoxService boxService;

	@Autowired
	private Validator validator;

	public List<Sponsor> findAll() {
		return this.SponsorRepository.findAll();
	}

	public Sponsor save(Sponsor h) {
		return this.SponsorRepository.save(h);
	}

	public void delete(Sponsor h) {
		this.SponsorRepository.delete(h);
	}

	public Sponsor findOne(int id) {
		return this.SponsorRepository.findOne(id);
	}

	public Sponsor createSponsor() {
		Sponsor spo = new Sponsor();

		// Se crean las listas vacias

		List<SocialProfile> socialProfiles = new ArrayList<SocialProfile>();
		List<Sponsorship> sponsorships = new ArrayList<Sponsorship>();

		List<Box> boxes = new ArrayList<Box>();

		UserAccount userAccount = new UserAccount();
		List<Authority> authorities = new ArrayList<Authority>();
		userAccount.setUsername("");
		userAccount.setPassword("");

		Authority authority = new Authority();
		authority.setAuthority(Authority.SPONSOR);
		authorities.add(authority);
		userAccount.setAuthorities(authorities);
		userAccount.setIsNotLocked(true);

		spo.setName("");
		spo.setSurname("");

		spo.setSponsorships(sponsorships);

		spo.setPolarity(0);
		spo.setHasSpam(false);
		spo.setPhoto(null);
		spo.setPhoneNumber("");
		spo.setAddress("");

		spo.setBoxes(boxes);
		spo.setEmail("");

		spo.setMiddleName("");
		spo.setSocialProfiles(socialProfiles);

		spo.setUserAccount(userAccount);

		return spo;
	}

	public Sponsor saveCreate(Sponsor bro) {

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

		return this.SponsorRepository.save(bro);
	}

	public Sponsor reconstruct(FormObjectSponsor formObjectSponsor, BindingResult binding) {

		Sponsor result = new Sponsor();

		result.setAddress(formObjectSponsor.getAddress());
		// result.setBoxes(boxes);
		result.setEmail(formObjectSponsor.getEmail());
		// result.setEnrolments(enrolments)
		// result.setFinder(finder)
		result.setHasSpam(false);
		result.setMiddleName(formObjectSponsor.getMiddleName());
		result.setName(formObjectSponsor.getName());
		result.setPhoneNumber(formObjectSponsor.getPhoneNumber());
		result.setPhoto(formObjectSponsor.getPhoto());
		// result.setRequests(requests);
		// result.setPolarity(polarity);
		// result.setSocialProfiles(socialProfiles);
		result.setSurname(formObjectSponsor.getSurname());

		// USER ACCOUNT
		UserAccount userAccount = new UserAccount();

		// Authorities
		List<Authority> authorities = new ArrayList<Authority>();
		Authority authority = new Authority();
		authority.setAuthority(Authority.SPONSOR);
		authorities.add(authority);
		userAccount.setAuthorities(authorities);

		// locked
		userAccount.setIsNotLocked(true);

		// Username
		userAccount.setUsername(formObjectSponsor.getUsername());

		// Password
		Md5PasswordEncoder encoder;
		encoder = new Md5PasswordEncoder();
		userAccount.setPassword(encoder.encodePassword(formObjectSponsor.getPassword(), null));

		result.setUserAccount(userAccount);

		return result;
	}

	public void loggedAsSponsor() {
		UserAccount userAccount;
		userAccount = LoginService.getPrincipal();
		final List<Authority> authorities = (List<Authority>) userAccount.getAuthorities();
		Assert.isTrue(authorities.get(0).toString().equals("SPONSOR"));
	}

	public Sponsor loggedSponsor() {
		Sponsor brother = new Sponsor();
		UserAccount userAccount;
		userAccount = LoginService.getPrincipal();
		brother = this.SponsorRepository.getSponsorByUsername(userAccount.getUsername());
		return brother;
	}

	public Sponsor securityAndSponsor() {
		UserAccount userAccount = LoginService.getPrincipal();
		String username = userAccount.getUsername();

		Assert.notNull(this.SponsorRepository.getSponsorByUsername(username));

		Sponsor loggedSponsor = this.SponsorRepository.getSponsorByUsername(username);

		List<Authority> authorities = (List<Authority>) loggedSponsor.getUserAccount().getAuthorities();
		Assert.isTrue(authorities.get(0).toString().equals("SPONSOR"));

		return loggedSponsor;
	}

	public Sponsor updateSponsor(Sponsor Sponsor) {
		this.loggedAsSponsor();
		Assert.isTrue(Sponsor.getId() != 0);
		return this.SponsorRepository.save(Sponsor);
	}

	public Sponsor reconstructSponsor(Sponsor Sponsor, BindingResult binding) {

		Sponsor result;
		Sponsor pururu;

		result = Sponsor;
		pururu = this.SponsorRepository.findOne(Sponsor.getId());

		result.setUserAccount(pururu.getUserAccount());
		result.setBoxes(pururu.getBoxes());
		result.setHasSpam(pururu.getHasSpam());
		result.setSocialProfiles(pururu.getSocialProfiles());
		result.setPolarity(pururu.getPolarity());

		result.setSponsorships(pururu.getSponsorships());

		this.validator.validate(result, binding);

		return result;
	}

}
