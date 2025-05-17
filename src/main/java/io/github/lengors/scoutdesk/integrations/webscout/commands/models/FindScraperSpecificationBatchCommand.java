package io.github.lengors.scoutdesk.integrations.webscout.commands.models;

import java.util.Collection;
import java.util.Map;

import io.github.lengors.protoscout.domain.scrapers.specifications.models.ScraperSpecification;
import io.github.lengors.scoutdesk.domain.commands.models.Command;

/**
 * Command for retrieving a batch of scraper specifications by their names.
 *
 * Used to fetch multiple {@link ScraperSpecification} resources in a single
 * operation.
 *
 * @author lengors
 */
public record FindScraperSpecificationBatchCommand()
    implements Command<Collection<String>, Map<String, ScraperSpecification>> {

}
