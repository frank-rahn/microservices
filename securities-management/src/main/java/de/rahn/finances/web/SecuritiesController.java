/*
 * Copyright © 2015 by Frank W. Rahn. Alle Rechte vorbehalten. All rights reserved.
 */
package de.rahn.finances.web;

import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import de.rahn.finances.domain.Security;
import de.rahn.finances.service.SecuritiesService;

/**
 * Der Controller für die Verwaltung der Wertpapiere.
 * @author Frank W. Rahn
 */
@Controller
public class SecuritiesController {

	private static final Logger LOGGER = LoggerFactory.getLogger(SecuritiesController.class);

	@Autowired
	private SecuritiesService service;

	/**
	 * Diese Methode stellt sicher, dass immer ein Filter vorhanden ist.
	 * @param filter den Filter mit den Daten aus dem Request oder <code>null</code>
	 * @return den Filter aus dem Request oder einen neuen Filter
	 */
	@ModelAttribute("filter")
	public SecuritiesFilter filter(@RequestParam(required = false) SecuritiesFilter filter) {
		LOGGER.info("Methode aufgerufen: filter({})", filter);
		return filter != null ? filter : new SecuritiesFilter();
	}

	/**
	 * Zeide die Startseite an.
	 */
	@RequestMapping(value = "/", method = GET)
	public String index() {
		LOGGER.info("Methode aufgerufen: index()");
		return "index";
	}

	/**
	 * @return die Liste der anzuzeigenden Wertpapiere
	 */
	@RequestMapping(value = "/securities", method = { GET, POST })
	@ModelAttribute("securities")
	public List<Security> securities(@RequestParam(required = false) SecuritiesFilter filter) {
		LOGGER.info("Methode aufgerufen: securities({})", filter);
		return service.getSecurities();
	}

	/**
	 * Ermittle das anzuzeigene Wertpapier.
	 * @param id die Id des Wertpapiers
	 * @return das Model mit dem Wertpapier
	 */
	@RequestMapping(value = "/security/{id}", method = GET)
	public ModelAndView security(@PathVariable("id") long id) {
		LOGGER.info("Methode aufgerufen: security({})", id);
		return new ModelAndView("security").addObject("security", service.getSecurities().get((int) id));
	}

	/**
	 * Speichere das Wertpapier.
	 * @param security das geänderte Wertpapier
	 * @return die nächste anzuzeigende View
	 */
	@RequestMapping(value = "/security", method = POST)
	public String security(@ModelAttribute("security") Security security) {
		LOGGER.info("Methode aufgerufen: security({})", security);
		return "redirect:/securities";
	}

}