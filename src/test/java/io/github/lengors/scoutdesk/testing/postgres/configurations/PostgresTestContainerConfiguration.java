package io.github.lengors.scoutdesk.testing.postgres.configurations;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.testcontainers.containers.PostgreSQLContainer;

/**
 * Test configuration for PostgreSQL test containers.
 *
 * This configuration sets up the necessary containers for testing the
 * PostgreSQL integration.
 *
 * @author lengors
 */
@TestConfiguration(proxyBeanMethods = false)
public class PostgresTestContainerConfiguration {

  /**
   * Creates a new PostgreSQL container for the test containers.
   *
   * @return the created PostgreSQL container
   */
  @Bean
  @Primary
  @ServiceConnection
  PostgreSQLContainer<?> postgresContainer() {
    return new PostgreSQLContainer<>("postgres:17.0");
  }
}
