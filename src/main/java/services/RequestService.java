
package services;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Validator;

import repositories.RequestRepository;
import security.LoginService;
import security.UserAccount;
import domain.Actor;
import domain.Brotherhood;
import domain.Member;
import domain.Message;
import domain.Procession;
import domain.Request;
import domain.Status;

@Service
@Transactional
public class RequestService {

	// Managed repository ------------------------------------------

	@Autowired
	private RequestRepository	requestRepository;
	@Autowired
	private MemberService		memberService;
	@Autowired
	private ProcessionService	processionService;
	@Autowired
	private Validator			validator;
	@Autowired
	private BrotherhoodService	brotherhoodService;
	@Autowired
	private ActorService		actorService;
	@Autowired
	private MessageService		messageService;


	//Simple CRUD methods ---------------------------------------------------------------------

	public Request createRequest(Member member, Procession procession) {
		Request res = new Request();

		res.setStatus(Status.PENDING);
		res.setColumnNumber(null);
		res.setRowNumber(null);
		res.setReasonDescription(null);

		res.setMember(member);
		res.setProcession(procession);

		return res;
	}

	// Simple CRUD methods ------------------------------------------

	public Collection<Request> findAll() {
		return this.requestRepository.findAll();
	}

	public Request findOne(int id) {
		return this.requestRepository.findOne(id);
	}

	public Request save(final Request request) {
		return this.requestRepository.save(request);
	}

	public void delete(Request request) {
		this.requestRepository.delete(request);
	}

	// Other methods
	public Collection<Request> getRequestsByMember(Member member) {
		return this.requestRepository.getRequestsByMember(member);
	}

	public Collection<Request> getRequestsByMemberAndStatus(Member member, Status status) {
		return this.requestRepository.getRequestsByMemberAndStatus(member, status);
	}

	public Collection<Request> getRequestsByBrotherhood(Brotherhood brotherhood) {
		return this.requestRepository.getRequestsByBrotherhood(brotherhood);
	}

	public List<Request> getRequestsByProcessionAndStatus(Procession procession, Status status) {
		return this.requestRepository.getRequestsByProcessionAndStatus(procession, status);
	}

	public Collection<Request> getRequestsByBrotherhoodAndStatus(Brotherhood brotherhood, Status status) {
		return this.requestRepository.getRequestsByBrotherhoodAndStatus(brotherhood, status);
	}

	public Request getRequestByBrotherhoodAndRequestId(Brotherhood brotherhood, Request request) {
		return this.requestRepository.getRequestByBrotherhoodAndRequestId(brotherhood, request);
	}

	public Collection<Request> getRequestApprovedByBrotherhoodAndProcession(Brotherhood brotherhood, Procession procession) {
		return this.requestRepository.getRequestApprovedByBrotherhoodAndProcession(brotherhood, procession);
	}

	public void deleteRequestAsMember(Member member, int requestId) {
		Request request = this.findOne(requestId);

		Assert.isTrue(this.getRequestsByMember(member).contains(request));
		Assert.isTrue(request.getStatus().equals(Status.PENDING));

		Procession procession = request.getProcession();
		List<Request> requests = procession.getRequests();
		requests.remove(request);
		procession.setRequests(requests);
		this.processionService.save(procession);

		List<Request> requests2 = member.getRequests();
		requests2.remove(request);
		member.setRequests(requests2);
		this.memberService.save(member);

		this.delete(request);

	}

	public void createRequestAsMember(Member member, int processionId) {
		Procession procession = this.processionService.findOne(processionId);
		List<Request> requests = procession.getRequests();

		Assert.isTrue(procession.getIsDraftMode() == false);
		for (Request r : requests)
			Assert.isTrue(!r.getMember().equals(member));

		Request newRequest = this.createRequest(member, procession);
		Request saveRequest = this.save(newRequest);

		List<Request> requests2 = member.getRequests();
		requests2.add(saveRequest);
		member.setRequests(requests2);
		this.memberService.save(member);

		List<Request> requests3 = procession.getRequests();
		requests3.add(saveRequest);
		procession.setRequests(requests3);
		this.processionService.save(procession);
	}

	public boolean canRequest(Member member, int processionId) {
		boolean res = true;

		Procession procession = this.processionService.findOne(processionId);
		List<Request> requests = procession.getRequests();

		if (procession.getIsDraftMode() == true)
			res = false;
		if (res == true)
			for (Request r : requests)
				if (r.getMember().equals(member)) {
					res = false;
					break;
				}

		return res;
	}

