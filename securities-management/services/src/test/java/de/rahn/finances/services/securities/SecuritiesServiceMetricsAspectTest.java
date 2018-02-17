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
import static de.rahn.finances.services.securities.SecuritiesServiceMetricsAspect.PREFIX_METRICNAME_CALL;
import static de.rahn.finances.services.securities.SecuritiesServiceMetricsAspect.PREFIX_METRICNAME_CALLS;
import static de.rahn.finances.services.securities.SecuritiesServiceMetricsAspect.PREFIX_METRICNAME_ERROR;
import static de.rahn.finances.services.securities.SecuritiesServiceMetricsAspect.PREFIX_METRICNAME_ERRORS;
import static de.rahn.finances.services.securities.SecuritiesServiceMetricsAspect.PREFIX_METRICNAME_EVENTS;
import static de.rahn.finances.services.securities.SecuritiesServiceMetricsAspect.PREFIX_METRICNAME_TIMER;
import static java.util.UUID.randomUUID;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.aop.aspectj.annotation.AspectJProxyFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import de.rahn.finances.domains.entities.Security;
import de.rahn.finances.services.SecuritiesService;
import de.rahn.finances.services.SecurityNotFoundException;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;

/**
 * Der Test für den Aspekt {@link SecuritiesServiceMetricsAspect}.
 *
 * @author Frank W. Rahn
 */
@RunWith(MockitoJUnitRunner.class)
public class SecuritiesServiceMetricsAspectTest {

	@Mock
	private SecuritiesService service;

	@Mock
	private MeterRegistry meterRegistry;

	@InjectMocks
	private SecuritiesServiceMetricsAspect classUnderTests = new SecuritiesServiceMetricsAspect();

	private SecuritiesService serviceProxy;

	private final Security testSecurity = new Security();

	private final List<String> counters = new ArrayList<>();

	private final List<String> gauges = new ArrayList<>();

	/**
	 * Initialisierung
	 */
	@Before
	public void setUp() {
		counters.clear();
		gauges.clear();

		AspectJProxyFactory factory = new AspectJProxyFactory(service);
		factory.addAspect(classUnderTests);

		serviceProxy = factory.getProxy();

		testSecurity.setId(randomUUID().toString());
		testSecurity.setIsin("DE0000000000");
		testSecurity.setWkn("000000");
		testSecurity.setSymbol("ABC");
		testSecurity.setName("ABC AG");
		testSecurity.setType(stock);

		List<Security> allSecurity = new ArrayList<>();
		allSecurity.add(testSecurity);

		PageImpl<Security> page = new PageImpl<>(allSecurity, PageRequest.of(0, 1), allSecurity.size());

		when(service.getSecurities()).thenReturn(allSecurity);
		when(service.getSecurities(any(Pageable.class))).thenReturn(page);
		when(service.save(testSecurity)).thenReturn(testSecurity);
		when(service.save((Security) null)).thenThrow(new SecurityNotFoundException(""));

		doAnswer(invocation -> {
			counters.add((String) invocation.getArguments()[0]);
			return new Counter() {

				@Override
				public Id getId() {
					return null;
				}

				@Override
				public void increment(double amount) {
				}

				@Override
				public double count() {
					return 0;
				}
			};

		}).when(meterRegistry).counter(anyString());
		doAnswer(invocation -> {
			gauges.add((String) invocation.getArguments()[0]);
			return null;

		}).when(meterRegistry).gauge(anyString(), any());
	}

	/**
	 * Test method for {@link SecuritiesService#getSecurities()}.
	 */
	@Test
	public void testGetSecurities() {
		List<Security> allSecurity = serviceProxy.getSecurities();

		assertThat(allSecurity).isNotNull().hasSize(1).contains(testSecurity);

		assertThat(counters).hasSize(3);
		assertThat(counters.get(0)).startsWith(PREFIX_METRICNAME_EVENTS);
		assertThat(counters.get(1)).startsWith(PREFIX_METRICNAME_CALLS);
		assertThat(counters.get(2)).startsWith(PREFIX_METRICNAME_CALL);
	}

	/**
	 * Test method for {@link SecuritiesService#getSecurities(Pageable)}.
	 */
	@Test
	public void testGetSecuritiesPageable() {
		Page<Security> page = serviceProxy.getSecurities(PageRequest.of(0, 10));

		assertThat(page).isNotNull();
		assertThat(page.getContent()).isNotNull().hasSize(1).contains(testSecurity);

		assertThat(gauges).hasSize(1);
		assertThat(gauges.get(0)).startsWith(PREFIX_METRICNAME_TIMER);
	}

	/**
	 * Test method for {@link SecuritiesService#save(Security)}.
	 */
	@Test
	public void testSave_01() {
		Security security = serviceProxy.save(testSecurity);

		assertThat(security).isNotNull().isEqualTo(testSecurity);

		assertThat(counters).hasSize(3);
		assertThat(counters.get(0)).startsWith(PREFIX_METRICNAME_EVENTS);
		assertThat(counters.get(1)).startsWith(PREFIX_METRICNAME_CALLS);
		assertThat(counters.get(2)).startsWith(PREFIX_METRICNAME_CALL);
	}

	/**
	 * Test method for {@link SecuritiesService#save(Security)}.
	 */
	@Test
	public void testSave_02() {
		try {
			serviceProxy.save((Security) null);
			fail("Hier hätte eine Exception geworfen werden sollen");
		} catch (SecurityNotFoundException expected) {
			// Erwartet
		}

		assertThat(counters).hasSize(2);
		assertThat(counters.get(0)).startsWith(PREFIX_METRICNAME_ERRORS);
		assertThat(counters.get(1)).startsWith(PREFIX_METRICNAME_ERROR);
	}

}