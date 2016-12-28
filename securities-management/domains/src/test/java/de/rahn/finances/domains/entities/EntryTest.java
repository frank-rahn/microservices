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

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;

/**
 * Teste die Entität {@link Entry}.
 *
 * @author Frank W. Rahn
 */
public class EntryTest {

	private Entry classUnderTest = new Entry();

	/**
	 * Test method for {@link Entry#update(Entry)}.
	 */
	@Test(expected = IllegalArgumentException.class)
	public void testUpdate_01() {
		classUnderTest.update(null);
	}

	/**
	 * Test method for {@link Entry#update(Entry)}.
	 */
	@Test(expected = IllegalArgumentException.class)
	public void testUpdate_02() {
		Entry entry = new Entry();

		classUnderTest.update(entry);
	}

	/**
	 * Test method for {@link Entry#update(Entry)}.
	 */
	@Test(expected = IllegalStateException.class)
	public void testUpdate_03() {
		Entry entry = new Entry();
		entry.setId("ID");

		classUnderTest.update(entry);
	}

	/**
	 * Test method for {@link Entry#update(Entry)}.
	 */
	@Test(expected = IllegalArgumentException.class)
	public void testUpdate_04() {
		Entry entry = new Entry();
		entry.setId("ID-1");
		classUnderTest.setId("ID-2");

		classUnderTest.update(entry);
	}

	/**
	 * Test method for {@link Entry#update(Entry)}.
	 */
	@Test
	public void testUpdate_05() {
		Entry entry = new Entry();
		entry.setId("ID");
		classUnderTest.setId("ID");

		Entry entry2 = classUnderTest.update(entry);

		assertThat(classUnderTest).isSameAs(entry2).isEqualTo(entry);
	}

}