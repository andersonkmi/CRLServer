package org.sharpsw.crlserver.data.service;

public class UpdateException extends CRUDException {
	private static final long serialVersionUID = -1008252300357039046L;

	public UpdateException() {
	}

	public UpdateException(String message) {
		super(message);
	}

	public UpdateException(Throwable cause) {
		super(cause);
	}

	public UpdateException(String message, Throwable cause) {
		super(message, cause);
	}

}
