
package services;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.encoding.Md5PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Validator;

import domain.Actor;
import domain.Admin;
import domain.Area;
import domain.Box;
import domain.Brotherhood;
import domain.Chapter;
import domain.Member;
import domain.Message;
import domain.Parade;
import domain.Position;
import domain.SocialProfile;
import domain.Sponsor;
import forms.FormObjectMember;
import repositories.AdminRepository;
import security.Authority;
import security.LoginService;
import security.UserAccount;

@Service
@Transactional
public class AdminService {

	@Autowired
	private AdminRepository adminRepository;

	@Autowired
	private MessageService messageService;

	@Autowired
	private ActorService actorService;

	@Autowired
	private BoxService boxService;

	@Autowired
	private ParadeService paradeService;

	@Autowired
	private RequestService requestService;

	@Autowired
	private AreaService areaService;

	@Autowired
	private PositionService positionService;

	@Autowired
	private Validator validator;

	@Autowired
	private FinderService finderService;

	@Autowired
	private MemberService memberService;

	@Autowired
	private HistoryService historyService;

	@Autowired
	private SponsorshipService sponsorshipService;

	// 1. Create user accounts for new administrators.
	public void loggedAsAdmin() {
		UserAccount userAccount;
		userAccount = LoginService.getPrincipal();
		List<Authority> authorities = (List<Authority>) userAccount.getAuthorities();
		Assert.isTrue(authorities.get(0).toString().equals("ADMIN"));
	}

	public Admin createAdmin() {

		this.loggedAsAdmin();

		Admin admin = new Admin();

		// Se crean las listas vacías
		// ACTOR
		List<SocialProfile> socialProfiles = new ArrayList<>();
		List<Box> boxes = new ArrayList<>();

		UserAccount userAccount = new UserAccount();
		userAccount.setUsername("");
		userAccount.setPassword("");

		// Actor
		admin.setAddress("");
		admin.setBoxes(boxes);
		admin.setEmail("");
		admin.setHasSpam(false);
		admin.setMiddleName("");
		admin.setName("");
		admin.setPhoneNumber("");
		admin.setPhoto("");
		admin.setPolarity(0.0);
		admin.setSocialProfiles(socialProfiles);
		admin.setSurname("");

		List<Authority> authorities = new ArrayList<Authority>();

		Authority authority = new Authority();
		authority.setAuthority(Authority.MEMBER);
		authorities.add(authority);

		userAccount.setAuthorities(authorities);
		userAccount.setIsNotLocked(true);

		admin.setUserAccount(userAccount);

		return admin;
	}

	public Admin createAdmin(String name, String middleName, String surname, String photo, String email,
			String phoneNumber, String address, String userName, String password) {

		Admin admin = new Admin();

		List<SocialProfile> socialProfiles = new ArrayList<SocialProfile>();
		List<Box> boxes = new ArrayList<Box>();

		UserAccount userAccountAdmin = new UserAccount();

		userAccountAdmin.setUsername(userName);
		userAccountAdmin.setPassword(password);

		Box spamBox = new Box();
		List<Message> messages1 = new ArrayList<>();
		spamBox.setIsSystem(true);
		spamBox.setMessages(messages1);
		spamBox.setName("Spam");

		Box trashBox = new Box();
		List<Message> messages2 = new ArrayList<>();
		trashBox.setIsSystem(true);
		trashBox.setMessages(messages2);
		trashBox.setName("Trash");

		Box sentBox = new Box();
		List<Message> messages3 = new ArrayList<>();
		sentBox.setIsSystem(true);
		sentBox.setMessages(messages3);
		sentBox.setName("Sent messages");

		Box receivedBox = new Box();
		List<Message> messages4 = new ArrayList<>();
		receivedBox.setIsSystem(true);
		receivedBox.setMessages(messages4);
		receivedBox.setName("Received messages");

		boxes.add(receivedBox);
		boxes.add(sentBox);
		boxes.add(spamBox);
		boxes.add(trashBox);

		admin.setName(name);
		admin.setMiddleName(middleName);
		admin.setSurname(surname);
		admin.setPhoto(photo);
		admin.setEmail(email);
		admin.setPhoneNumber(phoneNumber);
		admin.setAddress(address);
		admin.setSocialProfiles(socialProfiles);
		admin.setBoxes(boxes);
		admin.setHasSpam(false);

		List<Authority> authorities = new ArrayList<Authority>();

		Authority authority = new Authority();
		authority.setAuthority(Authority.ADMIN);
		authorities.add(authority);
		userAccountAdmin.setAuthorities(authorities);
		userAccountAdmin.setIsNotLocked(true);
		admin.setUserAccount(userAccountAdmin);

		return admin;
	}

