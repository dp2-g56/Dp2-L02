
package controllers;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import services.BrotherhoodService;
import services.MemberService;
import domain.Brotherhood;

@Controller
@RequestMapping("/brotherhood/member")
public class MemberController extends AbstractController {

	@Autowired
	private BrotherhoodService	brotherhoodService;

	@Autowired
	private MemberService		memberService;


	public MemberController() {
		super();
	}

	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public ModelAndView list() {
		ModelAndView result;
		List<Brotherhood> brotherhoods = new ArrayList<Brotherhood>();
		brotherhoods = this.brotherhoodService.findAll();

		result = new ModelAndView("brotherhood/member/list");

		result.addObject("brotherhoods", brotherhoods);
		result.addObject("requestURI", "brotherhood/member/list.do");
		return result;
	}

}
