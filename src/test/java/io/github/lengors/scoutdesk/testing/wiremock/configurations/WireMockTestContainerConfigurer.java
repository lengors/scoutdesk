package io.github.lengors.scoutdesk.testing.wiremock.configurations;

import org.wiremock.integrations.testcontainers.WireMockContainer;

/**
 * Interface for configuring a WireMock container in test environments. Implementations can customize the WireMock
 * container setup.
 *
 * @author lengors
 */
public interface WireMockTestContainerConfigurer {

  /**
   * Configures the provided WireMock container.
   *
   * @param wireMockContainer the WireMock container to configure
   * @return the configured WireMock container
   */
  WireMockContainer configure(WireMockContainer wireMockContainer);
}
