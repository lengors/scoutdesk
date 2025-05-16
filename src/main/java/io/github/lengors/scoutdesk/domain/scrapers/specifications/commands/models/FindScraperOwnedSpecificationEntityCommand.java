package io.github.lengors.scoutdesk.domain.scrapers.specifications.commands.models;

import io.github.lengors.scoutdesk.domain.commands.models.Command;
import io.github.lengors.scoutdesk.domain.scrapers.specifications.filters.ScraperOwnedSpecificationFilter;
import io.github.lengors.scoutdesk.domain.scrapers.specifications.models.ScraperOwnedSpecificationEntity;

/**
 * Command to find a single scraper-owned specification entity using a filter.
 *
 * This command is used to retrieve a single specification entity that matches the provided filter criteria.
 *
 * @author lengors
 */
public record FindScraperOwnedSpecificationEntityCommand()
    implements Command<ScraperOwnedSpecificationFilter, ScraperOwnedSpecificationEntity> {

}
