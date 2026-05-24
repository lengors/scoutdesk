package io.github.lengors.scoutdesk.domain.spring.security.converters;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.lengors.scoutdesk.domain.spring.security.models.User;
import io.github.lengors.scoutdesk.domain.spring.security.models.UserRole;
import io.github.lengors.scoutdesk.domain.spring.security.properties.UserMappingProperties;
import jakarta.validation.constraints.NotNull;
import org.apache.commons.lang3.StringUtils;
import org.springframework.core.convert.converter.Converter;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;

import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * Abstract base class for converting authentication objects into {@link User} instances.
 * <p>
 * This class provides a template for extracting user-related information such as username, roles, email, and avatar
 * from a given authentication source. Subclasses are responsible for providing the actual implementation of attribute
 * extraction and configuration-specific logic needed for their respective use cases.
 *
 * @param <T> the type of the authentication object that extends {@link Authentication}.
 * @author lengors
 */
public abstract class AbstractUserConverter<T extends Authentication> implements Converter<T, User> {
  private final ObjectMapper objectMapper;

  /**
   * Constructs an instance of AbstractUserConverter with the specified ObjectMapper.
   *
   * @param objectMapper the ObjectMapper used for JSON serialization and deserialization
   */
  protected AbstractUserConverter(final ObjectMapper objectMapper) {
    this.objectMapper = objectMapper;
  }

  /**
   * Get the attributes from the authentication source.
   *
   * @param source the authentication source
   * @return a map of attribute names to their values
   */
  protected abstract Map<String, Object> getAttributes(T source);

  /**
   * Get the authority prefix.
   *
   * @return the authority prefix
   */
  protected abstract String getAuthorityPrefix();

  /**
   * Get the user mapping properties.
   *
   * @return the user mapping properties
   */
  protected abstract UserMappingProperties getUserMappingProperties();

  @Override
  public final User convert(final @NotNull T source) {
    final var userMappingProperties = getUserMappingProperties();
    final var username = getAttribute(source, userMappingProperties.username()).orElseGet(source::getName);
    final var roles = source
      .getAuthorities()
      .stream()
      .map(GrantedAuthority::getAuthority)
      .map(it -> it.substring(getAuthorityPrefix().length()))
      .map(it -> objectMapper.convertValue(it, UserRole.class))
      .toList();

    final var tokenAttributes = getAttributes(source);
    final var name = getAttribute(source, userMappingProperties.name()).orElse(null);
    final var email = getAttribute(source, userMappingProperties.email()).orElse(null);
    final var avatar = getAttribute(source, userMappingProperties.avatar()).orElse(null);

    return new User(
      username,
      name,
      roles
        .stream()
        .map(Optional::ofNullable)
        .flatMap(Optional::stream)
        .map(it -> objectMapper.convertValue(it, UserRole.class))
        .toList(),
      email,
      avatar
    );
  }

  private Optional<String> getAttribute(final T source, final List<String> keys) {
    final var attributes = getAttributes(source);
    return keys
      .stream()
      .map(attributes::get)
      .map(Optional::ofNullable)
      .flatMap(Optional::stream)
      .map(it -> objectMapper.convertValue(it, String.class))
      .filter(StringUtils::isNotBlank)
      .findFirst();
  }
}
