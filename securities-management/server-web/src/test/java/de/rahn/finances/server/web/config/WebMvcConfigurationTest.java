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
package de.rahn.finances.server.web.config;

import static de.rahn.finances.domains.entities.SecurityType.loan;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.Matchers.nullValue;
import static org.junit.Assert.assertThat;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.MOCK;

import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import de.rahn.finances.domains.entities.Security;
import de.rahn.finances.services.SecuritiesService;

/**
 * Der Test zur Klasse {@link SecuritiesManagementApplication}.
 *
 * @author Frank W. Rahn
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = SecuritiesManagementApplication.class, webEnvironment = MOCK)
public class WebMvcConfigurationTest {

	/** SYMBOL */
	private static final String SYMBOL = "301";

	/** NAME */
	private static final String NAME = "Test AG";

	/** WKN */
	private static final String WKN = "300001";

	/** ISIN */
	private static final String ISIN = "DE0003000018";

	@Autowired
	private SecuritiesService service;

	/**
	 * Alle Wertpapiere laden, alledings gibt es noch keine.
	 */
	@Test
	public void loadAllSecurities() {
		List<Security> securities = service.getSecurities();

		assertThat("Die Liste darf nicht leer sein", securities, hasSize(21));
	}

	/**
	 * Alle Wertpapiere laden, alledings gibt es noch keine.
	 */
	@Test
	public void createSecurity() {
		Security security = new Security();
		security.setIsin(ISIN);
		security.setWkn(WKN);
		security.setName(NAME);
		security.setSymbol(SYMBOL);
		security.setType(loan);

		assertThat(security.getId(), nullValue());

		security = service.save(security);

		assertThat(security, notNullValue());
		assertThat(security.getId(), notNullValue());

		security = service.getSecurity(security.getId());

		assertThat(security, notNullValue());
		assertThat(security.getId(), notNullValue());
		assertThat(security.getIsin(), is(ISIN));
		assertThat(security.getWkn(), is(WKN));
		assertThat(security.getName(), is(NAME));
		assertThat(security.getSymbol(), is(SYMBOL));
		assertThat(security.getType(), is(loan));
	}

}