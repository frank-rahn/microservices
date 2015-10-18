/*
 * Copyright © 2015 by Frank W. Rahn. Alle Rechte vorbehalten. All rights reserved.
 */
package de.rahn.finances;

import static de.rahn.finances.domains.entities.SecurityType.loan;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.Matchers.nullValue;
import static org.junit.Assert.assertThat;

import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import de.rahn.finances.domains.entities.Security;
import de.rahn.finances.service.SecuritiesService;

/**
 * Der Test zur Klasse {@link SecuritiesManagementApplication}.
 * @author Frank W. Rahn
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = SecuritiesManagementApplication.class)
public class SecuritiesManagementApplicationTests {

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

		assertThat(security.isNew(), is(true));
		assertThat(security.getId(), nullValue());

		security = service.save(security);

		assertThat(security, notNullValue());
		assertThat(security.isNew(), is(false));
		assertThat(security.getId(), notNullValue());

		security = service.getSecurity(security.getId());

		assertThat(security, notNullValue());
		assertThat(security.isNew(), is(false));
		assertThat(security.getId(), notNullValue());
		assertThat(security.getIsin(), is(ISIN));
		assertThat(security.getWkn(), is(WKN));
		assertThat(security.getName(), is(NAME));
		assertThat(security.getSymbol(), is(SYMBOL));
		assertThat(security.getType(), is(loan));
	}

}