package ec.test.mocks;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import ec.data.model.Cluster;
import ec.data.model.DataSet;
import ec.data.model.DistanceMatrix;
import ec.data.model.SeriesWithID;

public class KMedoidsMock {
	private DataSet dataSet;
	private int sampleLength;
	@SuppressWarnings("unused")
	private DistanceMatrix distanceMatrix;
	private List<SeriesWithID> allSeriesWithId;
	private List<Cluster> clusters;

	public KMedoidsMock(DataSet dataSet, int sampleLength, int clusterNum) {
		this.dataSet = dataSet;
		this.sampleLength = sampleLength;
		this.allSeriesWithId = new ArrayList<SeriesWithID>();
		this.clusters = new ArrayList<Cluster>();
		try {
			init();
		}
		catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void init() throws IOException {
		for (int i = 0; i < this.dataSet.getFileInfo().getLineCount(); i++) {
			int[] seriesid = { i };
			this.allSeriesWithId.add(new SeriesWithID(i, this.dataSet.readLines2(seriesid, this.sampleLength).get(0).getSeries()));
		}
		this.distanceMatrix = DistanceMatrix.getInstance(this.allSeriesWithId);
	}

	public List<Cluster> getClusters() throws Exception {
		List<SeriesWithID> c0Members = new ArrayList<SeriesWithID>();
		List<SeriesWithID> c1Members = new ArrayList<SeriesWithID>();

		c0Members.add(this.allSeriesWithId.get(0));
		c0Members.add(this.allSeriesWithId.get(3));
		c1Members.add(this.allSeriesWithId.get(1));
		c1Members.add(this.allSeriesWithId.get(2));
		c1Members.add(this.allSeriesWithId.get(4));
		c1Members.add(this.allSeriesWithId.get(5));
		c1Members.add(this.allSeriesWithId.get(6));
		c1Members.add(this.allSeriesWithId.get(9));

		this.clusters.add(new Cluster(0, this.allSeriesWithId.get(7), c0Members));
		this.clusters.add(new Cluster(1, this.allSeriesWithId.get(8), c1Members));
		return this.clusters;
	}
	
	public List<SeriesWithID> getAllSeriesWithId(){
		return this.allSeriesWithId;
	}
	
}