package io.github.lengors.scoutdesk.domain.scrapers.specifications.filters;

import io.github.lengors.scoutdesk.domain.scrapers.specifications.models.ScraperOwnedSpecificationReference;
import io.github.lengors.scoutdesk.domain.scrapers.specifications.models.ScraperOwnedSpecificationStatus;

/**
 * Filter for querying batches of scraper specifications by reference and
 * status.
 *
 * Used to filter {@link ScraperOwnedSpecificationBatchFilter} results based on
 * multiple criteria.
 *
 * @param reference The reference of the specifications to filter by
 * @param status    The status of the specifications to filter by
 *
 * @author lengors
 */
public record ScraperOwnedSpecificationBatchByReferenceAndStatusFilter(
    ScraperOwnedSpecificationReference reference,
    ScraperOwnedSpecificationStatus status) implements ScraperOwnedSpecificationBatchFilter {

  /**
   * Constructor for creating a filter with a specific reference and with status
   * defaulting to {@link ScraperOwnedSpecificationStatus#DELETED}.
   *
   * @param reference The reference of the specifications to filter by
   */
  public ScraperOwnedSpecificationBatchByReferenceAndStatusFilter(final ScraperOwnedSpecificationReference reference) {
    this(reference, ScraperOwnedSpecificationStatus.DELETED);
  }
}
