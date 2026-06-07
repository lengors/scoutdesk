package io.github.lengors.scoutdesk.api.scrapers.specifications.services;

import io.github.lengors.protoscout.domain.scrapers.specifications.models.ScraperSpecification;
import io.github.lengors.scoutdesk.api.scrapers.specifications.models.ScraperOwnedSpecificationTestingEntity;
import io.github.lengors.scoutdesk.domain.scrapers.specifications.models.ScraperOwnedSpecificationEntity;
import io.github.lengors.scoutdesk.domain.scrapers.specifications.models.ScraperOwnedSpecificationReference;
import io.github.lengors.scoutdesk.domain.scrapers.specifications.repositories.ScraperOwnedSpecificationRepository;
import io.github.lengors.scoutdesk.integrations.webscout.clients.WebscoutRestClient;

import java.util.function.Supplier;

/**
 * This class is used to create a new instance of the {@link ScraperOwnedSpecificationEntity} class with a
 * {@link ScraperOwnedSpecificationTestingEntity} and a {@link ScraperOwnedSpecificationRepository}.
 * <p>
 * It implements the {@link Supplier} interface to provide a method for creating the entity.
 *
 * @param testingEntity      The {@link ScraperOwnedSpecificationTestingEntity} to be used in the test
 * @param repository         The {@link ScraperOwnedSpecificationRepository} to be used in the test
 * @param webscoutRestClient The {@link WebscoutRestClient} to be used in the test
 * @param specification      The {@link ScraperSpecification} to be used in the test
 */
public record ScraperOwnedSpecificationEntityFactory(
  ScraperOwnedSpecificationTestingEntity testingEntity,
  ScraperOwnedSpecificationRepository repository,
  WebscoutRestClient webscoutRestClient,
  ScraperSpecification specification
) implements Supplier<ScraperOwnedSpecificationEntity> {

  /**
   * Constructor to create a new instance of the factory.
   *
   * @param reference          The {@link ScraperOwnedSpecificationReference} to be used in the test
   * @param repository         The {@link ScraperOwnedSpecificationRepository} to be used in the test
   * @param webscoutRestClient The {@link WebscoutRestClient} to be used in the test
   * @param specification      The {@link ScraperSpecification} to be used in the test
   */
  public ScraperOwnedSpecificationEntityFactory(
    final ScraperOwnedSpecificationReference reference,
    final ScraperOwnedSpecificationRepository repository,
    final WebscoutRestClient webscoutRestClient,
    final ScraperSpecification specification
  ) {
    this(new ScraperOwnedSpecificationTestingEntity(reference, null), repository, webscoutRestClient, specification);
  }

  @Override
  public ScraperOwnedSpecificationEntity get() {
    // Instantiate the entity
    final var entity = repository.save(new ScraperOwnedSpecificationEntity(testingEntity.reference()));
    final var status = testingEntity.status();
    if (status != null) {
      entity.setStatus(status);
    }

    // Save the entity
    webscoutRestClient.save(new ScraperSpecification(
      testingEntity
        .reference()
        .fullyQualifiedName(),
      specification.getSettings(),
      specification.getHandlers()));

    return entity;
  }
}
