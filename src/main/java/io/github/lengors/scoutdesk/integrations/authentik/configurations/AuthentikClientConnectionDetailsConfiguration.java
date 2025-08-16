package io.github.lengors.scoutdesk.integrations.authentik.configurations;

import io.github.lengors.scoutdesk.integrations.authentik.properties.AuthentikClientConnectionDetails;
import io.github.lengors.scoutdesk.integrations.authentik.properties.AuthentikClientConnectionDetailsProperties;
import io.github.lengors.scoutdesk.integrations.authentik.properties.AuthentikClientProperties;
import org.checkerframework.checker.nullness.util.NullnessUtil;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.core.convert.ConversionService;

@AutoConfiguration
class AuthentikClientConnectionDetailsConfiguration {
  @Bean
  @ConditionalOnBean(AuthentikClientProperties.class)
  @ConditionalOnMissingBean(AuthentikClientConnectionDetails.class)
  AuthentikClientConnectionDetails authentikClientConnectionDetails(
    final AuthentikClientProperties authentikClientProperties,
    final ConversionService conversionService
  ) {
    return NullnessUtil.castNonNull(conversionService.convert(
      authentikClientProperties,
      NullnessUtil.castNonNull(AuthentikClientConnectionDetailsProperties.class)));
  }
}
