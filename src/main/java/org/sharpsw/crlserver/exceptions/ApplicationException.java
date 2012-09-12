package org.sharpsw.crlserver.exceptions;

public class ApplicationException extends Exception {
	private static final long serialVersionUID = 2054270511259074311L;

	public ApplicationException() {
	}

	public ApplicationException(String message) {
		super(message);
	}

	public ApplicationException(Throwable cause) {
		super(cause);
	}

	public ApplicationException(String message, Throwable cause) {
		super(message, cause);
	}

}
