package org.sharpsw.crlserver.data.service;

import java.io.File;

public abstract class BaseRevokedCertificateDAOInitializedDBTestCase extends BaseDatabaseUnitTestCase {
	protected final String getInitialDatabaseFile() {
		StringBuffer info = new StringBuffer();
		info.append("config").append(File.separator).append("RevokedCertificateDAO").append(File.separator).append("initialized-database.xml");
		return info.toString();
	}
}
