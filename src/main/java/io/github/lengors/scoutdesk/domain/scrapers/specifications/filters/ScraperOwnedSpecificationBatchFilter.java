package io.github.lengors.scoutdesk.domain.scrapers.specifications.filters;

/**
 * Sealed interface for batch filters on scraper-owned specifications.
 *
 * Permits various filter implementations for querying and filtering
 * specification batches.
 *
 * This interface is used to define a common type for different filter
 * implementations that can be used to filter batches of scraper-owned
 * specifications.
 *
 * @author lengors
 */
public sealed interface ScraperOwnedSpecificationBatchFilter
    permits ScraperOwnedSpecificationBatchByQueryAndOwnerAndIgnoreCaseAndStrictModeEnabledFilter,
    ScraperOwnedSpecificationBatchByReferenceOwnerAndStatusNotFilter,
    ScraperOwnedSpecificationBatchByReferenceBatchAndStatusFilter,
    ScraperOwnedSpecificationBatchByReferenceAndStatusFilter {

}
