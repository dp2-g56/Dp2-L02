
package repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import domain.Admin;
import domain.Brotherhood;
import domain.Member;

@Repository
public interface AdminRepository extends JpaRepository<Admin, Integer> {

	/*
	 * @Query("select avg(c.fixUpTasks.size), min(c.fixUpTasks.size), max(c.fixUpTasks.size), (sqrt(sum(c.fixUpTasks.size * c.fixUpTasks.size)/count(c.fixUpTasks.size) - (avg(c.fixUpTasks.size)* avg(c.fixUpTasks.size)))) from Endorser c"
	 * ) public Double[] fixUpTaskPerUser();
	 *
	 * @Query("select avg(a.applications.size), min(a.applications.size), max(a.applications.size), (sqrt(sum(a.applications.size * a.applications.size) / count(a.applications.size) - (avg(a.applications.size)*avg(a.applications.size)))) from FixUpTask a"
	 * ) public Double[] applicationPerFixUpTask();
	 *
	 * @Query("select avg(f.maxPrice), min(f.maxPrice), max(f.maxPrice), (sqrt(sum(f.maxPrice * f.maxPrice) / count(f.maxPrice) - (avg(f.maxPrice) * avg(f.maxPrice)))) from FixUpTask f"
	 * ) public Double[] maxPricePerFixUpTask();
	 *
	 * @Query("select avg(a.offeredPrice), min(a.offeredPrice), max(a.offeredPrice), (sqrt(sum(a.offeredPrice*a.offeredPrice)/count(a.offeredPrice)-(avg(a.offeredPrice) * avg(a.offeredPrice)))) from Application a"
	 * ) public Double[] priceOferredPerApplication();
	 *
	 * @Query("select distinct (cast((select count(a1.status) from Application a1 where status='PENDING') as float)/ (select count(a2.status) from Application a2) * 100) from Application a3"
	 * ) public Double ratioPendingApplications();
	 *
	 * @Query("select distinct (cast((select count(a1.status) from Application a1 where status='ACCEPTED') as float)/ (select count(a2.status) from Application a2) * 100) from Application a3"
	 * ) public Double ratioAcceptedApplications();
	 *
	 * @Query("select distinct (cast((select count(a1.status) from Application a1 where status='REJECTED') as float)/ (select count(a2.status) from Application a2) * 100) from Application a3"
	 * ) public Double ratioRejectedApplications();
	 *
	 * @Query("select distinct(cast((select count(a1.status) from Application a1 join a1.fixUpTask f where a1.status='PENDING' and f.realizationTime < (NOW())) as float)/ (select count(a2.status) from Application a2) * 100) from Application a3"
	 * ) public Double ratioPendingElapsedApplications();
	 *
	 * @Query("select distinct c from Customer c, FixUpTask d where c.fixUpTasks.size >= 1.1 * (select avg(c.fixUpTasks.size) from Customer c) order by d.applications.size"
	 * ) public List<Customer> customers10PercentMoreApplications();
	 *
	 * @Query("select c from HandyWorker c where c.applications.size >= 1.1 * (select avg(c.applications.size) from HandyWorker c) order by c.applications.size"
	 * ) public List<HandyWorker> handyWorkers10PercentMoreApplications();
	 *
	 * @Query("select avg(c.complaints.size), min(c.complaints.size), max(c.complaints.size), (sqrt(sum(c.complaints.size * c.complaints.size)/count(c.complaints.size) - (avg(c.complaints.size)* avg(c.complaints.size)))) from FixUpTask c"
	 * ) public Double[] numberComplaintsPerFixUpTask();
	 *
	 * @Query("select avg(c.notes.size), min(c.notes.size), max(c.notes.size), (sqrt(sum(c.notes.size * c.notes.size)/count(c.notes.size) - (avg(c.notes.size)* avg(c.notes.size)))) from Report c"
	 * ) public Double[] notesPerReferee();
	 *
	 * @Query("select distinct(cast((select count(a1) from FixUpTask a1 where a1.complaints is not empty) as float)/ (select count(a2) from FixUpTask a2) * 100) from Application a3"
	 * ) public Double fixUpTaskWithComplain();
	 *
	 * @Query("select distinct a from Customer a join a.fixUpTasks f where (f.complaints.size) > 0 order by f.complaints.size"
	 * ) public List<Customer> customerTermsofComplainsOrdered();
	 *
	 * @Query("select distinct a.handyWorker from Application a join a.fixUpTask f where (f.complaints.size) > 0 order by f.complaints.size"
	 * ) public List<HandyWorker> HandyWorkerTermsofComplainsOrdered();
	 *
	 * @Query("select a from Admin a join a.userAccount b where b.username = ?1")
	 * public Admin getAdminByUserName(String a);
	 *
	 * @Query("select a from Admin a") public List<Admin> findAll2();
	 */

