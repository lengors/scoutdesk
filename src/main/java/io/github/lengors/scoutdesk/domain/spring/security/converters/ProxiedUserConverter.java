package io.github.lengors.scoutdesk.domain.spring.security.converters;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.lengors.scoutdesk.domain.spring.security.models.ProxiedAuthentication;
import io.github.lengors.scoutdesk.domain.spring.security.properties.ProxiedAuthenticationProperties;
import io.github.lengors.scoutdesk.domain.spring.security.properties.ProxiedAuthenticationUserMappingProperties;
import io.github.lengors.scoutdesk.domain.spring.security.properties.UserMappingProperties;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * Converts an {@link ProxiedAuthentication} to a
 * {@link io.github.lengors.scoutdesk.domain.spring.security.models.User}. This converter is used to transform the
 * authenticated principal information into a user response format. It extracts the username, name, groups, email, and
 * avatar from the authenticated principal and constructs a
 * {@link io.github.lengors.scoutdesk.domain.spring.security.models.User} object.
 *
 * @author lengors
 */
@Component
@ConditionalOnProperty(ProxiedAuthenticationProperties.PREFIX + ".header.prefix")
final class ProxiedUserConverter extends AbstractUserConverter<ProxiedAuthentication> {
  private final ProxiedAuthenticationUserMappingProperties proxiedAuthenticationUserMappingProperties;
  private final ProxiedAuthenticationProperties proxiedAuthenticationProperties;

  ProxiedUserConverter(
    final ProxiedAuthenticationUserMappingProperties proxiedAuthenticationUserMappingProperties,
    final ProxiedAuthenticationProperties proxiedAuthenticationProperties,
    final ObjectMapper objectMapper
  ) {
    super(objectMapper);
    this.proxiedAuthenticationProperties = proxiedAuthenticationProperties;
    this.proxiedAuthenticationUserMappingProperties = proxiedAuthenticationUserMappingProperties;
  }

  @Override
  public Map<String, Object> getAttributes(final ProxiedAuthentication source) {
    return source.attributes();
  }

  @Override
  public String getAuthorityPrefix() {
    return proxiedAuthenticationProperties.authorityPrefix();
  }

  @Override
  protected UserMappingProperties getUserMappingProperties() {
    return proxiedAuthenticationUserMappingProperties;
  }
}
