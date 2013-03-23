package ec.utils;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import ec.data.model.ResultRow;

/**
 * @author I. Dávid
 * @since 2012.03.08.
 */
public class FileIO {
	private File outputFile;
	private String outputHeader;

	/**
	 * Flushes the result.
	 */
	public void flushResult(int sampleLength, double correctLabels, double variance, String filePath) {
		try {
			this.outputHeader = "SAMPLELENGTH, ACCURACY, VARIANCE\r\n";
			File file = getOutputFile(filePath);
			FileWriter outFile = new FileWriter(file, true);
			PrintWriter out = new PrintWriter(outFile);
			out.print(sampleLength + ", " + Formatter.formatDouble("###########.###").format(correctLabels) + ", "
					+ Formatter.formatDouble("###########.###").format(variance) + "\r\n");
			out.close();
		}
		catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void flushResult(List<ResultRow> result, String filePath) {
		try {
			this.outputHeader = "SAMPLELENGTH, ACCURACY, VARIANCE, RELATIVEACCURACY\r\n";
			File file = getOutputFile(filePath);
			FileWriter outFile = new FileWriter(file, true);
			PrintWriter out = new PrintWriter(outFile);
			for (ResultRow resultRow : result) {
				out.print(resultRow.getSeriesLength() + ", "
						+ Formatter.formatDouble("###########.###").format(resultRow.getCorrectLabels()) + ", "
						+ Formatter.formatDouble("#.#####").format(resultRow.getVariance()) + ", "
						+ Formatter.formatDouble("#.#####").format(resultRow.getRelativeAccuracy()) + "\r\n");
			}
			out.close();
		}
		catch (IOException e) {
			e.printStackTrace();
		}
	}

	private File getOutputFile(String name) {
		// FIXME: quick 'n dirty
		if (name.contains("/")) name = name.substring(name.lastIndexOf("/") + 1, name.length());
		Config config = Config.getInstance();
		if ((this.outputFile == null)
				|| !(this.outputFile.getPath().equalsIgnoreCase((config.getOutputPath() + name).replace("\\\\", "\\")))) {
			this.outputFile = new File(config.getOutputPath() + name);
			try {
				FileWriter outFile = new FileWriter(this.outputFile);
				PrintWriter out = new PrintWriter(outFile);
				out.print(this.outputHeader);
				out.close();
			}
			catch (IOException e) {
				e.printStackTrace();
			}
		}
		return this.outputFile;
	}

	/**
	 * For batch processing only
	 */
	public void flushStats(String databaseName, int setsSize, int samplesPerLines, String resultFileName, File file) {
		try {
			FileWriter outFile = new FileWriter(file);
			PrintWriter out = new PrintWriter(outFile);
			out.print("==========DIAGNOSTICAL STATS==========\r\n\r\n\r\n");
			out.close();
			FileWriter outFile2 = new FileWriter(file, true);
			PrintWriter out2 = new PrintWriter(outFile2);
			out2.print("ASSOCIATED RESULT FILE:\t\t\t" + resultFileName + "\r\n\r\n");
			out2.print("DATABASE NAME:\t\t\t\t" + databaseName + "\r\n\r\n");
			out2.print("SIZE OF THE TRAINING AND TEST SETS:\t" + setsSize + "\r\n");
			out2.print("(Max SUCCESS value.)\r\n\r\n");
			out2.print("SAMPLES PER SERIES:\t\t\t" + samplesPerLines + "\r\n");
			out2.print("(Max sample length.)");
			out2.close();
		}
		catch (IOException e) {
			e.printStackTrace();
		}
	}
}