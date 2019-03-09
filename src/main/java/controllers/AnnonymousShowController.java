
package controllers;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import services.BrotherhoodService;
import services.FloatService;
import domain.Brotherhood;
import domain.Member;
import domain.Parade;

@Controller
@RequestMapping("/showAll/annonymous")
public class AnnonymousShowController extends AbstractController {

	@Autowired
	private BrotherhoodService	brotherhoodService;

	@Autowired
	private FloatService		floatService;


	@RequestMapping(value = "/brotherhood/list", method = RequestMethod.GET)
	public ModelAndView listBrotherhood() {
		ModelAndView result;
		List<Brotherhood> brotherhoods = new ArrayList<Brotherhood>();
		brotherhoods = this.brotherhoodService.findAll();

		result = new ModelAndView("showAll/annonymous/brotherhood/list");

		result.addObject("brotherhoods", brotherhoods);
		result.addObject("requestURI", "showAll/annonymous/brotherhood/list.do");
		return result;
	}

	@RequestMapping(value = "/parade/list", method = RequestMethod.GET)
	public ModelAndView listParades(@RequestParam int brotherhoodId) {
		ModelAndView result;
		Brotherhood brotherhood = new Brotherhood();
		List<Parade> parades = new ArrayList<Parade>();

		brotherhood = this.brotherhoodService.findOne(brotherhoodId);
		parades = this.brotherhoodService.getParadesByBrotherhoodFinal(brotherhood);
		result = new ModelAndView("showAll/annonymous/parade/list");

		result.addObject("parades", parades);
		result.addObject("requestURI", "showAll/annonymous/parade/list.do");
		return result;
	}

	@RequestMapping(value = "/member/list", method = RequestMethod.GET)
	public ModelAndView listMember(@RequestParam int brotherhoodId) {
		ModelAndView result;
		Brotherhood brotherhood = new Brotherhood();
		List<Member> members = new ArrayList<Member>();

		brotherhood = this.brotherhoodService.findOne(brotherhoodId);
		members = this.brotherhoodService.getMembersOfBrotherhood(brotherhood);
		result = new ModelAndView("showAll/annonymous/member/list");

		result.addObject("members", members);
		result.addObject("requestURI", "showAll/annonymous/member/list.do");
		return result;
	}

	@RequestMapping(value = "/float/list", method = RequestMethod.GET)
	public ModelAndView listFloat(@RequestParam int brotherhoodId) {
		ModelAndView result;
		Brotherhood brotherhood = new Brotherhood();
		List<domain.Float> floats = new ArrayList<domain.Float>();

		brotherhood = this.brotherhoodService.findOne(brotherhoodId);
		floats = this.brotherhoodService.getFloatsByBrotherhood(brotherhood);
		result = new ModelAndView("showAll/annonymous/float/list");

		result.addObject("floats", floats);
		result.addObject("requestURI", "showAll/annonymous/float/list.do");
		return result;
	}

	@RequestMapping(value = "/picture/list", method = RequestMethod.GET)
	public ModelAndView listPicturesFloat(@RequestParam int floatId) {

		ModelAndView result;

		List<String> pictures;

		domain.Float floatt;

		floatt = this.floatService.findOne(floatId);

		Assert.notNull(floatt);

		pictures = floatt.getPictures();

		result = new ModelAndView("showAll/annonymous/picturesFloat");

		result.addObject("pictures", pictures);
		result.addObject("requestURI", "showAll/annonymous/picture/list.do");
		result.addObject("floatId", floatId);

		return result;
	}

	@RequestMapping(value = "/pictureBrother/list", method = RequestMethod.GET)
	public ModelAndView listPicturesBrotherhood(@RequestParam int brotherhoodId) {

		ModelAndView result;

		List<String> pictures;

		Brotherhood brotherhoood;

		brotherhoood = this.brotherhoodService.findOne(brotherhoodId);

		Assert.notNull(brotherhoood);

		pictures = brotherhoood.getPictures();

		result = new ModelAndView("showAll/annonymous/picturesBrotherhood");

		result.addObject("picturesBrotherhood", pictures);
		result.addObject("requestURI", "showAll/annonymous/pictureBrother/list.do");
		result.addObject("brotherhoodId", brotherhoodId);

		return result;
	}

}
