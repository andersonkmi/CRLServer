package org.sharpsw.crlserver.data.service;

import java.io.File;
import java.io.FileInputStream;
import java.math.BigInteger;
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
import org.sharpsw.crlserver.data.RevokedCertificate;
import org.sharpsw.crlserver.data.RevokedCertificateCompositePrimaryKey;

public class RevokedCertificateDAODeleteTestCase extends BaseRevokedCertificateDAOInitializedDBTestCase {
	private RevokedCertificateDAO service;
	
	@Before
	public void setUp() throws Exception {
		super.setUp();
		this.service = new RevokedCertificateDAO(this.getSession(), this.getBatchSize(), true);
	}
	
	@Test
	public void testDeleteSingleCertificateOK() throws Exception {
		CRLIssuer issuer = new CRLIssuer();
		issuer.setId("1111111111111111111111111111111111111111");
		issuer.setName("CN=AC Secretaria da Receita Federal do Brasil, O=ICP-Brasil, C=BR");
		
		RevokedCertificate certificate = new RevokedCertificate();
		RevokedCertificateCompositePrimaryKey pk = new RevokedCertificateCompositePrimaryKey(issuer, new BigInteger("11223344"));
		certificate.setPrimaryKey(pk);
		
		this.service.delete(certificate);
		
		StringBuffer dataSetFile = new StringBuffer();
		dataSetFile.append("config").append(File.separator).append("RevokedCertificateDAO").append(File.separator).append("testDeleteSingleCertificateOK.xml");
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
	
	@Test(expected = IllegalArgumentException.class)
	public void testDeleteNullArgumentFail() throws DeleteException {
		RevokedCertificate certificate = null;
		this.service.delete(certificate);
	}
	
	@Test
	public void testBulkDeleteOK() throws Exception {
		CRLIssuer issuer = new CRLIssuer();
		issuer.setId("1111111111111111111111111111111111111111");
		issuer.setName("CN=AC Secretaria da Receita Federal do Brasil, O=ICP-Brasil, C=BR");
		
		RevokedCertificateCompositePrimaryKey pk1 = new RevokedCertificateCompositePrimaryKey(issuer, new BigInteger("11223344"));
		RevokedCertificate cert1 = new RevokedCertificate(pk1);
		
		RevokedCertificateCompositePrimaryKey pk2 = new RevokedCertificateCompositePrimaryKey(issuer, new BigInteger("22334455"));
		RevokedCertificate cert2 = new RevokedCertificate(pk2);
		
		RevokedCertificateCompositePrimaryKey pk3 = new RevokedCertificateCompositePrimaryKey(issuer, new BigInteger("11224433"));
		RevokedCertificate cert3 = new RevokedCertificate(pk3);
		
		Set<RevokedCertificate> certs = new HashSet<RevokedCertificate>();
		certs.add(cert1);
		certs.add(cert2);
		certs.add(cert3);
		
		this.service.delete(certs);
		
		StringBuffer dataSetFile = new StringBuffer();
		dataSetFile.append("config").append(File.separator).append("RevokedCertificateDAO").append(File.separator).append("testBulkDeleteOK.xml");
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
	
	@Test(expected = IllegalArgumentException.class)
	public void testBulkDeleteNullArgFail() throws DeleteException {
		Set<RevokedCertificate> certs = null;
		this.service.delete(certs);
	}
}
