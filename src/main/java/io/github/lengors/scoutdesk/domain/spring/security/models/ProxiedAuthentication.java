package io.github.lengors.scoutdesk.domain.spring.security.models;

import org.apache.commons.lang3.StringUtils;
import org.springframework.security.core.AuthenticatedPrincipal;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Collection;
import java.util.Map;
import java.util.Optional;

/**
 * Represents an authentication object for proxied authentication.
 * <p>
 * This record implements both {@link org.springframework.security.core.Authentication} and
 * {@link org.springframework.security.core.AuthenticatedPrincipal}, encapsulating user details and providing methods to
 * access user information and authorities.
 *
 * @param username    the username of the authenticated user
 * @param authorities the authorities granted to the user
 * @param attributes  additional attributes associated with the user
 * @author lengors
 */
public record ProxiedAuthentication(
  String username,
  Collection<String> authorities,
  Map<String, Object> attributes
) implements Authentication, AuthenticatedPrincipal {
  /**
   * Retrieves the name of the authenticated user.
   *
   * @return the username or an empty string if not available
   */
  @Override
  public String getName() {
    return Optional
      .ofNullable(username)
      .orElse(StringUtils.EMPTY);
  }

  /**
   * Retrieves the authorities granted to the user.
   *
   * @return a collection of granted authorities
   */
  @Override
  public Collection<? extends GrantedAuthority> getAuthorities() {
    return authorities
      .stream()
      .map(SimpleGrantedAuthority::new)
      .toList();
  }

  /**
   * Retrieves the credentials of the authentication object.
   *
   * @return the current authentication object
   */
  @Override
  public ProxiedAuthentication getCredentials() {
    return this;
  }

  /**
   * Retrieves additional details about the authentication.
   *
   * @return the current authentication object
   */
  @Override
  public ProxiedAuthentication getDetails() {
    return this;
  }

  /**
   * Retrieves the principal of the authentication.
   *
   * @return the current authentication object
   */
  @Override
  public ProxiedAuthentication getPrincipal() {
    return this;
  }

  /**
   * Checks if the user is authenticated.
   *
   * @return true, indicating the user is authenticated
   */
  @Override
  public boolean isAuthenticated() {
    return true;
  }

  /**
   * Unsupported operation for setting authentication status.
   *
   * @param isAuthenticated the desired authentication status
   * @throws UnsupportedOperationException always thrown as this operation is not supported
   */
  @Override
  public void setAuthenticated(final boolean isAuthenticated) throws IllegalArgumentException {
    throw new UnsupportedOperationException(
      "%s does not support setAuthenticated".formatted(getClass().getSimpleName()));
  }
}
