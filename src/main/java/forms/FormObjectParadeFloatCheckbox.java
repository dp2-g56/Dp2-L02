
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

public class FormObjectParadeFloatCheckbox {

	//No añadir el @Valid
	//Atributos del desfile
	//Al controlador pasarle un @Valid FormObjectParadeCoach
	//Se puede validar en este objeto
	//Reconstruct de los dos tipos de objetos
	//No extiende a Domain Entity
	private String			titleParade;
	private String			descriptionParade;
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
	public String getTitleParade() {
		return this.titleParade;
	}

	public void setTitleParade(String titleParade) {
		this.titleParade = titleParade;
	}

	@NotBlank
	public String getDescriptionParade() {
		return this.descriptionParade;
	}

	public void setDescriptionParade(String descriptionParade) {
		this.descriptionParade = descriptionParade;
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
