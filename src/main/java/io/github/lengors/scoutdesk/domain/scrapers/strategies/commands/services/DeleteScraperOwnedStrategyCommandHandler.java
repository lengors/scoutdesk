package io.github.lengors.scoutdesk.domain.scrapers.strategies.commands.services;

import org.checkerframework.checker.nullness.qual.Nullable;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import io.github.lengors.scoutdesk.domain.commands.services.CommandHandler;
import io.github.lengors.scoutdesk.domain.commands.services.CommandService;
import io.github.lengors.scoutdesk.domain.scrapers.strategies.commands.models.DeleteScraperOwnedStrategyCommand;
import io.github.lengors.scoutdesk.domain.scrapers.strategies.commands.models.FindScraperOwnedStrategyEntityCommand;
import io.github.lengors.scoutdesk.domain.scrapers.strategies.filters.ScraperOwnedStrategyFilter;
import io.github.lengors.scoutdesk.domain.scrapers.strategies.repositories.ScraperOwnedStrategyRepository;

@Service
class DeleteScraperOwnedStrategyCommandHandler
    implements CommandHandler<DeleteScraperOwnedStrategyCommand, ScraperOwnedStrategyFilter, @Nullable Void> {
  private final ScraperOwnedStrategyRepository scraperOwnedStrategyRepository;
  private final CommandService commandService;

  DeleteScraperOwnedStrategyCommandHandler(
      final ScraperOwnedStrategyRepository scraperOwnedStrategyRepository,
      @Lazy final CommandService commandService) {
    this.scraperOwnedStrategyRepository = scraperOwnedStrategyRepository;
    this.commandService = commandService;
  }

  @Override
  @Transactional
  public @Nullable Void handle(
      final DeleteScraperOwnedStrategyCommand command,
      final ScraperOwnedStrategyFilter input) {
    final var entity = commandService.executeCommand(new FindScraperOwnedStrategyEntityCommand(), input);
    scraperOwnedStrategyRepository.delete(entity);
    return null;
  }
}
