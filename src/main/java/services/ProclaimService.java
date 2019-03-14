
package services;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Validator;

import domain.Chapter;
import domain.Proclaim;
import repositories.ProclaimRepository;
import security.LoginService;
import security.UserAccount;

@Service
@Transactional
public class ProclaimService {

	@Autowired
	ProclaimRepository proclaimRepository;

	@Autowired
	ChapterService chapterService;

	@Autowired
	ActorService actorService;

	@Autowired
	Validator validator;

	public Proclaim findOne(int id) {
		return this.proclaimRepository.findOne(id);
	}

	public List<Proclaim> findAll() {
		return this.proclaimRepository.findAll();
	}

	public Proclaim createProclaim() {
		this.chapterService.loggedAsChapter();
		Proclaim proclaim = new Proclaim();

		Date thisMoment = new Date();
		thisMoment.setTime(thisMoment.getTime() - 1000);

		proclaim.setPublishMoment(thisMoment);
		proclaim.setDescription("");

		return proclaim;

	}

	public List<Proclaim> showProclaims() {
		this.chapterService.loggedAsChapter();
		UserAccount userAccount;
		userAccount = LoginService.getPrincipal();
		Chapter chapter = new Chapter();
		chapter = (Chapter) this.actorService.getActorByUsername(userAccount.getUsername());

		List<Proclaim> proclaims = new ArrayList<Proclaim>();

		proclaims = chapter.getProclaims();

		return proclaims;
	}

	public void isProclaimInChapter(Proclaim proclaim) {

		Chapter chapter = new Chapter();
		chapter = this.chapterService.loggedChapter();
		Assert.isTrue(chapter.getProclaims().contains(proclaim));

	}

	public Proclaim saveProclaim(Proclaim proclaim) {
		Assert.isTrue(proclaim.getId() == 0);
		this.chapterService.loggedAsChapter();

		Chapter chapter = new Chapter();
		chapter = this.chapterService.loggedChapter();
		Proclaim savedProclaim = new Proclaim();

		savedProclaim = this.proclaimRepository.save(proclaim);

		chapter.getProclaims().add(savedProclaim);
		this.chapterService.save(chapter);

		return savedProclaim;

	}

	public Proclaim reconstruct(Proclaim proclaim, BindingResult binding) {

		Proclaim result;
		result = proclaim;

		Date thisMoment = new Date();
		thisMoment.setTime(thisMoment.getTime() - 1000);

		result.setPublishMoment(thisMoment);

		this.validator.validate(result, binding);

		return result;

	}

	public void flush() {
		this.proclaimRepository.flush();
	}
}
