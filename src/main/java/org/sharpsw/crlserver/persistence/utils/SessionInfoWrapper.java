package org.sharpsw.crlserver.persistence.utils;

import org.hibernate.Session;

public class SessionInfoWrapper {
	private int batchSize = 1;
	private Session session;
	
	public SessionInfoWrapper(Session session, int batchSize) {
		this.session = session;
		this.batchSize = batchSize;
	}
	
	public int getBatchSize() {
		return this.batchSize;
	}

	public Session getSession() {
		return this.session;
	}
}
