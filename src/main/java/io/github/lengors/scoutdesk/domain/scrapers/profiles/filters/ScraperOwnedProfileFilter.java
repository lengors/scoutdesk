package io.github.lengors.scoutdesk.domain.scrapers.profiles.filters;

/**
 * Sealed interface for filters on scraper-owned profiles.
 *
 * Permits various filter implementations for querying and filtering profiles.
 *
 * This interface is used to define a common type for different filter
 * implementations that can be used to filter scraper-owned profiles.
 *
 * @author lengors
 */
public sealed interface ScraperOwnedProfileFilter permits ScraperOwnedProfileByReferenceFilter {

}
