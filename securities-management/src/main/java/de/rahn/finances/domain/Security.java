/*
 * Copyright © 2015 by Frank W. Rahn. Alle Rechte vorbehalten. All rights reserved.
 */
package de.rahn.finances.domain;

import static javax.persistence.AccessType.FIELD;
import static javax.persistence.EnumType.STRING;

import java.io.Serializable;

import javax.persistence.Access;
import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.data.domain.Persistable;

import de.rahn.finances.domain.util.SecurityTypeConverter;

/**
 * Ein persistentes Wertpapier.
 * @author Frank W. Rahn
 */
@Entity
@Table(name = "SEC")
@Access(FIELD)
public class Security implements Serializable, Persistable<String> {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(generator = "uuid")
	@GenericGenerator(name = "uuid", strategy = "uuid2")
	private String id;

	/** Der Name des Wertpapiers. */
	@Column(nullable = false)
	private String name;

	/** Die International Security Identification Number. */
	@Column(length = 12, nullable = false, unique = true)
	private String isin;

	/** Die Wertpapier-Kennnummer (WKN). */
	@Column(length = 6, nullable = false, unique = true)
	private String wkn;

	/** Das Symbole des Wertpapiers. */
	@Column(length = 6)
	private String symbol;

	/** Die Wertpapierart. */
	@Column(nullable = false)
	@Enumerated(STRING)
	@Convert(converter = SecurityTypeConverter.class)
	private SecurityType type;

	/** Gibt es zu diesem Wertpapier noch einen Bestand? */
	@Column(nullable = false)
	@ColumnDefault("false")
	private boolean inventory = false;

	/**
	 * {@inheritDoc}
	 * @see Persistable#isNew()
	 */
	@Override
	public boolean isNew() {
		return id == null;
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
	 * @return the id
	 */
	@Override
	public String getId() {
		return id;
	}

	/**
	 * {@inheritDoc}
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (id == null ? 0 : id.hashCode());
		result = prime * result + (inventory ? 1231 : 1237);
		result = prime * result + (isin == null ? 0 : isin.hashCode());
		result = prime * result + (name == null ? 0 : name.hashCode());
		result = prime * result + (symbol == null ? 0 : symbol.hashCode());
		result = prime * result + (type == null ? 0 : type.hashCode());
		result = prime * result + (wkn == null ? 0 : wkn.hashCode());
		return result;
	}

	/**
	 * {@inheritDoc}
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (!(obj instanceof Security)) {
			return false;
		}
		Security other = (Security) obj;
		if (id == null) {
			if (other.id != null) {
				return false;
			}
		} else if (!id.equals(other.id)) {
			return false;
		}
		if (inventory != other.inventory) {
			return false;
		}
		if (isin == null) {
			if (other.isin != null) {
				return false;
			}
		} else if (!isin.equals(other.isin)) {
			return false;
		}
		if (name == null) {
			if (other.name != null) {
				return false;
			}
		} else if (!name.equals(other.name)) {
			return false;
		}
		if (symbol == null) {
			if (other.symbol != null) {
				return false;
			}
		} else if (!symbol.equals(other.symbol)) {
			return false;
		}
		if (type != other.type) {
			return false;
		}
		if (wkn == null) {
			if (other.wkn != null) {
				return false;
			}
		} else if (!wkn.equals(other.wkn)) {
			return false;
		}
		return true;
	}

	/**
	 * {@inheritDoc}
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("Security [id=").append(id).append(", name=").append(name).append(", isin=").append(isin).append("]");
		return builder.toString();
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

}