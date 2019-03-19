package controllers;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import domain.Segment;
import services.ParadeService;
import services.PathService;
import services.SegmentService;

@Controller
@RequestMapping("/segment/brotherhood")
public class SegmentController {

	@Autowired
	private SegmentService segmentService;
	@Autowired
	private ParadeService paradeService;
	@Autowired
	private PathService pathService;

	// Constructors -----------------------------------------------------------

	public SegmentController() {
		super();
	}

	// Create
	@RequestMapping(value = "/edit", method = RequestMethod.GET)
	public ModelAndView edit(@RequestParam Integer segmentId, @RequestParam Integer paradeId) {
		ModelAndView result;

		Segment segment = new Segment();
		Boolean isOrigin = false;
		if (segmentId != 0)
			segment = this.segmentService.findOne(segmentId);

		if (this.segmentService.isOrigin(segment, paradeId))
			isOrigin = true;

		result = this.createEditModelAndView(segment);
		result.addObject("isOrigin", isOrigin);

		return result;
	}

	// Save
	@RequestMapping(value = "/edit", method = RequestMethod.POST, params = "save")
	public ModelAndView save(Segment segmentForm, BindingResult binding, @RequestParam Integer paradeId) {
		ModelAndView result;

		Segment segment = this.segmentService.recontruct(segmentForm, paradeId, binding);
		if (binding.hasErrors())
			result = this.createEditModelAndView(segmentForm);
		else
			try {
				System.out.println(segment);
				segment = this.segmentService.editSegment(segment, paradeId);
				System.out.println(segment);
				System.out.println(this.paradeService.findOne(paradeId).getPath().getSegments());
				result = new ModelAndView("redirect:/path/brotherhood/list.do?" + paradeId);
			} catch (Throwable oops) {
				result = this.createEditModelAndView(segmentForm, "segment.commit.error");
			}
		result.addObject("paradeId", paradeId);
		return result;

	}

	// Delete

	@RequestMapping(value = "/edit", method = RequestMethod.DELETE)
	public ModelAndView delete(Segment segmentForm, BindingResult binding, @Valid Integer paradeId) {
		ModelAndView result;
		Integer pathId = this.paradeService.findOne(paradeId).getPath().getId();
		if (binding.hasErrors())
			result = this.createEditModelAndView(segmentForm);
		else
			try {
				this.segmentService.deleteSegment(segmentForm, pathId);
				result = new ModelAndView("redirect:list.do");
			} catch (Throwable oops) {
				result = this.createEditModelAndView(segmentForm, "segment.commit.error");
				result.addObject("pathId", pathId);
			}
		return result;

	}

	// CreateEditModelAndView
	protected ModelAndView createEditModelAndView(Segment segment) {
		ModelAndView result;

		result = this.createEditModelAndView(segment, null);

		return result;
	}

	protected ModelAndView createEditModelAndView(Segment segment, String messageCode) {
		ModelAndView result;

		result = new ModelAndView("segment/brotherhood/create");

		result.addObject("segment", segment);
		result.addObject("message", messageCode);

		return result;

	}

}
