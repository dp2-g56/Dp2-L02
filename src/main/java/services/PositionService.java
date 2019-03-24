
package services;

import java.util.Collection;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import repositories.PositionRepository;
import domain.Position;

@Service
@Transactional
public class PositionService {

	// Managed repository ------------------------------------------

	@Autowired
	private PositionRepository	positionRepository;
	@Autowired
	private AdminService		adminService;


	// Simple CRUD methods ------------------------------------------

	public Position createPosition() {
		this.adminService.loggedAsAdmin();
		Position position = new Position();

		position.setTitleEnglish("");
		position.setTitleSpanish("");

		return position;
	}

	public List<Position> findAll() {
		return this.positionRepository.findAll();
	}

	public Position findOne(int id) {
		return this.positionRepository.findOne(id);
	}

	public Position save(Position position) {
		return this.positionRepository.save(position);
	}
	public void delete(Position position) {
		this.positionRepository.delete(position);
	}

	public Collection<Position> getPositions() {
		return this.positionRepository.getPositions();
	}

	public Collection<Position> getUsedPositions() {
		return this.positionRepository.getUsedPositions();
	}

	public Boolean canBeDeleted(Position position) {
		Boolean res = true;

		if (position.getId() == 0)
			res = false;
		else {
			Position positionFounded = this.findOne(position.getId());
			Collection<Position> usedPositions = this.getUsedPositions();
			if (usedPositions.contains(positionFounded))
				res = false;
		}
		return res;
	}

	public Position savePosition(Position position) {
		this.adminService.loggedAsAdmin();
		Position positionSaved = this.save(position);
		return positionSaved;
	}

	public void deletePosition(Position position) {
		this.adminService.loggedAsAdmin();
		Assert.isTrue(this.canBeDeleted(position));
		this.delete(position);
	}

	public void flush() {
		this.positionRepository.flush();
	}

}
