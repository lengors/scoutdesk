package io.github.lengors.scoutdesk.integrations.webscout.commands.models;

import java.util.Collection;
import java.util.List;

import io.github.lengors.protoscout.domain.scrapers.specifications.models.ScraperSpecification;
import io.github.lengors.scoutdesk.domain.commands.models.Command;

/**
 * Command for deleting a batch of scraper specifications by their names.
 *
 * Used to trigger the removal of multiple {@link ScraperSpecification}
 * resources.
 *
 * @author lengors
 */
public record DeleteScraperSpecificationBatchCommand()
    implements Command<Collection<String>, List<ScraperSpecification>> {

}
