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
package de.rahn.finances.domains.security;

import org.springframework.data.domain.AuditorAware;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

/**
 * Diese Spring bean ermittelt den Namen des aktuellen angemeldeten Benutzers von Spring Security.
 *
 * @author Frank W. Rahn
 */
@Component
public class SpringSecurityAuditorAwareBean implements AuditorAware<String> {

	/**
	 * {@inheritDoc}
	 *
	 * @see org.springframework.data.domain.AuditorAware#getCurrentAuditor()
	 */
	public String getCurrentAuditor() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

		if (authentication == null || !authentication.isAuthenticated()) {
			// Es gibt keinen angemeldeten Benutzer
			return "anonymous";
		}

		return authentication.getName();
	}

}