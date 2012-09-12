package org.sharpsw.crlserver.data.service;

import java.io.File;
import java.io.FileInputStream;
import java.util.Properties;

import org.dbunit.IDatabaseTester;
import org.dbunit.JdbcDatabaseTester;
import org.dbunit.dataset.IDataSet;
import org.dbunit.dataset.xml.FlatXmlDataSetBuilder;
import org.dbunit.operation.DatabaseOperation;
import org.hibernate.Session;
import org.junit.After;
import org.junit.Before;
import org.sharpsw.crlserver.persistence.utils.BaseHibernateSessionFactory;
import org.sharpsw.crlserver.persistence.utils.DefaultHibernateSessionFactory;


public abstract class BaseDatabaseUnitTestCase {
	private static final String DATABASE_DRIVER_CLASS = "database.driver";
	private static final String DATABASE_USER = "database.user";
	private static final String DATABASE_PASSWORD = "database.password";
	private static final String DATABASE_CONNECTION_URL = "database.connection_url";
	
	protected IDatabaseTester databaseTester;
	private Session session;
	private int batchSize = 0;
	private BaseHibernateSessionFactory factory;
	
	@Before
	public void setUp() throws Exception {
		StringBuffer filePath = new StringBuffer();
		filePath.append("config").append(File.separator).append("database.properties");
		FileInputStream databaseUnderTestConnectionConfig = new FileInputStream(filePath.toString());		
		Properties props = new Properties();
		props.load(databaseUnderTestConnectionConfig);
		
		this.databaseTester = new JdbcDatabaseTester(props.getProperty(DATABASE_DRIVER_CLASS), props.getProperty(DATABASE_CONNECTION_URL), props.getProperty(DATABASE_USER), props.getProperty(DATABASE_PASSWORD));
		this.databaseTester.setSetUpOperation(DatabaseOperation.CLEAN_INSERT);
		FileInputStream dbFileStream = new FileInputStream(this.getInitialDatabaseFile());
		
		IDataSet set = new FlatXmlDataSetBuilder().build(dbFileStream);		
		this.databaseTester.setDataSet(set);
		this.databaseTester.setTearDownOperation(DatabaseOperation.DELETE_ALL);		
		this.databaseTester.onSetup();
		
		this.factory = new DefaultHibernateSessionFactory();
		this.factory.loadConfiguration();
		this.session = factory.getSession();
		this.batchSize = this.factory.getBatchSize();
	}
	
	@After
	public void tearDown() throws Exception {
		this.databaseTester.onTearDown();
		this.session.close();
	}
	
	protected Session getSession() {
		return this.session;
	}
	
	protected int getBatchSize() {
		return this.batchSize;
	}
	
	protected String getInitialDatabaseFile() {
		StringBuffer info = new StringBuffer();
		info.append("config").append(File.separator).append("database.xml");
		return info.toString();
	}
}
