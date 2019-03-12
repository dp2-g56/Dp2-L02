
package controllers;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import services.BrotherhoodService;
import services.ChapterService;
import services.ConfigurationService;
import services.MemberService;
import domain.Brotherhood;
import domain.Chapter;
import domain.Configuration;
import domain.Member;
import forms.FormObjectBrotherhood;
import forms.FormObjectChapter;
import forms.FormObjectMember;

@Controller
@RequestMapping("/anonymous")
public class AnonymousController extends AbstractController {

	@Autowired
	private MemberService			memberService;
	@Autowired
	private ChapterService			chapterService;
	@Autowired
	private BrotherhoodService		brotherhoodService;
	@Autowired
	private ConfigurationService	configurationService;


	public AnonymousController() {
		super();
	}

	@RequestMapping(value = "/termsAndConditionsEN", method = RequestMethod.GET)
	public ModelAndView listEN() {
		ModelAndView result;

		result = new ModelAndView("termsAndConditionsEN");

		return result;
	}

	@RequestMapping(value = "/termsAndConditionsES", method = RequestMethod.GET)
	public ModelAndView listES() {

		ModelAndView result;

		result = new ModelAndView("termsAndConditionsES");

		return result;
	}

	//------------------------ CHAPTER -----------------------------------------------------	
	//Create
	@RequestMapping(value = "/createChapter", method = RequestMethod.GET)
	public ModelAndView createChapter() {
		ModelAndView result;

		FormObjectChapter formObjectChapter = new FormObjectChapter();
		formObjectChapter.setTermsAndConditions(false);

		String locale = LocaleContextHolder.getLocale().getLanguage().toUpperCase();

		result = this.createEditModelAndView(formObjectChapter);
		result.addObject("areas", this.chapterService.listFreeAreas());
		result.addObject("locale", locale);

		return result;
	}

	//SAVE
	@RequestMapping(value = "/createChapter", method = RequestMethod.POST, params = "save")
	public ModelAndView save(@Valid FormObjectChapter formObjectChapter, BindingResult binding) {
		ModelAndView result;

		Chapter chapter = new Chapter();
		chapter = this.chapterService.createChapter();

		Configuration configuration = this.configurationService.getConfiguration();
		String prefix = configuration.getSpainTelephoneCode();

		chapter = this.chapterService.reconstruct(formObjectChapter, binding);
		if (chapter.getPhoneNumber().matches("([0-9]{4,})$")) {
			chapter.setPhoneNumber(prefix + chapter.getPhoneNumber());
		}

		if (binding.hasErrors()) {
			result = this.createEditModelAndView(chapter);
		} else {
			try {
				this.chapterService.saveCreate(chapter);
				result = new ModelAndView("redirect:/security/login.do");
			} catch (Throwable oops) {
				result = this.createEditModelAndView(chapter, "brotherhood.commit.error");
			}
		}
		return result;
	}

	//MODEL AND VIEW FORM OBJECT CHAPTER
	protected ModelAndView createEditModelAndView(FormObjectChapter formObjectChapter) {
		ModelAndView result;

		String locale = LocaleContextHolder.getLocale().getLanguage().toUpperCase();

		result = this.createEditModelAndView(formObjectChapter, null);
		result.addObject("locale", locale);
		result.addObject("areas", this.chapterService.listFreeAreas());

		return result;
	}

	protected ModelAndView createEditModelAndView(FormObjectChapter formObjectChapter, String messageCode) {
		ModelAndView result;

		String locale = LocaleContextHolder.getLocale().getLanguage().toUpperCase();

		result = new ModelAndView("anonymous/createChapter");
		result.addObject("formObjectChapter", formObjectChapter);
		result.addObject("areas", this.chapterService.listFreeAreas());
		result.addObject("message", messageCode);
		result.addObject("locale", locale);

		return result;
	}

