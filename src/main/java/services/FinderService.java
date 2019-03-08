
package services;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Validator;

import repositories.FinderRepository;
import security.Authority;
import security.LoginService;
import security.UserAccount;
import domain.Finder;
import domain.Member;
import domain.Procession;

@Service
@Transactional
public class FinderService {

	// Managed repository ------------------------------------------

	@Autowired
	private FinderRepository		finderRepository;

	// Supporting Services -----------------------------------------

	@Autowired
	private MemberService			memberRepository;
	@Autowired
	private Validator				validator;
	@Autowired
	private ConfigurationService	configurationService;


	// Simple CRUD methods ------------------------------------------

	public Finder createFinder() {

		Finder finder = new Finder();

		List<Procession> processions = new ArrayList<>();

		Date lastEdit = new Date();

		lastEdit.setTime(lastEdit.getTime() - 1);

		finder.setKeyWord("");
		finder.setArea("");
		finder.setMaxDate(null);
		finder.setMinDate(null);
		finder.setLastEdit(lastEdit);
		finder.setProcessions(processions);

		return finder;

	}

	public List<Finder> findAll() {
		return this.finderRepository.findAll();
	}

	public Finder findOne(int id) {
		return this.finderRepository.findOne(id);
	}

	public Finder save(Finder finder) {
		return this.finderRepository.save(finder);
	}
	public void delete(Finder finder) {
		this.finderRepository.delete(finder);
	}

	public void filterProcessionsByFinder(Finder finder) {
		UserAccount userAccount = LoginService.getPrincipal();

		List<Authority> authorities = (List<Authority>) userAccount.getAuthorities();
		Assert.isTrue(authorities.get(0).toString().equals("MEMBER"));

		Member loggedMember = this.memberRepository.getMemberByUsername(userAccount.getUsername());

		Assert.isTrue(loggedMember.getFinder().getId() == finder.getId());

		List<Procession> filter = new ArrayList<>();
		List<Procession> result = this.getAllPublishedProcessions();

		//KeyWord
		if (!finder.getKeyWord().equals(null) && !finder.getKeyWord().equals("")) {
			filter = this.finderRepository.getProcessionsByKeyWord("%" + finder.getKeyWord() + "%");
			result.retainAll(filter);
		}
		//Area
		if (!finder.getArea().equals(null) && !finder.getArea().equals("")) {
			filter = this.finderRepository.getProcessionsByArea("%" + finder.getArea() + "%");
			result.retainAll(filter);
		}
		// Dates

		if (finder.getMinDate() != null) {
			filter = this.finderRepository.getProcessionsByMinDate(finder.getMinDate());
			result.retainAll(filter);

		}
		if (finder.getMaxDate() != null) {
			filter = this.finderRepository.getProcessionsByMaxDate(finder.getMaxDate());
			result.retainAll(filter);
		}

		finder.setProcessions(result);
		Finder finderRes = this.finderRepository.save(finder);
		loggedMember.setFinder(finderRes);
		this.memberRepository.save(loggedMember);

	}
	public List<Procession> getAllPublishedProcessions() {
		return this.finderRepository.getPublushedProcessions();
	}

	public Finder reconstruct(Finder finderForm, BindingResult binding) {
		Finder result = new Finder();

		Finder finder = this.findOne(finderForm.getId());

		result.setId(finder.getId());
		result.setVersion(finder.getVersion());
		result.setProcessions(finder.getProcessions());
		Date date = new Date();
		result.setLastEdit(date);
		result.setArea(finderForm.getArea());
		result.setKeyWord(finderForm.getKeyWord());
		result.setMaxDate(finderForm.getMaxDate());
		result.setMinDate(finderForm.getMinDate());

		this.validator.validate(result, binding);

		return result;
	}

	public Finder getCurrentFinder() {
		UserAccount userAccount = LoginService.getPrincipal();
		Member loggedMember = this.memberRepository.getMemberByUsername(userAccount.getUsername());
		return loggedMember.getFinder();
	}

	public void updateAllFinders() {

		//LastEdit Finder

		List<Finder> finders = this.findAll();
		//Current Date
		Date currentDate = new Date();

		Calendar calendar = Calendar.getInstance();
		calendar.setTime(currentDate);
		Integer currentDay = calendar.get(Calendar.DATE);
		Integer currentMonth = calendar.get(Calendar.MONTH);
		Integer currentYear = calendar.get(Calendar.YEAR);
		Integer currentHour = calendar.get(Calendar.HOUR);

		//Max Time Finder
		Integer time = this.configurationService.getConfiguration().getTimeFinder();

		//Empty List processions
		List<Procession> processions = new ArrayList<>();

		for (Finder f : finders) {

			//Last Edit Date
			Date lasEdit = f.getLastEdit();
			calendar.setTime(lasEdit);
			Integer lastEditDay = calendar.get(Calendar.DATE);
			Integer lastEditMonth = calendar.get(Calendar.MONTH);
			Integer lastEditYear = calendar.get(Calendar.YEAR);
			Integer lastEditHour = calendar.get(Calendar.HOUR);
			if (!(currentDay.equals(lastEditDay) && currentMonth.equals(lastEditMonth) && currentYear.equals(lastEditYear) && lastEditHour < (currentHour + time))) {
				f.setProcessions(processions);
				this.save(f);
			}
		}
	}
}
