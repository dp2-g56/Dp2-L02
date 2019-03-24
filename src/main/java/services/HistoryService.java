
package services;

import java.util.ArrayList;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import repositories.HistoryRepository;
import domain.Brotherhood;
import domain.History;
import domain.LegalRecord;
import domain.LinkRecord;
import domain.MiscellaneousRecord;
import domain.PeriodRecord;

@Service
@Transactional
public class HistoryService {

	@Autowired
	private HistoryRepository	historyRepository;


	public History save(History history) {
		return this.historyRepository.save(history);
	}

	public History createAndSaveHistory(Brotherhood brotherhood) {
		History history = new History();
		history.setInceptionRecord(null);
		history.setLegalRecords(new ArrayList<LegalRecord>());
		history.setLinkRecords(new ArrayList<LinkRecord>());
		history.setMiscellaneousRecords(new ArrayList<MiscellaneousRecord>());
		history.setPeriodRecords(new ArrayList<PeriodRecord>());
		History saved = this.save(history);
		brotherhood.setHistory(saved);

		return saved;
	}

	public void deleteHistory(History history) {
		this.historyRepository.delete(history);
	}
}
