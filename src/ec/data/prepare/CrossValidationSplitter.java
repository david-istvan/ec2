package ec.data.prepare;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.apache.commons.lang3.ArrayUtils;

import ec.data.model.FileInfo;

/**
 * FIXME: valami�rt rosszul splitteli az adathalmazt. adatot nem vesz�t el, csak
 * 1-1 elem valami�rt rossz subsetbe ker�l (val�sz�n�leg a v�g�n a floor miatt)
 */
public class CrossValidationSplitter {
	private int folds;
	private int[][] splits;
	private int splitSize;
	private FileInfo fileInfo;

	public CrossValidationSplitter(FileInfo fileInfo, int folds) {
		this.folds = folds;
		this.fileInfo = fileInfo;
		this.splitSize = (int) Math.floor(fileInfo.getLineCount() / folds) + 1;
		this.splits = new int[folds][0];
	}

	public int[][] getSplits() {
		Random random = new Random();

		List<Integer> availableSplits = new ArrayList<Integer>();
		for (int i = 0; i < folds; i++) {
			availableSplits.add(i);
		}

		for (int i = 0; i < fileInfo.getLineCount(); i++) {
			int splitID = availableSplits.get(random.nextInt(availableSplits.size()));
			this.splits[splitID] = ArrayUtils.addAll(this.splits[splitID], i);

			if (i < fileInfo.getLineCount() && this.splits[splitID].length == splitSize) {
				availableSplits.remove((Object) splitID);
			}

		}
		return this.splits;
	}
}