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
package de.rahn.finances.server.web.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configurers.provisioning.InMemoryUserDetailsManagerConfigurer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

/**
 * Die Konfiguration für Spring Security Web.
 *
 * @author Frank W. Rahn
 */
@Configuration
@EnableWebSecurity
public class WebSecurityConfiguration extends WebSecurityConfigurerAdapter {

	/**
	 * {@inheritDoc}
	 *
	 * @see WebSecurityConfigurerAdapter#configure(AuthenticationManagerBuilder)
	 */
	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		// @formatter:off
		new InMemoryUserDetailsManagerConfigurer<AuthenticationManagerBuilder>()
			// User mit erweiterten Rechten
				.withUser("admin").password("admin").roles("USER", "ADMIN")
			// User mit Leserecheten
			.and().withUser("user").password("user").roles("USER")
			// User mit keinen Rechten
			.and().withUser("gast").password("gast").roles("GAST")
			// So jetzt in den Builder
			.and().configure(auth);
		// @formatter:on
	}

	/**
	 * {@inheritDoc}
	 *
	 * @see WebSecurityConfigurerAdapter#configure(HttpSecurity)
	 */
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		// @formatter:off
		http
			.authorizeRequests()
				.anyRequest().hasRole("USER")
				// Konfiguration der Login-Seite
				.and().formLogin().loginPage("/login")
					// Alle dürfen auf die Login-Seite zugreifen
					.permitAll()
					// Diese Seite wird nach einem Fehler bei der Anmeldung angezeigt (Default)
					//.failureUrl("/login?error")
				// Konfiguration des Logouts
				.and().logout().permitAll()
					// Beim Logout alle gesetzten Cookies wieder löschen
					.deleteCookies()
					// Diese Seite wird nach einem Fehler bei der Abmeldung angezeigt (Default)
					//.logoutSuccessUrl("/login?logout")
					// Request Mapper für /logout zum abmelden
					.logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
		;
		// @formatter:on
	}

}