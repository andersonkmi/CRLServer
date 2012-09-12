package org.sharpsw.crlserver.data;

import java.io.Serializable;
import java.math.BigInteger;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import com.google.common.base.Objects;

@Embeddable
public class RevokedCertificateCompositePrimaryKey implements Serializable {
	private static final long serialVersionUID = 7057605874947350186L;

	@ManyToOne
	@JoinColumn(name = "issuer_id", updatable = false, insertable = false)
	private CRLIssuer crlIssuer;
	
	@Column(name = "serial_number", updatable = false, insertable = false)
	private BigInteger serialNumber;
	
	public RevokedCertificateCompositePrimaryKey() {
		
	}
	
	public RevokedCertificateCompositePrimaryKey(CRLIssuer issuer, BigInteger serialNumber) {
		this.crlIssuer = issuer;
		this.serialNumber = serialNumber;
	}
	
	public void setCrlIssuer(CRLIssuer issuer) {
		this.crlIssuer = issuer;
	}
	
	public CRLIssuer getCrlIssuer() {
		return this.crlIssuer;
	}
	
	public void setSerialNumber(BigInteger serialNumber) {
		this.serialNumber = serialNumber;
	}
	
	public BigInteger getSerialNumber() {
		return this.serialNumber;
	}
	
	public boolean equals(Object other) {
		if(this == other) {
			return true;
		}
		
		if(!this.getClass().equals(other.getClass())) {
			return false;
		}
		
		RevokedCertificateCompositePrimaryKey instance = (RevokedCertificateCompositePrimaryKey) other;
		return Objects.equal(this.crlIssuer.getName(), instance.crlIssuer.getName()) &&
			   Objects.equal(this.serialNumber, instance.serialNumber);
	}
	
	public int hashCode() {
		return Objects.hashCode(this.crlIssuer, this.serialNumber);
	}
}
