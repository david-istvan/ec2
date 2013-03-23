package ec.data.model;

/**
 * DTO representing a series of doubles with a label.
 * 
 * @author I. Dávid
 * @since 2012.03.11.
 */
public class SeriesNoLabel {
	private float[] series;

	public SeriesNoLabel() {
	}

	public SeriesNoLabel(float[] series) {
		this.series = series;
	}

	public float[] getSeries() {
		return series;
	}

	public void setSeries(float[] series) {
		this.series = series;
	}
}
