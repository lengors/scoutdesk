package io.github.lengors.scoutdesk.domain.scrapers.specifications.filters;

import io.github.lengors.scoutdesk.domain.scrapers.specifications.models.ScraperOwnedSpecificationReferrer;
import io.github.lengors.scoutdesk.domain.scrapers.specifications.models.ScraperOwnedSpecificationStatus;

import java.util.Collection;

/**
 * Filter for querying batches of scraper specifications by referrer batch and status.
 * <p>
 * Used to filter {@link ScraperOwnedSpecificationBatchFilter} results based on multiple criteria.
 *
 * @param referrers The referrers of the specifications to filter by
 * @param status    The status of the specifications to filter by
 * @author lengors
 */
public record ScraperOwnedSpecificationBatchByReferrerBatchAndStatusFilter(
  Collection<? extends ScraperOwnedSpecificationReferrer> referrers,
  ScraperOwnedSpecificationStatus status
) implements ScraperOwnedSpecificationBatchFilter {

  /**
   * Constructor for creating a filter with a specific referrer batch and with status defaulting to
   * {@link ScraperOwnedSpecificationStatus#DELETED}.
   *
   * @param referrers The referrers of the specifications to filter by
   */
  public ScraperOwnedSpecificationBatchByReferrerBatchAndStatusFilter(
    final Collection<? extends ScraperOwnedSpecificationReferrer> referrers
  ) {
    this(referrers, ScraperOwnedSpecificationStatus.DELETED);
  }
}
