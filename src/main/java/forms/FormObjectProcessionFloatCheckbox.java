
package forms;

import java.util.Date;
import java.util.List;

import javax.persistence.ElementCollection;
import javax.validation.constraints.Digits;
import javax.validation.constraints.Future;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.format.annotation.DateTimeFormat;

public class FormObjectProcessionFloatCheckbox {

	//No añadir el @Valid
	//Atributos de la procesion
	//Al controlador pasarle un @Valid FormObjectProcessionCoach
	//Se puede validar en este objeto
	//Reconstruct de los dos tipos de objetos
	//No extiende a Domain Entity
	private String			titleProcession;
	private String			descriptionProcession;
	private Date			moment;
	//private String			ticker;
	private Boolean			isDraftMode;
	private int				rowNumber;
	private int				columnNumber;
	private int				id;

	//Atributos del paso
	private List<Integer>	floats;


	@NotNull
	public int getId() {
		return this.id;
	}

	public void setId(int id) {
		this.id = id;
	}

	@ElementCollection(targetClass = Integer.class)
	@NotEmpty
	public List<Integer> getFloats() {
		return this.floats;
	}

	public void setFloats(List<Integer> floats) {
		this.floats = floats;
	}

	@NotBlank
	public String getTitleProcession() {
		return this.titleProcession;
	}

	public void setTitleProcession(String titleProcession) {
		this.titleProcession = titleProcession;
	}

	@NotBlank
	public String getDescriptionProcession() {
		return this.descriptionProcession;
	}

	public void setDescriptionProcession(String descriptionProcession) {
		this.descriptionProcession = descriptionProcession;
	}

	@DateTimeFormat(pattern = "dd/MM/yyyy HH:mm")
	@NotNull
	@Future
	public Date getMoment() {
		return this.moment;
	}

	public void setMoment(Date moment) {
		this.moment = moment;
	}

	@NotNull
	public Boolean getIsDraftMode() {
		return this.isDraftMode;
	}

	public void setIsDraftMode(boolean isDraftMode) {
		this.isDraftMode = isDraftMode;
	}

	@NotNull
	@Min(1)
	@Digits(integer = 6, fraction = 0)
	public int getRowNumber() {
		return this.rowNumber;
	}

	public void setRowNumber(int rowNumber) {
		this.rowNumber = rowNumber;
	}

	@NotNull
	@Min(1)
	@Digits(integer = 6, fraction = 0)
	public int getColumnNumber() {
		return this.columnNumber;
	}

	public void setColumnNumber(int columnNumber) {
		this.columnNumber = columnNumber;
	}

}
