
package forms;

import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.URL;

import domain.Parade;

public class FormObjectSponsorshipCreditCard {

	// No añadir el @Valid
	// Atributos del sponsorship
	// Al controlador pasarle un @Valid FormObjectSponsorshipCreditCard
	// Se puede validar en este objeto
	// Reconstruct de los dos tipos de objetos
	// No extiende a Domain Entity
	private String banner;
	private String targetURL;
	private Parade parade;

	// Atributos de la credit card
	private String holderName;
	private String brandName;
	private Long number;
	private Integer expirationMonth;
	private Integer expirationYear;
	private Integer cvvCode;

	public FormObjectSponsorshipCreditCard() {

	}

	@URL
	@NotBlank
	public String getBanner() {
		return this.banner;
	}

	public void setBanner(String banner) {
		this.banner = banner;
	}

	@URL
	@NotBlank
	public String getTargetURL() {
		return this.targetURL;
	}

	public void setTargetURL(String targetURL) {
		this.targetURL = targetURL;
	}

	@NotNull
	@ManyToOne(optional = false)
	public Parade getParade() {
		return this.parade;
	}

	public void setParade(Parade parade) {
		this.parade = parade;
	}

	@NotBlank
	public String getHolderName() {
		return this.holderName;
	}

	public void setHolderName(String holderName) {
		this.holderName = holderName;
	}

	@NotBlank
	public String getBrandName() {
		return this.brandName;
	}

	public void setBrandName(String brandName) {
		this.brandName = brandName;
	}

	@NotNull
	public Long getNumber() {
		return this.number;
	}

	public void setNumber(Long number) {
		this.number = number;
	}

	@NotNull
	public Integer getExpirationMonth() {
		return this.expirationMonth;
	}

	public void setExpirationMonth(Integer expirationMonth) {
		this.expirationMonth = expirationMonth;
	}

	@NotNull
	public Integer getExpirationYear() {
		return this.expirationYear;
	}

	public void setExpirationYear(Integer expirationYear) {
		this.expirationYear = expirationYear;
	}

	@NotNull
	public Integer getCvvCode() {
		return this.cvvCode;
	}

	public void setCvvCode(Integer cvvCode) {
		this.cvvCode = cvvCode;
	}

}
