package services;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Validator;

import domain.Brotherhood;
import domain.Parade;
import domain.Path;
import domain.Segment;
import repositories.SegmentRepository;

@Service
@Transactional
public class SegmentService {

	// Managed repository ------------------------------------------

	@Autowired
	private SegmentRepository segmnentRepository;
	@Autowired
	private ParadeService paradeService;
	@Autowired
	private BrotherhoodService brotherhoodService;
	@Autowired
	private PathService pathService;
	@Autowired
	private Validator validator;

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

	public List<Segment> getSegmentByParade(Integer paradeId) {
		Parade parade = this.paradeService.findOne(paradeId);
		return parade.getPath().getSegments();
	}

	public void segmentSecurity(Integer paradeId) {
		Parade parade = this.paradeService.findOne(paradeId);
		Brotherhood brotherhood = this.brotherhoodService.loggedBrotherhood();
		Assert.isTrue(brotherhood.getParades().contains(parade));
	}

	public Segment recontruct(Segment segmentForm, Integer paradeId, BindingResult binding) {

		Segment result = new Segment();

		Parade parade = this.paradeService.findOne(paradeId);
		List<Segment> segments = parade.getPath().getSegments();

		if (!this.isOrigin(segmentForm, paradeId)) {
			Segment segmentBefore = segments.get(segments.size() - 1);
			result.setOriginLatitude(segmentBefore.getDestinationLatitude());
			result.setOriginLongitude(segmentBefore.getDestinationLongitude());
		} else {
			result.setOriginLatitude(segmentForm.getOriginLatitude());
			result.setOriginLongitude(segmentForm.getOriginLongitude());
		}

		result.setId(segmentForm.getId());
		result.setDestinationLatitude(segmentForm.getDestinationLatitude());
		result.setDestinationLongitude(segmentForm.getDestinationLongitude());
		result.setTime(segmentForm.getTime());

		this.validator.validate(result, binding);
		return result;

	}

	public Segment editSegment(Segment segment, Integer paradeId) {
		this.segmentSecurity(paradeId);
		Parade parade = this.paradeService.findOne(paradeId);
		Path path = parade.getPath();
		List<Segment> segments = path.getSegments();
		Boolean isNew = segment.getId() == 0;

		segment = this.save(segment);

		if (isNew) {
			segments.add(segment);
			path.setSegments(segments);
			parade.setPath(path);
			this.paradeService.save(parade);
		}

		return segment;
	}

	public boolean isOrigin(Segment segment, Integer paradeId) {
		Path path = this.paradeService.findOne(paradeId).getPath();
		List<Segment> segments = path.getSegments();

		return segments.indexOf(segment) == 0 || segments.size() == 0;
	}

	public boolean isLast(Segment segment, Path path) {
		List<Segment> segments = path.getSegments();
		return segments.indexOf(segment) == segments.size() - 1;
	}

	public void deleteSegment(Segment segment, Integer pathId) {
		Path path = this.pathService.findOne(pathId);
		List<Segment> segments = path.getSegments();

		if (!this.isOrigin(segment, pathId) && !this.isLast(segment, path)) {
			Segment segmentBefore = segments.get(segments.indexOf(segment) - 1);
			Segment segmentAfter = segments.get(segments.indexOf(segment) + 1);
			segmentAfter.setOriginLatitude(segmentBefore.getDestinationLatitude());
			segmentAfter.setOriginLongitude(segmentBefore.getDestinationLongitude());
			this.save(segmentAfter);
		}

		this.delete(segment);
	}

}
