package io.github.lengors.scoutdesk.domain.scrapers.strategies.filters;

/**
 * Sealed interface for scraper owned strategy batch filters.
 *
 * This interface is used to define a common type for different filter
 * implementations that can be used to filter batches of scraper owned
 * strategies.
 *
 * @author lengors
 */
public sealed interface ScraperOwnedStrategyBatchFilter
    permits ScraperOwnedStrategyBatchByReferenceOwnerAndReferenceNameBatchFilter,
    ScraperOwnedStrategyBatchByReferenceOwnerFilter {

}
