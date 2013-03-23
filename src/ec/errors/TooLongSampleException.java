package ec.errors;

public class TooLongSampleException extends Exception {

	private static final long serialVersionUID = 1L;
	private String message;

	public TooLongSampleException() {
		this.message = "Series too long!";
	}

	public TooLongSampleException(int maxLength) {
		this.message = "Sample too long. The maximum length permitted is " + maxLength + "!";
	}

	public TooLongSampleException(int maxLength, int sampleLength) {
		this.message = "Sample too long. (" + sampleLength + ") The maximum length permitted is " + maxLength + "!";
	}

	@Override
	public String getMessage() {
		return this.message;
	}
}