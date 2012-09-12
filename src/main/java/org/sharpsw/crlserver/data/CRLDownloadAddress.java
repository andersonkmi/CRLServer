package org.sharpsw.crlserver.data;

import java.io.Serializable;

import javax.persistence.AssociationOverride;
import javax.persistence.AssociationOverrides;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.google.common.base.Objects;

@Entity
@Table(name = "CRL_DOWNLOAD_ADDRESS")
@AssociationOverrides( {@AssociationOverride(name = "primaryKey.address", joinColumns = @JoinColumn(name = "address")), 
                        @AssociationOverride(name = "primaryKey.crlIssuer", joinColumns = @JoinColumn(name = "issuer_id", referencedColumnName = "id")) })
public class CRLDownloadAddress implements Serializable, Comparable<CRLDownloadAddress> {
	private static final long serialVersionUID = -5859116586640995790L;

	@EmbeddedId
	private CRLDownloadAddressCompositePrimaryKey primaryKey = new CRLDownloadAddressCompositePrimaryKey();
		
	@Column(name = "description")
	private String description;
	
	public CRLDownloadAddress() {
		
	}
		
	public CRLDownloadAddress(CRLDownloadAddressCompositePrimaryKey key) {
		this.primaryKey = key;
	}
	
	public void setPrimaryKey(CRLDownloadAddressCompositePrimaryKey key) {
		this.primaryKey = key;
	}
	
	public CRLDownloadAddressCompositePrimaryKey getPrimaryKey() {
		return this.primaryKey;
	}
	
	
	@Transient
	public String getAddress() {
		return this.getPrimaryKey().getAddress();
	}
	
	public void setAddress(String address) {
		this.getPrimaryKey().setAddress(address);
	}
	
	
	@Transient
	public CRLIssuer getCrlIssuer() {
		return this.getPrimaryKey().getCrlIssuer();
	}
	
	public void setCrlIssuer(CRLIssuer issuer) {
		this.getPrimaryKey().setCrlIssuer(issuer);
	}
	
	public void setDescription(String description) {
		this.description = description;
	}
	
	public String getDescription() {
		return this.description;
	}
	
	public boolean equals(Object other) {
		if(this == other) {
			return true;
		}
		
		if(!this.getClass().equals(other.getClass())) {
			return false;
		}
		
		CRLDownloadAddress instance = (CRLDownloadAddress) other;
		return Objects.equal(this.primaryKey.getCrlIssuer().getId(), instance.primaryKey.getCrlIssuer().getId()) &&
			   Objects.equal(this.description, instance.description);
	}
	
	public int hashCode() {
		return Objects.hashCode(this.primaryKey, this.description);
	}
	
	public String toString() {
		return Objects.toStringHelper(CRLDownloadAddress.class)
					   .add("issuer_id", this.getPrimaryKey().getCrlIssuer().getId())
				       .add("address", this.getPrimaryKey().getAddress())
				       .add("description", this.description)
				       .toString();
	}

	@Override
	public int compareTo(CRLDownloadAddress other) {
		return this.primaryKey.getAddress().compareTo(other.primaryKey.getAddress());
	}
}
