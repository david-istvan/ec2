package ec.calculations.evaluators;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import ec.calculations.interfaces.IEvaluator;
import ec.data.model.Cluster;
import ec.data.model.DataSet;
import ec.data.model.DistanceMatrix;
import ec.data.model.SeriesWithID;
import ec.errors.TooLongSampleException;

public class KMedoids implements IEvaluator {
	private DataSet dataSet;
	private int sampleLength;
	private int clusterNum;
	private List<SeriesWithID> allSeriesWithId;
	private int[] currentMedoidIDs;
	private List<Cluster> currentClusters;
	private float currentError;
	private List<Cluster> alternateClusters;
	private DistanceMatrix distanceMatrix;

	public KMedoids(DataSet dataSet, int sampleLength, int clusterNum) {
		this.dataSet = dataSet;
		this.sampleLength = sampleLength;
		this.clusterNum = clusterNum;
		this.currentError = 0;
		this.currentMedoidIDs = new int[clusterNum];
		this.currentClusters = new ArrayList<Cluster>();
		this.allSeriesWithId = new ArrayList<SeriesWithID>();
		this.alternateClusters = new ArrayList<Cluster>();
	}

	// ezt használja majd az EpsilonPerturbation
	public List<Cluster> getClusters() throws TooLongSampleException {
		this.evaluate();
		return this.currentClusters;
	}
	
	public List<SeriesWithID> getAllSeriesWithId(){
		return this.allSeriesWithId;
	}

	@Override
	public void evaluate() throws TooLongSampleException {
		if (this.sampleLength > this.dataSet.getFileInfo().getSeriesLength()) {
			throw new TooLongSampleException(this.dataSet.getFileInfo().getSeriesLength(), this.sampleLength);
		}
		try {
			this.initializeDistances();
			this.initalizeClusters();
			this.currentError = this.assignPointsToClosestMedoid(this.currentClusters);
			this.log();
			this.swap();

		}
		catch (IOException e) {
			e.printStackTrace();
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void initializeDistances() throws IOException {
		// all series with an additional ID
		for (int i = 0; i < this.dataSet.getFileInfo().getLineCount(); i++) {
			int[] seriesid = { i };
			this.allSeriesWithId.add(new SeriesWithID(i, this.dataSet.readLines2(seriesid, this.sampleLength).get(0).getSeries()));
		}
		this.distanceMatrix = DistanceMatrix.getInstance(this.allSeriesWithId);
	}

	private void initalizeClusters() throws Exception {
		// random selected initial medoids
		Random random = new Random();

		List<Integer> availableMedoidIDs = new ArrayList<Integer>();
		for (int i = 0; i < this.dataSet.getFileInfo().getLineCount(); i++) {
			availableMedoidIDs.add(i);
		}

		for (int i = 0; i < this.clusterNum; i++) {
			int medoidID = availableMedoidIDs.get(random.nextInt(availableMedoidIDs.size()));
			System.out.println(medoidID);
			this.currentMedoidIDs[i] = medoidID;

			availableMedoidIDs.remove((Object) medoidID);
		}

		// for testing purposes
		// this.currentMedoidIDs[0] = 6;
		// this.currentMedoidIDs[1] = 4;
		// this.currentMedoidIDs[2] = 2;

		// create clusters - no series yet, only medoids
		for (int i = 0; i < this.currentMedoidIDs.length; i++) {
			this.currentClusters.add(new Cluster(i, this.allSeriesWithId.get(this.currentMedoidIDs[i])));
		}
	}

	private float assignPointsToClosestMedoid(List<Cluster> clusters) throws IOException {
		float cost = 0;
		for (SeriesWithID series : this.allSeriesWithId) {
			if (!isMedoid(series, clusters)) {
				float minDistance = Float.MAX_VALUE;
				int bestClusterId = 0;

				for (Cluster cluster : clusters) {
					float actualDistance = this.distanceMatrix.getDistance(series.getId(), cluster.getMedoid().getId());
					if (actualDistance < minDistance) {
						minDistance = actualDistance;
						bestClusterId = cluster.getId();
					}
				}

				// add to cluster
				for (Cluster cluster : clusters) {
					if (cluster.getId() == bestClusterId) cluster.addMember(series);
				}

				// increment cost
				cost += minDistance * minDistance;
			}
		}

		float error = (float) java.lang.Math.sqrt(cost / (this.allSeriesWithId.size() - clusterNum));
		for (Cluster cluster : clusters) {
			cluster.calulateError();
		}

		return error;
	}

	private float calculateSwapCost() throws Exception {
		float minAlternateError = Float.MAX_VALUE;
		float alternateError = 0;
		List<Cluster> bestAlternateClusters = new ArrayList<Cluster>();

		for (Cluster cluster : this.currentClusters) {
			for (SeriesWithID alternateMedoid : cluster.getMembers()) {
				this.alternateClusters = new ArrayList<Cluster>();

				for (Cluster curCluster : this.currentClusters) {
					if (curCluster.getId() != cluster.getId()) {
						this.alternateClusters.add(new Cluster(curCluster.getId(), curCluster.getMedoid()));
					}
				}

				this.alternateClusters.add(new Cluster(cluster.getId(), alternateMedoid));

				alternateError = this.assignPointsToClosestMedoid(this.alternateClusters);

				if (alternateError < minAlternateError) {
					minAlternateError = alternateError;
					bestAlternateClusters = this.alternateClusters;
				}
			}
		}

		this.alternateClusters = bestAlternateClusters;
		return minAlternateError;
	}

	private void swap() throws Exception {
		while (calculateSwapCost() < this.currentError) {
			float minAlternateError = calculateSwapCost();

			this.currentClusters = this.alternateClusters;
			this.currentError = minAlternateError;

			this.alternateClusters = new ArrayList<Cluster>();

			this.log();
		}
	}

	private boolean isMedoid(SeriesWithID series, List<Cluster> clusters) {
		for (Cluster cluster : clusters) {
			if (cluster.getMedoid().getId() == series.getId()) return true;
		}
		return false;
	}

	private void log() {
		System.out.println("LIST CLUSTERS");
		for (Cluster cluster : this.currentClusters) {
			System.out.println("Cluster #" + cluster.getId());
			System.out.print("\tMEDOID: " + (int) cluster.getMedoid().getId() + " MEMBERS: ");
			for (SeriesWithID member : cluster.getMembers()) {
				System.out.print((int) member.getId() + ", ");
			}
			System.out.print("\n");
			System.out.println("Error: " + cluster.getError());
			System.out.println("\n");
		}
		/*
		 * System.out.println("DISTANCES:"); for (SeriesWithID series :
		 * this.allSeriesWithId) { if(!isMedoid(series, this.currentClusters)){
		 * System.out.print("Series #" + (int)series.getId() +
		 * " distances from medoids... "); for (Cluster cluster :
		 * this.currentClusters) {
		 * System.out.print((int)cluster.getMedoid().getId() + ": " +
		 * distance.calculateDistance(series.getSeries(),
		 * cluster.getMedoid().getSeries()) + ", "); } System.out.print("\n"); }
		 * }
		 */
		System.out.println("Current error: " + this.currentError);
	}
}