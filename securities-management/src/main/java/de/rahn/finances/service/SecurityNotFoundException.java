/*
 * Copyright © 2015 by Frank W. Rahn. Alle Rechte vorbehalten. All rights reserved.
 */
package de.rahn.finances.service;

import static org.springframework.http.HttpStatus.NOT_FOUND;

import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Diese Exception wird geworfen, wenn ein Wertpapier nicht gefunden wird.
 * @author Frank W. Rahn
 */
@ResponseStatus(value = NOT_FOUND)
public class SecurityNotFoundException extends RuntimeException {

	public static final String MESSAFE = "Zur ID '%s' wurde kein Wertpapier gefunden.";

	private static String format(String id) {
		return String.format(MESSAFE, id);
	}

	/**
	 * @param id Die ID des Wertpapieres, welches nicht gefunden wurde
	 * @see RuntimeException#RuntimeException(String)
	 */
	public SecurityNotFoundException(String id) {
		super(format(id));
	}

	/**
	 * @param id Die ID des Wertpapieres, welches nicht gefunden wurde
	 * @param cause eine zusätzlich Ausnahme
	 * @see RuntimeException#RuntimeException(String, Throwable)
	 */
	public SecurityNotFoundException(String id, Throwable cause) {
		super(id, cause);
	}

}