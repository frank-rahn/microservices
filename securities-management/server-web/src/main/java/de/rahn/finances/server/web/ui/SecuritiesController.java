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
package de.rahn.finances.server.web.ui;

import static de.rahn.finances.domains.entities.SecurityType.getKeyValueEntries;
import static org.slf4j.LoggerFactory.getLogger;
import static org.springframework.http.ResponseEntity.noContent;

import java.util.List;
import java.util.Map.Entry;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Description;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import de.rahn.finances.domains.entities.Security;
import de.rahn.finances.domains.entities.SecurityType;
import de.rahn.finances.services.SecuritiesService;
import de.rahn.finances.services.SecurityNotFoundException;

/**
 * Der Controller für die Verwaltung der Wertpapiere.
 *
 * @author Frank W. Rahn
 */
@Controller
@Description("Der Controller für die Verwaltung der Wertpapiere")
public class SecuritiesController {

	private static final Logger LOGGER = getLogger(SecuritiesController.class);

	private final SecuritiesService service;

	/**
	 * Konstruktor.
	 */
	@Autowired
	public SecuritiesController(SecuritiesService service) {
		this.service = service;
	}

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
	@GetMapping(path = "/securities")
	public String securities(@RequestParam(name = "inventory", defaultValue = "true") boolean inventory, Pageable pageable,
		Model model) {
		LOGGER.info("GetRequest: securities({}, {})", inventory, pageable);

		model.addAttribute("inventory", inventory).addAttribute("page", service.getSecurities(inventory, pageable));
		return "securities";
	}

	/**
	 * @return die Liste der anzuzeigenden Wertpapiere
	 */
	@GetMapping(path = "/securities/{type}")
	public String securities(@PathVariable("type") SecurityType type,
		@RequestParam(name = "inventory", required = false) boolean inventory, Pageable pageable, Model model) {
		LOGGER.info("GetRequest: securities({}, {}, {})", type, inventory, pageable);

		model.addAttribute("inventory", inventory).addAttribute("type", type).addAttribute("page",
			service.getSecurities(inventory, type, pageable));
		return "securities";
	}

	/**
	 * Zeige die Maske zum erfassen eines Wertpapieres an.
	 *
	 * @return das Model mit dem leeren Wertpapier
	 */
	@GetMapping(path = "/security")
	public String security(Model model) {
		LOGGER.info("GetRequest: security()");

		model.addAttribute("security", new Security());
		return "security";
	}

	/**
	 * Ermittle das anzuzeigene Wertpapier.
	 *
	 * @param id die Id des Wertpapiers
	 * @return das Model mit dem Wertpapier
	 */
	@GetMapping(path = "/security/{id}")
	public String security(@PathVariable("id") String id, Model model) {
		LOGGER.info("GetRequest: security({})", id);

		model.addAttribute("security", service.getSecurity(id));
		return "security";
	}

	/**
	 * Speichere das Wertpapier.
	 *
	 * @param security das geänderte Wertpapier
	 * @return die nächste anzuzeigende View
	 */
	@PostMapping(path = "/security")
	public String security(@Valid @ModelAttribute("security") Security security, BindingResult bindingResult) {
		LOGGER.info("PostRequest: security({})", security);

		if (bindingResult.hasErrors()) {
			return "security";
		}

		try {
			security = service.save(security);
		} catch (Exception exception) {
			LOGGER.error("Fehler beim Speichern eines Wertpapiers", exception);
			bindingResult.addError(new ObjectError("security", exception.toString()));
			return "security";
		}

		return "redirect:/securities";
	}

	/**
	 * Lösche das Wertpapier.
	 *
	 * @param id die Id des Wertpapiers
	 * @return der Status
	 */
	@DeleteMapping(path = "/security/{id}")
	public ResponseEntity<Void> securityDelete(@PathVariable("id") String id) {
		LOGGER.info("DeleteRequest: securityDelete({})", id);

		try {
			service.delete(service.getSecurity(id));
		} catch (SecurityNotFoundException exception) {
			// Alles Gut, da Wertpapier schon gelöscht ist => ignorieren
		} catch (Exception exception) {
			throw new IllegalArgumentException("Das Wertpapier zur ID '" + id + "' konnte nicht gelöscht werden.", exception);
		}

		return noContent().build();
	}

}