	public Request saveRequestWithPreviousChecking(Request request) {
		Request requestSaved;

		if (request.getStatus().equals(Status.APPROVED)) {
			Assert.notNull(request.getColumnNumber());
			Assert.notNull(request.getRowNumber());

			Procession procession = request.getProcession();
			Collection<Request> requests = procession.getRequests();

			Integer col = request.getColumnNumber();
			Integer row = request.getRowNumber();

			Request requestFound = this.findOne(request.getId());
			if (request.getColumnNumber() != null && request.getRowNumber() != null)
				requests.remove(requestFound);

			Boolean isFree = true;
			for (Request req : requests)
				if (req.getColumnNumber() == col && req.getRowNumber() == row) {
					isFree = false;
					break;
				}

			Boolean respectMaxAndMin = col <= procession.getColumnNumber() && row <= procession.getRowNumber() && col >= 1 && row >= 1;

			Assert.isTrue(isFree && respectMaxAndMin);

		} else if (request.getStatus().equals(Status.REJECTED)) {
			Assert.notNull(request.getReasonDescription());
			Assert.isTrue(!request.getReasonDescription().trim().equals(""));
		}

		requestSaved = this.save(request);

		this.sendMessagesToActorsInvolved(requestSaved);

		return requestSaved;
	}
	public Request reconstructRequest(Request request, BindingResult binding) {
		this.brotherhoodService.securityAndBrotherhood();

		Request result = this.requestRepository.findOne(request.getId());

		Boolean approved;
		if (result.getStatus().equals(Status.APPROVED))
			approved = true;
		else
			approved = false;

		Request result2 = new Request();

		result2.setId(result.getId());
		result2.setMember(result.getMember());
		result2.setProcession(result.getProcession());
		result2.setVersion(result.getVersion());

		Integer col = request.getColumnNumber();
		Integer row = request.getRowNumber();

		if (approved == false) {
			if (request.getStatus().equals(Status.APPROVED)) {
				result2.setColumnNumber(col);
				result2.setRowNumber(row);
				result2.setReasonDescription(null);
				result2.setStatus(request.getStatus());
			} else if (request.getStatus().equals(Status.REJECTED)) {
				result2.setColumnNumber(null);
				result2.setRowNumber(null);
				result2.setReasonDescription(request.getReasonDescription());
				result2.setStatus(request.getStatus());
			} else {
				result2.setColumnNumber(null);
				result2.setRowNumber(null);
				result2.setReasonDescription(null);
				result2.setStatus(request.getStatus());
			}
		} else {
			result2.setStatus(result.getStatus());
			result2.setReasonDescription(result.getReasonDescription());

			result2.setColumnNumber(col);
			result2.setRowNumber(row);
		}

		this.validator.validate(result, binding);
		return result2;
	}
	public List<Integer> getFreePosition(Request request) {
		List<Integer> position = new ArrayList<>();

		Procession procession = request.getProcession();

		Brotherhood brotherhood = this.brotherhoodService.securityAndBrotherhood();
		List<Request> requests = (List<Request>) this.getRequestApprovedByBrotherhoodAndProcession(brotherhood, procession);

		List<String> occupedPositions = new ArrayList<>();
		for (Request r : requests)
			if (r.getRowNumber() > 0 && r.getColumnNumber() > 0)
				occupedPositions.add(r.getRowNumber() + "-" + r.getColumnNumber());

		Integer row = 0;
		Integer col = 0;

		if (occupedPositions.size() != 0) {

			List<String> allPositions = new ArrayList<>();
			for (int i = 1; i <= procession.getRowNumber(); i++)
				for (int j = 1; j <= procession.getColumnNumber(); j++)
					allPositions.add(i + "-" + j);

			for (String all : allPositions)
				if (!occupedPositions.contains(all)) {
					String[] poss = all.split("-");

					row = new Integer(poss[0]);
					col = new Integer(poss[1]);

					break;
				}
		} else {
			row = 1;
			col = 1;
		}

		position.add(row);
		position.add(col);

		return position;
	}

	public void sendMessagesToActorsInvolved(Request request) {
		//Messages
		UserAccount userAccount;
		userAccount = LoginService.getPrincipal();
		String userName = userAccount.getUsername();
		Actor brotherhood = this.actorService.getActorByUsername(userAccount.getUsername());

		String statusES = "";
		String statusEN = "";
		if (request.getStatus().equals(Status.APPROVED)) {
			statusES = "APROBADA";
			statusEN = "APPROVED";
		} else {
			statusES = "RECHAZADA";
			statusEN = "REJECTED";
		}

		String subject = "Request updated: " + statusEN + " / Solicitud actualizada: " + statusES;

		String body = "Request associated to the procession: " + request.getProcession().getTicker() + ", " + request.getProcession().getTitle() + " has been updated" + " / " + "Solicitud asociada a la procesión: " + request.getProcession().getTicker()
			+ ", " + request.getProcession().getTitle() + " ha sido actualizada";

		Message messageB = this.messageService.createNotification(subject, body, "NEUTRAL", "Notification, Request", brotherhood);
		Message messageM = this.messageService.createNotification(subject, body, "NEUTRAL", "Notification, Request", request.getMember());

		this.messageService.sendMessageAnotherSender(messageB);
		this.messageService.sendMessageAnotherSender(messageM);
	}
}
