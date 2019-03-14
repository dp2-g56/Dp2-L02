
package repositories;

import java.util.Collection;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import domain.Area;
import domain.Parade;

@Repository
public interface ParadeRepository extends JpaRepository<Parade, Integer> {

	@Query("select p from Parade p where paradeStatus = 1")
	public Collection<Parade> getAcceptedParades();

	@Query("select p from Parade p where isDraftMode = true")
	public Collection<Parade> getDraftParades();

	@Query("select p from Brotherhood b join b.parades p where p.isDraftMode = false and b.area = ?1")
	public List<Parade> getParadesByArea(Area area);

}
