package io.github.lengors.scoutdesk.domain.scrapers.models;

import java.util.Set;

/**
 * Represents a query for scraping data.
 *
 * @param owner      The owner of the scraper.
 * @param strategies The set of strategies to be used in the scraping process.
 * @param searchTerm The term to search for during the scraping process.
 *
 * @author lengors
 */
public record ScraperQuery(
    String owner,
    Set<String> strategies,
    String searchTerm) {

}
