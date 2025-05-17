package io.github.lengors.scoutdesk.domain.scrapers.specifications.filters;

import io.github.lengors.scoutdesk.domain.scrapers.specifications.models.ScraperOwnedSpecificationStatus;

/**
 * Filter for querying batches of scraper specifications by reference owner and
 * status.
 *
 * Used to filter {@link ScraperOwnedSpecificationBatchFilter} results based on
 * multiple criteria.
 *
 * @param referenceOwner The owner of the specifications to filter by
 * @param status         The status of the specifications to filter by
 *
 * @author lengors
 */
public record ScraperOwnedSpecificationBatchByReferenceOwnerAndStatusNotFilter(
    String referenceOwner,
    ScraperOwnedSpecificationStatus status) implements ScraperOwnedSpecificationBatchFilter {

  /**
   * Constructor for creating a filter with a specific reference owner and with
   * status defaulting to {@link ScraperOwnedSpecificationStatus#DELETED}.
   *
   * @param referenceOwner The owner of the specifications to filter by
   */
  public ScraperOwnedSpecificationBatchByReferenceOwnerAndStatusNotFilter(final String referenceOwner) {
    this(referenceOwner, ScraperOwnedSpecificationStatus.DELETED);
  }
}
