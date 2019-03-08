package repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import domain.Actor;
import domain.Box;

@Repository
public interface ActorRepository extends JpaRepository<Actor, Integer> {

	// Si el actor no esta registrado, que se pueda registrar como customer,
	// como handy worker o
	// como sponsor.

	// Si el actor esta registrado, podrá editar su personal data
	// Intercambiar mensajes con otros actores y organizarlos
	// Manage las boxes excepto las de sistema(Enviado, recibido, basura, spam)
	// Un sponsor debe: ver el catalogo de tutoriales, Los actores deben de
	// poder ver los profile de los
	// Handy workers, incluyendo su personal data y una lista de tutoriales que
	// hayan escrito

	@Query("select a from Actor a join a.userAccount b where b.username = ?1")
	public Actor getActorByUserName(String a);

	@Query("select c.boxes from Actor c where c = ?1")
	public List<Box> listOfBoxes(Actor actor);

	@Query("select a from Actor a")
	public List<Actor> getActors();

}
