package io.github.lengors.scoutdesk.testing.webscout.configurations;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.context.annotation.Bean;
import org.testcontainers.containers.Network;
import org.testcontainers.containers.PostgreSQLContainer;

import io.github.lengors.scoutdesk.testing.duckling.containers.DucklingContainer;
import io.github.lengors.scoutdesk.testing.webscout.containers.WebscoutContainer;

/**
 * Test configuration for Webscout test containers.
 *
 * This configuration sets up the necessary containers for testing the Webscout
 * integration.
 *
 * @author lengors
 */
@TestConfiguration(proxyBeanMethods = false)
public class WebscoutTestContainerConfiguration {

  /**
   * Creates a new network for the Webscout test containers.
   *
   * @return the created network
   */
  @Qualifier("webscout")
  @Bean
  Network webscoutNetwork() {
    return Network.newNetwork();
  }

  /**
   * Creates a new Duckling container for the Webscout test containers.
   *
   * @param webscoutNetwork the network to use for the container
   * @return the created Duckling container
   */
  @Qualifier("webscout")
  @Bean
  @SuppressWarnings("resource")
  DucklingContainer<?> webscoutDucklingContainer(@Qualifier("webscout") final Network webscoutNetwork) {
    return new DucklingContainer<>("rasa/duckling")
        .withNetworkAliases("webscout-duckling")
        .withNetwork(webscoutNetwork);
  }

  /**
   * Creates a new PostgreSQL container for the Webscout test containers.
   *
   * @param webscoutNetwork the network to use for the container
   * @return the created PostgreSQL container
   */
  @Qualifier("webscout")
  @Bean
  @SuppressWarnings("resource")
  PostgreSQLContainer<?> webscoutPostgreSQLContainer(@Qualifier("webscout") final Network webscoutNetwork) {
    return new PostgreSQLContainer<>("postgres:17.0")
        .withNetworkAliases("webscout-postgres")
        .withNetwork(webscoutNetwork);
  }

  /**
   * Creates a new Webscout container for the Webscout test containers.
   *
   * @param webscoutNetwork             the network to use for the container
   * @param webscoutDucklingContainer   the Duckling container to depend on
   * @param webscoutPostgreSQLContainer the PostgreSQL container to depend on
   * @return the created Webscout container
   */
  @Qualifier("webscout")
  @Bean
  @ServiceConnection
  @SuppressWarnings("resource")
  WebscoutContainer<?> webscoutContainer(
      @Qualifier("webscout") final Network webscoutNetwork,
      @Qualifier("webscout") final DucklingContainer<?> webscoutDucklingContainer,
      @Qualifier("webscout") final PostgreSQLContainer<?> webscoutPostgreSQLContainer) {
    return new WebscoutContainer<>("ghcr.io/lengors/webscout:1.0.0-dev.8")
        .withNetwork(webscoutNetwork)
        .dependsOn(webscoutDucklingContainer, webscoutPostgreSQLContainer);
  }
}
