package ec.data.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import ec.calculations.evaluators.DistanceComparator;

public class Cluster {
	private int id;
	private SeriesWithID medoid;
	private List<SeriesWithID> members;
	private float error;
	private DistanceMatrix distanceMatrix;

	public Cluster(int id, SeriesWithID medoid, List<SeriesWithID> members) throws Exception {
		this.id = id;
		this.medoid = medoid;
		this.members = members;
		this.distanceMatrix = DistanceMatrix.getInstance();
	}

	public Cluster(int id, SeriesWithID medoid) throws Exception {
		this.id = id;
		this.medoid = medoid;
		this.members = new ArrayList<SeriesWithID>();
		this.distanceMatrix = DistanceMatrix.getInstance();
	}

	public int getId() {
		return id;
	}

	public SeriesWithID getMedoid() {
		return medoid;
	}

	public void setMedoid(SeriesWithID medoid) {
		this.medoid = medoid;
	}

	public List<SeriesWithID> getMembers() {
		return members;
	}

	public void addMember(SeriesWithID member) {
		this.members.add(member);
	}

	public float getError() {
		return this.error;
	}

	public void calulateError() {
		float cost = 0;
		for (SeriesWithID member : this.members) {
			cost += java.lang.Math.pow((double) (this.distanceMatrix.getDistance(member.getId(), this.medoid.getId())), 2);
		}
		this.error = (float) java.lang.Math.sqrt(cost / (this.members.size()));
	}
	
	public List<Integer> getAllMemberIDs(){
		List<Integer> getAllMemberIDs = new ArrayList<Integer>();
		getAllMemberIDs.add(this.medoid.getId());
		
		for (SeriesWithID member : this.members) {
			getAllMemberIDs.add(member.getId());
		}
		
		Collections.sort(getAllMemberIDs);
		
		return getAllMemberIDs;
	}
}