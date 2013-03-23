package ec.calculations.evaluators;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

import ec.data.model.ResultRow;
import ec.utils.FileIO;

public class RelativeAccuracy {

	public void calculateAccuracy(String path, String fileName) {
		List<ResultRow> result = new ArrayList<ResultRow>();

		try {
			System.out.println("Calculating relative accuracy.");
			String line = "";
			FileReader fr = new FileReader(path + fileName);
			BufferedReader br = new BufferedReader(fr);

			while ((line = br.readLine()) != null) {
				if (Character.isDigit(line.charAt(0))) {
					String[] rawData = line.trim().split(",");
					result.add(new ResultRow(Integer.parseInt(rawData[0].trim()), Double.parseDouble(rawData[1].trim()), Double
							.parseDouble(rawData[2].trim())));
				}
			}

			double maxAccuracy = result.get(result.size() - 1).getCorrectLabels();
			for (ResultRow resultRow : result) {
				resultRow.setRelativeAccuracy(resultRow.getCorrectLabels() / maxAccuracy);
			}

			FileIO io = new FileIO();
			// FIXME: quick 'n dirty
			if (path.contains("/")) {
				// LINUX FS
				io.flushResult(result, ("final/" + fileName).replace(".tmp", "-results.csv"));
			}
			else {
				// WINDOWS FS
				io.flushResult(result, ("final\\" + fileName).replace(".tmp", "-results.csv"));
			}

		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
}