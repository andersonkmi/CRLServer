package org.sharpsw.crlserver.data.service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.log4j.Logger;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.sharpsw.crlserver.data.RevokedCertificate;
import org.sharpsw.crlserver.data.RevokedCertificateCompositePrimaryKey;

public class RevokedCertificateDAO extends BaseDAO<RevokedCertificate> {
	private static final Logger logger = Logger.getLogger(RevokedCertificateDAO.class);
	
	public RevokedCertificateDAO(Session session, int batchSize, boolean useLocalTransaction) {
		super(session, batchSize, useLocalTransaction);
	}
	
	@Override
	public void save(RevokedCertificate entity) throws CreateException {
		try {
			// Trace logging
			if(logger.isTraceEnabled()) {
				logger.trace("Entering RevokedCertificateDAO.save() method");
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
			message.append("Error when saving a new revoked certificate. Message: '").append(exception.getMessage()).append("'.");
			throw new CreateException(message.toString(), exception);
		} finally {
			// Trace logging
			if(logger.isTraceEnabled()) {
				logger.trace("Leaving RevokedCertificateDAO.save() method");
			}			
		}
	}

	@Override
	public void save(Set<RevokedCertificate> entities) throws CreateException {
		try {
			// Trace logging
			if(logger.isTraceEnabled()) {
				logger.trace("Entering RevokedCertificateDAO.save(Set<RevokedCertificate>) method.");
			}
			
			if(entities != null) {
				if(!entities.isEmpty()) {
					this.beginTransaction();
					int counter = 0;
					for(RevokedCertificate certificate : entities) {
						this.add(certificate);
						++counter;
						if(counter % this.getBatchSize() == 0) {
							this.flush();
							this.clear();
						}
					}
					this.commit();
				}
			} else {
				throw new IllegalArgumentException("The set argument cannot be null");
			}
		} catch (HibernateException exception) {
			this.rollback();
			
			StringBuffer message = new StringBuffer();
			message.append("Error occurred when bulk saving revoked certificates. Message: '").append(exception.getMessage()).append("'.");
			throw new CreateException(message.toString(), exception);
		} finally {
			// Trace logging
			if(logger.isTraceEnabled()) {
				logger.trace("Leaving RevokedCertificateDAO.save(Set<RevokedCertificate>) method.");
			}	
		}
	}

	@SuppressWarnings("rawtypes")
	@Override
	public Set<RevokedCertificate> findAll() throws ReadException {
		try {
			// Trace logging
			if(logger.isTraceEnabled()) {
				logger.trace("Entering RevokedCertificateDAO.findAll() method.");
			}
			Set<RevokedCertificate> items = new HashSet<RevokedCertificate>();
			Query query = this.createQuery("FROM RevokedCertificate");
			List results = query.list();
			for(Object item : results) {
				RevokedCertificate certificate = (RevokedCertificate) item;
				items.add(certificate);
			}
			return items;
		} catch (HibernateException exception) {
			StringBuffer message = new StringBuffer();
			message.append("Error when finding all revoked certificates. Message: '").append(exception.getMessage()).append("'.");
			throw new ReadException(message.toString(), exception);
		} finally {
			// Trace logging
			if(logger.isTraceEnabled()) {
				logger.trace("Leaving RevokedCertificateDAO.findAll() method.");
			}
		}
	}
	
	public RevokedCertificate findByPrimaryKey(RevokedCertificateCompositePrimaryKey pk) throws ReadException {
		try {
			// Trace logging
			if(logger.isTraceEnabled()) {
				logger.trace("Entering RevokedCertificateDAO.findByPrimaryKey() method.");
			}
			
			if(pk != null) {
				Query query = this.createQuery("FROM RevokedCertificate cert WHERE cert.primaryKey = :pk");
				query.setParameter("pk", pk);
				RevokedCertificate certificate = (RevokedCertificate) query.uniqueResult();
				return certificate;				
			} else {
				throw new IllegalArgumentException("The primary key argument cannot be null");
			}
			
		} catch (HibernateException exception) {
			StringBuffer message = new StringBuffer();
			message.append("Error when finding certificate by primary key. Message: '").append(exception.getMessage()).append("'.");
			throw new ReadException(message.toString(), exception);
		} finally {
			// Trace logging
			if(logger.isTraceEnabled()) {
				logger.trace("Leaving RevokedCertificateDAO.findByPrimaryKey() method.");
			}			
		}
	}

	@Override
	public void update(RevokedCertificate entity) throws UpdateException {
		try {
			// Trace logging
			if(logger.isTraceEnabled()) {
				logger.trace("Entering RevokedCertificateDAO.update() method.");
			}
			
			if(entity != null) {
				this.beginTransaction();
				this.alter(entity);
				this.commit();
			} else {
				throw new IllegalArgumentException("The certificate argument cannot be null");
			}
		} catch (HibernateException exception) {
			this.rollback();
			
			StringBuffer message = new StringBuffer();
			message.append("Error when updating certificate. Message: '").append(exception.getMessage()).append("'.");
			throw new UpdateException(message.toString(), exception);
		} finally {
			// Trace logging
			if(logger.isTraceEnabled()) {
				logger.trace("Leaving RevokedCertificateDAO.update() method.");
			}
		}
	}

	@Override
	public void delete(RevokedCertificate entity) throws DeleteException {
		try {
			// trace logging
			if(logger.isTraceEnabled()) {
				logger.trace("Entering RevokedCertificateDAO.delete() method.");
			}
			this.beginTransaction();
			this.remove(entity);
			this.commit();
		} catch (HibernateException exception) {
			this.rollback();
			
			StringBuffer message = new StringBuffer();
			message.append("Error when deleting a certificate. Message: '").append(exception.getMessage()).append("'.");
			throw new DeleteException(message.toString(), exception);
		} finally {
			// trace logging
			if(logger.isTraceEnabled()) {
				logger.trace("Leaving RevokedCertificateDAO.delete() method.");
			}			
		}
	}

	@Override
	public void delete(Set<RevokedCertificate> entities) throws DeleteException {
		try {
			// Trace logging
			if(logger.isTraceEnabled()) {
				logger.trace("Entering RevokedCertificateDAO.delete(Set) method.");
			}
			
			if(entities != null) {
				this.beginTransaction();
				for(RevokedCertificate certificate : entities) {
					this.remove(certificate);
				}
				this.commit();				
			} else {
				throw new IllegalArgumentException("The certificate set argument cannot be null");
			}
		} catch (HibernateException exception) {
			this.rollback();
			
			StringBuffer message = new StringBuffer();
			message.append("Error when deleting multiple certificates. Message:'").append(exception.getMessage()).append("'.");
			throw new DeleteException(message.toString(), exception);
		} finally {
			// Trace logging
			if(logger.isTraceEnabled()) {
				logger.trace("Leaving RevokedCertificateDAO.delete(Set) method.");
			}
		}
		
	}

}
