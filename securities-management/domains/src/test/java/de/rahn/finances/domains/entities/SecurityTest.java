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
package de.rahn.finances.domains.entities;

import static de.rahn.finances.domains.entities.EntryType.buy;
import static de.rahn.finances.domains.entities.EntryType.charges;
import static de.rahn.finances.domains.entities.EntryType.dividend;
import static de.rahn.finances.domains.entities.SecurityType.stock;
import static java.math.BigDecimal.ONE;
import static java.math.BigDecimal.TEN;
import static java.time.LocalDate.now;
import static java.util.Arrays.asList;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.Matchers.nullValue;
import static org.junit.Assert.assertThat;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;

import org.junit.Before;
import org.junit.Test;

/**
 * Teste die Entität {@link Security}.
 *
 * @author Frank W. Rahn
 */
public class SecurityTest {

	/** AUDIT_USER */
	private static final String AUDIT_USER = "UnitTest";

	/** AUDIT_NOW */
	private static final LocalDateTime AUDIT_NOW = LocalDateTime.now();

	/** ID_ENTRY_CHATGES */
	private static final String ID_ENTRY_CHATGES = "8ad72f6f-2a39-4846-8943-f6139f3d5597";

	/** ID_ENTRY_BUY */
	private static final String ID_ENTRY_BUY = "8ad72f6f-2a39-4846-8942-f6139f3d5597";

	/** ID_SECURITY */
	private static final String ID_SECURITY = "8ad72f6f-2a39-4846-8940-f6139f3d5597";

	/** NOW */
	private static final LocalDate NOW = now();

	private Security classUnderTest;

	/**
	 * Initialisiere den Test.
	 */
	@Before
	@SuppressWarnings("serial")
	public void setUp() {
		classUnderTest = new Security() {
			{
				setId(ID_SECURITY);
				setIsin("DE0001234560");
				setWkn("123456");
				setName("Firma A AG");
				setSymbol("A01");
				setType(stock);
				setInventory(true);
				setCreateBy(AUDIT_USER);
				setCreateDate(AUDIT_NOW);
				setLastModifiedBy(AUDIT_USER);
				setLastModifiedDate(AUDIT_NOW);
			}
		};

		classUnderTest.setEntries(new ArrayList<>(asList(new Entry() {
			{
				setId(ID_ENTRY_BUY);
				setSecurity(classUnderTest);
				setDate(NOW);
				setNumberOf(TEN);
				setPrice(new BigDecimal("12.345678"));
				setAmount(new BigDecimal("1234.5678"));
				setType(buy);
				setCreateBy(AUDIT_USER);
				setCreateDate(AUDIT_NOW);
				setLastModifiedBy(AUDIT_USER);
				setLastModifiedDate(AUDIT_NOW);
			}
		}, new Entry() {
			{
				setId(ID_ENTRY_CHATGES);
				setSecurity(classUnderTest);
				setDate(NOW);
				setNumberOf(ONE);
				setPrice(new BigDecimal("9.90"));
				setAmount(new BigDecimal("9.90"));
				setType(charges);
				setCreateBy(AUDIT_USER);
				setCreateDate(AUDIT_NOW);
				setLastModifiedBy(AUDIT_USER);
				setLastModifiedDate(AUDIT_NOW);
			}
		})));
	}

	/**
	 * Test method for {@link Security#getEntries()}.
	 */
	@Test
	public void testSecurityInEntry() {
		classUnderTest.getEntries().forEach(e -> assertThat(e.getSecurity(), notNullValue()));
	}

	/**
	 * Test method for {@link Security#getEntry(String)}.
	 */
	@Test
	public void testGetEntryById_1() {
		Entry entry = classUnderTest.getEntry(ID_ENTRY_BUY);

		assertThat(entry, notNullValue());
		assertThat(entry, is(classUnderTest.getEntries().get(0)));
		assertThat(entry.getType(), is(buy));
		assertThat(entry.getDate(), is(NOW));
		assertThat(entry.getNumberOf(), is(TEN));
		assertThat(entry.getPrice(), is(new BigDecimal("12.345678")));
		assertThat(entry.getAmount(), is(new BigDecimal("1234.5678")));
	}

