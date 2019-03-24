
package controllers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import security.Authority;
import security.LoginService;
import security.UserAccount;
import services.ActorService;
import services.AdminService;
import services.BrotherhoodService;
import services.ChapterService;
import services.ConfigurationService;
import services.FloatService;
import services.HistoryService;
import services.InceptionRecordService;
import services.LegalRecordService;
import services.LinkRecordService;
import services.MemberService;
import services.MiscellaneousRecordService;
import services.PeriodRecordService;
import services.SocialProfileService;
import services.SponsorService;
import domain.Actor;
import domain.Admin;
import domain.Brotherhood;
import domain.Chapter;
import domain.Configuration;
import domain.InceptionRecord;
import domain.LegalRecord;
import domain.LinkRecord;
import domain.Member;
import domain.MiscellaneousRecord;
import domain.PeriodRecord;
import domain.SocialProfile;
import domain.Sponsor;
import forms.FormObjectLinkRecord;

@Controller
@RequestMapping("/authenticated")
public class SocialProfileController extends AbstractController {

	@Autowired
	private ActorService				actorService;

	@Autowired
	private SocialProfileService		socialProfileService;

	@Autowired
	private BrotherhoodService			brotherhoodService;

	@Autowired
	private AdminService				adminService;

	@Autowired
	private MemberService				memberService;

	@Autowired
	private FloatService				floatService;

	@Autowired
	private ConfigurationService		configurationService;

	@Autowired
	private SponsorService				sponsorService;

	@Autowired
	private LinkRecordService			linkRecordService;

	@Autowired
	private MiscellaneousRecordService	miscellaneousRecordService;

	@Autowired
	private LegalRecordService			legalRecordService;

	@Autowired
	private PeriodRecordService			periodRecordService;

	@Autowired
	private InceptionRecordService		inceptionRecordService;

	@Autowired
	private HistoryService				historyService;

	@Autowired
	private ChapterService				chapterService;


	//-------------------------------------------------------------------
	//---------------------------LIST BROTHERHOOD------------------------------------
	@RequestMapping(value = "/showProfile", method = RequestMethod.GET)
	public ModelAndView list() {
		ModelAndView result;
		Brotherhood broherhood = new Brotherhood();
		Chapter chapter = new Chapter();
		UserAccount userAccount;
		userAccount = LoginService.getPrincipal();
		Actor logguedActor = new Actor();
		List<SocialProfile> socialProfiles = new ArrayList<SocialProfile>();

		final List<Authority> authorities = (List<Authority>) userAccount.getAuthorities();

		result = new ModelAndView("authenticated/showProfile");

		if (authorities.get(0).toString().equals("BROTHERHOOD")) {
			broherhood = this.brotherhoodService.loggedBrotherhood();
			socialProfiles = broherhood.getSocialProfiles();

			Boolean showHistory = false;

			if (!(broherhood.getHistory() == null)) {
				//History
				List<LinkRecord> linkRecords = new ArrayList<>();
				linkRecords = broherhood.getHistory().getLinkRecords();

				List<MiscellaneousRecord> miscellaneousRecords = new ArrayList<>();
				miscellaneousRecords = broherhood.getHistory().getMiscellaneousRecords();

				List<LegalRecord> legalRecords = new ArrayList<>();
				legalRecords = broherhood.getHistory().getLegalRecords();

				List<PeriodRecord> periodRecords = new ArrayList<>();
				periodRecords = broherhood.getHistory().getPeriodRecords();

				InceptionRecord inceptionRecord = broherhood.getHistory().getInceptionRecord();
				List<InceptionRecord> inceptionRecords = new ArrayList<>();
				inceptionRecords.add(inceptionRecord);

				result.addObject("linkRecords", linkRecords);
				result.addObject("miscellaneousRecords", miscellaneousRecords);
				result.addObject("legalRecords", legalRecords);
				result.addObject("periodRecords", periodRecords);
				result.addObject("inceptionRecords", inceptionRecords);

				showHistory = true;
			}

			result.addObject("showHistory", showHistory);

		} else if (authorities.get(0).toString().equals("CHAPTER")) {
			chapter = this.chapterService.loggedChapter();
			socialProfiles = chapter.getSocialProfiles();

		} else {

			logguedActor = this.actorService.getActorByUsername(userAccount.getUsername());

			socialProfiles = logguedActor.getSocialProfiles();
		}

		result.addObject("socialProfiles", socialProfiles);
		result.addObject("actor", logguedActor);
		result.addObject("chapter", chapter);
		result.addObject("broherhood", broherhood);
		result.addObject("pictures", broherhood.getPictures());
		result.addObject("requestURI", "authenticated/showProfile.do");

		return result;
	}

	//---------------------------------------------------------------------
	@RequestMapping(value = "/socialProfile/create", method = RequestMethod.GET)
	public ModelAndView create() {
		ModelAndView result;
		SocialProfile socialProfile;

		socialProfile = this.socialProfileService.create();
		result = this.createEditModelAndView(socialProfile);

		return result;
	}

	//---------------------------------------------------------------------
	//---------------------------EDIT BROTHERHOOD--------------------------------------
	@RequestMapping(value = "/socialProfile/edit", method = RequestMethod.GET)
	public ModelAndView edit(@RequestParam int socialProfileId) {

		ModelAndView result;
		SocialProfile socialProfile;

		Actor logged = this.actorService.getActorByUsername(LoginService.getPrincipal().getUsername());

		List<SocialProfile> socialProfiles = logged.getSocialProfiles();

		socialProfile = this.socialProfileService.findOne(socialProfileId);
		Assert.notNull(socialProfile);
		result = this.createEditModelAndView(socialProfile);

		if (!(socialProfiles.contains(socialProfile)))
			result = this.list();
		return result;
	}

