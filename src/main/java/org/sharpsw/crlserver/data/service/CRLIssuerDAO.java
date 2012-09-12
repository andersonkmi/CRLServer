package org.sharpsw.crlserver.data.service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.Vector;

import org.apache.log4j.Logger;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.sharpsw.crlserver.data.CRLIssuer;

public class CRLIssuerDAO extends BaseDAO<CRLIssuer> {
	private static final Logger logger = Logger.getLogger(CRLIssuerDAO.class);
	private Vector<String> createdIds;
	
	public CRLIssuerDAO(Session session, int batchSize, boolean useLocalTransaction) {
		super(session, batchSize, useLocalTransaction);
		this.createdIds = new Vector<String>();
	}
	
	public void save(CRLIssuer issuer) throws CreateException, IllegalArgumentException {
		// Trace logging
		if(logger.isTraceEnabled()) {
			logger.trace("Entering CRLIssuerDataAccessService.save() method.");
		}
		
		try {
			if(issuer == null) {
				throw new IllegalArgumentException("The issuer argument cannot be null.");
			}
			
			this.beginTransaction();
			String id = (String) this.add(issuer);
			this.createdIds.add(id);
			this.commit();
			this.clear();
		} catch (HibernateException exception) {
			this.rollback();
			StringBuffer message = new StringBuffer();
			message.append("Error when creating a new issuer. Message: '").append(exception.getMessage()).append("'.");
			throw new CreateException(message.toString(), exception);
		} finally {
			// Trace logging
			if(logger.isTraceEnabled()) {
				logger.trace("Leaving CRLIssuerDataAccessService.save() method");
			}
		}
	}
	
	public void save(Set<CRLIssuer> entities) throws CreateException, IllegalArgumentException {
		try {
			// Trace logging
			if(logger.isTraceEnabled()) {
				logger.trace("Entering CRLIssuerDataAccessService.save(Set) method.");
			}
			if(entities == null) {
				throw new IllegalArgumentException("The list argument cannot be null");
			}
			
			this.beginTransaction();
			int counter = 0;
			for(CRLIssuer issuer : entities) {
				String id = (String) this.add(issuer);
				this.createdIds.add(id);
				counter++;
				if(counter % this.getBatchSize() == 0) {
					this.flush();
					this.clear();					
				}
			}
			this.commit();
		} catch (HibernateException exception) {
			this.rollback();
			
			StringBuffer message = new StringBuffer();
			message.append("Error when adding issuers into the database. Message: '").append(exception.getMessage()).append("'.");
			throw new CreateException(message.toString(), exception);
		} finally {
			// Trace logging
			if(logger.isTraceEnabled()) {
				logger.trace("Leaving CRLIssuerDataAccessService.save(Set) method.");
			}
		}
	}
	
	@SuppressWarnings("rawtypes")
	public Set<CRLIssuer> findAll() throws ReadException {		
		try {
			// Trace logging
			if(logger.isTraceEnabled()) {
				logger.trace("Entering CRLIssuerDataAccessService.read() method.");
			}
			
			Set<CRLIssuer> results = new HashSet<CRLIssuer>();
			Query query = this.createQuery("FROM CRLIssuer");
			List elements = query.list();
			for(Object element : elements) {
				CRLIssuer issuer = (CRLIssuer) element;
				results.add(issuer);
			}
			return results;			
		} catch (HibernateException exception) {
			StringBuffer message = new StringBuffer();
			message.append("Error when listing all issuers in the database. Message: '").append(exception.getMessage()).append("'.");
			throw new ReadException(message.toString(), exception);
		} finally {
			// Trace logging
			if(logger.isTraceEnabled()) {
				logger.trace("Leaving CRLIssuerDataAccessService.read() method.");
			}
		}
	}
	
	public void delete(CRLIssuer issuer) throws DeleteException, IllegalArgumentException {
		try {
			// Trace logging
			if(logger.isTraceEnabled()) {
				logger.trace("Entering CRLIssuerDataAccessService.delete() method.");
			}

			if(issuer == null) {
				throw new IllegalArgumentException("The issuer argument cannot be null");
			}
			
			this.beginTransaction();
			this.remove(issuer);
			this.commit();
		} catch (HibernateException exception) {
			this.rollback();
			
			StringBuffer message = new StringBuffer();
			message.append("Error when deleting a CRL issuer. Message: '").append(exception.getMessage()).append("'.");
			throw new DeleteException(message.toString(), exception);
		} finally {
			// Trace logging
			if(logger.isTraceEnabled()) {
				logger.trace("Entering CRLIssuerDataAccessService.delete() method.");
			}			
		}
	}
	
