package ec.utils;

import ec.data.model.SeriesDistance;

public class ArrayTools {
	public static int weight(int[] vector) {
		int weigth = 0;
		for (int i = 0; i < vector.length; i++) {
			if (vector[i] > 0) weigth++;
		}
		return weigth;
	}

	public static boolean contains(int[] vector, int value) {
		for (int i = 0; i < vector.length; i++) {
			if (vector[i] == value) return true;
		}
		return false;
	}

	public static int findFirstZeroIndex(int[] vector) {
		int i = 0;

		while (vector[i] != 0) {
			i++;
		}

		return i;
	}

	public static void mergeIn(float[] distances, float[] labels, float actualDistance, float actualLabel) {
		if (actualDistance < distances[distances.length - 1]) {
			int i = 0;
			for (i = 0; i < distances.length; i++) {
				if (actualDistance <= distances[i]) {
					break;
				}
			}

			float[] tmpDistances = new float[distances.length];
			float[] tmpLabels = new float[labels.length];

			int j;
			for (j = 0; j < i; j++) {
				tmpDistances[j] = distances[j];
				tmpLabels[j] = labels[j];
			}
			tmpDistances[i] = actualDistance;
			tmpLabels[i] = actualLabel;
			for (j = i + 1; j < distances.length; j++) {
				tmpDistances[j] = distances[j - 1];
				tmpLabels[j] = labels[j - 1];
			}

			for (int k = 0; k < distances.length; k++) {
				distances[k] = tmpDistances[k];
				labels[k] = tmpLabels[k];
			}
		}
	}

	public static void mergeIn(SeriesDistance[] distances, SeriesDistance actual) {
		// if(distances[0] == null){
		// distances[0] = actual;
		// return;
		// }
		if (actual.getDistance() < distances[distances.length - 1].getDistance()) {
			int i = 0;
			for (i = 0; i < distances.length; i++) {
				if (actual.getDistance() <= distances[i].getDistance()) {
					break;
				}
			}

			SeriesDistance[] tmp = new SeriesDistance[distances.length];

			int j;
			for (j = 0; j < i; j++) {
				tmp[j] = distances[j];
			}
			tmp[i] = actual;
			for (j = i + 1; j < distances.length; j++) {
				tmp[j] = distances[j - 1];
			}

			for (int k = 0; k < distances.length; k++) {
				distances[k] = tmp[k];
			}
		}
	}

	public static float maxOccurrence(SeriesDistance[] distances) {
		float[][] occurrences = new float[distances.length][2];
		for (int i = 0; i < distances.length; i++) {
			float label = distances[i].getLabel();

			boolean isNewLabel = true;

			for (int j = 0; j < occurrences.length; j++) {
				if (occurrences[j][0] == label) {
					occurrences[j][1]++;
					isNewLabel = false;
					break;
				}
			}
			if (isNewLabel) {
				for (int j = 0; j < occurrences.length; j++) {
					if (occurrences[j][0] == 0f) {
						occurrences[j][0] = label;
						occurrences[j][1]++;
						break;
					}
				}
			}
		}

		// select max
		float maxOccurrance = 0;
		float label = 0;
		for (int i = 0; i < occurrences.length; i++) {
			if (occurrences[i][1] > maxOccurrance) {
				maxOccurrance = occurrences[i][1];
				label = occurrences[i][0];
			}

		}

		return label;
	}
}