
package services;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Validator;

import domain.CreditCard;
import domain.Parade;
import domain.ParadeStatus;
import domain.Sponsor;
import domain.Sponsorship;
import forms.FormObjectSponsorshipCreditCard;
import repositories.SponsorshipRepository;

@Service
@Transactional
public class SponsorshipService {

	@Autowired
	private SponsorshipRepository sponsorshipRepository;
	@Autowired
	private ConfigurationService configurationService;
	@Autowired
	private Validator validator;
	@Autowired
	private SponsorService sponsorService;
	@Autowired
	private CreditCardService creditCardService;
	@Autowired
	private AdminService adminService;

	public List<Sponsorship> findAll() {
		return this.sponsorshipRepository.findAll();
	}

	public Sponsorship save(Sponsorship h) {
		return this.sponsorshipRepository.save(h);
	}

	public void delete(Sponsorship h) {
		this.sponsorshipRepository.delete(h);
	}

	public Sponsorship findOne(int id) {
		return this.sponsorshipRepository.findOne(id);
	}

	public void flush() {
		this.sponsorshipRepository.flush();
	}

	public Sponsorship createSponsorship() {
		Sponsorship spo = new Sponsorship();

		spo.setBanner("");
		spo.setTargetURL("");
		spo.setGain(0f);

		CreditCard card = new CreditCard();

		card.setHolderName("");
		card.setBrandName("");

		spo.setCreditCard(card);

		return spo;
	}

	public Sponsorship reconstruct(FormObjectSponsorshipCreditCard formObject, BindingResult binding,
			CreditCard creditCard, Parade parade) {
		Sponsorship spo;
		if (formObject.getId() == 0 && parade != null) {
			spo = new Sponsorship();
			spo.setIsActivated(true);
			spo.setGain(0f);
			spo.setParade(parade);
		} else
			spo = this.findOne(formObject.getId());

		spo.setBanner(formObject.getBanner());
		spo.setTargetURL(formObject.getTargetURL());
		spo.setCreditCard(creditCard);

		// this.validator.validate(spo, binding);

		return spo;
	}

	public Sponsorship addOrUpdateSponsorship(Sponsorship sponsorship) {
		Sponsorship result;

		List<String> cardType = this.configurationService.getConfiguration().getCardType();
		Sponsor sponsor = this.sponsorService.securityAndSponsor();

		if (sponsorship.getId() > 0)
			Assert.isTrue(sponsor.equals(this.getSponsorOfSponsorship(sponsorship.getId())));

		Assert.isTrue(cardType.contains(sponsorship.getCreditCard().getBrandName()));
		Assert.isTrue(!sponsorship.getParade().getIsDraftMode()
				&& sponsorship.getParade().getParadeStatus().equals(ParadeStatus.ACCEPTED));
		Assert.isTrue(this.creditCardService.validateNumberCreditCard(sponsorship.getCreditCard()));
		Assert.isTrue(this.creditCardService.validateDateCreditCard(sponsorship.getCreditCard()));
		Assert.isTrue(this.creditCardService.validateCvvCreditCard(sponsorship.getCreditCard()));

		Sponsorship spon = this.save(sponsorship);
		result = spon;

		if (sponsorship.getId() == 0) {
			List<Sponsorship> sps = sponsor.getSponsorships();
			sps.add(result);
			this.sponsorService.save(sponsor);
		}

		return result;
	}

	public Collection<Sponsorship> getSponsorships(Boolean isActivated) {
		Sponsor sponsor = this.sponsorService.securityAndSponsor();

		Collection<Sponsorship> sponsorships;
		if (isActivated == null)
			sponsorships = this.sponsorshipRepository.getAllSponsorshipsOfSponsor(sponsor.getId());
		else if (isActivated == true)
			sponsorships = this.sponsorshipRepository.getActivatedSponsorshipsOfSponsor(sponsor.getId());
		else
			sponsorships = this.sponsorshipRepository.getDeactivatedSponsorshipsOfSponsor(sponsor.getId());

		return sponsorships;
	}

