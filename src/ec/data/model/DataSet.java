package ec.data.model;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Class that represents the data set containing the examined time series.
 * 
 * @author I. Dávid
 * @since 2012.04.06.
 */
public class DataSet {
	private FileInfo fileInfo;

	/**
	 * Constructor - specifies the number of the available time series in the
	 * data set (i.e. the "line number") and the length of the single time
	 * series, which is assumed to be the same for all series.
	 * 
	 * @param fileName
	 *            The location of the physical file.
	 */
	public DataSet(String fileName) {
		this.fileInfo = new FileInfo(fileName, 0, 0);
		try {
			String line = "";
			FileReader fr = new FileReader(fileName);
			BufferedReader br = new BufferedReader(fr);

			while ((line = br.readLine()) != null) {
				this.fileInfo.setLineCount(fileInfo.getLineCount() + 1);

				if (this.fileInfo.getLineCount() == 1) {
					String[] rawData = line.trim().replace("  ", " ").split(" ");
					this.fileInfo.setSeriesLength(rawData.length - 1);
				}
			}
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Reads a subset of the selected lines.
	 * 
	 * @param lineIDs
	 *            The ID's of the selected lines.
	 * @param seriesLength
	 *            Defines the desired length of the subset.
	 * @return A list of series values and a label (SeriesWithLabel).
	 * @throws IOException
	 */
	public List<SeriesWithLabel> readLines(int[] lineIDs, int seriesLength) throws IOException {
		String line = "";
		FileReader fr = new FileReader(this.fileInfo.getFilePath());
		BufferedReader br = new BufferedReader(fr);
		int index = 0;
		List<SeriesWithLabel> series = new ArrayList<SeriesWithLabel>();

		while (((line = br.readLine()) != null) && (index <= lineIDs[lineIDs.length - 1])) {
			for (int i = 0; i < lineIDs.length; i++) {
				if (lineIDs[i] == index) {
					float label = Float.parseFloat(line.trim().replace("  ", " ").split(" ")[0]);
					String[] rawData = line.trim().replace("  ", " ").split(" ");
					float[] data = new float[seriesLength];

					for (int j = 0; j < seriesLength; j++) {
						if (j < rawData.length - 1) {
							data[j] = Float.parseFloat(rawData[j + 1]);
						}
					}
					series.add(new SeriesWithLabel(label, data));
					break;
				}
			}
			index++;
		}
		return series;
	}

	public List<SeriesNoLabel> readLines2(int[] lineIDs, int seriesLength) throws IOException {
		String line = "";
		FileReader fr = new FileReader(this.fileInfo.getFilePath());
		BufferedReader br = new BufferedReader(fr);
		int index = 0;
		List<SeriesNoLabel> series = new ArrayList<SeriesNoLabel>();

		while (((line = br.readLine()) != null) && (index <= lineIDs[lineIDs.length - 1])) {
			for (int i = 0; i < lineIDs.length; i++) {
				if (lineIDs[i] == index) {
					String[] rawData = line.trim().replace("  ", " ").split(" ");
					float[] data = new float[seriesLength];

					for (int j = 0; j < seriesLength; j++) {
						if (j < rawData.length - 1) {
							data[j] = Float.parseFloat(rawData[j + 1]);
						}
					}
					series.add(new SeriesNoLabel(data));
					break;
				}
			}
			index++;
		}
		return series;
	}

	public FileInfo getFileInfo() {
		return fileInfo;
	}
}