package org.sharpsw.crlserver.persistence.utils;

public class SessionFactoryConfigurationException extends Exception {
	private static final long serialVersionUID = 150205097511584478L;

	public SessionFactoryConfigurationException() {
	}

	public SessionFactoryConfigurationException(String message) {
		super(message);
	}

	public SessionFactoryConfigurationException(Throwable cause) {
		super(cause);
	}

	public SessionFactoryConfigurationException(String message, Throwable cause) {
		super(message, cause);
	}

}
