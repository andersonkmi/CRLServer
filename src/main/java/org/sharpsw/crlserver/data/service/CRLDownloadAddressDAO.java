package org.sharpsw.crlserver.data.service;

import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import org.apache.log4j.Logger;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.sharpsw.crlserver.data.CRLDownloadAddress;
import org.sharpsw.crlserver.data.CRLDownloadAddressCompositePrimaryKey;
import org.sharpsw.crlserver.data.CRLIssuer;

public class CRLDownloadAddressDAO extends BaseDAO<CRLDownloadAddress> {
	private static final Logger logger = Logger.getLogger(CRLDownloadAddressDAO.class);
	
	public CRLDownloadAddressDAO(Session session, int batchSize, boolean useLocalTransaction) {
		super(session, batchSize, useLocalTransaction);
	}
	
	@Override
	public void save(CRLDownloadAddress entity) throws CreateException, IllegalArgumentException {		
		try {
			// Trace logging
			if(logger.isTraceEnabled()) {
				logger.trace("Entering CRLDownloadAddressDAO.save(CRLDownloadAddress) method.");
			}

			if(entity == null) {
				throw new IllegalArgumentException("The entity argument cannot be null");
			}
			
			this.beginTransaction();
			this.add(entity);
			this.commit();
			this.getSession().clear();
		} catch (HibernateException exception) {
			this.rollback();
			
			StringBuffer message = new StringBuffer();
			message.append("Error when adding a new CRL download address. Message: '").append(exception.getMessage()).append("'.");
			throw new CreateException(message.toString());
		} finally {
			// Trace logging
			if(logger.isTraceEnabled()) {
				logger.trace("Leaving CRLDownloadAddressDAO.save(CRLDownloadAddress) method.");
			}
		}
	}

	@Override
	public void save(Set<CRLDownloadAddress> entities) throws CreateException {
		
		try {
			// Trace logging
			if(logger.isTraceEnabled()) {
				logger.trace("Entering CRLDownloadAddressDAO.save(Set<CRLDownloadAddress>) method.");
			}

			int counter = 0;
			if(entities != null && !entities.isEmpty()) {
				this.beginTransaction();
				for(CRLDownloadAddress address : entities) {
					this.add(address);
					counter++;
					if(counter % this.getBatchSize() == 0) {
						this.flush();
						this.clear();
					}
				}
				this.commit();				
			} else {
				throw new IllegalArgumentException("The entities list argument cannot be null");
			}
		} catch (HibernateException exception) {
			this.rollback();
		
			StringBuffer message = new StringBuffer();
			message.append("Error has occurred when bulk adding CRL download address. Message: '").append(exception.getMessage()).append("'.");
			throw new CreateException(message.toString());
		} finally {
			// Trace logging
			if(logger.isTraceEnabled()) {
				logger.trace("Leaving CRLDownloadAddressDAO.save(Set<CRLDownloadAddress>) method.");
			}	
		}
	}

	@SuppressWarnings("rawtypes")
	@Override
	public Set<CRLDownloadAddress> findAll() throws ReadException {
		try {
			Set<CRLDownloadAddress> results = new TreeSet<CRLDownloadAddress>();
			// Trace logging
			if(logger.isTraceEnabled()) {
				logger.trace("Entering CRLDownloadAddressDAO.read() method.");
			}
			
			Query query = this.getSession().createQuery("FROM CRLDownloadAddress");
			List elements = query.list();
			for(Object element : elements) {
				CRLDownloadAddress item = (CRLDownloadAddress) element;
				results.add(item);
			}
			return results;
		} catch (HibernateException exception) {
			StringBuffer message = new StringBuffer();
			message.append("Error when listing the CRL download address. Message: '").append(exception.getMessage()).append("'.");
			throw new ReadException(message.toString(), exception);
		}
	}
	
	
	@SuppressWarnings("rawtypes")
	public Set<CRLDownloadAddress> findCRLDownloadAddressByIssuer(CRLIssuer issuer) throws ReadException {
		try {
			// Trace logging
			if(logger.isTraceEnabled()) {
				logger.trace("Entering CRLDownloadAddressDAO.findCRLDownloadAddressByIssuer() method.");
			}
			
			if(issuer != null) {
				Set<CRLDownloadAddress> results = new TreeSet<CRLDownloadAddress>();
				Query query = this.getSession().createQuery("FROM CRLDownloadAddress addr WHERE addr.primaryKey.crlIssuer = :issuer");
				query.setParameter("issuer", issuer);
				List elements = query.list();
				for(Object element : elements) {
					CRLDownloadAddress item = (CRLDownloadAddress) element;
					results.add(item);
				}
				return results;				
			} else {
				throw new IllegalArgumentException("The issuer argument cannot be null");
			}
		} catch (HibernateException exception) {
			StringBuffer message = new StringBuffer();
			message.append("Error when finding CRL download address by issuer. Message: '").append(exception.getMessage()).append("'.");
			throw new ReadException(message.toString(), exception);
		} finally {
			// Trace logging
			if(logger.isTraceEnabled()) {
				logger.trace("Leaving CRLDownloadAddressDAO.findCRLDownloadAddressByIssuer() method.");
			}
		}
	}
	
