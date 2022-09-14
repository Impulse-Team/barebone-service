package es.fujitsu.barebone.exception;

import es.fujitsu.barebone.utils.ExceptionUtils;

public class FujitsuException extends Exception {

	private static final long serialVersionUID = 4582447316194662740L;

	private final String[] args;

	public boolean isPrintStacktrace() {
		return false;
	}

	// Default constructors without args
	public FujitsuException() {
		super();
		this.args = new String[] {};
	}

	public FujitsuException(String message) {
		super(ExceptionUtils.processMessage(message));
		this.args = new String[] {};
	}

	public FujitsuException(Throwable cause) {
		super(cause);
		this.args = new String[] {};
	}

	public FujitsuException(String message, Throwable cause) {
		super(ExceptionUtils.processMessage(message), cause);
		this.args = new String[] {};
	}

	// Constructors with args
	public FujitsuException(String[] args) {
		super();
		this.args = args;
	}

	public FujitsuException(String message, String[] args) {
		super(ExceptionUtils.processMessage(message, args));
		this.args = args;
	}

	public FujitsuException(String[] args, Throwable cause) {
		super(cause);
		this.args = args;
	}

	public FujitsuException(String message, String[] args, Throwable cause) {
		super(ExceptionUtils.processMessage(message, args), cause);
		this.args = args;
	}

	public FujitsuException(String message, Throwable cause, String[] args) {
		super(ExceptionUtils.processMessage(message, args), cause);
		this.args = args;
	}

}
