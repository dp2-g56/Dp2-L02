
package controllers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import services.BrotherhoodService;
import services.FloatService;
import services.ProcessionService;
import domain.Brotherhood;
import domain.Float;
import domain.Procession;
import domain.Request;
import forms.FormObjectProcessionFloat;
import forms.FormObjectProcessionFloatCheckbox;

@Controller
@RequestMapping("/procession/brotherhood")
public class ProcessionController extends AbstractController {

	@Autowired
	private ProcessionService	processionService;
	@Autowired
	private BrotherhoodService	brotherhoodService;
	@Autowired
	private FloatService		floatService;


	public ProcessionController() {
		super();
	}

	//-------------------------------------------------------------------
	//---------------------------LIST------------------------------------

	//Listar Processions
	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public ModelAndView list() {

		ModelAndView result;
		List<Procession> processions;

		this.brotherhoodService.loggedAsBrotherhood();

		Brotherhood loggedBrotherhood = this.brotherhoodService.loggedBrotherhood();

		Boolean hasArea = !(loggedBrotherhood.getArea() == null);

		processions = loggedBrotherhood.getProcessions();

		result = new ModelAndView("procession/brotherhood/list");
		result.addObject("processions", processions);
		result.addObject("requestURI", "procession/brotherhood/list.do");
		result.addObject("hasArea", hasArea);

		return result;
	}

	@RequestMapping(value = "/float/list", method = RequestMethod.GET)
	public ModelAndView list(@RequestParam int processionId) {
		ModelAndView result;

		this.brotherhoodService.loggedAsBrotherhood();
		Brotherhood loggedBrotherhood = this.brotherhoodService.loggedBrotherhood();

		Boolean hasArea = !(loggedBrotherhood.getArea() == null);

		List<Float> allFloats = new ArrayList<Float>();

		Procession procession = this.processionService.findOne(processionId);
		allFloats = procession.getFloats();

		result = new ModelAndView("float/brotherhood/list");

		result.addObject("allFloats", allFloats);
		result.addObject("requestURI", "float/brotherhood/list.do");
		result.addObject("hasArea", hasArea);
		result.addObject("processionId", processionId);
		result.addObject("restriction", true);

		return result;
	}

	@RequestMapping(value = "/request/list", method = RequestMethod.GET)
	public ModelAndView listRequest(@RequestParam int processionId) {
		ModelAndView result;

		this.brotherhoodService.loggedAsBrotherhood();
		Brotherhood loggedBrotherhood = this.brotherhoodService.loggedBrotherhood();

		Boolean hasArea = !(loggedBrotherhood.getArea() == null);

		List<Request> requests = new ArrayList<Request>();

		Procession procession = this.processionService.findOne(processionId);
		requests = procession.getRequests();

		result = new ModelAndView("procession/brotherhood/requests");

		result.addObject("requests", requests);
		result.addObject("requestURI", "procession/brotherhood/request/list.do");
		result.addObject("hasArea", hasArea);
		result.addObject("processionId", processionId);

		return result;
	}

	//CREATE PROCESSION Y CHECKBOX
	@RequestMapping(value = "/createCheckbox", method = RequestMethod.GET)
	public ModelAndView createProcessionCheckbox() {
		ModelAndView result;
		Procession procession;

		FormObjectProcessionFloatCheckbox formObjectProcessionFloatCheckbox = new FormObjectProcessionFloatCheckbox();

		//NUEVO
		List<Integer> floats = new ArrayList<>();
		formObjectProcessionFloatCheckbox.setFloats(floats);
		//FIN NUEVO

		result = this.createEditModelAndView(formObjectProcessionFloatCheckbox);

		return result;
	}

	//EDIT PROCESSION Y CHECKBOX
	@RequestMapping(value = "/editCheckbox", method = RequestMethod.GET)
	public ModelAndView edit(@RequestParam int processionId) {
		ModelAndView result;
		Procession procesion;
		procesion = this.processionService.findOne(processionId);
		Brotherhood brother = new Brotherhood();
		brother = this.brotherhoodService.loggedBrotherhood();

		if (!procesion.getIsDraftMode())
			return this.list();

		if (!(brother.getProcessions().contains(procesion)))
			return this.list();

		FormObjectProcessionFloatCheckbox formObjectProcessionFloatCheckbox = this.processionService.prepareFormObjectProcessionFloatCheckbox(processionId);

		result = this.createEditModelAndView(formObjectProcessionFloatCheckbox);

		return result;

	}
	@RequestMapping(value = "/editCheckbox", method = RequestMethod.POST, params = "save")
	public ModelAndView save(@Valid FormObjectProcessionFloatCheckbox formObjectProcessionFloatCheckbox, BindingResult binding) {

		ModelAndView result;

		Procession procession = new Procession();
		procession = this.processionService.create();

		if (binding.hasErrors())
			result = this.createEditModelAndView(procession);
		else
			try {

				List<domain.Float> floats = this.floatService.reconstructList(formObjectProcessionFloatCheckbox);

				procession = this.processionService.reconstructCheckbox(formObjectProcessionFloatCheckbox, binding);

				this.processionService.saveAssignList(procession, floats);

				result = new ModelAndView("redirect:/procession/brotherhood/list.do");

			} catch (Throwable oops) {

				result = this.createEditModelAndView(procession, "brotherhood.commit.error");

			}
		return result;
	}

