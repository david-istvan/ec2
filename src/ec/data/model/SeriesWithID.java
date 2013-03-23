package ec.data.model;

public class SeriesWithID {
	protected int id;
	protected float[] series;

	public SeriesWithID() {
	}

	public SeriesWithID(int id, float[] series) {
		this.id = id;
		this.series = series;
	}

	public int getId() {
		return id;
	}

	public float[] getSeries() {
		return series;
	}

	public void setSeries(float[] series) {
		this.series = series;
	}
}
