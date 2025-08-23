package io.github.lengors.scoutdesk.domain.scrapers.strategies.filters;

import io.github.lengors.scoutdesk.domain.scrapers.strategies.models.ScraperOwnedStrategyReferrer;

/**
 * Filter for scraper owned strategies by referrer.
 * <p>
 * This filter is used to select strategies based on the referrer.
 *
 * @param referrer The referrer to filter by.
 * @author lengors
 */
public record ScraperOwnedStrategyByReferrerFilter(ScraperOwnedStrategyReferrer referrer)
  implements ScraperOwnedStrategyFilter {

}
