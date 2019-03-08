
package repositories;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import domain.Finder;
import domain.Procession;

@Repository
public interface FinderRepository extends JpaRepository<Finder, Integer> {

	@Query("select p from Procession p where p.isDraftMode=FALSE")
	public List<Procession> getPublushedProcessions();

	@Query("select p from Procession p where p.title like ?1 or p.description like ?1")
	public List<Procession> getProcessionsByKeyWord(String keyWord);

	@Query("select p from Brotherhood b join b.area a join b.processions p where a.name like ?1")
	public List<Procession> getProcessionsByArea(String area);

	@Query("select p from Procession p where p.moment > ?1")
	public List<Procession> getProcessionsByMinDate(Date date);

	@Query("select p from Procession p where p.moment < ?1")
	public List<Procession> getProcessionsByMaxDate(Date date);
}
