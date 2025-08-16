package io.github.lengors.scoutdesk.integrations.authentik.models;

import io.github.lengors.scoutdesk.domain.spring.security.models.UserRole;
import org.apache.commons.lang3.StringUtils;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Collection;
import java.util.Optional;

/**
 * Represents an authentication object for Authentik proxied authentication.
 * <p>
 * This record implements both {@link org.springframework.security.core.Authentication} and
 * {@link org.springframework.security.core.AuthenticatedPrincipal}, encapsulating user details such as username, name,
 * and associated groups.
 *
 * @param username the username of the authenticated user
 * @param name     the display name of the authenticated user
 * @param groups   the roles or groups associated with the user
 * @param email    the email address of the authenticated user, can be null
 * @param avatar   the URL of the user's avatar image
 * @author lengors
 */
public record AuthentikProxiedAuthenticatedPrincipal(
  String username,
  String name,
  Collection<UserRole> groups,
  @Nullable String email,
  String avatar
) implements AuthentikProxiedAuthentication {

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
    return groups
      .stream()
      .map("ROLE_%s"::formatted)
      .map(SimpleGrantedAuthority::new)
      .toList();
  }

  /**
   * Retrieves the credentials of the authentication object.
   *
   * @return the current authentication object
   */
  @Override
  public AuthentikProxiedAuthenticatedPrincipal getCredentials() {
    return this;
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
   * @return true, indicating the user is authenticated
   */
  @Override
  public boolean isAuthenticated() {
    return true;
  }
}
