package io.github.lengors.scoutdesk.domain.scrapers.strategies.commands.models;

import io.github.lengors.scoutdesk.domain.commands.models.Command;
import io.github.lengors.scoutdesk.domain.scrapers.strategies.filters.ScraperOwnedStrategyFilter;
import io.github.lengors.scoutdesk.domain.scrapers.strategies.models.ScraperOwnedStrategy;

/**
 * Command to find a scraper-owned strategy.
 *
 * This command is used to retrieve a single strategy based on the specified
 * filter criteria.
 *
 * @author lengors
 */
public record FindScraperOwnedStrategyCommand() implements Command<ScraperOwnedStrategyFilter, ScraperOwnedStrategy> {

}
