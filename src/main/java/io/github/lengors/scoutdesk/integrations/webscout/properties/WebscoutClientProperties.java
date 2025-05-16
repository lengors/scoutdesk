package io.github.lengors.scoutdesk.integrations.webscout.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.bind.ConstructorBinding;
import org.springframework.boot.context.properties.bind.DefaultValue;

/**
 * Configuration properties for the Webscout client.
 *
 * Binds properties with the prefix 'webscout.client' to provide the Webscout
 * service URL.
 *
 * @param url The base URL for the Webscout service
 *
 * @author lengors
 */
@ConfigurationProperties(prefix = "webscout.client")
public record WebscoutClientProperties(
    @DefaultValue("http://localhost:8080") String url) {

  /**
   * Constructor for creating a new instance of {@link WebscoutClientProperties}.
   */
  @ConstructorBinding
  public WebscoutClientProperties {
    // Empty constructor for Spring Boot configuration binding
  }
}