	//---------------------------------------------------------------------
	//---------------------------SAVE --------------------------------------
	@RequestMapping(value = "/socialProfile/create", method = RequestMethod.POST, params = "save")
	public ModelAndView save(SocialProfile socialProfile, BindingResult binding) {
		ModelAndView result;
		Actor logguedActor = this.actorService.getActorByUsername(LoginService.getPrincipal().getUsername());

		socialProfile = this.socialProfileService.reconstruct(socialProfile, binding);

		if (binding.hasErrors())
			result = this.createEditModelAndView(socialProfile);
		else
			try {

				SocialProfile saved = this.socialProfileService.save(socialProfile);
				List<SocialProfile> socialProfiles = logguedActor.getSocialProfiles();

				if (socialProfiles.contains(socialProfile)) {
					socialProfiles.remove(saved);
					socialProfiles.add(saved);
				} else
					socialProfiles.add(saved);

				logguedActor.setSocialProfiles(socialProfiles);

				this.actorService.save(logguedActor);

				result = new ModelAndView("redirect:/authenticated/showProfile.do");
			} catch (Throwable oops) {
				result = this.createEditModelAndView(socialProfile, "socialProfile.commit.error");
			}
		return result;
	}
	//---------------------------------------------------------------------
	//---------------------------DELETE------------------------------------
	@RequestMapping(value = "/socialProfile/create", method = RequestMethod.POST, params = "delete")
	public ModelAndView delete(SocialProfile socialProfile, BindingResult binding) {

		ModelAndView result;

		socialProfile = this.socialProfileService.reconstruct(socialProfile, binding);

		try {

			this.socialProfileService.deleteSocialProfile(socialProfile);
			result = new ModelAndView("redirect:/authenticated/showProfile.do");

		} catch (Throwable oops) {
			result = this.createEditModelAndView(socialProfile, "socialProfile.commit.error");
		}
		return result;
	}

	//---------------------------------------------------------------------
	//---------------------------EDIT PERSONAL DATA------------------------
	@RequestMapping(value = "/edit", method = RequestMethod.GET)
	public ModelAndView editPersonalData() {

		ModelAndView result;

		UserAccount userAccount;
		userAccount = LoginService.getPrincipal();

		List<Authority> authorities = (List<Authority>) userAccount.getAuthorities();

		if (authorities.get(0).toString().equals("ADMIN")) {
			Admin admin = this.adminService.loggedAdmin();
			Assert.notNull(admin);
			result = this.createEditModelAndView(admin);

		} else if (authorities.get(0).toString().equals("BROTHERHOOD")) {
			Brotherhood brotherhood = this.brotherhoodService.loggedBrotherhood();
			Assert.notNull(brotherhood);
			result = this.createEditModelAndView(brotherhood);
		} else if (authorities.get(0).toString().equals("MEMBER")) {
			Member member = this.memberService.loggedMember();
			Assert.notNull(member);
			result = this.createEditModelAndView(member);
		} else if (authorities.get(0).toString().equals("CHAPTER")) {
			Chapter chapter = this.chapterService.loggedChapter();
			Assert.notNull(chapter);
			result = this.createEditModelAndView(chapter);
		} else {
			Sponsor sponsor = this.sponsorService.loggedSponsor();
			Assert.notNull(sponsor);
			result = this.createEditModelAndView(sponsor);
		}

		if (result == null)
			result = this.list();
		return result;
	}

	//---------------------------------------------------------------------
	//---------------------------SAVE PERSONAL DATA------------------------
	//Admin
	@RequestMapping(value = "/edit", method = RequestMethod.POST, params = "save")
	public ModelAndView saveAdmin(Admin admin, BindingResult binding) {
		ModelAndView result;

		admin = this.adminService.reconstruct(admin, binding);
		Configuration configuration = this.configurationService.getConfiguration();

		String prefix = configuration.getSpainTelephoneCode();

		if (binding.hasErrors())
			result = this.createEditModelAndView(admin);
		else
			try {
				if (admin.getPhoneNumber().matches("(\\+[0-9]{1,3})(\\([0-9]{1,3}\\))([0-9]{4,})$") || admin.getPhoneNumber().matches("(\\+[0-9]{1,3})([0-9]{4,})$"))
					this.adminService.updateAdmin(admin);
				else if (admin.getPhoneNumber().matches("([0-9]{4,})$")) {
					admin.setPhoneNumber(prefix + admin.getPhoneNumber());
					this.adminService.updateAdmin(admin);
				} else
					this.adminService.updateAdmin(admin);
				result = new ModelAndView("redirect:/authenticated/showProfile.do");
			} catch (Throwable oops) {
				result = this.createEditModelAndView(admin, "socialProfile.commit.error");
			}
		return result;
	}

	//Member
	@RequestMapping(value = "/editMember", method = RequestMethod.POST, params = "save")
	public ModelAndView saveMember(Member member, BindingResult binding) {
		ModelAndView result;

		member = this.memberService.reconstruct(member, binding);
		Configuration configuration = this.configurationService.getConfiguration();

		String prefix = configuration.getSpainTelephoneCode();

		if (binding.hasErrors())
			result = this.createEditModelAndView(member);
		else
			try {
				if (member.getEmail().matches("[\\w.%-]+\\<[\\w.%-]+\\@+\\>|[\\w.%-]+")) {
					if (LocaleContextHolder.getLocale().getLanguage().toUpperCase().contains("ES")) {
						binding.addError(new FieldError("member", "email", member.getEmail(), false, null, null, "No sigue el patron ejemplo@dominio.asd o alias <ejemplo@dominio.asd>"));
						return this.createEditModelAndView(member);
					} else {
						binding.addError(new FieldError("member", "email", member.getEmail(), false, null, null, "Dont follow the pattern example@domain.asd or alias <example@domain.asd>"));
						return this.createEditModelAndView(member);
					}

				} else if (member.getPhoneNumber().matches("(\\+[0-9]{1,3})(\\([0-9]{1,3}\\))([0-9]{4,})$") || member.getPhoneNumber().matches("(\\+[0-9]{1,3})([0-9]{4,})$"))
					this.memberService.updateMember(member);
				else if (member.getPhoneNumber().matches("([0-9]{4,})$")) {
					member.setPhoneNumber(prefix + member.getPhoneNumber());
					this.memberService.updateMember(member);
				} else
					this.memberService.updateMember(member);
				result = new ModelAndView("redirect:/authenticated/showProfile.do");
			} catch (Throwable oops) {
				result = this.createEditModelAndView(member, "socialProfile.commit.error");
			}
		return result;
	}