	public Admin saveCreate(Admin admin) {

		List<Box> boxes = new ArrayList<>();

		// Se crean las listas vacías
		// ACTOR
		List<SocialProfile> socialProfiles = new ArrayList<>();
		admin.setSocialProfiles(socialProfiles);

		// Boxes
		Box box1 = this.boxService.createSystem();
		box1.setName("INBOX");
		Box saved1 = this.boxService.saveSystem(box1);
		boxes.add(saved1);

		Box box2 = this.boxService.createSystem();
		box2.setName("OUTBOX");
		Box saved2 = this.boxService.saveSystem(box2);
		boxes.add(saved2);

		Box box3 = this.boxService.createSystem();
		box3.setName("TRASHBOX");
		Box saved3 = this.boxService.saveSystem(box3);
		boxes.add(saved3);

		Box box4 = this.boxService.createSystem();
		box4.setName("SPAMBOX");
		Box saved4 = this.boxService.saveSystem(box4);
		boxes.add(saved4);

		Box box5 = this.boxService.createSystem();
		box5.setName("NOTIFICATIONBOX");
		Box saved5 = this.boxService.saveSystem(box5);
		boxes.add(saved5);

		admin.setBoxes(boxes);

		Admin saved = new Admin();
		saved = this.adminRepository.save(admin);

		return saved;
	}

	public Admin reconstruct(FormObjectMember formObjectMember, BindingResult binding) {

		Admin result = new Admin();

		result.setAddress(formObjectMember.getAddress());
		// result.setBoxes(boxes);
		result.setEmail(formObjectMember.getEmail());
		// result.setEnrolments(enrolments)
		// result.setFinder(finder)
		result.setHasSpam(false);
		result.setMiddleName(formObjectMember.getMiddleName());
		result.setName(formObjectMember.getName());
		result.setPhoneNumber(formObjectMember.getPhoneNumber());
		result.setPhoto(formObjectMember.getPhoto());
		// result.setRequests(requests);
		// result.setPolarity(polarity);
		// result.setSocialProfiles(socialProfiles);
		result.setSurname(formObjectMember.getSurname());

		// USER ACCOUNT
		UserAccount userAccount = new UserAccount();

		// Authorities
		List<Authority> authorities = new ArrayList<Authority>();
		Authority authority = new Authority();
		authority.setAuthority(Authority.ADMIN);
		authorities.add(authority);
		userAccount.setAuthorities(authorities);

		// locked
		userAccount.setIsNotLocked(true);

		// Username
		userAccount.setUsername(formObjectMember.getUsername());

		// Password
		Md5PasswordEncoder encoder;
		encoder = new Md5PasswordEncoder();
		userAccount.setPassword(encoder.encodePassword(formObjectMember.getPassword(), null));

		result.setUserAccount(userAccount);

		return result;
	}

	public Admin save(Admin admin) {
		return this.adminRepository.save(admin);
	}

