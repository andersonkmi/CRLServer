package org.sharpsw.crlserver.data.service;

import java.io.File;
import java.io.FileInputStream;
import java.math.BigInteger;
import java.util.Calendar;
import java.util.HashSet;
import java.util.Set;

import org.dbunit.Assertion;
import org.dbunit.dataset.IDataSet;
import org.dbunit.dataset.ITable;
import org.dbunit.dataset.SortedTable;
import org.dbunit.dataset.filter.DefaultColumnFilter;
import org.dbunit.dataset.xml.FlatXmlDataSetBuilder;
import org.junit.Test;
import org.sharpsw.crlserver.data.CRLIssuer;
import org.sharpsw.crlserver.data.RevokedCertificate;
import org.sharpsw.crlserver.data.RevokedCertificateCompositePrimaryKey;

public class RevokedCertificateDAOSaveTestCase extends BaseRevokedCertificateDAOTestCase {
	private RevokedCertificateDAO service;
	
	public void setUp() throws Exception {
		super.setUp();
		this.service = new RevokedCertificateDAO(this.getSession(), this.getBatchSize(), true);
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void testCreateRevokedCertificateNullArgFail() throws CreateException {
		RevokedCertificate certificate = null;
		this.service.save(certificate);
	}
	
	@Test
	public void testCreateRevokedCertificateOK() throws Exception {
		CRLIssuer issuer = new CRLIssuer();
		issuer.setId("1111111111111111111111111111111111111111");
		issuer.setName("CN=AC Secretaria da Receita Federal do Brasil, O=ICP-Brasil, C=BR");
		issuer.setDescription("AC Secretaria da Receita Federal do Brasil");
		
		RevokedCertificateCompositePrimaryKey pk = new RevokedCertificateCompositePrimaryKey(issuer, new BigInteger("11223344"));
		RevokedCertificate certificate = new RevokedCertificate(pk);
		
		Calendar ts = Calendar.getInstance();
		ts.set(Calendar.YEAR, 2012);
		ts.set(Calendar.MONTH, Calendar.MARCH);
		ts.set(Calendar.DATE, 31);
		ts.set(Calendar.HOUR_OF_DAY, 11);
		ts.set(Calendar.MINUTE, 15);
		ts.set(Calendar.SECOND, 59);
		ts.set(Calendar.MILLISECOND, 0);
		
		certificate.setRevocationTimestamp(ts);
		certificate.setRevocationReason("UserRequest");
		
		this.service.save(certificate);
		
		StringBuffer dataSetFile = new StringBuffer();
		dataSetFile.append("config").append(File.separator).append("RevokedCertificateDAO").append(File.separator).append("testCreateRevokedCertificateOK.xml");
		IDataSet expectedDataSet = new FlatXmlDataSetBuilder().build(new FileInputStream(dataSetFile.toString()));
		ITable expectedTable = expectedDataSet.getTable("REVOKED_CERTIFICATE");
		
		IDataSet actualDataSet = this.databaseTester.getConnection().createDataSet();
		ITable actualTable = actualDataSet.getTable("REVOKED_CERTIFICATE");
		ITable filteredActualTable = DefaultColumnFilter.includedColumnsTable(actualTable, expectedTable.getTableMetaData().getColumns());

		Assertion.assertEquals(expectedTable, filteredActualTable);
	}
	
	@Test
	public void testBulkCreateRevokedCertificatesOK() throws Exception {
		CRLIssuer issuer = new CRLIssuer();
		issuer.setId("1111111111111111111111111111111111111111");
		issuer.setName("CN=AC Secretaria da Receita Federal do Brasil, O=ICP-Brasil, C=BR");
		issuer.setDescription("AC Secretaria da Receita Federal do Brasil");

		RevokedCertificateCompositePrimaryKey key1 = new RevokedCertificateCompositePrimaryKey(issuer, new BigInteger("11223344"));
		RevokedCertificate cert1 = new RevokedCertificate(key1);
		
		RevokedCertificateCompositePrimaryKey key2 = new RevokedCertificateCompositePrimaryKey(issuer, new BigInteger("22334455"));
		RevokedCertificate cert2 = new RevokedCertificate(key2);
		
		Calendar ts1 = Calendar.getInstance();
		ts1.set(Calendar.YEAR, 2011);
		ts1.set(Calendar.MONTH, Calendar.JUNE);
		ts1.set(Calendar.DATE, 1);
		ts1.set(Calendar.HOUR_OF_DAY, 8);
		ts1.set(Calendar.MINUTE, 9);
		ts1.set(Calendar.SECOND, 10);
		ts1.set(Calendar.MILLISECOND, 0);
		
		cert1.setRevocationReason("UserRequest");
		cert1.setRevocationTimestamp(ts1);
		
		Calendar ts2 = Calendar.getInstance();
		ts2.set(Calendar.YEAR, 2012);
		ts2.set(Calendar.MONTH, Calendar.JULY);
		ts2.set(Calendar.DATE, 20);
		ts2.set(Calendar.HOUR_OF_DAY, 15);
		ts2.set(Calendar.MINUTE, 10);
		ts2.set(Calendar.SECOND, 49);
		ts2.set(Calendar.MILLISECOND, 0);
		
		cert2.setRevocationReason("LostCertificate");
		cert2.setRevocationTimestamp(ts2);
		
		Set<RevokedCertificate> certificates = new HashSet<RevokedCertificate>();
		certificates.add(cert2);
		certificates.add(cert1);
		
		this.service.save(certificates);
		
		StringBuffer dataSetFile = new StringBuffer();
		dataSetFile.append("config").append(File.separator).append("RevokedCertificateDAO").append(File.separator).append("testBulkCreateRevokedCertificatesOK.xml");
		IDataSet expectedDataSet = new FlatXmlDataSetBuilder().build(new FileInputStream(dataSetFile.toString()));
		ITable expectedTable = expectedDataSet.getTable("REVOKED_CERTIFICATE");
		SortedTable sortedExpectedTable = new SortedTable(expectedTable, new String[] {"serial_number"});
		sortedExpectedTable.setUseComparable(true);
		
		IDataSet actualDataSet = this.databaseTester.getConnection().createDataSet();
		ITable actualTable = actualDataSet.getTable("REVOKED_CERTIFICATE");
		ITable filteredActualTable = DefaultColumnFilter.includedColumnsTable(actualTable, expectedTable.getTableMetaData().getColumns());
		SortedTable actualSortedTable = new SortedTable(filteredActualTable, new String[] {"serial_number"});
		actualSortedTable.setUseComparable(true);

		Assertion.assertEquals(sortedExpectedTable, actualSortedTable);
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void testBulkAddNullSetFail() throws CreateException {
		Set<RevokedCertificate> elements = null;
		this.service.save(elements);
	}
}
