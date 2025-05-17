package io.github.lengors.scoutdesk.domain.scrapers.strategies.filters;

/**
 * Filter for a batch of scraper-owned strategies based on the owner and a list
 * of names.
 *
 * This filter is used to specify the owner of the strategies and a list of
 * names to filter the strategies by.
 *
 * @param owner The owner of the strategies.
 * @param names The list of names to filter the strategies by.
 *
 * @author lengors
 */
public record ScraperOwnedStrategyBatchByReferenceOwnerAndReferenceNameBatchFilter(
    String owner,
    Iterable<String> names) implements ScraperOwnedStrategyBatchFilter {

}
