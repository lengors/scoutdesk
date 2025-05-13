package io.github.lengors.scoutdesk;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;

/**
 * ScoutDesk is the official management service for WebScout, the official
 * scraping service implementing the ProtoScout protocol.
 *
 * Entry point for the Scoutdesk application.
 *
 * This class is responsible for bootstrapping the application using Spring
 * Boot.
 *
 * @author lengors
 */
@SpringBootApplication
@ConfigurationPropertiesScan
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
