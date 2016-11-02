/*
 * Copyright 2011-2016 Frank W. Rahn and the project authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package de.rahn.finances.domains.entities;

import static javax.persistence.AccessType.FIELD;
import static org.springframework.format.annotation.DateTimeFormat.ISO.DATE_TIME;

import java.time.LocalDateTime;

import javax.persistence.Access;
import javax.persistence.EntityListeners;
import javax.persistence.MappedSuperclass;
import javax.persistence.Version;

import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.format.annotation.DateTimeFormat;

/**
 * Mit dieser Klasse wird das Auditing von Sprng Data JPA ermöglicht.
 *
 * @author Frank W. Rahn
 */
@MappedSuperclass
@Access(FIELD)
@EntityListeners({ AuditingEntityListener.class })
public abstract class Audit {

	/** Dieses Feld wird bei jedem verändernden Zugriff auf die Datenbank erhöht. */
	@Version
	private long version;

	/** Dieses Feld enthält das Datum, wann dieses Entität angelegt wurde. */
	@CreatedDate
	@DateTimeFormat(iso = DATE_TIME)
	private LocalDateTime createDate;

	/** Dieses Feld enthält den Namen des Benutzers, der dieses Entität angelegt hat. */
	@CreatedBy
	private String createBy;

	/** Dieses Feld enthält den Zeitstempel, wann dieses Entität zu letzt geändert wurde. */
	@LastModifiedDate
	@DateTimeFormat(iso = DATE_TIME)
	private LocalDateTime lastModifiedDate;

	/** Dieses Feld enthält den Namen des Benutzers, der dieses Entität zu letzt geändert hat. */
	@LastModifiedBy
	private String lastModifiedBy;

	/* Ab hier generiert: Getter ... */

	/**
	 * @return the createDate
	 */
	public LocalDateTime getCreateDate() {
		return createDate;
	}

	/**
	 * @param createDate the createDate to set
	 */
	public void setCreateDate(LocalDateTime createDate) {
		this.createDate = createDate;
	}

	/**
	 * @return the version
	 */
	public long getVersion() {
		return version;
	}

	/**
	 * @return the createBy
	 */
	public String getCreateBy() {
		return createBy;
	}

	/**
	 * @param createBy the createBy to set
	 */
	public void setCreateBy(String createBy) {
		this.createBy = createBy;
	}

	/**
	 * @return the lastModifiedDate
	 */
	public LocalDateTime getLastModifiedDate() {
		return lastModifiedDate;
	}

	/**
	 * @param lastModifiedDate the lastModifiedDate to set
	 */
	public void setLastModifiedDate(LocalDateTime lastModifiedDate) {
		this.lastModifiedDate = lastModifiedDate;
	}

	/**
	 * @return the lastModifiedBy
	 */
	public String getLastModifiedBy() {
		return lastModifiedBy;
	}

	/**
	 * @param lastModifiedBy the lastModifiedBy to set
	 */
	public void setLastModifiedBy(String lastModifiedBy) {
		this.lastModifiedBy = lastModifiedBy;
	}

}