	//MODEL AND VIEW CHAPTER
	protected ModelAndView createEditModelAndView(Chapter chapter) {
		ModelAndView result;

		String locale = LocaleContextHolder.getLocale().getLanguage().toUpperCase();

		result = this.createEditModelAndView(chapter, null);
		result.addObject("locale", locale);
		result.addObject("areas", this.chapterService.listFreeAreas());

		return result;
	}

	protected ModelAndView createEditModelAndView(Chapter chapter, String messageCode) {
		ModelAndView result;

		String locale = LocaleContextHolder.getLocale().getLanguage().toUpperCase();

		result = new ModelAndView("anonymous/createChapter");
		result.addObject("chapter", chapter);
		result.addObject("areas", this.chapterService.listFreeAreas());
		result.addObject("message", messageCode);
		result.addObject("locale", locale);

		return result;
	}

	//------------------------ MEMBER -----------------------------------------------------	
	//Create
	@RequestMapping(value = "/createMember", method = RequestMethod.GET)
	public ModelAndView createMember() {
		ModelAndView result;

		FormObjectMember formObjectMember = new FormObjectMember();
		formObjectMember.setTermsAndConditions(false);

		String locale = LocaleContextHolder.getLocale().getLanguage().toUpperCase();

		result = this.createEditModelAndView(formObjectMember);
		result.addObject("locale", locale);

		return result;
	}

	//SAVE
	@RequestMapping(value = "/createMember", method = RequestMethod.POST, params = "save")
	public ModelAndView save(@Valid FormObjectMember formObjectMember, BindingResult binding) {

		ModelAndView result;

		Member member = new Member();
		member = this.memberService.createMember();

		Configuration configuration = this.configurationService.getConfiguration();
		String prefix = configuration.getSpainTelephoneCode();

		//Confirmacion contraseña
		if (!formObjectMember.getPassword().equals(formObjectMember.getConfirmPassword())) {
			if (LocaleContextHolder.getLocale().getLanguage().toUpperCase().contains("ES")) {
				binding.addError(new FieldError("formObjectMember", "password", formObjectMember.getPassword(), false, null, null, "Las contraseñas no coinciden"));
				return this.createEditModelAndView(member);
			} else {
				binding.addError(new FieldError("formObjectMember", "password", formObjectMember.getPassword(), false, null, null, "Passwords don't match"));
				return this.createEditModelAndView(member);
			}
		}

		//Confirmacion terminos y condiciones
		if (!formObjectMember.getTermsAndConditions()) {
			if (LocaleContextHolder.getLocale().getLanguage().toUpperCase().contains("ES")) {
				binding.addError(new FieldError("formObjectMember", "termsAndConditions", formObjectMember.getTermsAndConditions(), false, null, null, "Debe aceptar los terminos y condiciones"));
				return this.createEditModelAndView(member);
			} else {
				binding.addError(new FieldError("formObjectMember", "termsAndConditions", formObjectMember.getTermsAndConditions(), false, null, null, "You must accept the terms and conditions"));
				return this.createEditModelAndView(member);
			}
		}

		//Reconstruccion
		member = this.memberService.reconstruct(formObjectMember, binding);

		if (binding.hasErrors()) {
			result = this.createEditModelAndView(member);
		} else {
			try {

				if (member.getEmail().matches("[\\w.%-]+\\<[\\w.%-]+\\@+\\>|[\\w.%-]+")) {
					if (LocaleContextHolder.getLocale().getLanguage().toUpperCase().contains("ES")) {
						binding.addError(new FieldError("member", "email", member.getEmail(), false, null, null, "No sigue el patron ejemplo@dominio.asd o alias <ejemplo@dominio.asd>"));
						return this.createEditModelAndView(member);
					} else {
						binding.addError(new FieldError("member", "email", member.getEmail(), false, null, null, "Dont follow the pattern example@domain.asd or alias <example@domain.asd>"));
						return this.createEditModelAndView(member);
					}

				} else if (member.getPhoneNumber().matches("(\\+[0-9]{1,3})(\\([0-9]{1,3}\\))([0-9]{4,})$") || member.getPhoneNumber().matches("(\\+[0-9]{1,3})([0-9]{4,})$")) {
					this.memberService.saveCreate(member);
				} else if (member.getPhoneNumber().matches("([0-9]{4,})$")) {
					member.setPhoneNumber(prefix + member.getPhoneNumber());
					this.memberService.saveCreate(member);
				} else {
					this.memberService.saveCreate(member);
				}

				result = new ModelAndView("redirect:/security/login.do");

			} catch (Throwable oops) {
				result = this.createEditModelAndView(member, "brotherhood.commit.error");

			}
		}
		return result;
	}