	//Brotherhood
	@RequestMapping(value = "/editBrotherhood", method = RequestMethod.POST, params = "save")
	public ModelAndView saveBrotherhood(Brotherhood brotherhood, BindingResult binding) {
		ModelAndView result;

		brotherhood = this.brotherhoodService.reconstructBrotherhood(brotherhood, binding);
		Configuration configuration = this.configurationService.getConfiguration();

		String prefix = configuration.getSpainTelephoneCode();

		if (binding.hasErrors())
			result = this.createEditModelAndView(brotherhood);
		else
			try {
				if (brotherhood.getEmail().matches("[\\w.%-]+\\<[\\w.%-]+\\@+\\>|[\\w.%-]+")) {
					if (LocaleContextHolder.getLocale().getLanguage().toUpperCase().contains("ES")) {
						binding.addError(new FieldError("brotherhood", "email", brotherhood.getEmail(), false, null, null, "No sigue el patron ejemplo@dominio.asd o alias <ejemplo@dominio.asd>"));
						return this.createEditModelAndView(brotherhood);
					} else {
						binding.addError(new FieldError("brotherhood", "email", brotherhood.getEmail(), false, null, null, "Dont follow the pattern example@domain.asd or alias <example@domain.asd>"));
						return this.createEditModelAndView(brotherhood);
					}

				} else if (brotherhood.getPhoneNumber().matches("(\\+[0-9]{1,3})(\\([0-9]{1,3}\\))([0-9]{4,})$") || brotherhood.getPhoneNumber().matches("(\\+[0-9]{1,3})([0-9]{4,})$"))
					this.brotherhoodService.save(brotherhood);
				else if (brotherhood.getPhoneNumber().matches("([0-9]{4,})$")) {
					brotherhood.setPhoneNumber(prefix + brotherhood.getPhoneNumber());
					this.brotherhoodService.save(brotherhood);
				} else
					this.brotherhoodService.save(brotherhood);
				result = new ModelAndView("redirect:/authenticated/showProfile.do");
			} catch (Throwable oops) {
				result = this.createEditModelAndView(brotherhood, "socialProfile.commit.error");
			}
		return result;
	}

	//Sponsor
	@RequestMapping(value = "/editSponsor", method = RequestMethod.POST, params = "save")
	public ModelAndView saveSponsor(Sponsor sponsor, BindingResult binding) {
		ModelAndView result;

		sponsor = this.sponsorService.reconstructSponsor(sponsor, binding);
		Configuration configuration = this.configurationService.getConfiguration();

		String prefix = configuration.getSpainTelephoneCode();

		if (binding.hasErrors())
			result = this.createEditModelAndView(sponsor);
		else
			try {
				if (sponsor.getEmail().matches("[\\w.%-]+\\<[\\w.%-]+\\@+\\>|[\\w.%-]+")) {
					if (LocaleContextHolder.getLocale().getLanguage().toUpperCase().contains("ES")) {
						binding.addError(new FieldError("sponsor", "email", sponsor.getEmail(), false, null, null, "No sigue el patron ejemplo@dominio.asd o alias <ejemplo@dominio.asd>"));
						return this.createEditModelAndView(sponsor);
					} else {
						binding.addError(new FieldError("sponsor", "email", sponsor.getEmail(), false, null, null, "Dont follow the pattern example@domain.asd or alias <example@domain.asd>"));
						return this.createEditModelAndView(sponsor);
					}

				} else if (sponsor.getPhoneNumber().matches("(\\+[0-9]{1,3})(\\([0-9]{1,3}\\))([0-9]{4,})$") || sponsor.getPhoneNumber().matches("(\\+[0-9]{1,3})([0-9]{4,})$"))
					this.sponsorService.save(sponsor);
				else if (sponsor.getPhoneNumber().matches("([0-9]{4,})$")) {
					sponsor.setPhoneNumber(prefix + sponsor.getPhoneNumber());
					this.sponsorService.save(sponsor);
				} else
					this.sponsorService.save(sponsor);
				result = new ModelAndView("redirect:/authenticated/showProfile.do");
			} catch (Throwable oops) {
				result = this.createEditModelAndView(sponsor, "socialProfile.commit.error");
			}
		return result;
	}

	//Chapter
	@RequestMapping(value = "/editChapter", method = RequestMethod.POST, params = "save")
	public ModelAndView saveChapter(Chapter chapter, BindingResult binding) {
		ModelAndView result;

		chapter = this.chapterService.reconstructPersonalData(chapter, binding);
		Configuration configuration = this.configurationService.getConfiguration();

		String prefix = configuration.getSpainTelephoneCode();
		if (chapter.getPhoneNumber().matches("([0-9]{4,})$"))
			chapter.setPhoneNumber(prefix + chapter.getPhoneNumber());

		if (binding.hasErrors())
			result = this.createEditModelAndView(chapter);
		else
			try {

				this.chapterService.update(chapter);
				result = new ModelAndView("redirect:/authenticated/showProfile.do");
			} catch (Throwable oops) {
				result = this.createEditModelAndView(chapter, "socialProfile.commit.error");
			}
		return result;
	}

	@RequestMapping(value = "/picture/list", method = RequestMethod.GET)
	public ModelAndView listPicturesBrotherhood(@RequestParam int brotherhoodId) {

		ModelAndView result;

		List<String> pictures;

		Brotherhood brotherhoood;

		brotherhoood = this.brotherhoodService.findOne(brotherhoodId);

		Assert.notNull(brotherhoood);
		Assert.isTrue(this.brotherhoodService.loggedBrotherhood().getId() == brotherhoodId);

		pictures = brotherhoood.getPictures();

		result = new ModelAndView("authenticated/picture/list");

		result.addObject("picturesBrotherhood", pictures);
		result.addObject("requestURI", "authenticated/picture/list.do");
		result.addObject("brotherhoodId", brotherhoodId);

		return result;
	}

	@RequestMapping(value = "/picture/create", method = RequestMethod.GET)
	public ModelAndView createPictures(@RequestParam int brotherhoodId) {
		ModelAndView result;

		result = new ModelAndView("authenticated/picture/create");
		result.addObject("brotherhoodId", brotherhoodId);

		return result;
	}

