package io.github.lengors.scoutdesk.integrations.authentik.services;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.checkerframework.checker.nullness.qual.Nullable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import io.github.lengors.scoutdesk.domain.spring.security.models.UserRole;
import io.github.lengors.scoutdesk.domain.spring.security.properties.UserRoleProperties;
import io.github.lengors.scoutdesk.domain.spring.security.services.ProxiedAuthenticationConverter;
import io.github.lengors.scoutdesk.integrations.authentik.models.AuthentikCustomHeaders;
import io.github.lengors.scoutdesk.integrations.authentik.models.AuthentikProxiedAuthentication;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;

/**
 * Converter for Authentik proxied authentication.
 *
 * This service processes HTTP requests and converts them into
 * {@link AuthentikProxiedAuthentication} objects based on custom headers and
 * role mappings.
 *
 * @author lengors
 */
@Service
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
class AuthentikProxiedAuthenticationConverter implements ProxiedAuthenticationConverter {

  /**
   * Mappings between user roles and their associated groups.
   */
  private final Map<String, List<UserRole>> mappings;

  /**
   * Constructs the converter with optional user role properties.
   *
   * @param userRoleProperties optional user role properties for role mappings
   */
  AuthentikProxiedAuthenticationConverter(
      @Autowired(required = false) final @Nullable UserRoleProperties userRoleProperties) {
    final var roles = Optional
        .ofNullable(userRoleProperties)
        .map(UserRoleProperties::mappings)
        .orElseGet(Collections::emptyMap);

    mappings = roles
        .entrySet()
        .stream()
        .flatMap(entry -> entry
            .getValue()
            .stream()
            .map(role -> Map.entry(entry.getKey(), role)))
        .collect(
            Collectors.groupingBy(Map.Entry::getValue, Collectors.mapping(Map.Entry::getKey, Collectors.toList())));
  }

  /**
   * Converts an HTTP request into an {@link AuthentikProxiedAuthentication} object.
   *
   * @param request the HTTP request to convert
   * @return the resulting {@link AuthentikProxiedAuthentication} object
   */
  @Override
  public Authentication convert(final HttpServletRequest request) {
    final var usernameHeader = request.getHeader(AuthentikCustomHeaders.USERNAME);
    if (usernameHeader == null) {
      return new AuthentikProxiedAuthentication(null, null, Collections.emptyList());
    }

    return new AuthentikProxiedAuthentication(
        usernameHeader,
        request.getHeader(AuthentikCustomHeaders.NAME),
        Collections
            .list(request.getHeaders(AuthentikCustomHeaders.GROUPS))
            .stream()
            .flatMap(header -> Arrays.stream(header.split("\\|")))
            .flatMap(header -> mappings
                .getOrDefault(header, Collections.emptyList())
                .stream())
            .toList());
  }
}
