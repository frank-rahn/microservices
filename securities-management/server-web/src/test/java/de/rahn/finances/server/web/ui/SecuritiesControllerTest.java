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

import static java.lang.Boolean.TRUE;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.notNullValue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.context.WebApplicationContext;

import de.rahn.finances.domains.entities.Security;
import de.rahn.finances.domains.entities.SecurityType;
import de.rahn.finances.server.web.config.SecuritiesManagementApplication;

/**
 * Einen Test für den {@link SecuritiesController}.
 *
 * @author Frank W. Rahn
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = SecuritiesManagementApplication.class)
@WebAppConfiguration
public class SecuritiesControllerTest {

	@Autowired
	private WebApplicationContext webApplicationContext;

	private MockMvc mockMvc;

	/**
	 * Initialisiere diesen Test.
	 */
	@Before
	public void setUp() throws Exception {
		mockMvc = webAppContextSetup(webApplicationContext).build();
	}

	/**
	 * Test method for {@link SecuritiesController#securityTypeList()}.
	 */
	@Test
	public void testAttributeModelSecurityTypeList() throws Exception {
		mockMvc.perform(get("/securities")).andExpect(status().isOk())
			.andExpect(model().attribute("securityTypeList", notNullValue()));
	}

	/**
	 * Test method for {@link SecuritiesController#securities(Pageable, Model)} .
	 */
	@Test
	public void testSecurities_01() throws Exception {
		mockMvc.perform(get("/securities")).andExpect(status().isOk())
			.andExpect(content().string(containsString("DE0001000010"))).andExpect(model().attribute("inventory", TRUE))
			.andExpect(model().attribute("type", SecurityType.stock)).andExpect(model().attribute("page", notNullValue()));
	}

	/**
	 * Test method for {@link SecuritiesController#securities(Pageable, Model)} .
	 */
	@Test
	public void testSecurities_02() throws Exception {
		mockMvc.perform(get("/securities").param("page", "0").param("size", "10")).andExpect(status().isOk())
			.andExpect(content().string(containsString("DE0001000010")));
	}

	/**
	 * Test method for {@link SecuritiesController#securities(Pageable, Model)} .
	 */
	@Test
	public void testSecurities_03() throws Exception {
		mockMvc.perform(get("/securities").param("page", "1").param("size", "10")).andExpect(status().isOk())
			.andExpect(content().string(containsString("DE0002000089")));
	}

	/**
	 * Test method for {@link SecuritiesController#security(String, Model)}.
	 *
	 * @throws Exception falls ein Fehler auftritt
	 */
	@Test
	public void testSecurityWithoutId() throws Exception {
		mockMvc.perform(get("/security")).andExpect(status().isOk())
			.andExpect(content().string(not(containsString("DE0001000010"))))
			.andExpect(model().attribute("security", notNullValue()));
	}

	/**
	 * Test method for {@link SecuritiesController#security(String, Model)}.
	 *
	 * @throws Exception falls ein Fehler auftritt
	 */
	@Test
	public void testSecurityWithId_01_Found() throws Exception {
		mockMvc.perform(get("/security/{id}", "067e6162-3b6f-4ae2-a171-2470b63df001")).andExpect(status().isOk())
			.andExpect(content().string(containsString("DE0001000010")))
			.andExpect(model().attribute("security", notNullValue()));
	}

	/**
	 * Test method for {@link SecuritiesController#security(String, Model)}.
	 *
	 * @throws Exception falls ein Fehler auftritt
	 */
	@Test
	public void testSecurityWithId_02_NotFound() throws Exception {
		mockMvc.perform(get("/security/{id}", "4711")).andExpect(status().isNotFound());
	}

	/**
	 * Test method for {@link SecuritiesController#security(Security, BindingResult)} .
	 */
	@Test
	public void testSecurityPost_01() throws Exception {
		mockMvc.perform(post("/security")).andExpect(status().isOk()).andExpect(content().string(containsString("Speichern")))
			.andExpect(content().string(containsString("may not be null")));
	}

	/**
	 * Test method for {@link SecuritiesController#security(Security, BindingResult)} .
	 */
	@Test
	public void testSecurityPost_02() throws Exception {
		mockMvc.perform(post("/security").param("isin", "")).andExpect(status().isOk())
			.andExpect(content().string(containsString("Speichern")))
			.andExpect(content().string(containsString("muss genau 12 Zeichen lang sein")));
	}

	/**
	 * Test method for {@link SecuritiesController#security(Security, BindingResult)} .
	 */
	@Test
	public void testSecurityPost_03() throws Exception {
		mockMvc
			.perform(post("/security").param("isin", "DE0002000109").param("wkn", "200010").param("name", "Fonds 10 LU")
				.param("symbol", "F10").param("type", "fonds"))
			.andExpect(status().isFound()).andExpect(content().string(not(containsString("Speichern"))))
			.andExpect(view().name("redirect:/securities"));
	}

	/**
	 * Test method for {@link SecuritiesController#security(Security, BindingResult)} .
	 */
	@Test
	public void testSecurityPost_04() throws Exception {
		mockMvc
			.perform(post("/security").param("isin", "SE0001000010").param("wkn", "100001").param("name", "Firma 1 AG")
				.param("symbol", "A01").param("type", "stock"))
			.andExpect(status().isOk()).andExpect(content().string(containsString("could not execute statement")))
			.andExpect(view().name("security"));
	}

	/**
	 * Test method for {@link SecuritiesController#securityDelete(String)}.
	 */
	@Test
	public void testSecurityDelete_01() throws Exception {
		mockMvc.perform(delete("/security/{id}", "067e6162-3b6f-4ae2-a171-2470b63df001")).andExpect(status().isNoContent());
	}

	/**
	 * Test method for {@link SecuritiesController#securityDelete(String)}.
	 */
	@Test
	public void testSecurityDelete_02() throws Exception {
		mockMvc.perform(delete("/security/{id}", "4711")).andExpect(status().isNoContent());
	}

	/**
	 * Test method for {@link SecuritiesController#securityDelete(String)}.
	 */
	@Test
	public void testSecurityDelete_03() throws Exception {
		mockMvc.perform(delete("/security/{id}", "067e6162-3b6f-4ae2-a171-2470b63df001-4711"))
			.andExpect(status().isNoContent());
	}

}