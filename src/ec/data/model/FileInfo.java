package ec.data.model;

import ec.utils.Formatter;

public class FileInfo {
	private String fileName = "";
	private String filePath = "";
	private int lineCount = 0;
	private int seriesLength = 0;

	public FileInfo(String filePath, int lineCount, int seriesLength) {
		this.filePath = filePath;
		this.fileName = Formatter.fileNameFromPath(filePath);
		this.lineCount = lineCount;
		this.seriesLength = seriesLength;
	}

	public String getFilePath() {
		return filePath;
	}

	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public int getLineCount() {
		return lineCount;
	}

	public void setLineCount(int lineCount) {
		this.lineCount = lineCount;
	}

	public int getSeriesLength() {
		return seriesLength;
	}

	public void setSeriesLength(int seriesLength) {
		this.seriesLength = seriesLength;
	}
}