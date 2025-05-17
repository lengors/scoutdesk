package io.github.lengors.scoutdesk.testing.utilities;

import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Supplier;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionTemplate;

import io.github.lengors.protoscout.domain.scrapers.specifications.models.ScraperSpecification;
import io.github.lengors.scoutdesk.api.scrapers.profiles.models.ScraperOwnedProfileTestingEntity;
import io.github.lengors.scoutdesk.api.scrapers.specifications.models.ScraperOwnedSpecificationTestingEntity;
import io.github.lengors.scoutdesk.domain.scrapers.profiles.models.ScraperOwnedProfileEntity;
import io.github.lengors.scoutdesk.domain.scrapers.profiles.repositories.ScraperOwnedProfileRepository;
import io.github.lengors.scoutdesk.domain.scrapers.specifications.models.ScraperOwnedSpecificationEntity;
import io.github.lengors.scoutdesk.domain.scrapers.specifications.models.ScraperOwnedSpecificationReference;
import io.github.lengors.scoutdesk.domain.scrapers.specifications.models.ScraperOwnedSpecificationStatus;
import io.github.lengors.scoutdesk.domain.scrapers.specifications.repositories.ScraperOwnedSpecificationRepository;
import io.github.lengors.scoutdesk.integrations.webscout.clients.WebscoutRestClient;
import io.github.lengors.scoutdesk.testing.postgres.configurations.PostgresTestContainerConfiguration;
import io.github.lengors.scoutdesk.testing.webscout.configurations.WebscoutTestContainerConfiguration;

/**
 * This interface is used as the base for all test classes in the project.
 *
 * It provides a set of default methods and annotations to simplify the testing
 * process.
 *
 * It includes the following features:
 * - Automatic transaction management
 * - Automatic resource loading
 * - Automatic cleanup of test data
 * - Automatic configuration of the test environment
 * - Automatic injection of test dependencies
 *
 * @author lengors
 */
public interface TestSuite {

  /**
   * This annotation is used to configure the test class with the default
   * settings.
   */
  @Target(ElementType.TYPE)
  @Retention(RetentionPolicy.RUNTIME)
  @Inherited
  @Import({
      PostgresTestContainerConfiguration.class,
      WebscoutTestContainerConfiguration.class
  })
  @SpringBootTest
  @ActiveProfiles("test")
  @AutoConfigureMockMvc
  @interface Defaults {

  }

  /**
   * This method is used to clean up the test data after each test case.
   * It deletes all the test data from the database and the webscout client.
   */
  @AfterEach
  default void cleanup() {
    transaction(status -> {
      getScraperOwnedProfileRepository().deleteAll();
      getScraperOwnedSpecificationRepository().deleteAll();
      getWebscoutRestClient().deleteAll();
    });
  }

  /**
   * This method is used to set up the test data before each test case.
   * It loads the test data from the specified resource and populates the database
   * with it.
   */
  @BeforeEach
  default void setup() {

    // Load specification
    final var specification = getResourceUtils()
        .loadResource("classpath:specifications/test.yml", ScraperSpecification.class);

    // Populate the database with test data
    transaction(status -> {

      /**
       * This class is used to create a new instance of the
       * {@link ScraperOwnedSpecificationEntity} class with the specified
       * {@link ScraperOwnedSpecificationTestingEntity}.
       *
       * @author lengors
       */
      final class ScraperOwnedSpecificationEntityFactory implements Supplier<ScraperOwnedSpecificationEntity> {
        private final ScraperOwnedSpecificationTestingEntity testingEntity;

        private ScraperOwnedSpecificationEntityFactory(final ScraperOwnedSpecificationTestingEntity testingEntity) {
          this.testingEntity = testingEntity;
        }

        private ScraperOwnedSpecificationEntityFactory(final ScraperOwnedSpecificationReference reference) {
          this(new ScraperOwnedSpecificationTestingEntity(reference, null));
        }

        @Override
        public ScraperOwnedSpecificationEntity get() {
          // Instantiate the entity
          final var entity = getScraperOwnedSpecificationRepository()
              .save(new ScraperOwnedSpecificationEntity(testingEntity.reference()));
          final var status = testingEntity.status();
          if (status != null) {
            entity.setStatus(status);
          }

          // Save the entity
          getWebscoutRestClient().save(new ScraperSpecification(
              testingEntity
                  .reference()
                  .fullyQualifiedName(),
              specification.getSettings(),
              specification.getHandlers()));

          return entity;
        }
      }

      // Instantiate the specification entity
      for (final var testingEntity : getScraperOwnedSpecificationTestingEntities()) {
        new ScraperOwnedSpecificationEntityFactory(testingEntity).get();
      }

      for (final var testingEntity : getScraperOwnedProfileTestingEntities()) {
        // Get the specification entity
        final var specificationEntity = getScraperOwnedSpecificationRepository()
            .findById(testingEntity.specificationReference())
            .orElseGet(new ScraperOwnedSpecificationEntityFactory(testingEntity.specificationReference()));

        // Instantiate the entity
        getScraperOwnedProfileRepository().save(new ScraperOwnedProfileEntity(
            testingEntity.profileReference(),
            new HashMap<>(testingEntity.inputs()),
            specificationEntity));
      }
    });
  }

