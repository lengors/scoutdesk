package io.github.lengors.scoutdesk.domain.scrapers.profiles.repositories;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import io.github.lengors.scoutdesk.domain.scrapers.profiles.models.ScraperOwnedProfileEntity;
import io.github.lengors.scoutdesk.domain.scrapers.profiles.models.ScraperOwnedProfileReference;

/**
 * Repository interface for managing scraper-owned profiles.
 *
 * This interface extends the {@link CrudRepository} interface to provide basic
 * CRUD operations for {@link ScraperOwnedProfileEntity} objects.
 *
 * @author lengors
 */
@Repository
public interface ScraperOwnedProfileRepository
    extends CrudRepository<ScraperOwnedProfileEntity, ScraperOwnedProfileReference> {

  /**
   * Finds all scraper-owned profiles by the specified reference owner.
   *
   * @param referenceOwner The reference owner to search for.
   * @return A list of scraper-owned profiles that match the specified reference
   *         owner.
   */
  List<ScraperOwnedProfileEntity> findAllByReferenceOwner(String referenceOwner);
}
