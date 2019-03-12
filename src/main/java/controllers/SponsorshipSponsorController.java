
package controllers;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import domain.CreditCard;
import domain.Parade;
import domain.Sponsor;
import domain.Sponsorship;
import forms.FormObjectSponsorshipCreditCard;
import services.CreditCardService;
import services.ParadeService;
import services.SponsorService;
import services.SponsorshipService;

@Controller
@RequestMapping("/sponsorship/sponsor/")
public class SponsorshipSponsorController extends AbstractController {

	@Autowired
	private ParadeService paradeService;
	@Autowired
	private SponsorshipService sponsorshipService;
	@Autowired
	private CreditCardService creditCardService;
	@Autowired
	private SponsorService sponsorService;

	// Constructors -----------------------------------------------------------

	public SponsorshipSponsorController() {
		super();
	}

	// CREATE Sponsorship with Credit Card
	@RequestMapping(value = "/create", method = RequestMethod.GET)
	public ModelAndView createSponsorship(@RequestParam int paradeId) {
		ModelAndView result;
		FormObjectSponsorshipCreditCard formObject = new FormObjectSponsorshipCreditCard();

		result = new ModelAndView("sponsor/createSponsorship");
		result.addObject("formObject", formObject);
		result.addObject("paradeId", paradeId);

		return result;
	}

	@RequestMapping(value = "/create", method = RequestMethod.POST, params = "save")
	public ModelAndView saveSponsorship(@ModelAttribute("formObject") @Valid FormObjectSponsorshipCreditCard formObject,
			BindingResult binding, @RequestParam int paradeId) {
		ModelAndView result;

		Sponsorship sponsorship = new Sponsorship();
		CreditCard creditCard = new CreditCard();

		Parade parade = this.paradeService.findOne(paradeId);

		creditCard = this.creditCardService.reconstruct(formObject, binding);
		sponsorship = this.sponsorshipService.reconstruct(formObject, binding, creditCard, parade);

		if (binding.hasErrors())
			result = this.createEditModelAndView("sponsor/createSponsorship", formObject, paradeId);
		else
			try {
				Assert.isTrue(this.creditCardService.validateCreditCard(creditCard));

				Sponsorship spon = this.sponsorshipService.save(sponsorship);

				Sponsor sponsor = this.sponsorService.securityAndSponsor();
				List<Sponsorship> sps = sponsor.getSponsorships();
				sps.add(spon);
				this.sponsorService.save(sponsor);

				result = new ModelAndView("redirect:/parade/sponsor/list.do");
			} catch (Throwable oops) {
				result = this.createEditModelAndView("sponsor/createSponsorship", formObject, paradeId,
						"sponsorship.commit.error");
			}

		return result;
	}

	private ModelAndView createEditModelAndView(String tiles, FormObjectSponsorshipCreditCard formObject,
			int paradeId) {
		ModelAndView result;

		result = new ModelAndView(tiles);
		result.addObject("formObject", formObject);
		result.addObject("paradeId", paradeId);

		return result;
	}

	private ModelAndView createEditModelAndView(String tiles, FormObjectSponsorshipCreditCard formObject, int paradeId,
			String message) {

		ModelAndView result = this.createEditModelAndView(tiles, formObject, paradeId);
		result.addObject("message", message);

		return result;
	}

}
