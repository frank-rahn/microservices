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
package de.rahn.finances.domains.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import de.rahn.finances.domains.entities.Security;
import de.rahn.finances.domains.entities.SecurityType;

/**
 * Der Zugriff auf die Wertpapiere.
 * 
 * @author Frank W. Rahn
 */
public interface SecuritiesRepository extends
	JpaRepository<Security, String> {

	/**
	 * Liefere die {@link Page} der Wertpapiere unter der Berücksichtigung der Filterparameter.
	 * 
	 * @param pageable die Information über die Paginierung
	 * @param inventory Filter: <code>true</code>, nur der aktuelle Bestand wird angezeigt
	 * @param type Filter: nur die Wertpapiere dieser Art anzeigen
	 * @return Eine Seite der Liste aller gefilterten Wertpapiere
	 */
		Page<Security> findByInventoryOrType(Pageable pageable, boolean inventory, SecurityType type);

}