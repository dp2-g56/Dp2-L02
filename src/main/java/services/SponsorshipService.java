
package services;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Random;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Validator;

import domain.Actor;
import domain.Configuration;
import domain.CreditCard;
import domain.Message;
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
	@Autowired
	private MessageService messageService;
	@Autowired
	private ActorService actorService;

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
		spo.setSpentMoney(0f);

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
			spo.setSpentMoney(0f);
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

		CreditCard creditCard = sponsorship.getCreditCard();

		Assert.isTrue(cardType.contains(sponsorship.getCreditCard().getBrandName()));
		Assert.isTrue(!sponsorship.getParade().getIsDraftMode()
				&& sponsorship.getParade().getParadeStatus().equals(ParadeStatus.ACCEPTED));

		Assert.isTrue(this.creditCardService.validateNumberCreditCard(creditCard));
		Assert.isTrue(this.creditCardService.validateDateCreditCard(creditCard));

		result = this.save(sponsorship);

		if (sponsorship.getId() == 0) {
			List<Sponsorship> sps = sponsor.getSponsorships();
			sps.add(result);
			this.sponsorService.save(sponsor);
		}

		this.flush();

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
			if (!(this.creditCardService.validateNumberCreditCard(card)
					&& this.creditCardService.validateDateCreditCard(card)
					&& this.creditCardService.validateCvvCreditCard(card)) && s.getIsActivated()) {
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
			// sponsorships.get(0).setParade(null);
			// sponsorships.get(0).setCreditCard(null);
			this.deleteSponsorship(sponsorships.get(0));
	}

	public void deleteSponsorship(Sponsorship sponsorship) {
		Sponsor sponsor = this.sponsorService.loggedSponsor();

		sponsor.getSponsorships().remove(sponsorship);

		this.sponsorshipRepository.delete(sponsorship);
	}

	public List<Sponsorship> getSponsorshipsOfParade(int paradeId) {
		return this.sponsorshipRepository.getSponsorshipsOfParade(paradeId);
	}

	public List<Sponsorship> getActivatedSponsorshipsOfParade(int paradeId) {
		return this.sponsorshipRepository.getActivatedSponsorshipsOfParade(paradeId);
	}

	public Sponsorship getRandomSponsorship(int paradeId) {
		List<Sponsorship> sponsorships = this.getActivatedSponsorshipsOfParade(paradeId);

		Random random = new Random();
		if (sponsorships.size() == 0)
			return this.createSponsorship();
		else
			return sponsorships.get(random.nextInt(sponsorships.size()));
	}

	public void sendMessageToSponsor(Sponsor sponsor) {
		java.lang.Float amount = this.configurationService.getConfiguration().getFare()
				+ this.configurationService.getConfiguration().getFare()
						* this.configurationService.getConfiguration().getVAT() / 100;

		String subject = "New charge for advertising / Nuevo cargo por publicidad";

		String body = "You have paid: " + amount.toString()
				+ " euros for advertising one of your sponsorship / Has pagado: " + amount.toString()
				+ " euros por publicitar uno de tus patrocinios";

		Message message = this.messageService.createNotification(subject, body, "NEUTRAL", "Notification, Sponsorship",
				sponsor);

		this.messageService.sendMessageAnotherSender(message);

	}

	public void updateSpentMoneyOfSponsorship(int paradeId, int sponsorshipId) {
		Sponsor sponsor = this.getSponsorOfSponsorship(sponsorshipId);

		Assert.isTrue(sponsorshipId > 0 && paradeId > 0);

		Sponsorship sponsorship = this.findOne(sponsorshipId);

		Assert.isTrue(paradeId == sponsorship.getParade().getId());
		Assert.isTrue(sponsorship.getIsActivated());

		Configuration conf = this.configurationService.getConfiguration();
		java.lang.Float newSpentMoney = sponsorship.getSpentMoney() + conf.getFare()
				+ conf.getFare() * conf.getVAT() / 100;

		sponsorship.setSpentMoney(newSpentMoney);

		Actor actor = this.actorService.loggedActor();

		if (actor.getId() != sponsor.getId()) {
			this.save(sponsorship);
			this.sendMessageToSponsor(sponsor);
			this.flush();
		}
	}

}
