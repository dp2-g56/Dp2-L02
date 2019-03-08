/*
 * EclipseStream.java
 * 
 * Copyright (C) 2019 Universidad de Sevilla
 * 
 * The use of this project is hereby constrained to the conditions of the
 * TDG Licence, a copy of which you may download from
 * http://www.tdg-seville.info/License.html
 */

package utilities.internal;

import java.io.IOException;
import java.io.OutputStream;

public class EclipseStream extends OutputStream {

	// Constructors -----------------------------------------------------------

	public EclipseStream(final OutputStream originalStream) {
		assert originalStream != null;

		this.target = originalStream;
	}


	// Internal state ---------------------------------------------------------

	private final OutputStream	target;
	private static OutputStream	lastStream;


	// OutputStream interface -------------------------------------------------

	@Override
	public void close() throws IOException {
		this.target.close();
	}

	@Override
	public void flush() throws IOException {
		this.target.flush();
	}

	@Override
	public void write(final byte[] buffer) throws IOException {
		assert buffer != null;

		this.swap();
		this.target.write(buffer);
	}

	@Override
	public void write(final byte[] buffer, final int offset, final int length) throws IOException {
		assert buffer != null;
		assert offset >= 0 && offset < buffer.length;
		assert offset + length - 1 < buffer.length;

		this.swap();
		this.target.write(buffer, offset, length);
	}

	@Override
	public void write(final int datum) throws IOException {
		this.swap();
		this.target.write(datum);
	}

	// Ancillary methods ------------------------------------------------------
	protected void swap() throws IOException {
		if (EclipseStream.lastStream != this && EclipseStream.lastStream != null) {
			EclipseStream.lastStream.flush();
			try {
				Thread.sleep(250);
			} catch (final InterruptedException oops) {
			}
		}
		EclipseStream.lastStream = this;
	}
}
