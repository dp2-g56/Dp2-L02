/*
 * EclipseConsole.java
 * 
 * Copyright (C) 2019 Universidad de Sevilla
 * 
 * The use of this project is hereby constrained to the conditions of the
 * TDG Licence, a copy of which you may download from
 * http://www.tdg-seville.info/License.html
 */

package utilities.internal;

import java.io.PrintStream;

public class EclipseConsole {

	// Internal state ---------------------------------------------------------

	private static boolean	isFixed	= false;


	// Business methods -------------------------------------------------------

	public static void fix() {
		// Inserts a 200ms delay into the System.err or System.out OutputStreams
		// every time the output switches from one to the other. This prevents the
		// Eclipse console from showing the output of the two streams out of order.
		EclipseStream out, err;
		try {
			if (!EclipseConsole.isFixed) {
				EclipseConsole.isFixed = true;
				out = new EclipseStream(System.out);
				err = new EclipseStream(System.err);
				System.setOut(new PrintStream(out, true, "iso-8859-1"));
				System.setErr(new PrintStream(err, true, "iso-8859-1"));
			}
		} catch (final Throwable oops) {
			throw new RuntimeException(oops);
		}
	}

}
