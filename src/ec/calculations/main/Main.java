package ec.calculations.main;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.time.StopWatch;

import ec.calculations.evaluators.KNN;
import ec.calculations.evaluators.RelativeAccuracy;
import ec.data.model.ConfigEntry;
import ec.data.model.DataSet;
import ec.data.model.FileInfo;
import ec.data.prepare.CrossValidationSplitter;
import ec.errors.TooLongSampleException;
import ec.utils.Config;

public class Main {
	public static void main(String[] args) throws TooLongSampleException {
		// cross-validation fold number
		int folds = 10;

		Config config = Config.getInstance();

		//for (String dataSetName : config.getDataSetNames()) {
		for (ConfigEntry ce : config.getInputDataSet()) {
			StopWatch stopWatch = new StopWatch();
			stopWatch.start();
			System.out.println("K-NN evaluation for data set " + ce.getDataSetName());

			// read data
			DataSet dataSet = new DataSet(config.getInputPath() + ce.getDataSetName());
			FileInfo inputInfo = dataSet.getFileInfo();

			// System.out.println("lines: " + inputInfo.getLineCount());

			CrossValidationSplitter cvs = new CrossValidationSplitter(inputInfo, folds);

			int[][] splits = cvs.getSplits();

			KNN e = new KNN(dataSet, folds);

			System.out.println("serieslength, folds: " + inputInfo.getSeriesLength() + "," + folds);
			for (int i = 1; i <= inputInfo.getSeriesLength(); i++) {
				for (int j = 0; j < folds; j++) {
					int[] testData = splits[j];
					int[] trainingData = new int[0];
					for (int k = 0; k < folds; k++) {
						if (k != j) {
							trainingData = ArrayUtils.addAll(trainingData, splits[k]);
						}
					}

					e.setTrainingData(trainingData);
					e.setTestData(testData);
					e.setSampleLength(i);

					System.out.println("evaluating for (" + i + "," + j + ")");
					e.evaluate();
				}
			}

			RelativeAccuracy ra = new RelativeAccuracy();
			ra.calculateAccuracy(config.getOutputPath(), ce.getDataSetName() + ".tmp");
			System.out.println("processing time: " + stopWatch.getTime() + " ms");
			stopWatch.stop();
		}
	}
}
