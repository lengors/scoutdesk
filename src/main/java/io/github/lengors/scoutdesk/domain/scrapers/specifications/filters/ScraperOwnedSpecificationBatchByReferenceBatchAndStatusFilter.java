package io.github.lengors.scoutdesk.domain.scrapers.specifications.filters;

import io.github.lengors.scoutdesk.domain.scrapers.specifications.models.ScraperOwnedSpecificationReference;
import io.github.lengors.scoutdesk.domain.scrapers.specifications.models.ScraperOwnedSpecificationStatus;

/**
 * Filter for querying batches of scraper specifications by reference batch and
 * status.
 *
 * Used to filter {@link ScraperOwnedSpecificationBatchFilter} results based on
 * multiple criteria.
 *
 * @param references The references of the specifications to filter by
 * @param status     The status of the specifications to filter by
 *
 * @author lengors
 */
public record ScraperOwnedSpecificationBatchByReferenceBatchAndStatusFilter(
    Iterable<ScraperOwnedSpecificationReference> references,
    ScraperOwnedSpecificationStatus status) implements ScraperOwnedSpecificationBatchFilter {

  /**
   * Constructor for creating a filter with a specific reference batch and with
   * status defaulting to {@link ScraperOwnedSpecificationStatus#DELETED}.
   *
   * @param references The references of the specifications to filter by
   */
  public ScraperOwnedSpecificationBatchByReferenceBatchAndStatusFilter(
      final Iterable<ScraperOwnedSpecificationReference> references) {
    this(references, ScraperOwnedSpecificationStatus.DELETED);
  }
}
