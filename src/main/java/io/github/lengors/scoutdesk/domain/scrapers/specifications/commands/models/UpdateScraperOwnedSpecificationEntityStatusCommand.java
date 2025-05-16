package io.github.lengors.scoutdesk.domain.scrapers.specifications.commands.models;

import io.github.lengors.scoutdesk.domain.commands.models.Command;
import io.github.lengors.scoutdesk.domain.scrapers.specifications.filters.ScraperOwnedSpecificationFilter;
import io.github.lengors.scoutdesk.domain.scrapers.specifications.models.ScraperOwnedSpecificationStatus;

/**
 * Command to update the status of a scraper-owned specification entity.
 *
 * @param status the new status of the specification entity
 *
 * @author lengors
 */
public record UpdateScraperOwnedSpecificationEntityStatusCommand(ScraperOwnedSpecificationStatus status)
    implements Command<ScraperOwnedSpecificationFilter, Void> {

}
