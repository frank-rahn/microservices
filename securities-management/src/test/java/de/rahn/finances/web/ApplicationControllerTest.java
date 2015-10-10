/*
 * Copyright © 2015 by Frank W. Rahn. Alle Rechte vorbehalten. All rights reserved.
 */
package de.rahn.finances.web;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.ConfigFileApplicationContextInitializer;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.context.WebApplicationContext;

import de.rahn.finances.SecuritiesManagementApplication;

/**
 * Einen Test für den {@link ApplicationController}.
 * @author Frank W. Rahn
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = SecuritiesManagementApplication.class,
	initializers = ConfigFileApplicationContextInitializer.class)
@WebAppConfiguration
public class ApplicationControllerTest {

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
	 * Test method for {@link ApplicationController#index()}.
	 */
	@Test
	public void testIndex() throws Exception {
		mockMvc.perform(get("/")).andExpect(status().isOk()).andExpect(view().name("index"))
			.andExpect(content().string(containsString("Wertpapierverwaltung")));
	}

	/**
	 * Test method for {@link ApplicationController#info()}.
	 */
	@Test
	public void testInfo() throws Exception {
		mockMvc.perform(get("/info")).andExpect(status().isOk()).andExpect(view().name("info"))
			.andExpect(content().string(containsString("Management-API")));
	}

}
