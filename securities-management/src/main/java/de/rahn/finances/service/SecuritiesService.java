/*
 * Copyright © 2015 by Frank W. Rahn. Alle Rechte vorbehalten. All rights reserved.
 */
package de.rahn.finances.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

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
	 * @return die Wertpapiere
	 */
		Page<Security> getSecurities(Pageable pageable);

	/**
	 * @param id der Identifizierer eines Wertpapiers
	 * @return das Wertpapier
	 */
		Security getSecurity(String id);

	/**
	 * Speichere das Wertpapier.
	 * @param security das Wertpapier
	 * @return das Wertpapier
	 */
		Security save(Security security);

	/**
	 * Lösche das Wertpapier.
	 * @param security das Wertpapier
	 */
		void delete(Security security);
}