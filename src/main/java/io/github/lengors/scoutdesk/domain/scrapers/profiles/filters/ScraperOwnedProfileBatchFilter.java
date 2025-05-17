package io.github.lengors.scoutdesk.domain.scrapers.profiles.filters;

/**
 * Sealed interface for batch filters on scraper-owned profiles.
 *
 * Permits various filter implementations for querying and filtering profile
 * batches.
 *
 * This interface is used to define a common type for different filter
 * implementations that can be used to filter batches of scraper-owned profiles.
 *
 * @author lengors
 */
public sealed interface ScraperOwnedProfileBatchFilter
    permits ScraperOwnedProfileBatchByReferenceOwnerAndReferenceNameBatchFilter,
    ScraperOwnedProfileBatchByReferenceOwnerFilter {

}
