package buza.attribute.distance;

public class ManhattanDistance implements  ElementaryDistance  {
	public float calculateDistance(float x1, float x2) {
		return Math.abs(x1-x2);
	}
}