	public CRLDownloadAddress findCRLDownloadAddressByPrimaryKey(CRLDownloadAddressCompositePrimaryKey key) throws ReadException {
		try {
			// Trace logging
			if(logger.isTraceEnabled()) {
				logger.trace("Entering CRLDownloadAddressDAO.findCRLDownloadAddressByPrimaryKey() method.");
			}
			
			if(key != null) {
				Query query = this.getSession().createQuery("FROM CRLDownloadAddress addr WHERE addr.primaryKey = :pk");
				query.setParameter("pk", key);
				CRLDownloadAddress result = (CRLDownloadAddress) query.uniqueResult();
				return result;
			} else {
				throw new IllegalArgumentException("The primary key argument cannot be null");
			}
		} catch (HibernateException exception) {
			StringBuffer message = new StringBuffer();
			message.append("Error when retrieving the CRL download address using the primary key info. Message: '").append(exception.getMessage()).append("'.");
			throw new ReadException(message.toString(), exception);
		} finally {
			// Trace logging
			if(logger.isTraceEnabled()) {
				logger.trace("Leaving CRLDownloadAddressDAO.findCRLDownloadAddressByPrimaryKey() method.");
			}
		}
		
	}
	
	@SuppressWarnings("rawtypes")
	public Set<CRLDownloadAddress> findCRLDownloadAddressBasedOnUrl(String url) throws ReadException {
		try {
			// Trace logging
			if(logger.isTraceEnabled()) {
				logger.trace("Entering CRLDownloadAddressDAO.findCRLDownloadAddressBasedOnUrl() method.");
			}
			Query query = this.getSession().createQuery("FROM CRLDownloadAddress addr WHERE addr.primaryKey.address LIKE :url");
			StringBuffer buffer = new StringBuffer();
			buffer.append("%").append(url).append("%");
			query.setParameter("url", buffer.toString());
			
			Set<CRLDownloadAddress> results = new TreeSet<CRLDownloadAddress>();
			
			List elements = query.list();
			for(Object element : elements) {
				CRLDownloadAddress item = (CRLDownloadAddress) element;
				results.add(item);
			}
			return results;
		} catch (HibernateException exception) {
			StringBuffer message = new StringBuffer();
			message.append("Error when retrieving the CRL download address based on URL. Message: '").append(exception.getMessage()).append("'.");
			throw new ReadException(message.toString(), exception);
		} finally {
			// Trace logging
			if(logger.isTraceEnabled()) {
				logger.trace("Leaving CRLDownloadAddressDAO.findCRLDownloadAddressBasedOnUrl() method.");
			}						
		}
	}

	@Override
	public void update(CRLDownloadAddress entity) throws UpdateException {
		try {
			// Trace logging
			if(logger.isTraceEnabled()) {
				logger.trace("Entering CRLDownloadAddressDAO.update() method.");
			}

			if(entity != null) {
				this.beginTransaction();
				this.alter(entity);
				this.commit();				
			} else {
				throw new IllegalArgumentException("The entity argument cannot be null");
			}
		} catch (HibernateException exception) {
			this.rollback();
		
			StringBuffer message = new StringBuffer();
			message.append("Error when updating a CRL download address entity. Message: '").append(exception.getMessage()).append("'.");
			throw new UpdateException(message.toString(), exception);
		} finally {
			// Trace logging
			if(logger.isTraceEnabled()) {
				logger.trace("Leaving CRLDownloadAddressDAO.update() method.");
			}			
		}
	}

	@Override
	public void delete(CRLDownloadAddress entity) throws DeleteException, IllegalArgumentException {
		try {
			// Trace logging
			if(logger.isTraceEnabled()) {
				logger.trace("Entering CRLDownloadAddressDAO.delete(CRLDownloadAddress) method.");			
			}

			if(entity != null) {
				this.beginTransaction();
				this.remove(entity);
				this.commit();				
			} else {
				throw new IllegalArgumentException("The entity argument cannot be null");
			}
		} catch (HibernateException exception) {
			this.rollback();
			
			StringBuffer message = new StringBuffer();
			message.append("Error when removing a CRL download address. Message: '").append(exception.getMessage()).append("'.");
			throw new DeleteException(message.toString());
		} finally {
			// Trace logging
			if(logger.isTraceEnabled()) {
				logger.trace("Leaving CRLDownloadAddressDAO.delete(CRLDownloadAddress) method.");			
			}	
		}
	}

	@Override
	public void delete(Set<CRLDownloadAddress> entities) throws DeleteException, IllegalArgumentException {		
		try {
			// Trace logging
			if(logger.isTraceEnabled()) {
				logger.trace("Entering CRLDownloadAddressDAO.delete(Set<CRLDownloadAddress>) method.");
			}
			
			if(entities != null && !entities.isEmpty()) {
				this.beginTransaction();
				for(CRLDownloadAddress addr : entities) {
					this.remove(addr);
				}
				this.commit();				
			} else {
				throw new IllegalArgumentException("The set argument cannot be null");
			}
		} catch (HibernateException exception) {
			this.rollback();
			
			StringBuffer message = new StringBuffer();
			message.append("Error when deleting a CRL download address. Message: '").append(exception.getMessage()).append("'.");
			throw new DeleteException(message.toString(), exception);
		} finally {
			// Trace logging
			if(logger.isTraceEnabled()) {
				logger.trace("Leaving CRLDownloadAddressDAO.delete(Set<CRLDownloadAddress>) method.");
			}	
		}
	}
}
