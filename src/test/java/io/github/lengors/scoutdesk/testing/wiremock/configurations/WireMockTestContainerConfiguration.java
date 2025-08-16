package io.github.lengors.scoutdesk.testing.wiremock.configurations;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.wiremock.integrations.testcontainers.WireMockContainer;

import java.util.List;

/**
 * Test configuration for WireMock test containers.
 * <p>
 * This configuration sets up the necessary containers for testing the WireMock integration.
 *
 * @author lengors
 */
@TestConfiguration(proxyBeanMethods = false)
public final class WireMockTestContainerConfiguration {
  @Bean
  @Primary
  WireMockContainer wireMockContainer(final List<WireMockTestContainerConfigurer> wireMockTestContainerConfigurers) {
    var wireMockContainer = new WireMockContainer("wiremock/wiremock:3.13.1-alpine");
    for (var wireMockTestContainerConfigurer : wireMockTestContainerConfigurers) {
      wireMockContainer = wireMockTestContainerConfigurer.configure(wireMockContainer);
    }
    return wireMockContainer;
  }
}
