
package repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import domain.Message;

@Repository
public interface MessageRepository extends JpaRepository<Message, Integer> {

	@Query("select m from Message m")
	public List<Message> findAll2();

	@Query("select (select a from Message a where a in p) from Actor b join b.boxes c join c.messages p where b.id = ?1")
	public List<Message> getAllMessagesFromActor(int idActor);

	@Query("select a from Message a join a.sender b where b.id = ?1")
	public List<Message> getSendedMessagesByActor(int idActor);
}
