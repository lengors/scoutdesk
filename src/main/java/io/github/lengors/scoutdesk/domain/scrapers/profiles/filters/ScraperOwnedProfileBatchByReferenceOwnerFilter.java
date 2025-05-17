package io.github.lengors.scoutdesk.domain.scrapers.profiles.filters;

/**
 * Filter for scraper owned profile batches.
 *
 * This filter is used to select profiles based on the reference owner.
 *
 * @param referenceOwner The owner of the reference to filter by.
 *
 * @author lengors
 */
public record ScraperOwnedProfileBatchByReferenceOwnerFilter(String referenceOwner)
    implements ScraperOwnedProfileBatchFilter {

}
