package io.github.lengors.scoutdesk.domain.scrapers.strategies.filters;

/**
 * Sealed interface for scraper owned strategy filters.
 *
 * This interface is used to define a common type for different filter
 * implementations that can be used to filter scraper owned strategies.
 *
 * @author lengors
 */
public sealed interface ScraperOwnedStrategyFilter permits ScraperOwnedStrategyByReferenceFilter {

}
