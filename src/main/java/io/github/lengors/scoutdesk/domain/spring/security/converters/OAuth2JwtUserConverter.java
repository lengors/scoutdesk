package io.github.lengors.scoutdesk.domain.spring.security.converters;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.lengors.scoutdesk.domain.spring.security.conditions.OAuth2ResourceServerPropertiesCondition;
import io.github.lengors.scoutdesk.domain.spring.security.properties.OAuth2ResourceServerUserMappingProperties;
import io.github.lengors.scoutdesk.domain.spring.security.properties.UserMappingProperties;
import org.springframework.boot.autoconfigure.security.oauth2.resource.OAuth2ResourceServerProperties;
import org.springframework.context.annotation.Conditional;
import org.springframework.security.oauth2.server.resource.authentication.AbstractOAuth2TokenAuthenticationToken;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
@Conditional(OAuth2ResourceServerPropertiesCondition.class)
final class OAuth2JwtUserConverter extends AbstractUserConverter<AbstractOAuth2TokenAuthenticationToken<?>> {
  private final OAuth2ResourceServerUserMappingProperties oAuth2ResourceServerUserMappingProperties;
  private final OAuth2ResourceServerProperties oAuth2ResourceServerProperties;

  OAuth2JwtUserConverter(
    final OAuth2ResourceServerUserMappingProperties oAuth2ResourceServerUserMappingProperties,
    final OAuth2ResourceServerProperties oAuth2ResourceServerProperties,
    final ObjectMapper objectMapper
  ) {
    super(objectMapper);
    this.oAuth2ResourceServerProperties = oAuth2ResourceServerProperties;
    this.oAuth2ResourceServerUserMappingProperties = oAuth2ResourceServerUserMappingProperties;
  }

  @Override
  public Map<String, Object> getAttributes(final AbstractOAuth2TokenAuthenticationToken<?> source) {
    return source.getTokenAttributes();
  }

  @Override
  public String getAuthorityPrefix() {
    return oAuth2ResourceServerProperties
      .getJwt()
      .getAuthorityPrefix();
  }

  @Override
  protected UserMappingProperties getUserMappingProperties() {
    return oAuth2ResourceServerUserMappingProperties;
  }
}
