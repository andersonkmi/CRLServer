package org.sharpsw.crlserver.data.service;

public class DeleteException extends CRUDException {
	private static final long serialVersionUID = -348249315660164650L;

	public DeleteException() {
	}

	public DeleteException(String message) {
		super(message);
	}

	public DeleteException(Throwable cause) {
		super(cause);
	}

	public DeleteException(String message, Throwable cause) {
		super(message, cause);
	}

}
