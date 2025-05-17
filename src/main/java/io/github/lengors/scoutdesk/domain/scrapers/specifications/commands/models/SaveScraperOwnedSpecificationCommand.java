package io.github.lengors.scoutdesk.domain.scrapers.specifications.commands.models;

import io.github.lengors.protoscout.domain.scrapers.specifications.models.ScraperSpecification;
import io.github.lengors.scoutdesk.domain.commands.models.Command;
import io.github.lengors.scoutdesk.domain.scrapers.specifications.models.ScraperOwnedSpecification;

/**
 * Command to save a scraper-owned specification.
 *
 * @param owner the owner of the specification
 *
 * @author lengors
 */
public record SaveScraperOwnedSpecificationCommand(String owner)
    implements Command<ScraperSpecification, ScraperOwnedSpecification> {

}
