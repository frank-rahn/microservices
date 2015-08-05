/*
 * Copyright © 2015 by Frank W. Rahn. Alle Rechte vorbehalten. All rights reserved.
 */
package de.rahn.finances.repository;

import java.util.List;

import org.springframework.data.repository.Repository;

import de.rahn.finances.domain.Security;

/**
 * Der Zugriff auf die Wertpapiere.
 * @author Frank W. Rahn
 */
public interface SecuritiesRepository extends
	Repository<Security, Long> {
			/**
			 * @return alle gespeicherten Wertpapiere
			 */
			List<Security> findAll();

	/**
	 * Liefere ein Wertpapier.
	 * @param id der Identifizierer des Wertpapiers
	 * @return das Wertpapier
	 */
		Security findOne(Long id);

	/**
	 * Speichere ein Wertpapier.
	 * @param security das Wertpapier
	 * @return das Wertpapier
	 */
		Security save(Security security);
}