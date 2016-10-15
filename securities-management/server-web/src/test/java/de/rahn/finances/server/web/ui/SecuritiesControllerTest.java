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

import static de.rahn.finances.domains.entities.SecurityType.stock;
import static java.lang.Boolean.TRUE;
import static java.util.Collections.emptyList;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.Assert.fail;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyBoolean;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.util.NestedServletException;

import de.rahn.finances.domains.entities.Security;
import de.rahn.finances.server.web.config.WebMvcConfiguration;
import de.rahn.finances.services.SecuritiesService;
import de.rahn.finances.services.SecurityNotFoundException;

/**
 * Einen Test für den {@link SecuritiesController}.
 *
 * @author Frank W. Rahn
 */
@RunWith(SpringRunner.class)
@ContextConfiguration(classes = WebMvcConfiguration.class)
@WebMvcTest(SecuritiesController.class)
public class SecuritiesControllerTest {

	/** ID_NOT_FOUND */
	private static final String ID_NOT_FOUND = "4711";

	/** ID_FOUND */
	private static final String ID_FOUND = "067e6162-3b6f-4ae2-a171-2470b63df001";

	/** ISIN_SAVE */
	private static final String ISIN_SAVE = "DE0001000010";

	/** ISIN_NOT_SAVE */
	private static final String ISIN_NOT_SAVE = "SE0001000010";

	/** ISIN */
	private static final String ISIN1 = "DE000100001";

	/** ISIN */
	private static final String ISIN2 = "DE000200008";

	@MockBean
	private SecuritiesService securitiesService;

	@Autowired
	private MockMvc mockMvc;

	/**
	 * Initialisiere die Testgrößen.
	 */
	@Before
	public void setup() {
		when(securitiesService.getSecurities(anyBoolean(), any())).thenAnswer(invocation -> {
			List<Security> securitiesList = new ArrayList<>();

			Pageable pageable = invocation.getArgumentAt(1, Pageable.class);

			if (pageable == null || pageable.getPageNumber() == 0) {
				for (int i = 0; i < 10; i++) {
					Security security = new Security();
					security.setIsin(ISIN1 + i);
					security.setType(stock);
					security.setInventory(true);

					securitiesList.add(security);
				}
			}
			if (pageable == null || pageable.getPageNumber() == 1) {
				for (int i = 0; i < 10; i++) {
					Security security = new Security();
					security.setIsin(ISIN2 + i);
					security.setType(stock);
					security.setInventory(true);

					securitiesList.add(security);
				}
			}

			return new PageImpl<>(securitiesList);
		});

		when(securitiesService.getSecurity(ID_FOUND)).thenReturn(new Security() {
			{
				setId(ID_FOUND);
				setIsin(ISIN1 + "0");
				setType(stock);
				setInventory(true);
			}
		});

		when(securitiesService.getSecurity(ID_NOT_FOUND)).thenThrow(new SecurityNotFoundException(ID_NOT_FOUND));

		when(securitiesService.save(any(Security.class))).thenAnswer(invocation -> {
			String isin = invocation.getArgumentAt(0, Security.class).getIsin();

			if (ISIN_NOT_SAVE.equals(isin)) {
				throw new RuntimeException("could not execute statement");
			}

			return null;
		});
	}

	/**
	 * Test method for {@link SecuritiesController#securityTypeList()}.
	 */
	@Test
	public void testAttributeModelSecurityTypeList() throws Exception {
		when(securitiesService.getSecurities(anyBoolean(), any())).thenReturn(new PageImpl<>(emptyList()));

		mockMvc.perform(get("/securities")).andExpect(status().isOk())
			.andExpect(model().attribute("securityTypeList", notNullValue()));
	}

	/**
	 * Test method for {@link SecuritiesController#securities(boolean, Pageable, Model)} .
	 */
	@Test
	public void testSecurities_01() throws Exception {
		mockMvc.perform(get("/securities")).andExpect(status().isOk()).andExpect(content().string(containsString(ISIN1 + "0")))
			.andExpect(model().attribute("inventory", TRUE)).andExpect(model().attribute("page", notNullValue()));
	}

