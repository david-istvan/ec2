package ec.data.model;

/**
 * DTO representing a series of doubles with a label.
 * 
 * @author I. Dávid
 * @since 2012.03.11.
 */
public class SeriesWithLabel {
	private float label;
	private float[] series;

	public SeriesWithLabel() {
	}

	public SeriesWithLabel(float label, float[] series) {
		this.label = label;
		this.series = series;
	}

	public float getLabel() {
		return label;
	}

	public void setLabel(float label) {
		this.label = label;
	}

	public float[] getSeries() {
		return series;
	}

	public void setSeries(float[] series) {
		this.series = series;
	}
}
