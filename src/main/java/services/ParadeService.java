
package services;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.transaction.Transactional;

import org.joda.time.LocalDate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;

import domain.Area;
import domain.Brotherhood;
import domain.Float;
import domain.Parade;
import domain.ParadeStatus;
import domain.Path;
import domain.Request;
import forms.FormObjectParadeFloat;
import forms.FormObjectParadeFloatCheckbox;
import repositories.ParadeRepository;
import utilities.RandomString;

@Service
@Transactional
public class ParadeService {

	// Managed repository ------------------------------------------

	@Autowired
	private ParadeRepository paradeRepository;
	@Autowired
	private BrotherhoodService brotherhoodService;

	// Simple CRUD methods ------------------------------------------

	public Parade create() {

		// Asegurar que está logueado como Brotherhood
		// Asegurar que la Brotherhood logueada tiene un área
		this.brotherhoodService.loggedAsBrotherhood();
		Brotherhood loggedBrotherhood = this.brotherhoodService.loggedBrotherhood();
		Assert.isTrue(!(loggedBrotherhood.getArea().equals(null)));

		final Parade parade = new Parade();

		final List<Float> floats = new ArrayList<>();
		List<Path> paths = new ArrayList<>();

		parade.setPaths(paths);
		parade.setFloats(floats);

		parade.setColumnNumber(0);
		parade.setDescription("");
		parade.setIsDraftMode(true);
		parade.setMoment(null);

		List<Request> requests = new ArrayList<>();
		parade.setRequests(requests);

		parade.setRowNumber(0);

		String ticker = this.generateTicker();
		parade.setTicker(ticker);

		parade.setTitle("");

		return parade;
	}

	public Parade edit(Parade parade, int columnNumber, int rowNumber, String description, boolean isDraftMode,
			String title, Date moment) {

		// Security
		this.brotherhoodService.loggedAsBrotherhood();
		final Brotherhood loggedBrotherhood = this.brotherhoodService.loggedBrotherhood();
		Assert.isTrue(!(loggedBrotherhood.getArea().equals(null)));
		Assert.isTrue(parade.getIsDraftMode());
		Assert.isTrue(loggedBrotherhood.getParades().contains(parade));

		List<Parade> parades = loggedBrotherhood.getParades();
		parades.remove(parade);

		// parade.setFloats(floats);
		parade.setColumnNumber(columnNumber);
		parade.setDescription(description);
		parade.setIsDraftMode(isDraftMode);
		parade.setMoment(moment);
		// parade.setRequests(requests);
		parade.setRowNumber(rowNumber);
		// parade.setTicker(ticker);

		parade.setTitle(title);

		final Parade saved = this.save(parade);
		parades.add(saved);
		loggedBrotherhood.setParades(parades);

		this.brotherhoodService.save(loggedBrotherhood);

		return saved;
	}

	public void deleteParade(Parade parade) {

		// Security
		this.brotherhoodService.loggedAsBrotherhood();
		final Brotherhood loggedBrotherhood = this.brotherhoodService.loggedBrotherhood();
		Assert.isTrue(!(loggedBrotherhood.getArea().equals(null)));
		Assert.isTrue(parade.getIsDraftMode());
		Assert.isTrue(loggedBrotherhood.getParades().contains(parade));

		// No debería tener Request porque está en Draft mode
		// Tampoco hay que preocuparse por el finder porque no se pueden buscar parades
		// en Draft mode

		final List<Float> floats = new ArrayList<>();
		parade.setFloats(floats);

		final List<Parade> parades = loggedBrotherhood.getParades();
		parades.remove(parade);
		loggedBrotherhood.setParades(parades);
		this.brotherhoodService.save(loggedBrotherhood);

		this.paradeRepository.delete(parade);
	}