	@Query("select cast((select count(b) from Enrolment b where b.brotherhood = a AND b.statusEnrolment = 'ACCEPTED') as float) from Brotherhood a")
	public List<Float> listNumberMembersPerBrotherhood();

	@Query("select max(cast((select count(b) from Enrolment b where b.brotherhood = a AND b.statusEnrolment = 'ACCEPTED') as float)) from Brotherhood a")
	public Float maxNumberMembersPerBrotherhood();

	@Query("select min(cast((select count(b) from Enrolment b where b.brotherhood = a AND b.statusEnrolment = 'ACCEPTED') as float)) from Brotherhood a")
	public Float minNumberMembersPerBrotherhood();

	@Query("select avg(cast((select count(b) from Enrolment b where b.brotherhood = a AND b.statusEnrolment = 'ACCEPTED') as float)) from Brotherhood a")
	public Float avgMembersPerBrotherhood();

	@Query("select stddev(cast((select count(b) from Enrolment b where b.brotherhood = a AND b.statusEnrolment = 'ACCEPTED') as float)) from Brotherhood a")
	public Float stddevMembersPerBrotherhood();

	@Query("select distinct (cast((select count(a1.status) from Request a1 where status='APPROVED') as float)/ (select count(a2) from Request a2) * 100) from Configuration a")
	public Float ratioApprovedRequests();

	@Query("select (cast((select count(a1.status) from Request a1 where status='APPROVED' and a1.parade = a) as float)/ (a.requests.size) * 100) from Parade a")
	public List<Float> ratioApprovedRequestsByParades();

	@Query("select distinct (cast((select count(a1.status) from Request a1 where status='PENDING') as float)/ (select count(a2) from Request a2) * 100) from Configuration a")
	public Float ratioPendingRequests();

	@Query("select (cast((select count(a1.status) from Request a1 where status='PENDING' and a1.parade = a) as float)/ (a.requests.size) * 100) from Parade a")
	public List<Float> ratioPendingRequestsByParades();

	@Query("select distinct (cast((select count(a1.status) from Request a1 where status='REJECTED') as float)/ (select count(a2) from Request a2) * 100) from Configuration a")
	public Float ratioRejectedRequests();

	@Query("select (cast((select count(a1.status) from Request a1 where status='REJECTED' and a1.parade = a) as float)/ (a.requests.size) * 100) from Parade a")
	public List<Float> ratioRejectedRequestsByParades();

	@Query("select a.title from Brotherhood a where cast((select count(b) from Enrolment b where b.brotherhood = a AND b.statusEnrolment = 'ACCEPTED') as float) = (select max(cast((select count(b) from Enrolment b where b.brotherhood = a AND b.statusEnrolment = 'ACCEPTED') as float)) from Brotherhood a)")
	public List<String> largestBrotherhoods();

	@Query("select a.title from Brotherhood a where cast((select count(b) from Enrolment b where b.brotherhood = a AND b.statusEnrolment = 'ACCEPTED') as float) = (select min(cast((select count(b) from Enrolment b where b.brotherhood = a AND b.statusEnrolment = 'ACCEPTED') as float)) from Brotherhood a)")
	public List<String> smallestBrotherhoods();

	@Query("select  a from Member a where cast((select count(b) from Request b where b.status = 'APPROVED' and b.member = a) as float) >= (cast((select count(b) from Request b where b.member = a) as float) * 0.1) and (select count(b) from Request b where b.member = a) > 0")
	public List<Member> listMembersAtLeastTenPercentRequestApproved();

	@Query("select (cast((select count(a) from Brotherhood a where a.area = b) as float)/(select count(c) from Area c)) from Area b")
	public List<Float> ratioBrotherhoodPerArea();

	@Query("select cast((select count(a) from Brotherhood a where a.area = b) as float) from Area b")
	public List<Float> numberBrotherhoodsPerArea();

	@Query("select avg(cast((select count(a) from Brotherhood a where a.area = b) as float)) from Area b")
	public Float avgNumberBrotherhoodPerArea();

