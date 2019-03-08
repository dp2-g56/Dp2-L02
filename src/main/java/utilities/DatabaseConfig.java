/*
 * DatabaseConfig.java
 * 
 * Copyright (C) 2019 Universidad de Sevilla
 * 
 * The use of this project is hereby constrained to the conditions of the
 * TDG Licence, a copy of which you may download from
 * http://www.tdg-seville.info/License.html
 */

package utilities;

public interface DatabaseConfig {

	public final String	PersistenceUnit				= "Acme-Madruga";

	public final String	entitySpecificationFilename	= "./src/main/resources/PopulateDatabase.xml";
	public final String	entityMapFilename			= "./src/main/resources/Entities.map";

}
