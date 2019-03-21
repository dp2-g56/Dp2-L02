package domain;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Entity;
import javax.validation.constraints.NotNull;

@Entity
@Access(AccessType.PROPERTY)
public class Segment extends DomainEntity {

	private Double originLatitude;
	private Double originLongitude;
	private Double destinationLatitude;
	private Double destinationLongitude;
	private Integer time;

	@NotNull
	public Double getOriginLatitude() {
		return this.originLatitude;
	}

	public void setOriginLatitude(Double originLatitude) {
		this.originLatitude = originLatitude;
	}

	@NotNull
	public Double getOriginLongitude() {
		return this.originLongitude;
	}

	public void setOriginLongitude(Double originLongitude) {
		this.originLongitude = originLongitude;
	}

	@NotNull
	public Double getDestinationLatitude() {
		return this.destinationLatitude;
	}

	public void setDestinationLatitude(Double destinationLatitude) {
		this.destinationLatitude = destinationLatitude;
	}

	@NotNull
	public Double getDestinationLongitude() {
		return this.destinationLongitude;
	}

	public void setDestinationLongitude(Double destinationLongitude) {
		this.destinationLongitude = destinationLongitude;
	}

	@NotNull
	public Integer getTime() {
		return this.time;
	}

	public void setTime(Integer time) {
		this.time = time;
	}

	@Override
	public String toString() {
		return "Segment [getOriginLatitude()=" + this.getOriginLatitude() + ", getOriginLongitude()="
				+ this.getOriginLongitude() + ", getDestinationLatitude()=" + this.getDestinationLatitude()
				+ ", getDestinationLongitude()=" + this.getDestinationLongitude() + ", getTime()=" + this.getTime()
				+ "]";
	}

}
