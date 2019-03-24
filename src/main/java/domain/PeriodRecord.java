
package domain;

import java.util.List;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotBlank;

@Entity
@Access(AccessType.PROPERTY)
public class PeriodRecord extends DomainEntity {

	private String			title;
	private String			description;
	private Integer			startYear;
	private Integer			endYear;
	private List<String>	photos;


	public PeriodRecord() {
		super();
	}

	@NotBlank
	public String getTitle() {
		return this.title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	@NotBlank
	public String getDescription() {
		return this.description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	@NotNull
	@Min(1)
	public Integer getStartYear() {
		return this.startYear;
	}

	public void setStartYear(Integer startYear) {
		this.startYear = startYear;
	}

	@NotNull
	@Min(1)
	public Integer getEndYear() {
		return this.endYear;
	}

	public void setEndYear(Integer endYear) {
		this.endYear = endYear;
	}

	@Valid
	@ElementCollection(targetClass = String.class)
	public List<String> getPhotos() {
		return this.photos;
	}

	public void setPhotos(List<String> photos) {
		this.photos = photos;
	}

}
