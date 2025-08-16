package io.github.lengors.scoutdesk.testing.authentik.configurations;


import io.github.lengors.scoutdesk.testing.wiremock.configurations.WireMockTestContainerConfigurer;
import org.springframework.stereotype.Component;
import org.wiremock.integrations.testcontainers.WireMockContainer;

@Component
class AuthentikWireMockTestContainerConfigurer implements WireMockTestContainerConfigurer {
  @Override
  public WireMockContainer configure(final WireMockContainer wireMockContainer) {
    return wireMockContainer.withMappingFromResource("mappings/authentik.json");
  }
}
