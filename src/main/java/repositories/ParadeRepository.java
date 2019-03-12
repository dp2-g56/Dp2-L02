
package repositories;

import java.util.Collection;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import domain.Parade;

@Repository
public interface ParadeRepository extends JpaRepository<Parade, Integer> {

	@Query("select p from Parade p where paradeStatus = 2")
	public Collection<Parade> getAcceptedParades();

	@Query("select p from Parade p where isDraftMode = true")
	public Collection<Parade> getDraftParades();

}
