package io.github.lengors.scoutdesk.domain.scrapers.specifications.commands.models;

import io.github.lengors.scoutdesk.domain.commands.models.Command;
import io.github.lengors.scoutdesk.domain.scrapers.specifications.filters.ScraperOwnedSpecificationFilter;
import io.github.lengors.scoutdesk.domain.scrapers.specifications.models.ScraperOwnedSpecification;

/**
 * Command to find a scraper-owned specification using a filter.
 *
 * This command is used to retrieve a single specification that matches the provided filter criteria.
 *
 * @author lengors
 */
public record FindScraperOwnedSpecificationCommand()
    implements Command<ScraperOwnedSpecificationFilter, ScraperOwnedSpecification> {

}
