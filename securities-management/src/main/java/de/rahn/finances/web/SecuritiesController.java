/*
 * Copyright © 2015 by Frank W. Rahn. Alle Rechte vorbehalten. All rights reserved.
 */
package de.rahn.finances.web;

import static de.rahn.finances.domain.SecurityType.getKeyValueEntries;
import static java.lang.Boolean.TRUE;
import static org.slf4j.LoggerFactory.getLogger;
import static org.springframework.http.HttpStatus.NO_CONTENT;
import static org.springframework.web.bind.annotation.RequestMethod.DELETE;
import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

import java.util.List;
import java.util.Map.Entry;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Description;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;

import de.rahn.finances.domain.Security;
import de.rahn.finances.domain.SecurityType;
import de.rahn.finances.service.SecuritiesService;
import de.rahn.finances.service.SecurityNotFoundException;

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
		return getKeyValueEntries();
	}

	/**
	 * @return die Liste der anzuzeigenden Wertpapiere
	 */
	@RequestMapping(value = "/securities", method = { GET, POST })
	public String securities(Pageable pageable, Model model) {
		LOGGER.info("Methode aufgerufen: securities({})", pageable);

		model.addAttribute("inventory", TRUE).addAttribute("type", SecurityType.stock).addAttribute("page",
			service.getSecurities(pageable));

		return "securities";
	}

	/**
	 * Zeige die Maske zum erfassen eines Wertpapieres an.
	 * @return das Model mit dem leeren Wertpapier
	 */
	@RequestMapping(value = "/security", method = GET)
	public String security(Model model) {
		LOGGER.info("Methode aufgerufen: security()");

		model.addAttribute("security", new Security());
		return "security";
	}

	/**
	 * Ermittle das anzuzeigene Wertpapier.
	 * @param id die Id des Wertpapiers
	 * @return das Model mit dem Wertpapier
	 */
	@RequestMapping(value = "/security/{id}", method = GET)
	public String security(@PathVariable("id") String id, Model model) {
		LOGGER.info("Methode aufgerufen: security({})", id);

		model.addAttribute("security", service.getSecurity(id));
		return "security";
	}

	/**
	 * Speichere das Wertpapier.
	 * @param security das geänderte Wertpapier
	 * @return die nächste anzuzeigende View
	 */
	@RequestMapping(value = "/security", method = POST)
	public String security(@Valid @ModelAttribute("security") Security security, BindingResult bindingResult) {
		LOGGER.info("Methode aufgerufen: security({})", security);

		if (bindingResult.hasErrors()) {
			return "security";
		}

		try {
			security = service.save(security);
		} catch (Exception exception) {
			bindingResult.addError(new ObjectError("security", exception.toString()));
			return "security";
		}

		return "redirect:/securities";
	}

	/**
	 * Lösche das Wertpapier.
	 * @param id die Id des Wertpapiers
	 * @return der Status
	 */
	@RequestMapping(value = "/security/{id}", method = DELETE)
	public ResponseEntity<String> securityDelete(@PathVariable("id") String id) {
		LOGGER.info("Methode aufgerufen: securityDelete({})", id);

		try {
			service.delete(service.getSecurity(id));
		} catch (SecurityNotFoundException exception) {
			// Alles Gut, da Wertpapier schon gelöscht ist
		} catch (Exception exception) {
			throw new IllegalArgumentException("Das Wertpapier zur ID '" + id + "' konnte nicht gelöscht werden.", exception);
		}

		return new ResponseEntity<>(NO_CONTENT);
	}

	/**
	 * Fehlerbehandlung, wenn ein Wertpapier nicht gefunden wird.
	 */
	@ResponseStatus(value = HttpStatus.NOT_FOUND)
	@ExceptionHandler(SecurityNotFoundException.class)
	public void securityNotFound() {
		// Leer
	}

}