
package repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import domain.Chapter;

@Repository
public interface ChapterRepository extends JpaRepository<Chapter, Integer> {

}
