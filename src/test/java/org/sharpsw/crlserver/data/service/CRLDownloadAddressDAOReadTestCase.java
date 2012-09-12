package org.sharpsw.crlserver.data.service;

import java.util.Set;
import java.util.TreeSet;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.sharpsw.crlserver.data.CRLDownloadAddress;
import org.sharpsw.crlserver.data.CRLDownloadAddressCompositePrimaryKey;
import org.sharpsw.crlserver.data.CRLIssuer;

public class CRLDownloadAddressDAOReadTestCase extends BaseCrlDownloadAddressDAOTestCase {
	private CRLDownloadAddressDAO service;
	
	@Before
	public void setUp() throws Exception {
		super.setUp();
		this.service = new CRLDownloadAddressDAO(this.getSession(), this.getBatchSize(), true);
	}
	
	@Test
	public void testReadAllAddressesOK() throws ReadException {
		Set<CRLDownloadAddress> elements = this.service.findAll();
		Assert.assertEquals(5, elements.size());
		
		Set<CRLDownloadAddress> expected = new TreeSet<CRLDownloadAddress>();
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
		
		CRLIssuer issuer2 = new CRLIssuer();
		issuer2.setId("2222222222222222222222222222222222222222");
		issuer2.setName("CN=Autoridade Certificadora Raiz Brasileira v1, OU=Instituto Nacional de Tecnologia da Informacao - ITI, O=ICP-Brasil, C=BR");
		issuer2.setDescription("Autoridade Certificadora Raiz Brasileira v1");
		
		CRLDownloadAddressCompositePrimaryKey key5 = new CRLDownloadAddressCompositePrimaryKey(issuer2, "http://www.url5.com/test.crl");
		CRLDownloadAddress addr5 = new CRLDownloadAddress(key5);
		addr5.setDescription("URL de teste 5");
		
		expected.add(addr1);
		expected.add(addr2);
		expected.add(addr3);
		expected.add(addr4);
		expected.add(addr5);
		
		Assert.assertEquals(expected, elements);
	}
	
	@Test
	public void testFindCRLDownloadAddressByIssuerOK() throws ReadException {
		CRLIssuer issuer = new CRLIssuer();
		issuer.setId("1111111111111111111111111111111111111111");
		issuer.setName("CN=AC Secretaria da Receita Federal do Brasil, O=ICP-Brasil, C=BR");
		issuer.setDescription("Secretaria da Receita Federal do Brasil");

		Set<CRLDownloadAddress> results = this.service.findCRLDownloadAddressByIssuer(issuer);
		Assert.assertEquals(4, results.size());

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

		
		Set<CRLDownloadAddress> expected = new TreeSet<CRLDownloadAddress>();
		expected.add(addr1);
		expected.add(addr2);
		expected.add(addr3);
		expected.add(addr4);
		
		Assert.assertEquals(expected, results);
	}
	
