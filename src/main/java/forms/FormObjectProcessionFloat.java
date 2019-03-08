
package forms;

import java.util.Date;

import javax.validation.constraints.Digits;
import javax.validation.constraints.Future;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotBlank;
import org.springframework.format.annotation.DateTimeFormat;

public class FormObjectProcessionFloat {

	//No añadir el @Valid
	//Atributos de la procesion
	//Al controlador pasarle un @Valid FormObjectProcessionCoach
	//Se puede validar en este objeto
	//Reconstruct de los dos tipos de objetos
	//No extiende a Domain Entity
	private String	titleProcession;
	private String	descriptionProcession;
	private Date	moment;
	private Boolean	isDraftMode;
	private int		rowNumber;
	private int		columnNumber;

	//Atributos del paso
	private String	title;
	private String	description;


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

}
