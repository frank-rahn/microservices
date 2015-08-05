/*
 * Copyright © 2015 by Frank W. Rahn. Alle Rechte vorbehalten. All rights reserved.
 */
package de.rahn.finances.service;

import java.util.List;

import de.rahn.finances.domain.Security;

/**
 * Der interne Service für Wertpapiere.
 * @author Frank W. Rahn
 */
public interface SecuritiesService {
		/**
		 * @return die Wertpapiere
		 */
		List<Security> getSecurities();

	/**
	 * @param id der Identifizierer eines Wertpapiers
	 * @return das Wertpapier
	 */
		Security getSecurity(Long id);

	/**
	 * Speichere das Wertpapier
	 * @param security das Wertpapier
	 * @return das Wertpapier
	 */
		Security save(Security security);
}