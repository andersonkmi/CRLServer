package org.sharpsw.crlserver.data.service;

import java.io.File;
import java.io.FileInputStream;
import java.util.Calendar;
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
import org.sharpsw.crlserver.data.CRLIssuer;
import org.sharpsw.crlserver.data.UpdateLog;

public class UpdateLogDAOSaveTestCase extends BaseUpdateLogDAODefaultTestCase {
	private UpdateLogDAO service;
	
	@Before
	public void setUp() throws Exception {
		super.setUp();
		this.service = new UpdateLogDAO(this.getSession(), this.getBatchSize(), true);
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void testCreateUpdateLogNullArgFail() throws CreateException {
		UpdateLog entry = null;
		this.service.save(entry);
	}
	
	@Test
	public void testCreateUpdateLogOK() throws Exception {
		CRLIssuer issuer = new CRLIssuer();
		issuer.setId("1111111111111111111111111111111111111111");
		issuer.setName("CN=AC Secretaria da Receita Federal do Brasil, O=ICP-Brasil, C=BR");
		
		UpdateLog log = new UpdateLog();
		log.setId("1111111111111111111111111111111111111111");
		log.setCrlIssuer(issuer);
		log.setUpdateStatus("S");
		Calendar ts = Calendar.getInstance();
		ts.set(Calendar.YEAR, 2012);
		ts.set(Calendar.MONTH, Calendar.AUGUST);
		ts.set(Calendar.DATE, 8);
		ts.set(Calendar.HOUR_OF_DAY, 15);
		ts.set(Calendar.MINUTE, 27);
		ts.set(Calendar.SECOND, 10);
		ts.set(Calendar.MILLISECOND, 0);
		log.setLastUpdateTimestamp(ts);
		
		this.service.save(log);
		
		StringBuffer dataSetFile = new StringBuffer();
		dataSetFile.append("config").append(File.separator).append("UpdateLogDAO").append(File.separator).append("testCreateUpdateLogOK.xml");
		IDataSet expectedDataSet = new FlatXmlDataSetBuilder().build(new FileInputStream(dataSetFile.toString()));
		ITable expectedTable = expectedDataSet.getTable("UPDATE_LOG");
		
		IDataSet actualDataSet = this.databaseTester.getConnection().createDataSet();
		ITable actualTable = actualDataSet.getTable("UPDATE_LOG");
		ITable filteredActualTable = DefaultColumnFilter.includedColumnsTable(actualTable, expectedTable.getTableMetaData().getColumns());

		Assertion.assertEquals(expectedTable, filteredActualTable);
	}
	
	@Test(expected = CreateException.class)
	public void testCreateDuplicateEntriesFail() throws CreateException {
		CRLIssuer issuer = new CRLIssuer();
		issuer.setId("1111111111111111111111111111111111111111");
		issuer.setName("CN=AC Secretaria da Receita Federal do Brasil, O=ICP-Brasil, C=BR");
		
		UpdateLog log = new UpdateLog();
		log.setId("1111111111111111111111111111111111111111");
		log.setCrlIssuer(issuer);
		log.setUpdateStatus("S");
		Calendar ts = Calendar.getInstance();
		ts.set(Calendar.YEAR, 2012);
		ts.set(Calendar.MONTH, Calendar.AUGUST);
		ts.set(Calendar.DATE, 8);
		ts.set(Calendar.HOUR_OF_DAY, 15);
		ts.set(Calendar.MINUTE, 27);
		ts.set(Calendar.SECOND, 10);
		ts.set(Calendar.MILLISECOND, 0);
		log.setLastUpdateTimestamp(ts);
		
		UpdateLog item2 = new UpdateLog();
		item2.setId("1111111111111111111111111111111111111111");
		item2.setCrlIssuer(issuer);
		item2.setUpdateStatus("S");
		item2.setLastUpdateTimestamp(Calendar.getInstance());
		
		this.service.save(log);
		this.service.save(item2);
	}
	
	@Test
	public void testBulkCreateOK() throws Exception {
		CRLIssuer issuer = new CRLIssuer();
		issuer.setId("1111111111111111111111111111111111111111");
		issuer.setName("CN=AC Secretaria da Receita Federal do Brasil, O=ICP-Brasil, C=BR");
		
		UpdateLog item1 = new UpdateLog();
		item1.setId("1111111111111111111111111111111111111111");
		item1.setCrlIssuer(issuer);
		item1.setUpdateStatus("S");
		Calendar ts = Calendar.getInstance();
		ts.set(Calendar.YEAR, 2012);
		ts.set(Calendar.MONTH, Calendar.AUGUST);
		ts.set(Calendar.DATE, 8);
		ts.set(Calendar.HOUR_OF_DAY, 15);
		ts.set(Calendar.MINUTE, 27);
		ts.set(Calendar.SECOND, 10);
		ts.set(Calendar.MILLISECOND, 0);
		item1.setLastUpdateTimestamp(ts);
		
		UpdateLog item2 = new UpdateLog();
		item2.setId("2222222222222222222222222222222222222222");
		item2.setCrlIssuer(issuer);
		item2.setUpdateStatus("S");
		Calendar ts2 = Calendar.getInstance();
		ts2.set(Calendar.YEAR, 2012);
		ts2.set(Calendar.MONTH, Calendar.AUGUST);
		ts2.set(Calendar.DATE, 7);
		ts2.set(Calendar.HOUR_OF_DAY, 10);
		ts2.set(Calendar.MINUTE, 10);
		ts2.set(Calendar.SECOND, 10);
		ts2.set(Calendar.MILLISECOND, 0);		
		item2.setLastUpdateTimestamp(ts2);
		
		Set<UpdateLog> updates = new HashSet<UpdateLog>();
		updates.add(item1);
		updates.add(item2);
		
		this.service.save(updates);
		
		StringBuffer dataSetFile = new StringBuffer();
		dataSetFile.append("config").append(File.separator).append("UpdateLogDAO").append(File.separator).append("testBulkCreateOK.xml");
		IDataSet expectedDataSet = new FlatXmlDataSetBuilder().build(new FileInputStream(dataSetFile.toString()));
		ITable expectedTable = expectedDataSet.getTable("UPDATE_LOG");
		SortedTable sortedExpected = new SortedTable(expectedTable, new String[]{"id"});
		sortedExpected.setUseComparable(true);
		
		IDataSet actualDataSet = this.databaseTester.getConnection().createDataSet();
		ITable actualTable = actualDataSet.getTable("UPDATE_LOG");
		ITable filteredActualTable = DefaultColumnFilter.includedColumnsTable(actualTable, expectedTable.getTableMetaData().getColumns());
		SortedTable sortedActual = new SortedTable(filteredActualTable, new String[]{"id"});
		sortedActual.setUseComparable(true);
		
		Assertion.assertEquals(sortedExpected, sortedActual);
	}
	
	@Test
	public void testBulkCreateBatchSizeOK() throws Exception {
		CRLIssuer issuer = new CRLIssuer();
		issuer.setId("1111111111111111111111111111111111111111");
		issuer.setName("CN=AC Secretaria da Receita Federal do Brasil, O=ICP-Brasil, C=BR");
		
		UpdateLog item1 = new UpdateLog();
		item1.setId("1111111111111111111111111111111111111111");
		item1.setCrlIssuer(issuer);
		item1.setUpdateStatus("S");
		Calendar ts = Calendar.getInstance();
		ts.set(Calendar.YEAR, 2012);
		ts.set(Calendar.MONTH, Calendar.AUGUST);
		ts.set(Calendar.DATE, 8);
		ts.set(Calendar.HOUR_OF_DAY, 15);
		ts.set(Calendar.MINUTE, 27);
		ts.set(Calendar.SECOND, 10);
		ts.set(Calendar.MILLISECOND, 0);
		item1.setLastUpdateTimestamp(ts);
		
		UpdateLog item2 = new UpdateLog();
		item2.setId("2222222222222222222222222222222222222222");
		item2.setCrlIssuer(issuer);
		item2.setUpdateStatus("S");
		Calendar ts2 = Calendar.getInstance();
		ts2.set(Calendar.YEAR, 2012);
		ts2.set(Calendar.MONTH, Calendar.AUGUST);
		ts2.set(Calendar.DATE, 7);
		ts2.set(Calendar.HOUR_OF_DAY, 10);
		ts2.set(Calendar.MINUTE, 10);
		ts2.set(Calendar.SECOND, 10);
		ts2.set(Calendar.MILLISECOND, 0);		
		item2.setLastUpdateTimestamp(ts2);
		
		UpdateLog item3 = new UpdateLog();
		item3.setId("3333333333333333333333333333333333333333");
		item3.setCrlIssuer(issuer);
		item3.setUpdateStatus("S");
		Calendar ts3 = Calendar.getInstance();
		ts3.set(Calendar.YEAR, 2012);
		ts3.set(Calendar.MONTH, Calendar.AUGUST);
		ts3.set(Calendar.DATE, 7);
		ts3.set(Calendar.HOUR_OF_DAY, 11);
		ts3.set(Calendar.MINUTE, 10);
		ts3.set(Calendar.SECOND, 10);
		ts3.set(Calendar.MILLISECOND, 0);		
		item3.setLastUpdateTimestamp(ts3);
		
		UpdateLog item4 = new UpdateLog();
		item4.setId("4444444444444444444444444444444444444444");
		item4.setCrlIssuer(issuer);
		item4.setUpdateStatus("S");
		Calendar ts4 = Calendar.getInstance();
		ts4.set(Calendar.YEAR, 2012);
		ts4.set(Calendar.MONTH, Calendar.AUGUST);
		ts4.set(Calendar.DATE, 7);
		ts4.set(Calendar.HOUR_OF_DAY, 12);
		ts4.set(Calendar.MINUTE, 10);
		ts4.set(Calendar.SECOND, 10);
		ts4.set(Calendar.MILLISECOND, 0);		
		item4.setLastUpdateTimestamp(ts4);
		
		UpdateLog item5 = new UpdateLog();
		item5.setId("5555555555555555555555555555555555555555");
		item5.setCrlIssuer(issuer);
		item5.setUpdateStatus("S");
		Calendar ts5 = Calendar.getInstance();
		ts5.set(Calendar.YEAR, 2012);
		ts5.set(Calendar.MONTH, Calendar.AUGUST);
		ts5.set(Calendar.DATE, 7);
		ts5.set(Calendar.HOUR_OF_DAY, 13);
		ts5.set(Calendar.MINUTE, 10);
		ts5.set(Calendar.SECOND, 10);
		ts5.set(Calendar.MILLISECOND, 0);		
		item5.setLastUpdateTimestamp(ts5);
		
		Set<UpdateLog> updates = new HashSet<UpdateLog>();
		updates.add(item1);
		updates.add(item2);
		updates.add(item3);
		updates.add(item4);
		updates.add(item5);
		this.service.save(updates);
		
		StringBuffer dataSetFile = new StringBuffer();
		dataSetFile.append("config").append(File.separator).append("UpdateLogDAO").append(File.separator).append("testBulkCreateBatchSizeOK.xml");
		IDataSet expectedDataSet = new FlatXmlDataSetBuilder().build(new FileInputStream(dataSetFile.toString()));
		ITable expectedTable = expectedDataSet.getTable("UPDATE_LOG");
		SortedTable sortedExpected = new SortedTable(expectedTable, new String[]{"id"});
		sortedExpected.setUseComparable(true);
		
		IDataSet actualDataSet = this.databaseTester.getConnection().createDataSet();
		ITable actualTable = actualDataSet.getTable("UPDATE_LOG");
		ITable filteredActualTable = DefaultColumnFilter.includedColumnsTable(actualTable, expectedTable.getTableMetaData().getColumns());
		SortedTable sortedActual = new SortedTable(filteredActualTable, new String[]{"id"});
		sortedActual.setUseComparable(true);
		
		Assertion.assertEquals(sortedExpected, sortedActual);
	}
	
	@Test(expected = CreateException.class)
	public void testBulkCreateDuplicateEntriesFail() throws CreateException {
		CRLIssuer issuer = new CRLIssuer();
		issuer.setId("1111111111111111111111111111111111111111");
		issuer.setName("CN=AC Secretaria da Receita Federal do Brasil, O=ICP-Brasil, C=BR");
		
		UpdateLog item1 = new UpdateLog();
		item1.setId("1111111111111111111111111111111111111111");
		item1.setCrlIssuer(issuer);
		item1.setUpdateStatus("S");
		Calendar ts = Calendar.getInstance();
		ts.set(Calendar.YEAR, 2012);
		ts.set(Calendar.MONTH, Calendar.AUGUST);
		ts.set(Calendar.DATE, 8);
		ts.set(Calendar.HOUR_OF_DAY, 15);
		ts.set(Calendar.MINUTE, 27);
		ts.set(Calendar.SECOND, 10);
		ts.set(Calendar.MILLISECOND, 0);
		item1.setLastUpdateTimestamp(ts);
		
		UpdateLog item2 = new UpdateLog();
		item2.setId("1111111111111111111111111111111111111111");
		item2.setCrlIssuer(issuer);
		item2.setUpdateStatus("S");
		Calendar ts2 = Calendar.getInstance();
		ts2.set(Calendar.YEAR, 2012);
		ts2.set(Calendar.MONTH, Calendar.AUGUST);
		ts2.set(Calendar.DATE, 7);
		ts2.set(Calendar.HOUR_OF_DAY, 10);
		ts2.set(Calendar.MINUTE, 10);
		ts2.set(Calendar.SECOND, 10);
		ts2.set(Calendar.MILLISECOND, 0);		
		item2.setLastUpdateTimestamp(ts2);
		
		Set<UpdateLog> updates = new HashSet<UpdateLog>();
		updates.add(item1);
		updates.add(item2);
		
		this.service.save(updates);
	}
}
