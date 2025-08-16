package io.github.lengors.scoutdesk.testing.authentik.configurations;

import io.github.lengors.scoutdesk.integrations.authentik.properties.AuthentikClientConnectionDetails;
import io.github.lengors.scoutdesk.integrations.authentik.properties.AuthentikClientConnectionDetailsProperties;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.wiremock.integrations.testcontainers.WireMockContainer;

/**
 * Configuration for Authentik client connection details in test environments. This configuration uses a WireMock
 * container to provide a mock Authentik server.
 *
 * @author lengors
 */
@TestConfiguration(proxyBeanMethods = false)
public final class AuthentikClientConnectionDetailsConfiguration {
  @Bean
  @Primary
  AuthentikClientConnectionDetails authentikClientConnectionDetails(final WireMockContainer wireMockContainer) {
    return new AuthentikClientConnectionDetailsProperties("authentik", wireMockContainer.getBaseUrl());
  }
}
