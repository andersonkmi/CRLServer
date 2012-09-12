package org.sharpsw.crlserver.data.service;

import java.util.Set;

import org.apache.log4j.Logger;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.sharpsw.crlserver.data.UpdateLog;

public class UpdateLogDAO extends BaseDAO<UpdateLog> {
	private static final Logger logger = Logger.getLogger(UpdateLogDAO.class);
	
	public UpdateLogDAO(Session session, int batchSize, boolean useLocalTransaction) {
		super(session, batchSize, useLocalTransaction);
	}

	@Override
	public void save(UpdateLog entity) throws CreateException {
		try {
			// Trace logging
			if(logger.isTraceEnabled()) {
				logger.trace("Entering UpdateLogDAO.save() method.");
			}
			
			if(entity != null) {
				this.beginTransaction();
				this.add(entity);
				this.commit();
			} else {
				throw new IllegalArgumentException("The entity argument cannot be null");
			}
		} catch (HibernateException exception) {
			this.rollback();
			
			StringBuffer message = new StringBuffer();
			message.append("Error when saving a new update log. Message: '").append(exception.getMessage()).append("'.");
			throw new CreateException(message.toString(), exception);
		} finally {
			// Trace logging
			if(logger.isTraceEnabled()) {
				logger.trace("Leaving UpdateLogDAO.save() method.");
			}			
		}
	}

	@Override
	public void save(Set<UpdateLog> entities) throws CreateException {
		try {
			// Trace logging
			if(logger.isTraceEnabled()) {
				logger.trace("Entering UpdateLogDAO.save(Set) method.");
			}
			
			this.beginTransaction();			
			int counter = 0;
			for(UpdateLog entity : entities) {
				this.add(entity);
				if(counter % this.getBatchSize() == 0) {
					this.flush();
					this.clear();
				}
			}			
			this.commit();
		} catch (HibernateException exception) {
			this.rollback();
			
			StringBuffer message = new StringBuffer();
			message.append("Error when adding update logs. Message: '").append(exception.getMessage()).append("'.");
			throw new CreateException(message.toString(), exception);
		} finally {
			// Trace logging
			if(logger.isTraceEnabled()) {
				logger.trace("Leaving UpdateLogDAO.save(Set) method.");
			}			
		}
	}

	@Override
	public Set<UpdateLog> findAll() throws ReadException {
		return null;
	}

	@Override
	public void update(UpdateLog entity) throws UpdateException {
		
	}

	@Override
	public void delete(UpdateLog entity) throws DeleteException {
		
	}

	@Override
	public void delete(Set<UpdateLog> entities) throws DeleteException {
		
	}
}
