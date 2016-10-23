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

import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import de.rahn.finances.server.web.config.WebMvcConfiguration;
import de.rahn.finances.services.SecuritiesService;

/**
 * Einen Test für die einfache Konfiguration der direkten Seitenaufrufe.
 *
 * @author Frank W. Rahn
 */
@RunWith(SpringRunner.class)
@ContextConfiguration(classes = WebMvcConfiguration.class)
@WebMvcTest
public class SimpleViewControllerTest {

	@MockBean
	private SecuritiesService securitiesService;

	@Autowired
	private MockMvc mockMvc;

	/**
	 * Test der Index-Seite.
	 */
	@Test
	public void testIndex() throws Exception {
		mockMvc.perform(get("/")).andExpect(status().isOk()).andExpect(view().name("index"))
			.andExpect(content().string(containsString("Wertpapierverwaltung")));
	}

	/**
	 * Test der Info-Seite.
	 */
	@Test
	public void testInfo() throws Exception {
		mockMvc.perform(get("/info")).andExpect(status().isOk()).andExpect(view().name("info"))
			.andExpect(content().string(containsString("Management-API")));
	}

}