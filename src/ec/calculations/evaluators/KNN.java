package ec.calculations.evaluators;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import buza.attribute.distance.ManhattanDistance;
import buza.timeseries.distance.DTW;
import ec.calculations.interfaces.IEvaluator;
import ec.data.model.DataSet;
import ec.data.model.SeriesDistance;
import ec.data.model.SeriesWithLabel;
import ec.errors.TooLongSampleException;
import ec.utils.ArrayTools;
import ec.utils.FileIO;

/**
 * Evaluator for the early classification metrics
 * 
 * @author I. Dávid
 * @since 2012.03.11.
 */
public class KNN implements IEvaluator {
	private DataSet dataSet;
	private int[] trainingData;
	private int[] testData;
	private List<SeriesWithLabel> trainingSeries;
	private List<SeriesWithLabel> testSeries;
	private int sampleLength;
	private int folds;
	private List<Integer> correctLabels;
	private int currentFold = 0;
	private DTW distance;
	private FileIO io;

	public KNN(DataSet dataSet, int folds) {
		this.dataSet = dataSet;
		this.distance = new DTW(0, new ManhattanDistance());
		this.trainingSeries = new ArrayList<SeriesWithLabel>();
		this.testSeries = new ArrayList<SeriesWithLabel>();
		this.correctLabels = new ArrayList<Integer>();
		this.folds = folds;
		this.io = new FileIO();
	}

	@Override
	public void evaluate() throws TooLongSampleException {
		try {
			if (this.sampleLength > this.dataSet.getFileInfo().getSeriesLength()) {
				throw new TooLongSampleException(this.dataSet.getFileInfo().getSeriesLength(), this.sampleLength);
			}

			currentFold++;

			this.trainingSeries = dataSet.readLines(this.trainingData, this.sampleLength);
			this.testSeries = dataSet.readLines(this.testData, this.sampleLength);

			System.out.println("calculating for fold #" + (this.currentFold - 1));
			calculateKNN(1);

			if (this.currentFold == this.folds) {
				double correct = average(correctLabels);
				double variance = variance(correct, correctLabels);
				this.io.flushResult(this.sampleLength, correct, variance, dataSet.getFileInfo().getFileName() + ".tmp");
				this.currentFold = 0;
			}
		}
		catch (IOException e) {
			e.printStackTrace();
		}
	}

	private double average(List<Integer> intList) {
		double sum = 0;
		for (Integer i : intList) {
			sum += i;
		}
		return sum / intList.size();
	}

	private double variance(double mean, List<Integer> intList) {
		double sum = 0;
		for (Integer i : intList) {
			sum += ((mean - i) * (mean - i));
		}
		return Math.sqrt(sum / intList.size());
	}

	// 1-NN
	@SuppressWarnings("unused")
	@Deprecated
	private void calculate1NN() {
		int hitCount = 0;
		int index = 0;
		for (SeriesWithLabel testSample : this.testSeries) {
			float minDistance = Float.MAX_VALUE;
			float pLabel = 0;
			float actualDistance = 0;
			for (SeriesWithLabel trainingSample : this.trainingSeries) {
				actualDistance = distance.calculateDistance(testSample.getSeries(), trainingSample.getSeries());
				if (actualDistance < minDistance) {
					minDistance = actualDistance;
					pLabel = trainingSample.getLabel();
				}
			}
			if (testSample.getLabel() == pLabel) {
				hitCount++;
			}
			index++;
		}
		this.correctLabels.add(hitCount);
	}

	// k-NN
	// use only with odd number of folds, since even fold number can cause
	// indeterminate results
	private void calculateKNN(int k) {
		int hitCount = 0;
		@SuppressWarnings("unused")
		int index = 0;
		for (SeriesWithLabel testSample : this.testSeries) {
			SeriesDistance[] distances = new SeriesDistance[k];
			for (int i = 0; i < k; i++) {
				distances[i] = new SeriesDistance(Float.MAX_VALUE, 0f);
			}
			float actualDistance = 0;
			float actualLabel = 0;

			for (SeriesWithLabel trainingSample : this.trainingSeries) {
				actualDistance = distance.calculateDistance(testSample.getSeries(), trainingSample.getSeries());
				actualLabel = trainingSample.getLabel();

				ArrayTools.mergeIn(distances, new SeriesDistance(actualDistance, actualLabel));
			}
			if (testSample.getLabel() == ArrayTools.maxOccurrence(distances)) {
				hitCount++;
			}
			index++;
		}
		this.correctLabels.add(hitCount);
	}

	public int[] getTestData() {
		return testData;
	}

	public void setTestData(int[] testData) {
		this.testData = testData;
	}

	public int[] getTrainingData() {
		return trainingData;
	}

	public void setTrainingData(int[] trainingData) {
		this.trainingData = trainingData;
	}

	public int getSampleLength() {
		return sampleLength;
	}

	public void setSampleLength(int sampleLength) {
		this.sampleLength = sampleLength;
	}
}