package io.github.lengors.scoutdesk.domain.scrapers.specifications.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import io.github.lengors.scoutdesk.domain.scrapers.specifications.models.ScraperOwnedSpecificationEntity;
import io.github.lengors.scoutdesk.domain.scrapers.specifications.models.ScraperOwnedSpecificationReference;
import io.github.lengors.scoutdesk.domain.scrapers.specifications.models.ScraperOwnedSpecificationStatus;

/**
 * Repository for accessing and managing scraper specification entities owned by
 * users.
 *
 * Extends {@link CrudRepository} to provide CRUD operations and custom queries
 * for {@link ScraperOwnedSpecificationEntity}.
 *
 * @author lengors
 */
@Repository
public interface ScraperOwnedSpecificationRepository
    extends CrudRepository<ScraperOwnedSpecificationEntity, ScraperOwnedSpecificationReference> {

  /**
   * Finds all scraper specifications by their reference owner and status.
   *
   * @param referenceOwner The owner of the specification
   * @param status         The status of the specification
   * @return A list of scraper specifications matching the reference owner and
   *         status
   */
  List<ScraperOwnedSpecificationEntity> findAllByReferenceOwnerAndStatusNot(
      String referenceOwner,
      ScraperOwnedSpecificationStatus status);

  /**
   * Finds all scraper specifications by their reference owner.
   *
   * @param referenceOwner The owner of the specification
   * @return A list of scraper specifications matching the reference owner
   */
  List<ScraperOwnedSpecificationEntity> findAllByReferenceOwner(String referenceOwner);

  /**
   * Finds all scraper specifications by their status.
   *
   * @param status The status of the specification
   * @return A list of scraper specifications matching the status
   */
  List<ScraperOwnedSpecificationEntity> findAllByStatusNot(ScraperOwnedSpecificationStatus status);

  /**
   * Finds a scraper specification by its reference and status.
   *
   * @param reference The reference of the specification
   * @param status    The status of the specification
   * @return An optional scraper specification matching the reference and status
   */
  Optional<ScraperOwnedSpecificationEntity> findByReferenceAndStatusNot(
      ScraperOwnedSpecificationReference reference,
      ScraperOwnedSpecificationStatus status);
}
