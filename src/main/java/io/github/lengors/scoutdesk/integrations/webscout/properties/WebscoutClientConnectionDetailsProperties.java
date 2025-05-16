package io.github.lengors.scoutdesk.integrations.webscout.properties;

/**
 * Implementation of {@link WebscoutClientConnectionDetails} using application
 * properties.
 *
 * Provides the Webscout service URL from configuration.
 *
 * @param url The base URL for the Webscout service
 *
 * @author lengors
 */
public record WebscoutClientConnectionDetailsProperties(String url) implements WebscoutClientConnectionDetails {

  /**
   * Constructor for creating a new instance of
   * {@link WebscoutClientConnectionDetailsProperties} from
   * {@link WebscoutClientProperties}.
   *
   * @param webscoutClientProperties The Webscout client properties
   */
  public WebscoutClientConnectionDetailsProperties(
      final WebscoutClientProperties webscoutClientProperties) {
    this(webscoutClientProperties.url());
  }
}
