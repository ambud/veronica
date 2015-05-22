package org.veronica.core;

public class VException extends Exception {

	private static final long serialVersionUID = 1L;

	public VException() {
		super();
	}

	public VException(String message, Throwable cause,
			boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

	public VException(String message, Throwable cause) {
		super(message, cause);
	}

	public VException(String message) {
		super(message);
	}

	public VException(Throwable cause) {
		super(cause);
	}

}
