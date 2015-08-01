/*
 * Copyright © 2015 by Frank W. Rahn. Alle Rechte vorbehalten. All rights reserved.
 */
package de.rahn.finances;

import static org.hamcrest.Matchers.hasSize;
import static org.junit.Assert.assertThat;

import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import de.rahn.finances.domain.Security;
import de.rahn.finances.service.SecuritiesService;

/**
 * Der Test zur Klasse {@link SecuritiesManagementApplication}.
 * @author Frank W. Rahn
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = SecuritiesManagementApplication.class)
public class SecuritiesManagementApplicationTests {

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

}