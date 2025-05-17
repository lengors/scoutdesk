package io.github.lengors.scoutdesk.domain.scrapers.strategies.commands.models;

import java.util.List;

import io.github.lengors.scoutdesk.domain.commands.models.Command;
import io.github.lengors.scoutdesk.domain.scrapers.strategies.filters.ScraperOwnedStrategyBatchFilter;
import io.github.lengors.scoutdesk.domain.scrapers.strategies.models.ScraperOwnedStrategy;

/**
 * Command to find a batch of scraper-owned strategies.
 *
 * This command is used to retrieve a list of strategies based on the specified
 * filter criteria.
 *
 * @author lengors
 */
public record FindScraperOwnedStrategyBatchCommand()
    implements Command<ScraperOwnedStrategyBatchFilter, List<ScraperOwnedStrategy>> {

}