	@RequestMapping(value = "/picture/save", method = RequestMethod.POST, params = "save")
	public ModelAndView savePicture(String picture, int brotherhoodId) {
		ModelAndView result;
		Brotherhood brotherhood = new Brotherhood();
		brotherhood = this.brotherhoodService.findOne(brotherhoodId);

		Assert.isTrue(this.brotherhoodService.loggedBrotherhood().equals(brotherhood));

		try {
			if (picture.trim().isEmpty() || picture.trim().isEmpty() || !this.floatService.isUrl(picture)) {
				result = new ModelAndView("authenticated/picture/create");
				result.addObject("brotherhoodId", brotherhoodId);
			} else {
				this.brotherhoodService.addPicture(picture, brotherhood);
				result = new ModelAndView("redirect:list.do?brotherhoodId=" + brotherhoodId);

			}
		} catch (Throwable oops) {
			result = new ModelAndView("picture/brotherhood/createPicture");

		}

		return result;
	}
	//---------------------------------------------------------------------
	//---------------------------CREATEEDITMODELANDVIEW--------------------

	protected ModelAndView createEditModelAndView(SocialProfile socialProfile) {

		ModelAndView result;

		result = this.createEditModelAndView(socialProfile, null);

		return result;
	}

	protected ModelAndView createEditModelAndView(SocialProfile socialProfile, String messageCode) {

		ModelAndView result;

		result = new ModelAndView("authenticated/socialProfile/create");
		result.addObject("socialProfile", socialProfile);
		result.addObject("message", messageCode);

		return result;
	}

	//---------------------------------------------------------------------
	//-------------------CREATEEDITMODELANDVIEW ACTOR----------------------

	//Admin
	protected ModelAndView createEditModelAndView(Admin admin) {

		ModelAndView result;

		result = this.createEditModelAndView(admin, null);

		return result;
	}

	protected ModelAndView createEditModelAndView(Admin admin, String messageCode) {

		ModelAndView result;

		result = new ModelAndView("authenticated/edit");
		result.addObject("admin", admin);
		result.addObject("message", messageCode);

		return result;
	}

	//Brotherhood
	protected ModelAndView createEditModelAndView(Brotherhood brotherhood) {

		ModelAndView result;

		result = this.createEditModelAndView(brotherhood, null);

		return result;
	}

	protected ModelAndView createEditModelAndView(Brotherhood brotherhood, String messageCode) {

		ModelAndView result;

		result = new ModelAndView("authenticated/edit");
		result.addObject("brotherhood", brotherhood);
		result.addObject("message", messageCode);

		return result;
	}

	//Member
	protected ModelAndView createEditModelAndView(Member member) {

		ModelAndView result;

		result = this.createEditModelAndView(member, null);

		return result;
	}

	protected ModelAndView createEditModelAndView(Member member, String messageCode) {

		ModelAndView result;

		result = new ModelAndView("authenticated/edit");
		result.addObject("member", member);
		result.addObject("message", messageCode);

		return result;
	}

	//Sponsor
	protected ModelAndView createEditModelAndView(Sponsor sponsor) {

		ModelAndView result;

		result = this.createEditModelAndView(sponsor, null);

		return result;
	}

	protected ModelAndView createEditModelAndView(Sponsor sponsor, String messageCode) {

		ModelAndView result;

		result = new ModelAndView("authenticated/edit");
		result.addObject("sponsor", sponsor);
		result.addObject("message", messageCode);

		return result;
	}

	//---------------------------HISTORY---------------------------------------------
	//-------------------------------------------------------------------------------

	//---------------------------LINK RECORD-----------------------------------------
	//-------------------------------------------------------------------------------
	//CREATE
	@RequestMapping(value = "/linkRecord/create", method = RequestMethod.GET)
	public ModelAndView createLinkRecord() {
		ModelAndView result;
		FormObjectLinkRecord formObjectLinkRecord = this.linkRecordService.createFormObjectLinkRecord();
		result = this.createEditModelAndView(formObjectLinkRecord);

		return result;
	}

	//EDIT
	@RequestMapping(value = "/linkRecord/edit", method = RequestMethod.GET)
	public ModelAndView editLinkRecord(@RequestParam int linkRecordId) {

		ModelAndView result;
		LinkRecord linkRecord;

		linkRecord = this.linkRecordService.findOne(linkRecordId);
		if (linkRecord == null)
			return this.list();

		Assert.notNull(linkRecord);

		Brotherhood loggedBrotherhood = this.brotherhoodService.loggedBrotherhood();
		List<LinkRecord> linkRecords = loggedBrotherhood.getHistory().getLinkRecords();

		if (!(linkRecords.contains(linkRecord)))
			return this.list();

		FormObjectLinkRecord formObjectLinkRecord = this.linkRecordService.prepareFormObjectLinkRecord(linkRecordId);

		result = this.createEditModelAndView(formObjectLinkRecord);

		return result;
	}

	//SAVE
	@RequestMapping(value = "/linkRecord/edit", method = RequestMethod.POST, params = "save")
	public ModelAndView save(@Valid FormObjectLinkRecord formObjectLinkRecord, BindingResult binding) {
		ModelAndView result;

		LinkRecord linkRecord = new LinkRecord();
		linkRecord = this.linkRecordService.create();

		if (binding.hasErrors())
			result = this.createEditModelAndView(formObjectLinkRecord);
		else
			try {

				linkRecord = this.linkRecordService.reconstructFormObject(formObjectLinkRecord, binding);

				this.linkRecordService.saveLinkRecord(linkRecord);

				result = new ModelAndView("redirect:/authenticated/showProfile.do");
			} catch (Throwable oops) {
				result = this.createEditModelAndView(formObjectLinkRecord, "socialProfile.commit.error");
			}
		return result;
	}

	//DELETE
	@RequestMapping(value = "/linkRecord/edit", method = RequestMethod.POST, params = "delete")
	public ModelAndView delete(FormObjectLinkRecord formObjectLinkRecord, BindingResult binding) {

		ModelAndView result;

		LinkRecord linkRecord = this.linkRecordService.findOne(formObjectLinkRecord.getId());

		try {

			this.linkRecordService.deleteLinkRecord(linkRecord);
			result = new ModelAndView("redirect:/authenticated/showProfile.do");

		} catch (Throwable oops) {
			result = this.createEditModelAndView(formObjectLinkRecord, "socialProfile.commit.error");
		}
		return result;
	}