	//MODEL AND VIEW FORM OBJECT MEMBER
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

		result = new ModelAndView("anonymous/createMember");
		result.addObject("formObjectMember", formObjectMember);
		result.addObject("message", messageCode);
		result.addObject("locale", locale);

		return result;
	}

	//MODEL AND VIEW MEMBER
	protected ModelAndView createEditModelAndView(Member member) {
		ModelAndView result;

		String locale = LocaleContextHolder.getLocale().getLanguage().toUpperCase();

		result = this.createEditModelAndView(member, null);
		result.addObject("locale", locale);

		return result;
	}

	protected ModelAndView createEditModelAndView(Member member, String messageCode) {
		ModelAndView result;

		String locale = LocaleContextHolder.getLocale().getLanguage().toUpperCase();

		result = new ModelAndView("anonymous/createMember");
		result.addObject("member", member);
		result.addObject("message", messageCode);
		result.addObject("locale", locale);

		return result;
	}

	//------------------------ BROTHERHOOD -----------------------------------------------------	
	//Create
	@RequestMapping(value = "/createBrotherhood", method = RequestMethod.GET)
	public ModelAndView createBrotherhood() {
		ModelAndView result;

		FormObjectBrotherhood formObjectBrotherhood = new FormObjectBrotherhood();
		formObjectBrotherhood.setTermsAndConditions(false);

		String locale = LocaleContextHolder.getLocale().getLanguage().toUpperCase();

		result = this.createEditModelAndView(formObjectBrotherhood);
		result.addObject("locale", locale);

		return result;
	}

	//SAVE
	@RequestMapping(value = "/createBrotherhood", method = RequestMethod.POST, params = "save")
	public ModelAndView save(@Valid FormObjectBrotherhood formObjectBrotherhood, BindingResult binding) {

		ModelAndView result;

		Brotherhood brotherhood = new Brotherhood();
		brotherhood = this.brotherhoodService.createBrotherhood();

		Configuration configuration = this.configurationService.getConfiguration();
		String prefix = configuration.getSpainTelephoneCode();

		//Confirmacion contraseña
		if (!formObjectBrotherhood.getPassword().equals(formObjectBrotherhood.getConfirmPassword())) {
			if (LocaleContextHolder.getLocale().getLanguage().toUpperCase().contains("ES")) {
				binding.addError(new FieldError("formObjectBrotherhood", "password", formObjectBrotherhood.getPassword(), false, null, null, "Las contraseñas no coinciden"));
				return this.createEditModelAndView(brotherhood);
			} else {
				binding.addError(new FieldError("formObjectBrotherhood", "password", formObjectBrotherhood.getPassword(), false, null, null, "Passwords don't match"));
				return this.createEditModelAndView(brotherhood);
			}
		}

		//Confirmacion terminos y condiciones
		if (!formObjectBrotherhood.getTermsAndConditions()) {
			if (LocaleContextHolder.getLocale().getLanguage().toUpperCase().contains("ES")) {
				binding.addError(new FieldError("formObjectBrotherhood", "termsAndConditions", formObjectBrotherhood.getTermsAndConditions(), false, null, null, "Debe aceptar los terminos y condiciones"));
				return this.createEditModelAndView(formObjectBrotherhood);
			} else {
				binding.addError(new FieldError("formObjectBrotherhood", "termsAndConditions", formObjectBrotherhood.getTermsAndConditions(), false, null, null, "You must accept the terms and conditions"));
				return this.createEditModelAndView(formObjectBrotherhood);
			}
		}

		//Reconstruccion 
		brotherhood = this.brotherhoodService.reconstruct(formObjectBrotherhood, binding);

		if (binding.hasErrors()) {
			result = this.createEditModelAndView(brotherhood);
		} else {
			try {

				if (brotherhood.getEmail().matches("[\\w.%-]+\\<[\\w.%-]+\\@+\\>|[\\w.%-]+")) {
					if (LocaleContextHolder.getLocale().getLanguage().toUpperCase().contains("ES")) {
						binding.addError(new FieldError("brotherhood", "email", brotherhood.getEmail(), false, null, null, "No sigue el patron ejemplo@dominio.asd o alias <ejemplo@dominio.asd>"));
						return this.createEditModelAndView(brotherhood);
					} else {
						binding.addError(new FieldError("brotherhood", "email", brotherhood.getEmail(), false, null, null, "Dont follow the pattern example@domain.asd or alias <example@domain.asd>"));
						return this.createEditModelAndView(brotherhood);
					}

				} else if (brotherhood.getPhoneNumber().matches("(\\+[0-9]{1,3})(\\([0-9]{1,3}\\))([0-9]{4,})$") || brotherhood.getPhoneNumber().matches("(\\+[0-9]{1,3})([0-9]{4,})$")) {
					this.brotherhoodService.saveCreate(brotherhood);
				} else if (brotherhood.getPhoneNumber().matches("([0-9]{4,})$")) {
					brotherhood.setPhoneNumber(prefix + brotherhood.getPhoneNumber());
					this.brotherhoodService.saveCreate(brotherhood);
				} else {
					this.brotherhoodService.saveCreate(brotherhood);
				}

				result = new ModelAndView("redirect:/security/login.do");

			} catch (Throwable oops) {
				result = this.createEditModelAndView(brotherhood, "brotherhood.commit.error");

			}
		}
		return result;
	}

	//MODEL AND VIEW FORM OBJECT BROTHERHOOD
	protected ModelAndView createEditModelAndView(FormObjectBrotherhood formObjectBrotherhood) {
		ModelAndView result;

		String locale = LocaleContextHolder.getLocale().getLanguage().toUpperCase();

		result = this.createEditModelAndView(formObjectBrotherhood, null);
		result.addObject("locale", locale);

		return result;
	}

	protected ModelAndView createEditModelAndView(FormObjectBrotherhood formObjectBrotherhood, String messageCode) {
		ModelAndView result;

		String locale = LocaleContextHolder.getLocale().getLanguage().toUpperCase();

		result = new ModelAndView("anonymous/createBrotherhood");
		result.addObject("formObjectBrotherhood", formObjectBrotherhood);
		result.addObject("message", messageCode);
		result.addObject("locale", locale);

		return result;
	}

	//MODEL AND VIEW BROTHERHOOD
	protected ModelAndView createEditModelAndView(Brotherhood brotherhood) {
		ModelAndView result;

		String locale = LocaleContextHolder.getLocale().getLanguage().toUpperCase();

		result = this.createEditModelAndView(brotherhood, null);
		result.addObject("locale", locale);

		return result;
	}

	protected ModelAndView createEditModelAndView(Brotherhood brotherhood, String messageCode) {
		ModelAndView result;

		String locale = LocaleContextHolder.getLocale().getLanguage().toUpperCase();

		result = new ModelAndView("anonymous/createBrotherhood");
		result.addObject("brotherhood", brotherhood);
		result.addObject("message", messageCode);
		result.addObject("locale", locale);

		return result;
	}

}
