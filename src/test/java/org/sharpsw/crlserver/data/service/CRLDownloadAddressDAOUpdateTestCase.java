package org.sharpsw.crlserver.data.service;

import java.io.File;
import java.io.FileInputStream;

import org.dbunit.Assertion;
import org.dbunit.dataset.IDataSet;
import org.dbunit.dataset.ITable;
import org.dbunit.dataset.filter.DefaultColumnFilter;
import org.dbunit.dataset.xml.FlatXmlDataSetBuilder;
import org.junit.Before;
import org.junit.Test;
import org.sharpsw.crlserver.data.CRLDownloadAddress;
import org.sharpsw.crlserver.data.CRLDownloadAddressCompositePrimaryKey;
import org.sharpsw.crlserver.data.CRLIssuer;

public class CRLDownloadAddressDAOUpdateTestCase extends BaseCrlDownloadAddressDAOTestCase {
	private CRLDownloadAddressDAO service;
	
	@Before
	public void setUp() throws Exception {
		super.setUp();
		this.service = new CRLDownloadAddressDAO(this.getSession(), this.getBatchSize(), true);
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void testUpdateCRLDownloadAddressNullArgFail() throws UpdateException {
		this.service.update(null);
	}
	
	@Test
	public void testUpdateCRLDownloadAddressOK() throws Exception {
		CRLIssuer issuer = new CRLIssuer();
		issuer.setId("2222222222222222222222222222222222222222");
		issuer.setName("CN=Autoridade Certificadora Raiz Brasileira v1, OU=Instituto Nacional de Tecnologia da Informacao - ITI, O=ICP-Brasil, C=BR");
		issuer.setDescription("Autoridade Certificadora Raiz Brasileira v1");
		
		CRLDownloadAddressCompositePrimaryKey pk = new CRLDownloadAddressCompositePrimaryKey(issuer, "http://www.url5.com/test.crl");
		CRLDownloadAddress addr = new CRLDownloadAddress(pk);
		addr.setDescription("URL de teste 5 - modificado");
		
		this.service.update(addr);
		
		StringBuffer dataSetFile = new StringBuffer();
		dataSetFile.append("config").append(File.separator).append("CRLDownloadAddressDAO").append(File.separator).append("testUpdateCRLDownloadAddressIssuerOK.xml");
		IDataSet expectedDataSet = new FlatXmlDataSetBuilder().build(new FileInputStream(dataSetFile.toString()));
		ITable expectedTable = expectedDataSet.getTable("CRL_DOWNLOAD_ADDRESS");
		
		IDataSet actualDataSet = this.databaseTester.getConnection().createDataSet();
		ITable actualTable = actualDataSet.getTable("CRL_DOWNLOAD_ADDRESS");
		ITable filteredActualTable = DefaultColumnFilter.includedColumnsTable(actualTable, expectedTable.getTableMetaData().getColumns());

		Assertion.assertEquals(expectedTable, filteredActualTable);
	}
}
