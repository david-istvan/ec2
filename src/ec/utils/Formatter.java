package ec.utils;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;

public class Formatter {
	public static DecimalFormat formatDouble(String template) {
		DecimalFormatSymbols sym = new DecimalFormatSymbols();
		sym.setDecimalSeparator('.');
		return new DecimalFormat(template, sym);
	}

	public static String fileNameFromPath(String fullPath) {
		String[] parts = fullPath.split("\\\\");
		return parts[parts.length - 1];
	}
}