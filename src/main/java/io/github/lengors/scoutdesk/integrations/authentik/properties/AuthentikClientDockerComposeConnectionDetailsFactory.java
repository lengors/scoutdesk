package io.github.lengors.scoutdesk.integrations.authentik.properties;

import org.apache.commons.lang3.StringUtils;
import org.springframework.boot.docker.compose.core.RunningService;
import org.springframework.boot.docker.compose.service.connection.DockerComposeConnectionDetailsFactory;
import org.springframework.boot.docker.compose.service.connection.DockerComposeConnectionSource;

class AuthentikClientDockerComposeConnectionDetailsFactory
  extends DockerComposeConnectionDetailsFactory<AuthentikClientConnectionDetails> {
  private static final String[] AUTHENTIK_CLIENT_CONTAINER_NAMES = {"authentik-server"};
  private static final int CONTAINER_PORT = 9000;

  AuthentikClientDockerComposeConnectionDetailsFactory() {
    super(AUTHENTIK_CLIENT_CONTAINER_NAMES);
  }

  @Override
  protected AuthentikClientConnectionDetails getDockerComposeConnectionDetails(
    final DockerComposeConnectionSource source
  ) {
    return new AuthentikClientDockerComposeConnectionDetails(source.getRunningService());
  }

  private static final class AuthentikClientDockerComposeConnectionDetails extends DockerComposeConnectionDetails
    implements AuthentikClientConnectionDetails {
    private final String serviceAccountToken;
    private final String url;

    private AuthentikClientDockerComposeConnectionDetails(final RunningService runningService) {
      super(runningService);
      final var host = runningService.host();
      final var port = runningService
        .ports()
        .get(CONTAINER_PORT);
      this.url = "http://%s:%d".formatted(host, port);
      this.serviceAccountToken = runningService
        .env()
        .getOrDefault("AUTHENTIK_BOOTSTRAP_TOKEN", StringUtils.EMPTY);
    }

    @Override
    public String serviceAccountToken() {
      return serviceAccountToken;
    }

    @Override
    public String url() {
      return url;
    }
  }
}
