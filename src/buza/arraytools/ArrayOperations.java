package buza.arraytools;

public class ArrayOperations {
	public static float min(float[] array, boolean[] mask) {
		float minimalValue = Float.MAX_VALUE;
		for (int i = 0; i < array.length; i++)
			if (mask[i] && array[i] < minimalValue)
				minimalValue = array[i];
		return minimalValue;
	}

	public static float min(float[] array) {
		float minimalValue = Float.MAX_VALUE;
		for (int i = 0; i < array.length; i++)
			if (array[i] < minimalValue)
				minimalValue = array[i];
		return minimalValue;
	}

	/** first_pos included, last_pos NOT included */
	public static float min(float[] array, int first_pos, int last_pos) {
		float minimalValue = Float.MAX_VALUE;
		for (int i = first_pos; i < last_pos; i++)
			if (array[i] < minimalValue)
				minimalValue = array[i];
		return minimalValue;
	}

	/** first_pos included, last_pos NOT included */
	public static float max(float[] array, int first_pos, int last_pos) {
		float maxValue = -Float.MAX_VALUE;
		for (int i = first_pos; i < last_pos; i++)
			if (array[i] > maxValue)
				maxValue = array[i];
		return maxValue;
	}
}