	@Test
	public void testFindCRLDownloadAddressByOtherIssuerOK() throws ReadException {
		CRLIssuer issuer = new CRLIssuer();
		issuer.setId("2222222222222222222222222222222222222222");
		issuer.setName("CN=Autoridade Certificadora Raiz Brasileira v1, OU=Instituto Nacional de Tecnologia da Informacao - ITI, O=ICP-Brasil, C=BR");
		issuer.setDescription("Autoridade Certificadora Raiz Brasileira v1");

		Set<CRLDownloadAddress> results = this.service.findCRLDownloadAddressByIssuer(issuer);
		Assert.assertEquals(1, results.size());

		CRLDownloadAddressCompositePrimaryKey key = new CRLDownloadAddressCompositePrimaryKey(issuer, "http://www.url5.com/test.crl");
		CRLDownloadAddress addr1 = new CRLDownloadAddress(key);
		addr1.setDescription("URL de teste 5");
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void testFindCrlDownloadAddressNullIssuerFail() throws ReadException {
		CRLIssuer issuer = null;
		this.service.findCRLDownloadAddressByIssuer(issuer);
	}
	
	@Test
	public void testFindCrlDownloadAddressByPrimaryKeyOK() throws ReadException {
		CRLIssuer issuer = new CRLIssuer();
		issuer.setId("1111111111111111111111111111111111111111");
		issuer.setName("CN=AC Secretaria da Receita Federal do Brasil, O=ICP-Brasil, C=BR");
		issuer.setDescription("Secretaria da Receita Federal do Brasil");
		
		CRLDownloadAddressCompositePrimaryKey pk = new CRLDownloadAddressCompositePrimaryKey(issuer, "http://www.url1.com/test.crl");
		CRLDownloadAddress actual = this.service.findCRLDownloadAddressByPrimaryKey(pk);
		
		CRLDownloadAddress expected = new CRLDownloadAddress(pk);
		expected.setDescription("URL de teste 1");
		
		Assert.assertEquals(expected, actual);
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void testFindCrlDownloadAddressByPrimaryKeyNullArgFail() throws ReadException {
		CRLDownloadAddressCompositePrimaryKey key = null;
		this.service.findCRLDownloadAddressByPrimaryKey(key);
	}
	
	
	@Test
	public void testFindCrlDownloadAddressByUrlMultipleResultsOK() throws ReadException {
		Set<CRLDownloadAddress> results = this.service.findCRLDownloadAddressBasedOnUrl("url");
		Assert.assertEquals(5, results.size());
		
		Set<CRLDownloadAddress> expected = new TreeSet<CRLDownloadAddress>();
		CRLIssuer issuer1 = new CRLIssuer();
		issuer1.setId("1111111111111111111111111111111111111111");
		issuer1.setName("CN=AC Secretaria da Receita Federal do Brasil, O=ICP-Brasil, C=BR");
		issuer1.setDescription("Secretaria da Receita Federal do Brasil");
		
		CRLIssuer issuer2 = new CRLIssuer();
		issuer2.setId("2222222222222222222222222222222222222222");
		issuer2.setName("CN=Autoridade Certificadora Raiz Brasileira v1, OU=Instituto Nacional de Tecnologia da Informacao - ITI, O=ICP-Brasil, C=BR");
		issuer2.setDescription("Autoridade Certificadora Raiz Brasileira v1");
		
		CRLDownloadAddressCompositePrimaryKey key1 = new CRLDownloadAddressCompositePrimaryKey(issuer1, "http://www.url1.com/test.crl");
		CRLDownloadAddress addr1 = new CRLDownloadAddress(key1);
		addr1.setDescription("URL de teste 1");
		
		CRLDownloadAddressCompositePrimaryKey key2 = new CRLDownloadAddressCompositePrimaryKey(issuer1, "http://www.url2.com/test.crl");
		CRLDownloadAddress addr2 = new CRLDownloadAddress(key2);
		addr2.setDescription("URL de teste 2");
		
		CRLDownloadAddressCompositePrimaryKey key3 = new CRLDownloadAddressCompositePrimaryKey(issuer1, "http://www.url3.com/test.crl");
		CRLDownloadAddress addr3 = new CRLDownloadAddress(key3);
		addr3.setDescription("URL de teste 3");
		
		CRLDownloadAddressCompositePrimaryKey key4 = new CRLDownloadAddressCompositePrimaryKey(issuer1, "http://www.url4.com/test.crl");
		CRLDownloadAddress addr4 = new CRLDownloadAddress(key4);
		addr4.setDescription("URL de teste 4");
		
		CRLDownloadAddressCompositePrimaryKey key5 = new CRLDownloadAddressCompositePrimaryKey(issuer2, "http://www.url5.com/test.crl");
		CRLDownloadAddress addr5 = new CRLDownloadAddress(key5);
		addr5.setDescription("URL de teste 5");
		
		expected.add(addr1);
		expected.add(addr2);
		expected.add(addr3);
		expected.add(addr4);
		expected.add(addr5);
		
		Assert.assertEquals(expected, results);
	}
	
	@Test
	public void testFindCRLDownloadAddressByUrlSingleResultOK() throws ReadException {
		Set<CRLDownloadAddress> results = this.service.findCRLDownloadAddressBasedOnUrl("url1");
		
		Assert.assertEquals(1, results.size());

		Set<CRLDownloadAddress> expected = new TreeSet<CRLDownloadAddress>();
		CRLIssuer issuer1 = new CRLIssuer();
		issuer1.setId("1111111111111111111111111111111111111111");
		issuer1.setName("CN=AC Secretaria da Receita Federal do Brasil, O=ICP-Brasil, C=BR");
		issuer1.setDescription("Secretaria da Receita Federal do Brasil");
		
		
		CRLDownloadAddressCompositePrimaryKey key1 = new CRLDownloadAddressCompositePrimaryKey(issuer1, "http://www.url1.com/test.crl");
		CRLDownloadAddress addr1 = new CRLDownloadAddress(key1);
		addr1.setDescription("URL de teste 1");
		
		expected.add(addr1);
		
		Assert.assertEquals(expected, results);
 	}
	
	@Test
	public void testFindCRLDownloadAddressEmptyResultOK() throws ReadException {
		Set<CRLDownloadAddress> results = this.service.findCRLDownloadAddressBasedOnUrl("fake");
		Assert.assertEquals(0, results.size());
	}
}
