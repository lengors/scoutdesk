package io.github.lengors.scoutdesk;

import org.junit.jupiter.api.Test;
import org.springframework.boot.SpringApplication;

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
        .run();
  }
}
