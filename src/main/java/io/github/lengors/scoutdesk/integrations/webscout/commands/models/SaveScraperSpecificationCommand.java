package io.github.lengors.scoutdesk.integrations.webscout.commands.models;

import io.github.lengors.protoscout.domain.scrapers.specifications.models.ScraperSpecification;
import io.github.lengors.scoutdesk.domain.commands.models.Command;

/**
 * Command for saving a scraper specification.
 *
 * Used to persist a {@link ScraperSpecification} resource.
 *
 * @author lengors
 */
public record SaveScraperSpecificationCommand() implements Command<ScraperSpecification, ScraperSpecification> {

}
