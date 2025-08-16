package io.github.lengors.scoutdesk;

import io.github.lengors.scoutdesk.testing.authentik.configurations.AuthentikClientConnectionDetailsConfiguration;
import io.github.lengors.scoutdesk.testing.wiremock.configurations.WireMockTestContainerConfiguration;
import org.junit.jupiter.api.Test;
import org.springframework.boot.SpringApplication;

import io.github.lengors.scoutdesk.testing.postgres.configurations.PostgresTestContainerConfiguration;
import io.github.lengors.scoutdesk.testing.webscout.configurations.WebscoutTestContainerConfiguration;

/**
 * Unit tests for the Scoutdesk application.
 *
 * @author lengors
 */
class ScoutdeskApplicationTests {

  /**
   * Verifies that the application boots correctly.
   */
  @Test
  void givenApplicationContextWhenBootingThenShouldStartSuccessfully() {
    SpringApplication
      .from(ScoutdeskApplication::main)
      .with(
        AuthentikClientConnectionDetailsConfiguration.class,
        PostgresTestContainerConfiguration.class,
        WebscoutTestContainerConfiguration.class,
        WireMockTestContainerConfiguration.class)
      .run();
  }
}
