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

import static org.slf4j.LoggerFactory.getLogger;
import static org.springframework.web.bind.annotation.RequestMethod.GET;

import org.slf4j.Logger;
import org.springframework.context.annotation.Description;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Der Controller für die Verwaltung der Application.
 * 
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