	/**
	 * Test method for {@link Security#getEntry(String)}.
	 */
	@Test(expected = IllegalArgumentException.class)
	public void testGetEntryById_2() {
		classUnderTest.setEntries(null);
		classUnderTest.getEntry(ID_ENTRY_BUY);
	}

	/**
	 * Test method for {@link Security#removeEntry(String)}.
	 */
	@Test
	public void testRemoveEntryById_1() {
		Entry entry = classUnderTest.removeEntry(ID_ENTRY_BUY);

		assertThat(entry, notNullValue());
		assertThat(entry.getSecurity(), nullValue());
		assertThat(classUnderTest.getEntries(), hasSize(1));
		assertThat(classUnderTest.getEntries(), not(hasItem(entry)));
	}

	/**
	 * Test method for {@link Security#removeEntry(String)}.
	 */
	@Test(expected = IllegalArgumentException.class)
	public void testRemoveEntryById_2() {
		classUnderTest.removeEntry("xxx");
	}

	/**
	 * Test method for {@link Security#removeEntry(String)}.
	 */
	@Test(expected = IllegalArgumentException.class)
	public void testRemoveEntryByIdNull() {
		classUnderTest.removeEntry((String) null);
	}

	/**
	 * Test method for {@link Security#removeEntry(Entry)}.
	 */
	@Test(expected = IllegalArgumentException.class)
	public void testRemoveEntryByEntryNull() {
		classUnderTest.removeEntry((Entry) null);
	}

	/**
	 * Test method for {@link Security#replaceEntry(String, Entry)}.
	 */
	@Test(expected = IllegalArgumentException.class)
	public void testReplaceEntry_1() {
		classUnderTest.replaceEntry("xxx", new Entry());
	}

	/**
	 * Test method for {@link Security#replaceEntry(Entry, Entry)}.
	 */
	@Test(expected = IllegalArgumentException.class)
	public void testReplaceEntry_2() {
		classUnderTest.replaceEntry(new Entry(), new Entry());
	}

	/**
	 * Test method for {@link Security#replaceEntry(String, Entry)}.
	 */
	@Test(expected = IllegalArgumentException.class)
	public void testReplaceEntry_3() {
		classUnderTest.replaceEntry(ID_ENTRY_BUY, null);
	}

	/**
	 * Test method for {@link Security#replaceEntry(String, Entry)}.
	 */
	@Test(expected = IllegalArgumentException.class)
	public void testReplaceEntry_4() {
		Entry oldEntry = classUnderTest.getEntry(ID_ENTRY_BUY);
		classUnderTest.setEntries(null);
		classUnderTest.replaceEntry(oldEntry, new Entry());
	}

	/**
	 * Test method for {@link Security#replaceEntry(String, Entry)}.
	 */
	@Test(expected = IllegalArgumentException.class)
	@SuppressWarnings("serial")
	public void testReplaceEntry_5() {
		classUnderTest.replaceEntry(ID_ENTRY_BUY, new Entry() {
			{
				setId("xxx");
			}
		});
	}

	/**
	 * Test method for {@link Security#replaceEntry(String, Entry)}.
	 */
	@Test
	public void testReplaceEntry_6() {
		@SuppressWarnings("serial")
		final Entry newEntry = new Entry() {
			{
				setType(dividend);
			}
		};

		Entry oldEntry = classUnderTest.replaceEntry(ID_ENTRY_BUY, newEntry);

		assertThat(oldEntry, notNullValue());
		assertThat(oldEntry.getId(), is(ID_ENTRY_BUY));
		assertThat(newEntry.getSecurity(), is(classUnderTest));
		assertThat(classUnderTest.getEntries(), hasItem(newEntry));
		assertThat(classUnderTest.getEntries().get(0), is(newEntry));
	}

}