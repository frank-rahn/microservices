/*
 * Copyright 2011-2015 the original author or authors.
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
 * 
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