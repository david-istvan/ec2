package ec.calculations.evaluators;

import java.util.Comparator;

import ec.data.model.DistanceMatrix;
import ec.data.model.SeriesWithID;

public class DistanceComparator implements Comparator<SeriesWithID>{

	private SeriesWithID medoid;
	
	public DistanceComparator(SeriesWithID medoid) {
		this.medoid = medoid;
	}

	@Override
	public int compare(SeriesWithID s1, SeriesWithID s2) {
		try {
			if (DistanceMatrix.getInstance().getDistance(s1.getId(), this.medoid.getId()) < DistanceMatrix.getInstance().getDistance(s2.getId(), this.medoid.getId())) return -1;
			else return 1;
		}
		catch (Exception e) {
			e.printStackTrace();
			return 0;
		}
	}
}
