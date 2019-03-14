package services;

import java.util.ArrayList;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import domain.Path;
import domain.Segment;
import repositories.PathRepository;

@Service
@Transactional
public class PathService {

	// Managed repository ------------------------------------------

	@Autowired
	private PathRepository pathRepository;

	// Simple CRUD methods ------------------------------------------

	public Path create() {
		Path path = new Path();

		List<Segment> segments = new ArrayList<>();

		path.setSegments(segments);

		return path;
	}

	public List<Path> findAll() {
		return this.pathRepository.findAll();
	}

	public Path findOne(Integer id) {
		return this.pathRepository.findOne(id);
	}

	public Path save(Path path) {
		return this.save(path);
	}

	public void delete(Path path) {
		this.pathRepository.delete(path);
	}
}
