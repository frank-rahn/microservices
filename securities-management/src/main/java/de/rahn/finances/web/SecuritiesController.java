/*
 * Copyright © 2015 by Frank W. Rahn. Alle Rechte vorbehalten. All rights reserved.
 */
package de.rahn.finances.web;

import static org.springframework.web.bind.annotation.RequestMethod.GET;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import de.rahn.finances.service.SecuritiesService;

/**
 * Der Controller für die Verwaltung der Wertpapiere.
 * @author Frank W. Rahn
 */
@Controller
public class SecuritiesController {

	@Autowired
	private SecuritiesService service;

	/**
	 * @return die Startseite
	 */
	@RequestMapping(value = "/", method = GET)
	public String home() {
		return "index";
	}

}