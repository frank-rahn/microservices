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
package de.rahn.finances.services.securities;

import static de.rahn.finances.domains.entities.SecurityType.stock;
import static de.rahn.finances.services.securities.SecuritiesServiceMetricsAspect.PREFIX_METRICNAME_CALLS;
import static de.rahn.finances.services.securities.SecuritiesServiceMetricsAspect.PREFIX_METRICNAME_ERRORS;
import static de.rahn.finances.services.securities.SecuritiesServiceMetricsAspect.PREFIX_METRICNAME_EVENTS;
import static de.rahn.finances.services.securities.SecuritiesServiceMetricsAspect.PREFIX_METRICNAME_TIMER;
import static java.util.UUID.randomUUID;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyDouble;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.hamcrest.CoreMatchers;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.aop.aspectj.annotation.AspectJProxyFactory;
import org.springframework.boot.actuate.metrics.CounterService;
import org.springframework.boot.actuate.metrics.GaugeService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import de.rahn.finances.domains.entities.Security;
import de.rahn.finances.services.SecuritiesService;
import de.rahn.finances.services.SecurityNotFoundException;

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
	private CounterService counterService;

	@Mock
	private GaugeService gaugeService;

	@InjectMocks
	private SecuritiesServiceMetricsAspect classUnderTests = new SecuritiesServiceMetricsAspect();

	private SecuritiesService serviceProxy;

	private Security testSecurity = new Security();

	private List<String> counters = new ArrayList<>();

	private List<String> gauges = new ArrayList<>();

	/**
	 * @throws Exception
	 */
	@Before
	public void setUp() throws Exception {
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

		PageImpl<Security> page = new PageImpl<>(allSecurity, new PageRequest(0, 1), allSecurity.size());

		when(service.getSecurities()).thenReturn(allSecurity);
		when(service.getSecurities(any(Pageable.class))).thenReturn(page);
		when(service.save(testSecurity)).thenReturn(testSecurity);
		when(service.save(null)).thenThrow(new SecurityNotFoundException(""));

		doAnswer(invocation -> {
			counters.add((String) invocation.getArguments()[0]);
			return null;

		}).when(counterService).increment(anyString());
		doAnswer(invocation -> {
			gauges.add((String) invocation.getArguments()[0]);
			return null;

		}).when(gaugeService).submit(anyString(), anyDouble());
	}

	/**
	 * Test method for {@link SecuritiesService#getSecurities()}.
	 */
	@Test
	public void testGetSecurities() {
		List<Security> allSecurity = serviceProxy.getSecurities();

		assertThat(allSecurity, notNullValue());
		assertThat(allSecurity.size(), is(1));
		assertThat(allSecurity, contains(testSecurity));

		assertThat(counters.size(), is(3));
		assertThat(counters.get(0), CoreMatchers.startsWith(PREFIX_METRICNAME_EVENTS));
		assertThat(counters.get(1), CoreMatchers.startsWith(PREFIX_METRICNAME_CALLS));
		assertThat(counters.get(2), CoreMatchers.startsWith(PREFIX_METRICNAME_CALLS));
	}

	/**
	 * Test method for {@link SecuritiesService#getSecurities(Pageable)}.
	 */
	@Test
	public void testGetSecuritiesPageable() {
		Page<Security> page = serviceProxy.getSecurities(new PageRequest(0, 10));

		assertThat(page, notNullValue());
		assertThat(page.getContent(), notNullValue());
		assertThat(page.getContent().size(), is(1));
		assertThat(page.getContent(), contains(testSecurity));

		assertThat(gauges.size(), is(1));
		assertThat(gauges.get(0), CoreMatchers.startsWith(PREFIX_METRICNAME_TIMER));
	}

	/**
	 * Test method for {@link SecuritiesService#save(Security)}.
	 */
	@Test
	public void testSave_01() {
		Security security = serviceProxy.save(testSecurity);

		assertThat(security, notNullValue());
		assertThat(security, is(testSecurity));

		assertThat(counters.size(), is(3));
		assertThat(counters.get(0), CoreMatchers.startsWith(PREFIX_METRICNAME_EVENTS));
		assertThat(counters.get(1), CoreMatchers.startsWith(PREFIX_METRICNAME_CALLS));
		assertThat(counters.get(2), CoreMatchers.startsWith(PREFIX_METRICNAME_CALLS));
	}

	/**
	 * Test method for {@link SecuritiesService#save(Security)}.
	 */
	@Test
	public void testSave_02() {
		try {
			serviceProxy.save(null);
			fail("Hier hätte eine Exception geworfen werden sollen");
		} catch (SecurityNotFoundException expected) {
			// Erwarted
		}

		assertThat(counters.size(), is(2));
		assertThat(counters.get(0), CoreMatchers.startsWith(PREFIX_METRICNAME_ERRORS));
		assertThat(counters.get(1), CoreMatchers.startsWith(PREFIX_METRICNAME_ERRORS));
	}

}