package io.github.lengors.scoutdesk.domain.scrapers.specifications.filters;

import io.github.lengors.scoutdesk.domain.scrapers.specifications.models.ScraperOwnedSpecificationReferrer;
import io.github.lengors.scoutdesk.domain.scrapers.specifications.models.ScraperOwnedSpecificationStatus;

/**
 * Filter for querying batches of scraper specifications by referrer and status.
 * <p>
 * Used to filter {@link ScraperOwnedSpecificationBatchFilter} results based on multiple criteria.
 *
 * @param referrer The referrer of the specifications to filter by
 * @param status   The status of the specifications to filter by
 * @author lengors
 */
public record ScraperOwnedSpecificationByReferrerAndStatusNotFilter(
  ScraperOwnedSpecificationReferrer referrer,
  ScraperOwnedSpecificationStatus status
) implements ScraperOwnedSpecificationFilter {

  /**
   * Constructor for creating a filter with a specific referrer and with status defaulting to
   * {@link ScraperOwnedSpecificationStatus#DELETED}.
   *
   * @param referrer The referrer of the specifications to filter by
   */
  public ScraperOwnedSpecificationByReferrerAndStatusNotFilter(final ScraperOwnedSpecificationReferrer referrer) {
    this(referrer, ScraperOwnedSpecificationStatus.DELETED);
  }
}
