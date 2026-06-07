package io.github.lengors.scoutdesk.domain.spring.security.conditions;

import org.springframework.boot.autoconfigure.condition.AnyNestedCondition;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;

/**
 * Condition for OAuth2 resource server properties.
 *
 * @author lengors
 */
public class OAuth2ResourceServerPropertiesCondition extends AnyNestedCondition {

  /**
   * Constructor for OAuth2ResourceServerPropertiesCondition.
   */
  public OAuth2ResourceServerPropertiesCondition() {
    super(ConfigurationPhase.PARSE_CONFIGURATION);
  }

  @ConditionalOnProperty("spring.security.oauth2.resourceserver.jwt.authority-prefix")
  static class OnJwtAuthorityPrefix {

  }

  @ConditionalOnProperty("spring.security.oauth2.resourceserver.jwt.principal-claim-name")
  static class OnJwtPrincipalClaimName {

  }

  @ConditionalOnProperty("spring.security.oauth2.resourceserver.jwt.authorities-claim-name")
  static class OnJwtAuthoritiesClaimName {

  }
}
