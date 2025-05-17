package io.github.lengors.scoutdesk.domain.scrapers.specifications.filters;

import org.checkerframework.checker.nullness.qual.Nullable;

/**
 * Filter for querying batches of scraper specifications by query, owner, case
 * sensitivity, and strict mode.
 *
 * Used to filter {@link ScraperOwnedSpecificationBatchFilter} results based on
 * multiple criteria.
 *
 * @param query             The search query to filter specifications by
 * @param owner             The owner of the specifications to filter by
 * @param ignoreCase        Whether to ignore case when filtering by query
 * @param strictModeEnabled Whether strict mode is enabled for the filter
 *
 * @author lengors
 */
public record ScraperOwnedSpecificationBatchByQueryAndOwnerAndIgnoreCaseAndStrictModeEnabledFilter(
    @Nullable String query,
    @Nullable String owner,
    @Nullable Boolean ignoreCase,
    @Nullable Boolean strictModeEnabled) implements ScraperOwnedSpecificationBatchFilter {

}
