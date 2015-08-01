/*
 * Copyright © 2015 by Frank W. Rahn. Alle Rechte vorbehalten. All rights reserved.
 */
package de.rahn.finances.domain;

/**
 * Die Art eines Wertpapiers.
 * @author Frank W. Rahn
 */
public enum SecurityType {
		stock("Aktie"), fonds("Investmentfonds"), loan("Anleihe"), certificate("Zertifikat"), warrant("Optionsscheine"),
		other("Sonstiges");

	/** Der Name der Art des Wertpapiers. */
	private String name;

	/**
	 * @param name der Name der Art des Wertpapiers
	 */
	private SecurityType(String name) {
		this.name = name;
	}

	/**
	 * @param name der Name der gesuchten Art des Wertpapiers
	 * @return das Suchergebnis oder <code>null</code>
	 */
	public static SecurityType searchText(String name) {
		if (name == null) {
			throw new NullPointerException("Attribut name ist null");
		}

		for (SecurityType s : values()) {
			if (s.name.equals(name)) {
				return s;
			}
		}

		throw new IllegalArgumentException("Der Name '" + name + "' ist nicht bekannt");
	}

	/**
	 * @return der Name der Art des Wertpapiers
	 */
	public String getName() {
		return name;
	}

}