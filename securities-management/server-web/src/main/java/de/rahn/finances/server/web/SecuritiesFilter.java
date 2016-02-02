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
package de.rahn.finances.server.web;

import java.io.Serializable;

import de.rahn.finances.domains.entities.SecurityType;

/**
 * Der Filter für die Wertpapiere.
 * 
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
	 * 
	 * @see Object#toString()
	 */
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder().append("SecuritiesFilter [inventory=").append(inventory);

		if (type != null) {
			builder.append(", type=").append(type);
		}

		return builder.append("]").toString();
	}

}