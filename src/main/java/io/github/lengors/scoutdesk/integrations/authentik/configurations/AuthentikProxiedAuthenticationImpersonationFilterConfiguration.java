package io.github.lengors.scoutdesk.integrations.authentik.configurations;

import io.github.lengors.scoutdesk.domain.spring.core.conditions.ConditionalOnNonBlankProperty;
import io.github.lengors.scoutdesk.integrations.authentik.services.AuthentikProxiedAuthenticationImpersonationFilter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.context.annotation.Bean;

@AutoConfiguration
class AuthentikProxiedAuthenticationImpersonationFilterConfiguration {
  @Bean
  @ConditionalOnNonBlankProperty("authentik.impersonated-user")
  AuthentikProxiedAuthenticationImpersonationFilter authentikProxiedAuthenticationFakerFilter(
    @Value("${authentik.impersonated-user:}") final String authentikImpersonatedUser
  ) {
    return new AuthentikProxiedAuthenticationImpersonationFilter(authentikImpersonatedUser);
  }
}
