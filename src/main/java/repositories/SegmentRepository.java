package repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import domain.Segment;

public interface SegmentRepository extends JpaRepository<Segment, Integer> {

}
