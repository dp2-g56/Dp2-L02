/*
 * AbstractController.java
 *
 * Copyright (C) 2019 Universidad de Sevilla
 *
 * The use of this project is hereby constrained to the conditions of the
 * TDG Licence, a copy of which you may download from
 * http://www.tdg-seville.info/License.html
 */

package controllers;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import domain.Proclaim;
import services.ChapterService;
import services.ProclaimService;

@Controller
@RequestMapping("/chapter")
public class ChapterController {

	@Autowired
	ChapterService chapterService;

	@Autowired
	ProclaimService proclaimService;

	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public ModelAndView list() {
		ModelAndView result;

		this.chapterService.loggedAsChapter();

		List<Proclaim> proclaims = new ArrayList<Proclaim>();

		proclaims = this.proclaimService.showProclaims();

		result = new ModelAndView("chapter/list");

		result.addObject("proclaims", proclaims);
		result.addObject("requestURI", "chapter/list.do");

		return result;
	}

	@RequestMapping(value = "/create", method = RequestMethod.GET)
	public ModelAndView create() {
		ModelAndView result;

		this.chapterService.loggedAsChapter();

		Proclaim proclaim = new Proclaim();

		proclaim = this.proclaimService.createProclaim();

		result = this.createEditModelAndView(proclaim);

		return result;
	}

	@RequestMapping(value = "/edit", method = RequestMethod.POST, params = "save")
	public ModelAndView saveFloat(Proclaim proclaim, BindingResult binding) {

		ModelAndView result;
		this.chapterService.loggedAsChapter();

		proclaim = this.proclaimService.reconstruct(proclaim, binding);

		if (binding.hasErrors())
			result = this.createEditModelAndView(proclaim);
		else
			try {
				this.proclaimService.saveProclaim(proclaim);

				result = new ModelAndView("redirect:list.do");
			} catch (Throwable oops) {
				result = this.createEditModelAndView(proclaim, "brotherhood.commit.error");

			}
		return result;
	}

	protected ModelAndView createEditModelAndView(Proclaim proclaim) {
		ModelAndView result;

		result = this.createEditModelAndView(proclaim, null);

		return result;
	}

	protected ModelAndView createEditModelAndView(Proclaim proclaim, String messageCode) {
		ModelAndView result;

		result = new ModelAndView("chapter/create");

		result.addObject("proclaim", proclaim);
		result.addObject("message", messageCode);

		return result;
	}
}