	// SERVICIO 1
	/*
	 * public Map<String, Double[]> computeStatistics() { this.loggedAsAdmin();
	 *
	 * final Map<String, Double[]> result; Double[] calculations1; Double[]
	 * calculations2; Double[] calculations3; Double[] calculations4; Double[]
	 * calculations5; Double[] calculations6;
	 *
	 * calculations1 = this.adminRepository.fixUpTaskPerUser(); calculations2 =
	 * this.adminRepository.applicationPerFixUpTask(); calculations3 =
	 * this.adminRepository.maxPricePerFixUpTask(); calculations4 =
	 * this.adminRepository.priceOferredPerApplication(); calculations5 =
	 * this.adminRepository.numberComplaintsPerFixUpTask(); calculations6 =
	 * this.adminRepository.notesPerReferee();
	 *
	 * result = new HashMap<String, Double[]>(); result.put("fixUpTaskPerUser",
	 * calculations1); result.put("applicationPerFixUpTask", calculations2);
	 * result.put("maxPricePerFixUpTask", calculations3);
	 * result.put("priceOferredPerApplication", calculations4);
	 * result.put("numberComplaintsPerFixUpTask", calculations5);
	 * result.put("notesPerReferee", calculations6);
	 *
	 * return result; }
	 *
	 *
	 *
	 * // SERVICIO 2 public Map<String, Double> computeStatisticsRatios() {
	 * this.loggedAsAdmin();
	 *
	 * Double ratioPendingApplications, ratioAcceptedApplications,
	 * ratioRejectedApplications, ratioPendingElapsedApplications; Double
	 * fixUpTaskWithComplain; final Map<String, Double> result;
	 *
	 * ratioPendingApplications = this.adminRepository.ratioPendingApplications();
	 * ratioAcceptedApplications = this.adminRepository.ratioAcceptedApplications();
	 * ratioRejectedApplications = this.adminRepository.ratioRejectedApplications();
	 * ratioPendingElapsedApplications =
	 * this.adminRepository.ratioPendingElapsedApplications(); fixUpTaskWithComplain
	 * = this.adminRepository.fixUpTaskWithComplain();
	 *
	 * result = new HashMap<String, Double>();
	 * result.put("ratioPendingApplications", ratioPendingApplications);
	 * result.put("ratioAcceptedApplications", ratioAcceptedApplications);
	 * result.put("ratioRejectedApplications", ratioRejectedApplications);
	 * result.put("ratioPendingElapsedApplications",
	 * ratioPendingElapsedApplications); result.put("fixUpTaskWithComplain",
	 * fixUpTaskWithComplain);
	 *
	 * return result; }
	 */

	public void broadcastMessage(Message message) {
		this.loggedAsAdmin();

		UserAccount userAccount;
		userAccount = LoginService.getPrincipal();
		Actor admin = this.actorService.getActorByUsername(userAccount.getUsername());

		List<Actor> actors = new ArrayList<Actor>();
		actors = this.actorService.findAll();

		for (Actor a : actors)
			if (!(a.equals(admin))) {
				message.setReceiver(a);
				this.messageService.sendMessageBroadcasted(message);
			}

	}

	public void banSuspiciousActor(Actor a) {
		this.loggedAsAdmin();

		a.getUserAccount().setIsNotLocked(false);
		this.actorService.save(a);
	}

	public void unBanSuspiciousActor(final Actor a) {
		this.loggedAsAdmin();

		a.getUserAccount().setIsNotLocked(true);
		this.actorService.save(a);
	}

	public List<Admin> findAll() {
		return this.adminRepository.findAll();
	}

	/*
	 * public Admin getAdminByUsername( String a) { return
	 * this.adminRepository.getAdminByUserName(a); }
	 */

	public Admin findOne(int adminId) {
		return this.adminRepository.findOne(adminId);

	}

