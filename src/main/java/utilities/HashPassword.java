/*
 * HashPassword.java
 * 
 * Copyright (C) 2019 Universidad de Sevilla
 * 
 * The use of this project is hereby constrained to the conditions of the
 * TDG Licence, a copy of which you may download from
 * http://www.tdg-seville.info/License.html
 */

package utilities;

import java.io.IOException;

import org.springframework.security.authentication.encoding.Md5PasswordEncoder;

import utilities.internal.ConsoleReader;
import utilities.internal.ThrowablePrinter;

public class HashPassword {

	public static void main(final String[] args) throws IOException {
		Md5PasswordEncoder encoder;
		ConsoleReader reader;
		String line, hash;

		try {
			System.out.println("HashPassword 1.18.2");
			System.out.println("-------------------");
			System.out.println();

			encoder = new Md5PasswordEncoder();
			reader = new ConsoleReader();

			line = reader.readLine();
			while (!line.equals("quit")) {
				hash = encoder.encodePassword(line, null);
				System.out.println(hash);
				line = reader.readLine();
			}
		} catch (final Throwable oops) {
			ThrowablePrinter.print(oops);
		}
	}

}
