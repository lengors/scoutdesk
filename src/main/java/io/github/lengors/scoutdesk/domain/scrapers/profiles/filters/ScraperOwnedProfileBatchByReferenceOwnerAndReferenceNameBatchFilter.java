package io.github.lengors.scoutdesk.domain.scrapers.profiles.filters;

/**
 * Filter for scraper owned profile batches by reference owner and reference
 * name.
 *
 * This filter is used to select profiles based on the reference owner and
 * reference names.
 *
 * @param owner The owner of the reference to filter by.
 * @param names The names of the references to filter by.
 *
 * @author lengors
 */
public record ScraperOwnedProfileBatchByReferenceOwnerAndReferenceNameBatchFilter(
    String owner,
    Iterable<String> names) implements ScraperOwnedProfileBatchFilter {

}
