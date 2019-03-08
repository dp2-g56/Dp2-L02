/*
 * Credentials.java
 * 
 * Copyright (C) 2019 Universidad de Sevilla
 * 
 * The use of this project is hereby constrained to the conditions of the
 * TDG Licence, a copy of which you may download from
 * http://www.tdg-seville.info/License.html
 */

package security;

import javax.validation.constraints.Size;

public class Credentials {

	// Constructors -----------------------------------------------------------

	public Credentials() {
		super();
	}


	// Attributes -------------------------------------------------------------

	private String	username;
	private String	password;


	@Size(min = 5, max = 32)
	public String getUsername() {
		return this.username;
	}

	public void setJ_username(final String username) {
		this.username = username;
	}

	@Size(min = 5, max = 32)
	public String getPassword() {
		return this.password;
	}

	public void setPassword(final String password) {
		this.password = password;
	}

}
