package controllers;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import domain.Chapter;
import domain.Parade;
import domain.ParadeStatus;
import services.ChapterService;
import services.ParadeService;

@Controller
@RequestMapping("/parade/chapter")
public class ParadesChapterController extends AbstractController {

	// Services

	@Autowired
	private ParadeService paradeService;

	@Autowired
	private ChapterService chapterService;

	// Constructor

	public ParadesChapterController() {
		super();
	}

	// List

	// Listar Parades
	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public ModelAndView list() {

		ModelAndView result;
		List<Parade> parades;

		this.chapterService.loggedAsChapter();

		Chapter logguedChapter = this.chapterService.loggedChapter();

		if (logguedChapter.getArea() == null)
			result = new ModelAndView("redirect:");
		else {
			parades = this.paradeService.getParadesByArea(logguedChapter.getArea());
			result = new ModelAndView("parade/chapter/list");
			result.addObject("parades", parades);
		}

		return result;
	}

	// Change status

	@RequestMapping(value = "/changeStatus", method = RequestMethod.GET)
	public ModelAndView changeStatus(@RequestParam int paradeId) {
		ModelAndView result;
		Assert.isTrue(this.chapterService.paradeSecurity(paradeId));
		Parade parade = this.paradeService.findOne(paradeId);

		List<ParadeStatus> paradeStatus = Arrays.asList(ParadeStatus.values());

		String locale = LocaleContextHolder.getLocale().getLanguage();

		List<String> statusName = new ArrayList<>();

		if (locale == "es") {
			statusName.add("PRESENTADO");
			statusName.add("ACEPTADO");
			statusName.add("RECHAZADO");
		} else if (locale == "en") {
			statusName.add("SUBMITTED");
			statusName.add("ACCEPTED");
			statusName.add("REJECTED");
		}

		result = new ModelAndView("parade/chapter/changeStatus");
		result.addObject("parade", parade);
		result.addObject("paradeStatus", paradeStatus);
		result.addObject("statusName", statusName);

		return result;
	}

	@RequestMapping(value = "/changeStatus", method = RequestMethod.POST, params = "save")
	public ModelAndView changeStatus(Parade parade, BindingResult binding) {
		ModelAndView result;

		this.chapterService.loggedAsChapter();
		Assert.isTrue(this.chapterService.paradeSecurity(parade.getId()));
		parade = this.paradeService.reconstrucParadeStatus(parade);

		if (binding.hasErrors()) {
			result = new ModelAndView("parade/chapter/changeStatus");
			result.addObject("parade", parade);
		} else
			try {
				if (parade.getParadeStatus().equals(ParadeStatus.REJECTED)) {
					Assert.notNull(parade.getRejectedReason());
					Assert.isTrue(!parade.getRejectedReason().trim().equals(""));
				}
				this.paradeService.save(parade);

				result = new ModelAndView("redirect:list.do");
			} catch (Throwable oops) {
				result = new ModelAndView("parade/chapter/changeStatus");
				result.addObject("parade", parade);
			}

		return null;
	}

}
