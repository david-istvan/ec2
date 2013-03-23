package ec.calculations.evaluators;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import ec.calculations.interfaces.IEvaluator;
import ec.data.model.Cluster;
import ec.data.model.DistanceMatrix;
import ec.data.model.SeriesWithID;

public class EpsilonPerturbation implements IEvaluator {

	private List<Cluster> clusters;
	private List<Cluster> alternateClusters;
	private List<SeriesWithID> allSeriesWithId;
	private int clusterNum;
	
	public EpsilonPerturbation(List<Cluster> clusters, List<SeriesWithID> allSeriesWithId, int clusterNum) {
		this.clusters = clusters;
		this.alternateClusters = new ArrayList<Cluster>();
		this.allSeriesWithId = allSeriesWithId;
		this.clusterNum = clusterNum;
	}

	@Override
	public void evaluate() throws Exception {	
		for (final Cluster cluster : this.clusters) {
			Collections.sort(cluster.getMembers(), new DistanceComparator(cluster.getMedoid()));
		}
		
		System.out.println("original clusters:");
		for (Cluster cluster : this.clusters) {
			System.out.println("MEDOID: " + cluster.getMedoid().getId());
			System.out.print("\tMEMBERS: ");
			for (SeriesWithID member : cluster.getMembers()) {
				System.out.print(member.getId() + "(" + DistanceMatrix.getInstance().getDistance(cluster.getMedoid().getId(), member.getId()) + ")" + ", ");
			}
			System.out.println("");
		}
		
		System.out.println("-------");
		
		
		for (Cluster cluster : this.clusters) {
			for (int i=0; i<cluster.getMembers().size(); i++){
				SeriesWithID alternateMedoid = cluster.getMembers().get(i);
				
				this.alternateClusters = new ArrayList<Cluster>();
				
				for (Cluster curCluster : this.clusters) {
					if (curCluster.getId() != cluster.getId()) {
						this.alternateClusters.add(new Cluster(curCluster.getId(), curCluster.getMedoid()));
					}
				}
				
				this.alternateClusters.add(new Cluster(cluster.getId(), alternateMedoid));
				
				this.assignPointsToClosestMedoid(this.alternateClusters);
				
				for (final Cluster c : this.alternateClusters) {
					Collections.sort(c.getMembers(), new DistanceComparator(c.getMedoid()));
				}
				
				System.out.println("actual clusters:");
				for (Cluster c : this.alternateClusters) {
					System.out.println("MEDOID: " + c.getMedoid().getId());
					System.out.print("\tMEMBERS: ");
					for (SeriesWithID member : c.getMembers()) {
						System.out.print(member.getId() + "(" + DistanceMatrix.getInstance().getDistance(c.getMedoid().getId(), member.getId()) + ")" + ", ");
					}
					System.out.println("");
				}
				
				//System.out.println("Alternate cluster identical with the original? " + this.identicalClusters(this.clusters, this.alternateClusters));
			}
			
			this.alternateClusters = new ArrayList<Cluster>();
			this.alternateClusters.add(this.clusters.get(1));
			this.alternateClusters.add(this.clusters.get(0));
			
			
			System.out.println("Alternate cluster identical with the original? " + this.identicalClusters(this.clusters, this.alternateClusters));
		}
		
		for(int i = 0; i<DistanceMatrix.getInstance().getDistances()[0].length; i++){
			for (int j = i+1; j<DistanceMatrix.getInstance().getDistances()[0].length; j++){
				System.out.println("(" + i + ", " + j + ")" + DistanceMatrix.getInstance().getDistance(i, j));
			}
		}
		
		/*for (int i=0; i<cluster.getMembers().size(); i++){
			SeriesWithID alternateMedoid = cluster.getMembers().get(i);
			this.alternateClusters = new ArrayList<Cluster>();

			for (Cluster curCluster : this.clusters) {
				if (curCluster.getId() != cluster.getId()) {
					this.alternateClusters.add(new Cluster(curCluster.getId(), curCluster.getMedoid()));
				}
			}

			this.alternateClusters.add(new Cluster(cluster.getId(), alternateMedoid));

			this.assignPointsToClosestMedoid(this.alternateClusters);

			if (alternateError < minAlternateError) {
				minAlternateError = alternateError;
				bestAlternateClusters = this.alternateClusters;
			}
		}*/
	}
	
	private boolean isMedoid(SeriesWithID series, List<Cluster> clusters) {
		for (Cluster cluster : clusters) {
			if (cluster.getMedoid().getId() == series.getId()) return true;
		}
		return false;
	}
	
	private void assignPointsToClosestMedoid(List<Cluster> clusters) throws Exception {
		for (SeriesWithID series : this.allSeriesWithId) {
			if (!isMedoid(series, clusters)) {
				float minDistance = Float.MAX_VALUE;
				int bestClusterId = 0;

				for (Cluster cluster : clusters) {
					float actualDistance = DistanceMatrix.getInstance().getDistance(series.getId(), cluster.getMedoid().getId());
					if (actualDistance < minDistance) {
						minDistance = actualDistance;
						bestClusterId = cluster.getId();
					}
				}

				// add to cluster
				for (Cluster cluster : clusters) {
					if (cluster.getId() == bestClusterId) cluster.addMember(series);
				}
			}
		}
	}
	
	//TODO: EZ NEM JÓ!
	private boolean identicalClusters(List<Cluster> clustering1, List<Cluster> clustering2){
		if(clustering1.size() != clustering2.size()) return false;
		
		for(int i = 0; i<clustering1.size(); i++){
			if(clustering1.get(i).getMembers().size() != clustering2.get(i).getMembers().size()) return false;
		}
		
		int[][] cluster1Elements = new int[this.clusterNum][];
		int[][] cluster2Elements = new int[this.clusterNum][];
		
		for(int i = 0; i<clustering1.size(); i++){
			cluster1Elements[i] = new int[clustering1.get(i).getAllMemberIDs().size()];
			for(int j = 0; j<clustering1.get(i).getAllMemberIDs().size(); j++){
				cluster1Elements[i][j] = clustering1.get(i).getAllMemberIDs().get(j);
			}
		}
		
		for(int i = 0; i<clustering2.size(); i++){
			cluster2Elements[i] = new int[clustering2.get(i).getAllMemberIDs().size()];
			for(int j = 0; j<clustering2.get(i).getAllMemberIDs().size(); j++){
				cluster2Elements[i][j] = clustering2.get(i).getAllMemberIDs().get(j);
			}
		}
		
		//System.out.println("c1e length:" + cluster1Elements[0].length);
		
		try{
			for(int i = 0; i<cluster1Elements.length; i++){
				for(int j = 0; j<cluster1Elements[i].length; j++){
					if(cluster1Elements[i][j] != cluster1Elements[i][j]) return false;
				}
			}
		}
		catch (Exception e) {
			return false;
		}
		
		return true;
	}
	
}