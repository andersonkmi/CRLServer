package org.sharpsw.crlserver.persistence.utils;

import java.io.File;

import org.hibernate.HibernateException;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistry;
import org.hibernate.service.ServiceRegistryBuilder;

public class FileHibernateSessionFactory extends BaseHibernateSessionFactory {

	private File file;
	
	public FileHibernateSessionFactory(File file) {
		this.file = file;
	}
	
	@Override
	public void loadConfiguration() throws SessionFactoryConfigurationException {
		if(!this.file.exists()) {
			StringBuffer msg = new StringBuffer();
			msg.append("The Hibernate config file '").append(this.file.getAbsolutePath()).append("' does not exist.");
			throw new SessionFactoryConfigurationException(msg.toString());
		}
		
		if(!this.file.canRead()) {
			StringBuffer msg = new StringBuffer();
			msg.append("The Hibernate config file '").append(this.file.getAbsolutePath()).append("' cannot be read.");
			throw new SessionFactoryConfigurationException(msg.toString());
		}
		
		try {			
			Configuration config = new Configuration();
			config.configure(this.file);
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
