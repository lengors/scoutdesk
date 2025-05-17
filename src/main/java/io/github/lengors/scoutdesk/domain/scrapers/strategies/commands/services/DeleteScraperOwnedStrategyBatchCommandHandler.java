package io.github.lengors.scoutdesk.domain.scrapers.strategies.commands.services;

import org.checkerframework.checker.nullness.qual.Nullable;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import io.github.lengors.scoutdesk.domain.commands.services.CommandHandler;
import io.github.lengors.scoutdesk.domain.commands.services.CommandService;
import io.github.lengors.scoutdesk.domain.scrapers.strategies.commands.models.DeleteScraperOwnedStrategyBatchCommand;
import io.github.lengors.scoutdesk.domain.scrapers.strategies.commands.models.FindScraperOwnedStrategyEntityBatchCommand;
import io.github.lengors.scoutdesk.domain.scrapers.strategies.filters.ScraperOwnedStrategyBatchFilter;
import io.github.lengors.scoutdesk.domain.scrapers.strategies.repositories.ScraperOwnedStrategyRepository;

@Service
class DeleteScraperOwnedStrategyBatchCommandHandler
    implements CommandHandler<DeleteScraperOwnedStrategyBatchCommand, ScraperOwnedStrategyBatchFilter, @Nullable Void> {
  private final ScraperOwnedStrategyRepository scraperOwnedStrategyRepository;
  private final CommandService commandService;

  DeleteScraperOwnedStrategyBatchCommandHandler(
      final ScraperOwnedStrategyRepository scraperOwnedStrategyRepository,
      @Lazy final CommandService commandService) {
    this.scraperOwnedStrategyRepository = scraperOwnedStrategyRepository;
    this.commandService = commandService;
  }

  @Override
  @Transactional
  public @Nullable Void handle(
      final DeleteScraperOwnedStrategyBatchCommand command,
      final ScraperOwnedStrategyBatchFilter input) {
    final var entities = commandService.executeCommand(new FindScraperOwnedStrategyEntityBatchCommand(), input);
    scraperOwnedStrategyRepository.deleteAll(entities);
    return null;
  }
}