	//---------------------------MISCELLANEOUS RECORD-----------------------------------------
	//-------------------------------------------------------------------------------
	//CREATE
	@RequestMapping(value = "/miscellaneousRecord/create", method = RequestMethod.GET)
	public ModelAndView createMiscellaneousRecord() {
		ModelAndView result;
		MiscellaneousRecord miscellaneousRecord;

		miscellaneousRecord = this.miscellaneousRecordService.create();
		result = this.createEditModelAndView(miscellaneousRecord);

		return result;
	}

	//EDIT
	@RequestMapping(value = "/miscellaneousRecord/edit", method = RequestMethod.GET)
	public ModelAndView editMiscellaneousRecord(@RequestParam int miscellaneousRecordId) {

		ModelAndView result;
		MiscellaneousRecord miscellaneousRecord;

		Brotherhood loggedBrotherhood = this.brotherhoodService.loggedBrotherhood();

		if (loggedBrotherhood.getHistory() == null)
			return this.list();

		List<MiscellaneousRecord> miscellaneousRecords = loggedBrotherhood.getHistory().getMiscellaneousRecords();

		miscellaneousRecord = this.miscellaneousRecordService.findOne(miscellaneousRecordId);
		Assert.notNull(miscellaneousRecords);
		result = this.createEditModelAndView(miscellaneousRecord);

		if (!(miscellaneousRecords.contains(miscellaneousRecord)))
			result = this.list();
		return result;
	}

	//SAVE
	@RequestMapping(value = "/miscellaneousRecord/edit", method = RequestMethod.POST, params = "save")
	public ModelAndView save(@Valid MiscellaneousRecord miscellaneousRecord, BindingResult binding) {
		ModelAndView result;

		if (binding.hasErrors())
			result = this.createEditModelAndView(miscellaneousRecord);
		else
			try {
				this.miscellaneousRecordService.saveMiscellaneousRecord(miscellaneousRecord);
				result = new ModelAndView("redirect:/authenticated/showProfile.do");
			} catch (Throwable oops) {
				result = this.createEditModelAndView(miscellaneousRecord, "socialProfile.commit.error");
			}
		return result;
	}

	//DELETE
	@RequestMapping(value = "/miscellaneousRecord/edit", method = RequestMethod.POST, params = "delete")
	public ModelAndView delete(@Valid MiscellaneousRecord miscellaneousRecord, BindingResult binding) {

		ModelAndView result;

		try {

			this.miscellaneousRecordService.deleteMiscellaneousRecord(miscellaneousRecord);
			result = new ModelAndView("redirect:/authenticated/showProfile.do");

		} catch (Throwable oops) {
			result = this.createEditModelAndView(miscellaneousRecord, "socialProfile.commit.error");
		}
		return result;
	}

	//---------------------------LEGAL RECORD-----------------------------------------
	//--------------------------------------------------------------------------------
	//CREATE
	@RequestMapping(value = "/legalRecord/create", method = RequestMethod.GET)
	public ModelAndView createLegalRecord() {
		ModelAndView result;
		LegalRecord legalRecord;

		legalRecord = this.legalRecordService.create();
		result = this.createEditModelAndView(legalRecord);

		return result;
	}

	//EDIT
	@RequestMapping(value = "/legalRecord/edit", method = RequestMethod.GET)
	public ModelAndView editLegalRecord(@RequestParam int legalRecordId) {

		ModelAndView result;
		LegalRecord legalRecord;

		Brotherhood loggedBrotherhood = this.brotherhoodService.loggedBrotherhood();

		if (loggedBrotherhood.getHistory() == null)
			return this.list();

		List<LegalRecord> legalRecords = loggedBrotherhood.getHistory().getLegalRecords();

		legalRecord = this.legalRecordService.findOne(legalRecordId);

		if ((legalRecord == null) || !(legalRecords.contains(legalRecord)))
			result = this.list();

		result = this.createEditModelAndView(legalRecord);

		return result;
	}

	//SAVE
	@RequestMapping(value = "/legalRecord/edit", method = RequestMethod.POST, params = "save")
	public ModelAndView save(LegalRecord legalRecord, BindingResult binding) {
		ModelAndView result;

		legalRecord = this.legalRecordService.reconstruct(legalRecord, binding);

		if (binding.hasErrors())
			result = this.createEditModelAndView(legalRecord);
		else
			try {
				this.legalRecordService.saveLegalRecord(legalRecord);
				result = new ModelAndView("redirect:/authenticated/showProfile.do");
			} catch (Throwable oops) {
				result = this.createEditModelAndView(legalRecord, "socialProfile.commit.error");
			}
		return result;
	}

	//DELETE
	@RequestMapping(value = "/legalRecord/edit", method = RequestMethod.POST, params = "delete")
	public ModelAndView delete(LegalRecord legalRecord, BindingResult binding) {

		ModelAndView result;

		legalRecord = this.legalRecordService.reconstruct(legalRecord, binding);

		try {

			this.legalRecordService.deleteLegalRecord(legalRecord);
			result = new ModelAndView("redirect:/authenticated/showProfile.do");

		} catch (Throwable oops) {
			result = this.createEditModelAndView(legalRecord, "socialProfile.commit.error");
		}
		return result;
	}

	//---------------------------PERIOD RECORD-----------------------------------------
	//-------------------------------------------------------------------------------
	//CREATE
	@RequestMapping(value = "/periodRecord/create", method = RequestMethod.GET)
	public ModelAndView createPeriodRecord() {
		ModelAndView result;
		PeriodRecord periodRecord;

		periodRecord = this.periodRecordService.create();
		result = this.createEditModelAndView(periodRecord);

		return result;
	}

	//EDIT
	@RequestMapping(value = "/periodRecord/edit", method = RequestMethod.GET)
	public ModelAndView editPeriodRecord(@RequestParam int periodRecordId) {

		ModelAndView result;
		PeriodRecord periodRecord;

		Brotherhood loggedBrotherhood = this.brotherhoodService.loggedBrotherhood();

		if (loggedBrotherhood.getHistory() == null)
			return this.list();

		List<PeriodRecord> periodRecords = loggedBrotherhood.getHistory().getPeriodRecords();

		periodRecord = this.periodRecordService.findOne(periodRecordId);

		if (!(periodRecords.contains(periodRecord)) || periodRecord == null)
			result = this.list();

		result = this.createEditModelAndView(periodRecord);

		return result;
	}

