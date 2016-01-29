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
package de.rahn.finances.domains.entities.util;

import static de.rahn.finances.domains.entities.SecurityType.stock;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

import javax.persistence.Convert;

import org.junit.Test;

import de.rahn.finances.domains.entities.SecurityType;

/**
 * Test des {@link Convert} für {@link SecurityType}.
 *
 * @author Frank W. Rahn
 */
public class SecurityTypeConverterTest {

	private SecurityTypeConverter classAnderTest = new SecurityTypeConverter();

	/**
	 * Test method for {@link SecurityTypeConverter#convertToDatabaseColumn(SecurityType)}.
	 */
	@Test
	public void testConvertToDatabaseColumn() {
		String value = classAnderTest.convertToDatabaseColumn(stock);

		assertThat(value, is(stock.name()));
	}

	/**
	 * Test method for {@link SecurityTypeConverter#convertToEntityAttribute(String)}.
	 */
	@Test
	public void testConvertToEntityAttribute() {
		SecurityType type = classAnderTest.convertToEntityAttribute(stock.name());

		assertThat(type, is(stock));
	}

}
