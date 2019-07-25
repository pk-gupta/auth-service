/*
 * Copyright 2014-2015 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.mytech.consumer.service;

import java.util.Collection;

import org.mytech.consumer.entity.user.Login;
import org.mytech.consumer.repo.user.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailsService implements UserDetailsService {

	private static final Logger LOG = LoggerFactory
			.getLogger(CustomUserDetailsService.class);

	private final UserRepository userRepository;

	@Autowired
	public CustomUserDetailsService(final UserRepository userRepository) {
		this.userRepository = userRepository;

	}

	public UserDetails loadUserByUsername(final String username)
			throws UsernameNotFoundException {

		LOG.info("########################loadUserByUsername###########################################START>>>>>");
		final Login user = userRepository.findByContactId(username);

		if (user == null) {
			throw new UsernameNotFoundException(String.format(
					"User %s does not exist!", username));
		}
		final LoginUserDetails lusr = new LoginUserDetails(user);
		LOG.info("########################loadUserByUsername###########################################ENDS>>>>>"
				+ lusr);
		return lusr;
	}

	protected UserDetails loadUserByUsernameLdap(final String username)
			throws UsernameNotFoundException {
		final Login user = userRepository.findByContactId(username);
		return new LoginUserDetails(user);
	}

	private final static class LoginUserDetails extends Login implements
			UserDetails {

		private static final long serialVersionUID = 1L;

		private LoginUserDetails(Login user) {
			super(user);
		}

		public Collection<? extends GrantedAuthority> getAuthorities() {
			return getRoles();
		}

		public String getUsername() {
			return getContactId();
		}

		public boolean isAccountNonExpired() {
			return true;
		}

		public boolean isAccountNonLocked() {
			return true;
		}

		public boolean isCredentialsNonExpired() {
			return true;
		}

		public boolean isEnabled() {
			return true;
		}

	}

}
