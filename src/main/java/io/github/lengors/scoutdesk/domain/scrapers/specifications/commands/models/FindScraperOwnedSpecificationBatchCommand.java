package io.github.lengors.scoutdesk.domain.scrapers.specifications.commands.models;

import java.util.List;

import io.github.lengors.scoutdesk.domain.commands.models.Command;
import io.github.lengors.scoutdesk.domain.scrapers.specifications.filters.ScraperOwnedSpecificationBatchFilter;
import io.github.lengors.scoutdesk.domain.scrapers.specifications.models.ScraperOwnedSpecification;

/**
 * Command to find a batch of scraper-owned specifications using a filter.
 *
 * @author lengors
 */
public record FindScraperOwnedSpecificationBatchCommand()
    implements Command<ScraperOwnedSpecificationBatchFilter, List<ScraperOwnedSpecification>> {

}
