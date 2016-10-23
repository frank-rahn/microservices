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
import static javax.persistence.EnumType.STRING;
import static org.apache.commons.lang3.StringUtils.isBlank;
import static org.apache.commons.lang3.builder.EqualsBuilder.reflectionEquals;
import static org.apache.commons.lang3.builder.HashCodeBuilder.reflectionHashCode;
import static org.apache.commons.lang3.builder.ToStringBuilder.reflectionToString;
import static org.apache.commons.lang3.builder.ToStringStyle.MULTI_LINE_STYLE;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Access;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.hibernate.annotations.ColumnDefault;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;
import org.springframework.data.domain.Persistable;

/**
 * Ein persistentes Wertpapier.
 *
 * @author Frank W. Rahn
 */
@Entity
@Access(FIELD)
@Table(name = "SEC")
public class Security implements Persistable<String> {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(generator = "uuid")
	@Column(length = 36)
	private String id;

	/** Der Name des Wertpapiers. */
	@Column(nullable = false)
	@NotNull
	@NotBlank
	private String name;

	/** Die International Security Identification Number. */
	@Column(length = 12, nullable = false, unique = true)
	@NotNull
	@Size(min = 12, max = 12, message = "muss genau {max} Zeichen lang sein")
	private String isin;

	/** Die Wertpapier-Kennnummer (WKN). */
	@Column(length = 6, nullable = false, unique = true)
	@NotNull
	@Size(min = 6, max = 6, message = "muss genau {max} Zeichen lang sein")
	private String wkn;

	/** Das Symbole des Wertpapiers. */
	@Column(length = 6)
	@Length(max = 6)
	private String symbol;

	/** Die Wertpapierart. */
	@Column(nullable = false)
	@Enumerated(STRING)
	@NotNull
	private SecurityType type;

	/** Gibt es zu diesem Wertpapier noch einen Bestand? */
	@Column(nullable = false)
	@ColumnDefault("false")
	@NotNull
	private boolean inventory = false;

	/** Die Buchungen zu diesem Wertpapier. */
	@OneToMany(mappedBy = "security", cascade = { CascadeType.ALL })
	@OrderBy(value = "date")
	private List<Entry> entries;

	/**
	 * {@inheritDoc}
	 *
	 * @see Persistable#getId()
	 */
	@Override
	public String getId() {
		return id;
	}

	/**
	 * {@inheritDoc}
	 *
	 * @see Persistable#isNew()
	 */
	@Override
	public boolean isNew() {
		return id == null;
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

	/**
	 * @param entryId Suche die Buchung mit der angegeben Id
	 * @return die gefundene Buchung oder <code>null</code>
	 * @throws IllegalArgumentException falls die Buchungs-Id ungültig ist
	 */
	public Entry getEntry(String entryId) {

		if (isBlank(entryId)) {
			throw new IllegalArgumentException("Illegal id: " + entryId);
		}

		if (entries != null) {
			for (Entry entry : entries) {
				if (entryId.equals(entry.getId())) {
					return entry;
				}
			}
		}

		return null;
	}

	/**
	 * @param entry Füge die Buchung dem Wertpapier hinzu
	 * @return die hinzugefügte Buchung
	 */
	public Entry addEntry(Entry entry) {
		entry.setSecurity(this);
		entry.setId(null);

		if (entries == null) {
			entries = new ArrayList<>();
		}

		entries.add(entry);
		return entry;
	}

	/**
	 * @param entry Ersetze die angegebene Buchung
	 * @return die ursprüngliche Buchung
	 * @throws IllegalArgumentException falls die Buchung nicht gefunden wurde
	 */
	public Entry replaceEntry(Entry entry) {
		if (entries != null && !entry.isNew()) {
			int index = entries.indexOf(entry);
			if (index >= 0) {
				entry.setSecurity(this);
				return entries.set(index, entry);
			}
		}

		throw new IllegalArgumentException("Entry in Security not found: " + entry);
	}

	/**
	 * @param entry Entferne die Buchung aus dem Wertpapier
	 * @return die ursprüngliche Buchung
	 * @throws IllegalArgumentException falls die Buchung nicht gefunden wurde
	 */
	public Entry removeEntry(Entry entry) {
		entry.setSecurity(null);

		if (entries != null) {
			if (entries.remove(entry)) {
				return entry;
			}
		}

		throw new IllegalArgumentException("Entry in Security not found: " + entry);
	}

	/**
	 * @param entryId Entferne die Buchung aus dem Wertpapier
	 * @return die ursprüngliche Buchung
	 * @throws IllegalArgumentException falls die Buchung nicht gefunden wurde
	 * @throws IllegalArgumentException falls die Buchungs-Id ungültig ist
	 */
	public Entry removeEntry(String entryId) {
		Entry entry = getEntry(entryId);

		if (entry != null) {
			return removeEntry(entry);
		}

		throw new IllegalArgumentException("Entry in Security not found: " + entryId);
	}

	/* Ab hier generiert: Setter, Getter, toString, hashCode, equals... */

	/**
	 * @return the isin
	 */
	public String getIsin() {
		return isin;
	}

	/**
	 * @param isin the isin to set
	 */
	public void setIsin(String isin) {
		this.isin = isin;
	}

	/**
	 * @return the wkn
	 */
	public String getWkn() {
		return wkn;
	}

	/**
	 * @param wkn the wkn to set
	 */
	public void setWkn(String wkn) {
		this.wkn = wkn;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return the symbol
	 */
	public String getSymbol() {
		return symbol;
	}

	/**
	 * @param symbol the symbol to set
	 */
	public void setSymbol(String symbol) {
		this.symbol = symbol;
	}

	/**
	 * @return the type
	 */
	public SecurityType getType() {
		return type;
	}

	/**
	 * @param type the type to set
	 */
	public void setType(SecurityType type) {
		this.type = type;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(String id) {
		this.id = id;
	}

	/**
	 * @return the inventory
	 */
	public boolean isInventory() {
		return inventory;
	}

	/**
	 * @param inventory the inventory to set
	 */
	public void setInventory(boolean inventory) {
		this.inventory = inventory;
	}

	/**
	 * @return the entries
	 */
	public List<Entry> getEntries() {
		return entries;
	}

	/**
	 * @param entries the entries to set
	 */
	public void setEntries(List<Entry> entries) {
		this.entries = entries;
	}

}