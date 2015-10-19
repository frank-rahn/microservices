/*
 * Copyright © 2015 by Frank W. Rahn. Alle Rechte vorbehalten. All rights reserved.
 */
package de.rahn.finances.domains.entities;

import static de.rahn.finances.domains.entities.SecurityType.getKeyValueEntries;
import static de.rahn.finances.domains.entities.SecurityType.other;
import static de.rahn.finances.domains.entities.SecurityType.searchType;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;

import java.util.Map.Entry;

import org.junit.Test;

/**
 * Teste die Enumeration {@link SecurityType}.
 * @author Frank W. Rahn
 */
public class SecurityTypeTest {

	/**
	 * Test method for {@link SecurityType#searchType(String)}.
	 */
	@Test(expected = NullPointerException.class)
	public void testSearchType_01() {
		searchType(null);
		fail("Hier sollte eine Ausnahme geworfen werden");
	}

	/**
	 * Test method for {@link SecurityType#searchType(String)}.
	 */
	@Test(expected = IllegalArgumentException.class)
	public void testSearchType_02() {
		searchType("abcdef-4711");
		fail("Hier sollte eine Ausnahme geworfen werden");
	}

	/**
	 * Test method for {@link SecurityType#searchType(String)}.
	 */
	@Test
	public void testSearchType_03() {
		SecurityType type = searchType("Sonstiges");

		assertThat(type, notNullValue());
		assertThat(type, is(other));
	}

	/**
	 * Test method for {@link SecurityType#getKeyValueEntries()}.
	 */
	@Test
	public void testGetKeyValueEntries() {
		Entry<String, String> entry = getKeyValueEntries().stream().findFirst().get();

		assertThat(entry, notNullValue());
		assertThat(entry.getKey(), is("stock"));
		assertThat(entry.getValue(), is("Aktie"));
	}

}