	/**
	 * Test method for {@link SecuritiesController#securities(boolean, Pageable, Model)} .
	 */
	@Test
	public void testSecurities_02() throws Exception {
		mockMvc.perform(get("/securities").param("page", "0").param("size", "10")).andExpect(status().isOk())
			.andExpect(content().string(containsString(ISIN1 + "0")));
	}

	/**
	 * Test method for {@link SecuritiesController#securities(boolean, Pageable, Model)} .
	 */
	@Test
	public void testSecurities_03() throws Exception {
		mockMvc.perform(get("/securities").param("page", "1").param("size", "10")).andExpect(status().isOk())
			.andExpect(content().string(containsString(ISIN2 + "9")));
	}

	/**
	 * Test method for {@link SecuritiesController#security(String, Model)}.
	 *
	 * @throws Exception falls ein Fehler auftritt
	 */
	@Test
	public void testSecurityWithoutId() throws Exception {
		mockMvc.perform(get("/security")).andExpect(status().isOk())
			.andExpect(content().string(not(containsString(ISIN1 + "0"))))
			.andExpect(model().attribute("security", notNullValue()));
	}

	/**
	 * Test method for {@link SecuritiesController#security(String, Model)}.
	 *
	 * @throws Exception falls ein Fehler auftritt
	 */
	@Test
	public void testSecurityWithId_01_Found() throws Exception {
		mockMvc.perform(get("/security/{id}", ID_FOUND)).andExpect(status().isOk())
			.andExpect(content().string(containsString(ISIN1 + "0"))).andExpect(model().attribute("security", notNullValue()));
	}

	/**
	 * Test method for {@link SecuritiesController#security(String, Model)}.
	 *
	 * @throws Exception falls ein Fehler auftritt
	 */
	@Test
	public void testSecurityWithId_02_NotFound() throws Exception {
		mockMvc.perform(get("/security/{id}", ID_NOT_FOUND)).andExpect(status().isNotFound());
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
			.perform(post("/security").param("isin", ISIN_SAVE).param("wkn", "200010").param("name", "Fonds 10 LU")
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
			.perform(post("/security").param("isin", ISIN_NOT_SAVE).param("wkn", "100001").param("name", "Firma 1 AG")
				.param("symbol", "A01").param("type", "stock"))
			.andExpect(status().isOk()).andExpect(content().string(containsString("could not execute statement")))
			.andExpect(view().name("security"));
	}

	/**
	 * Test method for {@link SecuritiesController#securityDelete(String)}.
	 */
	@Test
	public void testSecurityDelete_01() throws Exception {
		doNothing().when(securitiesService).delete(any(Security.class));

		mockMvc.perform(delete("/security/{id}", ID_FOUND)).andExpect(status().isNoContent());
	}

	/**
	 * Test method for {@link SecuritiesController#securityDelete(String)}.
	 */
	@Test
	public void testSecurityDelete_02() throws Exception {
		doThrow(new SecurityNotFoundException("02")).when(securitiesService).delete(any(Security.class));

		mockMvc.perform(delete("/security/{id}", ID_NOT_FOUND)).andExpect(status().isNoContent());
	}

	/**
	 * Test method for {@link SecuritiesController#securityDelete(String)}.
	 */
	@Test
	public void testSecurityDelete_03() throws Exception {
		doThrow(new SecurityNotFoundException("03")).when(securitiesService).delete(any(Security.class));

		mockMvc.perform(delete("/security/{id}", ID_FOUND + "-4711")).andExpect(status().isNoContent());
	}

	/**
	 * Test method for {@link SecuritiesController#securityDelete(String)}.
	 */
	@Test(expected = NestedServletException.class)
	public void testSecurityDelete_04() throws Exception {
		doThrow(new NullPointerException("04")).when(securitiesService).delete(any(Security.class));

		mockMvc.perform(delete("/security/{id}", ID_NOT_FOUND + "-not-delete")).andExpect(status().isNoContent());

		fail("Es hätte eine Exception geworfen werden müssen");
	}

}
