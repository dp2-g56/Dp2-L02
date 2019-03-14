package services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import domain.Segment;
import repositories.SegmentRepository;

public class SegmentService {

	// Managed repository ------------------------------------------

	@Autowired
	private SegmentRepository segmnentRepository;

	// Simple CRUD methods ------------------------------------------

	public Segment createSegment() {

		Segment segment = new Segment();

		segment.setDestinationLatitude(0.);
		segment.setDestinationLongitude(0.);

		segment.setOriginLatitude(0.);
		segment.setOriginLongitude(0.);

		segment.setTime(0);

		return segment;

	}

	public Segment findOne(Integer id) {
		return this.segmnentRepository.findOne(id);
	}

	public List<Segment> findAll() {
		return this.segmnentRepository.findAll();
	}

	public Segment save(Segment segment) {
		return this.segmnentRepository.save(segment);
	}

	public void delete(Segment segment) {
		this.segmnentRepository.delete(segment);
	}

}
