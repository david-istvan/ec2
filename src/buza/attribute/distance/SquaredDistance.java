package buza.attribute.distance;

public class SquaredDistance implements ElementaryDistance {
	public float calculateDistance(float x1, float x2) {
		return (x1-x2)*(x1-x2);
	}
}
