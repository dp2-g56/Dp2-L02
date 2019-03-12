package services;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import domain.History;
import repositories.HistoryRepository;

@Service
@Transactional
public class HistoryService {

	@Autowired
	private HistoryRepository historyRepository;

	public List<History> findAll() {
		return this.historyRepository.findAll();
	}

}
