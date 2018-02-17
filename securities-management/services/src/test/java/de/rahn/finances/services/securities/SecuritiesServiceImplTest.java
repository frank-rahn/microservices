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
import static java.util.Collections.singletonList;
import static java.util.UUID.randomUUID;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.isNull;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
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

	private static final Pageable zeroPage = PageRequest.of(0, 10);

	@Mock
	private SecuritiesRepository securitiesRepository;

	@Mock
	private EntriesRepository entriesRepository;

	private SecuritiesServiceImpl classUnderTests;

	private Optional<Security> testSecurity;

	private Optional<Entry> testEntry;

	/**
	 * Initialisierung
	 */
	@Before
	public void setUp() throws Exception {
		classUnderTests = new SecuritiesServiceImpl(securitiesRepository, entriesRepository);

		Security security = new Security();
		security.setId(randomUUID().toString());
		security.setIsin("DE0000000000");
		security.setWkn("000000");
		security.setSymbol("ABC");
		security.setName("ABC AG");
		security.setType(stock);

		testSecurity = Optional.of(security);

		List<Security> allSecurity = singletonList(security);

		PageImpl<Security> pageable1 = new PageImpl<>(allSecurity, PageRequest.of(0, 1), allSecurity.size() + 1);
		PageImpl<Security> pageable2 = new PageImpl<>(emptyList(), PageRequest.of(1, 1), allSecurity.size() + 1);
		PageImpl<Security> pageable3 = new PageImpl<>(emptyList(), zeroPage, 0);

		when(securitiesRepository.findAll()).thenReturn(allSecurity);

		when(securitiesRepository.findById(anyString())).thenReturn(Optional.empty());
		when(securitiesRepository.findById((String) null)).thenThrow(new IllegalArgumentException());
		when(securitiesRepository.findById(security.getId())).thenReturn(testSecurity);

		when(securitiesRepository.findByInventoryOrderByIsin(any(Pageable.class), eq(true))).thenReturn(pageable2);
		when(securitiesRepository.findByInventoryOrderByIsin(null, true)).thenReturn(pageable1);
		when(securitiesRepository.findByInventoryOrderByIsin(zeroPage, true)).thenReturn(pageable3);
		when(securitiesRepository.findByInventoryAndTypeOrderByIsin(isNull(), eq(false), eq(stock))).thenReturn(pageable1);

		when(securitiesRepository.save(security)).thenReturn(security);

		doThrow(new IllegalArgumentException()).when(securitiesRepository).delete((Security) null);
		doNothing().when(securitiesRepository).delete(security);

		Entry entry = new Entry();
		entry.setId(randomUUID().toString());
		entry.setType(buy);

		testEntry = Optional.of(entry);

		when(entriesRepository.findById(anyString())).thenReturn(Optional.empty());
		when(entriesRepository.findById((String) null)).thenThrow(new IllegalArgumentException());
		when(entriesRepository.findById(entry.getId())).thenReturn(testEntry);

		when(entriesRepository.save(entry)).thenReturn(entry);
	}

	/**
	 * Test method for {@link SecuritiesServiceImpl#getSecurities()}.
	 */
	@Test
	public void testGetSecurities() {
		List<Security> allSecurity = classUnderTests.getSecurities();

		assertThat(allSecurity).isNotNull().hasSize(1).contains(testSecurity.get());
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
		Security security = classUnderTests.getSecurity(testSecurity.get().getId());

		assertThat(security).isNotNull().isEqualTo(testSecurity.get());
	}

	/**
	 * Test method for {@link SecuritiesServiceImpl#getSecurities(Pageable)}.
	 */
	@Test
	public void testGetSecuritiesPageable() {
		Page<Security> page = classUnderTests.getSecurities(null);

		assertThat(page).isNotNull();
		assertThat(page.getNumberOfElements()).isEqualTo(1);
		assertThat(page.getContent()).isNotNull().hasSize(1).contains(testSecurity.get());

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

		page = classUnderTests.getSecurities(PageRequest.of(10, 10));
		assertThat(page).isNotNull();
		assertThat(page.getNumberOfElements()).isEqualTo(0);
		assertThat(page.getContent()).isEmpty();

		page = classUnderTests.getSecurities(false, stock, null);
		assertThat(page).isNotNull();
		assertThat(page.getNumberOfElements()).isEqualTo(1);
		assertThat(page.getContent()).isNotNull().hasSize(1).contains(testSecurity.get());
	}

	/**
	 * Test method for {@link SecuritiesServiceImpl#save(Security)}.
	 */
	@Test
	public void testSaveSecurity() {
		Security security = classUnderTests.save(testSecurity.get());

		assertThat(security).isEqualTo(testSecurity.get());
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
		classUnderTests.delete(testSecurity.get());
	}

	/**
	 * Test method for {@link SecuritiesServiceImpl#getEntry(String)}.
	 */
	@Test
	public void testGetEntry_id_known() {
		Entry entry = classUnderTests.getEntry(testEntry.get().getId());

		assertThat(entry).isNotNull().isEqualTo(testEntry.get());
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
		Entry entry = classUnderTests.save(testEntry.get());

		assertThat(entry).isEqualTo(testEntry.get());
	}

	/**
	 * Test method for {@link SecuritiesServiceImpl#save(Entry)}.
	 */
	@Test
	public void testSaveEntry_2() {
		Entry saveEntry = testEntry.get();
		saveEntry.setId(null);
		Entry savedEntry = classUnderTests.save(saveEntry);

		assertThat(savedEntry).isEqualTo(saveEntry);
	}

}