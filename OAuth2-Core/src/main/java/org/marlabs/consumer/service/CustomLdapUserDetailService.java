package org.marlabs.consumer.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.ldap.search.LdapUserSearch;
import org.springframework.security.ldap.userdetails.LdapUserDetailsService;
import org.springframework.stereotype.Service;

@Service
public class CustomLdapUserDetailService extends LdapUserDetailsService {

	private static final Logger LOG = LoggerFactory
			.getLogger(CustomLdapUserDetailService.class);

	@Autowired
	public CustomLdapUserDetailService(
			@Qualifier("ldapUserSearch") final LdapUserSearch ldapUserSearch) {
		super(ldapUserSearch);

	}

	public UserDetails loadUserByUsername(String username)
			throws UsernameNotFoundException {
		LOG.info("########################loadUserByUsername###########################################START>>>>>");
		final UserDetails user = super.loadUserByUsername(username);
		LOG.debug("########################loadUserByUsername###########################################END>>>>>"
				+ user);
		return user;
	}

}
