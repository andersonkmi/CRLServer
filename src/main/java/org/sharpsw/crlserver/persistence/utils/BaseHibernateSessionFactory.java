package org.sharpsw.crlserver.persistence.utils;

import org.hibernate.Session;
import org.hibernate.SessionFactory;

public abstract class BaseHibernateSessionFactory {
	private SessionFactory factory;
	private int batchSize = 10;
	
	public SessionFactory getSessionFactory() {
		return this.factory;
	}
	
	protected void setSessionFactory(SessionFactory factory) {
		this.factory = factory;
	}
		
	public Session getSession() {
		return this.factory.openSession();
	}
	
	protected void setBatchSize(int size) {
		this.batchSize = size;
	}
	
	public int getBatchSize() {
		return this.batchSize;
	}
	
	public void close() {
		this.factory.close();
	}
		
	public abstract void loadConfiguration() throws SessionFactoryConfigurationException;
}
