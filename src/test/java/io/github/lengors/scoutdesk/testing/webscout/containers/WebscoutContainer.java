package io.github.lengors.scoutdesk.testing.webscout.containers;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.lifecycle.Startable;
import org.testcontainers.utility.DockerImageName;

import com.github.dockerjava.api.command.InspectContainerResponse;

import io.github.lengors.scoutdesk.testing.duckling.containers.DucklingContainer;

/**
 * Container for the Webscout service.
 *
 * This class extends {@link GenericContainer} to provide a containerized
 * environment for the Webscout service.
 *
 * @param <T> The type of the container
 *
 * @author lengors
 */
public class WebscoutContainer<T extends WebscoutContainer<T>> extends GenericContainer<T> {

  /**
   * The default port for the Webscout service.
   */
  public static final int WEBSCOUT_PORT = 8080;

  /**
   * Creates a new instance of {@link WebscoutContainer} with the specified Docker
   * image name.
   *
   * @param dockerImageName The Docker image name for the Webscout service
   */
  @SuppressWarnings({ "nullness" })
  public WebscoutContainer(final DockerImageName dockerImageName) {
    super(dockerImageName);
    addExposedPort(WEBSCOUT_PORT);
  }

  /**
   * Creates a new instance of {@link WebscoutContainer} with the specified Docker
   * image name.
   *
   * @param dockerImageName The Docker image name for the Webscout service
   */
  public WebscoutContainer(final String dockerImageName) {
    this(DockerImageName.parse(dockerImageName));
  }

  /**
   * Configures the Webscout container with the necessary environment variables
   * and dependencies.
   */
  @Override
  protected void configure() {
    super.configure();

    final var postgreSQLContainer = getDependency(PostgreSQLContainer.class);
    final var ducklingContainer = getDependency(DucklingContainer.class);

    final var postgreSQLHost = getNetworkHost(postgreSQLContainer);
    final var ducklingHost = getNetworkHost(ducklingContainer);

    final var postgresUrl = String.format(
        "postgresql://%s:%d/%s",
        postgreSQLHost,
        PostgreSQLContainer.POSTGRESQL_PORT,
        postgreSQLContainer.getDatabaseName());
    addEnv("DATABASE_URL", postgresUrl);
    final var postgresUsername = postgreSQLContainer.getUsername();
    if (postgresUsername != null) {
      addEnv("DATABASE_USERNAME", postgresUsername);
    }
    final var postgresPassword = postgreSQLContainer.getPassword();
    if (postgresPassword != null) {
      addEnv("DATABASE_PASSWORD", postgresPassword);
    }

    final var ducklingUrl = String.format("http://%s:%d", ducklingHost, DucklingContainer.DUCKLING_PORT);
    addEnv("DUCKLING_CLIENT_URL", ducklingUrl);
  }

  /**
   * Called when the container is stopped. This method stops all dependencies of
   * the container.
   *
   * @param containerInfo The information about the stopped container
   */
  @Override
  protected void containerIsStopped(final InspectContainerResponse containerInfo) {
    super.containerIsStopped(containerInfo);
    dependencies.forEach(Startable::stop);
  }

  private <S extends Startable> S getDependency(final Class<S> dependencyType) {
    return getDependencies()
        .stream()
        .filter(dependencyType::isInstance)
        .findFirst()
        .map(dependencyType::cast)
        .get();
  }

  private static String getNetworkHost(final GenericContainer<?> container) {
    final var networkAliases = (List<String>) container.getNetworkAliases();
    return networkAliases
        .stream()
        .findFirst()
        .orElseGet(() -> StringUtils.removeStart(container.getContainerName(), '/'));
  }
}
