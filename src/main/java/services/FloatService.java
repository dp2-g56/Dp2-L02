
package services;

import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Validator;

import domain.Brotherhood;
import domain.Parade;
import forms.FormObjectParadeFloat;
import forms.FormObjectParadeFloatCheckbox;
import repositories.FloatRepository;

@Service
@Transactional
public class FloatService {

	@Autowired
	private FloatRepository floatRepository;

	@Autowired
	private BrotherhoodService brotherhoodService;
	@Autowired
	private ParadeService paradeService;
	@Autowired
	private Validator validator;

	public domain.Float reconstruct(domain.Float floatt, BindingResult binding) {
		domain.Float result = new domain.Float();

		if (floatt.getId() == 0)
			result = floatt;
		else {
			domain.Float copy = this.floatRepository.findOne(floatt.getId());

			result.setId(copy.getId());
			result.setVersion(copy.getVersion());
			result.setPictures(copy.getPictures());
			result.setDescription(floatt.getDescription());
			result.setTitle(floatt.getTitle());

			// result = floatt;

			// result.setPictures(floatt.getPictures());

		}
		this.validator.validate(result, binding);
		return result;
	}

	public List<domain.Float> showAssignedFloats(Parade parade) {
		List<domain.Float> floatts = new ArrayList<domain.Float>();
		floatts = parade.getFloats();
		return floatts;
	}

	public List<domain.Float> showAllFloats() {
		List<domain.Float> floatts = new ArrayList<domain.Float>();
		floatts = this.floatRepository.findAll();
		return floatts;
	}

	public List<domain.Float> showBrotherhoodFloats() {
		Brotherhood bro = new Brotherhood();
		bro = this.brotherhoodService.loggedBrotherhood();
		List<domain.Float> floatts = new ArrayList<domain.Float>();
		floatts = bro.getFloats();
		return floatts;
	}

	public List<domain.Float> findAll() {
		return this.floatRepository.findAll();
	}

	public domain.Float findOne(final int id) {
		return this.floatRepository.findOne(id);
	}

	public void remove(domain.Float floatt) {
		// No se pueden eliminar pasos asignados a desfiles en final mode

		this.brotherhoodService.loggedAsBrotherhood();
		Brotherhood bro = new Brotherhood();
		bro = this.brotherhoodService.loggedBrotherhood();
		List<Parade> pro = new ArrayList<Parade>();

		pro = this.brotherhoodService.getParadesByBrotherhood(bro);
		Assert.isTrue(this.allParadesDraftMode(pro));
		for (final Parade p : pro)
			if (p.getFloats().contains(floatt))
				p.getFloats().remove(floatt);
		bro.getFloats().remove(floatt);
		this.floatRepository.delete(floatt);
	}

	public domain.Float save(domain.Float floatt) {

		// Obtener float list
		// quitar float antiguo y añadir el nuevo
		// Hacer set del float list modificado
		// Save parade

		// Obtener loggedBrotherhood

		// A PARTIR DE AQUI PUEDE QUE SEA OPCIONAL
		// Quitar parade antigua y añadir nueva
		// Obt

		this.brotherhoodService.loggedAsBrotherhood();
		Brotherhood loggedBrotherhood = new Brotherhood();
		domain.Float floattSaved = new domain.Float();
		loggedBrotherhood = this.brotherhoodService.loggedBrotherhood();

		Assert.isTrue(!(loggedBrotherhood.getArea().equals(null)));

		floattSaved = this.floatRepository.save(floatt);

		loggedBrotherhood.getFloats().remove(floatt);
		loggedBrotherhood.getFloats().add(floattSaved);
		this.brotherhoodService.save(loggedBrotherhood);

		return floattSaved;
	}

	public domain.Float create() {
		final domain.Float floatt = new domain.Float();
		final List<String> pictures = new ArrayList<String>();

		floatt.setPictures(pictures);
		floatt.setTitle("");
		floatt.setDescription("");

		return floatt;
	}

	public Boolean allParadesDraftMode(final List<Parade> pro) {
		final Boolean res = true;
		for (final Parade p : pro)
			if (p.getIsDraftMode() == false)
				return true;
		return res;
	}

	public void AssingFloatToParade(final domain.Float floatt, final Parade parade) {
		Assert.isTrue(parade.getIsDraftMode() == true);
		if (!(parade.getFloats().contains(floatt)))
			parade.getFloats().add(floatt);
		this.paradeService.save(parade);
	}

	public void UnAssingFloatToParade(domain.Float floatt, Parade parade) {
		Assert.isTrue(parade.getIsDraftMode() == true);
		if (parade.getFloats().contains(floatt))
			parade.getFloats().remove(floatt);
		this.paradeService.save(parade);
	}

	public domain.Float reconstructForm(FormObjectParadeFloat formObjectParadeFloat, BindingResult binding) {
		domain.Float result = new domain.Float();

		result.setTitle(formObjectParadeFloat.getTitle());
		result.setDescription(formObjectParadeFloat.getDescription());

		// this.validator.validate(result, binding);

		return result;
	}

	public Map<Integer, String> getMapAvailableFloats() {
		this.brotherhoodService.loggedAsBrotherhood();

		Brotherhood loggedBrotherhood = this.brotherhoodService.loggedBrotherhood();

		List<domain.Float> floats = loggedBrotherhood.getFloats();

		Map<Integer, String> map = new HashMap<>();

		for (domain.Float floatt : floats)
			map.put(floatt.getId(), floatt.getTitle());

		return map;
	}

	public List<domain.Float> reconstructList(FormObjectParadeFloatCheckbox formObjectParadeFloatCheckbox) {

		List<Integer> ids = formObjectParadeFloatCheckbox.getFloats();

		this.brotherhoodService.loggedAsBrotherhood();

		Brotherhood loggedBrotherhood = this.brotherhoodService.loggedBrotherhood();

		List<domain.Float> floats = new ArrayList<>();

		for (Integer id : ids) {

			domain.Float floatt = this.floatRepository.findOne(id);
			Assert.notNull(floatt);
			Assert.isTrue(loggedBrotherhood.getFloats().contains(floatt));

			floats.add(floatt);

		}
		return floats;

	}

	public List<domain.Float> floatsInParadeInFinalMode() {
		this.brotherhoodService.loggedAsBrotherhood();
		Brotherhood bro = new Brotherhood();
		bro = this.brotherhoodService.loggedBrotherhood();
		List<domain.Float> floatt = new ArrayList<domain.Float>();

		floatt = this.floatRepository.getFloatsInParadeInFinalMode(bro.getId());

		return floatt;
	}

	public domain.Float addPicture(String picture, domain.Float floatt) {
		this.brotherhoodService.loggedAsBrotherhood();
		floatt.getPictures().add(picture);
		return this.save(floatt);
	}

	public Boolean isUrl(String url) {
		try {
			new URL(url).toURI();
			return true;
		} catch (Exception e) {
			return false;
		}
	}
}
