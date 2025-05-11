package io.github.lengors.scoutdesk;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * ScoutDesk is the official management service for WebScout, the official
 * scraping service implementing the ProtoScout protocol.
 *
 * @author lengors
 */
@SpringBootApplication
final class ScoutdeskApplication {

  /**
   * Main method to run the Scoutdesk application.
   *
   * @param arguments command-line arguments
   */
  public static void main(final String[] arguments) {
    SpringApplication.run(ScoutdeskApplication.class, arguments);
  }
}