	public List<Float> showStatistics() {

		this.finderService.updateAllFinders();

		List<Float> statistics = new ArrayList<Float>();

		if (this.memberService.findAll().isEmpty()) {
			statistics.add((float) 0);
			statistics.add((float) 0);
			statistics.add((float) 0);
			statistics.add((float) 0);

		} else {
			statistics.add(this.adminRepository.avgMembersPerBrotherhood());
			statistics.add(this.minMembersBrotherhood());
			statistics.add(this.maxMembersBrotherhood());
			statistics.add(this.adminRepository.stddevMembersPerBrotherhood());
		}

		if (this.requestService.findAll().isEmpty()) {
			statistics.add((float) 0);
			statistics.add((float) 0);
			statistics.add((float) 0);
		} else {
			statistics.add(this.adminRepository.ratioApprovedRequests());
			statistics.add(this.adminRepository.ratioPendingRequests());
			statistics.add(this.adminRepository.ratioRejectedRequests());
		}

		if (this.areaService.findAll().isEmpty()) {
			statistics.add((float) 0);
			statistics.add((float) 0);
			statistics.add((float) 0);
			statistics.add((float) 0);

		} else {
			statistics.add(this.minBrotherhoodsArea());
			statistics.add(this.maxBrotherhoodsArea());
			statistics.add(this.adminRepository.avgNumberBrotherhoodPerArea());
			statistics.add(this.adminRepository.stddevNumberBrotherhoodPerArea());
		}

		statistics.add(this.adminRepository.minResultFinders());
		statistics.add(this.adminRepository.maxResultFinders());
		statistics.add(this.adminRepository.avgResultFinders());
		statistics.add(this.adminRepository.stddevResultFinders());

		if (this.historyService.findAll().isEmpty()) {
			statistics.add((float) 0);
			statistics.add((float) 0);
			statistics.add((float) 0);
			statistics.add((float) 0);

		} else {
			statistics.add(this.adminRepository.minNumberRecordsPerHistory());
			statistics.add(this.adminRepository.maxNumberRecordsPerHistory());
			statistics.add(this.adminRepository.avgRecordsPerHistory());
			statistics.add(this.adminRepository.stddevRecordsPerHistory());
		}

		if (this.adminRepository.ratioAreasNotCoordinated() == null)
			statistics.add((float) -1);
		else
			statistics.add(this.adminRepository.ratioAreasNotCoordinated());

		if (this.paradeService.findAll().isEmpty()) {
			statistics.add((float) 0);
			statistics.add((float) 0);
			statistics.add((float) 0);
			statistics.add((float) 0);

		} else {
			statistics.add(this.adminRepository.minParadesCoordinated());
			statistics.add(this.adminRepository.maxParadesCoordinated());
			statistics.add(this.adminRepository.avgParadesCoordinated());
			statistics.add(this.adminRepository.stddevParadesCoordinated());
		}
		if (this.adminRepository.paradesDraftVSFinal() == null)
			statistics.add((float) -1);
		else
			statistics.add(this.adminRepository.paradesDraftVSFinal());
		if (this.adminRepository.ratioParadesAcceptedRequests() == null)
			statistics.add((float) -1);
		else
			statistics.add(this.adminRepository.ratioParadesAcceptedRequests());
		if (this.adminRepository.ratioParadesRejectedRequests() == null)
			statistics.add((float) -1);
		else
			statistics.add(this.adminRepository.ratioParadesRejectedRequests());
		if (this.adminRepository.ratioParadesSubmittedRequests() == null)
			statistics.add((float) -1);
		else
			statistics.add(this.adminRepository.ratioParadesSubmittedRequests());
		if (this.adminRepository.ratioActiveSponsorships() == null)
			statistics.add((float) -1);
		else
			statistics.add(this.adminRepository.ratioActiveSponsorships());

		if (this.sponsorshipService.findAll().isEmpty()) {
			statistics.add((float) 0);
			statistics.add((float) 0);
			statistics.add((float) 0);
			statistics.add((float) 0);

		} else {
			statistics.add(this.adminRepository.minSponsorshipsPerSponsor());
			statistics.add(this.adminRepository.maxSponsorshipsPerSponsor());
			statistics.add(this.adminRepository.avgSponsorshipsPerSponsor());
			statistics.add(this.adminRepository.stddevSponsorshipsPerSponsor());
		}

		if (this.adminRepository.numberNonEmptyFinders() == null)
			statistics.add((float) 0);
		else
			statistics.add(this.adminRepository.ratioEmptyFinder());

		statistics.add(this.adminRepository.getRatioSpammers());
		statistics.add(this.adminRepository.getRatioNonSpammers());
		statistics.add(this.adminRepository.avgAdminPolarity() + 1);
		if (this.adminRepository.avgMemberPolarity() == null)
			statistics.add((float) 0);
		else
			statistics.add(this.adminRepository.avgMemberPolarity() + 1);

		if (this.adminRepository.avgBrotherhoodPolarity() == null)
			statistics.add((float) 0);
		else
			statistics.add(this.adminRepository.avgBrotherhoodPolarity() + 1);

		return statistics;
	}

	public List<String> top5SponsorNumberActiveSponsorships() {
		List<String> res = new ArrayList<String>();
		if (this.adminRepository.top5SponsorNumberActiveSponsorships().size() > 5)
			for (int i = 0; i < 5; i++)
				res.add(this.adminRepository.top5SponsorNumberActiveSponsorships().get(i).getUserAccount()
						.getUsername());
		else
			for (Sponsor s : this.adminRepository.top5SponsorNumberActiveSponsorships())
				res.add(s.getUserAccount().getUsername());
		return res;
	}

