package io.github.lengors.scoutdesk;

import org.junit.jupiter.api.Test;
import org.springframework.boot.SpringApplication;

class ScoutdeskApplicationTests {
  @Test
  void shouldCorrectlyBoot() {
    SpringApplication
        .from(ScoutdeskApplication::main)
        .run();
  }
}