	//SAVE
	@RequestMapping(value = "/periodRecord/edit", method = RequestMethod.POST, params = "save")
	public ModelAndView save(PeriodRecord periodRecord, BindingResult binding) {
		ModelAndView result;

		periodRecord = this.periodRecordService.reconstruct(periodRecord, binding);

		if (binding.hasErrors())
			result = this.createEditModelAndView(periodRecord);
		else
			try {

				//Comprobacion a�o inicio y fin	
				if (periodRecord.getStartYear() > periodRecord.getEndYear())
					if (LocaleContextHolder.getLocale().getLanguage().toUpperCase().contains("ES")) {
						binding.addError(new FieldError("periodRecord", "startYear", periodRecord.getStartYear(), false, null, null, "El a&ntildeo de inicio debe ser anterior al de fin"));
						return this.createEditModelAndView(periodRecord);
					} else {
						binding.addError(new FieldError("periodRecord", "startYear", periodRecord.getStartYear(), false, null, null, "Start year must be previous to the End year"));
						return this.createEditModelAndView(periodRecord);
					}

				this.periodRecordService.savePeriodRecord(periodRecord);
				result = new ModelAndView("redirect:/authenticated/showProfile.do");
			} catch (Throwable oops) {
				result = this.createEditModelAndView(periodRecord, "socialProfile.commit.error");
			}
		return result;
	}

	//DELETE
	@RequestMapping(value = "/periodRecord/edit", method = RequestMethod.POST, params = "delete")
	public ModelAndView delete(PeriodRecord periodRecord, BindingResult binding) {

		ModelAndView result;

		periodRecord = this.periodRecordService.reconstruct(periodRecord, binding);

		try {

			this.periodRecordService.deletePeriodRecord(periodRecord);
			result = new ModelAndView("redirect:/authenticated/showProfile.do");

		} catch (Throwable oops) {
			result = this.createEditModelAndView(periodRecord, "socialProfile.commit.error");
		}
		return result;
	}

	//---------------------------INCEPTION RECORD------------------------------------
	//-------------------------------------------------------------------------------
	//CREATE
	@RequestMapping(value = "/inceptionRecord/create", method = RequestMethod.GET)
	public ModelAndView createInceptionRecord() {
		ModelAndView result;
		InceptionRecord inceptionRecord;

		inceptionRecord = this.inceptionRecordService.create();
		result = this.createEditModelAndView(inceptionRecord);

		return result;
	}

	//EDIT
	@RequestMapping(value = "/inceptionRecord/edit", method = RequestMethod.GET)
	public ModelAndView editInceptionRecord() {

		ModelAndView result;
		InceptionRecord inceptionRecord;

		inceptionRecord = this.inceptionRecordService.prepareEditInceptionRecord();

		result = this.createEditModelAndView(inceptionRecord);

		return result;
	}

	//SAVE
	@RequestMapping(value = "/inceptionRecord/edit", method = RequestMethod.POST, params = "save")
	public ModelAndView save(InceptionRecord inceptionRecord, BindingResult binding) {
		ModelAndView result;

		inceptionRecord = this.inceptionRecordService.reconstruct(inceptionRecord, binding);

		if (binding.hasErrors())
			result = this.createEditModelAndView(inceptionRecord);
		else
			try {

				this.inceptionRecordService.saveInceptionRecord(inceptionRecord);
				result = new ModelAndView("redirect:/authenticated/showProfile.do");

			} catch (Throwable oops) {
				result = this.createEditModelAndView(inceptionRecord, "socialProfile.commit.error");
			}
		return result;
	}
	//INCEPTION RECORD PHOTOS
	//LIST
	@RequestMapping(value = "/inceptionRecord/photo/list", method = RequestMethod.GET)
	public ModelAndView listPicturesInceptionRecord() {

		ModelAndView result;

		List<String> photos;

		Brotherhood loggedBrotherhood;
		loggedBrotherhood = this.brotherhoodService.loggedBrotherhood();

		if (loggedBrotherhood.getHistory() == null)
			return this.list();

		InceptionRecord inceptionRecord = loggedBrotherhood.getHistory().getInceptionRecord();

		Assert.notNull(loggedBrotherhood);

		photos = loggedBrotherhood.getHistory().getInceptionRecord().getPhotos();

		result = new ModelAndView("authenticated/inceptionRecord/photo/list");

		result.addObject("photos", photos);
		result.addObject("requestURI", "authenticated/inceptionRecord/photo/list.do");

		return result;
	}

	//CREATE
	@RequestMapping(value = "/inceptionRecord/photo/create", method = RequestMethod.GET)
	public ModelAndView createPhoto() {
		ModelAndView result;

		result = new ModelAndView("authenticated/inceptionRecord/photo/create");

		return result;
	}

	//SAVE
	@RequestMapping(value = "/inceptionRecord/photo/save", method = RequestMethod.POST, params = "save")
	public ModelAndView savePhotoInceptionRecord(String picture) {
		ModelAndView result;
		Brotherhood brotherhood = new Brotherhood();
		brotherhood = this.brotherhoodService.loggedBrotherhood();

		Assert.notNull(brotherhood);

		try {
			if (picture.trim().isEmpty() || picture.trim().isEmpty() || !this.floatService.isUrl(picture))
				result = new ModelAndView("authenticated/inceptionRecord/photo/create");
			else {
				this.inceptionRecordService.addPicture(picture);
				result = new ModelAndView("authenticated/inceptionRecord/photo/list");
				result.addObject("photos", brotherhood.getHistory().getInceptionRecord().getPhotos());

			}
		} catch (Throwable oops) {
			result = new ModelAndView("authenticated/inceptionRecord/photo/create");

		}

		return result;
	}

