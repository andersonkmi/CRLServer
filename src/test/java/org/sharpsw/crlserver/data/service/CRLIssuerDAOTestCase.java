package org.sharpsw.crlserver.data.service;

import java.io.File;
import java.io.FileInputStream;
import java.util.Calendar;
import java.util.HashSet;
import java.util.Set;
import java.util.Vector;

import junit.framework.Assert;

import org.dbunit.Assertion;
import org.dbunit.dataset.IDataSet;
import org.dbunit.dataset.ITable;
import org.dbunit.dataset.ReplacementDataSet;
import org.dbunit.dataset.SortedTable;
import org.dbunit.dataset.filter.DefaultColumnFilter;
import org.dbunit.dataset.xml.FlatXmlDataSetBuilder;
import org.junit.Before;
import org.junit.Test;
import org.sharpsw.crlserver.data.CRLIssuer;

public class CRLIssuerDAOTestCase extends BaseDatabaseUnitTestCase {
	private CRLIssuerDAO service;
	
	@Before
	public void setUp() throws Exception {
		super.setUp();
		this.service = new CRLIssuerDAO(this.getSession(), this.getBatchSize(), true);
	}
	
	@Test
	public void testAddCRLIssuerAllFieldsOK() throws Exception {
		CRLIssuer issuer = new CRLIssuer();
		issuer.setName("CN=AC Secretaria da Receita Federal do Brasil, O=ICP-Brasil, C=BR");
		issuer.setDescription("Receita Federal do Brasil");
		
		Calendar ts = Calendar.getInstance();
		ts.set(Calendar.YEAR, 2012);
		ts.set(Calendar.MONTH, Calendar.JULY);
		ts.set(Calendar.DATE, 19);
		ts.set(Calendar.HOUR_OF_DAY, 11);
		ts.set(Calendar.MINUTE, 36);
		ts.set(Calendar.SECOND, 10);
		ts.set(Calendar.MILLISECOND, 999);
		issuer.setNextUpdateTimestamp(ts);
		
		this.service.save(issuer);
		
		StringBuffer dataSetFile = new StringBuffer();
		dataSetFile.append("config").append(File.separator).append("testAddCRLIssuerOK.xml");
		IDataSet expectedDataSet = new FlatXmlDataSetBuilder().build(new FileInputStream(dataSetFile.toString()));
		ITable expectedTable = expectedDataSet.getTable("CRL_ISSUER");
		SortedTable expectedSortedTable = new SortedTable(expectedTable, new String[] {"name"});
		expectedSortedTable.setUseComparable(true);
		
		IDataSet actualDataSet = this.databaseTester.getConnection().createDataSet();
		ITable actualTable = actualDataSet.getTable("crl_issuer");
		ITable filteredActualTable = DefaultColumnFilter.includedColumnsTable(actualTable, expectedTable.getTableMetaData().getColumns());
		SortedTable actualSortedTable = new SortedTable(filteredActualTable, new String[] {"name"});
		actualSortedTable.setUseComparable(true);

		Assertion.assertEquals(expectedSortedTable, actualSortedTable);
	}
	
	@Test
	public void testAddCRLIssuerRequiredFieldsOnlyOK() throws Exception {
		CRLIssuer issuer = new CRLIssuer();
		issuer.setName("CN=AC Secretaria da Receita Federal do Brasil, O=ICP-Brasil, C=BR");
		
		this.service.save(issuer);
		
		StringBuffer dataSetFile = new StringBuffer();
		dataSetFile.append("config").append(File.separator).append("testAddCRLIssuerRequiredFieldsOnlyOK.xml");
		IDataSet expectedDataSet = new FlatXmlDataSetBuilder().build(new FileInputStream(dataSetFile.toString()));
		ReplacementDataSet replacement = new ReplacementDataSet(expectedDataSet);
		replacement.addReplacementObject("[null]", null);
		ITable expectedTable = replacement.getTable("CRL_ISSUER");
		SortedTable expectedSortedTable = new SortedTable(expectedTable, new String[] {"name"});
		expectedSortedTable.setUseComparable(true);
		
		IDataSet dataSet = this.databaseTester.getConnection().createDataSet();
		ITable actualTable = dataSet.getTable("CRL_ISSUER");
		ITable filteredActualTable = DefaultColumnFilter.includedColumnsTable(actualTable, expectedTable.getTableMetaData().getColumns());
		SortedTable actualSortedTable = new SortedTable(filteredActualTable, new String[] {"name"});
		actualSortedTable.setUseComparable(true);

		Assertion.assertEquals(expectedSortedTable, actualSortedTable);
	}
	
