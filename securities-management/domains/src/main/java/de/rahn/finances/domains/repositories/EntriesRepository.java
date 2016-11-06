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

import org.springframework.data.jpa.repository.JpaRepository;

import de.rahn.finances.domains.entities.Entry;

/**
 * Der Zugriff auf die Buchungen.
 *
 * @author Frank W. Rahn
 */
public interface EntriesRepository extends JpaRepository<Entry, String> {
	// Noch werden keine weiteren Zugriffe benötigt
}