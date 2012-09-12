package org.sharpsw.crlserver.persistence.utils;

import org.apache.log4j.Logger;
import org.hibernate.HibernateException;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistry;
import org.hibernate.service.ServiceRegistryBuilder;

public class DefaultHibernateSessionFactory extends BaseHibernateSessionFactory {
	private static final Logger logger = Logger.getLogger(DefaultHibernateSessionFactory.class);

	public DefaultHibernateSessionFactory() {
	}

	@Override
	public void loadConfiguration() throws SessionFactoryConfigurationException {
		try {			
			Configuration config = new Configuration();
			config.configure();
			String batchSize = config.getProperty("hibernate.jdbc.batch_size");
			if(logger.isDebugEnabled()) {
				StringBuffer log = new StringBuffer();
				log.append("hibernate.jdbc.batch_size: ").append(batchSize);
				logger.debug(log.toString());
			}
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
