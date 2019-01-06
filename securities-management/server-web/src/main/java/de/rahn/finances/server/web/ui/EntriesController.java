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
package de.rahn.finances.server.web.ui;

import de.rahn.finances.domains.entities.Entry;
import de.rahn.finances.domains.entities.EntryType;
import de.rahn.finances.services.SecuritiesService;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Description;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import javax.validation.Valid;
import java.util.List;

import static de.rahn.finances.domains.entities.EntryType.getKeyValueEntries;
import static org.slf4j.LoggerFactory.getLogger;

/**
 * Der Controller für die Verwaltung der Buchungen.
 *
 * @author Frank W. Rahn
 */
@Controller
@Description("Der Controller für die Verwaltung der Buchungen")
public class EntriesController {

	private static final Logger LOGGER = getLogger(EntriesController.class);

	private final SecuritiesService service;

	/**
	 * Konstruktor.
	 */
	@Autowired
	public EntriesController(SecuritiesService service) {
		this.service = service;
	}

	/**
	 * @return die Liste der {@link EntryType}s für die Auswahllisten
	 */
	@ModelAttribute("entryTypeList")
	public List<java.util.Map.Entry<String, String>> entryTypeList() {
		return getKeyValueEntries();
	}

	/**
	 * Zeige die ausgewählte Buchung an und ermögliche eine Bearbeitung der Buchung.
	 *
	 * @param id die Id der Buchung
	 * @return die Anzeige der Buchung
	 */
	@GetMapping(path = "/entry/{id}")
	public String entry(@PathVariable("id") String id, Model model) {
		LOGGER.info("GetRequest: entry({})", id);

		Entry entry = service.getEntry(id);
		model.addAttribute("entry", entry);
		return "entry";
	}

	/**
	 * Speichere die Buchung.
	 *
	 * @param entry die Buchung aus dem Formular
	 * @return ein Redirect auf die View des Wertpapiers zu dem die neue Buchung gehört
	 */
	@PostMapping(path = "/entry")
	public String saveEntry(@Valid @ModelAttribute("entry") Entry entry, BindingResult bindingResult) {
		LOGGER.info("PostRequest: entry({})", entry);

		if (bindingResult.hasErrors()) {
			return "entry";
		}

		try {
			entry = service.save(entry);
		} catch (Exception exception) {
			LOGGER.error("Fehler beim Speichern einer Buchung", exception);
			bindingResult.addError(new ObjectError("entry", exception.toString()));
			return "entry";
		}

		return "redirect:/security/" + entry.getSecurity().getId();
	}

}