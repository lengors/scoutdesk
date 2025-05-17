package io.github.lengors.scoutdesk.integrations.authentik.services;

import java.util.Objects;

import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Service;

import io.github.lengors.scoutdesk.integrations.authentik.exceptions.AuthentikProxiedAuthenticationException;
import io.github.lengors.scoutdesk.integrations.authentik.models.AuthentikProxiedAuthentication;

/**
 * Authentication provider for handling Authentik proxied authentication.
 *
 * This service validates and processes authentication requests using the
 * Authentik proxied authentication model.
 *
 * @author lengors
 */
@Service
class AuthentikProxiedAuthenticationProvider implements AuthenticationProvider {

  /**
   * Authenticates the provided authentication object.
   *
   * @param authentication the authentication request object
   * @return the authenticated authentication object
   * @throws AuthenticationException if authentication fails
   */
  @Override
  public Authentication authenticate(final Authentication authentication) throws AuthenticationException {
    if (!authentication.isAuthenticated()) {
      throw new AuthentikProxiedAuthenticationException();
    }
    return authentication;
  }

  /**
   * Checks if this provider supports the given authentication class.
   *
   * @param authentication the authentication class to check
   * @return true if the class is supported, false otherwise
   */
  @Override
  public boolean supports(final Class<?> authentication) {
    return Objects.equals(authentication, AuthentikProxiedAuthentication.class);
  }
}