  /**
   * This method is used to execute a transaction with the specified
   * {@link TransactionStatus}.
   *
   * @param consumer The consumer to be executed within the transaction
   */
  default void transaction(final Consumer<TransactionStatus> consumer) {
    final var transactionTemplate = new TransactionTemplate(getPlatformTransactionManager());
    transactionTemplate.execute(status -> {
      consumer.accept(status);
      return null;
    });
  }

  /**
   * This method is used to get the default test data for the
   * {@link ScraperOwnedProfileTestingEntity} class.
   *
   * @return The default test data for the
   *         {@link ScraperOwnedProfileTestingEntity}
   */
  default List<ScraperOwnedProfileTestingEntity> getScraperOwnedProfileTestingEntities() {
    return List.of(
        new ScraperOwnedProfileTestingEntity("tester-0", "test-specification-0", "tester-0", "test-profile-0"),
        new ScraperOwnedProfileTestingEntity("tester-0", "test-specification-1", "tester-0", "test-profile-1"),
        new ScraperOwnedProfileTestingEntity("tester-1", "test-specification-1", "tester-0", "test-profile-2"),
        new ScraperOwnedProfileTestingEntity("tester-1", "test-specification-0", "tester-1", "test-profile-0"),
        new ScraperOwnedProfileTestingEntity("tester-1", "test-specification-1", "tester-1", "test-profile-1"),
        new ScraperOwnedProfileTestingEntity("tester-1", "test-specification-1", "tester-5", "test-profile-0"),
        new ScraperOwnedProfileTestingEntity("tester-1", "test-specification-1", "tester-5", "test-profile-1"),
        new ScraperOwnedProfileTestingEntity("tester-1", "test-specification-1", "tester-5", "test-profile-2"),
        new ScraperOwnedProfileTestingEntity("tester-1", "test-specification-1", "tester-9", "test-profile-9"),
        new ScraperOwnedProfileTestingEntity(
            "tester-1",
            "test-specification-1",
            "tester-x",
            "test-profile-x",
            Map.of("description", "test-description-x", "brand_description", "test-brand-description-x")),
        new ScraperOwnedProfileTestingEntity(
            "tester-1",
            "test-specification-1",
            "tester-x",
            "test-profile-y",
            Map.of("description", "test-description-y", "brand_description", "test-brand-description-y")),
        new ScraperOwnedProfileTestingEntity(
            "tester-1",
            "test-specification-1",
            "tester-x",
            "test-profile-z",
            Map.of("description", "test-description-z", "brand_description", "test-brand-description-z")));
  }

  /**
   * This method is used to get the default test data for the
   * {@link ScraperOwnedSpecificationTestingEntity} class.
   *
   * @return The default test data for the
   *         {@link ScraperOwnedSpecificationTestingEntity}
   */
  default List<ScraperOwnedSpecificationTestingEntity> getScraperOwnedSpecificationTestingEntities() {
    return List.of(
        new ScraperOwnedSpecificationTestingEntity(
            "tester-0",
            "test-specification-0"),
        new ScraperOwnedSpecificationTestingEntity(
            "tester-0",
            "test-specification-1",
            ScraperOwnedSpecificationStatus.DELETED),
        new ScraperOwnedSpecificationTestingEntity(
            "tester-0",
            "test-specification-2",
            ScraperOwnedSpecificationStatus.ARCHIVED),
        new ScraperOwnedSpecificationTestingEntity(
            "tester-1",
            "test-specification-0"),
        new ScraperOwnedSpecificationTestingEntity(
            "tester-1",
            "test-specification-1",
            ScraperOwnedSpecificationStatus.DELETED));
  }

  /**
   * This method is used to get the {@link PlatformTransactionManager} instance.
   *
   * @return The {@link PlatformTransactionManager} instance
   */
  PlatformTransactionManager getPlatformTransactionManager();

  /**
   * This method is used to get the {@link ResourceUtils} instance.
   *
   * @return The {@link ResourceUtils} instance
   */
  ResourceUtils getResourceUtils();

  /**
   * This method is used to get the {@link ScraperOwnedProfileRepository}
   * instance.
   *
   * @return The {@link ScraperOwnedProfileRepository} instance
   */
  ScraperOwnedProfileRepository getScraperOwnedProfileRepository();

  /**
   * This method is used to get the {@link ScraperOwnedSpecificationRepository}
   * instance.
   *
   * @return The {@link ScraperOwnedSpecificationRepository} instance
   */
  ScraperOwnedSpecificationRepository getScraperOwnedSpecificationRepository();

  /**
   * This method is used to get the {@link WebscoutRestClient} instance.
   *
   * @return The {@link WebscoutRestClient} instance
   */
  WebscoutRestClient getWebscoutRestClient();
}
