package io.github.lengors.scoutdesk.domain.scrapers.strategies.filters;

/**
 * Filter for scraper owned strategy batches.
 *
 * This filter is used to select strategies based on the reference owner.
 *
 * @param referenceOwner The owner of the reference to filter by.
 *
 * @author lengors
 */
public record ScraperOwnedStrategyBatchByReferenceOwnerFilter(String referenceOwner)
    implements ScraperOwnedStrategyBatchFilter {

}
