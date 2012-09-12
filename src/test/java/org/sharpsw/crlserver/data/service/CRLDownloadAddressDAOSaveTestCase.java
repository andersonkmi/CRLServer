package org.sharpsw.crlserver.data.service;

import java.io.File;
import java.io.FileInputStream;
import java.util.HashSet;
import java.util.Set;

import junit.framework.Assert;

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

public class CRLDownloadAddressDAOSaveTestCase extends BaseCRLDownloadAddressDBSaveTestCase {
	private CRLDownloadAddressDAO service;
	
	@Before
	public void setUp() throws Exception {
		super.setUp();
		this.service = new CRLDownloadAddressDAO(this.getSession(), this.getBatchSize(), true);
	}

	@Test
	public void testAddCrlDownloadAddressOK() throws Exception {
		CRLIssuer issuer = new CRLIssuer();
		issuer.setId("1111111111111111111111111111111111111111");
		issuer.setName("CN=AC Secretaria da Receita Federal do Brasil, O=ICP-Brasil, C=BR");
		issuer.setDescription("Secretaria da Receita Federal do Brasil");
		
		CRLDownloadAddressCompositePrimaryKey key = new CRLDownloadAddressCompositePrimaryKey(issuer, "http://www.teste1.com.br/teste.crl");
		CRLDownloadAddress address = new CRLDownloadAddress(key);
		address.setDescription("URL de teste");			

		this.service.save(address);
		
		StringBuffer dataSetFile = new StringBuffer();
		dataSetFile.append("config").append(File.separator).append("CRLDownloadAddressDAO").append(File.separator).append("testAddCrlDownloadAddressOK.xml");
		IDataSet expectedDataSet = new FlatXmlDataSetBuilder().build(new FileInputStream(dataSetFile.toString()));
		ITable expectedTable = expectedDataSet.getTable("CRL_DOWNLOAD_ADDRESS");
		
		IDataSet actualDataSet = this.databaseTester.getConnection().createDataSet();
		ITable actualTable = actualDataSet.getTable("CRL_DOWNLOAD_ADDRESS");
		ITable filteredActualTable = DefaultColumnFilter.includedColumnsTable(actualTable, expectedTable.getTableMetaData().getColumns());

		Assertion.assertEquals(expectedTable, filteredActualTable);
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void testAddNullCrlDownloadAddressFail() throws Exception {
		CRLDownloadAddress entity = null;
		this.service.save(entity);
	}
	
	@Test
	public void testBulkAddCrlDownloadAddressOK() throws Exception {
		Set<CRLDownloadAddress> addresses = new HashSet<CRLDownloadAddress>();

		CRLIssuer issuer = new CRLIssuer();
		issuer.setId("1111111111111111111111111111111111111111");
		issuer.setName("CN=AC Secretaria da Receita Federal do Brasil, O=ICP-Brasil, C=BR");
		issuer.setDescription("Secretaria da Receita Federal do Brasil");
		
		CRLDownloadAddressCompositePrimaryKey key1 = new CRLDownloadAddressCompositePrimaryKey(issuer, "http://www.url1.com/test.crl");
		CRLDownloadAddress addr1 = new CRLDownloadAddress(key1);
		addr1.setDescription("URL de teste 1");
		
		addresses.add(addr1);
		
		CRLDownloadAddressCompositePrimaryKey key2 = new CRLDownloadAddressCompositePrimaryKey(issuer, "http://www.url2.com/test.crl");
		CRLDownloadAddress addr2 = new CRLDownloadAddress(key2);
		addr2.setDescription("URL de teste 2");
		
		addresses.add(addr2);
		
		this.service.save(addresses);
		
		StringBuffer dataSetFile = new StringBuffer();
		dataSetFile.append("config").append(File.separator).append("CRLDownloadAddressDAO").append(File.separator).append("testBulkAddCrlDownloadAddressOK.xml");
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
	
	
	@Test
	public void testBatchLimitBulkAddCrlDownloadAddressOK() throws Exception {
		Set<CRLDownloadAddress> addresses = new HashSet<CRLDownloadAddress>();

		CRLIssuer issuer = new CRLIssuer();
		issuer.setId("1111111111111111111111111111111111111111");
		issuer.setName("CN=AC Secretaria da Receita Federal do Brasil, O=ICP-Brasil, C=BR");
		issuer.setDescription("Secretaria da Receita Federal do Brasil");
		
		CRLDownloadAddressCompositePrimaryKey key1 = new CRLDownloadAddressCompositePrimaryKey(issuer, "http://www.url1.com/test.crl");
		CRLDownloadAddress addr1 = new CRLDownloadAddress(key1);
		addr1.setDescription("URL de teste 1");
		
		CRLDownloadAddressCompositePrimaryKey key2 = new CRLDownloadAddressCompositePrimaryKey(issuer, "http://www.url2.com/test.crl");
		CRLDownloadAddress addr2 = new CRLDownloadAddress(key2);
		addr2.setDescription("URL de teste 2");		
		
		CRLDownloadAddressCompositePrimaryKey key3 = new CRLDownloadAddressCompositePrimaryKey(issuer, "http://www.url3.com/test.crl");
		CRLDownloadAddress addr3 = new CRLDownloadAddress(key3);
		addr3.setDescription("URL de teste 3");		
		
		
		CRLDownloadAddressCompositePrimaryKey key4 = new CRLDownloadAddressCompositePrimaryKey(issuer, "http://www.url4.com/test.crl");
		CRLDownloadAddress addr4 = new CRLDownloadAddress(key4);
		addr4.setDescription("URL de teste 4");		
		
		CRLDownloadAddressCompositePrimaryKey key5 = new CRLDownloadAddressCompositePrimaryKey(issuer, "http://www.url5.com/test.crl");
		CRLDownloadAddress addr5 = new CRLDownloadAddress(key5);
		addr5.setDescription("URL de teste 5");	
		
		
		addresses.add(addr1);
		addresses.add(addr2);
		addresses.add(addr3);
		addresses.add(addr4);
		addresses.add(addr5);
		
		this.service.save(addresses);
		
		StringBuffer dataSetFile = new StringBuffer();
		dataSetFile.append("config").append(File.separator).append("CRLDownloadAddressDAO").append(File.separator).append("testBatchLimitBulkAddCrlDownloadAddressOK.xml");
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
	
	@Test
	public void testBatchOverLimitBulkAddCrlDownloadAddressOK() throws Exception {
		Set<CRLDownloadAddress> addresses = new HashSet<CRLDownloadAddress>();

		CRLIssuer issuer = new CRLIssuer();
		issuer.setId("1111111111111111111111111111111111111111");
		issuer.setName("CN=AC Secretaria da Receita Federal do Brasil, O=ICP-Brasil, C=BR");
		issuer.setDescription("Secretaria da Receita Federal do Brasil");
		
		CRLDownloadAddressCompositePrimaryKey key1 = new CRLDownloadAddressCompositePrimaryKey(issuer, "http://www.url1.com/test.crl");
		CRLDownloadAddress addr1 = new CRLDownloadAddress(key1);
		addr1.setDescription("URL de teste 1");
		
		CRLDownloadAddressCompositePrimaryKey key2 = new CRLDownloadAddressCompositePrimaryKey(issuer, "http://www.url2.com/test.crl");
		CRLDownloadAddress addr2 = new CRLDownloadAddress(key2);
		addr2.setDescription("URL de teste 2");		
		
		CRLDownloadAddressCompositePrimaryKey key3 = new CRLDownloadAddressCompositePrimaryKey(issuer, "http://www.url3.com/test.crl");
		CRLDownloadAddress addr3 = new CRLDownloadAddress(key3);
		addr3.setDescription("URL de teste 3");		
		
		
		CRLDownloadAddressCompositePrimaryKey key4 = new CRLDownloadAddressCompositePrimaryKey(issuer, "http://www.url4.com/test.crl");
		CRLDownloadAddress addr4 = new CRLDownloadAddress(key4);
		addr4.setDescription("URL de teste 4");		
		
		CRLDownloadAddressCompositePrimaryKey key5 = new CRLDownloadAddressCompositePrimaryKey(issuer, "http://www.url5.com/test.crl");
		CRLDownloadAddress addr5 = new CRLDownloadAddress(key5);
		addr5.setDescription("URL de teste 5");	
		
		CRLDownloadAddressCompositePrimaryKey key6 = new CRLDownloadAddressCompositePrimaryKey(issuer, "http://www.url6.com/test.crl");
		CRLDownloadAddress addr6 = new CRLDownloadAddress(key6);
		addr6.setDescription("URL de teste 6");	
		
		addresses.add(addr1);
		addresses.add(addr2);
		addresses.add(addr3);
		addresses.add(addr4);
		addresses.add(addr5);
		addresses.add(addr6);
		
		this.service.save(addresses);
		
		StringBuffer dataSetFile = new StringBuffer();
		dataSetFile.append("config").append(File.separator).append("CRLDownloadAddressDAO").append(File.separator).append("testBatchOverLimitBulkAddCrlDownloadAddressOK.xml");
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
	public void testBulkAddNullArgumentFail() throws Exception {
		Set<CRLDownloadAddress> elements = null;
		this.service.save(elements);
	}
	
	@Test(expected = CreateException.class)
	public void testAddDuplicateCrlDownloadAddressFail() throws Exception {
		CRLIssuer issuer = new CRLIssuer();
		issuer.setId("1111111111111111111111111111111111111111");
		issuer.setName("CN=AC Secretaria da Receita Federal do Brasil, O=ICP-Brasil, C=BR");
		issuer.setDescription("Secretaria da Receita Federal do Brasil");

		CRLDownloadAddressCompositePrimaryKey key = new CRLDownloadAddressCompositePrimaryKey(issuer, "http://www.url.com");
		CRLDownloadAddress address = new CRLDownloadAddress(key);
		
		this.service.save(address);
		this.service.save(address);
	}
	
	@Test
	public void testBulkAddDuplicateCrlDownloadAddressFail() throws Exception {
		Set<CRLDownloadAddress> addresses = new HashSet<CRLDownloadAddress>();
		
		CRLIssuer issuer = new CRLIssuer();
		issuer.setId("1111111111111111111111111111111111111111");
		issuer.setName("CN=AC Secretaria da Receita Federal do Brasil, O=ICP-Brasil, C=BR");
		issuer.setDescription("Secretaria da Receita Federal do Brasil");
		
		CRLDownloadAddressCompositePrimaryKey key1 = new CRLDownloadAddressCompositePrimaryKey(issuer, "http://www.url1.com/test.crl");
		CRLDownloadAddress addr1 = new CRLDownloadAddress(key1);
		addr1.setDescription("URL de teste 1");
		
		CRLDownloadAddressCompositePrimaryKey key2 = new CRLDownloadAddressCompositePrimaryKey(issuer, "http://www.url2.com/test.crl");
		CRLDownloadAddress addr2 = new CRLDownloadAddress(key2);
		addr2.setDescription("URL de teste 2");		
		
		CRLDownloadAddressCompositePrimaryKey key3 = new CRLDownloadAddressCompositePrimaryKey(issuer, "http://www.url3.com/test.crl");
		CRLDownloadAddress addr3 = new CRLDownloadAddress(key3);
		addr3.setDescription("URL de teste 3");		
		
		
		CRLDownloadAddressCompositePrimaryKey key4 = new CRLDownloadAddressCompositePrimaryKey(issuer, "http://www.url4.com/test.crl");
		CRLDownloadAddress addr4 = new CRLDownloadAddress(key4);
		addr4.setDescription("URL de teste 4");		
		
		CRLDownloadAddressCompositePrimaryKey key5 = new CRLDownloadAddressCompositePrimaryKey(issuer, "http://www.url5.com/test.crl");
		CRLDownloadAddress addr5 = new CRLDownloadAddress(key5);
		addr5.setDescription("URL de teste 5");	
		
		CRLDownloadAddressCompositePrimaryKey key6 = new CRLDownloadAddressCompositePrimaryKey(issuer, "http://www.url5.com/test.crl");
		CRLDownloadAddress addr6 = new CRLDownloadAddress(key6);
		addr6.setDescription("URL de teste 6");	
		
		addresses.add(addr1);
		addresses.add(addr2);
		addresses.add(addr3);
		addresses.add(addr4);
		addresses.add(addr5);
		addresses.add(addr6);
		
		try {
			this.service.save(addresses);
			Assert.fail("Exception was not thrown");
		} catch (CreateException exception) {
			
		}
		
		StringBuffer dataSetFile = new StringBuffer();
		dataSetFile.append("config").append(File.separator).append("CRLDownloadAddressDAO").append(File.separator).append("testBulkAddDuplicateCrlDownloadAddressFail.xml");
		IDataSet expectedDataSet = new FlatXmlDataSetBuilder().build(new FileInputStream(dataSetFile.toString()));
		ITable expectedTable = expectedDataSet.getTable("CRL_DOWNLOAD_ADDRESS");
		
		IDataSet actualDataSet = this.databaseTester.getConnection().createDataSet();
		ITable actualTable = actualDataSet.getTable("CRL_DOWNLOAD_ADDRESS");
		ITable filteredActualTable = DefaultColumnFilter.includedColumnsTable(actualTable, expectedTable.getTableMetaData().getColumns());

		Assertion.assertEquals(expectedTable, filteredActualTable);
	}
}
