package io.github.lengors.scoutdesk.domain.scrapers.strategies.commands.models;

import io.github.lengors.scoutdesk.domain.commands.models.Command;
import io.github.lengors.scoutdesk.domain.scrapers.strategies.models.ScraperOwnedStrategy;

/**
 * Command to save a scraper-owned strategy.
 *
 * This command is used to trigger the saving of a strategy entity.
 *
 * @author lengors
 */
public record SaveScraperOwnedStrategyCommand() implements Command<ScraperOwnedStrategy, ScraperOwnedStrategy> {

}
