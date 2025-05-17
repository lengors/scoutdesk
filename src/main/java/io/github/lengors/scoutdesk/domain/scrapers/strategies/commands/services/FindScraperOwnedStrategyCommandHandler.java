package io.github.lengors.scoutdesk.domain.scrapers.strategies.commands.services;

import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import io.github.lengors.scoutdesk.domain.commands.services.CommandHandler;
import io.github.lengors.scoutdesk.domain.commands.services.CommandService;
import io.github.lengors.scoutdesk.domain.scrapers.strategies.commands.models.FindScraperOwnedStrategyCommand;
import io.github.lengors.scoutdesk.domain.scrapers.strategies.commands.models.FindScraperOwnedStrategyEntityCommand;
import io.github.lengors.scoutdesk.domain.scrapers.strategies.filters.ScraperOwnedStrategyFilter;
import io.github.lengors.scoutdesk.domain.scrapers.strategies.models.ScraperOwnedStrategy;

@Service
class FindScraperOwnedStrategyCommandHandler
    implements CommandHandler<FindScraperOwnedStrategyCommand, ScraperOwnedStrategyFilter, ScraperOwnedStrategy> {
  private final CommandService commandService;

  FindScraperOwnedStrategyCommandHandler(@Lazy final CommandService commandService) {
    this.commandService = commandService;
  }

  @Override
  @Transactional(readOnly = true)
  public ScraperOwnedStrategy handle(
      final FindScraperOwnedStrategyCommand command,
      final ScraperOwnedStrategyFilter input) {
    final var entity = commandService.executeCommand(new FindScraperOwnedStrategyEntityCommand(), input);
    return new ScraperOwnedStrategy(entity);
  }
}
