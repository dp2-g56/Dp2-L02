/*
 * AdministratorController.java
 *
 * Copyright (C) 2019 Universidad de Sevilla
 *
 * The use of this project is hereby constrained to the conditions of the
 * TDG Licence, a copy of which you may download from
 * http://www.tdg-seville.info/License.html
 */

package controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import domain.Actor;
import security.Authority;
import services.ActorService;
import services.MemberService;

@Controller
@RequestMapping(value = "/authenticated")
public class DeleteUserController extends AbstractController {

	@Autowired
	private ActorService actorService;
	@Autowired
	private MemberService memberService;

	// Constructors -----------------------------------------------------------

	public DeleteUserController() {
		super();
	}

	@RequestMapping(value = "/deleteUser", method = RequestMethod.GET)
	public ModelAndView deleteUser() {
		ModelAndView result;

		Actor actor = this.actorService.loggedActor();

		List<Authority> authorities = (List<Authority>) actor.getUserAccount().getAuthorities();

		try {
			if (authorities.get(0).toString().equals("MEMBER"))
				this.memberService.deleteLoggedMember();
			result = new ModelAndView("redirect:/j_spring_security_logout");
		} catch (Throwable oops) {
			result = new ModelAndView("redirect:/");
		}

		return result;
	}

}
