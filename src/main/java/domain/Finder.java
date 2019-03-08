
package domain;

import java.util.Date;
import java.util.List;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Entity;
import javax.persistence.ManyToMany;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.Valid;

import org.springframework.format.annotation.DateTimeFormat;

@Entity
@Access(AccessType.PROPERTY)
public class Finder extends DomainEntity {

	private String				keyWord;
	private String				area;
	private Date				minDate;
	private Date				maxDate;
	private Date				lastEdit;

	private List<Parade>	parades;


	@Valid
	public String getKeyWord() {
		return this.keyWord;
	}

	public void setKeyWord(final String keyWord) {
		this.keyWord = keyWord;
	}

	@Valid
	@Temporal(TemporalType.TIMESTAMP)
	@DateTimeFormat(pattern = "dd/MM/yyyy HH:mm")
	public Date getMinDate() {
		return this.minDate;
	}

	public void setMinDate(final Date minDate) {
		this.minDate = minDate;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Valid
	@DateTimeFormat(pattern = "dd/MM/yyyy HH:mm")
	public Date getMaxDate() {
		return this.maxDate;
	}

	public void setMaxDate(final Date maxDate) {
		this.maxDate = maxDate;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Valid
	@DateTimeFormat(pattern = "dd/MM/yyyy HH:mm")
	public Date getLastEdit() {
		return this.lastEdit;
	}

	public void setLastEdit(final Date lastEdit) {
		this.lastEdit = lastEdit;
	}

	@Valid
	public String getArea() {
		return this.area;
	}

	public void setArea(final String area) {
		this.area = area;
	}

	@Valid
	@ManyToMany
	public List<Parade> getParades() {
		return this.parades;
	}

	public void setParades(final List<Parade> parades) {
		this.parades = parades;
	}

}
