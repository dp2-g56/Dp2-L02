
package domain;

import java.util.List;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.validation.Valid;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.Pattern;

import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.URL;

import security.UserAccount;

@Entity
@Access(AccessType.PROPERTY)
@Table(indexes = {
	@Index(columnList = "hasSpam")
})
public class Actor extends DomainEntity {

	private String				name;
	private String				middleName;
	private String				surname;
	private String				photo;
	private String				email;
	private String				phoneNumber;
	private String				address;
	private Boolean				hasSpam;
	private double				polarity;

	//Dependencias
	private List<SocialProfile>	socialProfiles;
	private List<Box>			boxes;

	private UserAccount			userAccount;


	@Min(-1)
	@Max(1)
	public double getPolarity() {
		return this.polarity;
	}

	public void setPolarity(final double polarity) {
		this.polarity = polarity;
	}

	@OneToMany(cascade = CascadeType.ALL)
	@Valid
	public List<Box> getBoxes() {
		return this.boxes;
	}

	public void setBoxes(final List<Box> boxes) {
		this.boxes = boxes;
	}

	@OneToMany(cascade = CascadeType.ALL)
	@Valid
	public List<SocialProfile> getSocialProfiles() {
		return this.socialProfiles;
	}

	public void setSocialProfiles(final List<SocialProfile> socialProfiles) {
		this.socialProfiles = socialProfiles;
	}

	@NotBlank
	public String getName() {
		return this.name;
	}

	public void setName(final String name) {
		this.name = name;
	}

	@Valid
	public String getMiddleName() {
		return this.middleName;
	}

	public void setMiddleName(final String middleName) {
		this.middleName = middleName;
	}

	@NotBlank
	public String getSurname() {
		return this.surname;
	}

	public void setSurname(final String surname) {
		this.surname = surname;
	}

	@URL
	public String getPhoto() {
		return this.photo;
	}

	public void setPhoto(final String photo) {
		this.photo = photo;
	}

	@NotBlank
	//@Email
	@Pattern(regexp = "[\\w.%-]+\\@[-.\\w]+\\.[A-Za-z]{2,4}|[\\w.%-]+\\<+[\\w.%-]+\\@[-.\\w]+\\.[A-Za-z]{2,4}|[\\w.%-]+\\<[\\w.%-]+\\@+\\>|[\\w.%-]+",
		message = "Email doesn't follow the pattern, must be identifier@domain.asd or alias <identifier@domain.asd>")
	@Column(unique = true)
	public String getEmail() {
		return this.email;
	}

	public void setEmail(final String email) {
		this.email = email;
	}

	@Valid
	public String getPhoneNumber() {
		return this.phoneNumber;
	}

	public void setPhoneNumber(final String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	@Valid
	public String getAddress() {
		return this.address;
	}

	public void setAddress(final String address) {
		this.address = address;
	}

	@OneToOne(optional = false, cascade = CascadeType.ALL)
	@Valid
	public UserAccount getUserAccount() {
		return this.userAccount;
	}

	public void setUserAccount(final UserAccount userAccount) {
		this.userAccount = userAccount;
	}

	public Boolean getHasSpam() {
		return this.hasSpam;
	}

	public void setHasSpam(final Boolean hasSpam) {
		this.hasSpam = hasSpam;
	}

}
