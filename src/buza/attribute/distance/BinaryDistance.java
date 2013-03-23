package buza.attribute.distance;

public class BinaryDistance implements  ElementaryDistance  {
	public float calculateDistance(float x1, float x2) {
		return x1==x2 ? 0 : 1;
	}
}
