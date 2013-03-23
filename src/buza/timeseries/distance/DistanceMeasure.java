package buza.timeseries.distance;

public interface DistanceMeasure {
	
	/** In many cases if the distance is above a given threshold, the actual value of the 
	 * distance is not interesting anymore (e.g. nearest neighbor classification 
	 * and we already found some neighbors that are closer then D). 
	 * If lower bounding is allowed, this function determines what should be the maximal 
	 * relevant value. If during the calculations we realize that the actual value of 
	 * the distance is higher than relevant_value, then the calculation is interrupted
	 * (in order to save runtime). 
	 * @param relevant_value
	 */
	public void setMaximalRelevantValue(float relevant_value);
	
	/** See setMaximalRelevantValue() */
	public void allowLowerBounding(boolean allow_lower_bounding);
	
	public float calculateDistance(float[] time_series1, float[] time_series2);
	
	/** Returns if the last call of calculateDistance returned the actual value of the distance
	 * (isLowerBounded()="false") or the calculations were interrupted because of lower bounding (isLowerBounded()="true"). 
	 * */ 
	public boolean isLowerBounded();
}
