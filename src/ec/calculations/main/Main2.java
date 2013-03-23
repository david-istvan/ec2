package ec.calculations.main;

import org.apache.commons.lang3.time.StopWatch;

import ec.calculations.evaluators.EpsilonPerturbation;
import ec.data.model.DataSet;
import ec.errors.TooLongSampleException;
import ec.test.mocks.KMedoidsMock;

public class Main2 {
	public static void main(String[] args) throws Exception {

		String dataSetName = "ItalyPowerDemand_TEST";
		String inputPath = "c:\\gaindata\\";
		
		int clusterNum = 2;

		StopWatch stopWatch = new StopWatch();
		stopWatch.start();
		System.out.println("K-medoids evaluation for data set " + dataSetName);

		// read data
		DataSet dataSet = new DataSet(inputPath + dataSetName);

		// KMedoids e = new KMedoids(dataSet, 24, clusterNum);
		KMedoidsMock e = new KMedoidsMock(dataSet, 24, clusterNum);

		try {
			EpsilonPerturbation ep = new EpsilonPerturbation(e.getClusters(), e.getAllSeriesWithId(), clusterNum);
			ep.evaluate();
		}
		catch (TooLongSampleException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		System.out.println("processing time: " + stopWatch.getTime() + " ms");
		stopWatch.stop();
	}
}