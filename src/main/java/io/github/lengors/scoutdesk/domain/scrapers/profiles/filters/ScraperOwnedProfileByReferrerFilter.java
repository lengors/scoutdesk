package io.github.lengors.scoutdesk.domain.scrapers.profiles.filters;

import io.github.lengors.scoutdesk.domain.scrapers.profiles.models.ScraperOwnedProfileReferrer;

/**
 * Filter for scraper owned profiles.
 * <p>
 * This filter is used to select profiles based on the referrer.
 *
 * @param referrer The referrer to filter by.
 * @author lengors
 */
public record ScraperOwnedProfileByReferrerFilter(ScraperOwnedProfileReferrer referrer)
  implements ScraperOwnedProfileFilter {

}
