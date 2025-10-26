package io.github.lengors.scoutdesk.domain.scrapers.strategies.commands;

import io.github.lengors.scoutdesk.domain.commands.CommandHandler;
import io.github.lengors.scoutdesk.domain.commands.CommandService;
import io.github.lengors.scoutdesk.domain.scrapers.strategies.repositories.ScraperOwnedStrategyRepository;
import org.checkerframework.checker.nullness.qual.Nullable;
import io.github.lengors.scoutdesk.domain.commands.Command;
import io.github.lengors.scoutdesk.domain.scrapers.strategies.filters.ScraperOwnedStrategyBatchFilter;

import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Command to delete a batch of scraper-owned strategies.
 * <p>
 * This command is used to trigger the deletion of multiple strategies that match the specified filter criteria.
 *
 * @author lengors
 */
public record DeleteScraperOwnedStrategyBatchCommand()
  implements Command<ScraperOwnedStrategyBatchFilter, @Nullable Void> {

  @Service
  static class Handler
    implements CommandHandler<DeleteScraperOwnedStrategyBatchCommand, ScraperOwnedStrategyBatchFilter, @Nullable Void> {
    private final ScraperOwnedStrategyRepository scraperOwnedStrategyRepository;
    private final CommandService commandService;

    Handler(
      final ScraperOwnedStrategyRepository scraperOwnedStrategyRepository,
      @Lazy final CommandService commandService
    ) {
      this.scraperOwnedStrategyRepository = scraperOwnedStrategyRepository;
      this.commandService = commandService;
    }

    @Override
    @Transactional
    public @Nullable Void handle(
      final DeleteScraperOwnedStrategyBatchCommand command,
      final ScraperOwnedStrategyBatchFilter input
    ) {
      final var entities = commandService.executeCommand(new FindScraperOwnedStrategyEntityBatchCommand(), input);
      scraperOwnedStrategyRepository.deleteAll(entities);
      return null;
    }
  }
}