	public List<Chapter> chaptersThatCoordinateAtLeast() {
		if (this.adminRepository.chaptersThatCoordinateAtLeast().isEmpty())
			return new ArrayList<Chapter>();
		else
			return this.adminRepository.chaptersThatCoordinateAtLeast();
	}

	public List<String> broLargestHistory() {
		List<String> res = new ArrayList<String>();
		for (Brotherhood b : this.adminRepository.broLargestHistory())
			res.add(b.getName());
		return res;
	}

	public List<Brotherhood> broHistoryLargerThanAvg() {
		if (this.adminRepository.broHistoryLargerThanAvg().isEmpty())
			return new ArrayList<Brotherhood>();
		else
			return this.adminRepository.broHistoryLargerThanAvg();
	}

	public Float maxMembersBrotherhood() {
		return this.adminRepository.maxNumberMembersPerBrotherhood();
	}

	public Float minMembersBrotherhood() {
		return this.adminRepository.minNumberMembersPerBrotherhood();
	}

	public Float maxBrotherhoodsArea() {
		return this.adminRepository.maxNumberBrotherhoodPerArea();
	}

	public Float minBrotherhoodsArea() {
		return this.adminRepository.minNumberBrotherhoodPerArea();
	}

	public Map<String, Float> countBrotherhoodsArea() {
		return this.nameStatisticsArea(this.adminRepository.numberBrotherhoodsPerArea());
	}

	public List<String> largestBrotherhoods() {
		if (this.adminRepository.largestBrotherhoods().isEmpty())
			return new ArrayList<String>();
		else
			return this.adminRepository.largestBrotherhoods();

	}

	public List<String> smallestBrotherhoods() {
		if (this.adminRepository.smallestBrotherhoods().isEmpty())
			return new ArrayList<String>();
		else
			return this.adminRepository.smallestBrotherhoods();

	}

	public Map<String, Float> ratioBrotherhoodPerArea() {
		if (this.areaService.findAll().isEmpty())
			return new HashMap<String, Float>();
		else
			return this.nameStatisticsArea(this.adminRepository.ratioBrotherhoodPerArea());
	}

	public Map<String, Float> ratioRequestApprovedByParade() {
		return this.nameStatisticsParade(this.noZeroDivision(this.adminRepository.ratioApprovedRequestsByParades()));
	}

	public Map<String, Float> ratioRequestPendingByParade() {
		return this.nameStatisticsParade(this.noZeroDivision(this.adminRepository.ratioPendingRequestsByParades()));
	}

	public Map<String, Float> ratioRequestRejectedByParade() {
		return this.nameStatisticsParade(this.noZeroDivision(this.adminRepository.ratioRejectedRequestsByParades()));
	}

	public List<Float> prueba() {
		return this.adminRepository.ratioRejectedRequestsByParades();
	}

	public List<String> paradesOfNextMonth() {
		return this.adminRepository.listParadeNext30Days();
	}

	public List<Member> membersAtLeastTenPercentRequestsApproved() {
		return this.adminRepository.listMembersAtLeastTenPercentRequestApproved();
	}

	public Float getRatioSpammers() {
		return this.adminRepository.getRatioSpammers();
	}

	public Float getRatioNonSpammers() {
		return this.adminRepository.getRatioNonSpammers();
	}

	public Float avgAdminPolarity() {
		return this.adminRepository.avgAdminPolarity();
	}

	public Float avgMemberPolarity() {
		return this.adminRepository.avgMemberPolarity();
	}

	public Float avgBrotherhoodPolarity() {
		return this.adminRepository.avgBrotherhoodPolarity();
	}

	public List<Float> noZeroDivision(List<Float> result) {
		List<Parade> pro = this.paradeService.findAll();
		Assert.isTrue(result.size() == pro.size());
		for (Parade p : pro)
			try {
				Assert.notNull(result.get(pro.indexOf(p)));
			} catch (Exception e) {
				result.set(pro.indexOf(p), (float) 0);
			}
		return result;

	}

	public Map<Position, Float> mapNumberPositions() {
		return this.numberPositions(this.adminRepository.numberPositions());
	}

	public Map<String, Float> nameStatisticsArea(List<Float> statistics) {
		Map<String, Float> result = new HashMap<String, Float>();
		List<Area> area = this.areaService.findAll();
		Assert.isTrue(statistics.size() == area.size());

		for (Area a : area)
			result.put(a.getName(), statistics.get(area.indexOf(a)));

		return result;
	}

