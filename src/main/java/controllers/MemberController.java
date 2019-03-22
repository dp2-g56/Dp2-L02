
package controllers;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import services.BrotherhoodService;
import services.MemberService;
import domain.Brotherhood;
import domain.Member;

@Controller
@RequestMapping("/brotherhood/member")
public class MemberController extends AbstractController {

	@Autowired
	private BrotherhoodService	brotherhoodService;

	@Autowired
	private MemberService		memberService;


	public MemberController() {
		super();
	}

	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public ModelAndView list() {
		ModelAndView result;
		List<Brotherhood> brotherhoods = new ArrayList<Brotherhood>();
		brotherhoods = this.brotherhoodService.findAll();

		result = new ModelAndView("brotherhood/member/list");

		result.addObject("brotherhoods", brotherhoods);
		result.addObject("requestURI", "brotherhood/member/list.do");
		return result;
	}

	@RequestMapping(value = "/export", method = RequestMethod.GET)
	public @ResponseBody String export(@RequestParam(value = "id", defaultValue = "-1") int id,
			HttpServletResponse response) throws IOException {

		this.memberService.loggedAsMember();

		Member member = new Member();
		member = this.memberService.findOne(id);

		// Defines un StringBuilder para construir tu string
		StringBuilder sb = new StringBuilder();

		// Cada append a�ade una linea, cada "line.separator" a�ade un salto de linea
		sb.append("Personal data:").append(System.getProperty("line.separator"));
		sb.append("Name: " + member.getName()).append(System.getProperty("line.separator"));
		sb.append("Middle name: " + member.getMiddleName()).append(System.getProperty("line.separator"));
		sb.append("Surname: " + member.getSurname()).append(System.getProperty("line.separator"));
		sb.append("Address: " + member.getAddress()).append(System.getProperty("line.separator"));
		sb.append("Email: " + member.getEmail()).append(System.getProperty("line.separator"));
		sb.append("Photo: " + member.getPhoto()).append(System.getProperty("line.separator"));
		sb.append("Phone: " + member.getPhoneNumber()).append(System.getProperty("line.separator"));
		sb.append(System.getProperty("line.separator"));
		sb.append("SocialProfiles: ").append(System.getProperty("line.separator"));

		// Este metodo te muestra los socialProfiles de la misma manera que el resto del
		// documento
		sb.append(this.memberService.SocialProfilesToString()).append(System.getProperty("line.separator"));

		if (member == null || this.memberService.loggedMember().getId() != id)
			return null;

		// Defines el nombre del archivo y la extension
		response.setContentType("text/txt");
		response.setHeader("Content-Disposition", "attachment;filename=exportDataMember.txt");

		// Con estos comandos permites su descarga cuando clickas
		ServletOutputStream outStream = response.getOutputStream();
		outStream.println(sb.toString());
		outStream.flush();
		outStream.close();

		// El return no llega nunca, es del metodo viejo
		return sb.toString();
	}

}
