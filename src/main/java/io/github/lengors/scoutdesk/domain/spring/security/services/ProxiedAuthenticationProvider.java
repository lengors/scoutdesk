package io.github.lengors.scoutdesk.domain.spring.security.services;

import io.github.lengors.scoutdesk.domain.spring.security.models.ProxiedAuthentication;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Service;

@Service
class ProxiedAuthenticationProvider implements AuthenticationProvider {
  @Override
  public Authentication authenticate(final Authentication authentication) throws AuthenticationException {
    return authentication;
  }

  @Override
  public boolean supports(final Class<?> authentication) {
    return ProxiedAuthentication.class.isAssignableFrom(authentication);
  }
}
