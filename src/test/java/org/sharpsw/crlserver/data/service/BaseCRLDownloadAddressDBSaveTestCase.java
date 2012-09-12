package org.sharpsw.crlserver.data.service;

import java.io.File;

public abstract class BaseCRLDownloadAddressDBSaveTestCase extends BaseDatabaseUnitTestCase {
	protected final String getInitialDatabaseFile() {
		StringBuffer info = new StringBuffer();
		info.append("config").append(File.separator).append("CRLDownloadAddressDAO").append(File.separator).append("crl_download_address_db.xml");
		return info.toString();
	}
}
