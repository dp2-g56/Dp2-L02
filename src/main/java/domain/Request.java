
package domain;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Index;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.Valid;
import javax.validation.constraints.Min;

@Entity
@Access(AccessType.PROPERTY)
@Table(indexes = {
	@Index(columnList = "status"), @Index(columnList = "status, procession"), @Index(columnList = "status, member"), @Index(columnList = "member")
})
public class Request extends DomainEntity {

	private Status		status;
	private Integer		rowNumber;
	private Integer		columnNumber;
	private String		reasonDescription;

	private Member		member;
	private Procession	procession;


	@Valid
	@Enumerated(EnumType.STRING)
	public Status getStatus() {
		return this.status;
	}

	public void setStatus(final Status status) {
		this.status = status;
	}

	@Min(1)
	public Integer getRowNumber() {
		return this.rowNumber;
	}

	public void setRowNumber(final Integer rowNumber) {
		this.rowNumber = rowNumber;
	}

	@Min(1)
	public Integer getColumnNumber() {
		return this.columnNumber;
	}

	public void setColumnNumber(final Integer columnNumber) {
		this.columnNumber = columnNumber;
	}

	@Valid
	public String getReasonDescription() {
		return this.reasonDescription;
	}

	public void setReasonDescription(final String reasonDescription) {
		this.reasonDescription = reasonDescription;
	}

	@ManyToOne(optional = false)
	public Member getMember() {
		return this.member;
	}

	public void setMember(final Member member) {
		this.member = member;
	}

	@ManyToOne(optional = false)
	public Procession getProcession() {
		return this.procession;
	}

	public void setProcession(final Procession procession) {
		this.procession = procession;
	}

}
