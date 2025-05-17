package io.github.lengors.scoutdesk.domain.scrapers.strategies.commands.models;

import org.checkerframework.checker.nullness.qual.Nullable;

import io.github.lengors.scoutdesk.domain.commands.models.Command;
import io.github.lengors.scoutdesk.domain.scrapers.strategies.filters.ScraperOwnedStrategyBatchFilter;

/**
 * Command to delete a batch of scraper-owned strategies.
 *
 * This command is used to trigger the deletion of multiple strategies that
 * match the specified filter criteria.
 *
 * @author lengors
 */
public record DeleteScraperOwnedStrategyBatchCommand()
    implements Command<ScraperOwnedStrategyBatchFilter, @Nullable Void> {

}
