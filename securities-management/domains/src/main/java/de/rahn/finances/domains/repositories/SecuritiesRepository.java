/*
 * Copyright © 2015 by Frank W. Rahn. Alle Rechte vorbehalten. All rights reserved.
 */
package de.rahn.finances.domains.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import de.rahn.finances.domains.entities.Security;
import de.rahn.finances.domains.entities.SecurityType;

/**
 * Der Zugriff auf die Wertpapiere.
 * @author Frank W. Rahn
 */
public interface SecuritiesRepository extends
	JpaRepository<Security, String> {

	/**
	 * Liefere die {@link Page} der Wertpapiere unter der Berücksichtigung der Filterparameter.
	 * @param pageable die Information über die Paginierung
	 * @param inventory Filter: <code>true</code>, nur der aktuelle Bestand wird angezeigt
	 * @param type Filter: nur die Wertpapiere dieser Art anzeigen
	 * @return Eine Seite der Liste aller gefilterten Wertpapiere
	 */
		Page<Security> findByInventoryOrType(Pageable pageable, boolean inventory, SecurityType type);

}