package io.github.lengors.scoutdesk.domain.scrapers.specifications.commands.models;

import io.github.lengors.scoutdesk.domain.commands.models.Command;
import io.github.lengors.scoutdesk.domain.scrapers.specifications.filters.ScraperOwnedSpecificationFilter;

/**
 * Command to delete a scraper-owned specification using a filter.
 *
 * @author lengors
 */
public record DeleteScraperOwnedSpecificationCommand() implements Command<ScraperOwnedSpecificationFilter, Void> {

}
