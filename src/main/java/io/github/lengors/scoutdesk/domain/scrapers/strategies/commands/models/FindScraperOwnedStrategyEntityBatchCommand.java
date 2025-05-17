package io.github.lengors.scoutdesk.domain.scrapers.strategies.commands.models;

import java.util.List;

import io.github.lengors.scoutdesk.domain.commands.models.Command;
import io.github.lengors.scoutdesk.domain.scrapers.strategies.filters.ScraperOwnedStrategyBatchFilter;
import io.github.lengors.scoutdesk.domain.scrapers.strategies.models.ScraperOwnedStrategyEntity;

/**
 * Command to find a batch of scraper-owned strategy entities.
 *
 * This command is used to retrieve a list of strategy entities based on the
 * specified filter criteria.
 *
 * @author lengors
 */
public record FindScraperOwnedStrategyEntityBatchCommand()
    implements Command<ScraperOwnedStrategyBatchFilter, List<ScraperOwnedStrategyEntity>> {

}
