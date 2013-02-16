CREATE TABLE CRL_ISSUER (
	id CHAR(40) PRIMARY KEY,
	name VARCHAR(300) NOT NULL UNIQUE,
	description VARCHAR(200),
	next_update_timestamp TIMESTAMP NULL
);

CREATE TABLE CRL_DOWNLOAD_ADDRESS (
	address VARCHAR(200) NOT NULL,
	issuer_id CHAR(40) NOT NULL,
	description VARCHAR(200)
);
ALTER TABLE CRL_DOWNLOAD_ADDRESS ADD FOREIGN KEY (issuer_id) REFERENCES CRL_ISSUER(id) ON DELETE CASCADE ON UPDATE CASCADE;
ALTER TABLE CRL_DOWNLOAD_ADDRESS ADD PRIMARY KEY (issuer_id, address);

CREATE TABLE UPDATE_LOG (
	id CHAR(40) NOT NULL PRIMARY KEY,
	last_update_timestamp TIMESTAMP NOT NULL,
	update_status CHAR(1) NOT NULL,
	issuer_id CHAR(40) NOT NULL
);
ALTER TABLE UPDATE_LOG ADD FOREIGN KEY (issuer_id) REFERENCES CRL_ISSUER(id) ON DELETE CASCADE ON UPDATE CASCADE;

CREATE TABLE REVOKED_CERTIFICATE (
	serial_number BIGINT NOT NULL,
	revocation_timestamp TIMESTAMP NOT NULL,
	revocation_reason VARCHAR(50),
	issuer_id CHAR(40) NOT NULL
);
ALTER TABLE REVOKED_CERTIFICATE ADD FOREIGN KEY (issuer_id) REFERENCES CRL_ISSUER(id) ON DELETE CASCADE ON UPDATE CASCADE;
ALTER TABLE REVOKED_CERTIFICATE ADD PRIMARY KEY(issuer_id, serial_number);