	@Test(expected = CreateException.class)
	public void testAddExistingCRLIssuerFail() throws Exception {
		CRLIssuer issuer = new CRLIssuer();
		issuer.setName("CN=AC Secretaria da Receita Federal do Brasil, O=ICP-Brasil, C=BR");
		
		this.service.save(issuer);
		this.service.save(issuer);
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void testAddCRLIssuerNullArgFail() throws Exception {
		CRLIssuer issuer = null;
		this.service.save(issuer);
	}
	
	@Test
	public void testBatchAddCRLIssuerOK() throws Exception {
		Set<CRLIssuer> issuers = new HashSet<CRLIssuer>();
		
		CRLIssuer issuer1 = new CRLIssuer();
		issuer1.setName("CN=AC Secretaria da Receita Federal do Brasil, O=ICP-Brasil, C=BR");
		issuer1.setDescription("Secretaria da Receita Federal do Brasil");
		
		CRLIssuer issuer2 = new CRLIssuer();
		issuer2.setName("CN=Autoridade Certificadora Raiz Brasileira v1, OU=Instituto Nacional de Tecnologia da Informacao - ITI, O=ICP-Brasil, C=BR");
		issuer2.setDescription("Autoriadade Certificaddora Raiz Brasileira v1");
		
		issuers.add(issuer1);
		issuers.add(issuer2);
		
		this.service.save(issuers);
		
		StringBuffer dataSetFile = new StringBuffer();
		dataSetFile.append("config").append(File.separator).append("testBatchAddCRLIssuerOK.xml");
		IDataSet expectedDataSet = new FlatXmlDataSetBuilder().build(new FileInputStream(dataSetFile.toString()));
		ReplacementDataSet replacement = new ReplacementDataSet(expectedDataSet);
		replacement.addReplacementObject("[null]", null);
		ITable expectedTable = replacement.getTable("CRL_ISSUER");
		SortedTable expectedSortedTable = new SortedTable(expectedTable, new String[] {"name"});
		expectedSortedTable.setUseComparable(true);
		
		IDataSet dataSet = this.databaseTester.getConnection().createDataSet();
		ITable actualTable = dataSet.getTable("CRL_ISSUER");
		ITable filteredActualTable = DefaultColumnFilter.includedColumnsTable(actualTable, expectedTable.getTableMetaData().getColumns());
		SortedTable actualSortedTable = new SortedTable(filteredActualTable, new String[] {"name"});
		actualSortedTable.setUseComparable(true);

		Assertion.assertEquals(expectedSortedTable, actualSortedTable);
	}
	
	@Test
	public void testBulkAddCRLIssuerBatchSizeOverlimitOK() throws Exception {
		Set<CRLIssuer> issuers = new HashSet<CRLIssuer>();
		
		CRLIssuer issuer1 = new CRLIssuer();
		issuer1.setName("CN=AC Secretaria da Receita Federal do Brasil, O=ICP-Brasil, C=BR");
		issuer1.setDescription("Secretaria da Receita Federal do Brasil");
		
		CRLIssuer issuer2 = new CRLIssuer();
		issuer2.setName("CN=Autoridade Certificadora Raiz Brasileira v1, OU=Instituto Nacional de Tecnologia da Informacao - ITI, O=ICP-Brasil, C=BR");
		issuer2.setDescription("Autoriadade Certificaddora Raiz Brasileira v1");
		
		CRLIssuer issuer3 = new CRLIssuer();
		issuer3.setName("CN = AC SERASA RFB v1, OU = Secretaria da Receita Federal do Brasil - RFB, O = ICP-Brasil, C = BR");
		issuer3.setDescription("AC SERASA RFB v1");
		
		CRLIssuer issuer4 = new CRLIssuer();
		issuer4.setName("CN = Autoridade Certificadora Raiz Brasileira v2, OU = Instituto Nacional de Tecnologia da Informacao - ITI, O = ICP-Brasil, C = BR");
		issuer4.setDescription("Autoridade Certificadora Raiz Brasileira v2");
		
		CRLIssuer issuer5 = new CRLIssuer();
		issuer5.setName("CN = AC Secretaria da Receita Federal do Brasil v3, OU = Autoridade Certificadora Raiz Brasileira v2, O = ICP-Brasil, C = BR");
		issuer5.setDescription("AC Secretaria da Receita Federal do Brasil v3");
		
		CRLIssuer issuer6 = new CRLIssuer();
		issuer6.setName("CN = AC SERASA RFB v2, OU = Secretaria da Receita Federal do Brasil - RFB, O = ICP-Brasil, C = BR");
		issuer6.setDescription("AC SERASA RFB v2");
		
		issuers.add(issuer1);
		issuers.add(issuer2);
		issuers.add(issuer3);
		issuers.add(issuer4);
		issuers.add(issuer5);
		issuers.add(issuer6);
		
		this.service.save(issuers);
		
		StringBuffer dataSetFile = new StringBuffer();
		dataSetFile.append("config").append(File.separator).append("testBulkAddCRLIssuerBatchSizeOverlimitOK.xml");
		IDataSet expectedDataSet = new FlatXmlDataSetBuilder().build(new FileInputStream(dataSetFile.toString()));
		ReplacementDataSet replacement = new ReplacementDataSet(expectedDataSet);
		replacement.addReplacementObject("[null]", null);
		ITable expectedTable = replacement.getTable("CRL_ISSUER");
		SortedTable expectedSortedTable = new SortedTable(expectedTable, new String[] {"name"});
		expectedSortedTable.setUseComparable(true);
		
		IDataSet dataSet = this.databaseTester.getConnection().createDataSet();
		ITable actualTable = dataSet.getTable("CRL_ISSUER");
		ITable filteredActualTable = DefaultColumnFilter.includedColumnsTable(actualTable, expectedTable.getTableMetaData().getColumns());
		SortedTable actualSortedTable = new SortedTable(filteredActualTable, new String[] {"name"});
		actualSortedTable.setUseComparable(true);

		Assertion.assertEquals(expectedSortedTable, actualSortedTable);
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void testBatchAddNullCRLIssuerListFail() throws Exception {
		Set<CRLIssuer> elements = null;
		this.service.save(elements);
	}
	
	@Test(expected = CreateException.class)
	public void testBatchAddExistingCRLIssuerFail() throws Exception {
		Set<CRLIssuer> issuers = new HashSet<CRLIssuer>();
		
		CRLIssuer issuer1 = new CRLIssuer();
		issuer1.setName("CN=AC Secretaria da Receita Federal do Brasil, O=ICP-Brasil, C=BR");
		issuer1.setDescription("Secretaria da Receita Federal do Brasil");
		
		CRLIssuer issuer2 = new CRLIssuer();
		issuer2.setName("CN=Autoridade Certificadora Raiz Brasileira v1, OU=Instituto Nacional de Tecnologia da Informacao - ITI, O=ICP-Brasil, C=BR");
		issuer2.setDescription("Autoriadade Certificaddora Raiz Brasileira v1");
		
		CRLIssuer issuer3 = new CRLIssuer();
		issuer3.setName("CN=AC Secretaria da Receita Federal do Brasil, O=ICP-Brasil, C=BR");
		issuer3.setDescription("Secretaria da Receita Federal do Brasil");		
		
		issuers.add(issuer1);
		issuers.add(issuer2);
		issuers.add(issuer3);
		
		this.service.save(issuers);
	}
	
	@Test
	public void testFindAllCRLIssuersOK() throws Exception {
		CRLIssuer issuer1 = new CRLIssuer();
		issuer1.setName("CN=AC Secretaria da Receita Federal do Brasil, O=ICP-Brasil, C=BR");
		issuer1.setDescription("Secretaria da Receita Federal do Brasil");
		
		this.service.save(issuer1);
		
		CRLIssuer issuer2 = new CRLIssuer();
		issuer2.setName("CN=Autoridade Certificadora Raiz Brasileira v1, OU=Instituto Nacional de Tecnologia da Informacao - ITI, O=ICP-Brasil, C=BR");
		issuer2.setDescription("Autoridade Certificadora Raiz Brasileira v1");
		
		this.service.save(issuer2);
		
		Set<CRLIssuer> results = this.service.findAll();
		Assert.assertEquals(3, results.size());
	}
	
	@Test
	public void testFindCRLIssuerByIdOK() throws Exception {
		CRLIssuer issuer = new CRLIssuer();
		issuer.setName("CN=AC Secretaria da Receita Federal do Brasil, O=ICP-Brasil, C=BR");
		issuer.setDescription("Receita Federal do Brasil");
		
		this.service.save(issuer);
		
		String id = this.service.getLastCreatedIds().firstElement();
		CRLIssuer retrieved = this.service.findById(id);
		
		Assert.assertEquals(issuer, retrieved);
	}
	
	@Test(expected = ReadException.class)
	public void testFindCRLIssuerByIdFail() throws Exception {
		CRLIssuer issuer = new CRLIssuer();
		issuer.setName("CN=AC Secretaria da Receita Federal do Brasil, O=ICP-Brasil, C=BR");
		issuer.setDescription("Receita Federal do Brasil");
		
		this.service.save(issuer);
		
		@SuppressWarnings("unused")
		CRLIssuer retrieved = this.service.findById("fake-id");
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void testFindCRLIssuerByNullIdFail() throws Exception {
		String id = null;
		this.service.findById(id);
	}
	
	@Test
	public void testFindCRLIssuerBasedOnNameOK() throws Exception {
		CRLIssuer issuer1 = new CRLIssuer();
		issuer1.setName("CN=AC Secretaria da Receita Federal do Brasil, O=ICP-Brasil, C=BR");
		issuer1.setDescription("Secretaria da Receita Federal do Brasil");
		
		this.service.save(issuer1);
		
		CRLIssuer issuer2 = new CRLIssuer();
		issuer2.setName("CN=Autoridade Certificadora Raiz Brasileira v1, OU=Instituto Nacional de Tecnologia da Informacao - ITI, O=ICP-Brasil, C=BR");
		issuer2.setDescription("Autoridade Certificadora Raiz Brasileira v1");
		
		this.service.save(issuer2);
		
		Set<CRLIssuer> results = this.service.findIssuersBasedOnName("Receita");
		Assert.assertEquals(1, results.size());
		
		Assert.assertTrue(results.contains(issuer1));
	}
	
	@Test
	public void testFindCRLIssuerBasedOnNameNoMatchOK() throws Exception {
		CRLIssuer issuer1 = new CRLIssuer();
		issuer1.setName("CN=AC Secretaria da Receita Federal do Brasil, O=ICP-Brasil, C=BR");
		issuer1.setDescription("Secretaria da Receita Federal do Brasil");
		
		this.service.save(issuer1);
		
		CRLIssuer issuer2 = new CRLIssuer();
		issuer2.setName("CN=Autoridade Certificadora Raiz Brasileira v1, OU=Instituto Nacional de Tecnologia da Informacao - ITI, O=ICP-Brasil, C=BR");
		issuer2.setDescription("Autoridade Certificadora Raiz Brasileira v1");
		
		this.service.save(issuer2);
		
		Set<CRLIssuer> results = this.service.findIssuersBasedOnName("UnitTesting");
		Assert.assertEquals(0, results.size());
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void testFindCRLIssuerBasedOnNullNameFail() throws Exception {
		this.service.findIssuersBasedOnName(null);
	}
	
	@Test
	public void testFindCRLIssuerUsingInvalidNameFail() throws Exception {		
		Set<CRLIssuer> elements = this.service.findIssuersBasedOnName("teste");
		Assert.assertEquals(0, elements.size());
	}
	
	@Test
	public void testUpdateCRLIssuerDescriptionOK() throws Exception {
		CRLIssuer issuer1 = new CRLIssuer();
		issuer1.setName("CN=AC Secretaria da Receita Federal do Brasil, O=ICP-Brasil, C=BR");
		issuer1.setDescription("Secretaria da Receita Federal do Brasil");
		
		this.service.save(issuer1);
		
		Vector<String> ids = this.service.getLastCreatedIds();
		
		CRLIssuer retrieved = this.service.findById(ids.firstElement());
		retrieved.setDescription("Changed description");
		
		this.service.update(retrieved);
		
		CRLIssuer other = this.service.findById(ids.firstElement());
		
		Assert.assertEquals(retrieved, other);
	}
	
	@Test
	public void testUpdateCRLIssuerNameOK() throws Exception {
		CRLIssuer issuer1 = new CRLIssuer();
		issuer1.setName("CN=AC Secretaria da Receita Federal do Brasil, O=ICP-Brasil, C=BR");
		issuer1.setDescription("Secretaria da Receita Federal do Brasil");
		
		this.service.save(issuer1);
		
		Vector<String> ids = this.service.getLastCreatedIds();
		
		CRLIssuer retrieved = this.service.findById(ids.firstElement());
		retrieved.setName("CN=AC Secretaria da Receita Federal, O=ICP-Brasil, C=BR");
		Calendar ts = Calendar.getInstance();
		ts.set(Calendar.YEAR, 2012);
		ts.set(Calendar.MONTH, Calendar.JULY);
		ts.set(Calendar.DATE, 21);
		ts.set(Calendar.HOUR_OF_DAY, 18);
		ts.set(Calendar.MINUTE, 48);
		ts.set(Calendar.SECOND, 10);
		ts.set(Calendar.MILLISECOND, 999);
		retrieved.setNextUpdateTimestamp(ts);
		
		this.service.update(retrieved);
		CRLIssuer other = this.service.findById(ids.firstElement());
		
		Assert.assertEquals(retrieved, other);
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void testUpdateCRLIssuerNullIdFail() throws Exception {
		this.service.update(null);
	}
	
	@Test
	public void testDeleteCRLIssuerOK() throws Exception {
		CRLIssuer issuer = new CRLIssuer();
		issuer.setName("CN=AC Secretaria da Receita Federal do Brasil, O=ICP-Brasil, C=BR");
		issuer.setDescription("Secretaria da Receita Federal do Brasil");
		
		this.service.save(issuer);
		
		CRLIssuer item = this.service.findById("1111111111111111111111111111111111111111");
		this.service.delete(item);
		
		StringBuffer dataSetFile = new StringBuffer();
		dataSetFile.append("config").append(File.separator).append("testDeleteCRLIssuerOK.xml");
		IDataSet expectedDataSet = new FlatXmlDataSetBuilder().build(new FileInputStream(dataSetFile.toString()));
		ReplacementDataSet replacement = new ReplacementDataSet(expectedDataSet);
		replacement.addReplacementObject("[null]", null);
		ITable expectedTable = replacement.getTable("CRL_ISSUER");
		
		IDataSet actualDataSet = this.databaseTester.getConnection().createDataSet();
		ITable actualTable = actualDataSet.getTable("crl_issuer");
		ITable filteredActualTable = DefaultColumnFilter.includedColumnsTable(actualTable, expectedTable.getTableMetaData().getColumns());
		
		Assertion.assertEquals(expectedTable, filteredActualTable);
	}
	
	@Test
	public void testDeleteInvalidCRLIssuerOK() throws Exception {
		CRLIssuer issuer = new CRLIssuer();
		this.service.delete(issuer);
		
		StringBuffer dataSetFile = new StringBuffer();
		dataSetFile.append("config").append(File.separator).append("testDeleteInvalidCRLIssuerOK.xml");
		IDataSet expectedDataSet = new FlatXmlDataSetBuilder().build(new FileInputStream(dataSetFile.toString()));
		ReplacementDataSet replacement = new ReplacementDataSet(expectedDataSet);
		replacement.addReplacementObject("[null]", null);
		ITable expectedTable = replacement.getTable("CRL_ISSUER");
		
		IDataSet actualDataSet = this.databaseTester.getConnection().createDataSet();
		ITable actualTable = actualDataSet.getTable("crl_issuer");
		ITable filteredActualTable = DefaultColumnFilter.includedColumnsTable(actualTable, expectedTable.getTableMetaData().getColumns());
		
		Assertion.assertEquals(expectedTable, filteredActualTable);
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void testDeleteCRLIssuerNullIdFail() throws Exception {
		CRLIssuer issuer = null;
		this.service.delete(issuer);
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void testDeleteCRLIssuerNullSetFail() throws Exception {
		Set<CRLIssuer> items = null;
		this.service.delete(items);
	}
	
	@Test
	public void testDeleteAllIssuersOK() throws Exception {
		CRLIssuer issuer1 = new CRLIssuer();
		issuer1.setName("CN=AC Secretaria da Receita Federal do Brasil, O=ICP-Brasil, C=BR");
		issuer1.setDescription("Secretaria da Receita Federal do Brasil");
		
		this.service.save(issuer1);
		
		CRLIssuer issuer2 = new CRLIssuer();
		issuer2.setName("CN=Autoridade Certificadora Raiz Brasileira v1, OU=Instituto Nacional de Tecnologia da Informacao - ITI, O=ICP-Brasil, C=BR");
		issuer2.setDescription("Autoridade Certificadora Raiz Brasileira v1");
		
		this.service.save(issuer2);
		
		CRLIssuer item1 = new CRLIssuer();
		item1.setId("1111111111111111111111111111111111111111");
		item1.setName("Fake");
		item1.setDescription("Fake");
		
		Set<CRLIssuer> issuers = new HashSet<CRLIssuer>();
		issuers.add(item1);
		issuers.add(issuer1);
		
		this.service.delete(issuers);
		
		StringBuffer dataSetFile = new StringBuffer();
		dataSetFile.append("config").append(File.separator).append("testDeleteAllIssuersOK.xml");
		IDataSet expectedDataSet = new FlatXmlDataSetBuilder().build(new FileInputStream(dataSetFile.toString()));
		ReplacementDataSet replacement = new ReplacementDataSet(expectedDataSet);
		replacement.addReplacementObject("[null]", null);
		ITable expectedTable = replacement.getTable("CRL_ISSUER");
		
		IDataSet actualDataSet = this.databaseTester.getConnection().createDataSet();
		ITable actualTable = actualDataSet.getTable("crl_issuer");
		ITable filteredActualTable = DefaultColumnFilter.includedColumnsTable(actualTable, expectedTable.getTableMetaData().getColumns());
		
		Assertion.assertEquals(expectedTable, filteredActualTable);
	}
}
