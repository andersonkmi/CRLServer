package org.sharpsw.crlserver.data.service;

public class CreateException extends CRUDException {
	private static final long serialVersionUID = 2313749282287583261L;

	public CreateException() {
	}

	public CreateException(String message) {
		super(message);
	}

	public CreateException(Throwable cause) {
		super(cause);
	}

	public CreateException(String message, Throwable cause) {
		super(message, cause);
	}

}
