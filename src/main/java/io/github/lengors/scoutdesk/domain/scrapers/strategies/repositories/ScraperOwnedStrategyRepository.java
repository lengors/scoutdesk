package io.github.lengors.scoutdesk.domain.scrapers.strategies.repositories;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import io.github.lengors.scoutdesk.domain.scrapers.strategies.models.ScraperOwnedStrategyEntity;
import io.github.lengors.scoutdesk.domain.scrapers.strategies.models.ScraperOwnedStrategyReference;

/**
 * Repository interface for managing scraper-owned strategies.
 *
 * This interface extends the {@link CrudRepository} interface to provide basic
 * CRUD operations for {@link ScraperOwnedStrategyEntity} objects.
 *
 * @author lengors
 */
@Repository
public interface ScraperOwnedStrategyRepository
    extends CrudRepository<ScraperOwnedStrategyEntity, ScraperOwnedStrategyReference> {

  /**
   * Find all scraper-owned strategies by their reference owner and names.
   *
   * @param referenceOwner The owner of the strategies
   * @param referenceNames The names of the strategies
   * @return A list of scraper-owned strategies
   */
  List<ScraperOwnedStrategyEntity> findAllByReferenceOwnerAndReferenceNameIn(
      String referenceOwner,
      Iterable<String> referenceNames);

  /**
   * Find all scraper-owned strategies by their reference owner.
   *
   * @param referenceOwner The owner of the strategies
   * @return A list of scraper-owned strategies
   */
  List<ScraperOwnedStrategyEntity> findAllByReferenceOwner(String referenceOwner);
}
