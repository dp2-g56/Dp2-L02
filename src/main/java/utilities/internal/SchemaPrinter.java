/*
 * SchemaPrinter.java
 * 
 * Copyright (C) 2019 Universidad de Sevilla
 * 
 * The use of this project is hereby constrained to the conditions of the
 * TDG Licence, a copy of which you may download from
 * http://www.tdg-seville.info/License.html
 */

package utilities.internal;

import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Field;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class SchemaPrinter {

	public static void print(final Collection<?> objects) {
		for (final Object obj : objects)
			SchemaPrinter.print(obj);
	}

	public static void print(final Object obj) {
		String text;
		StringBuffer buffer;

		buffer = new StringBuffer();
		if (SchemaPrinter.isValue(obj))
			SchemaPrinter.printValue(buffer, obj, true);
		else
			SchemaPrinter.printRecord(buffer, obj, false);

		text = buffer.toString();
		System.out.printf("%s%n", text);
	}

	protected static void printValue(final StringBuffer buffer, final Object value, final boolean summary) {
		if (SchemaPrinter.isPrimitive(value) || SchemaPrinter.isEnum(value))
			SchemaPrinter.printPrimitive(buffer, value, summary);
		else if (SchemaPrinter.isArray(value))
			SchemaPrinter.printArray(buffer, (Object[]) value, summary);
		else if (SchemaPrinter.isCollection(value))
			SchemaPrinter.printCollection(buffer, (Collection<?>) value, summary);
		else
			SchemaPrinter.printRecord(buffer, value, true);
	}

	protected static void printRecord(final StringBuffer buffer, final Object obj, final boolean summary) {
		List<Class<?>> superClazzes;
		Class<?> clazz;

		buffer.append(obj.toString());
		if (!summary) {
			clazz = obj.getClass();
			superClazzes = new ArrayList<Class<?>>();
			while (clazz != null) {
				superClazzes.add(clazz);
				clazz = clazz.getSuperclass();
			}

			for (int i = superClazzes.size() - 1; i >= 0; i--) {
				clazz = superClazzes.get(i);
				SchemaPrinter.printFieldsInClazz(buffer, clazz, obj);
			}
		}
	}
	protected static void printFieldsInClazz(final StringBuffer buffer, final Class<?> clazz, final Object obj) {
		Field fields[];
		String name;
		Class<?> type;
		Object value;

		fields = clazz.getDeclaredFields();
		AccessibleObject.setAccessible(fields, true);
		for (final Field field : fields) {
			name = field.getName();
			type = field.getType();
			try {
				value = field.get(obj);
			} catch (final Throwable oops) {
				value = String.format("{%s}", oops.getMessage());
			}

			buffer.append("\n\t");
			buffer.append(clazz.getName());
			buffer.append("::");
			buffer.append(name);
			buffer.append(": ");
			SchemaPrinter.printType(buffer, type);
			buffer.append(" = ");
			SchemaPrinter.printValue(buffer, value, true);
		}
	}

	protected static void printPrimitive(final StringBuffer buffer, final Object value, final boolean summary) {
		String left, right;

		if (value == null)
			left = right = "";
		else if (value instanceof String)
			left = right = "\"";
		else if (value instanceof Number)
			left = right = "";
		else if (value instanceof Character)
			left = right = "\'";
		else if (value instanceof Boolean)
			left = right = "";
		else {
			left = "<<";
			right = ">>";
		}

		buffer.append(left);
		buffer.append(value);
		buffer.append(right);
	}

	protected static void printArray(final StringBuffer buffer, final Object[] value, final boolean summary) {
		String separator;

		separator = "";
		buffer.append("[");
		for (final Object item : value) {
			buffer.append(separator);
			SchemaPrinter.printValue(buffer, item, summary);
			separator = ", ";
		}
		buffer.append("]");
	}

	private static void printCollection(final StringBuffer buffer, final Collection<?> value, final boolean summary) {
		String separator;

		separator = "";
		buffer.append("[");
		for (final Object item : value) {
			buffer.append(separator);
			SchemaPrinter.printValue(buffer, item, summary);
			separator = ", ";
		}
		buffer.append("]");
	}

	protected static void printType(final StringBuffer buffer, final Class<?> clazz) {
		String type;

		type = clazz.getName();
		buffer.append(type);
	}

	protected static void printType(final StringBuffer buffer, final Object value) {
		String type;

		if (value == null)
			type = Object.class.getName();
		else
			type = value.getClass().getName();
		buffer.append(type);
	}

	protected static boolean isPrimitive(final Object obj) {
		boolean result;

		result = (obj == null || obj instanceof String || obj instanceof Number || obj instanceof Character || obj instanceof Boolean || obj instanceof java.util.Date || obj instanceof java.sql.Date || obj instanceof Timestamp);

		return result;
	}

	protected static boolean isArray(final Object obj) {
		boolean result;

		result = (obj != null && obj.getClass().getName().charAt(0) == '[');

		return result;
	}

	protected static boolean isEnum(final Object obj) {
		boolean result;

		result = (obj == null || obj instanceof Enum);

		return result;
	}

	protected static boolean isCollection(final Object obj) {
		boolean result;

		result = (obj != null && obj instanceof Collection);

		return result;
	}

	protected static boolean isValue(final Object obj) {
		boolean result;

		result = (SchemaPrinter.isPrimitive(obj) || SchemaPrinter.isArray(obj) || SchemaPrinter.isEnum(obj));

		return result;
	}

	protected static boolean isRecord(final Object obj) {
		boolean result;

		result = !SchemaPrinter.isValue(obj);

		return result;
	}

}
