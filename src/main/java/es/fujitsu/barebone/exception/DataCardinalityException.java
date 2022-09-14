package es.fujitsu.barebone.exception;

public class DataCardinalityException extends FujitsuRuntimeException {

	public DataCardinalityException() {
		super();
	}

	public DataCardinalityException(String message) {
		super(message);
	}

	public DataCardinalityException(Throwable cause) {
		super(cause);
	}

	public DataCardinalityException(String message, Throwable cause) {
		super(message, cause);
	}

	// Constructors with args
	public DataCardinalityException(String[] args) {
		super(args);
	}

	public DataCardinalityException(String message, String[] args) {
		super(message, args);
	}

	public DataCardinalityException(String[] args, Throwable cause) {
		super(args, cause);
	}

	public DataCardinalityException(String message, String[] args, Throwable cause) {
		super(message, args, cause);
	}

	public DataCardinalityException(String message, Throwable cause, String[] args) {
		super(message, cause, args);
	}

}
