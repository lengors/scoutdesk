package io.github.lengors.scoutdesk.domain.scrapers.specifications.filters;

/**
 * Sealed interface for filters on scraper-owned specifications.
 * <p>
 * Permits various filter implementations for querying and filtering
 * specification results.
 * <p>
 * This interface is used to define a common type for different filter
 * implementations that can be used to filter scraper-owned specifications.
 *
 * @author lengors
 */
public sealed interface ScraperOwnedSpecificationFilter permits ScraperOwnedSpecificationByReferenceAndStatusNotFilter {

}
