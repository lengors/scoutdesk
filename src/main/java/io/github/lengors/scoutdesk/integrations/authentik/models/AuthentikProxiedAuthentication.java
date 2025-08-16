package io.github.lengors.scoutdesk.integrations.authentik.models;

import org.springframework.security.core.AuthenticatedPrincipal;
import org.springframework.security.core.Authentication;

/**
 * Represents an authentication mechanism that is proxied through Authentik.
 * <p>
 * This interface extends both {@link Authentication} and {@link AuthenticatedPrincipal}, allowing for integration with
 * Spring Security's authentication framework while providing additional principal information.
 * <p>
 * Implementations of this interface should not support setting the authenticated status, as it should be immutable
 * after authentication is established. Attempting to set the authenticated status will result in an
 * {@link UnsupportedOperationException} by default.
 *
 * @see AuthentikProxiedAuthenticatedPrincipal
 * @see AuthentikProxiedAnonymousPrincipal
 */
public sealed interface AuthentikProxiedAuthentication extends Authentication, AuthenticatedPrincipal
  permits AuthentikProxiedAuthenticatedPrincipal, AuthentikProxiedAnonymousPrincipal {

  /**
   * Unsupported operation for setting authentication status.
   *
   * @param isAuthenticated the desired authentication status
   * @throws UnsupportedOperationException always thrown as this operation is not supported
   */
  @Override
  default void setAuthenticated(final boolean isAuthenticated) throws IllegalArgumentException {
    throw new UnsupportedOperationException(
      "%s does not support setAuthenticated".formatted(getClass().getSimpleName()));
  }
}
