package io.github.lengors.scoutdesk;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;

/**
 * ScoutDesk is the official management service for WebScout, the official
 * scraping service implementing the ProtoScout protocol.
 * <p>
 * Entry point for the Scoutdesk application.
 * <p>
 * This class is responsible for bootstrapping the application using Spring
 * Boot.
 *
 * @author lengors
 */
@SpringBootApplication
@ConfigurationPropertiesScan
class ScoutdeskApplication {

  /**
   * Main method to run the Scoutdesk application.
   *
   * @param arguments command-line arguments
   */
  public static void main(final String[] arguments) {
    SpringApplication.run(ScoutdeskApplication.class, arguments);
  }
}
