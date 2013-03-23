// This is a DTW for comparision of time series with different length

package buza.timeseries.distance;

import buza.attribute.distance.ElementaryDistance;



public class DTW implements DistanceMeasure {
	
	private float wrapping_cost = 0;
	private ElementaryDistance d = null;
	private float constraint = 1;
	private boolean lowerBounded = false, allowLowerBounded = false;
	private float maximal_relevant_value = Float.MAX_VALUE;
	
	public void allowLowerBounding(boolean b) {
		allowLowerBounded = b;
	}
	
	public DTW(float wrapping_cost, ElementaryDistance d) {
		this.wrapping_cost = wrapping_cost;
		this.d = d;
	}
	
	public boolean isLowerBounded() {
		return lowerBounded;
	}
	
	public void setConstraint(float c) {
		constraint = c;
	}
	
	public void setMaximalRelevantValue(float maximal_relevant_value) {
		this.maximal_relevant_value = maximal_relevant_value;
	}
	
	public float calculateDistance(float[] time_series1, float[] time_series2) {
		
		lowerBounded = false;
		
		if (time_series1.length==0) return Float.MAX_VALUE;
		if (time_series2.length==0) return Float.MAX_VALUE;
		
		
		//if (time_series1.getNumColumns()!=time_series2.getNumColumns()) return -1;
		
		int n1 = time_series1.length; // ez van vizszintesen a matrix "felett"
		int n2 = time_series2.length; // ez van fuggolegesen a matrix "mellett"

		
		int c_range = (int) Math.floor(constraint*n2);
		
		// LB_Keogh
		if (allowLowerBounded) {
			float lb_keogh = 0;
			for (int i=0;i<time_series2.length;i++) {
				
				int first = Math.max(i-c_range,0);
				int last  = Math.min(i+c_range+1, time_series1.length);
				float u_i = buza.arraytools.ArrayOperations.max(time_series1, first, last);
				float l_i = buza.arraytools.ArrayOperations.min(time_series1, first, last);
				if (time_series2[i] > u_i) lb_keogh += d.calculateDistance(time_series2[i],u_i);
				if (time_series2[i] < l_i) lb_keogh += d.calculateDistance(time_series2[i],l_i);
			}
			if (lb_keogh > maximal_relevant_value) {
				lowerBounded = true;
				return lb_keogh;
			}	
		}
		
		
		float[] first_column = new float[n2];
		float[] second_column = new float[n2];
		boolean[] valid_positions_first = new boolean[n2];
		boolean[] valid_positions_second = new boolean[n2];
		
		first_column[0] = d.calculateDistance(time_series1[0], time_series2[0]);
		valid_positions_first[0] = true;
		
		for (int j=1;j<n2;j++) {
			first_column[j] = first_column[j-1]+wrapping_cost+d.calculateDistance(time_series1[0], time_series2[j]);
			valid_positions_first[j] = true;
		}
		
		// i = position in time_series1
		// j = position in time_series2
		
		// Constraint egy oldali szelessege
	
		
		for (int i=1;i<n1;i++) {
			
			boolean irrelevant = true;
			
			for (int j=0;j<n2;j++) valid_positions_second[j] = false;
			
			for (int j=Math.max(0,(n2*i)/n1-c_range);j<=Math.min(n2-1, (n2*i)/n1+c_range);j++) {
				
				
				
				if (j==0) {
					if (valid_positions_first[0]) {
						second_column[0] = first_column[0]+wrapping_cost+
							d.calculateDistance(time_series1[i], time_series2[0]);
						valid_positions_second[0] = true;
						if (second_column[0] <= maximal_relevant_value) irrelevant = false;
					}
				} else {
					
					
					second_column[j] = Math.min(valid_positions_second[j-1] ? (second_column[j-1]+wrapping_cost+d.calculateDistance(time_series1[i], time_series2[j])) : Float.MAX_VALUE,
						Math.min(valid_positions_first[j-1] ? first_column[j-1]+d.calculateDistance(time_series1[i], time_series2[j]) : Float.MAX_VALUE,
								 valid_positions_first[j] ? first_column[j]+wrapping_cost+d.calculateDistance(time_series1[i], time_series2[j]) : Float.MAX_VALUE
						));
					
					valid_positions_second[j] = true;
				
					if (second_column[j] <= maximal_relevant_value) irrelevant = false;
				}
			}
			if ( (irrelevant) && (allowLowerBounded) ) {
				lowerBounded = true;
				float min_in_second_col = Float.MAX_VALUE;
				for (int j=Math.max(0,(n2*i)/n1-c_range);j<=Math.min(n2-1, (n2*i)/n1+c_range);j++) 
					if (second_column[j] < min_in_second_col)
						min_in_second_col = second_column[j];
				return min_in_second_col;
//				return Float.MAX_VALUE;
			}
			first_column = second_column;
			valid_positions_first = valid_positions_second;
			
			second_column = new float[n2];
			valid_positions_second = new boolean[n2];
		}
		
		return first_column[n2-1];
	}
}
