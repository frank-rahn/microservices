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
package de.rahn.finances.domains.entities.util;

import static de.rahn.finances.domains.entities.SecurityType.valueOf;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

import de.rahn.finances.domains.entities.SecurityType;

/**
 * Ein Konverter für die Aufzählung {@link SecurityType}.
 *
 * @author Frank W. Rahn
 */
@Converter(autoApply = true)
public class SecurityTypeConverter implements AttributeConverter<SecurityType, String> {

	/**
	 * {@inheritDoc}
	 *
	 * @see AttributeConverter#convertToDatabaseColumn(Object)
	 */
	@Override
	public String convertToDatabaseColumn(SecurityType attribute) {
		return attribute.name();
	}

	/**
	 * {@inheritDoc}
	 *
	 * @see AttributeConverter#convertToEntityAttribute(Object)
	 */
	@Override
	public SecurityType convertToEntityAttribute(String dbData) {
		return valueOf(dbData);
	}

}