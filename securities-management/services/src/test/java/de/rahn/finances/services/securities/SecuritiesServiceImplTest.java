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
package de.rahn.finances.services.securities;

import static de.rahn.finances.domains.entities.SecurityType.stock;
import static java.util.Collections.emptyList;
import static java.util.UUID.randomUUID;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.eq;
import static org.mockito.Matchers.isNull;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import de.rahn.finances.domains.entities.Security;
import de.rahn.finances.domains.repositories.SecuritiesRepository;
import de.rahn.finances.services.SecuritiesService;
import de.rahn.finances.services.SecurityNotFoundException;

/**
 * Der Test für die Implementierung des Services {@link SecuritiesService}.
 *
 * @author Frank W. Rahn
 */
@RunWith(MockitoJUnitRunner.class)
public class SecuritiesServiceImplTest {

	private static final Pageable zeroPage = new PageRequest(0, 10);

	@Mock
	private SecuritiesRepository repository;

	@InjectMocks
	private SecuritiesServiceImpl classUnderTests = new SecuritiesServiceImpl();

	private Security testSecurity = new Security();

	/**
	 * @throws Exception
	 */
	@Before
	public void setUp() throws Exception {
		testSecurity.setId(randomUUID().toString());
		testSecurity.setIsin("DE0000000000");
		testSecurity.setWkn("000000");
		testSecurity.setSymbol("ABC");
		testSecurity.setName("ABC AG");
		testSecurity.setType(stock);

		List<Security> allSecurity = new ArrayList<>();
		allSecurity.add(testSecurity);

		PageImpl<Security> pageable1 = new PageImpl<>(allSecurity, new PageRequest(0, 1), allSecurity.size() + 1);
		PageImpl<Security> pageable2 = new PageImpl<>(emptyList(), new PageRequest(1, 1), allSecurity.size() + 1);
		PageImpl<Security> pageable3 = new PageImpl<>(emptyList(), zeroPage, 0);

		when(repository.findAll()).thenReturn(allSecurity);

		when(repository.findOne(anyString())).thenReturn(null);
		when(repository.findOne((String) null)).thenThrow(new IllegalArgumentException());
		when(repository.findOne(testSecurity.getId())).thenReturn(testSecurity);

		when(repository.findByInventory(any(Pageable.class), eq(true))).thenReturn(pageable2);
		when(repository.findByInventory(null, true)).thenReturn(pageable1);
		when(repository.findByInventory(zeroPage, true)).thenReturn(pageable3);
		when(repository.findByInventoryAndType(isNull(Pageable.class), eq(false), eq(stock))).thenReturn(pageable1);

		when(repository.save(testSecurity)).thenReturn(testSecurity);

		doThrow(new IllegalArgumentException()).when(repository).delete((Security) null);
		doNothing().when(repository).delete(testSecurity);
	}

	/**
	 * Test method for {@link SecuritiesServiceImpl#getSecurities()}.
	 */
	@Test
	public void testGetSecurities() {
		List<Security> allSecurity = classUnderTests.getSecurities();

		assertThat(allSecurity, notNullValue());
		assertThat(allSecurity.size(), is(1));
		assertThat(allSecurity, contains(testSecurity));
	}

	/**
	 * Test method for {@link SecuritiesServiceImpl#getSecurity(String)}.
	 */
	@Test(expected = IllegalArgumentException.class)
	public void testGetSecurity_id_null() {
		classUnderTests.getSecurity(null);

		fail("Eine IllegalArgumentException hätte geworfen werden müssen");
	}

	/**
	 * Test method for {@link SecuritiesServiceImpl#getSecurity(String)}.
	 */
	@Test(expected = SecurityNotFoundException.class)
	public void testGetSecurity_id_unknown() {
		classUnderTests.getSecurity("4711");

		fail("Hier sollte eine Exception geworfen werden");
	}

	/**
	 * Test method for {@link SecuritiesServiceImpl#getSecurity(String)}.
	 */
	@Test
	public void testGetSecurity_id_known() {
		Security security = classUnderTests.getSecurity(testSecurity.getId());

		assertThat(security, notNullValue());
		assertThat(security, is(testSecurity));
	}

	/**
	 * Test method for {@link SecuritiesServiceImpl#getSecurities(Pageable)}.
	 */
	@Test
	public void testGetSecuritiesPageable() {
		Page<Security> page = classUnderTests.getSecurities(null);

		assertThat(page, notNullValue());
		assertThat(page.getNumberOfElements(), is(1));
		assertThat(page.getContent(), notNullValue());
		assertThat(page.getContent().size(), is(1));
		assertThat(page.getContent(), contains(testSecurity));

		Pageable pageable = page.nextPageable();
		assertThat(pageable, notNullValue());

		page = classUnderTests.getSecurities(pageable);
		assertThat(page, notNullValue());
		assertThat(page.getNumberOfElements(), is(0));
		assertThat(page.getContent(), empty());

		page = classUnderTests.getSecurities(zeroPage);
		assertThat(page, notNullValue());
		assertThat(page.getNumberOfElements(), is(0));
		assertThat(page.getContent(), empty());

		page = classUnderTests.getSecurities(new PageRequest(10, 10));
		assertThat(page, notNullValue());
		assertThat(page.getNumberOfElements(), is(0));
		assertThat(page.getContent(), empty());

		page = classUnderTests.getSecurities(false, stock, null);
		assertThat(page, notNullValue());
		assertThat(page.getNumberOfElements(), is(1));
		assertThat(page.getContent(), notNullValue());
		assertThat(page.getContent().size(), is(1));
		assertThat(page.getContent(), contains(testSecurity));
	}

	/**
	 * Test method for {@link SecuritiesServiceImpl#save(Security)}.
	 */
	@Test
	public void testSave() {
		Security security = classUnderTests.save(testSecurity);

		assertThat(security, is(testSecurity));
	}

	/**
	 * Test method for {@link SecuritiesServiceImpl#delete(Security)}.
	 */
	@Test(expected = IllegalArgumentException.class)
	public void testDelete_without() {
		classUnderTests.delete(null);

		fail("Eine IllegalArgumentException hätte geworfen werden müssen");
	}

	/**
	 * Test method for {@link SecuritiesServiceImpl#delete(Security)}.
	 */
	@Test
	public void testDelete_with() {
		classUnderTests.delete(testSecurity);
	}

}