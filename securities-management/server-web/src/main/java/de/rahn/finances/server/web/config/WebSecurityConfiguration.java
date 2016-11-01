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

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
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
public class WebSecurityConfiguration {

	/**
	 * Die globale Spring Security Konfiguration.
	 *
	 * @see WebSecurityConfigurerAdapter#configure(AuthenticationManagerBuilder)
	 */
	@Autowired
	public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
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
	 * Diese WebSecurity-Konfiguration ist für das Management der Anwendung (/manage/*) mit HTTP Basic Authentication.
	 *
	 * @author Frank W. Rahn
	 */
	@Configuration
	@Order(1)
	public class ManagementWebSecurityConfiguration extends WebSecurityConfigurerAdapter {

		/**
		 * {@inheritDoc}
		 *
		 * @see WebSecurityConfigurerAdapter#configure(HttpSecurity)
		 */
		@Override
		protected void configure(HttpSecurity http) throws Exception {
			// @formatter:off
			http
				// Konfiguration der Requests auf dise URL
				.antMatcher("/manage/*")
					.authorizeRequests()
						// Alle Request benötigen einen User mit der Rolle USER
						.anyRequest().hasRole("USER")
				// Konfiguration HTTP Basic Authentication
				.and().httpBasic().realmName("Management-API")
			;
			// @formatter:on
		}

	}

	/**
	 * Diese WebSecurity-Konfiguration für die Anwendung mit Form Based-Authentication.
	 *
	 * @author Frank W. Rahn
	 */
	@Configuration
	@Order(2)
	public class FormLoginWebSecurityConfiguration extends WebSecurityConfigurerAdapter {

		/**
		 * {@inheritDoc}
		 *
		 * @see WebSecurityConfigurerAdapter#configure(HttpSecurity)
		 */
		@Override
		protected void configure(HttpSecurity http) throws Exception {
			// @formatter:off
			http
				// Konfiguration der berechtigten Requests
				.authorizeRequests()
					// Alle Request benötigen einen User mit der Rolle USER
					.anyRequest().hasRole("USER")
				// Konfiguration des Form Based Authentication
				.and().formLogin()
					// Die URL der Login-Seite
					.loginPage("/login")
					// Alle dürfen auf die Login-Seite zugreifen
					.permitAll()
					// Diese Seite wird nach einem Fehler bei der Anmeldung angezeigt (Default)
					//.failureUrl("/login?error")
				// Konfiguration des Logouts
				.and().logout()
					// Alle dürfen sich abmelden
					.permitAll()
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
}