	public void delete(Set<CRLIssuer> issuers) throws DeleteException {
		
		try {
			// Trace logging
			if(logger.isTraceEnabled()) {
				logger.trace("Entering CRLIssuerDataAccessService.delete(Set<T>) method.");
			}

			if(issuers == null) {
				throw new IllegalArgumentException("The list argument cannot be null");
			}
			
			this.beginTransaction();
			for(CRLIssuer issuer: issuers) {
				this.remove(issuer);
			}
			this.commit();
		} catch(HibernateException exception) {
			this.rollback();
			
			StringBuffer message = new StringBuffer();
			message.append("Error when deleting a set of CRL issuers. Message: '").append(exception.getMessage()).append("'.");
			throw new DeleteException(message.toString());
		} finally {
			this.getSession().clear();
			
			// Trace logging
			if(logger.isTraceEnabled()) {
				logger.trace("Leaving CRLIssuerDataAccessService.delete(Set<T>) method.");
			}	
		}
	}
	
	
	public void update(CRLIssuer issuer) throws UpdateException, IllegalArgumentException {		
		try {
			// Trace logging
			if(logger.isTraceEnabled()) {
				logger.trace("Entering CRLIssuerDataAccessService.update() method.");
			}
			
			if(issuer == null) {
				throw new IllegalArgumentException("The issuer argument cannot be null");
			}
			
			this.beginTransaction();
			this.getSession().update(issuer);
			this.commit();
		} catch (HibernateException exception) {
			this.rollback();
			
			StringBuffer message = new StringBuffer();
			message.append("Error when updating a CRL issuer. Message: '").append(exception.getMessage()).append("'.");
			throw new UpdateException(message.toString(), exception);
		} finally {
			// Trace logging
			if(logger.isTraceEnabled()) {
				logger.trace("Leaving CRLIssuerDataAccessService.update() method.");
			}	
		}
	}
	
	
	public CRLIssuer findById(String id) throws ReadException, IllegalArgumentException {
		// Trace logging
		if(logger.isTraceEnabled()) {
			logger.trace("Entering CRLIssuerDataAccessService.findById() method.");
		}
		
		try {
			if(id == null) {
				throw new IllegalArgumentException("The id argument cannot be null");
			}
			CRLIssuer result = null;
			Query query = this.getSession().createQuery("FROM CRLIssuer issuer WHERE issuer.id = ?");
			query.setString(0, id);			
			result = (CRLIssuer) query.uniqueResult();
			if(result == null) {
				StringBuffer message = new StringBuffer();
				message.append("No CRL issuer found with id = '").append(id).append("'.");
				throw new ReadException(message.toString());
			}
			return result;			
		} catch (HibernateException exception) {
			StringBuffer message = new StringBuffer();
			message.append("Error when trying to find CRL issuer with id = '").append(id).append("'. Message: '").append(exception.getMessage()).append("'.");
			throw new ReadException(message.toString(), exception);
		} finally {
			// Trace logging
			if(logger.isTraceEnabled()) {
				logger.trace("Leaving CRLIssuerDataAccessService.findById() method.");
			}
		}
	}
	
	@SuppressWarnings("rawtypes")
	public Set<CRLIssuer> findIssuersBasedOnName(String name) throws ReadException, IllegalArgumentException {
		// Trace logging
		if(logger.isTraceEnabled()) {
			logger.trace("Entering CRLIssuerDataAccessService.findIssuerBasedOnName() method.");
		}
		try {
			if(name == null) {
				throw new IllegalArgumentException("The name argument cannot be null");
			}
			Set<CRLIssuer> elements = new HashSet<CRLIssuer>();
			Query query = this.getSession().createQuery("FROM CRLIssuer issuer WHERE issuer.name LIKE :issuerName");
			StringBuilder parameter = new StringBuilder();
			parameter.append("%").append(name).append("%");
			query.setParameter("issuerName", parameter.toString());
			List results = query.list();
			for(Object item : results) {
				CRLIssuer element = (CRLIssuer) item;
				elements.add(element);
			}
			return elements;			
		} catch (HibernateException exception) {
			StringBuffer message = new StringBuffer();
			message.append("Error when trying to find CRL issuer based on the name '").append(name).append("'. Message: '").append(exception.getMessage()).append("'.");
			throw new ReadException(message.toString(), exception);
		} finally {
			// Trace logging
			if(logger.isTraceEnabled()) {
				logger.trace("Entering CRLIssuerDataAccessService.findIssuerBasedOnName() method.");
			}	
		}
	}

	Vector<String> getLastCreatedIds() {
		return this.createdIds;
	}
}