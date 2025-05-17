package io.github.lengors.scoutdesk.testing.duckling.containers;

import org.testcontainers.containers.GenericContainer;
import org.testcontainers.utility.DockerImageName;

/**
 * Container for the Duckling service.
 *
 * This class extends {@link GenericContainer} to provide a
 * containerized environment for the Duckling service.
 *
 * @param <T> The type of the container
 *
 * @author lengors
 */
public class DucklingContainer<T extends DucklingContainer<T>> extends GenericContainer<T> {

  /**
   * The default port for the Duckling service.
   */
  public static final int DUCKLING_PORT = 8000;

  /**
   * Creates a new instance of {@link DucklingContainer} with the specified Docker
   * image name.
   *
   * @param dockerImageName The Docker image name for the Duckling service
   */
  @SuppressWarnings({ "nullness" })
  public DucklingContainer(final DockerImageName dockerImageName) {
    super(dockerImageName);
    addExposedPort(DUCKLING_PORT);
  }

  /**
   * Creates a new instance of {@link DucklingContainer} with the specified Docker
   * image name.
   *
   * @param dockerImageName The Docker image name for the Duckling service
   */
  public DucklingContainer(final String dockerImageName) {
    this(DockerImageName.parse(dockerImageName));
  }
}
