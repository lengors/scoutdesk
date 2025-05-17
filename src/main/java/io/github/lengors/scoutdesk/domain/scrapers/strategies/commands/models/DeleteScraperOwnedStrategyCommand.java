package io.github.lengors.scoutdesk.domain.scrapers.strategies.commands.models;

import org.checkerframework.checker.nullness.qual.Nullable;

import io.github.lengors.scoutdesk.domain.commands.models.Command;
import io.github.lengors.scoutdesk.domain.scrapers.strategies.filters.ScraperOwnedStrategyFilter;

/**
 * Command to delete a scraper-owned strategy.
 *
 * This command is used to trigger the deletion of a strategy that matches the
 * specified filter criteria.
 *
 * @author lengors
 */
public record DeleteScraperOwnedStrategyCommand() implements Command<ScraperOwnedStrategyFilter, @Nullable Void> {

}
