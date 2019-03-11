package domain;

import java.util.List;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.validation.Valid;

@Entity
@Access(AccessType.PROPERTY)
public class History extends DomainEntity {

	private InceptionRecord inceptionRecord;
	private List<PeriodRecord> periodRecords;
	private List<LegalRecord> legalRecords;
	private List<LinkRecord> linkRecords;
	private List<MiscellaneousRecord> miscellaneousRecords;

	public History() {
		super();
	}

	@OneToOne(optional = true, cascade = CascadeType.ALL)
	@Valid
	public InceptionRecord getInceptionRecord() {
		return this.inceptionRecord;
	}

	public void setInceptionRecord(InceptionRecord inceptionRecord) {
		this.inceptionRecord = inceptionRecord;
	}

	@OneToMany(cascade = CascadeType.ALL)
	@Valid
	public List<PeriodRecord> getPeriodRecords() {
		return this.periodRecords;
	}

	public void setPeriodRecords(List<PeriodRecord> periodRecords) {
		this.periodRecords = periodRecords;
	}

	@OneToMany(cascade = CascadeType.ALL)
	@Valid
	public List<LegalRecord> getLegalRecords() {
		return this.legalRecords;
	}

	public void setLegalRecords(List<LegalRecord> legalRecords) {
		this.legalRecords = legalRecords;
	}

	@OneToMany(cascade = CascadeType.ALL)
	@Valid
	public List<LinkRecord> getLinkRecords() {
		return this.linkRecords;
	}

	public void setLinkRecords(List<LinkRecord> linkRecords) {
		this.linkRecords = linkRecords;
	}

	@OneToMany(cascade = CascadeType.ALL)
	@Valid
	public List<MiscellaneousRecord> getMiscellaneousRecords() {
		return this.miscellaneousRecords;
	}

	public void setMiscellaneousRecords(List<MiscellaneousRecord> miscellaneousRecords) {
		this.miscellaneousRecords = miscellaneousRecords;
	}

}
