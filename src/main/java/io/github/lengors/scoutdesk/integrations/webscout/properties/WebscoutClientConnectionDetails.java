package io.github.lengors.scoutdesk.integrations.webscout.properties;

import org.springframework.boot.autoconfigure.service.connection.ConnectionDetails;

/**
 * Defines the connection details required for the Webscout client.
 *
 * Provides the base URL for connecting to the Webscout service.
 *
 * @author lengors
 */
public interface WebscoutClientConnectionDetails extends ConnectionDetails {

  /**
   * Returns the base URL for the Webscout service.
   *
   * @return the base URL
   */
  String url();
}
