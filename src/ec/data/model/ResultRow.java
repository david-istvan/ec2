package ec.data.model;

public class ResultRow {
	private final int seriesLength;
	private final double correctLabels;
	private double variance;
	private double relativeAccuracy;

	public ResultRow(int seriesLength, double correctLabels, double variance) {
		this.seriesLength = seriesLength;
		this.correctLabels = correctLabels;
		this.variance = variance;
		this.relativeAccuracy = 0;
	}

	public int getSeriesLength() {
		return seriesLength;
	}

	public double getCorrectLabels() {
		return correctLabels;
	}

	public double getVariance() {
		return variance;
	}

	public double getRelativeAccuracy() {
		return relativeAccuracy;
	}

	public void setRelativeAccuracy(double relativeAccuracy) {
		this.relativeAccuracy = relativeAccuracy;
	}
}