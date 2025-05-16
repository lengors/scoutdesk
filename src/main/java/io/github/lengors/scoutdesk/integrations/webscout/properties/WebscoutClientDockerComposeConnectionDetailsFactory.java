package io.github.lengors.scoutdesk.integrations.webscout.properties;

import org.springframework.boot.docker.compose.core.RunningService;
import org.springframework.boot.docker.compose.service.connection.DockerComposeConnectionDetailsFactory;
import org.springframework.boot.docker.compose.service.connection.DockerComposeConnectionSource;

/**
 * Factory for creating {@link WebscoutClientConnectionDetails} from Docker
 * Compose services.
 *
 * Resolves the Webscout service URL based on running container information.
 *
 * @author lengors
 */
class WebscoutClientDockerComposeConnectionDetailsFactory
    extends DockerComposeConnectionDetailsFactory<WebscoutClientConnectionDetails> {
  private static final String[] WEBSCOUT_CLIENT_CONTAINER_NAMES = { "ghcr.io/lengors/webscout", "webscout" };
  private static final int CONTAINER_PORT = 8080;

  WebscoutClientDockerComposeConnectionDetailsFactory() {
    super(WEBSCOUT_CLIENT_CONTAINER_NAMES);
  }

  @Override
  protected WebscoutClientConnectionDetails getDockerComposeConnectionDetails(
      final DockerComposeConnectionSource source) {
    return new WebscoutClientDockerComposeConnectionDetails(source.getRunningService());
  }

  private static final class WebscoutClientDockerComposeConnectionDetails extends DockerComposeConnectionDetails
      implements WebscoutClientConnectionDetails {
    private final String url;

    private WebscoutClientDockerComposeConnectionDetails(final RunningService runningService) {
      super(runningService);
      final var host = runningService.host();
      final var port = runningService
          .ports()
          .get(CONTAINER_PORT);
      this.url = String.format("http://%s:%d", host, port);
    }

    @Override
    public String url() {
      return url;
    }
  }
}
