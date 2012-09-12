package org.sharpsw.crlserver.data.service;

import java.util.Set;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

public abstract class BaseDAO<T> {

	private Session session;
	private int batchSize = 1;
	private boolean useLocalTransaction = true;
	private Transaction transaction;
	
	public BaseDAO(Session session, int batchSize, boolean useLocalTransaction) {
		this.session = session;
		this.batchSize = batchSize;
		this.useLocalTransaction = useLocalTransaction;
	}
	
	public BaseDAO(Session session, int batchSize) {
		this.session = session;
		this.batchSize = batchSize;
		this.useLocalTransaction = true;
	}
	
	protected Session getSession() {
		return this.session;
	}
	
	protected int getBatchSize() {
		return this.batchSize;
	}
	
	protected boolean useLocalTransaction() {
		return this.useLocalTransaction;
	}
	
	protected void beginTransaction() {
		if(this.useLocalTransaction) {
			this.transaction = this.session.beginTransaction();
		}
	}
	
	protected void commit() {
		if(this.useLocalTransaction) {
			this.transaction.commit();
		}
	}
	
	protected void rollback() {
		if(this.useLocalTransaction) {
			if(this.transaction != null && this.transaction.isActive()) {
				this.transaction.rollback();
			}
		}
	}
	
	protected void flush() {
		this.session.flush();
	}
	
	protected void clear() {
		this.session.clear();
	}
	
	protected Query createQuery(String query) {
		return this.session.createQuery(query);
	}
	
	protected Object add(Object object) {
		return this.session.save(object);
	}
	
	protected void remove(Object item) {
		this.session.delete(item);
	}
	
	protected void alter(Object item) {
		this.session.update(item);
	}
	
	public abstract void save(T entity) throws CreateException;
	
	public abstract void save(Set<T> entities) throws CreateException;

	public abstract Set<T> findAll() throws ReadException;

	public abstract void update(T entity) throws UpdateException;

	public abstract void delete(T entity) throws DeleteException;
	
	public abstract void delete(Set<T> entities) throws DeleteException;

}