	@Query("select max(cast((select count(a) from Brotherhood a where a.area = b) as float)) from Area b")
	public Float maxNumberBrotherhoodPerArea();

	@Query("select min(cast((select count(a) from Brotherhood a where a.area = b) as float)) from Area b")
	public Float minNumberBrotherhoodPerArea();

	@Query("select stddev(cast((select count(a) from Brotherhood a where a.area = b) as float)) from Area b")
	public Float stddevNumberBrotherhoodPerArea();

	@Query("select a.title from Parade a where a.isDraftMode = false and a.moment between (NOW()) and (select (NOW() + 30000000) from Configuration c)")
	public List<String> listParadeNext30Days();

	@Query("select min(a.parades.size) from Finder a")
	public Float minResultFinders();

	@Query("select max(a.parades.size) from Finder a")
	public Float maxResultFinders();

	@Query("select avg(a.parades.size) from Finder a")
	public Float avgResultFinders();

	@Query("select stddev(a.parades.size) from Finder a")
	public Float stddevResultFinders();

	@Query("select (cast((select count(a) from Finder a where a.parades.size = 0) as float)/(select count(c) from Finder c where c.parades.size > 0)) from Configuration b")
	public Float ratioEmptyFinder();

	@Query("select count(c) from Finder c where c.parades.size > 0")
	public Integer numberNonEmptyFinders();

	@Query("select cast((select count(b) from Enrolment b where b.position = a and b.statusEnrolment = 'ACCEPTED') as float) from Position a")
	public List<Float> numberPositions();

	@Query("select a from Admin a join a.userAccount u where u.username = 'system'")
	public Admin getSystem();

	// NEW QUERIES FOR A+
	@Query("select distinct (cast((select count(a1.hasSpam) from Actor a1 where hasSpam=true) as float)/ (select count(a2.hasSpam) from Actor a2) * 100) from Configuration a")
	public Float getRatioSpammers();

	@Query("select distinct (cast((select count(a1.hasSpam) from Actor a1 where hasSpam=false) as float)/ (select count(a2.hasSpam) from Actor a2) * 100) from Configuration a")
	public Float getRatioNonSpammers();

	@Query("select avg(a.polarity) from Admin a")
	public Float avgAdminPolarity();

	@Query("select avg(a.polarity) from Member a")
	public Float avgMemberPolarity();

	@Query("select avg(a.polarity) from Brotherhood a")
	public Float avgBrotherhoodPolarity();

	@Query("select m from Admin m join m.userAccount u where u.username = ?1")
	public Admin getAdminByUsername(String username);

	@Query("select max(1+h.legalRecords.size+h.linkRecords.size+h.periodRecords.size+h.miscellaneousRecords.size) from Brotherhood b join b.history h")
	public Float maxNumberRecordsPerHistory();

	@Query("select min(1+h.legalRecords.size+h.linkRecords.size+h.periodRecords.size+h.miscellaneousRecords.size) from Brotherhood b join b.history h")
	public Float minNumberRecordsPerHistory();

	@Query("select avg(1+h.legalRecords.size+h.linkRecords.size+h.periodRecords.size+h.miscellaneousRecords.size) from Brotherhood b join b.history h")
	public Float avgRecordsPerHistory();

	@Query("select stddev(1+h.legalRecords.size+h.linkRecords.size+h.periodRecords.size+h.miscellaneousRecords.size) from Brotherhood b join b.history h")
	public Float stddevRecordsPerHistory();

	// @Query("select b,
	// max(1+h.legalRecords.size+h.linkRecords.size+h.periodRecords.size+h.miscellaneousRecords.size)
	// from Brotherhood b join b.history h")
	@Query("select b from Brotherhood b join b.history h where (1+h.legalRecords.size+h.linkRecords.size+h.periodRecords.size+h.miscellaneousRecords.size) = (select max(1+i.legalRecords.size+i.linkRecords.size+i.periodRecords.size+i.miscellaneousRecords.size) from Brotherhood a join a.history i)")
	public Brotherhood broLargestHistory();

	@Query("select b from Brotherhood b join b.history h where (1+h.legalRecords.size+h.linkRecords.size+h.periodRecords.size+h.miscellaneousRecords.size) > (select avg(1+i.legalRecords.size+i.linkRecords.size+i.periodRecords.size+i.miscellaneousRecords.size) from Brotherhood a join a.history i)")
	public List<Brotherhood> broHistoryLargerThanAvg();
}
