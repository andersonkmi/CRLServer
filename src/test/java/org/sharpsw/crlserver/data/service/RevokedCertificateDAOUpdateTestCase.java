package org.sharpsw.crlserver.data.service;

import java.io.File;
import java.io.FileInputStream;
import java.math.BigInteger;
import java.util.Calendar;

import org.dbunit.Assertion;
import org.dbunit.dataset.IDataSet;
import org.dbunit.dataset.ITable;
import org.dbunit.dataset.SortedTable;
import org.dbunit.dataset.filter.DefaultColumnFilter;
import org.dbunit.dataset.xml.FlatXmlDataSetBuilder;
import org.junit.Before;
import org.junit.Test;
import org.sharpsw.crlserver.data.CRLIssuer;
import org.sharpsw.crlserver.data.RevokedCertificate;
import org.sharpsw.crlserver.data.RevokedCertificateCompositePrimaryKey;

public class RevokedCertificateDAOUpdateTestCase extends BaseRevokedCertificateDAOInitializedDBTestCase {
	private RevokedCertificateDAO service;
	
	@Before
	public void setUp() throws Exception {
		super.setUp();
		this.service = new RevokedCertificateDAO(this.getSession(), this.getBatchSize(), true);
	}

	@Test
	public void testUpdateRevocationTimestampOK() throws Exception {
		CRLIssuer issuer = new CRLIssuer();
		issuer.setId("1111111111111111111111111111111111111111");
		issuer.setName("CN=AC Secretaria da Receita Federal do Brasil, O=ICP-Brasil, C=BR");
		
		RevokedCertificate certificate = new RevokedCertificate();
		RevokedCertificateCompositePrimaryKey pk = new RevokedCertificateCompositePrimaryKey(issuer, new BigInteger("11223344"));
		certificate.setPrimaryKey(pk);
		
		Calendar ts = Calendar.getInstance();
		ts.set(Calendar.YEAR, 2011);
		ts.set(Calendar.MONTH, Calendar.DECEMBER);
		ts.set(Calendar.DATE, 30);
		ts.set(Calendar.HOUR_OF_DAY, 12);
		ts.set(Calendar.MINUTE, 12);
		ts.set(Calendar.SECOND, 0);
		ts.set(Calendar.MILLISECOND, 0);
		
		certificate.setRevocationTimestamp(ts);
		certificate.setRevocationReason("UserRequest");
		
		this.service.update(certificate);
		
		StringBuffer dataSetFile = new StringBuffer();
		dataSetFile.append("config").append(File.separator).append("RevokedCertificateDAO").append(File.separator).append("testUpdateRevocationTimestampOK.xml");
		IDataSet expectedDataSet = new FlatXmlDataSetBuilder().build(new FileInputStream(dataSetFile.toString()));
		ITable expectedTable = expectedDataSet.getTable("REVOKED_CERTIFICATE");
		SortedTable expectedSortedTable = new SortedTable(expectedTable, new String[] {"issuer_id", "serial_number"});
		expectedSortedTable.setUseComparable(true);
		
		IDataSet actualDataSet = this.databaseTester.getConnection().createDataSet();
		ITable actualTable = actualDataSet.getTable("REVOKED_CERTIFICATE");
		ITable filteredActualTable = DefaultColumnFilter.includedColumnsTable(actualTable, expectedTable.getTableMetaData().getColumns());
		SortedTable actualSortedTable = new SortedTable(filteredActualTable, new String[] {"issuer_id", "serial_number"});
		actualSortedTable.setUseComparable(true);

		Assertion.assertEquals(expectedSortedTable, actualSortedTable);
	}

	@Test
	public void testUpdateRevocationReasonOK() throws Exception {
		CRLIssuer issuer = new CRLIssuer();
		issuer.setId("1111111111111111111111111111111111111111");
		issuer.setName("CN=AC Secretaria da Receita Federal do Brasil, O=ICP-Brasil, C=BR");
		
		RevokedCertificate certificate = new RevokedCertificate();
		RevokedCertificateCompositePrimaryKey pk = new RevokedCertificateCompositePrimaryKey(issuer, new BigInteger("11223344"));
		certificate.setPrimaryKey(pk);
		
		Calendar ts = Calendar.getInstance();
		ts.set(Calendar.YEAR, 2011);
		ts.set(Calendar.MONTH, Calendar.DECEMBER);
		ts.set(Calendar.DATE, 30);
		ts.set(Calendar.HOUR_OF_DAY, 12);
		ts.set(Calendar.MINUTE, 12);
		ts.set(Calendar.SECOND, 0);
		ts.set(Calendar.MILLISECOND, 0);
		
		certificate.setRevocationTimestamp(ts);
		certificate.setRevocationReason("CertificateLost");
		
		this.service.update(certificate);
		
		StringBuffer dataSetFile = new StringBuffer();
		dataSetFile.append("config").append(File.separator).append("RevokedCertificateDAO").append(File.separator).append("testUpdateRevocationReasonOK.xml");
		IDataSet expectedDataSet = new FlatXmlDataSetBuilder().build(new FileInputStream(dataSetFile.toString()));
		ITable expectedTable = expectedDataSet.getTable("REVOKED_CERTIFICATE");
		SortedTable expectedSortedTable = new SortedTable(expectedTable, new String[]{"issuer_id", "serial_number"});
		expectedSortedTable.setUseComparable(true);
		
		IDataSet actualDataSet = this.databaseTester.getConnection().createDataSet();
		ITable actualTable = actualDataSet.getTable("REVOKED_CERTIFICATE");
		ITable filteredActualTable = DefaultColumnFilter.includedColumnsTable(actualTable, expectedTable.getTableMetaData().getColumns());
		SortedTable actualSortedTable = new SortedTable(filteredActualTable, new String[]{"issuer_id", "serial_number"});
		actualSortedTable.setUseComparable(true);

		Assertion.assertEquals(expectedSortedTable, actualSortedTable);
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void testUpdateNullArgFail() throws UpdateException {
		RevokedCertificate certificate = null;
		this.service.update(certificate);
	}
}
