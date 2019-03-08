
package controllers;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import security.LoginService;
import security.UserAccount;
import services.ConfigurationService;
import services.FinderService;
import services.MemberService;
import domain.Finder;
import domain.Member;
import domain.Procession;

@Controller
@RequestMapping("/finder/member/")
public class FinderMemberController extends AbstractController {

	@Autowired
	private FinderService			finderService;
	@Autowired
	private MemberService			memberService;
	@Autowired
	private ConfigurationService	configurationService;


	// Constructors -----------------------------------------------------------

	public FinderMemberController() {
		super();
	}

	//List
	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public ModelAndView processionsList() {
		ModelAndView result;

		UserAccount userAccount = LoginService.getPrincipal();
		Member member = this.memberService.getMemberByUsername(userAccount.getUsername());

		Finder finder = member.getFinder();

		//Current Date
		Date currentDate = new Date();

		Calendar calendar = Calendar.getInstance();
		calendar.setTime(currentDate);
		Integer currentDay = calendar.get(Calendar.DATE);
		Integer currentMonth = calendar.get(Calendar.MONTH);
		Integer currentYear = calendar.get(Calendar.YEAR);
		Integer currentHour = calendar.get(Calendar.HOUR);

		//LastEdit Finder
		Date lasEdit = finder.getLastEdit();
		calendar.setTime(lasEdit);
		Integer lastEditDay = calendar.get(Calendar.DATE);
		Integer lastEditMonth = calendar.get(Calendar.MONTH);
		Integer lastEditYear = calendar.get(Calendar.YEAR);
		Integer lastEditHour = calendar.get(Calendar.HOUR);

		Integer time = this.configurationService.getConfiguration().getTimeFinder();

		List<Procession> processions = new ArrayList<>();
		List<Procession> finderProcessions = finder.getProcessions();

		if (currentDay.equals(lastEditDay) && currentMonth.equals(lastEditMonth) && currentYear.equals(lastEditYear) && lastEditHour < (currentHour + time)) {
			Integer numFinderResult = this.configurationService.getConfiguration().getFinderResult();

			if (finderProcessions.size() > numFinderResult)
				for (int i = 0; i < numFinderResult; i++)
					processions.add(finderProcessions.get(i));
			else
				processions = finderProcessions;
		}

		result = new ModelAndView("member/finderResult");

		result.addObject("processions", processions);
		result.addObject("member", member);

		return result;
	}

	//Create
	@RequestMapping(value = "/edit", method = RequestMethod.GET)
	public ModelAndView edit() {
		ModelAndView result;
		UserAccount userAccount = LoginService.getPrincipal();
		Member logguedMember = this.memberService.getMemberByUsername(userAccount.getUsername());

		Finder finder = logguedMember.getFinder();
		Assert.notNull(finder);

		result = this.createEditModelAndView(finder);

		return result;

	}
	//Save
	@RequestMapping(value = "/edit", method = RequestMethod.POST, params = "save")
	public ModelAndView save(Finder finderForm, BindingResult binding) {
		ModelAndView result;

		UserAccount userAccount = LoginService.getPrincipal();
		Member logguedMember = this.memberService.getMemberByUsername(userAccount.getUsername());

		Finder finder = this.finderService.reconstruct(finderForm, binding);
		if (binding.hasErrors())
			result = this.createEditModelAndView(finderForm);
		else
			try {
				this.finderService.filterProcessionsByFinder(finder);
				result = new ModelAndView("redirect:list.do");
			} catch (Throwable oops) {
				result = this.createEditModelAndView(finder, "finder.commit.error");
				result.addObject("member", logguedMember);
			}
		return result;
	}
	//Clean Filter
	@RequestMapping(value = "/clean", method = RequestMethod.POST, params = "save")
	public ModelAndView save() {
		ModelAndView result;
		UserAccount userAccount = LoginService.getPrincipal();
		Member logguedMember = this.memberService.getMemberByUsername(userAccount.getUsername());

		Finder finder = logguedMember.getFinder();
		Assert.notNull(finder);

		List<Procession> processions = this.finderService.getAllPublishedProcessions();

		Date date = new Date();

		finder.setArea("");
		finder.setKeyWord("");
		finder.setLastEdit(date);
		finder.setMaxDate(null);
		finder.setMinDate(null);
		finder.setProcessions(processions);
		this.finderService.save(finder);

		result = new ModelAndView("redirect:list.do");

		return result;

	}
	//CreateEditModelAndView
	protected ModelAndView createEditModelAndView(Finder finder) {
		ModelAndView result;

		result = this.createEditModelAndView(finder, null);

		return result;
	}

	protected ModelAndView createEditModelAndView(Finder finder, String messageCode) {
		ModelAndView result;

		result = new ModelAndView("member/finder");

		result.addObject("finder", finder);
		result.addObject("message", messageCode);

		return result;

	}

}