	//MODEL AND VIEW PROCESSION CHECKBOX
	protected ModelAndView createEditModelAndView(FormObjectProcessionFloatCheckbox formObjectProcessionFloatCheckbox) {
		ModelAndView result;

		result = this.createEditModelAndView(formObjectProcessionFloatCheckbox, null);

		return result;
	}

	protected ModelAndView createEditModelAndView(FormObjectProcessionFloatCheckbox formObjectProcessionFloatCheckbox, String messageCode) {
		ModelAndView result;

		Map<Integer, String> map = new HashMap<>();

		map = this.floatService.getMapAvailableFloats();

		result = new ModelAndView("procession/brotherhood/createCheckbox");
		result.addObject("formObjectProcessionFloatCheckbox", formObjectProcessionFloatCheckbox);
		result.addObject("message", messageCode);
		result.addObject("map", map);
		result.addObject("processionId", formObjectProcessionFloatCheckbox.getId());

		return result;
	}

	//MODEL AND VIEW PROCESSION
	protected ModelAndView createEditModelAndView(Procession procession) {
		ModelAndView result;

		result = this.createEditModelAndView(procession, null);

		return result;
	}

	protected ModelAndView createEditModelAndView(Procession procession, String messageCode) {
		ModelAndView result;

		Map<Integer, String> map = new HashMap<>();

		map = this.floatService.getMapAvailableFloats();

		result = new ModelAndView("procession/brotherhood/createCheckbox");
		result.addObject("procession", procession);
		result.addObject("message", messageCode);
		result.addObject("map", map);
		result.addObject("processionId", procession.getId());

		return result;
	}

	//-------------------------------------------------------------------
	//---------------------------DELETE----------------------------------
	@RequestMapping(value = "/editCheckbox", method = RequestMethod.POST, params = "delete")
	public ModelAndView delete(FormObjectProcessionFloatCheckbox formObjectProcessionFloatCheckbox, BindingResult binding) {

		ModelAndView result;

		try {
			this.processionService.delete(formObjectProcessionFloatCheckbox);
			result = new ModelAndView("redirect:/procession/brotherhood/list.do");
		} catch (Throwable oops) {
			result = this.createEditModelAndView(formObjectProcessionFloatCheckbox, "procession.commit.error");
		}
		return result;
	}

	//-----------------------------------------------------------------------------------------
	//---------------------------PROCESSION Y FLOAT A LA VEZ ----------------------------------
	@RequestMapping(value = "/create", method = RequestMethod.POST, params = "save")
	public ModelAndView save(@Valid FormObjectProcessionFloat formObjectProcessionFloat, BindingResult binding) {

		ModelAndView result;

		Procession procession = new Procession();
		procession = this.processionService.create();

		domain.Float coach = new domain.Float();
		coach = this.floatService.create();

		procession = this.processionService.reconstruct(formObjectProcessionFloat, binding);
		coach = this.floatService.reconstructForm(formObjectProcessionFloat, binding);

		if (binding.hasErrors())
			result = this.createEditModelAndView1(procession);
		else
			try {
				domain.Float savedFloat = this.floatService.save(coach);
				this.processionService.saveAssign(procession, savedFloat);
				//this.processionService.save(procession);
				result = new ModelAndView("redirect:/procession/brotherhood/list.do");
			} catch (Throwable oops) {
				result = this.createEditModelAndView1(procession, "brotherhood.commit.error");

			}
		return result;
	}

	//CREATE PROCESSION
	@RequestMapping(value = "/create", method = RequestMethod.GET)
	public ModelAndView createProcession() {
		ModelAndView result;
		Procession procession;

		FormObjectProcessionFloat formObjectProcessionFloat = new FormObjectProcessionFloat();

		result = this.createEditModelAndView1(formObjectProcessionFloat);

		return result;
	}

	//MODEL AND VIEW PROCESSION
	protected ModelAndView createEditModelAndView1(FormObjectProcessionFloat formObjectProcessionFloat) {
		ModelAndView result;

		result = this.createEditModelAndView1(formObjectProcessionFloat, null);

		return result;
	}

	protected ModelAndView createEditModelAndView1(FormObjectProcessionFloat formObjectProcessionFloat, String messageCode) {
		ModelAndView result;

		result = new ModelAndView("procession/brotherhood/create");
		result.addObject("formObjectProcessionFloat", formObjectProcessionFloat);
		result.addObject("message", messageCode);

		return result;
	}

	//MODEL AND VIEW PROCESSION
	protected ModelAndView createEditModelAndView1(Procession procession) {
		ModelAndView result;

		result = this.createEditModelAndView1(procession, null);

		return result;
	}

	protected ModelAndView createEditModelAndView1(Procession procession, String messageCode) {
		ModelAndView result;

		result = new ModelAndView("procession/brotherhood/create");
		result.addObject("procession", procession);
		result.addObject("message", messageCode);

		return result;
	}

}
