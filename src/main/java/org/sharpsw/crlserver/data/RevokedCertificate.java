package org.sharpsw.crlserver.data;

import java.io.Serializable;
import java.math.BigInteger;
import java.util.Calendar;

import javax.persistence.AssociationOverride;
import javax.persistence.AssociationOverrides;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import com.google.common.base.Objects;

@Entity
@Table(name = "REVOKED_CERTIFICATE")
@AssociationOverrides( {@AssociationOverride(name = "primaryKey.serialNumber", joinColumns = @JoinColumn(name = "serial_number")), 
    					@AssociationOverride(name = "primaryKey.crlIssuer", joinColumns = @JoinColumn(name = "issuer_id", referencedColumnName = "id")) })
public class RevokedCertificate implements Serializable, Comparable<RevokedCertificate> {
	private static final long serialVersionUID = 8724405278993922848L;

	@EmbeddedId
	private RevokedCertificateCompositePrimaryKey primaryKey = new RevokedCertificateCompositePrimaryKey();
	
	@Temporal(value = TemporalType.TIMESTAMP)
	@Column(name = "revocation_timestamp")
	private Calendar revocationTimestamp;
	
	@Column(name = "revocation_reason")
	private String revocationReason;
		
	
	public RevokedCertificate() {}
	
	public RevokedCertificate(RevokedCertificateCompositePrimaryKey key) {
		this.primaryKey = key;
	}
	
	public void setPrimaryKey(RevokedCertificateCompositePrimaryKey key) {
		this.primaryKey = key;
	}
	
	public RevokedCertificateCompositePrimaryKey getPrimaryKey() {
		return this.primaryKey;
	}
		
	
	public void setRevocationTimestamp(Calendar ts) {
		this.revocationTimestamp = ts;
	}
	
	public Calendar getRevocationTimestamp() {
		return this.revocationTimestamp;
	}
	
	public void setRevocationReason(String reason) {
		this.revocationReason = reason;
	}
	
	public String getRevocationReason() {
		return this.revocationReason;
	}
	
	@Transient
	public BigInteger getSerialNumber() {
		return this.getPrimaryKey().getSerialNumber();
	}
	
	public void setSerialNumber(BigInteger serialNumber) {
		this.getPrimaryKey().setSerialNumber(serialNumber);
	}
	
	@Transient
	public CRLIssuer getCrlIssuer() {
		return this.getPrimaryKey().getCrlIssuer();
	}
	
	public void setCrlIssuer(CRLIssuer issuer) {
		this.getPrimaryKey().setCrlIssuer(issuer);
	}
	
	public boolean equals(Object other) {
		if(this == other) {
			return true;
		}
		
		if(!this.getClass().equals(other.getClass())) {
			return false;
		}
		
		RevokedCertificate instance = (RevokedCertificate) other;
		
		return Objects.equal(this.primaryKey, instance.primaryKey) &&
			   Objects.equal(this.revocationReason, instance.revocationReason) &&
			   Objects.equal(this.revocationTimestamp, instance.revocationTimestamp);
	}
	
	public int hashCode() {
		return Objects.hashCode(this.primaryKey, this.revocationReason, this.revocationTimestamp);
	}
	
	public String toString() {
		return Objects.toStringHelper(RevokedCertificate.class)
				      .add("serialNumber", this.getPrimaryKey().getSerialNumber())
				      .add("revocationTimestamp", this.revocationTimestamp)
				      .add("revocationReason", this.revocationReason)
				      .toString();
	}

	@Override
	public int compareTo(RevokedCertificate other) {
		return this.primaryKey.getSerialNumber().compareTo(other.primaryKey.getSerialNumber());
	}
}
