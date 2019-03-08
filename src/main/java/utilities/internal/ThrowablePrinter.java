/*
 * ThrowablePrinter.java
 * 
 * Copyright (C) 2019 Universidad de Sevilla
 * 
 * The use of this project is hereby constrained to the conditions of the
 * TDG Licence, a copy of which you may download from
 * http://www.tdg-seville.info/License.html
 */

package utilities.internal;

import java.util.regex.Pattern;

public class ThrowablePrinter {

	public static void print(final Throwable oops) {
		assert oops != null;

		Throwable iterator;
		String message;
		int position;

		System.err.println();
		iterator = oops;
		while (iterator != null) {
			message = iterator.getMessage();
			if (message != null && Pattern.matches("^[a-zA-Z.]+:.*$", message)) {
				position = message.indexOf(':');
				message = message.substring(position + 1).trim();
			}
			System.err.printf("%s: %s%n", iterator.getClass().getName(), message);

			iterator = iterator.getCause();
		}
		System.err.println();
	}

}
