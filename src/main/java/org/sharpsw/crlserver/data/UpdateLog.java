package org.sharpsw.crlserver.data;

import java.io.Serializable;
import java.util.Calendar;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.sharpsw.crlserver.persistence.utils.GUIDGenerator;

import com.google.common.base.Objects;
import com.google.common.collect.ComparisonChain;
import com.google.common.collect.Ordering;

@Entity
@Table(name = "UPDATE_LOG")
public class UpdateLog implements Serializable, Comparable<UpdateLog> {
	private static final long serialVersionUID = 3483459336978047272L;
	
	@Id
	@Column(name = "id")
	private String id;
	
	@Temporal(value = TemporalType.TIMESTAMP)
	@Column(name = "last_update_timestamp")
	private Calendar lastUpdateTimestamp;
	
	@Column(name = "update_status")
	private String updateStatus;
	
	@ManyToOne
	@JoinColumn(name = "issuer_id")
	private CRLIssuer issuer;
	
	public UpdateLog() {
		GUIDGenerator guid = new GUIDGenerator(true);
		this.id = guid.getUniqueId();
	}
	
	public void setId(String id) {
		this.id= id;
	}
	
	public String getId() {
		return this.id;
	}
	
	public void setLastUpdateTimestamp(Calendar ts) {
		this.lastUpdateTimestamp = ts;
	}
	
	public Calendar getLastUpdateTimestamp() {
		return this.lastUpdateTimestamp;
	}
	
	public void setUpdateStatus(String status) {
		this.updateStatus = status;
	}
	
	public String getUpdateStatus() {
		return this.updateStatus;
	}
	
	public void setCrlIssuer(CRLIssuer issuer) {
		this.issuer = issuer;
	}
	
	public CRLIssuer getCrlIssuer() {
		return this.issuer;
	}
	
	public boolean equals(Object other) {
		if(this == other) {
			return true;
		}
		
		if(!this.getClass().equals(other.getClass())) {
			return false;			
		}
		
		UpdateLog instance = (UpdateLog) other;
		return Objects.equal(this.issuer, instance.issuer) &&
			   Objects.equal(this.lastUpdateTimestamp, instance.lastUpdateTimestamp) &&
			   Objects.equal(this.updateStatus, instance.updateStatus);
	}
	
	public int hashCode() {
		return Objects.hashCode(this.id, this.issuer, this.lastUpdateTimestamp, this.updateStatus);
	}
	
	public String toString() {
		return Objects.toStringHelper(UpdateLog.class)
				      .add("id", this.id)
				      .add("timestamp", this.lastUpdateTimestamp)
				      .add("status", this.updateStatus)
				      .toString();
	}

	@Override
	public int compareTo(UpdateLog other) {
		return ComparisonChain.start().compare(this.lastUpdateTimestamp, other.lastUpdateTimestamp, Ordering.natural().nullsLast()).result();
	}
}
