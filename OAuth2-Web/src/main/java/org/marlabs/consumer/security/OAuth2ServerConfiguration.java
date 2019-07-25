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

package org.marlabs.consumer.security;

import java.util.Arrays;

import org.marlabs.consumer.entity.user.Login;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.oauth2.common.DefaultOAuth2AccessToken;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.ClientDetailsService;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.DefaultTokenServices;
import org.springframework.security.oauth2.provider.token.TokenEnhancer;
import org.springframework.security.oauth2.provider.token.TokenEnhancerChain;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;

@Configuration
public class OAuth2ServerConfiguration {


    @EnableAuthorizationServer
    protected static class AuthorizationServerConfiguration extends
	    AuthorizationServerConfigurerAdapter {

	@Autowired
	@Qualifier("authenticationManagerBean")
	private AuthenticationManager authenticationManager;
	
	@Autowired
	@Qualifier("customeUserDetailsServiceBean")
	private UserDetailsService userDetailsService;
	
	@Autowired
	private ClientDetailsService clientDetailsService;

	@Autowired
	private Environment env;

	@Override
	public void configure(ClientDetailsServiceConfigurer clients)
		throws Exception {

	    clients.inMemory().withClient(env.getProperty("oauth.clientapp"))
		    .authorizedGrantTypes("password", env.getProperty("oauth.clientapp.password"))
		    .authorities("USER").scopes("read", "write")
		    .resourceIds(env.getProperty("oauth.clientapp.resourceId")).secret(env.getProperty("oauth.clientapp.secret"));

	}

	@Bean
	public JwtTokenStore tokenStore() {
	    return new JwtTokenStore(tokenEnhancer());
	}

	@Bean
	public JwtAccessTokenConverter tokenEnhancer() {
	    final JwtAccessTokenConverter jwtAccessTokenConverter = new JwtAccessTokenConverter();
	    jwtAccessTokenConverter.setSigningKey("abc");
	    return jwtAccessTokenConverter;
	}

	@Bean
	public TokenEnhancerChain tokenEnhancerChain() {
	    final TokenEnhancerChain tokenEnhancerChain = new TokenEnhancerChain();
	    tokenEnhancerChain.setTokenEnhancers(Arrays.asList(
		    new CustTokenEnhancer(), tokenEnhancer()));
	    return tokenEnhancerChain;
	}

	@Override
	public void configure(AuthorizationServerEndpointsConfigurer endpoints)
		throws Exception {

	    endpoints.tokenServices(defaultTokenServices())
		    .authenticationManager(authenticationManager)
		    .userDetailsService(userDetailsService)
		    .accessTokenConverter(tokenEnhancer());

	}

	@Override
	public void configure(AuthorizationServerSecurityConfigurer oauthServer)
		throws Exception {
	    oauthServer.allowFormAuthenticationForClients().realm("abc/cde");
	}



	@Bean
	public DefaultTokenServices defaultTokenServices() {
	    final DefaultTokenServices defaultTokenServices = new DefaultTokenServices();
	    defaultTokenServices.setTokenStore(tokenStore());
	    defaultTokenServices.setClientDetailsService(clientDetailsService);
	    defaultTokenServices.setTokenEnhancer(tokenEnhancerChain());
	    defaultTokenServices.setSupportRefreshToken(true);
	    return defaultTokenServices;
	}

	private static class CustTokenEnhancer implements TokenEnhancer {
	
	    public OAuth2AccessToken enhance(OAuth2AccessToken accessToken,
		    OAuth2Authentication authentication) {
		final DefaultOAuth2AccessToken result = new DefaultOAuth2AccessToken(
			accessToken);
		final Login user = (Login) authentication.getPrincipal();
		result.getAdditionalInformation().put("id", user.getId());
		result.getAdditionalInformation().put("name", user.getName());
		result.getAdditionalInformation().put("contactId",
			user.getContactId());
		
		result.getAdditionalInformation().put("roles", user.getRoles());
		return result;
	    }
	}
    }

}
