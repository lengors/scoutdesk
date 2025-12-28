package io.github.lengors.scoutdesk.integrations.authentik.services;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import io.github.lengors.scoutdesk.domain.commands.CommandException;
import io.github.lengors.scoutdesk.domain.commands.CommandService;
import io.github.lengors.scoutdesk.integrations.authentik.commands.FindAuthentikUserCommand;
import io.github.lengors.scoutdesk.integrations.authentik.models.AuthentikGroup;
import io.github.lengors.scoutdesk.integrations.authentik.models.AuthentikProxiedAuthentication;
import io.github.lengors.scoutdesk.integrations.authentik.models.AuthentikUser;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import io.github.lengors.scoutdesk.domain.spring.security.models.UserRole;
import io.github.lengors.scoutdesk.domain.spring.security.properties.UserRoleProperties;
import io.github.lengors.scoutdesk.domain.spring.security.services.ProxiedAuthenticationConverter;
import io.github.lengors.scoutdesk.integrations.authentik.models.AuthentikCustomHeaders;
import jakarta.servlet.http.HttpServletRequest;

/**
 * Converter for Authentik proxied authentication.
 * <p>
 * This service processes HTTP requests and converts them into
 * {@link io.github.lengors.scoutdesk.integrations.authentik.models.AuthentikProxiedAuthentication} objects based on
 * custom headers and role mappings.
 *
 * @author lengors
 */
@Service
class AuthentikProxiedAuthenticationConverter implements ProxiedAuthenticationConverter {
  private static final Logger LOG = LoggerFactory.getLogger(AuthentikProxiedAuthenticationConverter.class);

  /**
   * Mappings between user roles and their associated groups.
   */
  private final Map<String, List<UserRole>> mappings;

  /**
   * Service for executing commands.
   */
  private final CommandService commandService;

  /**
   * Constructs the converter with optional user role properties.
   *
   * @param userRoleProperties optional user role properties for role mappings
   * @param commandService     service for executing commands
   */
  AuthentikProxiedAuthenticationConverter(
    @Autowired(required = false) final @Nullable UserRoleProperties userRoleProperties,
    final CommandService commandService
  ) {
    final var roles = Optional
      .ofNullable(userRoleProperties)
      .map(UserRoleProperties::mappings)
      .orElseGet(Map::of);
    this.mappings = roles
      .entrySet()
      .stream()
      .flatMap(entry -> entry
        .getValue()
        .stream()
        .map(role -> Map.entry(entry.getKey(), role)))
      .collect(
        Collectors.groupingBy(Map.Entry::getValue, Collectors.mapping(Map.Entry::getKey, Collectors.toList())));

    this.commandService = commandService;
  }

  /**
   * Converts an HTTP request into an
   * {@link io.github.lengors.scoutdesk.integrations.authentik.models.AuthentikProxiedAuthentication} object.
   *
   * @param request the HTTP request to convert
   * @return the resulting
   * {@link io.github.lengors.scoutdesk.integrations.authentik.models.AuthentikProxiedAuthentication} object
   */
  @Override
  @SuppressWarnings("nullness")
  public @Nullable Authentication convert(final HttpServletRequest request) {
    final var usernameHeader = request.getHeader(AuthentikCustomHeaders.USERNAME);
    if (usernameHeader == null) {
      return null;
    }

    final AuthentikUser authentikUser;
    try {
      authentikUser =
        CommandException.unwrap(() -> commandService.executeCommand(new FindAuthentikUserCommand(), usernameHeader));
    } catch (final NoSuchElementException exception) {
      return null;
    }

    if (authentikUser == null) {
      return null;
    }

    if (!Objects.equals(authentikUser.username(), usernameHeader)) {
      LOG.error(
        "User {username={}} does not match requested header: {username={}} ",
        authentikUser.username(),
        usernameHeader);
      return null;
    }

    final var userRoles = Optional
      .ofNullable(authentikUser.groups())
      .stream()
      .flatMap(Collection::stream)
      .map(AuthentikGroup::name)
      .flatMap(header -> mappings
        .getOrDefault(header, List.of())
        .stream())
      .toList();

    return new AuthentikProxiedAuthentication(
      authentikUser.username(),
      authentikUser.name(),
      userRoles,
      authentikUser.email(),
      authentikUser.avatar());
  }
}
