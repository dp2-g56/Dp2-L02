/*
 * ConsoleReader.java
 * 
 * Copyright (C) 2019 Universidad de Sevilla
 * 
 * The use of this project is hereby constrained to the conditions of the
 * TDG Licence, a copy of which you may download from
 * http://www.tdg-seville.info/License.html
 */

package utilities.internal;

import java.io.BufferedReader;
import java.io.InputStreamReader;

import org.apache.commons.lang.StringUtils;

public class ConsoleReader {

	private final InputStreamReader	stream;
	private final BufferedReader	reader;


	public ConsoleReader() {
		this.stream = new InputStreamReader(System.in);
		this.reader = new BufferedReader(this.stream);
	}

	public String readCommand() throws Throwable {
		String result;
		StringBuilder buffer;
		String line;
		String prompt;

		prompt = "> ";
		buffer = new StringBuilder();
		do {
			System.out.printf(prompt);
			line = this.reader.readLine();
			line = StringUtils.trim(line);
			buffer.append(line);
			buffer.append(' ');
			if (!line.isEmpty()) {
				prompt = "\t> ";
			}
		} while (line != null && !line.endsWith(";"));

		if (line != null && line.endsWith(";") && buffer.length() >= 2) {
			buffer.deleteCharAt(buffer.length() - 2);
		}

		result = StringUtils.trim(buffer.toString());

		return result;
	}

	public String readLine() throws Throwable {
		String result;

		System.out.printf("> ");
		result = this.reader.readLine();
		result = StringUtils.trim(result);

		return result;
	}

}