	public Collection<Sponsorship> getAllSponsorshipsOfSponsor(int sponsorId) {
		return this.sponsorshipRepository.getAllSponsorshipsOfSponsor(sponsorId);
	}

	public Collection<Sponsorship> getActivatedSponsorshipsOfSponsor(int sponsorId) {
		return this.sponsorshipRepository.getActivatedSponsorshipsOfSponsor(sponsorId);
	}

	public Collection<Sponsorship> getDeactivatedSponsorshipsOfSponsor(int sponsorId) {
		return this.sponsorshipRepository.getDeactivatedSponsorshipsOfSponsor(sponsorId);
	}

	public Collection<Sponsorship> getAllSponsorshipsAsAdmin() {
		return this.sponsorshipRepository.getAllSponsorshipsAsAdmin();
	}

	public Collection<Sponsorship> getActivatedSponsorshipsAsAdmin() {
		return this.sponsorshipRepository.getActivatedSponsorshipsAsAdmin();
	}

	public Collection<Sponsorship> getDeactivatedSponsorshipsAsAdmin() {
		return this.sponsorshipRepository.getDeactivatedSponsorshipsAsAdmin();
	}

	public Sponsor getSponsorOfSponsorship(int sponsorshipId) {
		return this.sponsorshipRepository.getSponsorOfSponsorship(sponsorshipId);
	}

	public void changeStatus(int sponsorshipId) {
		Sponsor loggedSponsor = this.sponsorService.securityAndSponsor();
		Sponsorship sponsorship = this.findOne(sponsorshipId);

		Assert.isTrue(loggedSponsor.equals(this.getSponsorOfSponsorship(sponsorship.getId())));

		if (sponsorship.getIsActivated() == true)
			sponsorship.setIsActivated(false);
		else {
			Assert.isTrue(this.creditCardService.validateNumberCreditCard(sponsorship.getCreditCard()));
			Assert.isTrue(this.creditCardService.validateDateCreditCard(sponsorship.getCreditCard()));
			Assert.isTrue(this.creditCardService.validateCvvCreditCard(sponsorship.getCreditCard()));
			sponsorship.setIsActivated(true);
		}

		this.save(sponsorship);
	}

	public Collection<Sponsorship> getSponsorshipsAsAdmin(Boolean isActivated) {
		this.adminService.loggedAsAdmin();

		Collection<Sponsorship> sponsorships;
		if (isActivated == null)
			sponsorships = this.sponsorshipRepository.getAllSponsorshipsAsAdmin();
		else if (isActivated == true)
			sponsorships = this.sponsorshipRepository.getActivatedSponsorshipsAsAdmin();
		else
			sponsorships = this.sponsorshipRepository.getDeactivatedSponsorshipsAsAdmin();

		return sponsorships;
	}

	public void checkAndDeactivateSponsorships() {
		this.adminService.loggedAsAdmin();

		Collection<Sponsorship> sponsorships = this.findAll();

		for (Sponsorship s : sponsorships) {
			CreditCard card = s.getCreditCard();
			if (!(this.creditCardService.validateNumberCreditCard(card) && this.creditCardService.validateDateCreditCard(card) && this.creditCardService.validateCvvCreditCard(card)) && s.getIsActivated()) {
				s.setIsActivated(false);
				this.save(s);
			}
		}
	}

	public void deleteAllSponsorships() {
		Sponsor sponsor = this.sponsorService.loggedSponsor();
		Integer cont = sponsor.getSponsorships().size();
		List<Sponsorship> sponsorships = new ArrayList<Sponsorship>();
		List<Sponsorship> deletedSponsorships = new ArrayList<Sponsorship>();
		sponsorships = sponsor.getSponsorships();

		for (int i = 0; i < cont; i++)
			//sponsorships.get(0).setParade(null);
			//sponsorships.get(0).setCreditCard(null);
			this.deleteSponsorship(sponsorships.get(0));
	}

	public void deleteSponsorship(Sponsorship sponsorship) {
		Sponsor sponsor = this.sponsorService.loggedSponsor();

		sponsor.getSponsorships().remove(sponsorship);

		this.sponsorshipRepository.delete(sponsorship);
	}
}
