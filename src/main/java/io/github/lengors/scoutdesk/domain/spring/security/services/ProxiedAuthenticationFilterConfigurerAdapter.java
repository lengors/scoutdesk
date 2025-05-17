package io.github.lengors.scoutdesk.domain.spring.security.services;

import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.SecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.DefaultSecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationFilter;
import org.springframework.stereotype.Component;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;

/**
 * Configures the authentication filter for proxied authentication.
 *
 * This adapter integrates the {@link ProxiedAuthenticationConverter} into the
 * Spring Security filter chain.
 *
 * @author lengors
 */
@Component
@RequiredArgsConstructor(access = AccessLevel.PACKAGE)
@ConditionalOnBean(value = ProxiedAuthenticationConverter.class)
public class ProxiedAuthenticationFilterConfigurerAdapter
    extends SecurityConfigurerAdapter<DefaultSecurityFilterChain, HttpSecurity> {

  /**
   * Converter for proxied authentication.
   */
  private final ProxiedAuthenticationConverter proxiedAuthenticationConverter;

  /**
   * Configures the authentication filter for proxied authentication.
   *
   * This method sets up the authentication filter and success handler.
   *
   * @param httpSecurity the HTTP security configuration
   * @throws Exception if an error occurs during configuration
   */
  @Override
  public void configure(final HttpSecurity httpSecurity) throws Exception {
    super.configure(httpSecurity);
    final var authenticationManager = httpSecurity.getSharedObject(AuthenticationManager.class);
    final var authenticationFilter = new AuthenticationFilter(authenticationManager, proxiedAuthenticationConverter);
    authenticationFilter.setSuccessHandler(new ProxiedAuthenticationSuccessHandler());
    httpSecurity.addFilterAt(authenticationFilter, AuthenticationFilter.class);
  }
}
