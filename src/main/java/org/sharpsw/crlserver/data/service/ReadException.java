package org.sharpsw.crlserver.data.service;

public class ReadException extends CRUDException {
	private static final long serialVersionUID = -622532094191787534L;

	public ReadException() {
	}

	public ReadException(String message) {
		super(message);
	}

	public ReadException(Throwable cause) {
		super(cause);
	}

	public ReadException(String message, Throwable cause) {
		super(message, cause);
	}

}
