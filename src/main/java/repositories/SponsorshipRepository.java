
package repositories;

import java.util.Collection;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import domain.Sponsorship;

@Repository
public interface SponsorshipRepository extends JpaRepository<Sponsorship, Integer> {

	@Query("select s from Sponsor spo join spo.sponsorships s where spo.id = ?1")
	Collection<Sponsorship> getAllSponsorshipsOfSponsor(int sponsorId);

	@Query("select s from Sponsor spo join spo.sponsorships s where spo.id = ?1 and s.isActivated = true")
	Collection<Sponsorship> getActivatedSponsorshipsOfSponsor(int sponsorId);

	@Query("select s from Sponsor spo join spo.sponsorships s where spo.id = ?1 and s.isActivated = false")
	Collection<Sponsorship> getDeactivatedSponsorshipsOfSponsor(int sponsorId);
}
