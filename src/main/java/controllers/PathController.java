package controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import domain.Segment;
import services.ParadeService;
import services.SegmentService;

@Controller
@RequestMapping("/path/")
public class PathController {

	// Services

	@Autowired
	private SegmentService segmentService;
	@Autowired
	private ParadeService paradeService;

	// Constructors -----------------------------------------------------------

	public PathController() {
		super();
	}

	// LIST
	@RequestMapping(value = { "/brotherhood/list", "/list", "member/list" }, method = RequestMethod.GET)
	public ModelAndView paradesList(@RequestParam Integer paradeId) {
		ModelAndView result;

		List<Segment> segments = this.segmentService.getSegmentByParade(paradeId);

		result = new ModelAndView("path/brotherhood/list");

		result.addObject("segments", segments);

		return result;
	}

	// --- Brotherhood ---

	@RequestMapping(value = { "/brotherhood/create", "/brotherhood/delete" }, method = RequestMethod.GET)
	public ModelAndView pathCreateOrDelete(@RequestParam Integer paradeId) {
		ModelAndView result;

		this.paradeService.putOrDeletePath(paradeId);

		result = new ModelAndView("redirect:brotherhood/list");

		return result;

	}

}
