/*
 * Copyright © 2015 by Frank W. Rahn. Alle Rechte vorbehalten. All rights reserved.
 */
package de.rahn.finances.domains.entities.util;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

import de.rahn.finances.domains.entities.SecurityType;

/**
 * Ein Konverter für die Aufzählung {@link SecurityType}.
 * @author Frank W. Rahn
 */
@Converter(autoApply = true)
public class SecurityTypeConverter implements AttributeConverter<SecurityType, String> {

	/**
	 * {@inheritDoc}
	 * @see javax.persistence.AttributeConverter#convertToDatabaseColumn(java.lang.Object)
	 */
	@Override
	public String convertToDatabaseColumn(SecurityType attribute) {
		return attribute.getDescription();
	}

	/**
	 * {@inheritDoc}
	 * @see javax.persistence.AttributeConverter#convertToEntityAttribute(java.lang.Object)
	 */
	@Override
	public SecurityType convertToEntityAttribute(String dbData) {
		return SecurityType.searchType(dbData);
	}

}