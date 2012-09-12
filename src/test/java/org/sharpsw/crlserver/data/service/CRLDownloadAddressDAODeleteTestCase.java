package org.sharpsw.crlserver.data.service;

import java.io.File;
import java.io.FileInputStream;
import java.util.HashSet;
import java.util.Set;

import org.dbunit.Assertion;
import org.dbunit.dataset.IDataSet;
import org.dbunit.dataset.ITable;
import org.dbunit.dataset.SortedTable;
import org.dbunit.dataset.filter.DefaultColumnFilter;
import org.dbunit.dataset.xml.FlatXmlDataSetBuilder;
import org.junit.Before;
import org.junit.Test;
import org.sharpsw.crlserver.data.CRLDownloadAddress;
import org.sharpsw.crlserver.data.CRLDownloadAddressCompositePrimaryKey;
import org.sharpsw.crlserver.data.CRLIssuer;

public class CRLDownloadAddressDAODeleteTestCase extends BaseCrlDownloadAddressDAOTestCase {
	private CRLDownloadAddressDAO service;
	
	@Before
	public void setUp() throws Exception {
		super.setUp();
		this.service = new CRLDownloadAddressDAO(this.getSession(), this.getBatchSize(), true);
	}
	
	@Test
	public void testDeleteCrlDownloadAddressOK() throws Exception {
		CRLIssuer issuer = new CRLIssuer();
		issuer.setId("1111111111111111111111111111111111111111");
		issuer.setName("CN=AC Secretaria da Receita Federal do Brasil, O=ICP-Brasil, C=BR");
		issuer.setDescription("Secretaria da Receita Federal do Brasil");
		
		CRLDownloadAddressCompositePrimaryKey key = new CRLDownloadAddressCompositePrimaryKey(issuer, "http://www.url1.com/test.crl");
		CRLDownloadAddress address = new CRLDownloadAddress(key);
		address.setDescription("URL de teste 1");
		
		this.service.delete(address);
		
		StringBuffer dataSetFile = new StringBuffer();
		dataSetFile.append("config").append(File.separator).append("CRLDownloadAddressDAO").append(File.separator).append("testDeleteCrlDownloadAddressOK.xml");
		IDataSet expectedDataSet = new FlatXmlDataSetBuilder().build(new FileInputStream(dataSetFile.toString()));
		ITable expectedTable = expectedDataSet.getTable("CRL_DOWNLOAD_ADDRESS");
		SortedTable expectedSortedTable = new SortedTable(expectedTable, new String[] {"address"});
		expectedSortedTable.setUseComparable(true);
		
		IDataSet actualDataSet = this.databaseTester.getConnection().createDataSet();
		ITable actualTable = actualDataSet.getTable("CRL_DOWNLOAD_ADDRESS");
		ITable filteredActualTable = DefaultColumnFilter.includedColumnsTable(actualTable, expectedTable.getTableMetaData().getColumns());
		SortedTable actualSortedTable = new SortedTable(filteredActualTable, new String[] {"address"});
		actualSortedTable.setUseComparable(true);

		Assertion.assertEquals(expectedSortedTable, actualSortedTable);
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void testDeleteNullCrlDownloadAddressFail() throws DeleteException {
		CRLDownloadAddress addr = null;
		this.service.delete(addr);
	}
	
	@Test
	public void testDeleteMultipleCrlDownloadAddressOK() throws Exception {
		Set<CRLDownloadAddress> addrs = new HashSet<CRLDownloadAddress>();
		
		CRLIssuer issuer = new CRLIssuer();
		issuer.setId("1111111111111111111111111111111111111111");
		issuer.setName("CN=AC Secretaria da Receita Federal do Brasil, O=ICP-Brasil, C=BR");
		issuer.setDescription("Secretaria da Receita Federal do Brasil");
		
		CRLDownloadAddressCompositePrimaryKey key = new CRLDownloadAddressCompositePrimaryKey(issuer, "http://www.url1.com/test.crl");
		CRLDownloadAddress address = new CRLDownloadAddress(key);
		address.setDescription("URL de teste 1");
		
		CRLDownloadAddressCompositePrimaryKey key2 = new CRLDownloadAddressCompositePrimaryKey(issuer, "http://www.url3.com/test.crl");
		CRLDownloadAddress address2 = new CRLDownloadAddress(key2);
		address2.setDescription("URL de teste 3");
		
		addrs.add(address);
		addrs.add(address2);
		
		this.service.delete(addrs);
		
		StringBuffer dataSetFile = new StringBuffer();
		dataSetFile.append("config").append(File.separator).append("CRLDownloadAddressDAO").append(File.separator).append("testDeleteMultipleCrlDownloadAddressOK.xml");
		IDataSet expectedDataSet = new FlatXmlDataSetBuilder().build(new FileInputStream(dataSetFile.toString()));
		ITable expectedTable = expectedDataSet.getTable("CRL_DOWNLOAD_ADDRESS");
		SortedTable expectedSortedTable = new SortedTable(expectedTable, new String[] {"address"});
		expectedSortedTable.setUseComparable(true);
		
		IDataSet actualDataSet = this.databaseTester.getConnection().createDataSet();
		ITable actualTable = actualDataSet.getTable("CRL_DOWNLOAD_ADDRESS");
		ITable filteredActualTable = DefaultColumnFilter.includedColumnsTable(actualTable, expectedTable.getTableMetaData().getColumns());
		SortedTable actualSortedTable = new SortedTable(filteredActualTable, new String[] {"address"});
		actualSortedTable.setUseComparable(true);

		Assertion.assertEquals(expectedSortedTable, actualSortedTable);
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void testDeleteNullSetFail() throws DeleteException {
		Set<CRLDownloadAddress> addrs = null;
		this.service.delete(addrs);
	}
}
