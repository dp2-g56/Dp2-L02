
package repositories;

import java.util.Collection;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import domain.Sponsor;
import domain.Sponsorship;

@Repository
public interface SponsorshipRepository extends JpaRepository<Sponsorship, Integer> {

	@Query("select s from Sponsor spo join spo.sponsorships s where spo.id = ?1")
	Collection<Sponsorship> getAllSponsorshipsOfSponsor(int sponsorId);

	@Query("select s from Sponsor spo join spo.sponsorships s where spo.id = ?1 and s.isActivated = true")
	Collection<Sponsorship> getActivatedSponsorshipsOfSponsor(int sponsorId);

	@Query("select s from Sponsor spo join spo.sponsorships s where spo.id = ?1 and s.isActivated = false")
	Collection<Sponsorship> getDeactivatedSponsorshipsOfSponsor(int sponsorId);

	@Query("select spo from Sponsor spo join spo.sponsorships s where s.id = ?1")
	Sponsor getSponsorOfSponsorship(int sponsorshipId);

	@Query("select s from Sponsorship s")
	Collection<Sponsorship> getAllSponsorshipsAsAdmin();

	@Query("select s from Sponsorship s where s.isActivated = true")
	Collection<Sponsorship> getActivatedSponsorshipsAsAdmin();

	@Query("select s from Sponsorship s where s.isActivated = false")
	Collection<Sponsorship> getDeactivatedSponsorshipsAsAdmin();

	@Query("select s from Sponsorship s join s.parade p where p.id = ?1")
	List<Sponsorship> getSponsorshipsOfParade(int paradeId);
}
