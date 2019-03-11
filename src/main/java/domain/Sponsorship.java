
package domain;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.validation.constraints.Digits;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.URL;

@Entity
@Access(AccessType.PROPERTY)
public class Sponsorship extends DomainEntity {

	private String banner;
	private String targetURL;
	private java.lang.Float gain;

	private CreditCard creditCard;
	private Parade parade;

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
	@Digits(integer = 9, fraction = 2)
	public java.lang.Float getGain() {
		return this.gain;
	}

	public void setGain(java.lang.Float gain) {
		this.gain = gain;
	}

	@NotNull
	public CreditCard getCreditCard() {
		return this.creditCard;
	}

	public void setCreditCard(CreditCard creditCard) {
		this.creditCard = creditCard;
	}

	@NotNull
	@ManyToOne(optional = false)
	public Parade getParade() {
		return this.parade;
	}

	public void setParade(Parade parade) {
		this.parade = parade;
	}

	/*
	 * public Parade getParade() { return this.parade; }
	 *
	 * public void setParade(Parade parade) { this.parade = parade; }
	 */

}
