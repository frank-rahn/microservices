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
package de.rahn.finances.domains.entities;

import static java.util.Arrays.stream;
import static java.util.Collections.unmodifiableList;
import static java.util.Comparator.comparing;
import static java.util.stream.Collectors.toList;

import java.util.AbstractMap.SimpleEntry;
import java.util.List;
import java.util.Map.Entry;

/**
 * Die Art einer Buchung.
 *
 * @author Frank W. Rahn
 */
public enum EntryType {
	charges("Gebühren", true), buy("Wertpapier Kauf", true), sell("Wertpapier Verkauf", false), dividend("Dividende", false),
	interest("Zinsen", false), credit("Sonstige Gutschrift", false), debit("Sonstige Belastung", true),
	reduce("Bestandsminderung", true), raise("Bestandserhöhung", false);

	/** Die Liste der Key-Value-Entries dieser Aufzählung. */
	private static final List<Entry<String, String>> ENTRIES = unmodifiableList(
		stream(values()).sorted(comparing(EntryType::getDescription)).map(s -> s.getListEntry()).collect(toList()));

	/** Die Beschreibung der Art der Buchung. */
	private String description;

	/** Belastet diese Art der Buchung das Konto? */
	private boolean expense;

	/**
	 * @param description die Beschreibung der Art der Buchung
	 * @param expense Belastet diese Art der Buchung das Konto?
	 */
	private EntryType(String description, boolean expense) {
		this.description = description;
		this.expense = expense;
	}

	/**
	 * @param description Die Beschreibung der gesuchten Art der Buchung
	 * @return die Aufzählung
	 * @throws NullPointerException falls die <code>description</code> <code>null</code> ist
	 * @throws IllegalArgumentException falls keine Aufzählung gefunden wird
	 */
	public static EntryType searchType(String description) {
		if (description == null) {
			throw new NullPointerException("Der Parameter 'description' ist null");
		}

		for (EntryType s : EntryType.values()) {
			if (s.description.equals(description)) {
				return s;
			}
		}

		throw new IllegalArgumentException("Die Beschreibung '" + description + "' ist nicht bekannt");
	}

	/**
	 * @return die Liste der Key-Value-Entries dieser Aufzählung
	 */
	public static List<Entry<String, String>> getKeyValueEntries() {
		return ENTRIES;
	}

	/**
	 * @return die Beschreibung der Art der Buchung
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * @return Belastet diese Art der Buchung das Konto?
	 */
	public boolean isExpense() {
		return expense;
	}

	/**
	 * @return Lifere ein {@link Entry} für diese Aufzählung
	 */
	public Entry<String, String> getListEntry() {
		return new SimpleEntry<>(name(), getDescription());
	}

}