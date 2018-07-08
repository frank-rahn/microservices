/*
 * Copyright 2011-2016 Frank W. Rahn and the project authors.
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package de.rahn.finances.domains.entities;

import static javax.persistence.AccessType.FIELD;
import static javax.persistence.CascadeType.MERGE;
import static javax.persistence.CascadeType.REFRESH;
import static javax.persistence.EnumType.STRING;
import static org.apache.commons.lang3.builder.EqualsBuilder.reflectionEquals;
import static org.apache.commons.lang3.builder.HashCodeBuilder.reflectionHashCode;
import static org.apache.commons.lang3.builder.ToStringBuilder.reflectionToString;
import static org.apache.commons.lang3.builder.ToStringStyle.MULTI_LINE_STYLE;
import static org.springframework.format.annotation.DateTimeFormat.ISO.DATE;

import java.math.BigDecimal;
import java.time.LocalDate;

import javax.persistence.Access;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import org.springframework.format.annotation.DateTimeFormat;

/**
 * Eine Buchung.
 *
 * @author Frank W. Rahn
 */
@Entity
@Access(FIELD)
@Table
public class Entry extends Audit {

	@Id
	@GeneratedValue(generator = "uuid")
	@Column(length = 36)
	private String id;

	/** Das Datum des Eintrags. */
	@Column(nullable = false)
	@NotNull
	@DateTimeFormat(iso = DATE)
	private LocalDate date;

	/** Die Anzahl oder Stückzahl. */
	@Column(precision = 16, scale = 4, nullable = false)
	@NotNull
	private BigDecimal numberOf;

	/** Die Gesamtsumme. */
	@Column(precision = 16, scale = 6, nullable = false)
	@NotNull
	private BigDecimal amount;

	/** Der Einzelpreis bzw. Kurs. */
	@Column(precision = 16, scale = 6, nullable = false)
	@NotNull
	private BigDecimal price;

	/** Der Typ der Buchung. */
	@Column(nullable = false)
	@Enumerated(STRING)
	@NotNull
	private EntryType type;

	/** Das zugehörige Wertpapier. */
	@ManyToOne(optional = false, cascade = { MERGE, REFRESH })
	private Security security;

	/**
	 * Übernehme die Änderungen.
	 *
	 * @param entry die Buchung mit den Änderungen
	 * @return die geänderte Buchung
	 */
	public Entry update(Entry entry) {
		if (entry == null || entry.getId() == null) {
			throw new IllegalArgumentException("Entry is new. Entry=" + entry);
		}

		if (id == null) {
			throw new IllegalStateException("This Entry is new. Entry=" + this);
		}

		if (!id.equals(entry.getId())) {
			throw new IllegalArgumentException("The Entries do not belong together. Entry: id=" + id + ", id=" + entry.getId());
		}

		date = entry.date;
		numberOf = entry.numberOf;
		amount = entry.amount;
		price = entry.price;
		type = entry.type;

		return this;
	}

	/**
	 * {@inheritDoc}
	 *
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		return reflectionHashCode(this, false);
	}

	/**
	 * {@inheritDoc}
	 *
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		return reflectionEquals(this, obj, false);
	}

	/**
	 * {@inheritDoc}
	 *
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return reflectionToString(this, MULTI_LINE_STYLE);
	}

	/* Ab hier generiert: Setter, Getter, toString, hashCode, equals... */

	/**
	 * @return the id
	 */
	public String getId() {
		return id;
	}

	/**
	 * @return the date
	 */
	public LocalDate getDate() {
		return date;
	}

	/**
	 * @param date the date to set
	 */
	public void setDate(LocalDate date) {
		this.date = date;
	}

	/**
	 * @return the numberOf
	 */
	public BigDecimal getNumberOf() {
		return numberOf;
	}

	/**
	 * @param numberOf the numberOf to set
	 */
	public void setNumberOf(BigDecimal numberOf) {
		this.numberOf = numberOf;
	}

	/**
	 * @return the amount
	 */
	public BigDecimal getAmount() {
		return amount;
	}

	/**
	 * @param amount the amount to set
	 */
	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}

	/**
	 * @return the price
	 */
	public BigDecimal getPrice() {
		return price;
	}

	/**
	 * @param price the price to set
	 */
	public void setPrice(BigDecimal price) {
		this.price = price;
	}

	/**
	 * @return the type
	 */
	public EntryType getType() {
		return type;
	}

	/**
	 * @param type the type to set
	 */
	public void setType(EntryType type) {
		this.type = type;
	}

	/**
	 * @return the security
	 */
	public Security getSecurity() {
		return security;
	}

	/**
	 * @param security the security to set
	 */
	public void setSecurity(Security security) {
		this.security = security;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(String id) {
		this.id = id;
	}

}