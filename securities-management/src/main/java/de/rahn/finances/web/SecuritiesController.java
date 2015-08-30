/*
 * Copyright © 2015 by Frank W. Rahn. Alle Rechte vorbehalten. All rights reserved.
 */
package de.rahn.finances.web;

import static org.slf4j.LoggerFactory.getLogger;
import static org.springframework.web.bind.annotation.RequestMethod.DELETE;
import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

import java.util.List;
import java.util.Map.Entry;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Description;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import de.rahn.finances.domain.Security;
import de.rahn.finances.domain.SecurityType;
import de.rahn.finances.service.SecuritiesService;

/**
 * Der Controller für die Verwaltung der Wertpapiere.
 * @author Frank W. Rahn
 */
@Controller
@Description("Der Controller für die Verwaltung der Wertpapiere")
public class SecuritiesController {
	private static final Logger LOGGER = getLogger(SecuritiesController.class);

	@Autowired
	private SecuritiesService service;

	/**
	 * @return die Liste der {@link SecurityType}s für die Auswahllisten
	 */
	@ModelAttribute("securityTypeList")
	public List<Entry<String, String>> securityTypeList() {
		return SecurityType.getKeyValueEntries();
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
	public String securities(Pageable pageable, Model model) {
		LOGGER.info("Methode aufgerufen: securities({})", pageable);
		model.addAttribute("inventory", Boolean.TRUE).addAttribute("type", SecurityType.stock).addAttribute("page",
			service.getSecurities(pageable));
		return "securities";
	}

	/**
	 * Ermittle das anzuzeigene Wertpapier.
	 * @param id die Id des Wertpapiers
	 * @return das Model mit dem Wertpapier
	 */
	@RequestMapping(value = "/security", method = GET)
	public ModelAndView security() {
		LOGGER.info("Methode aufgerufen: security()");
		return new ModelAndView("security").addObject("security", new Security());
	}

	/**
	 * Speichere das Wertpapier.
	 * @param security das geänderte Wertpapier
	 * @return die nächste anzuzeigende View
	 */
	@RequestMapping(value = "/security", method = POST)
	public String security(@ModelAttribute("security") Security security) {
		LOGGER.info("Methode aufgerufen: security({})", security);
		service.save(security);
		return "redirect:/securities";
	}

	/**
	 * Ermittle das anzuzeigene Wertpapier.
	 * @param id die Id des Wertpapiers
	 * @return das Model mit dem Wertpapier
	 */
	@RequestMapping(value = "/security/{id}", method = GET)
	public ModelAndView security(@PathVariable("id") Long id) {
		LOGGER.info("Methode aufgerufen: security({})", id);
		return new ModelAndView("security").addObject("security", service.getSecurity(id));
	}

	/**
	 * Lösche das Wertpapier.
	 * @param id die Id des Wertpapiers
	 * @return die nächste anzuzeigende View
	 */
	@RequestMapping(value = "/security/{id}", method = DELETE)
	public String securityDelete(@PathVariable("id") Long id) {
		LOGGER.info("Methode aufgerufen: securityDelete({})", id);
		service.save(service.getSecurity(id));
		return "redirect:/securities";
	}
}