	@RequestMapping(value = "/inceptionRecord/photo/delete", method = RequestMethod.POST, params = "delete")
	public ModelAndView delete(String picture) {

		ModelAndView result;

		this.brotherhoodService.loggedAsBrotherhood();
		Brotherhood loggedBrotherhood = this.brotherhoodService.loggedBrotherhood();
		Assert.notNull(loggedBrotherhood);

		if (loggedBrotherhood.getHistory() == null)
			return this.list();

		InceptionRecord inceptionRecord = loggedBrotherhood.getHistory().getInceptionRecord();

		try {

			this.inceptionRecordService.removePhoto(picture);

			result = new ModelAndView("redirect:/authenticated/showProfile.do");

		} catch (Throwable oops) {
			result = this.createEditModelAndView(inceptionRecord, "socialProfile.commit.error");
		}
		return result;
	}

	//PERIOD RECORD PHOTOS
	//LIST
	@RequestMapping(value = "/periodRecord/photo/list", method = RequestMethod.GET)
	public ModelAndView listPicturesPeriodRecord(@RequestParam int periodRecordId) {

		ModelAndView result;

		List<String> photos;

		Brotherhood loggedBrotherhood;

		loggedBrotherhood = this.brotherhoodService.loggedBrotherhood();

		Assert.notNull(loggedBrotherhood);
		Assert.notNull(loggedBrotherhood.getHistory());

		PeriodRecord periodRecord = this.periodRecordService.findOne(periodRecordId);
		Assert.notNull(periodRecord);

		List<PeriodRecord> periodRecords = loggedBrotherhood.getHistory().getPeriodRecords();

		if (!periodRecords.contains(periodRecord))
			return this.list();

		photos = periodRecord.getPhotos();

		result = new ModelAndView("authenticated/periodRecord/photo/list");

		result.addObject("photos", photos);
		result.addObject("requestURI", "authenticated/periodRecord/photo/list.do");
		result.addObject("periodRecordId", periodRecordId);

		return result;
	}

	//CREATE
	@RequestMapping(value = "/periodRecord/photo/create", method = RequestMethod.GET)
	public ModelAndView createPhotoPeriodRecord(@RequestParam int periodRecordId) {
		ModelAndView result;

		PeriodRecord periodRecord = this.periodRecordService.findOne(periodRecordId);

		Brotherhood loggedBrotherhood = this.brotherhoodService.loggedBrotherhood();

		if ((periodRecord == null) || (loggedBrotherhood.getHistory() == null) || !loggedBrotherhood.getHistory().getPeriodRecords().contains(periodRecord))
			return this.list();

		result = new ModelAndView("authenticated/periodRecord/photo/create");
		result.addObject("periodRecordId", periodRecordId);

		return result;
	}

	//SAVE
	@RequestMapping(value = "/periodRecord/photo/save", method = RequestMethod.POST, params = "save")
	public ModelAndView savePhotoPeriodRecord(String picture, @RequestParam int periodRecordId) {
		ModelAndView result;
		Brotherhood brotherhood = new Brotherhood();
		brotherhood = this.brotherhoodService.loggedBrotherhood();

		Assert.notNull(brotherhood);
		Assert.notNull(brotherhood.getHistory());

		PeriodRecord periodRecord = this.periodRecordService.findOne(periodRecordId);
		if (!brotherhood.getHistory().getPeriodRecords().contains(periodRecord))
			return this.list();

		try {
			if (picture.trim().isEmpty() || picture.trim().isEmpty() || !this.floatService.isUrl(picture)) {
				result = new ModelAndView("authenticated/periodRecord/photo/create");
				result.addObject("periodRecordId", periodRecordId);
			} else {
				this.periodRecordService.addPicture(periodRecord, picture);
				result = this.list();
			}

		} catch (Throwable oops) {
			result = new ModelAndView("authenticated/periodRecord/photo/create");
			result.addObject("periodRecordId", periodRecordId);
		}

		return result;
	}

	@RequestMapping(value = "/periodRecord/photo/delete", method = RequestMethod.POST, params = "delete")
	public ModelAndView deletePhotoPeriodRecord(String picture, @RequestParam int periodRecordId) {

		ModelAndView result;

		this.brotherhoodService.loggedAsBrotherhood();
		Brotherhood loggedBrotherhood = this.brotherhoodService.loggedBrotherhood();
		Assert.notNull(loggedBrotherhood);

		if (loggedBrotherhood.getHistory() == null)
			return this.list();

		PeriodRecord periodRecord = this.periodRecordService.findOne(periodRecordId);
		Assert.notNull(periodRecord);

		if (!loggedBrotherhood.getHistory().getPeriodRecords().contains(periodRecord))
			return this.list();

		try {

			this.periodRecordService.removePhoto(periodRecord, picture);

			result = new ModelAndView("redirect:/authenticated/showProfile.do");

		} catch (Throwable oops) {
			result = this.createEditModelAndView(periodRecord, "socialProfile.commit.error");
			result.addObject("periodRecordId", periodRecordId);
		}
		return result;
	}

	//LEGAL RECORD LAWS
	//LIST
	@RequestMapping(value = "/legalRecord/law/list", method = RequestMethod.GET)
	public ModelAndView listLawsLegalRecord(@RequestParam int legalRecordId) {

		ModelAndView result;

		List<String> laws;

		Brotherhood loggedBrotherhood;

		loggedBrotherhood = this.brotherhoodService.loggedBrotherhood();

		Assert.notNull(loggedBrotherhood);
		Assert.notNull(loggedBrotherhood.getHistory());

		LegalRecord legalRecord = this.legalRecordService.findOne(legalRecordId);
		Assert.notNull(legalRecord);

		List<LegalRecord> legalRecords = loggedBrotherhood.getHistory().getLegalRecords();

		if (!legalRecords.contains(legalRecord))
			return this.list();

		laws = legalRecord.getLaws();

		result = new ModelAndView("authenticated/legalRecord/law/list");

		result.addObject("laws", laws);
		result.addObject("requestURI", "authenticated/legalRecord/law/list.do");
		result.addObject("legalRecordId", legalRecordId);

		return result;
	}

