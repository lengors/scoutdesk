package io.github.lengors.scoutdesk.integrations.authentik.converters;

import io.github.lengors.scoutdesk.domain.spring.security.models.User;
import io.github.lengors.scoutdesk.integrations.authentik.models.AuthentikProxiedAuthentication;
import jakarta.validation.constraints.NotNull;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

/**
 * Converts an {@link AuthentikProxiedAuthentication} to a {@link User}. This converter is used to transform the
 * authenticated principal information into a user response format. It extracts the username, name, groups, email, and
 * avatar from the authenticated principal and constructs a {@link User} object.
 *
 * @author lengors
 */
@Component
final class AuthentikUserConverter implements Converter<AuthentikProxiedAuthentication, User> {
  @Override
  public User convert(final @NotNull AuthentikProxiedAuthentication source) {
    return new User(source.username(), source.name(), source.groups(), source.email(), source.avatar());
  }
}
