package io.github.lengors.scoutdesk.integrations.authentik.models;

import org.apache.commons.lang3.StringUtils;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;
import java.util.List;

/**
 * Represents an anonymous principal in Authentik proxied authentication.
 * <p>
 * This class implements the {@link AuthentikProxiedAuthentication} interface and provides methods to retrieve user
 * details, authorities, and authentication status for an anonymous user.
 *
 * @author lengors
 */
public record AuthentikProxiedAnonymousPrincipal() implements AuthentikProxiedAuthentication {
  /**
   * Retrieves the name of the authenticated user.
   *
   * @return the username or an empty string if not available
   */
  @Override
  public String getName() {
    return StringUtils.EMPTY;
  }

  /**
   * Retrieves the authorities granted to the user.
   *
   * @return a collection of granted authorities
   */
  @Override
  public Collection<? extends GrantedAuthority> getAuthorities() {
    return List.of();
  }

  /**
   * Retrieves the credentials of the authentication object.
   *
   * @return the current authentication object
   */
  @Override
  public AuthentikProxiedAnonymousPrincipal getCredentials() {
    return this;
  }

  /**
   * Retrieves additional details about the authentication.
   *
   * @return the current authentication object
   */
  @Override
  public AuthentikProxiedAnonymousPrincipal getDetails() {
    return this;
  }

  /**
   * Retrieves the principal of the authentication.
   *
   * @return the current authentication object
   */
  @Override
  public AuthentikProxiedAnonymousPrincipal getPrincipal() {
    return this;
  }

  /**
   * Checks if the user is authenticated.
   *
   * @return false, as this principal represents an anonymous user
   */
  @Override
  public boolean isAuthenticated() {
    return false;
  }
}