	//CREATE
	@RequestMapping(value = "/legalRecord/law/create", method = RequestMethod.GET)
	public ModelAndView createLawLegalRecord(@RequestParam int legalRecordId) {
		ModelAndView result;

		LegalRecord legalRecord = this.legalRecordService.findOne(legalRecordId);

		Brotherhood loggedBrotherhood = this.brotherhoodService.loggedBrotherhood();

		if ((legalRecord == null) || (loggedBrotherhood.getHistory() == null) || !loggedBrotherhood.getHistory().getLegalRecords().contains(legalRecord))
			return this.list();

		result = new ModelAndView("authenticated/legalRecord/law/create");
		result.addObject("legalRecordId", legalRecordId);

		return result;
	}

	//SAVE
	@RequestMapping(value = "/legalRecord/law/save", method = RequestMethod.POST, params = "save")
	public ModelAndView saveLawLegalRecord(String law, @RequestParam int legalRecordId) {
		ModelAndView result;
		Brotherhood brotherhood = new Brotherhood();
		brotherhood = this.brotherhoodService.loggedBrotherhood();

		Assert.notNull(brotherhood);
		Assert.notNull(brotherhood.getHistory());

		LegalRecord legalRecord = this.legalRecordService.findOne(legalRecordId);
		if (!brotherhood.getHistory().getLegalRecords().contains(legalRecord))
			return this.list();

		try {
			if (law.trim().isEmpty()) {
				result = new ModelAndView("authenticated/legalRecord/law/create");
				result.addObject("legalRecordId", legalRecordId);
			} else {
				this.legalRecordService.addLaw(legalRecord, law);
				result = this.list();
			}

		} catch (Throwable oops) {
			result = new ModelAndView("authenticated/legalRecord/law/create");
			result.addObject("legalRecordId", legalRecordId);
		}

		return result;
	}

	@RequestMapping(value = "/legalRecord/law/delete", method = RequestMethod.POST, params = "delete")
	public ModelAndView deleteLawLegalRecord(String law, @RequestParam int legalRecordId) {

		ModelAndView result;

		this.brotherhoodService.loggedAsBrotherhood();
		Brotherhood loggedBrotherhood = this.brotherhoodService.loggedBrotherhood();
		Assert.notNull(loggedBrotherhood);

		if (loggedBrotherhood.getHistory() == null)
			return this.list();

		LegalRecord legalRecord = this.legalRecordService.findOne(legalRecordId);
		Assert.notNull(legalRecordId);

		if (!loggedBrotherhood.getHistory().getLegalRecords().contains(legalRecord))
			return this.list();

		try {

			this.legalRecordService.removeLaw(legalRecord, law);

			result = new ModelAndView("redirect:/authenticated/showProfile.do");

		} catch (Throwable oops) {
			result = this.createEditModelAndView(legalRecord, "socialProfile.commit.error");
			result.addObject("legalRecordId", legalRecordId);
		}
		return result;
	}

	//---------------------------------------------------------------------
	//-------------------CREATEEDITMODELANDVIEW HISTORY--------------------

	//Link Record
	protected ModelAndView createEditModelAndView(FormObjectLinkRecord formObjectLinkRecord) {

		ModelAndView result;

		result = this.createEditModelAndView(formObjectLinkRecord, null);

		return result;
	}

	protected ModelAndView createEditModelAndView(FormObjectLinkRecord formObjectLinkRecord, String messageCode) {

		ModelAndView result;

		Brotherhood loggedBrotherhood = this.brotherhoodService.loggedBrotherhood();

		Map<Integer, String> map = new HashMap<>();

		//map = this.brotherhoodService.getMapBrotherhoods();
		List<Brotherhood> brotherhoods = this.brotherhoodService.findAll();
		brotherhoods.remove(loggedBrotherhood);

		result = new ModelAndView("authenticated/linkRecord");
		result.addObject("formObjectLinkRecord", formObjectLinkRecord);
		result.addObject("brotherhoods", brotherhoods);
		result.addObject("message", messageCode);

		return result;
	}

	//Miscellaneous Record
	protected ModelAndView createEditModelAndView(MiscellaneousRecord miscellaneousRecord) {

		ModelAndView result;

		result = this.createEditModelAndView(miscellaneousRecord, null);

		return result;
	}

	protected ModelAndView createEditModelAndView(MiscellaneousRecord miscellaneousRecord, String messageCode) {

		ModelAndView result;

		result = new ModelAndView("authenticated/miscellaneousRecord");
		result.addObject("miscellaneousRecord", miscellaneousRecord);
		result.addObject("message", messageCode);

		return result;
	}

	//Legal Record
	protected ModelAndView createEditModelAndView(LegalRecord legalRecord) {

		ModelAndView result;

		result = this.createEditModelAndView(legalRecord, null);

		return result;
	}

	protected ModelAndView createEditModelAndView(LegalRecord legalRecord, String messageCode) {

		ModelAndView result;

		result = new ModelAndView("authenticated/legalRecord");
		result.addObject("legalRecord", legalRecord);
		result.addObject("message", messageCode);

		return result;
	}

	//Period Record
	protected ModelAndView createEditModelAndView(PeriodRecord periodRecord) {

		ModelAndView result;

		result = this.createEditModelAndView(periodRecord, null);

		return result;
	}

	protected ModelAndView createEditModelAndView(PeriodRecord periodRecord, String messageCode) {

		ModelAndView result;

		result = new ModelAndView("authenticated/periodRecord");
		result.addObject("periodRecord", periodRecord);
		result.addObject("message", messageCode);

		return result;
	}

	//Inception Record
	protected ModelAndView createEditModelAndView(InceptionRecord inceptionRecord) {

		ModelAndView result;

		result = this.createEditModelAndView(inceptionRecord, null);

		return result;
	}

	protected ModelAndView createEditModelAndView(InceptionRecord inceptionRecord, String messageCode) {

		ModelAndView result;

		result = new ModelAndView("authenticated/inceptionRecord");
		result.addObject("inceptionRecord", inceptionRecord);
		result.addObject("message", messageCode);

		return result;
	}

	//Chapter
	protected ModelAndView createEditModelAndView(Chapter chapter) {

		ModelAndView result;

		result = this.createEditModelAndView(chapter, null);

		return result;
	}

	protected ModelAndView createEditModelAndView(Chapter chapter, String messageCode) {

		ModelAndView result;

		result = new ModelAndView("authenticated/edit");
		result.addObject("chapter", chapter);
		result.addObject("message", messageCode);

		return result;
	}
}
