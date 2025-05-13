package io.github.lengors.scoutdesk.integrations.authentik.models;

import java.util.Collection;
import java.util.Optional;

import org.checkerframework.checker.nullness.qual.Nullable;
import org.springframework.security.core.AuthenticatedPrincipal;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import io.github.lengors.scoutdesk.domain.spring.security.models.UserRole;

/**
 * Represents an authentication object for Authentik proxied authentication.
 *
 * This record implements both {@link Authentication} and
 * {@link AuthenticatedPrincipal}, encapsulating user details such as username,
 * name, and associated groups.
 *
 * @param username the username of the authenticated user
 * @param name     the display name of the authenticated user
 * @param groups   the roles or groups associated with the user
 *
 * @author lengors
 */
public record AuthentikProxiedAuthentication(
    @Nullable String username,
    @Nullable String name,
    Collection<UserRole> groups) implements Authentication, AuthenticatedPrincipal {

  /**
   * Retrieves the name of the authenticated user.
   *
   * @return the username or an empty string if not available
   */
  @Override
  public String getName() {
    return Optional
        .ofNullable(username)
        .orElse(""); // TODO: use StringUtils.EMPTY
  }

  /**
   * Retrieves the authorities granted to the user.
   *
   * @return a collection of granted authorities
   */
  @Override
  public Collection<? extends GrantedAuthority> getAuthorities() {
    return groups
        .stream()
        .map(group -> String.format("ROLE_%s", group))
        .map(SimpleGrantedAuthority::new)
        .toList();
  }

  /**
   * Retrieves the credentials of the authentication object.
   *
   * @return null as credentials are not applicable
   */
  @Override
  @SuppressWarnings("nullness")
  public @Nullable Object getCredentials() {
    return null;
  }

  /**
   * Retrieves additional details about the authentication.
   *
   * @return the current authentication object
   */
  @Override
  public AuthentikProxiedAuthentication getDetails() {
    return this;
  }

  /**
   * Retrieves the principal of the authentication.
   *
   * @return the current authentication object
   */
  @Override
  public AuthentikProxiedAuthentication getPrincipal() {
    return this;
  }

  /**
   * Checks if the user is authenticated.
   *
   * @return true if the username is not null, false otherwise
   */
  @Override
  public boolean isAuthenticated() {
    return username != null;
  }

  /**
   * Unsupported operation for setting authentication status.
   *
   * @param isAuthenticated the desired authentication status
   * @throws UnsupportedOperationException always thrown as this operation is not
   *                                       supported
   */
  @Override
  public void setAuthenticated(final boolean isAuthenticated) throws IllegalArgumentException {
    throw new UnsupportedOperationException(
        String.format("%s does not support setAuthenticated", getClass().getSimpleName()));
  }
}
