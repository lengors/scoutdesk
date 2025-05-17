package io.github.lengors.scoutdesk.domain.scrapers.strategies.filters;

import io.github.lengors.scoutdesk.domain.scrapers.strategies.models.ScraperOwnedStrategyReference;

/**
 * Filter for scraper owned strategies by reference.
 *
 * This filter is used to select strategies based on the reference.
 *
 * @param reference The reference to filter by.
 *
 * @author lengors
 */
public record ScraperOwnedStrategyByReferenceFilter(ScraperOwnedStrategyReference reference)
    implements ScraperOwnedStrategyFilter {

}
