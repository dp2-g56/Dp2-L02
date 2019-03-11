package domain;

import java.util.List;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.validation.Valid;
import javax.validation.constraints.Pattern;

import org.hibernate.validator.constraints.NotBlank;

@Entity
@Access(AccessType.PROPERTY)
public class LegalRecord extends DomainEntity {

	private String title;
	private String description;
	private String legalName;
	private String vatNumber;
	private List<String> laws;

	public LegalRecord() {
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

	@NotBlank
	public String getLegalName() {
		return this.legalName;
	}

	public void setLegalName(String legalName) {
		this.legalName = legalName;
	}

	@NotBlank
	@Pattern(regexp = "ES[0-9]{8}[A-Z]{1}")
	public String getVatNumber() {
		return this.vatNumber;
	}

	public void setVatNumber(String vatNumber) {
		this.vatNumber = vatNumber;
	}

	@Valid
	@ElementCollection(targetClass = String.class)
	public List<String> getLaws() {
		return this.laws;
	}

	public void setLaws(List<String> laws) {
		this.laws = laws;
	}

}
