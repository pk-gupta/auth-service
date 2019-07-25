/*
 * Copyright 2014 the original author or authors.
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

package org.mytech.consumer.security;

import java.util.EnumSet;

import javax.servlet.DispatcherType;

import org.mytech.consumer.filter.AccessCtrlFilter;
import org.mytech.consumer.ldap.config.CustomLdapAuthManager;
import org.mytech.consumer.repo.user.UserRepository;
import org.mytech.consumer.service.CustomLdapUserDetailService;
import org.mytech.consumer.service.CustomUserDetailsService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.embedded.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.servlet.configuration.EnableWebMvcSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.ldap.search.LdapUserSearch;

@Configuration
@EnableWebSecurity
@EnableWebMvcSecurity
public class WebSecurityConfiguration extends WebSecurityConfigurerAdapter {
    
    private static final Logger LOG = LoggerFactory
	    .getLogger(WebSecurityConfiguration.class);

    @Autowired
    @Qualifier("userRepository")
    private UserRepository userRepository;
    @Autowired
    @Qualifier("ldapUserSearch")
    private LdapUserSearch ldapUserSearch;
    @Autowired
    private Environment env;
  
    @Override
    protected void configure(AuthenticationManagerBuilder auth)
	    throws Exception {
	auth.userDetailsService(customeUserDetailsServiceBean());
    }
    
    @Bean
    public FilterRegistrationBean accessCtrlFilter() {
	LOG.info("**************accessCtrlFilter************************START");
	FilterRegistrationBean registration = new FilterRegistrationBean();
	registration.setFilter(new AccessCtrlFilter());
	registration.setDispatcherTypes(EnumSet.allOf(DispatcherType.class));
	registration.addUrlPatterns("/*");
	LOG.info("**************accessCtrlFilter************************END");
	return registration;
    }


    @Bean
    public AuthenticationManager authenticationManagerBean()
	    throws Exception {
	LOG.info(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>"+env.getProperty("auth.via.ldap"));
	final boolean ldapAuth = Boolean.parseBoolean(env.getProperty("auth.via.ldap"));
	LOG.info(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>"+ldapAuth);
	LOG.info("####################authenticationManagerBean###################authenticationManagerBean"+ldapAuth);
	return ldapAuth ? new CustomLdapAuthManager() : super.authenticationManagerBean();
    }

    @Bean
    public UserDetailsService customeUserDetailsServiceBean() {
	final boolean ldapAuth = Boolean.parseBoolean(env.getProperty("auth.via.ldap"));
	LOG.info("####################userDetailsService###################userDetailsService"+ldapAuth);
	return ldapAuth ? new CustomLdapUserDetailService(ldapUserSearch) : new CustomUserDetailsService(userRepository);
    }

}
