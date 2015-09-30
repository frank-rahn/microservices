/*
 * Copyright © 2015 by Frank W. Rahn. Alle Rechte vorbehalten. All rights reserved.
 */
package de.rahn.finances.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.Repository;

import de.rahn.finances.domain.Security;
import de.rahn.finances.domain.SecurityType;

/**
 * Der Zugriff auf die Wertpapiere.
 * @author Frank W. Rahn
 */
public interface SecuritiesRepository extends
	Repository<Security, String> {

	/**
	 * @return alle gespeicherten Wertpapiere
	 */
		List<Security> findAll();

	/**
	 * Liefere die {@link Page} der Wertpapiere unter der Berücksichtigung der Filterparameter.
	 * @param pageable die Information über die Paginierung
	 * @param inventory Filter: <code>true</code>, nur der aktuelle Bestand wird angezeigt
	 * @param type Filter: nur die Wertpapiere dieser Art anzeigen
	 * @return Eine Seite der Liste aller gefilterten Wertpapiere
	 */
		Page<Security> findByInventoryOrType(Pageable pageable, boolean inventory, SecurityType type);

	/**
	 * Liefere ein Wertpapier.
	 * @param id der Identifizierer des Wertpapiers
	 * @return das Wertpapier
	 */
		Security findOne(String id);

	/**
	 * Speichere ein Wertpapier.
	 * @param security das Wertpapier
	 * @return das Wertpapier
	 */
		Security save(Security security);

	/**
	 * Lösche das Wertpapier.
	 * @param id der Identifizierer des Wertpapiers
	 */
		void delete(String id);

}