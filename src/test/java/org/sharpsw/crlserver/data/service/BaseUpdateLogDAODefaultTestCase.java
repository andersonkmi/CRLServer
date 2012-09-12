package org.sharpsw.crlserver.data.service;

import java.io.File;

public abstract class BaseUpdateLogDAODefaultTestCase extends BaseDatabaseUnitTestCase {
	protected final String getInitialDatabaseFile() {
		StringBuffer info = new StringBuffer();
		info.append("config").append(File.separator).append("UpdateLogDAO").append(File.separator).append("clean-database.xml");
		return info.toString();
	}
}
