package io.github.lengors.scoutdesk.integrations.webscout.commands.models;

import io.github.lengors.protoscout.domain.scrapers.specifications.models.ScraperSpecification;
import io.github.lengors.scoutdesk.domain.commands.models.Command;

/**
 * Command for retrieving a single scraper specification by name.
 *
 * Used to fetch a specific {@link ScraperSpecification} resource.
 *
 * @author lengors
 */
public record FindScraperSpecificationCommand() implements Command<String, ScraperSpecification> {

}
