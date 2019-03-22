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

import java.io.IOException;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import services.AdminService;
import services.ConfigurationService;
import domain.Admin;
import domain.Configuration;
import forms.FormObjectMember;

@Controller
@RequestMapping("/administrator")
public class AdministratorController extends AbstractController {

	@Autowired
	private AdminService			adminService;
	@Autowired
	private ConfigurationService	configurationService;


	// Constructor -----------------------------------------------------------
	public AdministratorController() {
		super();
	}

	//------------------------ ADMIN -----------------------------------------------------	
	//Create
	@RequestMapping(value = "/createAdmin", method = RequestMethod.GET)
	public ModelAndView createAdmin() {
		ModelAndView result;

		FormObjectMember formObjectMember = new FormObjectMember();
		formObjectMember.setTermsAndConditions(false);

		String locale = LocaleContextHolder.getLocale().getLanguage().toUpperCase();

		result = this.createEditModelAndView(formObjectMember);
		result.addObject("locale", locale);

		return result;
	}

	//SAVE
	@RequestMapping(value = "/createAdmin", method = RequestMethod.POST, params = "save")
	public ModelAndView save(@Valid FormObjectMember formObjectMember, BindingResult binding) {

		ModelAndView result;

		Admin admin = new Admin();
		admin = this.adminService.createAdmin();

		Configuration configuration = this.configurationService.getConfiguration();
		String prefix = configuration.getSpainTelephoneCode();

		//Confirmacion contrase�a
		if (!formObjectMember.getPassword().equals(formObjectMember.getConfirmPassword()))
			if (LocaleContextHolder.getLocale().getLanguage().toUpperCase().contains("ES")) {
				binding.addError(new FieldError("formObjectMember", "password", formObjectMember.getPassword(), false, null, null, "Las contrase�as no coinciden"));
				return this.createEditModelAndView(admin);
			} else {
				binding.addError(new FieldError("formObjectMember", "password", formObjectMember.getPassword(), false, null, null, "Passwords don't match"));
				return this.createEditModelAndView(admin);
			}

		//Confirmacion terminos y condiciones
		if (!formObjectMember.getTermsAndConditions())
			if (LocaleContextHolder.getLocale().getLanguage().toUpperCase().contains("ES")) {
				binding.addError(new FieldError("formObjectMember", "termsAndConditions", formObjectMember.getTermsAndConditions(), false, null, null, "Debe aceptar los terminos y condiciones"));
				return this.createEditModelAndView(admin);
			} else {
				binding.addError(new FieldError("formObjectMember", "termsAndConditions", formObjectMember.getTermsAndConditions(), false, null, null, "You must accept the terms and conditions"));
				return this.createEditModelAndView(admin);
			}

		//Reconstruccion
		admin = this.adminService.reconstruct(formObjectMember, binding);

		if (binding.hasErrors())
			result = this.createEditModelAndView(admin);
		else
			try {

				if (admin.getPhoneNumber().matches("(\\+[0-9]{1,3})(\\([0-9]{1,3}\\))([0-9]{4,})$") || admin.getPhoneNumber().matches("(\\+[0-9]{1,3})([0-9]{4,})$"))
					this.adminService.saveCreate(admin);
				else if (admin.getPhoneNumber().matches("([0-9]{4,})$")) {
					admin.setPhoneNumber(prefix + admin.getPhoneNumber());
					this.adminService.saveCreate(admin);
				} else
					this.adminService.saveCreate(admin);

				result = new ModelAndView("redirect:/");

			} catch (Throwable oops) {
				result = this.createEditModelAndView(admin, "brotherhood.commit.error");

			}
		return result;
	}

//	@RequestMapping(value = "/export", method = RequestMethod.GET)
//	public @ResponseBody Admin export(@RequestParam(value = "id", defaultValue = "-1") int id) {
//		Admin admin = new Admin();
//		admin = this.adminService.findOne(id);
//		if (admin == null || this.adminService.loggedAdmin().getId() != id)
//			return null;
//		return admin;
//	}

	@RequestMapping(value = "/export", method = RequestMethod.GET)
	public @ResponseBody String export(@RequestParam(value = "id", defaultValue = "-1") int id,
			HttpServletResponse response) throws IOException {

		this.adminService.loggedAsAdmin();

		Admin admin = new Admin();
		admin = this.adminService.findOne(id);

		// Defines un StringBuilder para construir tu string
		StringBuilder sb = new StringBuilder();

		// Cada append a�ade una linea, cada "line.separator" a�ade un salto de linea
		sb.append("Personal data:").append(System.getProperty("line.separator"));
		sb.append("Name: " + admin.getName()).append(System.getProperty("line.separator"));
		sb.append("Middle name: " + admin.getMiddleName()).append(System.getProperty("line.separator"));
		sb.append("Surname: " + admin.getSurname()).append(System.getProperty("line.separator"));
		sb.append("Address: " + admin.getAddress()).append(System.getProperty("line.separator"));
		sb.append("Email: " + admin.getEmail()).append(System.getProperty("line.separator"));
		sb.append("Photo: " + admin.getPhoto()).append(System.getProperty("line.separator"));
		sb.append("Phone: " + admin.getPhoneNumber()).append(System.getProperty("line.separator"));
		sb.append(System.getProperty("line.separator"));
		sb.append("SocialProfiles: ").append(System.getProperty("line.separator"));

		// Este metodo te muestra los socialProfiles de la misma manera que el resto del
		// documento
		sb.append(this.adminService.SocialProfilesToString()).append(System.getProperty("line.separator"));

		if (admin == null || this.adminService.loggedAdmin().getId() != id)
			return null;

		// Defines el nombre del archivo y la extension
		response.setContentType("text/txt");
		response.setHeader("Content-Disposition", "attachment;filename=exportDataAdmin.txt");

		// Con estos comandos permites su descarga cuando clickas
		ServletOutputStream outStream = response.getOutputStream();
		outStream.println(sb.toString());
		outStream.flush();
		outStream.close();

		// El return no llega nunca, es del metodo viejo
		return sb.toString();
	}

	// MODEL AND VIEW FORM OBJECT MEMBER
	protected ModelAndView createEditModelAndView(FormObjectMember formObjectMember) {
		ModelAndView result;

		String locale = LocaleContextHolder.getLocale().getLanguage().toUpperCase();

		result = this.createEditModelAndView(formObjectMember, null);
		result.addObject("locale", locale);

		return result;
	}

	protected ModelAndView createEditModelAndView(FormObjectMember formObjectMember, String messageCode) {
		ModelAndView result;

		String locale = LocaleContextHolder.getLocale().getLanguage().toUpperCase();

		result = new ModelAndView("administrator/createAdmin");
		result.addObject("formObjectMember", formObjectMember);
		result.addObject("message", messageCode);
		result.addObject("locale", locale);

		return result;
	}

	//MODEL AND VIEW ADMIN
	protected ModelAndView createEditModelAndView(Admin admin) {
		ModelAndView result;

		String locale = LocaleContextHolder.getLocale().getLanguage().toUpperCase();

		result = this.createEditModelAndView(admin, null);
		result.addObject("locale", locale);

		return result;
	}

	protected ModelAndView createEditModelAndView(Admin admin, String messageCode) {
		ModelAndView result;

		String locale = LocaleContextHolder.getLocale().getLanguage().toUpperCase();

		result = new ModelAndView("administrator/createAdmin");
		result.addObject("admin", admin);
		result.addObject("message", messageCode);
		result.addObject("locale", locale);

		return result;
	}

}
