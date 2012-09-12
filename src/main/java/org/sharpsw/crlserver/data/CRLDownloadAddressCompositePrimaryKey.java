package org.sharpsw.crlserver.data;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import com.google.common.base.Objects;

@Embeddable
public class CRLDownloadAddressCompositePrimaryKey implements Serializable {
	private static final long serialVersionUID = 2167193288225987891L;

	@ManyToOne
	@JoinColumn(name = "issuer_id", updatable = false, insertable = false)
	private CRLIssuer crlIssuer;
	
	@Column(name = "address", updatable = false, insertable = false)
	private String address;
	
	public CRLDownloadAddressCompositePrimaryKey() {
		
	}
	
	public CRLDownloadAddressCompositePrimaryKey(CRLIssuer issuer, String address) {
		this.crlIssuer = issuer;
		this.address = address;
	}
	
	public void setCrlIssuer(CRLIssuer issuer) {
		this.crlIssuer = issuer;
	}
	
	public CRLIssuer getCrlIssuer() {
		return this.crlIssuer;
	}
	
	public void setAddress(String address) {
		this.address = address;
	}
	
	public String getAddress() {
		return this.address;
	}
	
	public boolean equals(Object other) {
		if(this == other) {
			return true;
		}
		
		if(!this.getClass().equals(other.getClass())) {
			return false;
		}
		
		CRLDownloadAddressCompositePrimaryKey instance = (CRLDownloadAddressCompositePrimaryKey) other;
		
		return Objects.equal(this.crlIssuer, instance.crlIssuer) && 
			   Objects.equal(this.address, instance.address);
	}
	
	public int hashCode() {
		return Objects.hashCode(this.crlIssuer, this.address);
	}
}
