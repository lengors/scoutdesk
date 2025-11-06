package io.github.lengors.scoutdesk.integrations.authentik.services;

import io.github.lengors.scoutdesk.domain.collections.IterableConverters;
import io.github.lengors.scoutdesk.domain.collections.IteratorConverters;
import io.github.lengors.scoutdesk.domain.spring.security.services.ProxiedAuthenticationImpersonationFilter;
import io.github.lengors.scoutdesk.integrations.authentik.models.AuthentikCustomHeaders;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletRequestWrapper;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.constraints.NotNull;
import org.apache.commons.collections4.EnumerationUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Enumeration;
import java.util.List;
import java.util.stream.Stream;

/**
 * A filter that impersonates a user by overriding the "X-Authentik-Username" header. This is useful for testing
 * purposes or when you need to simulate requests as a specific user.
 * <p>
 * This filter extends {@link OncePerRequestFilter} to ensure that it is executed once per request. It implements
 * {@link ProxiedAuthenticationImpersonationFilter} to provide the necessary functionality for impersonation in a
 * proxied authentication scenario.
 * <p>
 * The filter wraps the incoming request and overrides the `getHeader`, `getHeaderNames`, and `getHeaders` methods to
 * return the impersonated user's username when the "X-Authentik-Username" header is requested. This allows the
 * application to treat the request as if it was made by the impersonated user, while still allowing other headers to be
 * accessed normally.
 * <p>
 * Usage of this filter should be done with caution, as it can lead to security issues if not handled properly. It is
 * primarily intended for testing and development purposes.
 *
 * @author lengors
 */
public final class AuthentikProxiedAuthenticationImpersonationFilter extends OncePerRequestFilter
  implements ProxiedAuthenticationImpersonationFilter {
  private final String impersonatedUser;

  /**
   * Constructs a new {@link AuthentikProxiedAuthenticationImpersonationFilter} with the specified impersonated user.
   *
   * @param impersonatedUser The username of the user to impersonate in the request headers.
   */
  public AuthentikProxiedAuthenticationImpersonationFilter(final String impersonatedUser) {
    this.impersonatedUser = impersonatedUser;
  }

  @Override
  protected void doFilterInternal(
    final @NotNull HttpServletRequest request,
    final @NotNull HttpServletResponse response,
    final @NotNull FilterChain filterChain
  ) throws ServletException, IOException {
    final var wrappedRequest = new HttpServletRequestWrapper(request) {
      @Override
      public String getHeader(final String name) {
        return AuthentikCustomHeaders.USERNAME.equals(name)
          ? impersonatedUser
          : super.getHeader(name);
      }

      @Override
      public Enumeration<String> getHeaderNames() {
        return IteratorConverters.toEnumeration(Stream
          .concat(
            EnumerationUtils
              .toList(super.getHeaderNames())
              .stream(),
            Stream.of(AuthentikCustomHeaders.USERNAME))
          .distinct()
          .iterator());
      }

      @Override
      public Enumeration<String> getHeaders(final String name) {
        return AuthentikCustomHeaders.USERNAME.equals(name)
          ? IterableConverters.toEnumeration(List.of(impersonatedUser))
          : super.getHeaders(name);
      }
    };

    filterChain.doFilter(wrappedRequest, response);
  }
}
