package io.github.lengors.scoutdesk;

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
  void shouldCorrectlyBoot() {
    SpringApplication
        .from(ScoutdeskApplication::main)
        .with(PostgresTestContainerConfiguration.class, WebscoutTestContainerConfiguration.class)
        .run();
  }
}
