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
package de.rahn.finances.services;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import de.rahn.finances.domains.entities.Entry;
import de.rahn.finances.domains.entities.Security;
import de.rahn.finances.domains.entities.SecurityType;

/**
 * Der interne Service für Wertpapiere.
 *
 * @author Frank W. Rahn
 */
public interface SecuritiesService {

	/**
	 * Liefere alle Wertpapiere.
	 *
	 * @return die Wertpapiere
	 */
	List<Security> getSecurities();

	/**
	 * Liefere alle Wertpapiere die einen Bestand haben.
	 *
	 * @param pageable die gewünschte {@link Page}
	 * @return alle Wertpapiere mit einem Bestand
	 */
	default Page<Security> getSecurities(Pageable pageable) {
		return getSecurities(true, pageable);
	}

	/**
	 * Liefere alle Wertpapiere in Abhängigkeit zum Bestand.
	 *
	 * @param inventory Gibt es zum Wertpapier hat einen Bestand?
	 * @param pageable die gewünschte {@link Page}
	 * @return alle Wertpapiere
	 */
	default Page<Security> getSecurities(boolean inventory, Pageable pageable) {
		return getSecurities(inventory, null, pageable);
	}

	/**
	 * Falls die angeforderte Seite ausserhalb des zuslässigen Bereiches liegt, wird die letzte Seite geliefert.
	 *
	 * @param inventory Gibt es zum Wertpapier hat einen Bestand?
	 * @param type die gewünschte Art des Wertpapiers, <code>null</code> liefert alle
	 * @return alle Wertpapiere
	 */
	Page<Security> getSecurities(boolean inventory, SecurityType type, Pageable pageable);

	/**
	 * Leifere das Wertpapier.
	 *
	 * @param id der Identifizierer eines Wertpapiers
	 * @return das Wertpapier
	 * @throws SecurityNotFoundException, wenn das Wertpapier nicht gefunden wird
	 */
	Security getSecurity(String id);

	/**
	 * Speichere das Wertpapier.
	 *
	 * @param security das Wertpapier
	 * @return das Wertpapier
	 */
	Security save(Security security);

	/**
	 * Lösche das Wertpapier.
	 *
	 * @param security das Wertpapier
	 */
	void delete(Security security);

	/**
	 * Liefere die Buchung.
	 *
	 * @param id der Identifizierer einer Buchung
	 * @return die Buchung
	 * @throws EntryNotFoundException, wenn die Buchung nicht gefunden wird
	 */
	Entry getEntry(String id);

	/**
	 * Speichere die Buchung.
	 *
	 * @param entry die zuspeicherne Buchung
	 * @return die Buchung
	 */
	Entry save(Entry entry);

}