
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

import repositories.EnrolmentRepository;
import domain.Brotherhood;
import domain.Enrolment;
import domain.Member;
import domain.Position;
import domain.StatusEnrolment;

@Service
@Transactional
public class EnrolmentService {

	@Autowired
	private EnrolmentRepository	enrolmentRepository;

	@Autowired
	private MemberService		memberService;

	@Autowired
	private BrotherhoodService	brotherhoodService;

	@Autowired(required = false)
	private Validator			validator;


	public List<Enrolment> findAll() {
		return this.enrolmentRepository.findAll();
	}

	public Enrolment findOne(Integer enrolmentId) {
		return this.enrolmentRepository.findOne(enrolmentId);
	}

	public Enrolment save(Enrolment e) {
		return this.enrolmentRepository.save(e);
	}

	public void delete(Enrolment e) {
		this.enrolmentRepository.delete(e);
	}

	public Enrolment create() {
		Enrolment e = new Enrolment();

		Date creationMoment = new Date();
		creationMoment.setTime(creationMoment.getTime() - 1);
		Position position = null;
		StatusEnrolment statusEnrolment = StatusEnrolment.PENDING;
		Date dropOutDate = null;
		Member member = null;
		Brotherhood brotherhood = null;

		e.setCreationMoment(creationMoment);
		e.setPosition(position);
		e.setStatusEnrolment(statusEnrolment);
		e.setDropOutDate(dropOutDate);
		e.setMember(member);
		e.setBrotherhood(brotherhood);

		return e;

	}

	public Enrolment createEnrolment(Enrolment enrolment) {
		Member loggedMember = this.memberService.loggedMember();

		Enrolment enrolmentSaved = this.enrolmentRepository.save(enrolment);

		List<Enrolment> enrolments = loggedMember.getEnrolments();
		if (enrolments.contains(enrolment))
			enrolments.remove(enrolment);
		enrolments.add(enrolmentSaved);
		loggedMember.setEnrolments(enrolments);
		this.memberService.save(loggedMember);

		return enrolment;

	}

	public List<Enrolment> getEnrolmentsPerMember(Member m) {
		return m.getEnrolments();
	}

	public Enrolment createEnrolment(Brotherhood brotherhood, Enrolment enrolment, Member m) {
		Assert.notNull(m);
		this.memberService.loggedAsMember();

		enrolment = this.create();
		enrolment.setBrotherhood(brotherhood);
		enrolment.setMember(m);
		enrolment.setStatusEnrolment(StatusEnrolment.PENDING);
		List<Enrolment> enrolments = new ArrayList<>();
		enrolments = brotherhood.getEnrolments();
		enrolments.add(enrolment);
		brotherhood.setEnrolments(enrolments);

		this.enrolmentRepository.save(enrolment);
		this.brotherhoodService.save(brotherhood);
		return enrolment;
	}

	public Enrolment reconstructEnrolment(Enrolment enrolment, BindingResult binding) {
		this.brotherhoodService.securityAndBrotherhood();

		Enrolment result = this.enrolmentRepository.findOne(enrolment.getId());

		//result.setId(enrolment.getId());
		//result.setVersion(enrolment.getVersion());
		result.setPosition(enrolment.getPosition());

		if (result.getPosition() == null)
			result.setStatusEnrolment(StatusEnrolment.PENDING);
		else
			result.setStatusEnrolment(StatusEnrolment.ACCEPTED);

		this.validator.validate(result, binding);

		return result;
	}

	public Enrolment saveEnrolmentWithCheck(Enrolment enrolment) {
		Enrolment enrolmentSaved;

		Assert.isTrue(enrolment.getStatusEnrolment().equals(StatusEnrolment.ACCEPTED));
		Assert.notNull(enrolment.getPosition());
		System.out.println(enrolment.getPosition());
		Assert.notNull(enrolment.getBrotherhood());
		Assert.notNull(enrolment.getMember());
		Assert.notNull(enrolment.getCreationMoment());

		enrolmentSaved = this.save(enrolment);

		return enrolmentSaved;
	}
}
