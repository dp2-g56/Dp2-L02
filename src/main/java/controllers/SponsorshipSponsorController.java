
package controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import domain.Parade;
import forms.FormObjectSponsorshipCreditCard;
import services.ParadeService;

@Controller
@RequestMapping("/sponsorship/sponsor/")
public class SponsorshipSponsorController extends AbstractController {

	@Autowired
	private ParadeService paradeService;

	// Constructors -----------------------------------------------------------

	public SponsorshipSponsorController() {
		super();
	}

	// CREATE Sponsorship with Credit Card
	@RequestMapping(value = "/create", method = RequestMethod.GET)
	public ModelAndView createSponsorship(@RequestParam int paradeId) {
		ModelAndView result;
		FormObjectSponsorshipCreditCard formObject = new FormObjectSponsorshipCreditCard();

		Parade parade = this.paradeService.findOne(paradeId);

		result = new ModelAndView("sponsor/createSponsorship");
		result.addObject("formObject", formObject);
		result.addObject("parade", parade);

		return result;
	}

}
