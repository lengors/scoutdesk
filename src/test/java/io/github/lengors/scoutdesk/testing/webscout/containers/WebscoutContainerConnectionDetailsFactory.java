package io.github.lengors.scoutdesk.testing.webscout.containers;

import org.springframework.boot.testcontainers.service.connection.ContainerConnectionDetailsFactory;
import org.springframework.boot.testcontainers.service.connection.ContainerConnectionSource;

import io.github.lengors.scoutdesk.integrations.webscout.properties.WebscoutClientConnectionDetails;

/**
 * Factory for creating {@link WebscoutClientConnectionDetails} from
 * {@link WebscoutContainer} instances.
 *
 * This factory resolves the Webscout service URL based on the container
 * information.
 *
 * @author lengors
 */
public class WebscoutContainerConnectionDetailsFactory
    extends ContainerConnectionDetailsFactory<WebscoutContainer<?>, WebscoutClientConnectionDetails> {

  /**
   * Creates a new instance of
   * {@link WebscoutContainerConnectionDetailsFactory}.
   *
   * This constructor is used by Spring to create the factory.
   *
   * @param source The source of the container connection details
   */
  @Override
  protected WebscoutClientConnectionDetails getContainerConnectionDetails(
      final ContainerConnectionSource<WebscoutContainer<?>> source) {
    return new WebscoutContainerConnectionDetails(source);
  }

  private static final class WebscoutContainerConnectionDetails extends ContainerConnectionDetails<WebscoutContainer<?>>
      implements WebscoutClientConnectionDetails {
    private WebscoutContainerConnectionDetails(final ContainerConnectionSource<WebscoutContainer<?>> source) {
      super(source);
    }

    @Override
    public String url() {
      final var host = getContainer().getHost();
      final var port = getContainer().getMappedPort(WebscoutContainer.WEBSCOUT_PORT);
      return String.format("http://%s:%d", host, port);
    }
  }
}
