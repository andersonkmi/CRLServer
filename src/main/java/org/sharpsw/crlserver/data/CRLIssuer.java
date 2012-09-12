package org.sharpsw.crlserver.data;

import java.io.Serializable;
import java.util.Calendar;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.sharpsw.crlserver.persistence.utils.GUIDGenerator;

import com.google.common.base.Objects;

@Entity
@Table(name = "CRL_ISSUER")
public class CRLIssuer implements Serializable, Comparable<CRLIssuer> {
	private static final long serialVersionUID = -311724546394487015L;

	@Id
	@Column(name = "id")
	private String id;
	
	@Column(name = "name")
	private String name;
	
	@Column(name = "description")
	private String description;
	
	@Temporal(value = TemporalType.TIMESTAMP)
	@Column(name = "next_update_timestamp")
	private Calendar nextUpdateTimestamp;
	
	@OneToMany(mappedBy = "primaryKey.crlIssuer")
	Set<CRLDownloadAddress> addresses = new HashSet<CRLDownloadAddress>();
	
	@OneToMany(mappedBy = "primaryKey.crlIssuer")
	Set<RevokedCertificate> certificates = new HashSet<RevokedCertificate>();
	
	@OneToMany(mappedBy = "issuer")
	Set<UpdateLog> updateLogs = new HashSet<UpdateLog>();
	
	public CRLIssuer() {
		GUIDGenerator guid = new GUIDGenerator(true);
		this.id = guid.getUniqueId();
	}
	
	public void setId(String id) {
		this.id= id;
	}
	
	public String getId() {
		return this.id;
	}
	
	public void setName(String name) {
		this.name= name;
	}
	
	public String getName() {
		return this.name;
	}
	
	public void setDescription(String description) {
		this.description = description;
	}
	
	public String getDescription() {
		return this.description;
	}
	
	public void setNextUpdateTimestamp(Calendar ts) {
		this.nextUpdateTimestamp = ts;
	}
	
	public Calendar getNextUpdateTimestamp() {
		return this.nextUpdateTimestamp;
	}
	
	public void setCrlDownloadAddresses(Set<CRLDownloadAddress> items) {
		this.addresses = items;
	}
	
	public Set<CRLDownloadAddress> getCrlDownloadAddresses() {
		return this.addresses;
	}
	
	public void setRevokedCertificates(Set<RevokedCertificate> items) {
		this.certificates = items;
	}
	
	public Set<RevokedCertificate> getRevokedCertificates() {
		return this.certificates;
	}
	
	public void setUpdateLogs(Set<UpdateLog> items) {
		this.updateLogs = items;
	}
	
	public Set<UpdateLog> getUpdateLogs() {
		return this.updateLogs;
	}
	
	public boolean equals(Object other) {
		if(this == other) {
			return true;
		}
		
		if(!this.getClass().equals(other.getClass())) {
			return false;
		}
		
		CRLIssuer instance = (CRLIssuer) other;
		return Objects.equal(this.name, instance.name) &&
			   Objects.equal(this.description, instance.description) &&
			   Objects.equal(this.nextUpdateTimestamp, instance.nextUpdateTimestamp) &&
			   Objects.equal(this.addresses, instance.addresses) &&
			   Objects.equal(this.certificates, instance.certificates) &&
			   Objects.equal(this.updateLogs, instance.updateLogs);
	}
	
	public String toString() {
		return Objects.toStringHelper(CRLIssuer.class)
				      .add("id", this.id)
				      .add("name", this.name)
				      .add("description", this.description)
				      .add("nextUpdateTimestamp", this.nextUpdateTimestamp)
				      .toString();
	}
	
	public int hashCode() {
		return Objects.hashCode(this.id, this.name, this.description, this.nextUpdateTimestamp);
	}

	public int compareTo(CRLIssuer other) {
		return this.name.compareTo(other.name);
	}
}
