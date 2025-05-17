package io.github.lengors.scoutdesk.domain.scrapers.profiles.filters;

import io.github.lengors.scoutdesk.domain.scrapers.profiles.models.ScraperOwnedProfileReference;

/**
 * Filter for scraper owned profiles.
 *
 * This filter is used to select profiles based on the reference.
 *
 * @param reference The reference to filter by.
 *
 * @author lengors
 */
public record ScraperOwnedProfileByReferenceFilter(ScraperOwnedProfileReference reference)
    implements ScraperOwnedProfileFilter {

}
