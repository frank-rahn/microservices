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

import static de.rahn.finances.domains.entities.EntryType.buy;
import static de.rahn.finances.domains.entities.SecurityType.stock;
import static java.util.Collections.emptyList;
import static java.util.UUID.randomUUID;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;
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

import de.rahn.finances.domains.entities.Entry;
import de.rahn.finances.domains.entities.Security;
import de.rahn.finances.domains.repositories.EntriesRepository;
import de.rahn.finances.domains.repositories.SecuritiesRepository;
import de.rahn.finances.services.EntryNotFoundException;
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
	private SecuritiesRepository securitiesRepository;

	@Mock
	private EntriesRepository entriesRepository;

	@InjectMocks
	private SecuritiesServiceImpl classUnderTests = new SecuritiesServiceImpl();

	private Security testSecurity = new Security();

	private Entry testEntry = new Entry();

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

		when(securitiesRepository.findAll()).thenReturn(allSecurity);

		when(securitiesRepository.findOne(anyString())).thenReturn(null);
		when(securitiesRepository.findOne((String) null)).thenThrow(new IllegalArgumentException());
		when(securitiesRepository.findOne(testSecurity.getId())).thenReturn(testSecurity);

		when(securitiesRepository.findByInventoryOrderByIsin(any(Pageable.class), eq(true))).thenReturn(pageable2);
		when(securitiesRepository.findByInventoryOrderByIsin(null, true)).thenReturn(pageable1);
		when(securitiesRepository.findByInventoryOrderByIsin(zeroPage, true)).thenReturn(pageable3);
		when(securitiesRepository.findByInventoryAndTypeOrderByIsin(isNull(Pageable.class), eq(false), eq(stock)))
			.thenReturn(pageable1);

		when(securitiesRepository.save(testSecurity)).thenReturn(testSecurity);

		doThrow(new IllegalArgumentException()).when(securitiesRepository).delete((Security) null);
		doNothing().when(securitiesRepository).delete(testSecurity);

		testEntry.setId(randomUUID().toString());
		testEntry.setType(buy);

		when(entriesRepository.findOne(anyString())).thenReturn(null);
		when(entriesRepository.findOne((String) null)).thenThrow(new IllegalArgumentException());
		when(entriesRepository.findOne(testEntry.getId())).thenReturn(testEntry);

		when(entriesRepository.save(testEntry)).thenReturn(testEntry);
	}

	/**
	 * Test method for {@link SecuritiesServiceImpl#getSecurities()}.
	 */
	@Test
	public void testGetSecurities() {
		List<Security> allSecurity = classUnderTests.getSecurities();

		assertThat(allSecurity).isNotNull().hasSize(1).contains(testSecurity);
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

		assertThat(security).isNotNull().isEqualTo(testSecurity);
	}

	/**
	 * Test method for {@link SecuritiesServiceImpl#getSecurities(Pageable)}.
	 */
	@Test
	public void testGetSecuritiesPageable() {
		Page<Security> page = classUnderTests.getSecurities(null);

		assertThat(page).isNotNull();
		assertThat(page.getNumberOfElements()).isEqualTo(1);
		assertThat(page.getContent()).isNotNull().hasSize(1).contains(testSecurity);

		Pageable pageable = page.nextPageable();
		assertThat(pageable).isNotNull();

		page = classUnderTests.getSecurities(pageable);
		assertThat(page).isNotNull();
		assertThat(page.getNumberOfElements()).isEqualTo(0);
		assertThat(page.getContent()).isEmpty();

		page = classUnderTests.getSecurities(zeroPage);
		assertThat(page).isNotNull();
		assertThat(page.getNumberOfElements()).isEqualTo(0);
		assertThat(page.getContent()).isEmpty();

		page = classUnderTests.getSecurities(new PageRequest(10, 10));
		assertThat(page).isNotNull();
		assertThat(page.getNumberOfElements()).isEqualTo(0);
		assertThat(page.getContent()).isEmpty();

		page = classUnderTests.getSecurities(false, stock, null);
		assertThat(page).isNotNull();
		assertThat(page.getNumberOfElements()).isEqualTo(1);
		assertThat(page.getContent()).isNotNull().hasSize(1).contains(testSecurity);
	}

	/**
	 * Test method for {@link SecuritiesServiceImpl#save(Security)}.
	 */
	@Test
	public void testSaveSecurity() {
		Security security = classUnderTests.save(testSecurity);

		assertThat(security).isEqualTo(testSecurity);
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

	/**
	 * Test method for {@link SecuritiesServiceImpl#getEntry(String)}.
	 */
	@Test
	public void testGetEntry_id_known() {
		Entry entry = classUnderTests.getEntry(testEntry.getId());

		assertThat(entry).isNotNull().isEqualTo(testEntry);
	}

	/**
	 * Test method for {@link SecuritiesServiceImpl#getEntry(String)}.
	 */
	@Test(expected = IllegalArgumentException.class)
	public void testGetEntry_id_null() {
		classUnderTests.getEntry(null);

		fail("Eine IllegalArgumentException hätte geworfen werden müssen");
	}

	/**
	 * Test method for {@link SecuritiesServiceImpl#getEntry(String)}.
	 */
	@Test(expected = EntryNotFoundException.class)
	public void testGetEntry_id_unknown() {
		classUnderTests.getEntry("4711");

		fail("Hier sollte eine Exception geworfen werden");
	}

	/**
	 * Test method for {@link SecuritiesServiceImpl#save(Entry)}.
	 */
	@Test
	public void testSaveEntry_1() {
		Entry entry = classUnderTests.save(testEntry);

		assertThat(entry).isEqualTo(testEntry);
	}

	/**
	 * Test method for {@link SecuritiesServiceImpl#save(Entry)}.
	 */
	@Test
	public void testSaveEntry_2() {
		testEntry.setId(null);
		Entry entry = classUnderTests.save(testEntry);

		assertThat(entry).isEqualTo(testEntry);
	}

}