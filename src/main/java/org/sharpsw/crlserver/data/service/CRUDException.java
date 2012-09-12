package org.sharpsw.crlserver.data.service;

public class CRUDException extends Exception {
	private static final long serialVersionUID = 372513163356356592L;

	public CRUDException() {
	}

	public CRUDException(String message) {
		super(message);
	}

	public CRUDException(Throwable cause) {
		super(cause);
	}

	public CRUDException(String message, Throwable cause) {
		super(message, cause);
	}

}
