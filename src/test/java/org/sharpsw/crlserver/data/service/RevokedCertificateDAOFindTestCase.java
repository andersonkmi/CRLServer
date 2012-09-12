package org.sharpsw.crlserver.data.service;

import java.math.BigInteger;
import java.util.Calendar;
import java.util.Set;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.sharpsw.crlserver.data.CRLIssuer;
import org.sharpsw.crlserver.data.RevokedCertificate;
import org.sharpsw.crlserver.data.RevokedCertificateCompositePrimaryKey;

public class RevokedCertificateDAOFindTestCase extends BaseRevokedCertificateDAOInitializedDBTestCase {
	private RevokedCertificateDAO service;
	
	@Before
	public void setUp() throws Exception {
		super.setUp();
		this.service = new RevokedCertificateDAO(this.getSession(), this.getBatchSize(), true);
	}

	@Test
	public void testFindAllRevokedCertificatesOK() throws ReadException {
		Set<RevokedCertificate> certificates = this.service.findAll();
		Assert.assertEquals(5, certificates.size());
	}
	
	@Test
	public void testFindRevokedCertificateByPrimaryKeyOK() throws ReadException {
		CRLIssuer issuer = new CRLIssuer();
		issuer.setId("1111111111111111111111111111111111111111");
		issuer.setName("CN=AC Secretaria da Receita Federal do Brasil, O=ICP-Brasil, C=BR");
		
		RevokedCertificateCompositePrimaryKey pk = new RevokedCertificateCompositePrimaryKey(issuer, new BigInteger("11223344"));
		RevokedCertificate certificate = this.service.findByPrimaryKey(pk);
		
		RevokedCertificate expected = new RevokedCertificate();
		expected.setPrimaryKey(new RevokedCertificateCompositePrimaryKey(issuer, new BigInteger("11223344")));
		expected.setRevocationReason("UserRequest");
		Calendar ts = Calendar.getInstance();
		ts.set(Calendar.YEAR, 2012);
		ts.set(Calendar.MONTH, Calendar.JANUARY);
		ts.set(Calendar.DATE, 1);
		ts.set(Calendar.HOUR_OF_DAY, 22);
		ts.set(Calendar.MINUTE, 39);
		ts.set(Calendar.SECOND, 10);
		ts.set(Calendar.MILLISECOND, 0);
		expected.setRevocationTimestamp(ts);
		
		Assert.assertEquals(expected, certificate);
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void testFindRevokedCertificateNullArgFail() throws ReadException {
		RevokedCertificateCompositePrimaryKey pk = null;
		this.service.findByPrimaryKey(pk);
	}
}
