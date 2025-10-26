package io.github.lengors.scoutdesk.domain.scrapers.strategies.commands;

import io.github.lengors.scoutdesk.domain.commands.CommandHandler;
import io.github.lengors.scoutdesk.domain.commands.CommandService;
import io.github.lengors.scoutdesk.domain.scrapers.strategies.repositories.ScraperOwnedStrategyRepository;
import org.checkerframework.checker.nullness.qual.Nullable;
import io.github.lengors.scoutdesk.domain.commands.Command;
import io.github.lengors.scoutdesk.domain.scrapers.strategies.filters.ScraperOwnedStrategyFilter;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Command to delete a scraper-owned strategy.
 * <p>
 * This command is used to trigger the deletion of a strategy that matches the specified filter criteria.
 *
 * @author lengors
 */
public record DeleteScraperOwnedStrategyCommand() implements Command<ScraperOwnedStrategyFilter, @Nullable Void> {
  @Service
  static class Handler
    implements CommandHandler<DeleteScraperOwnedStrategyCommand, ScraperOwnedStrategyFilter, @Nullable Void> {
    private final ScraperOwnedStrategyRepository scraperOwnedStrategyRepository;
    private final CommandService commandService;

    Handler(
      final ScraperOwnedStrategyRepository scraperOwnedStrategyRepository,
      final CommandService commandService
    ) {
      this.scraperOwnedStrategyRepository = scraperOwnedStrategyRepository;
      this.commandService = commandService;
    }

    @Override
    @Transactional
    public @Nullable Void handle(
      final DeleteScraperOwnedStrategyCommand command,
      final ScraperOwnedStrategyFilter input
    ) {
      final var entity = commandService.executeCommand(new FindScraperOwnedStrategyEntityCommand(), input);
      scraperOwnedStrategyRepository.delete(entity);
      return null;
    }
  }
}
