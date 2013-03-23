package ec.calculations.interfaces;

import ec.errors.TooLongSampleException;

public interface IEvaluator {
	public void evaluate() throws TooLongSampleException, Exception;
}