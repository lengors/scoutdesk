package io.github.lengors.scoutdesk.integrations.webscout.configurations;

import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;

import io.github.lengors.scoutdesk.integrations.webscout.properties.WebscoutClientConnectionDetails;
import io.github.lengors.scoutdesk.integrations.webscout.properties.WebscoutClientConnectionDetailsProperties;
import io.github.lengors.scoutdesk.integrations.webscout.properties.WebscoutClientProperties;

/**
 * Auto-configuration for providing {@link WebscoutClientConnectionDetails}
 * beans.
 *
 * Registers a bean if {@link WebscoutClientProperties} is present and no other
 * connection details bean exists.
 *
 * @author lengors
 */
@AutoConfiguration
class WebscoutClientConnectionDetailsConfiguration {
  @Bean
  @ConditionalOnBean(WebscoutClientProperties.class)
  @ConditionalOnMissingBean(WebscoutClientConnectionDetails.class)
  WebscoutClientConnectionDetails webscoutClientConnectionDetails(
      final WebscoutClientProperties webscoutClientProperties) {
    return new WebscoutClientConnectionDetailsProperties(webscoutClientProperties);
  }
}
