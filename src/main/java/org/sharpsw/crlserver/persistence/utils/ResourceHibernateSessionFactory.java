package org.sharpsw.crlserver.persistence.utils;

import org.hibernate.HibernateException;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistry;
import org.hibernate.service.ServiceRegistryBuilder;

public class ResourceHibernateSessionFactory extends BaseHibernateSessionFactory {

	private String resource;
	
	public ResourceHibernateSessionFactory(String resource) {
		this.resource = resource;
	}
	
	public String getResource() {
		return this.resource;
	}

	@Override
	public void loadConfiguration() throws SessionFactoryConfigurationException {
		try {			
			Configuration config = new Configuration();
			config.configure(this.getResource());
			String batchSize = config.getProperty("hibernate.jdbc.batch_size");
			Integer batchSizeNumber = Integer.valueOf(batchSize);
			this.setBatchSize(batchSizeNumber.intValue());
			
			ServiceRegistry registry = new ServiceRegistryBuilder().applySettings(config.getProperties()).buildServiceRegistry();
			SessionFactory factory = config.buildSessionFactory(registry);
			setSessionFactory(factory);
		} catch (HibernateException exception) {
			StringBuffer msg = new StringBuffer();
			msg.append("Error when loading Hibernate configuration file. Message: '").append(exception.getMessage()).append("'.");
			throw new SessionFactoryConfigurationException(msg.toString(), exception);
		}
	}
}
