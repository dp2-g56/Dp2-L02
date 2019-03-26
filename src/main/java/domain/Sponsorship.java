
package domain;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.URL;

@Entity
@Access(AccessType.PROPERTY)
@Table(indexes = { @Index(columnList = "isActivated") })
public class Sponsorship extends DomainEntity {

	private String			banner;
	private String			targetURL;
	private java.lang.Float	spentMoney;
	private Boolean			isActivated;

	private CreditCard		creditCard;
	private Parade			parade;


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
	public java.lang.Float getSpentMoney() {
		return this.spentMoney;
	}

	public void setSpentMoney(java.lang.Float spentMoney) {
		this.spentMoney = spentMoney;
	}

	@Valid
	@NotNull
	public CreditCard getCreditCard() {
		return this.creditCard;
	}

	public void setCreditCard(CreditCard creditCard) {
		this.creditCard = creditCard;
	}

	@ManyToOne(optional = true)
	public Parade getParade() {
		return this.parade;
	}

	public void setParade(Parade parade) {
		this.parade = parade;
	}

	@NotNull
	public Boolean getIsActivated() {
		return this.isActivated;
	}

	public void setIsActivated(Boolean isActivated) {
		this.isActivated = isActivated;
	}

}
