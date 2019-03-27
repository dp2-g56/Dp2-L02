
package domain;

import java.util.Date;
import java.util.List;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.CascadeType;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.validation.Valid;
import javax.validation.constraints.Past;

import org.hibernate.validator.constraints.NotBlank;
import org.springframework.format.annotation.DateTimeFormat;

@Entity
@Access(AccessType.PROPERTY)
@Table(indexes = { @Index(columnList = "area"), @Index(columnList = "polarity"), @Index(columnList = "history") })
public class Brotherhood extends Actor {

	private String title;
	private Date establishmentDate;
	private List<String> pictures;

	// Relaciones
	private Area area;
	private List<Float> floats;
	private List<Parade> parades;
	private List<Enrolment> enrolments;
	private History history;

	@OneToOne(optional = true, cascade = CascadeType.ALL)
	@Valid
	public History getHistory() {
		return this.history;
	}

	public void setHistory(History history) {
		this.history = history;
	}

	@NotBlank
	public String getTitle() {
		return this.title;
	}

	public void setTitle(final String title) {
		this.title = title;
	}

	@Past
	@DateTimeFormat(pattern = "dd/MM/yyyy HH:mm")
	public Date getEstablishmentDate() {
		return this.establishmentDate;
	}

	public void setEstablishmentDate(final Date establishmentDate) {
		this.establishmentDate = establishmentDate;
	}

	@Valid
	@ElementCollection(targetClass = String.class)
	public List<String> getPictures() {
		return this.pictures;
	}

	public void setPictures(final List<String> pictures) {
		this.pictures = pictures;
	}

	@Valid
	@ManyToOne(optional = true)
	public Area getArea() {
		return this.area;
	}

	public void setArea(final Area area) {
		this.area = area;
	}

	@Valid
	@OneToMany
	public List<Float> getFloats() {
		return this.floats;
	}

	public void setFloats(final List<Float> floats) {
		this.floats = floats;
	}

	@Valid
	@OneToMany
	public List<Parade> getParades() {
		return this.parades;
	}

	public void setParades(final List<Parade> parades) {
		this.parades = parades;
	}

	@Valid
	@OneToMany(mappedBy = "brotherhood")
	public List<Enrolment> getEnrolments() {
		return this.enrolments;
	}

	public void setEnrolments(final List<Enrolment> enrolments) {
		this.enrolments = enrolments;
	}

}
