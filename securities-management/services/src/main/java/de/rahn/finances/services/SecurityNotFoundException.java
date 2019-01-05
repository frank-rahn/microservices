/*
 * Copyright 2011-2016 Frank W. Rahn and the project authors.
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package de.rahn.finances.services;

import static org.springframework.http.HttpStatus.NOT_FOUND;

import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Diese Exception wird geworfen, wenn ein Wertpapier nicht gefunden wird.
 *
 * @author Frank W. Rahn
 */
@ResponseStatus(code = NOT_FOUND)
public class SecurityNotFoundException extends RuntimeException {
	/** serialVersionUID */
	private static final long serialVersionUID = 1L;

	private static final String MESSAGE = "Zur ID '%s' wurde kein Wertpapier gefunden.";

	/**
	 * @param id Die ID des Wertpapiers, welches nicht gefunden wurde
	 * @see RuntimeException#RuntimeException(String)
	 */
	public SecurityNotFoundException(String id) {
		super(String.format(MESSAGE, id));
	}

}