	public Map<Position, Float> numberPositions(List<Float> statistics) {
		Map<Position, Float> result = new HashMap<Position, Float>();
		List<Position> positions = this.positionService.findAll();
		Assert.isTrue(statistics.size() == positions.size());

		for (Position p : positions)
			result.put(p, statistics.get(positions.indexOf(p)));

		return result;
	}

	public Map<String, Float> nameStatisticsParade(List<Float> statistics) {
		Map<String, Float> result = new HashMap<String, Float>();
		List<Parade> parades = this.paradeService.findAll();
		Assert.isTrue(statistics.size() == parades.size());

		for (Parade p : parades)
			result.put(p.getTitle(), statistics.get(parades.indexOf(p)));

		return result;
	}

	public Admin getSystem() {
		return this.adminRepository.getSystem();
	}
	/*
	 * public List<Admin> findAll2() { return this.adminRepository.findAll2(); }
	 */

	public Message reconstruct(Message message, BindingResult binding) {

		UserAccount userAccount;
		userAccount = LoginService.getPrincipal();
		Actor actor = this.actorService.getActorByUsername(userAccount.getUsername());

		domain.Message result;
		result = this.messageService.create();
		if (message.getId() == 0) {
			result = message;
			result.setSender(actor);
			Date thisMoment = new Date();
			thisMoment.setTime(thisMoment.getTime() - 1000);
			result.setMoment(thisMoment);
			result.setReceiver(actor);

		} else {
			result = this.messageService.findOne(message.getId());

			result.setBody(message.getBody());
			result.setPriority(message.getPriority());
			result.setTags(message.getTags());
			result.setSubject(message.getSubject());
			result.setReceiver(actor);
			result.setSender(actor);

		}

		this.validator.validate(result, binding);

		return result;

	}

	public Admin loggedAdmin() {
		Admin admin = new Admin();
		UserAccount userAccount;
		userAccount = LoginService.getPrincipal();
		admin = this.adminRepository.getAdminByUsername(userAccount.getUsername());
		return admin;
	}

	public Admin reconstruct(Admin admin, BindingResult binding) {

		Admin result;
		Admin pururu;

		result = admin;
		pururu = this.adminRepository.findOne(admin.getId());

		result.setUserAccount(pururu.getUserAccount());
		result.setBoxes(pururu.getBoxes());
		result.setHasSpam(pururu.getHasSpam());
		result.setSocialProfiles(pururu.getSocialProfiles());
		result.setPolarity(pururu.getPolarity());

		/*
		 * result.setName(admin.getName()); result.setMiddleName(admin.getMiddleName());
		 * result.setSurname(admin.getSurname()); result.setPhoto(admin.getPhoto());
		 * result.setEmail(admin.getEmail());
		 * result.setPhoneNumber(admin.getPhoneNumber());
		 * result.setAddress(admin.getAddress());
		 */

		this.validator.validate(result, binding);
		/*
		 * try {
		 *
		 * } catch (Throwable oops) { System.out.println("LOL EKISDE"); }
		 *
		 * System.out.println(binding); System.out.println("LOL");
		 */

		return result;
	}

	public String SocialProfilesToString() {
		String res = "";
		Admin admin = this.loggedAdmin();
		List<SocialProfile> socialProfiles = new ArrayList<SocialProfile>();
		StringBuilder sb = new StringBuilder();
		socialProfiles = admin.getSocialProfiles();

		Integer cont = 1;

		for (SocialProfile f : socialProfiles) {
			sb.append("Profile" + cont + " Name: " + f.getName() + " Nick: " + f.getNick() + " Profile link: "
					+ f.getProfileLink()).append(System.getProperty("line.separator"));
			cont++;
		}
		return sb.toString();
	}

	public void flush() {
		this.adminRepository.flush();

	}

	public Admin getAdminByUsername(String username) {
		return this.adminRepository.getAdminByUsername(username);
	}

	public Admin updateAdmin(Admin admin) {
		this.loggedAsAdmin();
		Assert.isTrue(admin.getId() != 0 && this.loggedAdmin().getId() == admin.getId());
		return this.adminRepository.save(admin);
	}

	public Float maxParadesCoordinated() {
		return this.adminRepository.maxParadesCoordinated();
	}
}
