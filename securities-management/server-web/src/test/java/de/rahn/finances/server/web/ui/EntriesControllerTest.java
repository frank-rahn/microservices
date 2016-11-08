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
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import java.time.LocalDateTime;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;

import de.rahn.finances.domains.entities.Entry;
import de.rahn.finances.domains.entities.Security;
import de.rahn.finances.server.web.config.WebMvcConfiguration;
import de.rahn.finances.server.web.config.WebSecurityConfiguration;
import de.rahn.finances.services.SecuritiesService;

/**
 * Einen Test für den {@link SecuritiesController}.
 *
 * @author Frank W. Rahn
 */
@RunWith(SpringRunner.class)
@ContextConfiguration(classes = { WebMvcConfiguration.class, WebSecurityConfiguration.class })
@WithMockUser(username = "user", roles = "USER")
@WebMvcTest(controllers = { EntriesController.class }, secure = true)
public class EntriesControllerTest {

	/** ID */
	private static final String ID = "067e6162-3b6f-4ae2-a171-2470b63df001";

	/** ID */
	private static final String ID_ENTRY = "067e6162-3b6f-4ae2-a172-2470b63df001";

	/** ISIN */
	private static final String ISIN = "DE0001000010";

	private static final Entry ENTRY = new Entry() {
		{
			setId(ID_ENTRY);
		}
	};

	private static final Security SEC = new Security() {
		{
			setId(ID);
			setIsin(ISIN);
			setType(stock);
			setInventory(true);
			setLastModifiedBy("user");
			setLastModifiedDate(LocalDateTime.now());
			setCreateBy("user");
			setCreateDate(LocalDateTime.now());

			addEntry(ENTRY);
			ENTRY.setId(ID_ENTRY);
		}
	};

	@MockBean
	private SecuritiesService securitiesService;

	@Autowired
	private MockMvc mockMvc;

	/**
	 * Initialisiere die Testgrößen.
	 */
	@Before
	public void setup() {
		when(securitiesService.getSecurity(ID)).thenReturn(SEC);

		when(securitiesService.getEntry(ID_ENTRY)).thenReturn(ENTRY);

		when(securitiesService.save(any(Security.class))).thenAnswer(invocation -> {
			String isin = invocation.getArgumentAt(0, Security.class).getIsin();
			assertThat(isin).isEqualTo(ISIN);

			return null;
		});
	}

	/**
	 * Test method for {@link EntriesController#entryTypeList()}.
	 */
	@Test
	public void testAttributeModelEntryTypeList() throws Exception {
		mockMvc.perform(get("/entry/" + ID_ENTRY)).andExpect(status().isOk())
			.andExpect(model().attribute("entryTypeList", notNullValue()));
	}

	/**
	 * Test method for {@link EntriesController#entry(String, Model)} .
	 */
	@Test
	public void testEntry_01() throws Exception {
		mockMvc.perform(get("/entry/" + ID_ENTRY)).andExpect(status().isOk()).andExpect(model().attribute("entry", is(ENTRY)))
			.andExpect(content().string(containsString(ID_ENTRY)));
	}

	/**
	 * Test method for {@link EntriesController#saveEntry(String, Entry, BindingResult)} .
	 */
	@Test
	public void testSaveEntry_01() throws Exception {
		mockMvc.perform(post("/entry/" + ID_ENTRY).with(csrf())).andExpect(status().isOk()).andExpect(view().name("entry"));
	}

}