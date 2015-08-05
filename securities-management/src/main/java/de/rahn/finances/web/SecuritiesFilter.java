/*
 * Copyright © 2015 by Frank W. Rahn. Alle Rechte vorbehalten. All rights reserved.
 */
package de.rahn.finances.web;

import java.io.Serializable;

import de.rahn.finances.domain.SecurityType;

/**
 * Der Filter für die Wertpapiere.
 * @author Frank W. Rahn
 */
public class SecuritiesFilter implements Serializable {

	private static final long serialVersionUID = 1L;

	private boolean inventory = true;

	private SecurityType type;

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
	 * {@inheritDoc}
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("SecuritiesFilter [inventory=");
		builder.append(inventory);
		builder.append(", ");
		if (type != null) {
			builder.append("type=");
			builder.append(type);
		}
		builder.append("]");
		return builder.toString();
	}

}