package io.github.lengors.scoutdesk.integrations.webscout.commands.models;

import io.github.lengors.protoscout.domain.scrapers.specifications.models.ScraperSpecification;
import io.github.lengors.scoutdesk.domain.commands.models.Command;

/**
 * Command for deleting a single scraper specification by name.
 *
 * Used to trigger the removal of a specific {@link ScraperSpecification}
 * resource.
 *
 * @author lengors
 */
public record DeleteScraperSpecificationCommand() implements Command<String, ScraperSpecification> {

}
