
package domain;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Entity;

import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.URL;

@Entity
@Access(AccessType.PROPERTY)
public class Sponsorship extends DomainEntity {

	private String	banner;
	private String	targetURL;


	//TODO: Credit card 
	//private CreditCard	creditCard;
	//private Parade	parade;	//Only accepted parades

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

	/*
	 * public Parade getParade() {
	 * return this.parade;
	 * }
	 * 
	 * public void setParade(Parade parade) {
	 * this.parade = parade;
	 * }
	 */

}
