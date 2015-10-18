/*
 * Copyright © 2015 by Frank W. Rahn. Alle Rechte vorbehalten. All rights reserved.
 */
package de.rahn.finances.server.web;

import static org.slf4j.LoggerFactory.getLogger;
import static org.springframework.web.bind.annotation.RequestMethod.GET;

import org.slf4j.Logger;
import org.springframework.context.annotation.Description;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Der Controller für die Verwaltung der Application.
 * @author Frank W. Rahn
 */
@Controller
@Description("Der Controller für die Verwaltung der Wertpapiere")
public class ApplicationController {

	private static final Logger LOGGER = getLogger(ApplicationController.class);

	/**
	 * Zeige die Startseite an.
	 */
	@RequestMapping(value = "/", method = GET)
	public String index() {
		LOGGER.info("Methode aufgerufen: index()");

		return "index";
	}

	/**
	 * Zeige die Managementseite an.
	 */
	@RequestMapping(value = "/info", method = GET)
	public String info() {
		LOGGER.info("Methode aufgerufen: info()");

		return "info";
	}

}