	// Método auxiliar para generar el ticker-------------------------------
	private String generateTicker() {
		String res = "";
		Date date = null;
		String date1;
		String date2 = LocalDate.now().toString();
		String gen = new RandomString(6).nextString();
		List<Parade> lc = this.paradeRepository.findAll();
		SimpleDateFormat df_in = new SimpleDateFormat("yyMMdd");
		SimpleDateFormat df_output = new SimpleDateFormat("yyyy-MM-dd");
		try {
			date = df_output.parse(date2);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		date1 = df_in.format(date);
		res = res + date1 + "-" + gen;
		for (Parade c : lc)
			if (c.getTicker() == res)
				return this.generateTicker();
		return res;
	}

	public List<Parade> findAll() {
		return this.paradeRepository.findAll();
	}

	public Parade findOne(int id) {
		return this.paradeRepository.findOne(id);
	}

	public Parade save(Parade parade) {
		return this.paradeRepository.save(parade);
	}

	public void delete(Parade parade) {
		this.paradeRepository.delete(parade);
	}

	public Parade reconstruct(FormObjectParadeFloat formObjectParadeCoach, BindingResult binding) {
		Parade result = new Parade();

		result.setTitle(formObjectParadeCoach.getTitleParade());
		result.setDescription(formObjectParadeCoach.getDescriptionParade());
		result.setMoment(formObjectParadeCoach.getMoment());
		result.setIsDraftMode(formObjectParadeCoach.getIsDraftMode());
		result.setRowNumber(formObjectParadeCoach.getRowNumber());
		result.setColumnNumber(formObjectParadeCoach.getColumnNumber());
		result.setId(0);
		if (!formObjectParadeCoach.getIsDraftMode())
			result.setParadeStatus(ParadeStatus.SUBMITTED);

		result.setTicker(this.generateTicker());

		// this.validator.validate(result, binding);

		return result;
	}

	public Parade reconstructCheckbox(FormObjectParadeFloatCheckbox formObjectParadeFloatCheckbox,
			BindingResult binding) {
		Parade result = new Parade();

		if (formObjectParadeFloatCheckbox.getId() == 0)
			result.setTicker(this.generateTicker());
		else
			result = this.paradeRepository.findOne(formObjectParadeFloatCheckbox.getId());

		result.setTitle(formObjectParadeFloatCheckbox.getTitleParade());
		result.setDescription(formObjectParadeFloatCheckbox.getDescriptionParade());
		result.setMoment(formObjectParadeFloatCheckbox.getMoment());
		result.setIsDraftMode(formObjectParadeFloatCheckbox.getIsDraftMode());
		result.setRowNumber(formObjectParadeFloatCheckbox.getRowNumber());
		result.setColumnNumber(formObjectParadeFloatCheckbox.getColumnNumber());
		if (!formObjectParadeFloatCheckbox.getIsDraftMode())
			result.setParadeStatus(ParadeStatus.SUBMITTED);

		// this.validator.validate(result, binding); //YA VIENE VALIDADO

		return result;
	}

	public Parade saveAssign(Parade parade, domain.Float newFloat) {
		List<domain.Float> floats = new ArrayList<>();
		floats.add(newFloat);
		parade.setFloats(floats);
		Parade saved = new Parade();
		saved = this.paradeRepository.save(parade);

		Brotherhood brotherhood = this.brotherhoodService.loggedBrotherhood();

		brotherhood.getParades().add(saved);
		this.brotherhoodService.save(brotherhood);

		return saved;
	}

	public Parade saveAssignList(Parade parade, List<domain.Float> floats) { // TERMINADO

		// parade.getFloats().add(newFloat);

		parade.setFloats(floats);
		Parade saved = new Parade();
		saved = this.paradeRepository.save(parade);

		Brotherhood brotherhood = this.brotherhoodService.loggedBrotherhood();

		brotherhood.getParades().remove(parade); // NUEVO
		brotherhood.getParades().add(saved);
		this.brotherhoodService.save(brotherhood);

		return saved;
	}

	public FormObjectParadeFloatCheckbox prepareFormObjectParadeFloatCheckbox(int paradeId) {

		Parade parade = this.paradeRepository.findOne(paradeId);

		Assert.notNull(parade);
		this.brotherhoodService.loggedAsBrotherhood();
		Brotherhood loggedBrotherhood = this.brotherhoodService.loggedBrotherhood();
		Assert.isTrue(loggedBrotherhood.getParades().contains(parade));
		Assert.isTrue(parade.getIsDraftMode());

		FormObjectParadeFloatCheckbox result = new FormObjectParadeFloatCheckbox();

		List<Integer> floats = new ArrayList<>();
		for (domain.Float f : parade.getFloats())
			floats.add(f.getId());

		result.setColumnNumber(parade.getColumnNumber());
		result.setDescriptionParade(parade.getDescription());
		result.setFloats(floats);
		result.setIsDraftMode(parade.getIsDraftMode());
		result.setMoment(parade.getMoment());
		result.setRowNumber(parade.getRowNumber());
		result.setTitleParade(parade.getTitle());
		result.setId(paradeId);

		return result;
	}

	public void delete(FormObjectParadeFloatCheckbox formObjectParadeFloatCheckbox) {

		Parade parade = this.paradeRepository.findOne(formObjectParadeFloatCheckbox.getId());

		this.brotherhoodService.loggedAsBrotherhood();

		Brotherhood loggedBrotherhood = this.brotherhoodService.loggedBrotherhood();

		Assert.notNull(parade);
		Assert.isTrue(loggedBrotherhood.getParades().contains(parade));
		Assert.isTrue(parade.getIsDraftMode());

		parade.setFloats(new ArrayList<domain.Float>());

		loggedBrotherhood.getParades().remove(parade);
		this.brotherhoodService.save(loggedBrotherhood);

		this.paradeRepository.delete(parade);

	}

	public List<Parade> getParadesByArea(Area area) {
		return this.paradeRepository.getParadesByArea(area);
	}

	public Parade reconstrucParadeStatus(Parade parade) {
		Parade rParade = new Parade();

		rParade = this.findOne(parade.getId());

		rParade.setParadeStatus(parade.getParadeStatus());
		rParade.setRejectedReason(parade.getRejectedReason());

		return rParade;
	}

}
