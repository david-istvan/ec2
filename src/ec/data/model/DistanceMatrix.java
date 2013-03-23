package ec.data.model;

import java.util.List;

import buza.attribute.distance.ManhattanDistance;
import buza.timeseries.distance.DTW;

public class DistanceMatrix {
	private static DistanceMatrix instance;
	private float[][] distances;
	private DTW distance;

	private DistanceMatrix() {
		this.distance = new DTW(0, new ManhattanDistance());
	}

	// TODO: create custom error in ec.errors
	public static DistanceMatrix getInstance() throws Exception {
		if (instance == null) {
			throw new Exception("Distances not initialized.");
		}
		return instance;
	}

	public static DistanceMatrix getInstance(List<SeriesWithID> allSeriesWithId) {
		if (instance == null) {
			instance = new DistanceMatrix();
			instance.calulateDistances(allSeriesWithId);
		}
		return instance;
	}

	private void calulateDistances(List<SeriesWithID> allSeriesWithId) {
		System.out.println("initializing distance matrix...");
		this.distances = new float[allSeriesWithId.size()][allSeriesWithId.size()];
		for (SeriesWithID series1 : allSeriesWithId) {
			for (SeriesWithID series2 : allSeriesWithId) {
				distances[series1.getId()][series2.getId()] = distance.calculateDistance(series1.getSeries(), series2.getSeries());
			}
		}
	}

	public float[][] getDistances() {
		return distances;
	}

	public float getDistance(int series1ID, int series2ID) {
		return distances[series1ID][series2ID];
	}
}